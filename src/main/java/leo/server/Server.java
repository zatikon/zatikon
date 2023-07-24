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
    public static final long GOLD_TIMER = 90000;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static LoginServer loginServer;
    private static ChatServer chatServer;
    private static final Vector<Player> players = new Vector<Player>();
    private static final Vector<Player> logins = new Vector<Player>();
    private static Lobby lobby;
    private static LobbyDuel lobbyDuel;
    private static LobbyMirroredDuel lobbyMirrored;
    private static LobbyPractice lobbyPractice;
    private static LobbyCooperative lobbyCooperative;
    private static LobbyTeam lobbyTeam;
    private static final Random random = new Random();
    private static ServerJanitor janitor;
    private static DatabaseManager dbm;
    private static final Vector<ServerGame> gameListMulti = new Vector<ServerGame>();
    private static final Vector<PracticeGame> gameListSingle = new Vector<PracticeGame>();

    /////////////////////////////////////////////////////////////////
    // Main module
    /////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        var succeeded = new File(Constants.LOCAL_DIR).mkdirs();
        boolean useTls = !(Arrays.asList(args).contains(Constants.STANDALONE_ARG));

        try {
            dbm = new DatabaseManager();
            //dbm.initialize();
            //TopTen.main();

        } catch (Exception e) {
            Log.error("Server.main " + e);
        }

        Log.system("Booting Zatikon Server ver. " + Client.VERSION);
        loginServer = new LoginServer(Client.LOGIN_PORT, useTls);
        chatServer = new ChatServer(Client.CHAT_PORT, useTls);
        janitor = new ServerJanitor();
        lobby = new Lobby();
        lobbyDuel = new LobbyDuel();
        lobbyMirrored = new LobbyMirroredDuel();
        lobbyPractice = new LobbyPractice();
        lobbyCooperative = new LobbyCooperative();
        lobbyTeam = new LobbyTeam();

        //ratingDumpTemp();
    }

    private static void ratingDumpTemp() {
        System.out.println("Begining ratings dump...");
        dbm.getScoreDump();
        System.out.println("Dumping complete.");

    }

    /////////////////////////////////////////////////////////////////
    // Add a player to the logins
    /////////////////////////////////////////////////////////////////
    public static void addLogin(Player player) {
        logins.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the server
    /////////////////////////////////////////////////////////////////
    public static void add(Player player) {
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
    public static void removeLogin(Player player) {
        logins.remove(player);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a player
    /////////////////////////////////////////////////////////////////
    public static void remove(Player player) {
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
    public static void addToLobby(Player player) {
        lobby.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the Random Duel lobby
    /////////////////////////////////////////////////////////////////
    public static void addToDuelLobby(Player player) {
        lobbyDuel.add(player);
    }

    /////////////////////////////////////////////////////////////////
    // Add a player to the Mirrored Random lobby
    /////////////////////////////////////////////////////////////////
    public static void addToMirrDuelLobby(Player player) {
        lobbyMirrored.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public static void addToPracticeLobby(Player player) {
        lobbyPractice.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public static void addToCooperativeLobby(Player player) {
        lobbyCooperative.add(player);
    }


    /////////////////////////////////////////////////////////////////
    // Add a player to the lobby
    /////////////////////////////////////////////////////////////////
    public static void addToTeamLobby(Player player) {
        lobbyTeam.add(player);
    }

    public static void addToNextTeamGame(Player player, short team) {
        lobbyTeam.addToGame(player, team);
    }

    public static void startTeamGame() {
        lobbyTeam.startGame();
    }


    /////////////////////////////////////////////////////////////////
    // Check for player
    /////////////////////////////////////////////////////////////////
    public static Player checkForPlayer(String playerName) {
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
    public static short getPlayerCount() {
        return (byte) players.size();
    }


    /////////////////////////////////////////////////////////////////
    // Get the chat id
    /////////////////////////////////////////////////////////////////
    public static int getChatID() {
        return random.nextInt(30000);
    }


    /////////////////////////////////////////////////////////////////
    // Get the players
    /////////////////////////////////////////////////////////////////
    public static Vector<Player> getPlayers() {
        return players;
    }


    /////////////////////////////////////////////////////////////////
    // Get the players logging in
    /////////////////////////////////////////////////////////////////
    public static Player getPlayerChat(int id) {
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
    public static Vector<Player> getLogins() {
        return logins;
    }


    /////////////////////////////////////////////////////////////////
    // Send text to players
    /////////////////////////////////////////////////////////////////
    public static void sendText(Player sender, String rawMessage) {
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
    public static void sendRating(Player updatePlayer) {
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
    public static void sendState(Player updatePlayer, short sentState) {
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
    public static long getSeed() {
        return random.nextLong();
    }


    /////////////////////////////////////////////////////////////////
    // Randomy
    /////////////////////////////////////////////////////////////////
    public static Random random() {
        return random;
    }


    /////////////////////////////////////////////////////////////////
    // Get a random unit
    /////////////////////////////////////////////////////////////////
    public static short getRandomUnit() {
        int tier = getTier();
        while (tier > 8) tier = getTier();
        return Unit.UNITS[tier][random.nextInt(Unit.UNITS[tier].length)];
    }


    /////////////////////////////////////////////////////////////////
    // Get a random number
    /////////////////////////////////////////////////////////////////
    public static int getTier() {
        int tier = 0;
        while (random.nextInt(3) > 0)
            tier++;
        return tier;
    }


    /////////////////////////////////////////////////////////////////
    // Get a random unit of a specific tier
    /////////////////////////////////////////////////////////////////
    public static short getRandomUnit(short tier) {
        return Unit.UNITS[tier][random.nextInt(Unit.UNITS[tier].length)];
    }


    /////////////////////////////////////////////////////////////////
    // Get the database manager
    /////////////////////////////////////////////////////////////////
    public static DatabaseManager getDB() {
        return dbm;
    }

    /////////////////////////////////////////////////////////
    // Functions for the game list
    /////////////////////////////////////////////////////////
    public static Vector<ServerGame> gameListMulti() {
        return gameListMulti;
    }

    public static Vector<PracticeGame> gameListSingle() {
        return gameListSingle;
    }
}
