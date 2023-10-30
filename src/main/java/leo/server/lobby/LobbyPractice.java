///////////////////////////////////////////////////////////////////////
// Name: LobbyPractice
// Desc: Players waiting for a single player game
// Date: 11/12/2007 - Created [Gabe Jones]
//       10/26/2010 - Level 1 players enter tutorial game [Dan Healy]
//       10/27/2010 - Infinite loop found. Waiting on decision. [Dan Healy]
//       11/5/2010 - Nvm, it's fine if method at bottom isn't called [Dan Healy]
//       4/14/2011 - Level ( <= tutorial_level ) players enter tutorial level [Julian Noble]
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.lobby;

// imports

import leo.shared.BuildConfig;
import leo.shared.Log;
import leo.server.Player;
import leo.server.Server;
import leo.server.game.PracticeGame;
import leo.server.game.TutorialGame;
import leo.shared.Action;

import java.util.Stack;
import java.util.Vector;


public class LobbyPractice implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private final Vector<Player> players = new Vector<Player>();
    private final Stack<Player> removes = new Stack<Player>();
    private final int tutorial_level = 1;
    private final Server server;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LobbyPractice(Server server) {
        this.server = server;
        runner = new Thread(this, "LobbyPracticeThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void add(Player newPlayer) {
        players.add(newPlayer);
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

                while (players.size() > 0) {
                    Player player = players.elementAt(0);
                    PracticeGame pg;
                    TutorialGame tg;
                    if (player.getLevel() > tutorial_level || BuildConfig.skipTutorial) pg = new PracticeGame(server, player);
                    else tg = new TutorialGame(server, player);
                    server.sendText(player, "*** " + player.getChatName() + " is facing against the Artificial Opponent(" + player.getLevel() + ") ***");
                    players.remove(player);
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                Log.error("LobbyPractice.run " + e);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove
    /////////////////////////////////////////////////////////////////
    public void remove(Player player) {
        removes.add(player);
    }
}
