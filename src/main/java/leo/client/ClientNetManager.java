///////////////////////////////////////////////////////////////////////
// Name: ClientNetManager
// Desc: The net manager
// Date: 2/7/2003 - Gabe Jones
//     11/18/2010 - Tony Schwartz
//   Updates: Finished redrawArmy
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.*;
import leo.shared.network.SocketProvider;
import org.tinylog.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.Vector;


public class ClientNetManager implements Runnable {


    private final boolean useTls;
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Thread runner;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private boolean active = true;
    private final int player = 1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientNetManager(boolean useTls) {
        this.useTls = useTls;
    }


    /////////////////////////////////////////////////////////////////
    // End
    /////////////////////////////////////////////////////////////////
    private void end() {
    }


    /////////////////////////////////////////////////////////////////
    // Start
    /////////////////////////////////////////////////////////////////
    public void start() { //System.out.println("Started net loop.");
        active = true;
        runner = new Thread(this, "ClientNetManagerThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Connect to the server
    /////////////////////////////////////////////////////////////////
    public LoginResponse connect(LoginAttempt loginAttempt) throws Exception {
        try {
            // Create the connection to the server
            socket = SocketProvider.newSocket(Client.serverName, Client.LOGIN_PORT, useTls);

            // Bye delay
            //socket.setSoTimeout(0);
            socket.setTcpNoDelay(true);

            // Initialize the streams
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // Send the login request
            dos.writeUTF(loginAttempt.getUsername());
            dos.writeUTF(loginAttempt.getPassword());
            dos.writeUTF(loginAttempt.getEmail());
            dos.writeShort(loginAttempt.isNewAccount());
            dos.writeUTF(loginAttempt.getVersion());
            dos.writeBoolean(loginAttempt.newsletter());

            // Get the response
            Logger.debug("Waiting for login response");
            LoginResponse response = new LoginResponse(dis.readInt(), dis.readInt());
            Logger.debug("Got login response");

            return response;

        } catch (Exception e) {
            Logger.error("connect: " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void requestGame() {
        try { // Start the join
            dos.writeShort(Action.JOIN);

        } catch (Exception e) {
            Logger.error("req game: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void requestDuel() {
        try { // Start the join
            dos.writeShort(Action.JOIN_DUEL);

        } catch (Exception e) {
            Logger.error("req duel " + e);
            Client.getGameData().screenDisconnect();
        }
    }

    /////////////////////////////////////////////////////////////////
    // Initialize the game state for Mirrored Random mode
    /////////////////////////////////////////////////////////////////
    public void requestMirrDuel() {
        try { // Start the join
            dos.writeShort(Action.JOIN_MIRRORED_DUEL);

        } catch (Exception e) {
            Logger.error("req duel " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void requestPractice() {
        try { // Start the join
            dos.writeShort(Action.PRACTICE);

        } catch (Exception e) {
            Logger.error("req practice " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void requestCooperative() {
        try { // Start the join
            dos.writeShort(Action.COOPERATIVE);

        } catch (Exception e) {
            Logger.error("req coop" + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the edit army data
    /////////////////////////////////////////////////////////////////
    public void getArmyUnits() {
        try { // Clear the castles
            //Client.getGameData().getArmy().clear();

            // Wait for the game to end
            //runner.sleep(1000);

            // Start the join
            dos.writeShort(Action.GET_ARMY);

        } catch (Exception e) {
            Logger.error("get army units " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Main loop
    /////////////////////////////////////////////////////////////////
    public void run() { //System.out.println("Main net thread begun");
        try {
            while (active && !Client.shuttingDown() && !Client.timingOut()) {
                short action = dis.readShort();
                if (!active) return;
                //System.out.println("Run Action received: " + action);
                short ID = dis.readShort();
                if (!active) return;
                //System.out.println("Run Actor received: " + ID);
                short target = dis.readShort();
                if (!active) return;
                //System.out.println("Run Target received: " + target);
                process(action, ID, target);
            }

            //System.out.println("Main net loop ended.");

        } catch (Exception e) { //kill();
            Logger.error("ClientNetManager.run: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Process an action
    /////////////////////////////////////////////////////////////////
    private void process(short action, short actor, short target) throws Exception {
        try {

            //System.out.println("received: " + action + ", " + actor + ", " + target);

            // If under 30, it's a unit
            if (action < 30 && Client.getGameData().playing()) {
                Unit unit = Client.getGameData().getBattleField().getUnitAt(actor);
                if (unit == null) {
                    Logger.error("process called on a null actor");
                    return;
                }
                Client.getGameData().getTextBoard().add(unit.performAction(action, target));
            }

            switch (action) {
                case Action.TIME_OUT:
                    Client.timeOut();
                    break;

                case Action.NOOB:
                    if (!BuildConfig.skipTutorial) {
                        Client.getGameData().noob();
                    }
                    break;

                case Action.QUIT:
                    System.exit(0);
                    break;

                case Action.GROW:
                    Client.getGameData().getBattleField().getUnitAt(actor).grow(target);
                    break;

                case Action.DISCONNECT:
                    Client.restart();
                    Client.getGameData().opponentDisconnect();
                    break;

                case Action.RESYNCH:
                    Client.restart();
                    Client.getGameData().resynch();
                    break;

                case Action.RESYNCH_READY:
                    Client.getGameData().setPlaying(true);
                    break;

                case Action.END_RESYNCH:
                    Client.getGameData().endResynch();
                    break;

                case Action.TOP_SCORES:
                    String topString = dis.readUTF();
                    //Thread.sleep(500);
                    ScoresBox top = new ScoresBox(topString);
                    break;

                case Action.NEED_EMAIL:
                    Client.needEmail(true);
                    break;

                case Action.ACCEPT_KEY:
                    ClientMessageDialog npak = new ClientMessageDialog("Congratulations, you've activated " + Unit.GAME_NAME[actor] + "!");
                    break;

                case Action.NO_REFERRAL:
                    ClientMessageDialog noref = new ClientMessageDialog("That address has already been referred.");
                    break;

                case Action.REJECT_KEY:
                    ClientMessageDialog nprk = new ClientMessageDialog("Your registration key was rejected.");
                    AccountFrame af = new AccountFrame(Client.getFrame());
                    break;

                case Action.REGISTER:
                    Client.register(actor);
                    break;

                case Action.DEPLOY_ALLY:
                    Client.getGameData().getMyCastle().add(0, Unit.getUnit(actor, Client.getGameData().getMyCastle()));
                    Client.getGameData().getTextBoard().add(Client.getGameData().getMyCastle().deploy(Unit.TEAM_2, Client.getGameData().getMyCastle().getUnit(0), target));
                    break;

                case Action.DEPLOY:
                    Client.getGameData().getTextBoard().add(Client.getGameData().getMyCastle().deploy(Unit.TEAM_1, Client.getGameData().getMyCastle().getUnit(actor), target));
                    break;

                case Action.END_TURN:
                    break;

                case Action.REFRESH:
                    Client.getGameData().getMyCastle().refresh(Unit.TEAM_1);
                    break;

                case Action.REFRESH_ENEMY:
                    Client.getGameData().getEnemyCastle().refresh(Unit.TEAM_1);
                    break;

                case Action.REFRESH_ENEMY_ALLY:
                    Client.getGameData().getEnemyCastle().refresh(Unit.TEAM_2);
                    break;

                case Action.REFRESH_ALLY:
                    Client.getGameData().getMyCastle().refresh(Unit.TEAM_2);
                    break;

                case Action.DEPLOY_ENEMY:
                    Client.getGameData().getEnemyCastle().add(Unit.getUnit(actor, Client.getGameData().getEnemyCastle()));
                    Client.getGameData().getTextBoard().add(Client.getGameData().getEnemyCastle().deploy(Unit.TEAM_1, Client.getGameData().getEnemyCastle().getUnit(0), target));
                    break;

                case Action.DEPLOY_ENEMY_ALLY:
                    Client.getGameData().getEnemyCastle().add(Unit.getUnit(actor, Client.getGameData().getEnemyCastle()));
                    Client.getGameData().getTextBoard().add(Client.getGameData().getEnemyCastle().deploy(Unit.TEAM_2, Client.getGameData().getEnemyCastle().getUnit(0), target));
                    break;

                case Action.NEW_UNIT:
                    Client.getGameData().getMyCastle().add(Unit.getUnit(actor, Client.getGameData().getMyCastle()));
                    Client.getGameData().castleChange();
                    break;

                case Action.CLEAR_CASTLE:
                    Client.getGameData().getMyCastle().clear();
                    break;

                case Action.NEW_ARMY_UNIT:
                    Client.getGameData().getArmy().add(Unit.getUnit(actor, Client.getGameData().getArmy()));
                    Client.getGameData().castleChange();
                    break;

                case Action.UNLOCK_UNITS:
                    Client.getUnits()[actor] = target;
                    break;

                case Action.RECRUIT_UNIT:
                    Client.getGameData().getMyCastle().getObserver().playSound(Constants.SOUND_BUY);
                    Client.getUnits()[actor] = target;
                    Client.getGameData().recruit(actor);
                    break;

                case Action.START_TURN_ENEMY_ALLY:
                    Client.getGameData().getMyCastle().getObserver().playSound(Constants.SOUND_END_TURN);
                    Client.getGameData().getEnemyCastle().startTurn(Unit.TEAM_2);
                    Client.getGameData().setCastlePlaying(Client.getGameData().getEnemyCastle());
                    break;

                case Action.START_TURN_ENEMY:
                    Client.getGameData().getMyCastle().getObserver().playSound(Constants.SOUND_END_TURN);
                    Client.getGameData().getEnemyCastle().startTurn(Unit.TEAM_1);
                    Client.getGameData().setCastlePlaying(Client.getGameData().getEnemyCastle());
                    break;

                case Action.P1_TURN:
                    Client.getGameData().setCurrPlayer(1);
                    break;

                case Action.P2_TURN:
                    Client.getGameData().setCurrPlayer(2);
                    break;

                case Action.START_TURN:
                    Client.getGameData().getMyCastle().getObserver().playSound(Constants.SOUND_START_TURN);
                    Client.getGameData().getMyCastle().startTurn(Unit.TEAM_1);
                    Client.getGameData().setTimer(90);
                    //System.out.println("My turn.");
                    if (Client.getGameData().getMyCastle().depleted() && !Client.getGameData().drawOffered() && !Client.getGameData().getEnemyCastle().depleted()) {
                        try {
                            Thread.sleep(500);
                        } catch (Exception e) {
                        }
                        Client.getGameData().endTurn();
                    } else {
                        Client.getGameData().setCastlePlaying(Client.getGameData().getMyCastle());
                    }
                    break;


                case Action.START_TURN_ALLY:
                    Client.getGameData().getMyCastle().getObserver().playSound(Constants.SOUND_START_TURN);
                    Client.getGameData().getMyCastle().startTurn(Unit.TEAM_2);
                    Client.getGameData().setCastlePlaying(null);
                    break;


                case Action.START_GAME:
                    Client.getImages().stopMusic();
                    Client.getGameData().screenGame();

                    Client.getChat().disallowRematch();

                    if (actor != Action.SET_RANDOM) {
                        Client.getGameData().setPanelLocation();
                        Client.getGameData().setGameType(Constants.CONSTRUCTED);
                    } else
                        Client.getGameData().setGameType(Constants.RANDOM);
                    break;

                case Action.ENEMY_LEFT:
                    leo.shared.Observer observer = Client.getGameData().getMyCastle().getObserver();
                    observer.enemySurrendered();
                    break;

                case Action.ALLY_LEFT:
                    leo.shared.Observer observer2 = Client.getGameData().getMyCastle().getObserver();
                    observer2.allySurrendered();
                    break;

                case Action.SET_RATING:
                    Client.setRating(actor, target);
                    break;

                case Action.SET_RANK:
                    Client.setRank(actor, target);
                    break;

                case Action.SET_WINS:
                    Client.setWins(actor, target);
                    break;

                case Action.SET_LOSSES:
                    Client.setLosses(actor, target);
                    break; 

                case Action.SET_GOLD:
                    try {
                        Client.setGold(dis.readLong());
                    } catch (Exception e) {
                    }
                    //Client.setGold(actor, target);
                    break;

                case Action.OPPONENT:
                    getOpponent();
                    Client.getGameData().screenVersus();
                    Client.getImages().stopMusic();
                    try {
                        Thread.sleep(4000);
                    } catch (Exception e) {
                    }
                    break;


                case Action.NEW_GAME:
                    Client.restart();
                    break;

                case Action.NEW_CASTLE:
                    break;

                case Action.NEW_ARMY:
                    Client.getGameData().getArmy().clear();
                    break;

                case Action.CANCEL:
                    //Client.getGameData().screenRoster();
                    Client.getGameData().cancelQueue();
                    break;

                case Action.CLEAR_TEAM:
                    Client.getGameData().getTeamLoadingPanel().clearTeams();
                    break;

                case Action.SELECT_TEAM:
                    int chatplayerid = dis.readInt();
                    ChatPlayer chatplayer = Client.getPlayer(chatplayerid);
                    if (chatplayer != null)
                        Client.getGameData().getTeamLoadingPanel().setTeam("" + chatplayer, actor, target);
                    else
                        Client.getGameData().getTeamLoadingPanel().setTeam("", actor, target);
                    break;

                case Action.NEW_PASSWORD:
                    ClientMessageDialog npa = new ClientMessageDialog("Your password was accepted");
                    break;

                case Action.REJECT_PASSWORD:
                    ClientMessageDialog npr = new ClientMessageDialog("Your password was rejected");
                    break;

                case Action.AI:
                    Client.getGameData().getEnemyCastle().ai();
                    break;

                case Action.SEND_ARCHIVE:
                    getCastleArchives();
                    break;
   
   /*case Action.SET_CONSTRUCTED:
 Client.getGameData().setGameType(Constants.CONSTRUCTED);
 //Client.getGameData().setPanelLocation();
 break;
   
   case Action.SET_RANDOM:
 Client.getGameData().setGameType(Constants.RANDOM);
 break;
   
   case Action.SET_MIRRORED_RANDOM:
 Client.getGameData().setGameType(Constants.MIRRORED_RANDOM);
 //Client.getGameData().setPanelLocation();
 break;*/

                case Action.DISABLE_REPICK_P1:
                    Client.getGameData().DisableRepickP1();
                    break;

                case Action.DISABLE_REPICK_P2:
                    Client.getGameData().DisableRepickP2();
                    break;

                case Action.MOVE_PANEL:
                    Client.getGameData().setPanelLocation();
                    break;

                case Action.OFFER_DRAW:
                    if (Client.getGameData().getMyDraw()) {
                        Client.getGameData().getMyCastle().getObserver().drawGame();
                        break;
                    }

                    if (Client.getGameData().drawOffered()) {
                        Client.addText(Client.getGameData().getEnemyName() + " has accepted your draw.");
                    } else {
                        Client.addText(Client.getGameData().getEnemyName() + " has offered you a draw.");
                        Client.getGameData().offerDraw();
                    }
                    break;
            }
        } catch (Exception e) {
            Logger.error("process: " + action + " " + actor + " " + target + " " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a new password
    /////////////////////////////////////////////////////////////////
    public void sendNewPassword(String oldPassword, String newPassword) {
        try {
            dos.writeShort(Action.NEW_PASSWORD);
            dos.writeUTF(oldPassword);
            dos.writeUTF(newPassword);
        } catch (Exception e) {
            Logger.error("sendNewPassword: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // send your email
    /////////////////////////////////////////////////////////////////
    public void sendEmail(String email) {
        try {
            dos.writeShort(Action.NEED_EMAIL);
            dos.writeUTF(email);
        } catch (Exception e) {
            Logger.error("sendEmail: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // send the referral
    /////////////////////////////////////////////////////////////////
    public void referFriend(String email) {
        try {
            dos.writeShort(Action.REFER_FRIEND);
            dos.writeUTF(email);
        } catch (Exception e) {
            Logger.error("referFriend " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle archives
    /////////////////////////////////////////////////////////////////
    private void getCastleArchives() {
        try {
            for (int i = 0; i < 10; i++) {
                String name = dis.readUTF();
                short size = dis.readShort();
                if (size > 0) {
                    Client.getCastleArchives()[i] =
                            new CastleArchive(name, size);
                } else {
                    Client.getCastleArchives()[i] =
                            new CastleArchive("<empty>", size);
                }

                //System.out.println
                // (
                // Client.getCastleArchives()[i].toString()
                // + ": " +
                // Client.getCastleArchives()[i].size()
                // );
            }
        } catch (Exception e) {
            Logger.error("getCastleArchive: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Opponent info
    /////////////////////////////////////////////////////////////////
    private void getOpponent() {
        try {
            int enemyRating = dis.readInt();
            char tmp;
            String name = "";
            while (true) {
                tmp = dis.readChar();
                if (tmp != 0)
                    name = name + tmp;
                else {
                    Client.getGameData().setOpponent(enemyRating, name);
                    return;
                }
            }

        } catch (Exception e) {
            Logger.error("ClientNetManager.getOpponent: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send action
    /////////////////////////////////////////////////////////////////
    public void sendAction(short action, short actor, short target) {
        try {
            dos.writeShort(action);
            dos.writeShort(actor);
            dos.writeShort(target);
        } catch (Exception e) {
            Logger.error("ClientNetManager.sendAction: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send action
    /////////////////////////////////////////////////////////////////
    public void sendAction(short action) {
        try {
            dos.writeShort(action);
        } catch (Exception e) {
            Logger.error("ClientNetManager.sendAction: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send castle
    /////////////////////////////////////////////////////////////////
    public void sendCastle() {
        try {
            Vector units = Client.getGameData().getArmy().getBarracks();
            dos.writeShort(Action.SET_ARMY);

            for (int i = 0; i < units.size(); i++) {
                UndeployedUnit unit = (UndeployedUnit) units.elementAt(i);
                for (int c = 0; c < unit.count(); c++)
                    dos.writeShort(unit.getID());
            }
            dos.writeShort(Action.END_ARMY);
        } catch (Exception e) {
            Logger.error("sendCastle " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Save a castle
    /////////////////////////////////////////////////////////////////
    public void saveCastleArchive(short index, String name) {
        try {
            dos.writeShort(Action.SAVE_ARCHIVE);
            dos.writeShort(index);
            dos.writeUTF(name);

        } catch (Exception e) {
            Logger.error("saveCastleArchive " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // send a byte
    /////////////////////////////////////////////////////////////////
    public void sendByte(short sendMe) {
        try {
            dos.writeShort(sendMe);

        } catch (Exception e) {
            Logger.error("sendByte " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Try to register
    /////////////////////////////////////////////////////////////////
    public void register(String key) {
        try {
            dos.writeShort(Action.REGISTER);
            dos.writeUTF(key);

        } catch (Exception e) {
            Logger.error("register " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Shut down
    /////////////////////////////////////////////////////////////////
    public void stop() { //active = false;
    }


    /////////////////////////////////////////////////////////////////
    // Shut down
    /////////////////////////////////////////////////////////////////
    public void kill() {
        try {
            active = false;
            if (socket != null)
                socket.close();
        } catch (Exception e) {
            Logger.error("ClientNetManager.kill: " + e);
        } finally {
            socket = null;
            dis = null;
            dos = null;
        }

    }
}
