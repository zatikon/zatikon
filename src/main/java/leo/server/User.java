///////////////////////////////////////////////////////////////////////
// Name: User
// Desc: A user on the server
// Date: 5/8/2003 - Gabe Jones
//     10/19/2010 - Tony Schwartz
//   Updates: Added MirroredRandom mode
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.client.Client;
import leo.server.game.*;
import leo.shared.*;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;


public class User implements Runnable {


    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String SUBSCRIBE = "http://www.chroniclogic.com/cgi-bin/add_email.pl?email=";
    private static final String REFER = "http://www.chroniclogic.com/cgi-bin/940938_zat_invite.pl?to=";


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean interrupted = false;
    private boolean retired = false;
    private boolean disconnect = true;
    private Player player = null;
    private final Socket socket;
    private final Thread runner;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean active = true;
    private Game game = null;
    private boolean waiting = false;
    private boolean idle = false;
    private boolean wentIdle = false;
    private boolean cancelled = true;
    private final int chatID;
    private ChatUser chat = null;
    private int idling = 0;
    private short waitingGame = 0;
    private boolean editing = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public User(Socket newSocket) {
        chatID = Server.getChatID();
        socket = newSocket;
        runner = new Thread(this, "UserThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Close
    /////////////////////////////////////////////////////////////////
    public void close() {
        if (!active || retired) return;
        active = false;
        waiting = false;
        cancelled = true;
        if (game != null) {
            game.disconnect(player);
            game = null;
        }
        active = false;
        try {
            if (chat != null) chat.close();
            socket.close();
        } catch (Exception e) {
            Log.error("User.close " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Close without saving
    /////////////////////////////////////////////////////////////////
    public void retire() {
        retired = true;
        active = false;
        waiting = false;
        cancelled = true;
        try {
            socket.close();
        } catch (Exception e) {
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send an action
    /////////////////////////////////////////////////////////////////
    public void sendAction(short action, short actor, short target) {
        try {
            dos.writeShort(action);
            dos.writeShort(actor);
            dos.writeShort(target);
        } catch (Exception e) {
            Log.error("User.sendAction " + e);
            if (player != null) Log.error("sendAction Player: " + player.getName());
        }
    }

    public void sendInt(int intToSend) {
        try {
            dos.writeInt(intToSend);
        } catch (Exception e) {
            Log.error("User.sendInt " + e);
            if (player != null) Log.error("sendInt Player: " + player.getName());
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send registration
    /////////////////////////////////////////////////////////////////
    public void sendRegistration(short gameId) throws Exception {
        try {
            dos.writeShort(Action.REGISTER);
            dos.writeShort(gameId);
            dos.writeShort(Action.NOTHING);
        } catch (Exception e) {
            Log.error("User.sendRegistration");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send gold
    /////////////////////////////////////////////////////////////////
    public void sendGold() throws Exception {
        try {
            dos.writeShort(Action.SET_GOLD);
            dos.writeShort(Action.NOTHING);
            dos.writeShort(Action.NOTHING);
            dos.writeLong(player.getGold());
        } catch (Exception e) {
            Log.error("user.sendGold");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send ping
    /////////////////////////////////////////////////////////////////
    public void sendPing() throws Exception {
        try {
            dos.writeShort(Action.NOTHING);
            dos.writeShort(Action.NOTHING);
            dos.writeShort(Action.NOTHING);
        } catch (Exception e) {
            Log.error("user.sendPing");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send opponent info
    /////////////////////////////////////////////////////////////////
    public void sendOpponent(Player opponent) {
        try {
            sendAction(Action.OPPONENT, Action.NOTHING, Action.NOTHING);
            dos.writeInt(opponent.getRank());
            String name = opponent.getChatName();
            for (int i = 0; i < name.length(); i++) {
                dos.writeChar(name.charAt(i));
            }
            dos.writeChar(0);
        } catch (Exception e) {
            Log.error("User.sendOpponent" + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send opponent info
    /////////////////////////////////////////////////////////////////
    public void sendTeam(Player one, Player two) {
        try {
            sendAction(Action.OPPONENT, Action.NOTHING, Action.NOTHING);
            dos.writeInt(one.getRank() + two.getRank());
            String name = one.getChatName() + " & " + two.getChatName();
            for (int i = 0; i < name.length(); i++) {
                dos.writeChar(name.charAt(i));
            }
            dos.writeChar(0);
        } catch (Exception e) {
            Log.error("User.sendTeam" + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send the computer opponent
    /////////////////////////////////////////////////////////////////
    public void sendComputer(int level) {
        try {
            sendAction(Action.OPPONENT, Action.NOTHING, Action.NOTHING);
            dos.writeInt(level);
            String name = "The Artificial Opponent";
            for (int i = 0; i < name.length(); i++) {
                dos.writeChar(name.charAt(i));
            }
            dos.writeChar(0);
        } catch (Exception e) {
            Log.error("User.sendComputer " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send the players
    /////////////////////////////////////////////////////////////////
    private void sendPlayers() {
        Vector<Player> players = Server.getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player tmpPlayer = it.next();
            sendPlayer(tmpPlayer);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a player
    /////////////////////////////////////////////////////////////////
    public void sendPlayer(Player sentPlayer) {
        chat.sendPlayer(sentPlayer);
    }


    /////////////////////////////////////////////////////////////////
    // Send whisper
    /////////////////////////////////////////////////////////////////
    public void sendText(short action, Player fromPlayer, String message) {
        chat.sendText(action, fromPlayer, message);
    }


    /////////////////////////////////////////////////////////////////
    // Send a player
    /////////////////////////////////////////////////////////////////
    public void removePlayer(Player sentPlayer) {
        chat.removePlayer(sentPlayer);
    }


    /////////////////////////////////////////////////////////////////
    // Update rating
    /////////////////////////////////////////////////////////////////
    public void updateRating(Player sentPlayer) {
        chat.updateRating(sentPlayer);
    }


    /////////////////////////////////////////////////////////////////
    // Update rating
    /////////////////////////////////////////////////////////////////
    public void sendState(Player sentPlayer, short newState) {
        chat.sendState(sentPlayer, newState);
    }


    /////////////////////////////////////////////////////////////////
    // Start the game
    /////////////////////////////////////////////////////////////////
    public void startGame(Game newGame) {
        game = newGame;
        cancelled = true;
        waiting = false;
        if (game instanceof PracticeGame)
            Server.sendState(getPlayer(), Action.CHAT_FIGHTING_SINGLE);
        else if (game instanceof CoopGame)
            Server.sendState(getPlayer(), Action.CHAT_FIGHTING_COOP);
        else if (game instanceof TeamGame)
            Server.sendState(getPlayer(), Action.CHAT_FIGHTING_2V2);
        else if (game instanceof ServerGame) {
            switch (((ServerGame) game).getGameType()) {
                case Action.GAME_CONSTRUCTED:
                    Server.sendState(getPlayer(), Action.CHAT_FIGHTING_CONS);
                    break;
                case Action.GAME_RANDOM:
                    Server.sendState(getPlayer(), Action.CHAT_FIGHTING_RAND);
                    break;
                case Action.GAME_MIRRORED_RANDOM:
                    Server.sendState(getPlayer(), Action.CHAT_FIGHTING_MIRR_RAND);
                    break;
            }
        } else
            Server.sendState(getPlayer(), Action.CHAT_DISABLE);
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame() {
        game = null;
        waiting = false;
        Server.sendState(getPlayer(), Action.CHAT_CHATTING);
    }


    /////////////////////////////////////////////////////////////////
    // The thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        short lastRequest = 0;
        User oldUser = null;
        try { // Get rid of the friggen delay!
            socket.setTcpNoDelay(true);
            //socket.setSoTimeout(0);
            //socket.setKeepAlive(true);

            // Create the streams
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            // Get the login info
            LoginAttempt login = new LoginAttempt(dis.readUTF(), dis.readUTF(), dis.readUTF(), dis.readShort(), dis.readUTF(), dis.readBoolean());

            // Check the version
//            if (login.getVersion() < Client.VERSION) {
            // I don't think we can do backward compatibility just yet
            if (!Client.PROTOCOL_VERSION.equals(login.getVersion())) {
                Log.activity(
                        "User "
                                + socket.getInetAddress()
                                + " tried to use version: "
                                + login.getVersion());

                dos.writeInt(LoginResponse.FAIL_OLD_VERSION);
                dos.writeInt(0);
                return;
            }


            // Make sure the data is ok
            if (!validLoginData(login)) {
                dos.writeInt(LoginResponse.FAIL_WRONG_PASSWORD);
                dos.writeInt(0);
                return;
            }

            // Are they creating a new account?
            if (login.isNewAccount() == LoginAttempt.EXISTING_OR_NEW) {
                boolean newbie = Server.getDB().checkPlayer(login.getUsername());

                if (newbie)
                    player = getExistingPlayer(login);
                else
                    player = createNewPlayer(login);

                if (player == null) return;
                Player existing = Server.checkForPlayer(login.getUsername());
                if (existing != null) {
                    Server.remove(existing);
                    //existing.getUser().close();
                    oldUser = existing.getUser();
                    oldUser.sendAction(Action.QUIT, Action.NOTHING, Action.NOTHING);
                    oldUser.retire();
                    player = existing;
                    player.setUser(this);
                    Thread.sleep(1000);
                }
                dos.writeInt(LoginResponse.LOGIN_SUCCESSFUL);
                dos.writeInt(getChatID());
            } else if (login.isNewAccount() == LoginAttempt.NEW_ACCOUNT) {
                player = createNewPlayer(login);
                if (player == null) return;
                dos.writeInt(LoginResponse.CREATE_SUCCESSFUL);
                dos.writeInt(getChatID());

            } else {
                player = getExistingPlayer(login);
                if (player == null) {
                    return;
                }
                Player existing = Server.checkForPlayer(login.getUsername());
                if (existing != null) {
                    Server.remove(existing);
                    //existing.getUser().close();
                    oldUser = existing.getUser();
                    oldUser.sendAction(Action.QUIT, Action.NOTHING, Action.NOTHING);
                    oldUser.retire();
                    player = existing;
                    player.setUser(this);
                    Thread.sleep(1000);
                }

                dos.writeInt(LoginResponse.LOGIN_SUCCESSFUL);
                dos.writeInt(getChatID());
            }

            // Check for login failure
            if (player == null) return;

            // get the fb chat name
            player.setChatName(Server.getDB().getChatName(player.getName()));

            // Set the user object
            player.setUser(this);

            // Wait for chat
            Server.addLogin(player);
            while (chat == null) {
                Thread.sleep(100);
            }
            Server.removeLogin(player);

            // Tell them who is on
            sendPlayers();

            // Add the player to the server
            Server.add(player);

            if (!validateCastle(player.getStartingCastle())) {
                Log.alert(player.getName() + " had an invalid castle");
                player.getStartingCastle().clear();
            }

            // Tell them what they've unlocked
            sendUnits();
            sendCastleArchives();

            // send registration
            if (player.access(Unit.CRUSADES)) {
                sendRegistration(Unit.CRUSADES);
            }
            if (player.access(Unit.LEGIONS)) {
                sendRegistration(Unit.LEGIONS);
            }
            if (player.access(Unit.INQUISITION)) {
                sendRegistration(Unit.INQUISITION);
            }

            // get those empty email
            if (player.getEmail() == null || player.getEmail().length() < 1) {
                dos.writeShort(Action.NEED_EMAIL);
                dos.writeShort(Action.NOTHING);
                dos.writeShort(Action.NOTHING);
            }

            sendGold();

            if (oldUser != null) {
                Game orphan = oldUser.getGame();
                if (orphan != null) {
                    startGame(orphan);
                    orphan.resynch();
                } else {
                    Server.sendState(getPlayer(), Action.CHAT_CHATTING);
                }
            }

            if (player.getLevel() == 1) {
                sendAction(Action.NOOB, Action.NOTHING, Action.NOTHING);
            }

            Server.sendText(player, "*** " + player.getChatName() + " has entered the chatroom ***");
            while (active && !retired) {
                idle = true;
                short request = dis.readShort();
                lastRequest = request;
                idle = false;
                interpretRequest(request);
                clearIdle();
            }

        } catch (Exception e) {
            Log.error("User.run " + player.getName() + ": " + e);
            Log.error("Last request: " + lastRequest);
        } finally {
            cancelled = true;
            waiting = false;
            try {
                socket.close();
            } catch (Exception e) {
                Log.error("User.run socket close " + e);
            }

            try {
                interrupted = true;
                if (disconnect) {
                    if (game != null) {
                        game.interrupt(player);
                    }

                    Server.sendState(getPlayer(), Action.CHAT_DISABLE);

                    Thread.sleep(20000);
                    if (!retired) {
                        quit();
                    }
                } else {
                    quit();
                }
            } catch (Exception e) {
                Log.error("User.run.finally " + e);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Quit
    /////////////////////////////////////////////////////////////////
    public void quit() {
        close();
        if (player != null) {
            Server.sendText(player, "*** " + player.getChatName() + " has left the game ***");
            Server.remove(player);
            player.save();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Interpret the request
    /////////////////////////////////////////////////////////////////
    private void interpretRequest(short request) throws Exception {
        try {
            //Log.activity("interpretRequest");
            if (request < 50) {
                if (game != null && active) {
                    short actor = dis.readShort();
                    if (!(game != null && active)) return;
                    short target = dis.readShort();
                    if (!(game != null && active)) return;

                    //Log.activity("Action: " + request + ", actor: " + actor + ", target: " + target);
                    game.interpretAction(player, request, actor, target);
                    clearIdle();
                }
                return;
            }


            switch (request) {

                case Action.NOTHING:
                    break;

                case Action.CHATTING:
                    editing = false;
                    Server.sendState(getPlayer(), Action.CHAT_CHATTING);
                    break;

                case Action.EDIT_ARMY:
                    editing = true;
                    Server.sendState(getPlayer(), Action.CHAT_EDIT);
                    break;

                case Action.TOP_SCORES:
                    dis.readShort();
                    dis.readShort();
                    dos.writeShort(Action.TOP_SCORES);
                    dos.writeShort(Action.NOTHING);
                    dos.writeShort(Action.NOTHING);
                    dos.writeUTF(Server.getDB().topScores());
                    break;

                case Action.NEED_EMAIL:
                    player.setEmail(dis.readUTF());
                    player.save();
                    break;

                case Action.NO_REFERRAL:

                    // check for referral rewards
                    int referrals = Server.getDB().getReferrals(player.getName());
                    if (referrals > 0) {
                        Log.activity(player.getName() + " cashed in " + referrals + " referrals");
                        player.setGold(player.getGold() + (5000 * referrals));
                        sendGold();
                    }
                    break;

                case Action.SAVE_ARCHIVE:
                    player.saveArchive(dis.readShort(), dis.readUTF());
                    sendCastleArchives();
                    sendAction(Action.CANCEL, Action.NOTHING, Action.NOTHING);
                    break;

                case Action.LOAD_ARCHIVE:
                    player.loadArchive(dis.readShort());
                    dis.readShort();
                    if (!validateCastle(player.getStartingCastle())) {
                        player.getStartingCastle().clear();
                        sendText("Army archive contained missing units and failed to load.");
                    }
                    sendArmy(player.getStartingCastle());
                    sendAction(Action.CANCEL, Action.NOTHING, Action.NOTHING);
                    break;


                case Action.SELL_UNIT:
                    sellUnit(dis.readShort());
                    break;


                case Action.BUY_UNIT:
                    buyUnit();
                    break;


                case Action.CHOOSE_UNIT:
                    chooseUnit(dis.readShort());
                    break;


                case Action.NEW_PASSWORD:
                    getNewPassword(dis.readUTF(), dis.readUTF());
                    break;

                case Action.REGISTER:
                    register(dis.readUTF());
                    break;

                case Action.JOIN:
                    if (initializeGame()) {
                        Log.activity("" + player.getName() + " has entered the lobby.");
                        Server.sendState(getPlayer(), Action.CHAT_WAITING_CONS);
                        waitingGame = Action.CHAT_WAITING_CONS;
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.CANCEL:
                    cancel();
                    break;

                case Action.GAME_LOOP:
                    dis.readShort();
                    dis.readShort();
                    break;

                case Action.JOIN_DUEL:
                    if (initializeDuelGame()) {
                        Server.sendState(getPlayer(), Action.CHAT_WAITING_RAND);
                        Log.activity(player.getName() + " has entered the duel lobby.");
                        waitingGame = Action.CHAT_WAITING_RAND;
                    }
                    waiting = true;
                    cancelled = false;
                    break;

                case Action.JOIN_MIRRORED_DUEL:
                    if (initializeMirrDuelGame()) {
                        Server.sendState(getPlayer(), Action.CHAT_WAITING_RAND);
                        Log.activity(player.getName() + " has entered the mirrored duel lobby.");
                        waitingGame = Action.CHAT_WAITING_RAND;
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.PRACTICE:
                    if (initializePracticeGame()) {
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.COOPERATIVE:
                    if (initializeCooperativeGame()) {
                        Server.sendState(getPlayer(), Action.CHAT_WAITING_COOP);
                        waitingGame = Action.CHAT_WAITING_COOP;
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.TEAM:
                    if (initializeTeamGame()) {
                        Server.sendState(getPlayer(), Action.CHAT_WAITING_2V2);
                        waitingGame = Action.CHAT_WAITING_2V2;
                    }
                    waiting = true;
                    cancelled = false;
                    break;

                case Action.SELECT_TEAM:
                    Server.addToNextTeamGame(getPlayer(), dis.readShort());
                    short check = dis.readShort();
                    if (check != Action.NOTHING)
                        Log.error("Weird things happening with selecting team");
                    break;

                case Action.START_TEAM_GAME:
                    Server.startTeamGame();
                    short check1 = dis.readShort();
                    short check2 = dis.readShort();
                    if (check1 != Action.NOTHING && check2 != Action.NOTHING)
                        Log.error("User : Action.START_TEAM_GAME");
                    break;

                case Action.SET_ARMY:
                    setArmy();
                    break;


                case Action.REFER_FRIEND:
                    try {
                        String address = dis.readUTF();
                        if (Server.getDB().insertReferral(player.getName(), address)) {
                            Log.activity(player.getName() + " referring " + address);
                            URL url = new URL(REFER + address + "&from=" + player.getEmail());
                            url.openConnection();
                            Object nothing = url.getContent();

                        } else {
                            dos.writeShort(Action.NO_REFERRAL);
                            dos.writeShort(Action.NOTHING);
                            dos.writeShort(Action.NOTHING);
                        }
                        break;
                    } catch (Exception referE) {
                        Log.error("User.interpretRequest.REFER_FRIEND " + referE);
                    }

                case Action.GET_UPDATE:

                    // Send their rating
                    sendAction(Action.SET_RATING, (byte) (player.getRating() / 100), (byte) (player.getRating() % 100));
                    // Send their rank
                    sendAction(Action.SET_RANK, (byte) (player.getRank() / 100), (byte) (player.getRank() % 100));
                    // Send their gold
                    sendGold();

                    //sendPlayers();
                    break;

                case Action.GET_ARMY:

                    // Send the castle
                    sendArmy(player.getStartingCastle());

                    // Send units
                    //sendUnits();

                    break;

                case Action.QUIT:
                    disconnect = false;
                    break;

                default:
                    Log.error("An unknown request: " + request + " was received from "
                            + socket.getInetAddress());
            }

        } catch (Exception e) {
            Log.error("! user.interpretRequest " + request);
            throw e;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Send a castle to the player
    /////////////////////////////////////////////////////////////////
    public void sendCastle(Castle castle) {
        try {  //sendAction(Action.NEW_CASTLE, Action.NOTHING, Action.NOTHING);

            Vector<UndeployedUnit> barracks = castle.getBarracks();

            // Send the castle
            for (short i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = barracks.elementAt(i);
                for (int c = 0; c < unit.count(); c++)
                    sendAction(Action.NEW_UNIT, unit.getID(), Action.NOTHING);
            }

        } catch (Exception e) {
            if (active) close();
        }
    }

    /////////////////////////////////////////////////////////////////
    // Send a new castle to the player
    /////////////////////////////////////////////////////////////////
    public void newCastle(Castle castle) {
        try {
            //sendAction(Action.NEW_CASTLE, Action.NOTHING, Action.NOTHING);
            sendAction(Action.CLEAR_CASTLE, Action.NOTHING, Action.NOTHING);

            Vector<UndeployedUnit> barracks = castle.getBarracks();


            // Send the castle
            for (short i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = barracks.elementAt(i);
                for (int c = 0; c < unit.count(); c++)
                    sendAction(Action.NEW_UNIT, unit.getID(), Action.NOTHING);
            }

        } catch (Exception e) {
            if (active) close();
        }
    }

    /////////////////////////////////////////////////////////////////
    // Empty the current castle
    /////////////////////////////////////////////////////////////////

    public void clearCastle() {
        try {
            sendAction(Action.CLEAR_CASTLE, Action.NOTHING, Action.NOTHING);
        } catch (Exception e) {
            if (active) close();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a castle to the player
    /////////////////////////////////////////////////////////////////
    public void sendArmy(Castle castle) {
        try {
            sendAction(Action.NEW_ARMY, Action.NOTHING, Action.NOTHING);

            Vector<UndeployedUnit> barracks = castle.getBarracks();

            // Send the castle
            for (short i = 0; i < barracks.size(); i++) {
                UndeployedUnit unit = barracks.elementAt(i);
                for (int c = 0; c < unit.count(); c++)
                    sendAction(Action.NEW_ARMY_UNIT, unit.getID(), Action.NOTHING);
            }

        } catch (Exception e) {
            if (active) close();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a castle to the player
    /////////////////////////////////////////////////////////////////
    public void sendCastleArchives() throws Exception {
        try {
            sendAction(Action.SEND_ARCHIVE, Action.NOTHING, Action.NOTHING);

            // Send the archives
            CastleArchive[] archives = player.getCastleArchives();

            for (short i = 0; i < 10; i++) {
                dos.writeUTF(archives[i].getName());
                dos.writeShort(archives[i].size());
            }

        } catch (Exception e) {
            Log.error("Send archive error for " + player.getName() + ": " + e);
            throw e;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Send unlocked units to the player
    /////////////////////////////////////////////////////////////////
    public void sendUnits() throws Exception {
        try {
            short[] units = player.getUnits();

            // Send unlocked units
            for (short i = 0; i < units.length; i++) {
                short total = units[i];
                if (units[i] > 0)
                    sendAction(
                            Action.UNLOCK_UNITS,
                            i,
                            total);
            }
        } catch (Exception e) {
            Log.error("Unit unlock error for " + player.getName() + ": " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Receive army info from the user
    /////////////////////////////////////////////////////////////////
    public void setArmy() throws Exception {
        short unit = 0;

        player.getStartingCastle().clear();
        try {
            while (unit != Action.END_ARMY) {
                unit = dis.readShort();
                if (unit != Action.END_ARMY)
                    player.getStartingCastle().add(Unit.getUnit(unit, player.getStartingCastle()));
            }
        } catch (Exception e) {
            Log.error("Roster error for " + player.getName() + ": " + e);
            throw e;
        }
        if (!validateCastle(player.getStartingCastle())) {
            Log.alert(player.getName() + " had an invalid castle");
            player.getStartingCastle().clear();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the castle
    /////////////////////////////////////////////////////////////////
    private boolean validateCastle(Castle castle) {
        if (castle.getValue() > Constants.MAX_ARMY_SIZE) {
            Log.alert("Overvalue castle");
            return false;
        }
        for (int i = 0; i < Unit.UNIT_COUNT; i++) {
            if (player.getUnits()[i] - castle.getUnits()[i] < 0) {
                Log.alert("Nonexistant unit");
                return false;
            }
        }
        Vector<UndeployedUnit> barracks = castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            if (!player.access(unit.getUnit().accessLevel())) {
                Log.alert("Invalid unit access");
                return false;
            }
        }
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Is the login data valid
    /////////////////////////////////////////////////////////////////
    private boolean validLoginData(LoginAttempt login) {
        String name = login.getUsername();
        String password = login.getPassword();

        // Check the user name
        if (name.length() < 3) return false;
        if (name.length() > 100) return false;
        if (password.length() > 100) return false;
        for (int i = 0; i < name.length(); i++) {
            if (!Character.isLetterOrDigit(name.charAt(i))) {
                return false;
            }
        }

        // Everythings ok
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Attempt to register
    /////////////////////////////////////////////////////////////////
    private void register(String rawkey) throws Exception {
//        String key = stripGarbage(rawkey);

//        try {
//            short gameId = Server.getDB().register(player.getName(), key, player.getEmail());
//            if (gameId != Unit.FREE) {
//                Log.activity(player.getName() + " registered " + Unit.GAME_NAME[gameId]);
//                player.register(Server.getDB().refund(key), gameId);
//                sendGold();
//                sendRegistration(gameId);
//
//                dos.writeShort(Action.ACCEPT_KEY);
//                dos.writeShort(gameId);
//                dos.writeShort(Action.NOTHING);
//            } else {
        // todo for now we don't need registration, but later on it could be useful for a new feature
        dos.writeShort(Action.REJECT_KEY);
        dos.writeShort(Action.NOTHING);
        dos.writeShort(Action.NOTHING);
//            }
//        } catch (Exception e) {
//            Log.error("User.register");
//            throw e;
//        }
    }


    /////////////////////////////////////////////////////////////////
    // Strip the string
    /////////////////////////////////////////////////////////////////
    private static String stripGarbage(String s) {
        String good =
                "-ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (good.indexOf(s.charAt(i)) >= 0)
                result += s.charAt(i);
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Load existing player
    /////////////////////////////////////////////////////////////////
    private void getNewPassword(String oldPassword, String newPassword) throws Exception {
        try {
            if (oldPassword.contentEquals(player.getPassword()) || oldPassword.contentEquals(player.getPasswordHashed())) {
                /* TODO cleanup; this sets the password in the unhashed field; then the update should take care of the hashing;
                *   */
                player.setPassword(newPassword);
                player.save();
                player.setPasswordHashed(Server.getDB().getPasswordHashed(player.getName()));
                dos.writeShort(Action.NEW_PASSWORD);
                dos.writeShort(Action.NOTHING);
                dos.writeShort(Action.NOTHING);
            } else {
                dos.writeShort(Action.REJECT_PASSWORD);
                dos.writeShort(Action.NOTHING);
                dos.writeShort(Action.NOTHING);
            }
        } catch (Exception e) {
            Log.error("User.getNewPassword");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Load existing player
    /////////////////////////////////////////////////////////////////
    private Player getExistingPlayer(LoginAttempt login) throws Exception {
        try {
            Player newPlayer = new Player(login.getUsername());
            if (!newPlayer.loaded()) {
                Log.activity("Login username not found: " + login.getUsername());
                dos.writeInt(LoginResponse.FAIL_NOT_EXIST);
                dos.writeInt(0);
                return null;
            }

            // TODO reduce the passwords into one; there was password2 here instead of hashed when there were no hashes and it's rather confusing
            if (!(newPlayer.getPassword().equals(login.getPassword()) || newPlayer.getPasswordHashed().equals(login.getPassword()))) {
                Log.activity("Bad password for: " + login.getUsername());
                dos.writeInt(LoginResponse.FAIL_WRONG_PASSWORD);
                dos.writeInt(0);
                return null;
            }


            return newPlayer;

        } catch (Exception e) { //active = false;
            //return null;
            Log.error("User.getExistingPlayer: " + e);
            throw e;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Create a new player
    /////////////////////////////////////////////////////////////////
    private Player createNewPlayer(LoginAttempt login) throws Exception {
        try {
            // Does the file already exist?
            if (Server.getDB().playerExists(login.getUsername())) {
                dos.writeInt(LoginResponse.FAIL_ACCOUNT_EXISTS);
                dos.writeInt(0);
                return null;
            }

            // Create the new player object
            Player newPlayer = new Player(login.getUsername(), login.getPassword(), login.getEmail());

            // Save the player object
            newPlayer.save();

            if (login.newsletter()) {
                // if they want a newsletter, give it to'm
                URL url = new URL(SUBSCRIBE + login.getEmail());
                url.openConnection();
                Object nothing = url.getContent();
            }


            // Give them the new player
            return newPlayer;
        } catch (Exception e) {
            Log.error("User.createNewPlayer");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the user's address
    /////////////////////////////////////////////////////////////////
    public InetAddress getAddress() {
        return socket.getInetAddress();
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeGame() {
        Server.addToLobby(player);
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Initialize the mirrored random game
    /////////////////////////////////////////////////////////////////
    public boolean initializeMirrDuelGame() {
        Server.addToMirrDuelLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializePracticeGame() {
        Server.addToPracticeLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeCooperativeGame() {
        Server.addToCooperativeLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeTeamGame() {
        Server.addToTeamLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeDuelGame() {
        Server.addToDuelLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Are they closed?
    /////////////////////////////////////////////////////////////////
    public boolean isClosed() {
        return !active;
    }


    /////////////////////////////////////////////////////////////////
    // Are they playing a game?
    /////////////////////////////////////////////////////////////////
    public boolean playing() {
        return game != null;
    }

    public boolean playing_ai() {
        return game != null && (game instanceof PracticeGame || game instanceof TutorialGame);
    }

    public boolean playing_cons() {
        return game != null && game instanceof ServerGame && ((ServerGame) game).getGameType() == Action.GAME_CONSTRUCTED;
    }

    public boolean playing_coop() {
        return game != null && game instanceof CoopGame;
    }

    public boolean playing_team() {
        return game != null && game instanceof TeamGame;
    }

    public boolean playing_rand() {
        return game != null && game instanceof ServerGame && ((ServerGame) game).getGameType() == Action.GAME_RANDOM;
    }

    public boolean playing_mirr_rand() {
        return game != null && game instanceof ServerGame && ((ServerGame) game).getGameType() == Action.GAME_MIRRORED_RANDOM;
    }

    /////////////////////////////////////////////////////////////////
    // Waiting for a game?
    /////////////////////////////////////////////////////////////////
    public boolean waiting() {
        return waiting;
    }

    public short getWaitingGame() {
        return waitingGame;
    }

    /////////////////////////////////////////////////////////////////
    // Editing Army?
    /////////////////////////////////////////////////////////////////
    public boolean editing() {
        return editing;
    }


    /////////////////////////////////////////////////////////////////
    // Have they cancelled?
    /////////////////////////////////////////////////////////////////
    public boolean cancelled() {
        return cancelled;
    }


    /////////////////////////////////////////////////////////////////
    // Cancel out
    /////////////////////////////////////////////////////////////////
    public void cancel() throws Exception {
        try {
            cancelled = true;
            waiting = false;
            Server.sendState(getPlayer(), Action.CHAT_CHATTING);
            //sendAction(Action.CANCEL, Action.NOTHING, Action.NOTHING);
        } catch (Exception e) {
            Log.error("User.cancel");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the chat ID
    /////////////////////////////////////////////////////////////////
    public int getChatID() {
        return chatID;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player
    /////////////////////////////////////////////////////////////////
    public Player getPlayer() {
        return player;
    }


    /////////////////////////////////////////////////////////////////
    // Set the chat
    /////////////////////////////////////////////////////////////////
    public void setChat(ChatUser newChat) {
        chat = newChat;
    }

    public ChatUser getChat() {
        return chat;
    }


    /////////////////////////////////////////////////////////////////
    // Send text
    /////////////////////////////////////////////////////////////////
    public void sendText(String message) {
        if (chat != null) chat.sendText(Action.CHAT_SYSTEM, getPlayer(), message);
    }


    /////////////////////////////////////////////////////////////////
    // Check idle
    /////////////////////////////////////////////////////////////////
    public void checkIdle() {
        idling += 100;

        // If waiting for a game, takes longer to idle
        if (waiting)
            idling -= 90;

        // Player has been idle for a long time, log him out
        if (idling >= 3000) {
            Log.activity("Idling out user " + socket.getInetAddress());
            disconnect = false;
            sendAction(Action.TIME_OUT, Action.NOTHING, Action.NOTHING);
            close();
            return;
        }

        // Don't set to idle if in a game
        if (game == null) {
            // Set the Player's state to be idle if haven't communicated
            //   with server in a couple minutes
            if (idling >= 200) {
                if (!wentIdle) {
                    Log.activity("Idle player " + getPlayer().getName());
                    wentIdle = true;
                    Server.sendState(getPlayer(), Action.IDLE);
                    //disconnect = false;
                    //close();
                }
            }
            // Otherwise stop idling
            else if (wentIdle) {
                wentIdle = false;
                if (!waiting && !editing)
                    Server.sendState(getPlayer(), Action.CHAT_CHATTING);
            }
        }

        if (chat != null) sendText("");
        if (game == null && idling > 0)
            try {
                sendPing();
            } catch (Exception e) {
                if (active) close();
            }
    }


    /////////////////////////////////////////////////////////////////
    // Clear idle
    /////////////////////////////////////////////////////////////////
    public void clearIdle() {
        idling = 0;
        if (wentIdle == true) {
            wentIdle = false;
            if (game == null)
                if (!waiting && !editing)
                    Server.sendState(getPlayer(), Action.CHAT_CHATTING);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Buy a unit
    /////////////////////////////////////////////////////////////////
    private void buyUnit() throws Exception {
        try {
            if (player.getGold() < 100) {
                Log.alert(player.getName() + " tried to buy a unit with insufficient gold");
                return;
            }

            // Do the gold
            player.setGold(player.getGold() - 100);
            sendGold();

            // Do the unit
            short unit = Server.getRandomUnit();
  /*Unit checker = Unit.getUnit(unit, new Castle());
  while (!getPlayer().access(checker.accessLevel())) {     // Code to limit purchase to unlocked units
   unit = Server.getRandomUnit();
   checker = Unit.getUnit(unit, new Castle());
  }*/
            if (player.getUnits()[unit] < 100)
                player.getUnits()[unit]++;
            sendAction(Action.RECRUIT_UNIT, unit, player.getUnits()[unit]);
            sendText("*** You have recruited a new " + Unit.getUnit(unit, null).getName());
            Log.activity(player.getName() + " has recruited: " + Unit.getUnit(unit, null).getName());
            player.save();
        } catch (Exception e) {
            Log.error("User.buyUnit");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Buy a unit
    /////////////////////////////////////////////////////////////////
    private void chooseUnit(short chosenID) throws Exception {
        try {

            int cost = Balance.getUnitBuyPrice(chosenID);

            if (player.getGold() < cost) {
                Log.alert(player.getName() + " tried to choose a unit with insufficient gold");
                return;
            }

            // Do the gold
            player.setGold(player.getGold() - cost);
            sendGold();

            // Git it
            player.getUnits()[chosenID]++;

            Log.activity(player.getName() + " chose a: " + Unit.getUnit(chosenID, null).getName());
            player.save();
        } catch (Exception e) {
            Log.error("User.chooseUnit");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // sell a unit
    /////////////////////////////////////////////////////////////////
    private void sellUnit(short soldID) throws Exception {
        try {

            if ((player.getUnits()[soldID] - Unit.startingCount(soldID)) < 1) {
                Log.alert(player.getName() + " tried to sell a non-existent unit");
                return;
            }

            // Do the gold
            player.setGold(player.getGold() + (Balance.getUnitSellPrice(soldID)));
            sendGold();

            // Toss the sucker out
            player.getUnits()[soldID]--;

            Log.activity(player.getName() + " sold a: " + Unit.getUnit(soldID, null).getName());
            if (!validateCastle(player.getStartingCastle())) {
                player.getStartingCastle().clear();
            }
            player.save();
        } catch (Exception e) {
            Log.error("User.sellUnit");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // allowRematch(): Tell client to allow to invite the player
    //    to replay their last random match together
    /////////////////////////////////////////////////////////////////
    public void allowRematch(Player player) {
        chat.allowRematch(player);
    }

    public void setActive(boolean newState) {
        active = newState;
    }

    public Game getGame() {
        return game;
    }

    public boolean interrupted() {
        return interrupted;
    }

    /////////////////////////////////////////////////////////////////
    // wentIdle(): returns whether this player has gone idle,
    //    eg hasn't communicated with the server for a couple minutes
    /////////////////////////////////////////////////////////////////
    public boolean wentIdle() {
        return wentIdle;
    }
}
