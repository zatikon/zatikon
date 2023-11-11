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

public class DatabaseManager {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String DB_DRIVER = "org.sqlite.JDBC";

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
            PreparedStatement preparedStatement = connection.prepareStatement("""
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
                      PRIMARY KEY  (`username`)
                    )""");


            preparedStatement.execute();

            connection.createStatement().execute("create table if not exists `referrals` (email varchar, username varchar, registered int, primary key (username))");

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    /////////////////////////////////////////////////////////////////
    // save a player
    /////////////////////////////////////////////////////////////////
    public void update(String username, String password, int rating, byte[] buf, String email) throws Exception {
        try {
            // open a connection
            //Connection connection = initialize();

            byte[] salt = PasswordHasher.salt();
            String hashedPassword = PasswordHasher.hashPassword(password, salt);

            PreparedStatement ps = connection.prepareStatement("UPDATE players SET `rating` = ?, `data` = ?, `email` = ?, `password` = ?, `salt` = ? WHERE `username` = ?");
            ps.setInt(1, rating);
            ps.setObject(2, buf);
            ps.setString(3, email);
            ps.setString(4, hashedPassword);
            ps.setBytes(5, salt);
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
    public void insert(String username, String password, int rating, byte[] buf, String email) throws Exception { //if (!validEmail(email)) return;
        try {
            //ByteArrayInputStream bais = new ByteArrayInputStream(buf);

            // open a connection
            //Connection connection = initialize();
            byte[] salt = PasswordHasher.salt();
            String hashedPassword = PasswordHasher.hashPassword(password, salt);

            // TODO replace datetime with Java code
            PreparedStatement ps = connection.prepareStatement("INSERT INTO players VALUES (?, ?, ?, ?, ?, NULL, ?, NULL, NULL, datetime())");
            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setBytes(3, salt);
            ps.setInt(4, rating);
            //ps.setBinaryStream (5, bais, buf.length);
            ps.setObject(5, buf);
            ps.setString(6, email);
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
            String q = "SELECT `username`, `rating` FROM `players` ORDER BY `rating` DESC LIMIT 0,20";
            statement.execute(q);
            ResultSet rs = statement.getResultSet();

            scores.append("<center><b>Top 20 Players</b></center>");
            scores.append("<ol>");
            while (rs.next()) {
                scores.append("<li>");
                scores.append(rs.getString("username"));
                //scores = scores + ": " + rs.getString("rating");
                scores.append("</li>");
            }
            scores.append("</ol>");

            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.topScores");
            throw e;
        }
        return scores.toString();
    }


    ////////////////////////////////
    // Scoredump (Ratings transition)
    ////////////////////////////////
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
                tempPlayer = new Player(temp);
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
    }

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
    public byte[] getPlayer(String name) throws Exception {
        byte[] buf = null;
        try { // open a connection
            //Connection connection = initialize();

            PreparedStatement ps = connection.prepareStatement("SELECT `data` FROM `players` WHERE `username` = ?");
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
            }
            rs.close();
            ps.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getPlayer");
            throw e;
        }
        return buf;
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
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public int getRank(int rating) {
        int rank = 0;
        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            //statement.execute("select count(*) + 1 as rank from players where rating > " + rating + " and keycode is not null");
            statement.execute("select count(*) + 1 as rank from players where rating > " + rating);
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
