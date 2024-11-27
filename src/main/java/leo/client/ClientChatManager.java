///////////////////////////////////////////////////////////////////////
// Name: ClientChatManager
// Desc: The chat manager
// Date: 8/6/2003 - Created [Gabe Jones]
//       10/19/2010 - Added Mirrored Random Mode [Tony Schwartz]
//       11/5/2010 - Now catches events from TutorialGame [Dan Healy]
//       12/9/2010 - The message is represented as an int [Dan Healy]
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.network.SocketProvider;
import org.tinylog.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;


public class ClientChatManager implements Runnable {


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private final int chatID;
    private final StringBuffer sofar = new StringBuffer();
    private boolean alive = true;
    private int currentRematchID = -1;
    private boolean useTls = true;


    /////////////////////////////////////////////////////////////////
    // Invite
    /////////////////////////////////////////////////////////////////
    public void invite(ChatPlayer to, int game) {
        try {
            dos.writeShort(Action.CHAT_INVITE);
            dos.writeInt(to.getID());
            dos.writeInt(game);

        } catch (Exception e) {
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Accept invitation
    /////////////////////////////////////////////////////////////////
    public void accept(ChatPlayer to, int game) {
        try {
            dos.writeShort(Action.CHAT_ACCEPT);
            dos.writeInt(to.getID());
            dos.writeInt(game);

        } catch (Exception e) {
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Whisper
    /////////////////////////////////////////////////////////////////
    public void whisper(ChatPlayer to, String message) {
        try {
            dos.writeShort(Action.CHAT_WHISPER);
            dos.writeInt(to.getID());
            dos.writeInt(message.length());

            for (int i = 0; i < message.length(); i++) {
                dos.writeShort((byte) message.charAt(i));
            }
        } catch (Exception e) {
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send
    /////////////////////////////////////////////////////////////////
    public void broadcast(String message) {
        try {
            dos.writeShort(Action.CHAT_BROADCAST);
            dos.writeInt(Client.getChat().getID());
            dos.writeInt(message.length());
            for (int i = 0; i < message.length(); i++) {
                dos.writeShort((byte) message.charAt(i));
            }
        } catch (Exception e) {
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientChatManager(int newChatID, boolean useTls) {
        this.chatID = newChatID;
        this.useTls = useTls;
        this.runner = new Thread(this, "ClientChatManagerThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Connect to the server
    /////////////////////////////////////////////////////////////////
    private void connect() throws Exception {
        try {
            // Create the connection to the server
            socket = SocketProvider.newSocket(Client.serverName, Client.CHAT_PORT, useTls);

            // Bye delay
            socket.setTcpNoDelay(true);
            //socket.setSoTimeout(0);

            // Initialize the streams
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());

            // Send the chat ID
            dos.writeInt(chatID);

            //short wtf = dis.readShort();
            //System.out.println("Wtf received: " + wtf);


        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Main loop
    /////////////////////////////////////////////////////////////////
    public void run() { //System.out.println("Chat thread begun with ID: " + chatID);
        try {
            connect();
            while (alive && !Client.shuttingDown() && !Client.timingOut()) {
                short action = dis.readShort();
                process(action);
            }

        } catch (Exception e) { //kill();
            //System.out.println("ClientChatManager.run: " + e);
            Client.getGameData().screenDisconnect();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Main loop
    /////////////////////////////////////////////////////////////////
    private void process(short action) throws Exception {
        try {
            switch (action) {
                case Action.CHAT_ADD_PLAYER:
                    addPlayer();
                    break;

                case Action.CHAT_REMOVE_PLAYER:
                    removePlayer();
                    break;

                case Action.CHAT_WHISPER:
                    getWhisper();
                    break;

                case Action.CHAT_INVITE:
                    getInvite();
                    break;

                case Action.CHAT_ACCEPT:
                    getAccept();
                    break;

                case Action.CHAT_BROADCAST:
                    getBroadcast();
                    break;

                case Action.CHAT_SYSTEM:
                    getSystem();
                    break;

                case Action.CHAT_MESSAGE:
                    getMessage();
                    break;

                case Action.CHAT_TUTORIAL_MESSAGE:
                    getTutorialMessage();
                    break;

                case Action.CHAT_UPDATE_RATING:
                    updateRating();
                    break;

                case Action.CHAT_CHATTING:
                case Action.CHAT_FIGHTING_SINGLE:
                case Action.CHAT_FIGHTING_CONS:
                case Action.CHAT_FIGHTING_COOP:
                case Action.CHAT_FIGHTING_RAND:
                case Action.CHAT_FIGHTING_MIRR_RAND:
                case Action.CHAT_FIGHTING_2V2:
                case Action.CHAT_DISABLE:
                case Action.IDLE:
                case Action.CHAT_WAITING_CONS:
                case Action.CHAT_WAITING_COOP:
                case Action.CHAT_WAITING_RAND:
                case Action.CHAT_WAITING_2V2:
                case Action.CHAT_EDIT:
                case Action.CHAT_CRU:
                case Action.CHAT_LEG:
                case Action.CHAT_CRU_LEG:
                    updatePlayerState(action);
                    break;

                case Action.GAME_LOOP:
                    Client.getNetManager().sendAction(Action.GAME_LOOP, Action.NOTHING, Action.NOTHING);
                    break;

                case Action.ALLOW_REMATCH:
                    allowRematch();
                    break;

                default:
                    Logger.warn("Invalid chat action: " + action);
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Add a player
    /////////////////////////////////////////////////////////////////
    private void addPlayer() throws Exception {
        try {
            StringBuffer nameBuffer = new StringBuffer();
            int id = dis.readInt();
            int rating = dis.readInt();
            int rank = dis.readInt();
            short state = dis.readShort();
            short length = dis.readShort();
            for (int i = 0; i < length; i++) {
                char ctmp = (char) dis.readShort();
                nameBuffer.append(ctmp);
            }
            int wins = dis.readInt();
            int losses = dis.readInt();            
            ChatPlayer player = new ChatPlayer(id, nameBuffer.toString(), rating, rank, wins, losses);
            player.setState(state);
            Client.add(player);
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a whisper
    /////////////////////////////////////////////////////////////////
    private void getWhisper() throws Exception {
        try {
            StringBuffer messageBuffer = new StringBuffer();
            int id = dis.readInt();
            int length = dis.readInt();
            for (int i = 0; i < length; i++) {
                char ctmp = (char) dis.readShort();
                messageBuffer.append(ctmp);
            }
            ChatPlayer player = Client.getPlayer(id);
            if (player != null && !Client.blocked(player.getName())) {
                player.receive(messageBuffer.toString());
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Allow player to request a random mode rematch with other player
    /////////////////////////////////////////////////////////////////
    private void allowRematch() throws Exception {
        try {
            int id = dis.readInt();
            ChatPlayer player = Client.getPlayer(id);
            currentRematchID = id;
            if (player != null)
                player.getMessageFrame().addRematchFunction();
        } catch (Exception e) {
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Player can no longer request rematch
    /////////////////////////////////////////////////////////////////
    public void disallowRematch() {
        if (currentRematchID == -1) return;
        ChatPlayer player = Client.getPlayer(currentRematchID);
        if (player != null)
            player.getMessageFrame().removeRematchFunction();
        currentRematchID = -1;
    }


    /////////////////////////////////////////////////////////////////
    // get an accept
    /////////////////////////////////////////////////////////////////
    private void getAccept() throws Exception {
        try {
            Client.getNetManager().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

            int id = dis.readInt();
            int game = dis.readInt();

            ChatPlayer player = Client.getPlayer(id);
            if (player != null) {
                player.getMessageFrame().accept();
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a invite
    /////////////////////////////////////////////////////////////////
    private void getInvite() throws Exception {
        try {
            int id = dis.readInt();
            int game = dis.readInt();

            ChatPlayer player = Client.getPlayer(id);
            if (player != null) {
                player.getMessageFrame().invite(game);
                //player.getMessageFrame().bump();
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a broadcast
    /////////////////////////////////////////////////////////////////
    private void getBroadcast() throws Exception {
        try {
            StringBuffer messageBuffer = new StringBuffer();
            int id = dis.readInt();
            int length = dis.readInt();
            for (int i = 0; i < length; i++) {
                char ctmp = (char) dis.readShort();
                messageBuffer.append(ctmp);
            }
            ChatPlayer player = Client.getPlayer(id);
            if (player != null && !Client.blocked(player.getName())) { //Client.addText(player.getName() + ": " + messageBuffer.toString());
                if (Client.getGameData().getPlayerPanel() != null) {
                    Client.getGameData().getPlayerPanel().getMessage(player.getName() + ": " + messageBuffer);
                    //Client.flashChatFrame();
                }
            }

        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a system message
    /////////////////////////////////////////////////////////////////
    private void getSystem() throws Exception {
        try {
            StringBuffer messageBuffer = new StringBuffer();
            int id = dis.readInt();
            int length = dis.readInt();
            for (int i = 0; i < length; i++) {
                char ctmp = (char) dis.readShort();
                messageBuffer.append(ctmp);
            }
            if (length > 0) { //Client.addText(messageBuffer.toString());
                if (Client.getGameData().getPlayerPanel() != null) {
                    Client.getGameData().getPlayerPanel().getMessage("" + messageBuffer);
                    //Client.flashChatFrame();
                }
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a system message
    /////////////////////////////////////////////////////////////////
    private void getMessage() throws Exception {
        try {
            String message = dis.readUTF();
            Client.addText(message);
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // get a system tutorial message
    /////////////////////////////////////////////////////////////////
    private void getTutorialMessage() throws Exception {
        try {
            ClientGameData cgd = Client.getGameData();
            int first = dis.readInt();
            int message = dis.readInt();
            cgd.tutorialEvent(message, first);
        } catch (Exception e) {
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////
    // remove a player
    /////////////////////////////////////////////////////////////////
    private void removePlayer() throws Exception {
        try {
            int id = dis.readInt();
            Client.removePlayer(id);

        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // player rating
    /////////////////////////////////////////////////////////////////
    private void updateRating() throws Exception {
        try {
            int id = dis.readInt();
            int newRating = dis.readInt();
            int newRank = dis.readInt();
            ChatPlayer player = Client.getPlayer(id);
            if (player != null) {
                player.setRating(newRating);
                player.setRank(newRank);
                Client.updateChatList();
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // update player's chat state
    /////////////////////////////////////////////////////////////////
    private void updatePlayerState(short action) throws Exception {
        try {
            int id = dis.readInt();
            ChatPlayer player = Client.getPlayer(id);
            if (player != null) {
                player.setState(action);
                Client.updateChatList();
            }
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Kill it!
    /////////////////////////////////////////////////////////////////
    public void kill() {
        try {
            alive = false;
            socket.close();
        } catch (Exception e) {
            Logger.error("ClientChatManager.kill(): " + e);
        }
    }


    public int getID() {
        return chatID;
    }
}
