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

public class Player {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final short STOP = -1;

    private static final int VERSION = 1;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    //private int accessLevel = Unit.FREE;
    private boolean admin = false;
    private boolean accessLoaded = false;
    private final short[] units = new short[Unit.UNIT_COUNT];
    private String name;
    private String password;
    private String passwordHashed;
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
    public Player(String newName, String newPassword, String newEmail) {
        name = newName;
        password = newPassword;
        email = newEmail;
        createStartingArmy();
        for (int i = 0; i < 10; i++) {
            String archiveName = "";
            castleArchives[i] = new CastleArchive(archiveName);
        }
        try {
            Server.getDB().insert(name, password, getRating(), toBytes(), email);
        } catch (Exception e) {
            Log.error("Player.constructor " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Constructor 2
    /////////////////////////////////////////////////////////////////
    public Player(DatabaseManager db, String username) throws Exception {
        try {
            byte[] buf;

            buf = getPlayerBytes(db, username);
            if (buf == null) return;
            email = db.getEmail(username);
            passwordHashed = db.getPasswordHashed(username);

            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            DataInputStream dis = new DataInputStream(bais);

            int version = dis.readInt();

            name = dis.readUTF();
            password = dis.readUTF();
            winsToLower = dis.readInt();
            wins = dis.readInt();
            lossesToHigher = dis.readInt();
            losses = dis.readInt();
            gamesPlayed = dis.readInt();

            // AI stuff
            computerWins = dis.readInt();
            computerLosses = dis.readInt();

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
            // risky override!
            name = username;

            dis.close();
            bais.close();
            loaded = true;

            // wipe, this should be commented out
            //save();
            eloRating = db.getRating(name);
            //if(filename.equalsIgnoreCase("test")){eloRating = 1600;}
            //if(filename.equalsIgnoreCase("tes")){eloRating = 1400;}
        } catch (Exception e) {
            Log.error("Player.constructor 2");
            throw e;
        }
    }

    public Player(String username) throws Exception {
        this(Server.getDB(), username);
    }


    /////////////////////////////////////////////////////////////////
    // Load access
    /////////////////////////////////////////////////////////////////
    private void loadAccess() {
        try {
//            admin = name.equals("gabe") || name.equals("ravean");
            // TODO cleanup the expansion loading. In FOSS version we don't need to restrict the players
            access[Unit.CRUSADES] = true; //Server.getDB().getRegistration(getName(), DatabaseManager.CRUSADES);
            access[Unit.LEGIONS] = true; //Server.getDB().getRegistration(getName(), DatabaseManager.LEGIONS);
            access[Unit.INQUISITION] = true; //Server.getDB().getRegistration(getName(), DatabaseManager.INQUISITION);

            Log.activity("loadAccess for " + getName() + " C: " + access[Unit.CRUSADES] + ", L: " + access[Unit.LEGIONS]);

            accessLoaded = true;

        } catch (Exception e) {
            Log.error("Player.loadAccess: " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get bytes from db
    /////////////////////////////////////////////////////////////////
    public byte[] getPlayerBytes(String filename) throws Exception {
        return getPlayerBytes(Server.getDB(), filename);
    }

    public byte[] getPlayerBytes(DatabaseManager db, String filename) throws Exception {
        try {
            return db.getPlayer(filename);
        } catch (Exception e) {
            Log.error("Player.getPlayerBytes");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get bytes
    /////////////////////////////////////////////////////////////////
    public byte[] getBytes(String filename) {
        try {
            File file = new File("./users/" + filename);
            if (!file.exists()) {
                return null;
            }

            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            byte[] buffer;
            int i;
            while ((i = fis.read()) != -1) {
                baos.write(i);
            }
            baos.close();
            fis.close();
            buffer = baos.toByteArray();

            return buffer;

        } catch (Exception e) {
            Log.error("Player.getBytes " + e);
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Save the player
    /////////////////////////////////////////////////////////////////
    public synchronized void save() {
        save(Server.getDB());
    }

    public synchronized void save(DatabaseManager db) {
        try {
   /*FileOutputStream fos = new FileOutputStream("./users/" + name);
   DataOutputStream dos = new DataOutputStream(fos);

   short buf[] = toBytes();

   dos.write(buf, 0, buf.length);
   dos.close();
                        fos.close();*/

            // update the database
            byte[] buf = toBytes();
            db.update(name, password, getRating(), buf, email);
            Server.sendRating(this);

        } catch (Exception e) {
            Log.error("Player.save " + e);
        }
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
            dos.writeUTF(password);
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
        boolean goldOk = (new Date().getTime() - goldStamp > Server.GOLD_TIMER);
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
    public void win(long reward) {
        gold += reward;
        computerWins++;
        save();
    }


    /////////////////////////////////////////////////////////////////
    // Lose against the AI
    /////////////////////////////////////////////////////////////////
    public void lose() {
        if (getLevel() > 15)
            computerLosses++;
    }


    /////////////////////////////////////////////////////////////////
    // Win
    /////////////////////////////////////////////////////////////////
    public boolean win(Player defeated, boolean rated) {
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
        Server.sendRating(this);
        return gained;
    }


    /////////////////////////////////////////////////////////////////
    // Lose
    /////////////////////////////////////////////////////////////////
    public boolean lose(Player victorious, boolean rated) {
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
        Server.sendRating(this);
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
        for (short i = 0; i < Unit.UNIT_COUNT; i++) {
            units[i] += Unit.startingCount(i);
        }

        // build the starting army
        startingCastle.add(Unit.getUnit(Unit.GENERAL, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.KNIGHT, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.WARRIOR, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.RANGER, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.ARCHER, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.PIKEMAN, startingCastle));
        startingCastle.add(Unit.getUnit(Unit.PIKEMAN, startingCastle));
    }


    /////////////////////////////////////////////////////////////////
    // Get the player's rating
    /////////////////////////////////////////////////////////////////
    public int getRating() {
        if (usingELO) return eloRating;
        else return 100 + gamesPlayed + ((wins - losses) * 10) + ((winsToLower - lossesToHigher) * 3);
    }

    public int getRank() {
        return Server.getDB().getRank(getRating());
    }

    /*public int getNewRank()
    {

     return Server.getDB().getRank(this);
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
    public void register(boolean refund, short gameId) {
        // TODO cleanup
//        if (refund)
//            gold += 50000;
//        else
//            gold += 10000;

        access[gameId] = true;
        save();
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
    }

    public String getChatName() {
        return chatName == null ? name : chatName;
    }

    public String getPassword() {
        return password;
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

    public void setPassword(String newPassword) {
        password = newPassword;
//        passwordHashed = newPassword;
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
