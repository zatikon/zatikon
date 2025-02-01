///////////////////////////////////////////////////////////////////////
//      Name:   DatabaseManager
//      Desc:   Manage the database connection
//      Date:   8/8/2008
//      Notes: uses ping() in ServerJanitor.java once a minute
//              This maintains the connection that would otherwise close after
//              5 minutes
//             Only one connection is open at a time now
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Constants;
import leo.shared.Log;
import org.tinylog.Logger;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class DatabaseManager {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String DB_DRIVER = "org.sqlite.JDBC";
    public static final String DB_VERSION = "1.1.6";
    private static final String DB_PATH = Optional
            .ofNullable(System.getProperty("server.db.path"))
            .orElse(Constants.LOCAL_DIR + File.separator + "zatikon.db");
    private static final String DB_ADDRESS = "jdbc:sqlite://" + Path.of(DB_PATH).toAbsolutePath();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Connection connection;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public DatabaseManager() {
        try {
            connection = initialize();
            prepare();
        } catch (Exception e) {
            Log.error("DatabaseManager" + e);
        }
    }

    private void prepare() {
        try {
            boolean tableExists = connection.getMetaData().getTables(null, null, "players", null).next();
            connection.createStatement().execute("""
                    CREATE TABLE if not exists `players` (
                      `username` varchar(20) NOT NULL default '',
                      `password` varchar(64) NOT NULL default '',
                      `salt` blob NOT NULL default '',
                      `rating` int(11) NOT NULL default '0',
                      `data` blob NOT NULL,
                      `keycode` varchar(64) default NULL,
                      `email` varchar NOT NULL,
                      `keycodeLegions` varchar(64) default NULL,
                      `keycodeInquisition` varchar(64) default NULL,
                      `joined` varchar,
                      `jsonData` json NOT NULL default '{}',
                      `admin` int default 0,
                      PRIMARY KEY  (`username`)
                    )""");

            connection.createStatement().execute("create table if not exists `referrals` (email varchar, username varchar, registered int, primary key (username))");
            connection.createStatement().execute("create table if not exists `metadata` (metakey varchar, metavalue varchar, primary key (metakey))");

            //if the player table did not exist then it is created at the latest version so set that in the metadata
            if(!tableExists) {
                try {
                    setDbVersion(DB_VERSION);
                } catch (Exception e) {
                    Logger.error("Failed to fetch DB version", e);
                }                
            }

            try {
                String version = getDbVersion();
                Logger.info("db version: " + version);
                if(!DB_VERSION.equals(version)) {
                    migrateDb(version);
                }
            } catch (Exception e) {
                Logger.error("Failed to fetch DB version", e);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    ////////////////////////////////
    // Migrate the DB to a new version
    ////////////////////////////////
    
    public void migrateDb(String currentVersion) {
        try {
            Logger.info("migrating db from " + currentVersion +" to " + DB_VERSION);

            if(currentVersion.equals("")) { //if no version is available it means the db is probably 1.1.5
                Logger.info("no version is available it means the db is probably 1.1.5");

                // Add jsonData column
                try {
                    connection.createStatement().execute("ALTER TABLE `players` ADD COLUMN `jsonData` TEXT NOT NULL DEFAULT '{}';");
                } catch (Exception e) {
                    Logger.error("DatabaseManager.migrateDb" + e);
                }
                // Add admin column
                 try {
                    connection.createStatement().execute("ALTER TABLE `players` ADD COLUMN `admin` INTEGER DEFAULT 0;");
                } catch (Exception e) {
                    Logger.error("DatabaseManager.migrateDb" + e);
                }                
                //update the player data
                movePlayerDataToJson();
                setDbVersion(DB_VERSION);
            }
        } catch (Exception e) {
            Logger.error("DatabaseManager.migrateDb" + e);
        }        
    }

    ////////////////////////////////
    // movePlayerDataToJson Update all the players in the DB by loading and saving them (DB transition)
    ////////////////////////////////
    
    public void movePlayerDataToJson() {
        Player tempPlayer;

        try {
            Logger.info("loading all players");
            var list = getPlayerList();
            //Logger.info("players: " + list);
            for (var player: list) {
                try {
                    tempPlayer = new Player(this, player, true);
                    tempPlayer.save();
                    Logger.info("Processed player: " + player);
                } catch (Exception e) {
                    Logger.error("DatabaseManager error loading: " + player + " - " + e);
                } 
            }
        } catch (Exception e) {
            Logger.error("DatabaseManager.updateDatabase" + e);
        }        
    }

    /////////////////////////////////////////////////////////////////
    // Set/Update the db version
    /////////////////////////////////////////////////////////////////
    public void setDbVersion(String newVersion) throws Exception {
        try {
            var ps = connection.prepareStatement("INSERT INTO `metadata` (metakey, metavalue) VALUES (?, ?) ON CONFLICT(metakey) DO UPDATE SET `metavalue` = ?");
            ps.setString(1, "version");
            ps.setString(2, newVersion);
            ps.setString(3, newVersion);
            ps.execute();
            ps.close();

        } catch (Exception e) {
            Log.error("DatabaseManager.dbVersion");
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Get the db version
    /////////////////////////////////////////////////////////////////
    public String getDbVersion() throws Exception {
        String result = "";
        try {
            var statement = connection.prepareStatement("SELECT `metavalue` FROM `metadata` WHERE `metakey` = ?");
            statement.setString(1, "version");
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                result = rs.getString("metavalue");
            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getDbVersion");
            throw e;
        }
        return result;
    }

    /////////////////////////////////////////////////////////////////
    // save a player
    /////////////////////////////////////////////////////////////////
    public void update(String username, String hashedPassword, byte[] salt, int rating, byte[] buf, String email, String jsonData) throws Exception {
        try {
            //Logger.info("saving json: " + jsonData);
            // open a connection
            //Connection connection = initialize();

            PreparedStatement ps = connection.prepareStatement("UPDATE players SET `rating` = ?, `email` = ?, `password` = ?, `salt` = ?, `jsonData` = ? WHERE `username` = ?");
            ps.setInt(1, rating);
            //ps.setObject(2, buf);
            ps.setString(2, email);
            ps.setString(3, hashedPassword);
            ps.setBytes(4, salt);
            ps.setString(5, jsonData);
            ps.setString(6, username);
            ps.execute();
            ps.close();

            //connection.close();

        } catch (Exception e) {
            Log.error("DatabaseManager.update");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // insert a new player
    /////////////////////////////////////////////////////////////////
    public boolean insertReferral(String username, String email) { //if (!validEmail(email)) return false;
        try {
            // open a connection
            //Connection connection = initialize();

            PreparedStatement ps = connection.prepareStatement("INSERT INTO referrals VALUES (?, ?, 0)");
            ps.setString(1, username);
            ps.setString(2, email);
            ps.execute();
            ps.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.insertReferral " + e);
            return false;
        }
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // insert a new player
    /////////////////////////////////////////////////////////////////
    public void insert(String username, String hashedPassword, byte[] salt, int rating, byte[] buf, String email, String jsonData) throws Exception { //if (!validEmail(email)) return;
        try {
            //ByteArrayInputStream bais = new ByteArrayInputStream(buf);

            // open a connection
            //Connection connection = initialize();

            // TODO replace datetime with Java code
            PreparedStatement ps = connection.prepareStatement("INSERT INTO players VALUES (?, ?, ?, ?, ?, NULL, ?, NULL, NULL, datetime(), ?, ?)");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setBytes(3, salt);
            ps.setInt(4, rating);
            //ps.setBinaryStream (5, bais, buf.length);
            ps.setObject(5, buf);
            ps.setString(6, email);
            ps.setString(7, jsonData);
            ps.setInt(8, 0);
            ps.execute();
            ps.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.insert");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public boolean playerExists(String name) throws Exception {
        boolean result;
        try {
            // open a connection
            //Connection connection = initialize();

            var statement = connection.prepareStatement("SELECT `username` FROM `players` WHERE `username` = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            result = rs.next();

            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.playerExists");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Get the top scores
    /////////////////////////////////////////////////////////////////
    public String topScores() throws Exception {
        StringBuilder scores = new StringBuilder();

        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            //String q = "SELECT username,rating FROM players WHERE keycode IS NOT NULL ORDER BY RATING DESC LIMIT 0,20";
            //String q = "SELECT `username`, `rating` FROM `players` WHERE `rating` != 1000 ORDER BY `rating` DESC LIMIT 0,20";
            String q = "SELECT `username`, `rating` FROM `players` WHERE json_extract(`jsonData`, '$.gamesPlayed') != 0 ORDER BY `rating` DESC LIMIT 20";
            statement.execute(q);
            ResultSet rs = statement.getResultSet();

            // Store the Top 20 Players in a list
            ArrayList<String> topPlayers = new ArrayList<>();
            while (rs.next()) {
                topPlayers.add(rs.getString("username") + ": " + rs.getString("rating"));
            }
            rs.close();

            q = "SELECT `username`, (json_extract(jsonData, '$.computerWins') - json_extract(jsonData, '$.computerLosses') / 2) AS rating FROM `players` ORDER BY rating DESC LIMIT 20";
            statement.execute(q);
            rs = statement.getResultSet();

            // Store the AI Top 20 Players in a list
            ArrayList<String> aiPlayers = new ArrayList<>();
            while (rs.next()) {
                if(rs.getString("rating") != null) {
                    aiPlayers.add(rs.getString("username") + ": " + (Integer.parseInt(rs.getString("rating")) + 1));
                }
            }
            rs.close();

            //scores.append("<table style='width: 100%;'>"); // border-spacing: 10px;
            scores.append("<table>"); //font-size: 12px;
            scores.append("<tr><td>PvP Top 20 Players</td><td>AI Top 20 Players</td></tr>");
            // Ensure both lists have the same number of rows
            int maxRows = Math.max(topPlayers.size(), aiPlayers.size());
            for (int i = 0; i < maxRows; i++) {
                scores.append("<tr>");
                // Add Top 20 Player
                scores.append("<td>");
                if (i < topPlayers.size()) {
                    scores.append((i + 1) + ". " + topPlayers.get(i));
                }
                scores.append("</td>");
                // Add AI Top 20 Player
                scores.append("<td>");
                if (i < aiPlayers.size()) {
                    scores.append((i + 1) + ". " + aiPlayers.get(i));
                }
                scores.append("</td>");
                scores.append("</tr>");
            }

            scores.append("</table>");


            //connection.close();
        } catch (Exception e) {
            Logger.error("DatabaseManager.topScores");
            throw e;
        }
        return scores.toString();
    }

    ////////////////////////////////
    // Scoredump (Ratings transition)
    ////////////////////////////////
    /*
    public void getScoreDump() {
        try {
            FileWriter fstream = new FileWriter("StatDump.txt");
            PrintWriter out = new PrintWriter(fstream);
            String temp; // = "USERNAME: RATING, Wins, Wins to Lower, Losses to Higher, Losses\n\n";
            //out.println(temp);
            Player tempPlayer;
            Statement statement = connection.createStatement();
            String q = "SELECT username,rating FROM `players` ORDER BY `rating` DESC LIMIT 0,500";
            statement.execute(q);
            ResultSet rs = statement.getResultSet();
            while (rs.next()) {
                temp = rs.getString("username");
                tempPlayer = new Player(this, temp);
                temp += ": " + rs.getString("rating");
                temp += ", WinsVsHigher: " + tempPlayer.getWins();
                temp += ", WinsVsLower: " + tempPlayer.getWinsToLower();
                temp += ", LossesVsHigher: " + tempPlayer.getLossesToHigher();
                temp += ", LossesVsLower: " + tempPlayer.getLosses();
                temp += "\n";
                out.println(temp);
                System.out.println(temp);
            }
            rs.close();

        } catch (Exception e) {
            Log.error("DatabaseManager.getScoreDump");
            //throw e;
        }
    }*/


    public int getRating(String userName) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT `rating` FROM `players` WHERE `username` = ?");
            statement.setString(1, userName);
            statement.execute();
            ResultSet rs = statement.getResultSet();
            String temp = "";
            while (rs.next()) {
                temp = rs.getString("rating");
                //System.out.println("Loading player "+userName+" with rating "+temp);
            }
            return Integer.parseInt(temp);
        } catch (Exception e) {
            Logger.error(e);
            return -1;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public Map<String, Object> getPlayer(String name) throws Exception {
        Map<String, Object> result = new HashMap<>(); // Declaration of result map
        byte[] buf = null;
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement ps = connection.prepareStatement("SELECT `data`, `jsonData` FROM `players` WHERE `username` = ?");
            ps.setString(1, name);
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                byte[] data = rs.getBytes("data");
                InputStream is = new ByteArrayInputStream(data);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int i;
                while ((i = is.read()) != -1) {
                    baos.write(i);
                }
                baos.close();
                is.close();
                buf = baos.toByteArray();
                result.put("data", buf);  // backward compatibility

                // Retrieve JSON data
                String jsonData = rs.getString("jsonData");
                result.put("jsonData", jsonData);                
                //Logger.info("load player " + name + ": " + jsonData);
            }
            rs.close();
            ps.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getPlayer");
            throw e;
        }
        return result;
    }

    public List<String> getPlayerList() throws Exception {
        var result = new LinkedList<String>();
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement ps = connection.prepareStatement("SELECT `username` FROM `players`");
            ps.execute();

            ResultSet rs = ps.getResultSet();

            while (rs.next()) {
                String data = rs.getString("username");
                result.add(data);
            }
            rs.close();
            ps.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getPlayerList");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Check for player
    /////////////////////////////////////////////////////////////////
    public boolean checkPlayer(String name) {
        boolean result;

        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement statement = connection.prepareStatement("SELECT username FROM players WHERE username = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            result = rs.next();
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.checkPlayer " + name);
            return false;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public String getPasswordHashed(String name) throws Exception {
        String password = "";

        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement statement = connection.prepareStatement("SELECT password FROM players WHERE username = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                password = rs.getString("password");

            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getPassword");
            throw e;
        }
        return password;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public String getChatName(String name) throws Exception {
        String chatName = name;
        long uid;
        try {
            uid = Long.parseLong(name);
        } catch (Exception n) {
            return name;
        }

        try { // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT name FROM login WHERE userid = " + uid);
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                chatName = rs.getString("name");

            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getChatName");
            throw e;
        }
        return chatName;
    }


    /////////////////////////////////////////////////////////////////
    // Get referrals
    /////////////////////////////////////////////////////////////////
    public int getReferrals(String name) throws Exception {
        int count = 0;
        try {
            // open a connection
            //Connection connection = initialize();

            // count
            PreparedStatement statement = connection.prepareStatement("SELECT email FROM referrals WHERE username = ? AND registered = 1");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                count++;
            }
            rs.close();

            // delete
            if (count > 0) {
                PreparedStatement st = connection.prepareStatement("DELETE FROM referrals WHERE username = ? AND registered = 1");
                st.execute();
            }

            //connection.close();

        } catch (Exception e) {
            Log.error("DatabaseManager.getReferrals");
            throw e;
        }
        return count;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public String getEmail(String name) throws Exception {
        String email = "";
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement statement = connection.prepareStatement("SELECT email FROM players WHERE username = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                email = rs.getString("email");
            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getEmail");
            throw e;
        }
        return email;
    }

    /////////////////////////////////////////////////////////////////
    // Get the player admin data
    /////////////////////////////////////////////////////////////////
    public boolean getAdmin(String name) throws Exception {
        boolean admin = false;
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement statement = connection.prepareStatement("SELECT admin FROM players WHERE username = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                admin = (rs.getInt("admin") == 1);
            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getEmail");
            throw e;
        }
        return admin;
    }

    public byte[] getSalt(String name) throws Exception {
        byte[] salt = null;
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement statement = connection.prepareStatement("SELECT salt FROM players WHERE username = ?");
            statement.setString(1, name);
            statement.execute();
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                salt = rs.getBytes("salt");
            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getSalt");
            throw e;
        }
        return salt;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public int getRank(int rating) {
        int rank = 0;
        //if (rating == 1000) {
        //    return 0;
        //}
        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            //statement.execute("select count(*) + 1 as rank from players where rating > " + rating + " and keycode is not null");
            //statement.execute("SELECT count(*) + 1 as rank FROM players WHERE rating > " + rating + " AND rating != 1000");
            statement.execute("SELECT count(*) + 1 as rank FROM players WHERE rating > " + rating + " AND json_extract(jsonData, '$.gamesPlayed') != 0");

            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                rank = rs.getInt("rank");
            }
            rs.close();
            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getRank " + e);
        }
        return rank;
    }

    /////////////////////////////////////////////////////////////////
    // Initialize the connection
    /////////////////////////////////////////////////////////////////
    public Connection initialize() throws Exception {
        try {
            Class.forName(DB_DRIVER);
            Log.system("Connecting to DB at " + DB_ADDRESS);
            Connection conn = DriverManager.getConnection(DB_ADDRESS);
            conn.setCatalog("zatikon");
            return conn;

        } catch (Exception e) {
            Log.error("DatabaseManager.initialize " + e);
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Maintain connection between server and database
    /////////////////////////////////////////////////////////////////
    public void ping() {
        //Log.activity(" ** ping **");
        try {
            Statement statement = connection.createStatement();
            // Could be changed, just needs to be a query so that the connection doesn't fail
            String q = "SELECT username FROM players WHERE username = 'test'";
            statement.execute(q);
        } catch (Exception e) {
            Log.error("DatabaseManager.ping " + e);
            try {
                connection = initialize();
                ping();
            } catch (Exception ee) {
                Log.error("reinitialize failure: " + ee);
            }
        }
    }

}
