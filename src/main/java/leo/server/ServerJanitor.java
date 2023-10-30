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

// imports

import leo.shared.Log;

import java.util.Vector;


public class ServerJanitor implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
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
        Vector players;
        Player sentPlayer;
        // Loop indefinately, checking idle users
        int cycle = 0;
        int timer;
        while (true) {
            try {
                players = server.getPlayers();
                for (int i = 0; i < players.size(); i++) {
                    Player player = (Player) players.elementAt(i);
                    player.getUser().checkIdle();
                }
                // Timer = how many ping calls between rank/rating updates
                timer = ((players.size()) / 100) + 1;
                if (cycle > timer) {
                    //Updates the rank/rating lists for each client's char menu
                    for (int i = 0; i < players.size(); i++) {
                        sentPlayer = (Player) players.elementAt(i);
                        for (int j = 0; j < players.size(); j++) {
                            ((Player) players.elementAt(j)).getUser().getChat().updateRating(sentPlayer);
                        }
                    }
                    cycle = 0;
                } else {
                    cycle++;
                }
                server.getDB().ping();
                Thread.sleep(60000);
            } catch (Exception e) {
                Log.error("Janitor: " + e);
            }
        }
    }
}
