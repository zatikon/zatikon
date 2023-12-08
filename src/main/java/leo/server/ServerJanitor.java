///////////////////////////////////////////////////////////////////////
// Name: ServerJanitor
// Desc: The login server
// Date: 12/23/2003 - Gabe Jones
// Notes: Queries the database through "DatabaseManager.ping()" once a minute
//         This maintains the connection that would otherwise close after
//         5 minutes
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

import leo.shared.Log;

import java.util.Vector;

public class ServerJanitor implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static final int INACTIVITY_TIMEOUT_HOURS = 6;
    private static final long INACTIVITY_TIMEOUT_MILLIS = INACTIVITY_TIMEOUT_HOURS * 60 * 60 * 1000;

    private final Thread runner;
    private final Server server;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ServerJanitor(Server server) {
        this.server = server;
        runner = new Thread(this, "ServerJanitorThread");
        runner.start();
    }

    /////////////////////////////////////////////////////////////////
    // The main loop
    /////////////////////////////////////////////////////////////////
    public void run() {
        // Declaration
        Vector<Player> players;
        Player sentPlayer;
        // Loop indefinitely, checking idle users
        int cycle = 0;
        int timer;
        while (true) {
            try {
                players = server.getPlayers();
                for (Player player : players) {
                    player.getUser().checkIdle();
                }

                updateRankings(players);

                server.getDB().ping();
                Thread.sleep(INACTIVITY_TIMEOUT_MILLIS);
            } catch (Exception e) {
                Log.error("Janitor: " + e);
            }
        }
    }

    private void updateRankings(Vector<Player> players) {
        int cycle = 0;
        int timer = ((players.size()) / 100) + 1;

        if (cycle > timer) {
            for (Player sentPlayer : players) {
                for (Player player : players) {
                    player.getUser().getChat().updateRating(sentPlayer);
                }
            }
            cycle = 0;
        } else {
            cycle++;
        }
    }
}
