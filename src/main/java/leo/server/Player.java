///////////////////////////////////////////////////////////////////////
// Name: Player 
// Desc: A player on the server 
// Date: 2/7/2003 - Gabe Jones 
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.*;

import java.io.*;
import java.util.Date;
import java.util.Vector;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Map;
//import java.util.HashMap;
import java.util.Iterator;
import org.tinylog.Logger;

public class Player {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final short STOP = -1;

    private static final int VERSION = 1;
//    private final Server server;
    private final DatabaseManager dbm;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    //private int accessLevel = Unit.FREE;
    //private boolean admin = false;
    private boolean accessLoaded = false;
    private final short[] units = new short[UnitType.UNIT_COUNT.value()];
    private String name;
    private String passwordHashed;
    byte[] salt;
    private String email;
    private int winsToLower = 0;
    private int wins = 0;
    private int lossesToHigher = 0;
    private int losses = 0;
    private int computerWins = 0;
    private int computerLosses = 0;
    private int gamesPlayed;
    private long gold = Balance.STARTING_GOLD;
    private transient Castle currentCastle = null;
    private Castle startingCastle = new Castle();
    private final Castle prevRandomCastle = new Castle();
    private transient User user;
    private transient boolean loaded = false;
    private long goldStamp = 0;
    private final CastleArchive[] castleArchives = new CastleArchive[10];
    private String chatName = null;

    private final boolean usingELO = true;
    private int eloRating = 1000;
    private boolean admin = false;
    // access
    private final boolean[] access =
            {true,           // free
                    false,          // crusades
                    false,          // legions
                    false          // inquisitions
            };


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Player(DatabaseManager dbm, String newName, String newPassword, String newEmail) {
        this.dbm = dbm;
        name = newName;
        setNewPassword(newPassword);
        email = newEmail;
        createStartingArmy();
        for (int i = 0; i < 10; i++) {
            String archiveName = "";
            castleArchives[i] = new CastleArchive(archiveName);
        }
        try {
            dbm.insert(name, passwordHashed, salt, getRating(), toBytes(), email, toJson());
        } catch (Exception e) {
            Log.error("Player.constructor " + e);
        }
    }

