///////////////////////////////////////////////////////////////////////
// Name: ChatUser
// Desc: A chat user
// Date: 8/6/2003 - Created [Gabe Jones]
//       10/15/2010 - Updates: Added MirroredRandom mode [Tony Schwartz]
//       11/5/2010 - Now TutorialGame can use this to get at the client [Dan Healy]
//       12/9/2010 - The tutorial message now sends an int [Dan Healy]
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Action;
import leo.shared.Log;
import leo.shared.Unit;
import leo.server.game.*;
import org.tinylog.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;


public class ChatUser implements Runnable {
    private final Server server;
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean closed = false;
    private Socket socket;
    private User user = null;
    private Thread runner;
    private DataInputStream dis;
    private DataOutputStream dos;
    private int chatID = -1;
    private final StringBuffer sofar = new StringBuffer();
    private boolean alive = true;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatUser(Server server, Socket newSocket) {
        this.server = server;
        try {
            Logger.info("ChatUser thread started");
            socket = newSocket;
            //socket.setSoTimeout(0);
            runner = new Thread(this, "ChatUserThread");            
            runner.start();
        } catch (Exception e) {
            Log.error("ChatUser.constructor " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send Player
    /////////////////////////////////////////////////////////////////
    public void sendPlayer(Player sentPlayer) {
        try {
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
            tmpDos.writeShort(Action.CHAT_ADD_PLAYER);
            tmpDos.writeInt(sentPlayer.getUser().getChatID());
            //Change rank back to rating
            tmpDos.writeInt(sentPlayer.getRating());
            tmpDos.writeInt(sentPlayer.getRank());

            if (sentPlayer.getUser().interrupted()) {
                tmpDos.writeShort(Action.CHAT_DISABLE);
            } else if (sentPlayer.getUser().playing()) {
                if (sentPlayer.getUser().playing_ai())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_SINGLE);
                else if (sentPlayer.getUser().playing_cons())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_CONS);
                else if (sentPlayer.getUser().playing_coop())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_COOP);
                else if (sentPlayer.getUser().playing_rand())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_RAND);
                else if (sentPlayer.getUser().playing_mirr_rand())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_MIRR_RAND);
                else if (sentPlayer.getUser().playing_team())
                    tmpDos.writeShort(Action.CHAT_FIGHTING_2V2);
                else
                    tmpDos.writeShort(Action.CHAT_DISABLE);
            } else if (sentPlayer.getUser().waiting()) {
                tmpDos.writeShort(sentPlayer.getUser().getWaitingGame());
            } else if (sentPlayer.getUser().editing()) {
                tmpDos.writeShort(Action.CHAT_EDIT);
            } else {
                if (sentPlayer.getUser().wentIdle())
                    tmpDos.writeShort(Action.IDLE);
                else {
                    boolean crusades = sentPlayer.access(Unit.CRUSADES);
                    boolean legions = sentPlayer.access(Unit.LEGIONS);

                    if (crusades && legions)
                        tmpDos.writeShort(Action.CHAT_CRU_LEG);
                    else if (crusades && !legions)
                        tmpDos.writeShort(Action.CHAT_CRU);
                    else if (!crusades && legions)
                        tmpDos.writeShort(Action.CHAT_LEG);
                    else
                        tmpDos.writeShort(Action.CHAT_CHATTING);
                }
            }

            tmpDos.writeShort(sentPlayer.getChatName().length());

            for (int i = 0; i < sentPlayer.getChatName().length(); i++) {
                tmpDos.writeShort((byte) sentPlayer.getChatName().charAt(i));
            }

            tmpDos.writeInt(sentPlayer.getWins() + sentPlayer.getWinsToLower());
            tmpDos.writeInt(sentPlayer.getLosses() + sentPlayer.getLossesToHigher());

