///////////////////////////////////////////////////////////////////////
// Name: Server
// Desc: Where the server starts
// Date: 5/8/2003 - Gabe Jones
//     10/19/2010 - Tony Schwartz
//   Updates: Added MirroredRandom Lobby
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.client.Client;
import leo.server.game.PracticeGame;
import leo.server.game.ServerGame;
import leo.server.lobby.*;
import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Log;
import leo.shared.Unit;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class Server {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    private static Server instance = null;
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final LoginServer loginServer;
    private final ChatServer chatServer;
    private final Vector<Player> players = new Vector<Player>();
    private final Vector<Player> logins = new Vector<Player>();
    private final Lobby lobby;
    private final LobbyDuel lobbyDuel;
    private final LobbyMirroredDuel lobbyMirrored;
    private final LobbyPractice lobbyPractice;
    private final LobbyCooperative lobbyCooperative;
    private final LobbyTeam lobbyTeam;
    private final Random random = new Random();
    private final ServerJanitor janitor;
    private final DatabaseManager dbm;
    private final Vector<ServerGame> gameListMulti = new Vector<ServerGame>();
    private final Vector<PracticeGame> gameListSingle = new Vector<PracticeGame>();

    /////////////////////////////////////////////////////////////////
    // Main module
    /////////////////////////////////////////////////////////////////

    public Server(boolean useTls) {
        dbm = new DatabaseManager();
        //TopTen.main();

        Log.system("Booting Zatikon Server ver. " + Client.VERSION);
        loginServer = new LoginServer(this, Client.LOGIN_PORT, useTls);
        chatServer = new ChatServer(this, Client.CHAT_PORT, useTls);
        janitor = new ServerJanitor(this);
        lobby = new Lobby(this);
        lobbyDuel = new LobbyDuel(this);
        lobbyMirrored = new LobbyMirroredDuel(this);
        lobbyPractice = new LobbyPractice(this);
        lobbyCooperative = new LobbyCooperative(this);
        lobbyTeam = new LobbyTeam(this);
    }

    public static void main(String[] args) {
        var succeeded = new File(Constants.LOCAL_DIR).mkdirs();
        boolean useTls = !(Arrays.asList(args).contains(Constants.STANDALONE_ARG));

        instance = new Server(useTls);
    }

//    private void ratingDumpTemp() {
//        System.out.println("Beginning ratings dump...");
//        dbm.getScoreDump();
//        System.out.println("Dumping complete.");
//    }

    public boolean isReady() {
        return loginServer.isReady() && chatServer.isReady();
    }

    /////////////////////////////////////////////////////////////////
    // Add a player to the logins
    /////////////////////////////////////////////////////////////////
    public void addLogin(Player player) {
        logins.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the server
    /////////////////////////////////////////////////////////////////
    public void add(Player player) {
        // Add the new player
        players.add(player);

        // Find a player with the right chat ID
        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player tmp = it.next();
            tmp.getUser().sendPlayer(player);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove a player from logins
    /////////////////////////////////////////////////////////////////
    public void removeLogin(Player player) {
        logins.remove(player);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a player
    /////////////////////////////////////////////////////////////////
    public void remove(Player player) {
        players.remove(player);

        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player tmp = it.next();
            tmp.getUser().removePlayer(player);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void addToLobby(Player player) {
        lobby.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the Random Duel lobby
    /////////////////////////////////////////////////////////////////
    public void addToDuelLobby(Player player) {
        lobbyDuel.add(player);
    }

    /////////////////////////////////////////////////////////////////
    // Add a player to the Mirrored Random lobby
    /////////////////////////////////////////////////////////////////
    public void addToMirrDuelLobby(Player player) {
        lobbyMirrored.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void addToPracticeLobby(Player player) {
        lobbyPractice.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void addToCooperativeLobby(Player player) {
        lobbyCooperative.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public void addToTeamLobby(Player player) {
        lobbyTeam.add(player);
    }

    public void addToNextTeamGame(Player player, short team) {
        lobbyTeam.addToGame(player, team);
    }

    public void startTeamGame() {
        lobbyTeam.startGame();
    }


    /////////////////////////////////////////////////////////////////
    // Check for player
    /////////////////////////////////////////////////////////////////
    public Player checkForPlayer(String playerName) {
        // Check for a player
        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player player = it.next();

            if (player.getName().equals(playerName))
                return player;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Get the player count
    /////////////////////////////////////////////////////////////////
    public short getPlayerCount() {
        return (byte) players.size();
    }


    /////////////////////////////////////////////////////////////////
    // Get the chat id
    /////////////////////////////////////////////////////////////////
    public int getChatID() {
        return random.nextInt(30000);
    }


    /////////////////////////////////////////////////////////////////
    // Get the players
    /////////////////////////////////////////////////////////////////
    public Vector<Player> getPlayers() {
        return players;
    }


    /////////////////////////////////////////////////////////////////
    // Get the players logging in
    /////////////////////////////////////////////////////////////////
    public Player getPlayerChat(int id) {
        // Find a player with the right chat ID
        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            if (player.getUser().getChatID() == id)
                return player;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Get the players logging in
    /////////////////////////////////////////////////////////////////
    public Vector<Player> getLogins() {
        return logins;
    }


    /////////////////////////////////////////////////////////////////
    // Send text to players
    /////////////////////////////////////////////////////////////////
    public void sendText(Player sender, String rawMessage) {
        String message = rawMessage;

        // is it an admin announcement?
        boolean announcement = (sender != null && sender.admin() && message.charAt(0) == '#');

        // Send text
        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player tmp = it.next();
            //if (!tmp.playing() && tmp != sender) tmp.getUser().sendText(message);

            if (announcement) {
                if (message.charAt(0) == '#') message = message.substring(1);
                tmp.getUser().getChat().sendMessage(message);
                tmp.getUser().sendText(message);
            } else if (tmp != sender) {
                tmp.getUser().sendText(message);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a rating update
    /////////////////////////////////////////////////////////////////
    public void sendRating(Player updatePlayer) {
        // Find a player with the right chat ID
        Vector<Player> players = getPlayers();
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            player.getUser().updateRating(updatePlayer);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send a rating update
    /////////////////////////////////////////////////////////////////
    public void sendState(Player updatePlayer, short sentState) {
        try {
            short newState = sentState;
            if (newState == Action.CHAT_CHATTING) {
                boolean crusades = updatePlayer.access(Unit.CRUSADES);
                boolean legions = updatePlayer.access(Unit.LEGIONS);

                if (crusades && legions)
                    newState = Action.CHAT_CRU_LEG;
                else if (crusades && !legions)
                    newState = Action.CHAT_CRU;
                else if (!crusades && legions)
                    newState = Action.CHAT_LEG;
            }
            // Find a player with the right chat ID
            Vector<Player> players = getPlayers();
            Iterator<Player> it = players.iterator();
            while (it.hasNext()) {
                Player player = it.next();
                player.getUser().sendState(updatePlayer, newState);
            }
        } catch (Exception e) {
            Log.error("Server.sendState " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get a seed
    /////////////////////////////////////////////////////////////////
    public long getSeed() {
        return random.nextLong();
    }


    /////////////////////////////////////////////////////////////////
    // Random
    /////////////////////////////////////////////////////////////////
    public Random random() {
        return random;
    }


    /////////////////////////////////////////////////////////////////
    // Get a random unit
    /////////////////////////////////////////////////////////////////
    public short getRandomUnit() {
        int tier = getTier();
        while (tier > 8) tier = getTier();
        return Unit.UNITS[tier][random.nextInt(Unit.UNITS[tier].length)];
    }


    /////////////////////////////////////////////////////////////////
    // Get a random number
    /////////////////////////////////////////////////////////////////
    public int getTier() {
        int tier = 0;
        while (random.nextInt(3) > 0)
            tier++;
        return tier;
    }


    /////////////////////////////////////////////////////////////////
    // Get a random unit of a specific tier
    /////////////////////////////////////////////////////////////////
    public short getRandomUnit(short tier) {
        return Unit.UNITS[tier][random.nextInt(Unit.UNITS[tier].length)];
    }


    /////////////////////////////////////////////////////////////////
    // Get the database manager
    /////////////////////////////////////////////////////////////////
    public DatabaseManager getDB() {
        return dbm;
    }

    /////////////////////////////////////////////////////////
    // Functions for the game list
    /////////////////////////////////////////////////////////
    public Vector<ServerGame> gameListMulti() {
        return gameListMulti;
    }

    public Vector<PracticeGame> gameListSingle() {
        return gameListSingle;
    }
}
