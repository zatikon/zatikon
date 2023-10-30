///////////////////////////////////////////////////////////////////////
// Name: LobbyTeam
// Desc: Players waiting for a team game
// Date: 4/3/2009 - Gabe Jones
//       New Match-Making System: 9/12/2011 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.lobby;

// imports

import leo.shared.Log;
import leo.server.Player;
import leo.server.Server;
import leo.server.game.TeamGame;
import leo.shared.Action;
import leo.shared.Unit;

import java.util.Stack;


public class LobbyTeam implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private final Stack<Player> players = new Stack<Player>();
    private final Stack<Player> removes = new Stack<Player>();
    private final Server server;
    private Player T1P1 = null;
    private Player T1P2 = null;    // Players who have selected a team
    private Player T2P1 = null;
    private Player T2P2 = null;
    private boolean mutexBlock = false;    // Thread safe!
    private boolean mutexBlock2 = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LobbyTeam(Server server) {
        this.server = server;
        runner = new Thread(this, "LobbyTeamThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void add(Player newPlayer) {
        players.add(newPlayer);
        server.sendText(newPlayer, "*** " + newPlayer.getChatName() + " is looking for a team game ***");
        sendSetupToPlayer(newPlayer);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the next game
    /////////////////////////////////////////////////////////////////
    public void addToGame(Player player, short team) {
        if (player == null) {
            Log.error("Tried to add a null player to a team game");
            return;
        }
        if (!players.contains(player)) {
            Log.error("Tried to add player " + player.getName() + " to a team game who isn't in lobby");
            return;
        }
        if (mutexBlock) {
            Log.error("Tried to add player " + player.getName() + " while starting new game, which means no spots to add to");
            return;
        }

        // Wait for thread access
        while (mutexBlock2) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
        }

        // Remove them from any position they might have already held on one of the teams
        gameRemove(player);

        // Attempt to add the player to the team they requested
        switch (team) {
            case Unit.TEAM_1:
                if (T1P1 == null)
                    T1P1 = player;
                else if (T1P2 == null)
                    T1P2 = player;
                else {
                    Log.error("LobbyTeam: no spots to put player " + player.getName() + " on team 1");
                    return;
                }
                break;
            case Unit.TEAM_2:
                if (T2P1 == null)
                    T2P1 = player;
                else if (T2P2 == null)
                    T2P2 = player;
                else {
                    Log.error("LobbyTeam: no spots to put player " + player.getName() + " on team 2");
                    return;
                }
                break;
            default:   // if not team 1 or team 2, will just remove player from any position
                break;
        }

        // Send the new team setup
        sendTeamSetup();
    }


    /////////////////////////////////////////////////////////////////
    // Main thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        while (true) {
            try {

                // Check for players who have cancelled out of the lobby
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.elementAt(i);
                    if (player.getUser().isClosed()) remove(player);
                    else if (player.getUser().cancelled()) {
                        remove(player);
                        player.getUser().sendAction(Action.CANCEL, Action.NOTHING, Action.NOTHING);
                    }
                }

                while (removes.size() > 0) {
                    Player player = removes.pop();
                    players.remove(player);
                }

                // Old game start code, before selecting teams was implemented
   /*while (players.size() > 3)
   { Player player1 = players.pop();
    Player player2 = players.pop();
    Player player3 = players.pop();
    Player player4 = players.pop();

    TeamGame tg = new TeamGame(player1, player3, player2, player4);

    player1.getUser().sendText(Action.CHAT_WHISPER, player2, "Hi, I'm your ally in a team game.");
    player2.getUser().sendText(Action.CHAT_WHISPER, player1, "Hi, I'm your ally in a team game.");

    player3.getUser().sendText(Action.CHAT_WHISPER, player4, "Hi, I'm your ally in a team game.");
    player4.getUser().sendText(Action.CHAT_WHISPER, player3, "Hi, I'm your ally in a team game.");
   }*/

                Thread.sleep(5000);
            } catch (Exception e) {
                Log.error("LobbyTeam.run" + e);
            }
        }
    }


    //////////////////////////////////////////////////////////////////////
    // Teams are full and someone clicked the middle button to start the game
    //////////////////////////////////////////////////////////////////////
    public void startGame() {
        // Make sure teams are full
        if (T1P1 != null && T1P2 != null && T2P1 != null && T2P2 != null) {

            // Block mutex so no adding or removing from teams allowed while starting game
            mutexBlock = true;
            players.remove(T1P1);
            players.remove(T1P2);
            players.remove(T2P1);
            players.remove(T2P2);

            // Create the game
            TeamGame tg = new TeamGame(server, T1P1, T2P1, T1P2, T2P2);
            T1P1.getUser().sendText(Action.CHAT_WHISPER, T1P2, "Hi, I'm your ally in a team game.");
            T1P2.getUser().sendText(Action.CHAT_WHISPER, T1P1, "Hi, I'm your ally in a team game.");
            T2P1.getUser().sendText(Action.CHAT_WHISPER, T2P2, "Hi, I'm your ally in a team game.");
            T2P2.getUser().sendText(Action.CHAT_WHISPER, T2P1, "Hi, I'm your ally in a team game.");
            T1P1 = null;
            T1P2 = null;
            T2P1 = null;
            T2P2 = null;

            // Unblock mutex and Clear the team setup for clients
            mutexBlock = false;
            sendTeamSetup();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove a player from the lobby
    /////////////////////////////////////////////////////////////////
    public void remove(Player player) {
        removes.add(player);
        gameRemove(player);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a player from any team they may be listed under
    /////////////////////////////////////////////////////////////////
    private void gameRemove(Player player) {
        // Can't do while starting game, and afterwards teams will be empty
        if (!mutexBlock) {

            // Wait for access, remove while sending team
            while (mutexBlock2) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
            if (T1P1 == player) {
                T1P1 = null;
                T1P1 = T1P2;
                T1P2 = null;
            } else if (T1P2 == player)
                T1P2 = null;
            else if (T2P1 == player) {
                T2P1 = null;
                T2P1 = T2P2;
                T2P2 = null;
            } else if (T2P2 == player)
                T2P2 = null;

            // Send the new team setup to the client
            sendTeamSetup();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send the current team setup to the clients
    /////////////////////////////////////////////////////////////////
    public void sendTeamSetup() {
        for (int i = 0; i < players.size(); ++i) {
            Player player = players.elementAt(i);
            sendSetupToPlayer(player);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send the team setup to one player
    /////////////////////////////////////////////////////////////////
    private void sendSetupToPlayer(Player player) {
        // Aquire mutex so removing/adding players has to wait until after data is gathered and sent
        // Can still create a game though...
        mutexBlock2 = true;

        // Clear the teams
        player.getUser().sendAction(Action.CLEAR_TEAM, Action.NOTHING, Action.NOTHING);

        // Send the current teams (-1 is used as a null chat id, eg there is no player in that slot)
        player.getUser().sendAction(Action.SELECT_TEAM, (byte) 1, Unit.TEAM_1);
        if (T1P1 != null)
            player.getUser().sendInt(T1P1.getUser().getChatID());
        else
            player.getUser().sendInt(-1);

        player.getUser().sendAction(Action.SELECT_TEAM, (byte) 2, Unit.TEAM_1);
        if (T1P2 != null)
            player.getUser().sendInt(T1P2.getUser().getChatID());
        else
            player.getUser().sendInt(-1);

        player.getUser().sendAction(Action.SELECT_TEAM, (byte) 1, Unit.TEAM_2);
        if (T2P1 != null)
            player.getUser().sendInt(T2P1.getUser().getChatID());
        else
            player.getUser().sendInt(-1);

        player.getUser().sendAction(Action.SELECT_TEAM, (byte) 2, Unit.TEAM_2);
        if (T2P2 != null)
            player.getUser().sendInt(T2P2.getUser().getChatID());
        else
            player.getUser().sendInt(-1);

        // Release mutex
        mutexBlock2 = false;
    }
}