            tmpDos.flush();
            send(tmpBaos.toByteArray());

        } catch (Exception e) {
            Log.error("ChatUser.sendPlayer " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove
    /////////////////////////////////////////////////////////////////
    public void removePlayer(Player sentPlayer) {
        try {
            Logger.info("ChatUser.removePlayer");
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
            tmpDos.writeShort(Action.CHAT_REMOVE_PLAYER);
            tmpDos.writeInt(sentPlayer.getUser().getChatID());
            tmpDos.flush();
            send(tmpBaos.toByteArray());
        } catch (Exception e) {
            Log.error("ChatUser.removePlayer " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Update rating
    /////////////////////////////////////////////////////////////////
    public void updateRating(Player sentPlayer) {
        try {
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
            tmpDos.writeShort(Action.CHAT_UPDATE_RATING);
            tmpDos.writeInt(sentPlayer.getUser().getChatID());
            //Changing rank back to rating
            tmpDos.writeInt(sentPlayer.getRating());
            tmpDos.writeInt(sentPlayer.getRank());
            tmpDos.flush();
            send(tmpBaos.toByteArray());
        } catch (Exception e) {
            Log.error("ChatUser.updateRating " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Update state
    /////////////////////////////////////////////////////////////////
    public void sendState(Player sentPlayer, short newState) {
        try {
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
            tmpDos.writeShort(newState);
            tmpDos.writeInt(sentPlayer.getUser().getChatID());
            tmpDos.flush();
            send(tmpBaos.toByteArray());
        } catch (Exception e) {
            Log.error("ChatUser.sendState " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send player
    /////////////////////////////////////////////////////////////////
    public void sendText(short action, Player fromPlayer, String message) {
        try {
            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
            tmpDos.writeShort(action);
            tmpDos.writeInt(fromPlayer.getUser().getChatID());
            tmpDos.writeInt(message.length());
            for (int i = 0; i < message.length(); i++) {
                tmpDos.writeShort((byte) message.charAt(i));
            }

            tmpDos.flush();
            send(tmpBaos.toByteArray());

        } catch (Exception e) {
            Log.error("ChatUser.sendText " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send
    /////////////////////////////////////////////////////////////////
    public void send(byte[] data) {
        try {
            dos.write(data, 0, data.length);
        } catch (Exception e) {
            Log.error("ChatUser.send " + e);
            close();
        }
    }


    /////////////////////////////////////////////////////////////////
    // The thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        try { // Get rid of the friggen delay!
            //socket.setTcpNoDelay(true);

            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());

            // Get the chat ID
            chatID = dis.readInt();

            // Find a player with the right chat ID
            Vector<Player> logins = server.getLogins();
            Iterator<Player> it = logins.iterator();
            while (it.hasNext()) {
                Player player = it.next();
                if (player.getUser().getChatID() == chatID)
                    user = player.getUser();
            }

            // If nobody is found
            if (user == null) {
                Log.error("Invalid chat ID (" + chatID + ") for " + socket.getInetAddress());
                return;
            }

            user.setChat(this);
            Log.activity("Successful chat login for " + user.getPlayer().getName() + ", starting sleep");

            Thread.sleep(1000);
            Log.activity("Finished sleep");

            user.sendText("Welcome to Zatikon!");
            user.sendText(" ");

            //check if a server shutdown has been initiated, if so inform the client.
            if(server.getWillShutDown()) {
                user.sendAction(Action.SERVER_WILL_SHUTDOWN, Action.NOTHING, Action.NOTHING);
                user.sendText("Server shutdown initiated. Games can't be started. When all games have finished the server will shutdown and update.");    
            }

            try {
                BufferedReader in = new BufferedReader(new FileReader("MOTD.txt"));
                String line;
                while ((line = in.readLine()) != null) {
                    user.sendText(line);
                }
                in.close();
                user.sendText(" ");
            } catch (Exception e) {
                Log.error("Message of the day error: " + e);
            }

            while (alive) {
                try {
                    //Logger.info("ChatUser blocking read, waiting");
                    short action = dis.readShort();  // This call will throw if the socket is closed remotely.
                    //Logger.info("ChatUser action " + action);
                    process(action);
                    user.clearIdle();
                } catch (InterruptedException e) {
                    Logger.info("ChatUser thread was interrupted and is stopping.");
                    Thread.currentThread().interrupt(); // Restore interrupt status and break out.
                    break;
                } catch (EOFException | SocketException e) {
                    // EOFException is thrown when the end of the stream is reached,
                    // SocketException can be thrown if the connection is reset or closed.
                    Logger.info("Socket closed from remote end or connection reset: " + e.getMessage());
                    break;  // Exit the loop gracefully.
                } catch (Exception e) {
                    Logger.error("ChatUser error: " + e);
                    break;
                }
            }
            close();
            Logger.info("ChatUser thread is finishing gracefully.");
        } catch (Exception e) {
            Log.error("ChatUser.run " + e);
            close();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Process the action
    /////////////////////////////////////////////////////////////////
    private void process(short action) throws Exception {
        try {
            switch (action) {
                case Action.CHAT_INVITE:
                    Log.activity("action: CHAT_INVITE");
                    invite();
                    break;

                case Action.CHAT_ACCEPT:
                    Log.activity("action: CHAT_ACCEPT");
                    accept();
                    break;

                case Action.CHAT_WHISPER:
                    Log.activity("action: CHAT_WHISPER");
                    getText(Action.CHAT_WHISPER);
                    break;

                case Action.CHAT_BROADCAST:
                    Log.activity("action: CHAT_BROADCAST");
                    getText(Action.CHAT_BROADCAST);
                    break;

                default:
                    Log.error("Bad chat action: " + action);
            }

        } catch (Exception e) {
            Log.error("ChatUser.process: " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Invite to game
    /////////////////////////////////////////////////////////////////
    private void invite() throws Exception {
        try {
            int id = dis.readInt();
            int game = dis.readInt();

            ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
            DataOutputStream tmpDos = new DataOutputStream(tmpBaos);

            tmpDos.writeShort(Action.CHAT_INVITE);
            tmpDos.writeInt(chatID);
            tmpDos.writeInt(game);
            tmpDos.flush();

            Player gotPlayer = server.getPlayerChat(id);
            if (gotPlayer != null && !gotPlayer.getUser().interrupted()) {
                ChatUser tmpChat = gotPlayer.getUser().getChat();
                if (tmpChat != null) {
                    tmpChat.send(tmpBaos.toByteArray());
                }
            }

        } catch (Exception e) {
            Log.error("ChatUser.invite: " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Invite to game
    /////////////////////////////////////////////////////////////////
    private void accept() throws Exception {
        try {
            int id = dis.readInt();
            int game = dis.readInt();

            Player gotPlayer = server.getPlayerChat(id);
            if (gotPlayer != null && !gotPlayer.getUser().interrupted()) {
                Player player1 = user.getPlayer();
                Player player2 = gotPlayer;
                Player first = null;
                Player second = null;

                player1.getUser().sendText(Action.CHAT_WHISPER, player2, "I've accepted your invitation.");
                player2.getUser().sendText(Action.CHAT_WHISPER, player1, "I've accepted your invitation.");

                if (user.getGame() != null || gotPlayer.getUser().getGame() != null) {
                    if (user.getGame() != null) user.getGame().disconnect(user.getPlayer());
                    if (gotPlayer.getUser().getGame() != null) gotPlayer.getUser().getGame().disconnect(gotPlayer);
                    Thread.sleep(1000);
                }

                ByteArrayOutputStream tmpBaos = new ByteArrayOutputStream();
                DataOutputStream tmpDos = new DataOutputStream(tmpBaos);
                tmpDos.writeShort(Action.CHAT_ACCEPT);
                tmpDos.writeInt(chatID);
                tmpDos.writeInt(game);
                tmpDos.flush();
                ChatUser tmpChat = gotPlayer.getUser().getChat();
                if (tmpChat != null) {
                    tmpChat.send(tmpBaos.toByteArray());
                }

                tmpBaos = new ByteArrayOutputStream();
                tmpDos = new DataOutputStream(tmpBaos);
                tmpDos.writeShort(Action.CHAT_ACCEPT);
                tmpDos.writeInt(id);
                tmpDos.writeInt(game);
                tmpDos.flush();
                tmpChat = user.getChat();
                if (tmpChat != null) {
                    tmpChat.send(tmpBaos.toByteArray());
                }

                // If player 1's rating is lower...
                if (player1.getRating() < player2.getRating()) {
                    first = player1;
                    second = player2;
                } else if (player2.getRating() < player1.getRating()) {
                    first = player2;
                    second = player1;
                } else if (player1.getRating() == player2.getRating()) {
                    if (server.random().nextInt(2) == 0) {
                        first = player1;
                        second = player2;
                    } else {
                        first = player2;
                        second = player1;
                    }
                }

                try {

                    switch (game) {
                        case Action.GAME_CONSTRUCTED:
                            //Log.error("constructed");
                            ServerGame newCon = new ServerGame(server, first, second, ServerGame.MATCH, false, false, false);
                            server.sendText(null, "*** Constructed game started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                            break;

                        case Action.GAME_RANDOM:
                            //Log.error("random");
                            //ServerGame nnewCon = new ServerGame(first, second, ServerGame.MATCH, false, false);
                            ServerGame newRan = new ServerGame(server, first, second, ServerGame.DUEL, false, false, false);
                            server.sendText(null, "*** Random game started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                            break;

                        case Action.GAME_MIRRORED_RANDOM:
                            //Log.error("mirrored random");
                            ServerGame newMirrRan = new ServerGame(server, first, second, ServerGame.DUEL, false, true, false);
                            server.sendText(null, "*** Mirrored random game started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                            break;

                        case Action.GAME_COOPERATIVE:
                            //Log.error("cooperative");
                            CoopGame newCoop = new CoopGame(server, second, first);
                            server.sendText(null, "*** Cooperative game started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                            break;

                        case Action.GAME_REMATCH_RANDOM:
                            //Log.error("cooperative");
                            ServerGame newRem = new ServerGame(server, first, second, ServerGame.DUEL, false, false, true);
                            server.sendText(null, "*** Random game rematch started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                            break;
                    }

                } catch (Exception e) {
                    Log.error("ChatUser.accept.switch(game): " + e);
                    throw e;
                }

                tmpBaos = new ByteArrayOutputStream();
                tmpDos = new DataOutputStream(tmpBaos);
                tmpDos.writeShort(Action.GAME_LOOP);
                tmpDos.flush();
                tmpChat = user.getChat();
                if (tmpChat != null) {
                    tmpChat.send(tmpBaos.toByteArray());
                }
                tmpChat = gotPlayer.getUser().getChat();
                if (tmpChat != null) {
                    tmpChat.send(tmpBaos.toByteArray());
                }
            }
        } catch (Exception e) {
            Log.error("ChatUser.accept: " + e);
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a message
    /////////////////////////////////////////////////////////////////
    public void sendMessage(String text) {
        try {
            dos.writeShort(Action.CHAT_MESSAGE);
            dos.writeUTF(text);
        } catch (Exception e) {
            Log.error("ChatUser.sendMessage " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // allowRematch(): Tell client to allow to invite the player
    //    to replay their last random match together
    /////////////////////////////////////////////////////////////////
    public void allowRematch(Player player) {
        try {
            dos.writeShort(Action.ALLOW_REMATCH);
            dos.writeInt(player.getUser().getChatID());
        } catch (Exception e) {
            Log.error("ChatUser.allowRematch " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a message from the tutorial
    /////////////////////////////////////////////////////////////////
    public void sendTutorialMessage(int message, int first) {
        try {
            dos.writeShort(Action.CHAT_TUTORIAL_MESSAGE);
            dos.writeInt(first);
            dos.writeInt(message);
        } catch (Exception e) {
            Log.error("ChatUser.sendTutorialMessage " + e);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Whisper
    /////////////////////////////////////////////////////////////////
    private void getText(short action) throws Exception {
        try {
            StringBuffer messageBuffer = new StringBuffer();
            int id = dis.readInt();
            int length = dis.readInt();
            for (int i = 0; i < length; i++) {
                char ctmp = (char) dis.readShort();
                messageBuffer.append(ctmp);
            }
            if (action == Action.CHAT_WHISPER) {
                // check for announcement
                if (user.getPlayer().admin() && messageBuffer.toString().charAt(0) == '#') {
                    server.sendText(user.getPlayer(), messageBuffer.toString());
                }

                Player gotPlayer = server.getPlayerChat(id);
                if (gotPlayer != null) {
                    gotPlayer.getUser().sendText(action, user.getPlayer(), messageBuffer.toString());
                    Log.chat("Whisper " + user.getPlayer().getName() + " to " + gotPlayer.getName() + ": " + messageBuffer);
                }
            } else if (action == Action.CHAT_BROADCAST) {
                System.out.println("message: " + messageBuffer.toString());
                //start server shutdown
                if(user.getPlayer().admin() && messageBuffer.toString().equals("#shutdown")) {
                    server.startShutDown();
                    return;
                }
                Vector<Player> players = server.getPlayers();
                Iterator<Player> it = players.iterator();
                while (it.hasNext()) {
                    Player player = it.next();
                    if (true)//(player != user.getPlayer())
                    {
                        player.getUser().sendText(action, user.getPlayer(), messageBuffer.toString());
                        //Log.chat(player.getName() + " recieves message: " + messageBuffer.toString());
                    }
                }
                Log.chat(user.getPlayer().getName() + " broadcasts: " + messageBuffer);
            }
        } catch (Exception e) {
            Log.error("ChatUser.getText");
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Close it
    /////////////////////////////////////////////////////////////////
    public void close() {
        if (closed) return;
        closed = true;
        //user.close();
        alive = false;
        try {
            socket.close();
        } catch (Exception e) {
            Log.error("ChatUser.close " + e);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Stop Method
    /////////////////////////////////////////////////////////////////
    public void stop() {
        Log.system("Stopping ChatUser...");
        close();

        try {
            runner.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }

}
