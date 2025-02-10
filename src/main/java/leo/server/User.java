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
import org.tinylog.Logger;

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
    private final Server server;

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

    private volatile boolean dataReady = false;
    private volatile short lastRequest = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public User(Server server, Socket newSocket) {
        this.server = server;
        chatID = server.getChatID();
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
        //active = false;
        try {
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
    // Quit
    /////////////////////////////////////////////////////////////////
    public void quit() {
        close();
        if (player != null) {
            server.sendText(player, "*** " + player.getChatName() + " has left the game ***");
            server.remove(player);
            player.save();
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
            Logger.info("sendPing");
            if(game == null && !waiting && !editing) {
                dos.writeShort(Action.PING);
                dos.writeShort(Action.NOTHING);
                dos.writeShort(Action.NOTHING);
            }
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
        Vector<Player> players = server.getPlayers();
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
        if (game instanceof PracticeGame || game instanceof TutorialGame)
            server.sendState(getPlayer(), Action.CHAT_FIGHTING_SINGLE);
        else if (game instanceof CoopGame)
            server.sendState(getPlayer(), Action.CHAT_FIGHTING_COOP);
        else if (game instanceof TeamGame)
            server.sendState(getPlayer(), Action.CHAT_FIGHTING_2V2);
        else if (game instanceof ServerGame) {
            switch (((ServerGame) game).getGameType()) {
                case Action.GAME_CONSTRUCTED:
                    server.sendState(getPlayer(), Action.CHAT_FIGHTING_CONS);
                    break;
                case Action.GAME_RANDOM:
                    server.sendState(getPlayer(), Action.CHAT_FIGHTING_RAND);
                    break;
                case Action.GAME_MIRRORED_RANDOM:
                    server.sendState(getPlayer(), Action.CHAT_FIGHTING_MIRR_RAND);
                    break;
            }
        } else
            server.sendState(getPlayer(), Action.CHAT_DISABLE);
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame() {
        player.endTurn();
        game = null;
        waiting = false;
        server.sendState(getPlayer(), Action.CHAT_CHATTING);
    }


    /////////////////////////////////////////////////////////////////
    // The thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        //short lastRequest = 0;
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
                boolean newbie = server.getDB().checkPlayer(login.getUsername());

                if (newbie)
                    player = getExistingPlayer(login);
                else
                    player = createNewPlayer(login);

                if (player == null) return;
                Player existing = server.checkForPlayer(login.getUsername());
                if (existing != null) {
                    server.remove(existing);
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
                Player existing = server.checkForPlayer(login.getUsername());
                if (existing != null) {
                    server.remove(existing);
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
            player.setChatName(server.getDB().getChatName(player.getName()));

            // Set the user object
            player.setUser(this);

            // Wait for chat
            server.addLogin(player);
            while (chat == null) {
                Thread.sleep(100);
            }
            server.removeLogin(player);

            // Tell them who is on
            sendPlayers();

            // Add the player to the server
            server.add(player);

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
                    server.sendState(getPlayer(), Action.CHAT_CHATTING);
                }
            }

            if (player.getLevel() == 1) {
                sendAction(Action.NOOB, Action.NOTHING, Action.NOTHING);
            }

            server.sendText(player, "*** " + player.getChatName() + " has entered the chatroom ***");

            // Start the reading thread
            startReadingThread();
            //int showDebugText = 0;
            while (active && !retired) {
                idle = true;
                long currentTime = System.currentTimeMillis();

                /*
                showDebugText++;
                if (game != null && showDebugText >= 200) {
                    Logger.info("check " + player.getName() + " time: " + (int) ((currentTime / 1000) % 60) + " - " + (player.getStartTurnTime() == -1 ? "-1" : (int) ((player.getStartTurnTime() / 1000) % 60)));
                    showDebugText = 0;
                } */
                // if it is players turn check if time limit is up, set a few seconds later than client will end turn in cause
                if (game != null && player.getStartTurnTime() != -1 && currentTime - player.getStartTurnTime() >= 10000) { // && player.getCurrentCastle() == game.getCurrentCastle()
                    Logger.info("server forced end of turn, client should have already");
                    sendAction(Action.END_TURN, Action.NOTHING, Action.NOTHING);
                    //end the turn
                    game.interpretAction(player, Action.END_TURN, Action.NOTHING, Action.NOTHING);
                }

                if (dataReady) {
                    //short request = lastRequest; // Get the last read request
                    //Logger.info("received request: " + lastRequest);
                    //lastRequest = request;
                    idle = false;
                    interpretRequest(lastRequest);
                    //clearIdle();
                    dataReady = false; // Reset the flag after processing
                }
                Thread.sleep(10);
            }

        } catch (Exception e) {
            Log.error("User.run " + player.getName() + ": " + e);
            Log.error("Last request: " + lastRequest);
        } finally {
            Logger.info("User.run.finally ");
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

                    server.sendState(getPlayer(), Action.CHAT_DISABLE);
                    //removePlayer(player); // added because was not cleaning up
                    Thread.sleep(1000);
                    if (!retired) {
                        quit();
                    }
                } else {
                    quit();
                }
            } catch (Exception e) {
                Logger.error("User.run.finally " + e);
            }
        }
    }

    private void startReadingThread() {
        // Start a thread to handle the blocking dis.readShort()
        // Logger.info("starting new thread startReadingThread()");
        new Thread(() -> {
            try {
                while (active && !retired) {
                    if(!dataReady) {
                        lastRequest = dis.readShort();  // Blocking read
                        dataReady = true;  // Indicate that data is ready to be processed
                    }

                    Thread.sleep(100);
                }
            } catch (InterruptedException e) {
                Logger.info("User.run startReadingThread() interrupted " + player.getName() + ": " + e);
                Thread.currentThread().interrupt();  // Re-set the interrupt flag
            } catch (Exception e) {
                Logger.error("User.run startReadingThread() " + player.getName() + ": " + e);
            } finally {
                Logger.info("User.run startReadingThread() finally");
            }
        }).start();
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

                    //Logger.info("Action: " + request + ", actor: " + actor + ", target: " + target);
                    game.interpretAction(player, request, actor, target);
                    clearIdle();
                } else {
                    switch (request) {
                    case Action.PING:
                        sendPing();
                        break;
                    }
                }

                return;
            }


            switch (request) {

                case Action.NOTHING:
                    break;

                case Action.CHATTING:
                    editing = false;
                    server.sendState(getPlayer(), Action.CHAT_CHATTING);
                    break;

                case Action.EDIT_ARMY:
                    editing = true;
                    server.sendState(getPlayer(), Action.CHAT_EDIT);
                    break;

                case Action.TOP_SCORES:
                    dis.readShort();
                    dis.readShort();
                    dos.writeShort(Action.TOP_SCORES);
                    dos.writeShort(Action.NOTHING);
                    dos.writeShort(Action.NOTHING);
                    dos.writeUTF(server.getDB().topScores());
                    break;

                case Action.NEED_EMAIL:
                    player.setEmail(dis.readUTF());
                    player.save();
                    break;

                case Action.NO_REFERRAL:

                    // check for referral rewards
                    int referrals = server.getDB().getReferrals(player.getName());
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
                    // TODO remove registration altogether (or replace with promo mechanism)
//                    register(dis.readUTF());
                    break;

                case Action.JOIN:
                    if(server.getWillShutDown() == true)
                        break;
                    if (initializeGame()) {
                        Log.activity("" + player.getName() + " has entered the lobby.");
                        server.sendState(getPlayer(), Action.CHAT_WAITING_CONS);
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
                    if(server.getWillShutDown() == true)
                        break;
                    if (initializeDuelGame()) {
                        server.sendState(getPlayer(), Action.CHAT_WAITING_RAND);
                        Log.activity(player.getName() + " has entered the duel lobby.");
                        waitingGame = Action.CHAT_WAITING_RAND;
                    }
                    waiting = true;
                    cancelled = false;
                    break;

                case Action.JOIN_MIRRORED_DUEL:
                    if(server.getWillShutDown() == true)
                        break;
                    if (initializeMirrDuelGame()) {
                        server.sendState(getPlayer(), Action.CHAT_WAITING_RAND);
                        Log.activity(player.getName() + " has entered the mirrored duel lobby.");
                        waitingGame = Action.CHAT_WAITING_RAND;
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.PRACTICE:
                    if(server.getWillShutDown() == true)
                        break;
                    initializePracticeGame();
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.COOPERATIVE:
                    if(server.getWillShutDown() == true)
                        break;  
                    if (initializeCooperativeGame()) {
                        server.sendState(getPlayer(), Action.CHAT_WAITING_COOP);
                        waitingGame = Action.CHAT_WAITING_COOP;
                    }
                    waiting = true;
                    cancelled = false;
                    break;


                case Action.TEAM:
                       if(server.getWillShutDown() == true)
                        break;  
                    if (initializeTeamGame()) {
                        server.sendState(getPlayer(), Action.CHAT_WAITING_2V2);
                        waitingGame = Action.CHAT_WAITING_2V2;
                    }
                    waiting = true;
                    cancelled = false;
                    break;

                case Action.SELECT_TEAM:
                    if(server.getWillShutDown() == true)
                        break;
                    server.addToNextTeamGame(getPlayer(), dis.readShort());
                    short check = dis.readShort();
                    if (check != Action.NOTHING)
                        Log.error("Weird things happening with selecting team");
                    break;

                case Action.START_TEAM_GAME:
                    if(server.getWillShutDown() == true)
                        break;
                    server.startTeamGame();
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
                        if (server.getDB().insertReferral(player.getName(), address)) {
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

                    sendAction(Action.SET_WINS, (byte) ((player.getWins() + player.getWinsToLower()) / 100), (byte) ((player.getWins() + player.getWinsToLower()) % 100));
                    sendAction(Action.SET_LOSSES, (byte) ((player.getLosses() + player.getLossesToHigher()) / 100), (byte) ((player.getLosses() + player.getLossesToHigher()) % 100));
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
                    quit(); //added as it seemed like it was not cleaning up on a client quitting
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
        for (int i = 0; i < UnitType.UNIT_COUNT.value(); i++) {
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

    public boolean verifyPasswordHashed(String passwordHashed) {
        return player.getPasswordHashed().equals(passwordHashed);
    }


    /////////////////////////////////////////////////////////////////
    // Load existing player
    /////////////////////////////////////////////////////////////////
    private void getNewPassword(String oldPassword, String newPassword) throws Exception {
        try {
            if (verifyPasswordHashed(PasswordHasher.hashPassword(oldPassword, player.salt))) {
                player.setNewPassword(newPassword);
                player.save();
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
            Player newPlayer = new Player(server.getDB(), login.getUsername());
            if (!newPlayer.loaded()) {
                Log.activity("Login username not found: " + login.getUsername());
                dos.writeInt(LoginResponse.FAIL_NOT_EXIST);
                dos.writeInt(0);
                return null;
            }

            if (!(newPlayer.getPasswordHashed().equals(PasswordHasher.hashPassword(login.getPassword(), newPlayer.salt)))) {
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
            if (server.getDB().playerExists(login.getUsername())) {
                dos.writeInt(LoginResponse.FAIL_ACCOUNT_EXISTS);
                dos.writeInt(0);
                return null;
            }

            // Create the new player object
            Player newPlayer = new Player(server.getDB(), login.getUsername(), login.getPassword(), login.getEmail());

            // Save the player object
            newPlayer.save();
            server.sendRating(newPlayer);

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
        server.addToLobby(player);
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Initialize the mirrored random game
    /////////////////////////////////////////////////////////////////
    public boolean initializeMirrDuelGame() {
        server.addToMirrDuelLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializePracticeGame() {
        server.addToPracticeLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeCooperativeGame() {
        server.addToCooperativeLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeTeamGame() {
        server.addToTeamLobby(player);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public boolean initializeDuelGame() {
        server.addToDuelLobby(player);
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
        //System.out.println("player trying to cancel");

        try {
            cancelled = true;
            waiting = false;
            server.sendState(getPlayer(), Action.CHAT_CHATTING);
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
        if(Client.standalone)
            return;

        // If waiting for a game, takes longer to idle
        if (waiting)
            idling += 10;
        else
            idling += 100;

        // Player has been idle for a long time, log him out
        if (idling >= 30000) {
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
            if (idling >= 500) {
                if (!wentIdle) {
                    Log.activity("Idle player " + getPlayer().getName());
                    wentIdle = true;
                    server.sendState(getPlayer(), Action.IDLE);
                    //disconnect = false;
                    //close();
                }
            }
            // Otherwise stop idling
            else if (wentIdle) {
                wentIdle = false;
                if (!waiting && !editing)
                    server.sendState(getPlayer(), Action.CHAT_CHATTING);
            }
        }

        if (chat != null) sendText("");
        if (game == null && idling > 1000 && !waiting && !editing)
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
                    server.sendState(getPlayer(), Action.CHAT_CHATTING);
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
            short unit = server.getRandomUnit();
  /*Unit checker = Unit.getUnit(unit, new Castle());
  while (!getPlayer().access(checker.accessLevel())) {     // Code to limit purchase to unlocked units
   unit = server.getRandomUnit();
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
