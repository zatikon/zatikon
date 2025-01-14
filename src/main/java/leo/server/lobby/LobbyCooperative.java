///////////////////////////////////////////////////////////////////////
//	Name:	LobbyCooperative
//	Desc:	Players waiting for a game
//	Date:	8/24/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.lobby;

// imports

import leo.shared.Log;
import leo.server.Player;
import leo.server.Server;
import leo.server.game.CoopGame;
import leo.shared.Action;

import java.util.Stack;
import java.util.Vector;


public class LobbyCooperative implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private final Vector<Player> players = new Vector<Player>();
    private final Stack<Player> removes = new Stack<Player>();
    private final Server server;
    private boolean running = true; // Control flag for the main loop

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LobbyCooperative(Server server) {
        this.server = server;
        runner = new Thread(this, "LobbyCooperativeThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void add(Player newPlayer) {
        players.add(newPlayer);
        server.sendText(newPlayer, "*** " + newPlayer.getChatName() + " is now looking for a cooperative game ***");
    }


    /////////////////////////////////////////////////////////////////
    // Main thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        while (running) {
            try {
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.elementAt(i);
                    if (player.getUser().isClosed()) removes.add(player);
                    else if (player.getUser().cancelled()) {
                        removes.add(player);
                        player.getUser().sendAction(Action.CANCEL, Action.NOTHING, Action.NOTHING);
                    }
                }

                while (removes.size() > 0) {
                    Player player = removes.pop();
                    players.remove(player);
                }


                while (players.size() > 1) {
                    Player player1 = players.elementAt(0);
                    Player player2 = findMatch(player1);
                    if (player2 != null) {
                        // If player 1's rating is lower...
                        if (player1.getRating() < player2.getRating()) {
                            CoopGame newGame = new CoopGame(server, player2, player1);

                        } else if (player2.getRating() < player1.getRating()) {

                            CoopGame newGame = new CoopGame(server, player1, player2);

                        } else if (player1.getRating() == player2.getRating()) {
                            if (server.random().nextInt(2) == 0) {
                                CoopGame newGame = new CoopGame(server, player1, player2);
                            } else {
                                CoopGame newGame = new CoopGame(server, player2, player1);
                            }
                        }

                        players.remove(player1);
                        players.remove(player2);
                        server.sendText(null, "*** Cooperative game started with " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                        player1.getUser().sendText(Action.CHAT_WHISPER, player2, "Hi, I'm your ally in a cooperative game.");
                        player2.getUser().sendText(Action.CHAT_WHISPER, player1, "Hi, I'm your ally in a cooperative game.");
                    }
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                Log.error("LobbyCooperative.run " + e);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove
    /////////////////////////////////////////////////////////////////
    public void remove(Player player) {
        removes.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Find a match
    /////////////////////////////////////////////////////////////////
    private Player findMatch(Player player) {
        Player bestMatch = null;
        for (int i = 0; i < players.size(); i++) {
            Player tmpPlayer = players.elementAt(i);
            if (tmpPlayer != player && (bestMatch == null || compareRating(player, tmpPlayer) < compareRating(player, bestMatch)))
                bestMatch = tmpPlayer;
        }
        return bestMatch;
    }


    /////////////////////////////////////////////////////////////////
    // Compare rating
    /////////////////////////////////////////////////////////////////
    private int compareRating(Player player1, Player player2) {
        return Math.abs(player1.getRating() - player2.getRating());
    }

    /////////////////////////////////////////////////////////////////
    // Stop the lobby thread
    /////////////////////////////////////////////////////////////////
    public void stop() {
        Log.system("Stopping lobbyCoop thread...");
        running = false; // Stop the loop
        runner.interrupt(); // Interrupt the thread if it's sleeping
        try {
            // Optionally clear any resources or reset state if needed
        } catch (Exception e) {
            Log.error("Error while stopping the lobbyCoop thread: " + e);
        }
    }     
}