    public void setNewPassword(String newPassword) {
        salt = PasswordHasher.salt();
        passwordHashed = PasswordHasher.hashPassword(newPassword, salt);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor 2
    /////////////////////////////////////////////////////////////////
    public Player(DatabaseManager dbm, String username) throws Exception {
        this.dbm = dbm;
        try {
            byte[] buf = null;

            // risky override!
            name = username;

            //check if this player is an admin from the database
            admin = dbm.getAdmin(username);
            email = dbm.getEmail(username);
            salt = dbm.getSalt(username);
            passwordHashed = dbm.getPasswordHashed(username);
            eloRating = dbm.getRating(username);

            //buf = getPlayerBytes(dbm, username);
            Map<String, Object> playerData = getPlayerBytes(dbm, username);
            if (playerData == null) return;

            // Get json_data as a String and convert it to JSONObject
            String jsonData = (String) playerData.get("jsonData");
            //Log.activity("loaded json: " + jsonData); 
            if (jsonData != null) {
                JSONObject jsonObject = new JSONObject(jsonData);
                // Read the json if it is not empty.
                if (!jsonObject.isEmpty()) {
                    int version = jsonObject.optInt("version", 0);
                    winsToLower = jsonObject.optInt("winsToLower", 0);
                    wins = jsonObject.optInt("wins", 0);
                    lossesToHigher = jsonObject.optInt("lossesToHigher", 0);
                    losses = jsonObject.optInt("losses", 0);
                    gamesPlayed = jsonObject.optInt("gamesPlayed", 0);

                    // AI stuff
                    computerWins = jsonObject.optInt("computerWins", 0);
                    computerLosses = jsonObject.optInt("computerLosses", 0);
                    Logger.info("new wins/losses: " + computerWins + "/" + computerLosses);

                    gold = jsonObject.optLong("gold", 0L);
                    goldStamp = jsonObject.optLong("goldStamp", 0L);

                    JSONArray currentCastleArray = jsonObject.getJSONArray("currentCastle");
                    if (currentCastleArray != null) {
                        for (int i = 0; i < currentCastleArray.length(); i++) {
                            short unitId = (short) currentCastleArray.getInt(i); // Get each unit ID
                            getStartingCastle().add(Unit.getUnit(unitId, getStartingCastle()));
                        }
                    }             

                    // Retrieve the unlocked units object from the JSON
                    JSONObject unlockedUnitsObject = jsonObject.getJSONObject("unlockedUnits");

                    // Iterate through the keys and restore the units data
                    Iterator<String> keys = unlockedUnitsObject.keys();
                    while (keys.hasNext()) {
                        String unitID = keys.next();
                        short count = (short) unlockedUnitsObject.getInt(unitID);

                        // Restore the unit data (e.g., in your units array)
                        units[Short.parseShort(unitID)] = count;
                    }

                    // Get the castleArchives JSON object from the main JSON object
                    JSONArray castleArchivesArray = jsonObject.optJSONArray("castleArchives");

                    if(castleArchivesArray == null) {
                        for (int i = 0; i < 10; i++) {
                            castleArchives[i] = new CastleArchive("");
                        }
                    } else {
                        // Iterate over the array of castle archives
                        for (int i = 0; i < 10; i++) {
                            JSONObject archiveObject = castleArchivesArray.optJSONObject(i);

                            if (archiveObject != null) {
                                // Retrieve the archive name and units array
                                String archiveName = archiveObject.optString("archiveName", "");
                                JSONArray archiveUnitsArray = archiveObject.optJSONArray("units");

                                // Initialize the castle archive with the name
                                castleArchives[i] = new CastleArchive(archiveName);

                                // Iterate through the units in the array
                                if(archiveUnitsArray != null) {
                                    for (int a = 0; a < archiveUnitsArray.length(); a++) {
                                        short archiveID = (short) archiveUnitsArray.optInt(a);  // Convert to short and add to the archive
                                        castleArchives[i].add(archiveID);
                                    }
                                }
                            }
                        }
                    }
                loaded = true;
                //buf = (byte[]) playerData.get("data");
                return;
                } else { //use the old data
                    //Log.activity("json is empty, loading the old data");
                    buf = (byte[]) playerData.get("data");
                    if (buf == null) return;
                }
            } else {  // Get the buf (player data) if json_data is empty
                //Log.activity("json is null, loading the old data");
                buf = (byte[]) playerData.get("data");
                if (buf == null) return;
            }

            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            DataInputStream dis = new DataInputStream(bais);

            int version = dis.readInt();
  
            dis.readUTF(); // discard username (backwards compat)
            dis.readUTF(); // discard password (same)
            winsToLower = dis.readInt();
            wins = dis.readInt();
            lossesToHigher = dis.readInt();
            losses = dis.readInt();
            gamesPlayed = dis.readInt();

            // AI stuff
            computerWins = dis.readInt();
            computerLosses = dis.readInt();

            //Logger.info("Cwins/Closses: " + computerWins + "/" + computerLosses + "pwins/Ploss " + wins + "/" + losses);
            // Current Castle
            int size = dis.readInt();

            for (int i = 0; i < size; i++) {
                short unitId = dis.readShort();
                getStartingCastle().add(Unit.getUnit(unitId, getStartingCastle()));
            }

            // Unlocked units
            short unit = 0;
            while ((unit = dis.readShort()) != STOP) {
                units[unit] = dis.readShort();
            }

            // the gold
            gold = dis.readLong();

            // gold for test, TEMPORARY
//            if (username.equalsIgnoreCase("test"))
//                gold += 10000;
//            // units for tes
//            if (username.equalsIgnoreCase("tes"))
//                for (int i = 0; i < units.length; i++)
//                    units[i] = 20;

            // gold time stamp
            goldStamp = dis.readLong();

            // castle archives
            for (int i = 0; i < 10; i++) {
                String archiveName = dis.readUTF();
                castleArchives[i] = new CastleArchive(archiveName);
                int archiveSize = dis.readShort();
                for (int a = 0; a < archiveSize; a++) {
                    short archiveID = dis.readShort();
                    castleArchives[i].add(archiveID);
                }
            }

/*   
   // wipe time
   units = new short[Unit.UNIT_COUNT];
   createStartingArmy();
   gold = 0;
   gold+=(wins*150);
   gold+=(winsToLower*150);
   gold+=(lossesToHigher*100);
   gold+=(losses*100);
   gold+=(computerWins*50);
*/
            dis.close();
            bais.close();
            loaded = true;

            // wipe, this should be commented out
            //save();
            
            //if(filename.equalsIgnoreCase("test")){eloRating = 1600;}
            //if(filename.equalsIgnoreCase("tes")){eloRating = 1400;}
        } catch (Exception e) {
            Log.error("Player.constructor 2");
            throw e;
        }
    }

//    public Player(DatabaseManager dbm, String username) throws Exception {
//        this(dbm, username);
//    }


    /////////////////////////////////////////////////////////////////
    // Load access
    /////////////////////////////////////////////////////////////////
    private void loadAccess() {
        try {
            // TODO cleanup the expansion loading. In FOSS version we don't need to restrict the players
            access[Unit.CRUSADES] = true; //server.getDB().getRegistration(getName(), DatabaseManager.CRUSADES);
            access[Unit.LEGIONS] = true; //server.getDB().getRegistration(getName(), DatabaseManager.LEGIONS);
            access[Unit.INQUISITION] = true; //server.getDB().getRegistration(getName(), DatabaseManager.INQUISITION);

            //Log.activity("loadAccess for " + getName() + " C: " + access[Unit.CRUSADES] + ", L: " + access[Unit.LEGIONS]);

            accessLoaded = true;

        } catch (Exception e) {
            Log.error("Player.loadAccess: " + e);
        }
    }

    ///////////////////////////////////////////////////////////////
    // Get bytes and json_data from db
    ///////////////////////////////////////////////////////////////
    public Map<String, Object> getPlayerBytes(String filename) throws Exception {
        return getPlayerBytes(dbm, filename);
    }

    public Map<String, Object> getPlayerBytes(DatabaseManager db, String filename) throws Exception {
        try {
            return db.getPlayer(filename);  // Call the updated getPlayer method
        } catch (Exception e) {
            Log.error("Player.getPlayerBytes");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get bytes from db - OLD
    /////////////////////////////////////////////////////////////////
    /*
    public byte[] getPlayerBytes(String filename) throws Exception {
        return getPlayerBytes(dbm, filename);
    }

    public byte[] getPlayerBytes(DatabaseManager db, String filename) throws Exception {
        try {
            return db.getPlayer(filename);
        } catch (Exception e) {
            Log.error("Player.getPlayerBytes");
            throw e;
        }
    }*/


    /////////////////////////////////////////////////////////////////
    // Save the player
    /////////////////////////////////////////////////////////////////
    public synchronized void save() {
        try {
   /*FileOutputStream fos = new FileOutputStream("./users/" + name);
   DataOutputStream dos = new DataOutputStream(fos);

   short buf[] = toBytes();

   dos.write(buf, 0, buf.length);
   dos.close();
                        fos.close();*/

            // update the database
            byte[] buf = toBytes();
            String jsonData = toJson();
            //Log.activity("json: " + jsonData); 
            dbm.update(name, passwordHashed, salt, getRating(), buf, email, jsonData);

        } catch (Exception e) {
            Log.error("Player.save " + e);
        }
    }

    public String toJson() {
        try {
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("version", VERSION);
            jsonObject.put("winsToLower", winsToLower);
            jsonObject.put("wins", wins);
            jsonObject.put("lossesToHigher", lossesToHigher);
            jsonObject.put("losses", losses);
            jsonObject.put("gamesPlayed", gamesPlayed);

            // AI stuff
            jsonObject.put("computerWins", computerWins);
            jsonObject.put("computerLosses", computerLosses);

            jsonObject.put("gold", gold);
            jsonObject.put("goldStamp", goldStamp);

             // Send the barracks
            JSONArray currentCastleArray = new JSONArray();
            Vector<UndeployedUnit> barracks = startingCastle.getBarracks();
            for (int i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = barracks.get(i);  // Using get(i) instead of elementAt(i)
                for (int c = 0; c < unit.count(); c++) {
                    currentCastleArray.put(unit.getID());  // Add each unit ID to the array
                }
            }
            jsonObject.put("currentCastle", currentCastleArray);


            // Create a JSONObject to store the unlocked units
            JSONObject unlockedUnitsObject = new JSONObject();

            // Iterate through the units array and add the unit ID and count to the JSON object
            for (short i = 0; i < units.length; i++) {
                if (units[i] > 0) {
                    unlockedUnitsObject.put(String.valueOf(i), units[i]);  // Add unit ID as key, count as value
                }
            }
            jsonObject.put("unlockedUnits", unlockedUnitsObject);

            // Create a JSONArray to store all castle archives
            JSONArray castleArchivesArray = new JSONArray();

            // Iterate through the castle archives and add them to the JSON array
            for (int i = 0; i < 10; i++) {
                JSONObject archiveObject = new JSONObject();
                String archiveName = castleArchives[i].getName();
                JSONArray archiveUnitsArray = new JSONArray();

                // Add the units in the archive to the JSONArray
                for (int a = 0; a < castleArchives[i].size(); a++) {
                    archiveUnitsArray.put(castleArchives[i].get(a));  // Add unit ID to the array
                }

                // Set the archive name and units array in the archive object
                archiveObject.put("archiveName", archiveName);
                archiveObject.put("units", archiveUnitsArray);

                // Add the archive object to the main JSONArray
                castleArchivesArray.put(archiveObject);
            }

            // Add the castleArchivesObject to the main JSON object
            jsonObject.put("castleArchives", castleArchivesArray);

            // Convert to string and return
            return jsonObject.toString();
        } catch (Exception e) {
            Log.error("Player.toJson " + e);
        }
    return null;        
    }

    /////////////////////////////////////////////////////////////////
    // Save the player to text
    /////////////////////////////////////////////////////////////////
    public byte[] toBytes() {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);

            dos.writeInt(VERSION);

            dos.writeUTF(name);
            dos.writeUTF("");
            dos.writeInt(winsToLower);
            dos.writeInt(wins);
            dos.writeInt(lossesToHigher);
            dos.writeInt(losses);
            dos.writeInt(gamesPlayed);

            // AI
            dos.writeInt(computerWins);
            dos.writeInt(computerLosses);

            // Send the barracks
            Vector barracks = startingCastle.getBarracks();
            dos.writeInt(startingCastle.getCount());
            for (int i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = (UndeployedUnit) barracks.elementAt(i);
                for (int c = 0; c < unit.count(); c++)
                    dos.writeShort(unit.getID());
            }

            // Save the unlocked units
            for (short i = 0; i < units.length; i++) {
                if (units[i] > 0) {
                    dos.writeShort(i);
                    dos.writeShort(units[i]);
                }
            }
            dos.writeShort(STOP);

            // Send gold
            dos.writeLong(gold);

            // gold time stamp
            dos.writeLong(goldStamp);

            // write the castle archives
            for (int i = 0; i < 10; i++) {
                dos.writeUTF(castleArchives[i].getName());
                dos.writeShort(castleArchives[i].size());
                for (int a = 0; a < castleArchives[i].size(); a++) {
                    dos.writeShort(castleArchives[i].get(a));
                }
            }

            dos.close();
            baos.close();
            return baos.toByteArray();
        } catch (Exception e) {
            Log.error("Player.toBytes " + e);
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Player draw) {
        gainGold(Balance.REWARD_PVP_DRAW);
    }


    /////////////////////////////////////////////////////////////////
    // Gain gold
    /////////////////////////////////////////////////////////////////
    private boolean gainGold(long amount) {
        boolean goldOk = (new Date().getTime() - goldStamp > Constants.GOLD_TIMER);
        if (goldOk) {
            gold += amount;
            goldStamp = new Date().getTime();
        } else {
            goldStamp = new Date().getTime();
            Log.alert(getName() + " had quick gold");
        }
        save();
        return goldOk;
    }


    /////////////////////////////////////////////////////////////////
    // Beat the AI
    /////////////////////////////////////////////////////////////////
    public void winAi(long reward) {
        gold += reward;
        computerWins++;
        save();
    }


    /////////////////////////////////////////////////////////////////
    // Lose against the AI
    /////////////////////////////////////////////////////////////////
    public void loseAi() {
        if (getLevel() > 2)
            computerLosses++;
        save();
    }


    /////////////////////////////////////////////////////////////////
    // Win
    /////////////////////////////////////////////////////////////////
    public boolean win(Server server, Player defeated, boolean rated) {
        if (rated) {
            if (defeated.getRating() < getRating())
                winsToLower++;
            else
                wins++;

            // Add to the total of games they've played
            gamesPlayed++;
        }

        // Gold!
        boolean gained = gainGold(Balance.REWARD_PVP_WIN);
        server.sendRating(this);
        return gained;
    }


    /////////////////////////////////////////////////////////////////
    // Lose
    /////////////////////////////////////////////////////////////////
    public boolean lose(Server server, Player victorious, boolean rated) {
        if (rated) {
            if (victorious.getRating() > getRating())
                lossesToHigher++;
            else
                losses++;

            // Add to the total of games they've played
            gamesPlayed++;
        }
        // Gold!
        boolean gained = gainGold(Balance.REWARD_PVP_LOSS);
        server.sendRating(this);
        return gained;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize game
    /////////////////////////////////////////////////////////////////
    public void initializeGame(int type) {
        Castle tmpCastle = new Castle();
        switch (type) {
            case Action.GAME_PRACTICE:
            case Action.GAME_CONSTRUCTED:
                tmpCastle = startingCastle;
                break;
        }
        currentCastle = new Castle();

        // Temporary code
        Vector<UndeployedUnit> barracks = tmpCastle.getBarracks();
        for (short i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            for (int c = 0; c < unit.count(); c++)
                currentCastle.add(Unit.getUnit(unit.getID(), currentCastle));
        }
    }

    /////////////////////////////////////////////////////////////////
    // Initialize game to a specific castle
    /////////////////////////////////////////////////////////////////
    public void initializeGame(Castle castle) {
        currentCastle = new Castle();

        // Temporary code
        Vector<UndeployedUnit> barracks = castle.getBarracks();
        for (short i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            for (int c = 0; c < unit.count(); c++)
                currentCastle.add(Unit.getUnit(unit.getID(), currentCastle));
        }
    }


    /////////////////////////////////////////////////////////////////
    // Save castle to archive
    /////////////////////////////////////////////////////////////////
    public void saveArchive(short index, String newName) {
        Log.activity(getName() + " saving archive " + index);
        castleArchives[index] = new CastleArchive(newName);
        Vector<UndeployedUnit> barracks = startingCastle.getBarracks();
        for (short i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            for (int c = 0; c < unit.count(); c++)
                castleArchives[index].add(unit.getID());
        }
    }


    /////////////////////////////////////////////////////////////////
    // Load archive to castle
    /////////////////////////////////////////////////////////////////
    public void loadArchive(short index) {
        Log.activity(getName() + " loading archive " + index);

        startingCastle = new Castle();
        CastleArchive ca = castleArchives[index];
        for (short i = 0; i < ca.size(); i++) {
            startingCastle.add(Unit.getUnit(ca.get(i), startingCastle));
        }
    }


    /////////////////////////////////////////////////////////////////
    // Create starting army
    /////////////////////////////////////////////////////////////////
    private void createStartingArmy() {
        for (short i = 0; i < UnitType.UNIT_COUNT.value(); i++) {
            units[i] += Unit.startingCount(i);
        }

        // build the starting army
        startingCastle.add(Unit.getUnit(UnitType.GENERAL.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.KNIGHT.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.WARRIOR.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.RANGER.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.ARCHER.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.PIKEMAN.value(), startingCastle));
        startingCastle.add(Unit.getUnit(UnitType.PIKEMAN.value(), startingCastle));
    }


    /////////////////////////////////////////////////////////////////
    // Get the player's rating
    /////////////////////////////////////////////////////////////////
    public int getRating() {
        if (usingELO) return eloRating;
        else return 100 + gamesPlayed + ((wins - losses) * 10) + ((winsToLower - lossesToHigher) * 3);
    }

    public int getRank() {
        if(gamesPlayed == 0)
            return 0;
        return dbm.getRank(getRating());
    }

    /*public int getNewRank()
    {

     return server.getDB().getRank(this);
    }*/
    /////////////////////////////////////////////////////////////////
    // Get AI's level
    /////////////////////////////////////////////////////////////////
    public int getLevel() {
        int level = (computerWins) - (computerLosses / 2);
        if (level < 0) level = 0;
        return level + 1;
    }


    /////////////////////////////////////////////////////////////////
    // Register the account
    /////////////////////////////////////////////////////////////////
//    public void register(boolean refund, short gameId) {
//        // TODO cleanup
////        if (refund)
////            gold += 50000;
////        else
////            gold += 10000;
//
//        access[gameId] = true;
//        save();
//    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
    }

    public String getChatName() {
        return chatName == null ? name : chatName;
    }

    public String getEmail() {
        return email;
    }

    public Castle getCurrentCastle() {
        return currentCastle;
    }

    public Castle getPrevRandomCastle() {
        return prevRandomCastle;
    }

    public User getUser() {
        return user;
    }

    public Castle getStartingCastle() {
        return startingCastle;
    }

    public boolean playing() {
        return user.playing();
    }

    public boolean loaded() {
        return loaded;
    }

    public short[] getUnits() {
        return units;
    }

    public long getGold() {
        return gold;
    }

    public CastleArchive[] getCastleArchives() {
        return castleArchives;
    }

    public boolean admin() {
        return admin;
    }

    public String getPasswordHashed() {
        return passwordHashed;
    }

    public int getWins() {
        return wins;
    }

    public int getWinsToLower() {
        return winsToLower;
    }

    public int getLosses() {
        return losses;
    }

    public int getComputerWins() { return computerWins; }

    public void setComputerWins(int wins) { computerWins = wins; }

    public int getLossesToHigher() {
        return lossesToHigher;
    }

    public int getEloRating() {
        return eloRating;
    }

    public int getEloKfactor() {
        if (gamesPlayed < 20) return 50;
        else if (gamesPlayed < 50) return 40;
        else if (gamesPlayed < 100) return 30;
        else return 20;
    }

    public boolean access(short game) {
        if (!accessLoaded) loadAccess();
        return access[game];
    }

    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void wipeStamp() {
        goldStamp = 0;
    }

    public void setUser(User newUser) {
        user = newUser;
    }

    public void setGold(long newGold) {
        gold = newGold;
    }

    public void setPasswordHashed(String newPassword) {
        passwordHashed = newPassword;
    }

    public void setEmail(String newEmail) {
        email = newEmail;
    }

    public void setChatName(String newName) {
        chatName = newName;
    }

    public void setEloRating(int newRating) {
        eloRating = newRating;
    }

    //////////////////////////////////////////////////////////////////
    // Modify the castle saved for random game rematches
    //////////////////////////////////////////////////////////////////
    public void clearPrevRandomCastle() {
        prevRandomCastle.clear();
    }

    public void addToPrevRandomCastle(Unit newUnit) {
        prevRandomCastle.add(newUnit);
    }
}
