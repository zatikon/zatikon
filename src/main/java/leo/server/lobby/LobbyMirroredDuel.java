///////////////////////////////////////////////////////////////////////
//	Name:	LobbyMirroredDuel
//	Desc:	Players waiting for a mirrored random game
//	Date:	10/19/2010 - Tony Schwartz
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.lobby;

// imports

import leo.shared.Log;
import leo.server.Player;
import leo.server.Server;
import leo.server.game.ServerGame;
import leo.shared.Action;

import java.util.Stack;
import java.util.Vector;


public class LobbyMirroredDuel implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private final Vector<Player> players = new Vector<Player>();
    private final Stack<Player> removes = new Stack<Player>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LobbyMirroredDuel() {
        runner = new Thread(this);
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void add(Player newPlayer) {
        players.add(newPlayer);
        Server.sendText(newPlayer, "*** " + newPlayer.getChatName() + " is now looking for a mirrored random game ***");
    }


    /////////////////////////////////////////////////////////////////
    // Main thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        while (true) {
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
                            ServerGame newGame = new ServerGame(player1, player2, ServerGame.DUEL, true, true, false);

                        } else if (player2.getRating() < player1.getRating()) {

                            ServerGame newGame = new ServerGame(player2, player1, ServerGame.DUEL, true, true, false);

                        } else if (player1.getRating() == player2.getRating()) {
                            if (Server.random().nextInt(2) == 0) {
                                ServerGame newGame = new ServerGame(player1, player2, ServerGame.DUEL, true, true, false);
                            } else {
                                ServerGame newGame = new ServerGame(player2, player1, ServerGame.DUEL, true, true, false);
                            }
                        }

                        players.remove(player1);
                        players.remove(player2);
                        Server.sendText(null, "*** Mirrored Random game started between " + player1.getChatName() + " and " + player2.getChatName() + " ***");
                        player1.getUser().sendText(Action.CHAT_WHISPER, player2, "Hi, I'm your opponent in a mirrored random game.");
                        player2.getUser().sendText(Action.CHAT_WHISPER, player1, "Hi, I'm your opponent in a mirrored random game.");
                    }
                }

                Thread.sleep(5000);
            } catch (Exception e) {
                Log.error("LobbyMirroredDuel.run " + e);
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
}
