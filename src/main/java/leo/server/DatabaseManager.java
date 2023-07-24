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
import leo.shared.Unit;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class DatabaseManager {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String WEBSCRIPT = "http://www.chroniclogic.com/cgi-bin/zat_noncl_reg.pl?keycode=";
    private static final String NONCL = "FFFF";
    private static final String DB_DRIVER = "org.sqlite.JDBC";

    private static final String DB_PATH = Optional
            .ofNullable(System.getProperty("server.db.path"))
            .orElse(Constants.LOCAL_DIR + File.separator + "zatikon.db");
    private static final String DB_ADDRESS = "jdbc:sqlite://" + Path.of(DB_PATH).toAbsolutePath();

    /////////////////////////////////////////////////////////////////
    // Pubcons
    /////////////////////////////////////////////////////////////////
    public static final int NONE = -1;
    public static final int CRUSADES = 9;
    public static final int LEGIONS = 10;
    public static final int INQUISITION = 11;


    /////////////////////////////////////////////////////////////////
    // Get the keycode field
    /////////////////////////////////////////////////////////////////
    private static String getKeycodeColumn(int gameId) {
        switch (gameId) {
            case CRUSADES:
                return "keycode";

            case LEGIONS:
                return "keycodeLegions";

            case INQUISITION:
                return "keycodeInquisition";

        }
        return "error";
    }


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
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE if not exists `players` (\n" +
                                                                              "  `username` varchar(20) NOT NULL default '',\n" +
                                                                              "  `password` varchar(64) NOT NULL default '',\n" +
                                                                              "  `salt` blob NOT NULL default '',\n" +
                                                                              "  `rating` int(11) NOT NULL default '0',\n" +
                                                                              "  `data` blob NOT NULL,\n" +
                                                                              "  `keycode` varchar(64) default NULL,\n" +
                                                                              "  `email` varchar NOT NULL,\n" +
                                                                              "  `keycodeLegions` varchar(64) default NULL,\n" +
                                                                              "  `keycodeInquisition` varchar(64) default NULL,\n" +
                                                                              "  `joined` varchar,\n" +
                                                                              "  PRIMARY KEY  (`username`)\n" +
                                                                              ")");


            preparedStatement.execute();

//            preparedStatement = connection.prepareStatement("create table if not exists `referrals` (email varchar, username varchar, registered int, primary key (username))");

            connection.createStatement().execute("create table if not exists `referrals` (email varchar, username varchar, registered int, primary key (username))");

//            preparedStatement.execute();

        } catch (SQLException e) {
            e.printStackTrace();
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
            Log.error("DatabaseManager.insertRefferal " + e);
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
    public boolean refund(String keycode) throws Exception {
        boolean result = false;
        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT keycode FROM refunds WHERE keycode = '" + keycode + "'");
            ResultSet rs = statement.getResultSet();

            result = rs.next();

            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.refund");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public boolean playerExists(String name) throws Exception {
        boolean result = false;
        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT `username` FROM `players` WHERE `username` = '" + name + "'");
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
        String scores = "";

        try {
            // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            //String q = "SELECT username,rating FROM players WHERE keycode IS NOT NULL ORDER BY RATING DESC LIMIT 0,20";
            String q = "SELECT `username`, `rating` FROM `players` ORDER BY `rating` DESC LIMIT 0,20";
            statement.execute(q);
            ResultSet rs = statement.getResultSet();

            scores = scores + "<center><b>Top 20 Players</b></center>";
            scores = scores + "<ol>";
            while (rs.next()) {
                scores = scores + "<li>";
                scores = scores + rs.getString("username");
                //scores = scores + ": " + rs.getString("rating");
                scores = scores + "</li>";
            }
            scores = scores + "</ol>";

            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.topScores");
            throw e;
        }
        return scores;
    }


    ////////////////////////////////
    // Scoredump (Ratings transition)
    ////////////////////////////////
    public void getScoreDump() {
        try {
            FileWriter fstream = new FileWriter("StatDump.txt");
            PrintWriter out = new PrintWriter(fstream);
            String temp = "USERNAME: RATING, Wins, Wins to Lower, Losses to Higher, Losses\n\n";
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
            Statement statement = connection.createStatement();
            String q = "SELECT `rating` FROM `players` WHERE `username` = '" + userName + "'";
            statement.execute(q);
            ResultSet rs = statement.getResultSet();
            String temp = "";
            while (rs.next()) {
                temp = rs.getString("rating");
                //System.out.println("Loading player "+userName+" with rating "+temp);
            }
            return Integer.parseInt(temp);
        } catch (Exception e) {
            System.out.println(e);
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
        boolean result = false;

        try { // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT username FROM players WHERE username = '" + name + "'");
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

            Statement statement = connection.createStatement();
            statement.execute("SELECT password FROM players WHERE username = '" + name + "'");
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
            Statement statement = connection.createStatement();
            statement.execute("SELECT email FROM referrals WHERE username = '" + name + "' AND registered = 1");
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                count++;
            }
            rs.close();

            // delete
            if (count > 0) {
                Statement st = connection.createStatement();
                st.execute("DELETE FROM referrals WHERE username = '" + name + "' AND registered = 1");
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

            Statement statement = connection.createStatement();
            statement.execute("SELECT email FROM players WHERE username = '" + name + "'");
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

 /*public int getRank(Player owner)
 { int rank = 0;
  try
  { 
   // open a connection
   //Connection connection = initialize();
   Server.sendRating(owner);
   Statement statement = connection.createStatement();
   statement.execute("SELECT rating FROM players WHERE username = '" + owner.getName() + "'");
   ResultSet prs = statement.getResultSet();
   int rating = -1;
   while(prs.next())
   { 
      rating = prs.getInt("rating");
   }
   statement = connection.createStatement();
   //statement.execute("select count(*) + 1 as rank from players where rating > " + rating + " and keycode is not null");
   statement.execute("select count(*) + 1 as rank from players where rating > " + rating);
   ResultSet rs = statement.getResultSet();
   
   while(rs.next())
   { 
    rank = rs.getInt("rank");
   }
   rs.close();
   //connection.close();
  } catch (Exception e)
  {
   Log.error("DatabaseManager.getRank " + e);
  }
  return rank;
 }*/

    /////////////////////////////////////////////////////////////////
    // Get the player data
    /////////////////////////////////////////////////////////////////
    public boolean getRegistration(String name, int gameId) throws Exception {
        String keycodeColumn = getKeycodeColumn(gameId);
        boolean result = false;

        try { // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT " + keycodeColumn + " FROM players WHERE username = '" + name + "'");
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                String keycode = rs.getString(keycodeColumn);
                result = keycode != null;
            }
            rs.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.getRegistration");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Attempt to register
    /////////////////////////////////////////////////////////////////
    public short register(String name, String key, String email) throws Exception {
        try {
            String orderTable;
            if (key.startsWith(NONCL)) {
                noncl(key);
                orderTable = "noncl_sales_orders";
            } else
                orderTable = "sales_orders";

            int gameId = checkKeySale(key, orderTable);

            // check if the key exists in the sales db
            if (gameId == NONE) {
                Log.alert(name + " attempted to register invalid key: " + key);
                return Unit.FREE;
            }

            // make sure the key isn't in use
            if (checkKeyUse(key, gameId)) {
                Log.alert(name + " attempted register activated key: " + key);
                return Unit.FREE;
            }

            // save the changes
            saveRegistration(name, key, email, orderTable, gameId);
            Log.activity(name + " has registered using key: " + key);

            // all done
            switch (gameId) {
                case NONE:
                    return Unit.FREE;

                case CRUSADES:
                    return Unit.CRUSADES;

                case LEGIONS:
                    return Unit.LEGIONS;

                case INQUISITION:
                    return Unit.INQUISITION;
            }
            return Unit.FREE;

        } catch (Exception e) {
            Log.error("DatabaseManager.register");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Check to make sure the key has been sold
    /////////////////////////////////////////////////////////////////
    private void saveRegistration(String name, String key, String email, String orderTable, int gameId) throws Exception {
        try {
            String keycodeColumn = getKeycodeColumn(gameId);

            // open a connection
            //Connection connection = initialize();

            // the player database
            PreparedStatement ps = connection.prepareStatement("UPDATE players SET " + keycodeColumn + " = ? WHERE username = '" + name + "'");
            ps.setString(1, key);
            ps.execute();
            ps.close();

            // sales.orders
            PreparedStatement ps2 = connection.prepareStatement("UPDATE " + orderTable + " SET activated = ? WHERE keycode = '" + key + "'");
            ps2.setInt(1, 1);
            ps2.execute();
            ps2.close();

            // referrals
            PreparedStatement ps3 = connection.prepareStatement("UPDATE referrals SET registered = ? WHERE email = '" + email + "'");
            ps3.setInt(1, 1);
            ps3.execute();
            ps3.close();

            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.saveRegistration");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Check to make sure the key exists
    /////////////////////////////////////////////////////////////////
    private int checkKeySale(String key, String orderTable) throws Exception {
        int result = NONE;
        try { // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT keycode, game_id FROM " + orderTable + " WHERE keycode = '" + key + "' AND activated = 0");
            ResultSet rs = statement.getResultSet();

            while (rs.next()) {
                result = rs.getInt("game_id");
            }

            rs.close();
            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.checkKeySale");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Make sure the key isn't used by someone else
    /////////////////////////////////////////////////////////////////
    private boolean checkKeyUse(String key, int gameId) throws Exception {
        // get the keycode field
        String keycodeField = getKeycodeColumn(gameId);

        boolean result = false;
        try { // open a connection
            //Connection connection = initialize();

            Statement statement = connection.createStatement();
            statement.execute("SELECT " + keycodeField + " FROM players WHERE " + keycodeField + " = '" + key + "'");
            ResultSet rs = statement.getResultSet();

            result = rs.next();

            rs.close();
            //connection.close();
        } catch (Exception e) {
            Log.error("DatabaseManager.checkKeyUse");
            throw e;
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // valid email
    /////////////////////////////////////////////////////////////////
    public boolean validEmail(String address) {
        String lowered = address.toLowerCase();
        return lowered.matches(".+@.+\\.[a-z]{2,4}");
    }


    /////////////////////////////////////////////////////////////////
    // Run the noncl script
    /////////////////////////////////////////////////////////////////
    private void noncl(String keycode) {
        try {
            URL url = new URL(WEBSCRIPT + keycode);
            url.openConnection();
            Object nothing = url.getContent();
        } catch (Exception e) {
            Log.error("DatabaseManager.noncl " + e);
        }
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
   /*
   Properties properties = new Properties();
   properties.put("user", DB_USER);
   properties.put("password",DB_PASSWORD);
   
   return DriverManager.getConnection
   (DB_ADDRESS, properties);
*/
            //return DriverManager.getConnection
            // (DB_ADDRESS, DB_USER, DB_PASSWORD);

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
