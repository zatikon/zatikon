///////////////////////////////////////////////////////////////////////
//	Name:	TeamGame
//	Desc:	2v2
//	Date:	3/27/2009
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.game;

// imports

import leo.server.*;
import leo.server.observers.CoopObserver;
import leo.shared.*;

import java.util.Random;
import java.util.Vector;


public class TeamGame implements Game {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int MIN_TURNS = 4;
    private static final int MIN_ACTIONS = 4;
    private final Server server;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean over = false;
    private final Player player1;
    private final Player player2;
    private final Player player3;
    private final Player player4;
    private Castle player1Castle;
    private Castle player2Castle;
    private Castle player3Castle;
    private Castle player4Castle;
    private Castle whiteCastle;
    private Castle redCastle;
    private Castle currentCastle;
    private BattleField battleField;
    private CoopObserver observer;
    private int turns = 0;
    private int team1Actions;
    private int team2Actions;
    private Random random;
    private final long seed;
    private boolean rebuilding = false;
    private final Vector<GameAction> actions = new Vector<GameAction>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TeamGame(Server server, Player newPlayer1, Player newPlayer2, Player newPlayer3, Player newPlayer4) {
        this.server = server;
        // get a new seed
        seed = server.getSeed();

        // set some variables
        player1 = newPlayer1;
        player2 = newPlayer2;
        player3 = newPlayer3;
        player4 = newPlayer4;

        // initialize the server state
        initialize();

        // Log it
        printCastles();

        // send the player stuff
        sendInitialization();
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void initialize() {
        turns = 0;
        team1Actions = 0;
        team2Actions = 0;

        // set the random to the constructed seed
        random = new Random(seed);

        // send game initializations
        player1.initializeGame(Action.GAME_CONSTRUCTED);
        player2.initializeGame(Action.GAME_CONSTRUCTED);
        player3.initializeGame(Action.GAME_CONSTRUCTED);
        player4.initializeGame(Action.GAME_CONSTRUCTED);

        // set the castles up
        player1Castle = player1.getCurrentCastle();
        player2Castle = player2.getCurrentCastle();
        player3Castle = player3.getCurrentCastle();
        player4Castle = player4.getCurrentCastle();

        // set the teams
        player1Castle.setTeam(Unit.TEAM_1);
        player2Castle.setTeam(Unit.TEAM_2);
        player3Castle.setTeam(Unit.TEAM_1);
        player4Castle.setTeam(Unit.TEAM_2);

        // set the combined castles
        whiteCastle = new Castle();
        redCastle = new Castle();

        // set the current castle
        currentCastle = whiteCastle;

        // initialize the battlefield
        battleField = new BattleField(whiteCastle, redCastle);
        whiteCastle.setBattleField(battleField);
        redCastle.setBattleField(battleField);

        // set the castle locations
        whiteCastle.setLocation(BattleField.getLocation((byte) 5, (byte) 10));
        redCastle.setLocation(BattleField.getLocation((byte) 5, (byte) 0));

        // set up the observer
        observer = new CoopObserver(this);
        whiteCastle.setObserver(observer);
        redCastle.setObserver(observer);

        // set up the game objects for the players
        player1.getUser().startGame(this);
        player2.getUser().startGame(this);
        player3.getUser().startGame(this);
        player4.getUser().startGame(this);

        // refresh the castles
        whiteCastle.refresh(Unit.TEAM_1);
        redCastle.refresh(Unit.TEAM_1);
        whiteCastle.refresh(Unit.TEAM_2);
        redCastle.refresh(Unit.TEAM_2);
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void sendInitialization() {
        if (!rebuilding) {
            // spawn the client side games
            player1.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);

            // Send the users
            player1.getUser().sendTeam(player2, player4);
            player2.getUser().sendTeam(player1, player3);
            player3.getUser().sendTeam(player2, player4);
            player4.getUser().sendTeam(player1, player3);
        }

        // send their castles
        player1.getUser().sendCastle(player1.getCurrentCastle());
        player2.getUser().sendCastle(player2.getCurrentCastle());
        player3.getUser().sendCastle(player3.getCurrentCastle());
        player4.getUser().sendCastle(player4.getCurrentCastle());


        // kick off the game
        if (!rebuilding) {
            player1.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
        }
		/*player1.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
		player2.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
		player3.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
		player4.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);*/


        // Start the game
        player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
        player3.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);
        player4.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);

        finishInitCheck();
    }

    private void finishInitCheck() {
        if (player1.getUser().isClosed()) {
            Log.game("Player " + player1.getChatName() + " left after pairing but before game began");
            over = true;
            player2.getUser().endGame();
            player3.getUser().endGame();
            player4.getUser().endGame();
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
        } else if (player2.getUser().isClosed()) {
            Log.game("Player " + player2.getChatName() + " left after pairing but before game began");
            over = true;
            player1.getUser().endGame();
            player3.getUser().endGame();
            player4.getUser().endGame();
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
        } else if (player3.getUser().isClosed()) {
            Log.game("Player " + player3.getChatName() + " left after pairing but before game began");
            over = true;
            player1.getUser().endGame();
            player2.getUser().endGame();
            player4.getUser().endGame();
            player1.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
        } else if (player4.getUser().isClosed()) {
            Log.game("Player " + player4.getChatName() + " left after pairing but before game began");
            over = true;
            player1.getUser().endGame();
            player2.getUser().endGame();
            player3.getUser().endGame();
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
        } else {
            sendMessage(player1.getChatName() + " is now playing");
            sendMessage(player2.getChatName() + " will be next");
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send text. TODO: have fun with this
    /////////////////////////////////////////////////////////////////
    public void sendText(Player sender, String message) {
    }


    /////////////////////////////////////////////////////////////////
    // Send text. TODO: have fun with this
    /////////////////////////////////////////////////////////////////
    public void sendMessage(String message) {
        player1.getUser().getChat().sendMessage(message);
        player2.getUser().getChat().sendMessage(message);
        player3.getUser().getChat().sendMessage(message);
        player4.getUser().getChat().sendMessage(message);
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    private boolean validGame() {
        return turns > MIN_TURNS && team1Actions > MIN_ACTIONS && team2Actions > MIN_ACTIONS;
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame(Castle winner) {
        over = true;
        player1.getUser().endGame();
        player2.getUser().endGame();
        player3.getUser().endGame();
        player4.getUser().endGame();

        if (validGame()) {
            if (winner == whiteCastle) {
                // do some updates
                if (player1.win(server, player2, false))
                    player1.wipeStamp();
                player1.win(server, player4, false);
                if (player2.lose(server, player1, false))
                    player2.wipeStamp();
                player2.lose(server, player3, false);
                if (player3.win(server, player2, false))
                    player3.wipeStamp();
                player3.win(server, player4, false);
                if (player4.lose(server, player1, false))
                    player4.wipeStamp();
                player4.lose(server, player3, false);
                Log.game(player1.getName() + " & " + player3.getName() + " defeated " + player2.getName() + " & " + player4.getName());
            }

            if (winner == redCastle) {
                // do some updates
                if (player1.lose(server, player2, false))
                    player1.wipeStamp();
                player1.lose(server, player4, false);
                if (player2.win(server, player1, false))
                    player2.wipeStamp();
                player2.win(server, player3, false);
                if (player3.lose(server, player2, false))
                    player3.wipeStamp();
                player3.lose(server, player4, false);
                if (player4.win(server, player1, false))
                    player4.wipeStamp();
                player4.win(server, player3, false);
                Log.game(player2.getName() + " & " + player4.getName() + " defeated " + player1.getName() + " & " + player3.getName());
            }

        } else {
            Log.alert("Weird game: " + player1.getName() + " & " + player3.getName() + " vs " + player2.getName() + " & " + player4.getName());
        }

        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player3.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player4.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }


    /////////////////////////////////////////////////////////////////
    // Draw game
    /////////////////////////////////////////////////////////////////
    public void drawGame() {
        over = true;

        if (validGame()) {
            Log.game("Draw game: " + player1.getName() + " & " + player3.getName() + " vs " + player2.getName() + " & " + player4.getName());
            player1.draw(player2);
            player1.draw(player4);
            player2.draw(player1);
            player2.draw(player3);
            player3.draw(player2);
            player3.draw(player4);
            player4.draw(player1);
            player4.draw(player3);
        }

        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player3.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player4.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        player1.getUser().endGame();
        player2.getUser().endGame();
        player1.getUser().endGame();
        player2.getUser().endGame();
    }


    /////////////////////////////////////////////////////////////////
    // Disconnect a player
    /////////////////////////////////////////////////////////////////
    public void interrupt(Player player) {
        if (player != player1) player1.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
        if (player != player2) player2.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
        if (player != player3) player3.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
        if (player != player4) player4.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
    }


    /////////////////////////////////////////////////////////////////
    // Disconnect a player
    /////////////////////////////////////////////////////////////////
    public void disconnect(Player player) {
        if (over) return;
        over = true;

        if (player == player1) {
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(redCastle);
        }
        if (player == player2) {
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(whiteCastle);
        }
        if (player == player3) {
            player1.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(redCastle);
        }
        if (player == player4) {
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(whiteCastle);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Resynch the game
    /////////////////////////////////////////////////////////////////
    public void resynch() {
        if (rebuilding) return;

        try {
            over = true;
            rebuilding = true;

            // send inform the players
            // temp version is sendInitialization
            player1.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);

            Thread.sleep(2000);

            player1.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);

            // reinitialize
            over = false;
            initialize();

            sendInitialization();

            for (int i = 0; i < actions.size(); i++) {
                GameAction action = actions.elementAt(i);
                processAction(
                        action.getPlayer(),
                        action.getAction(),
                        action.getActor(),
                        action.getTarget());
            }
            rebuilding = false;
            player1.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);
            player3.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);
            player4.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);

        } catch (Exception e) {
            Log.error("ServerGame.resynch " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Interpret action
    /////////////////////////////////////////////////////////////////
    public void interpretAction(Player player, short action, short actorLocation, short target) throws Exception {
        try {
            if (rebuilding && player != null) return;
            processAction(player, action, actorLocation, target);
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Invert a location
    /////////////////////////////////////////////////////////////////
    private short invert(short location) {
        return BattleField.getLocation((byte) (10 - BattleField.getX(location)), (byte) (10 - BattleField.getY(location)));
    }


    /////////////////////////////////////////////////////////////////
    // Process the action
    /////////////////////////////////////////////////////////////////
    public void processAction(Player player, short action, short actorLocation, short target) throws Exception {
        if (action == Action.NOTHING) return;

        if (rebuilding) {
            player.getUser().sendAction(action, actorLocation, target);
        } else {
            actions.add(new GameAction(player, action, actorLocation, target));
        }

        short tmpActorLocation = actorLocation;
        short tmpTarget = target;

        if (player == player2 || player == player4) {
            tmpActorLocation = invert(actorLocation);
            tmpTarget = invert(target);
        }

        // Unit actions
        if (action < 30) {
            Unit actor = battleField.getUnitAt(tmpActorLocation);

            // Make sure the unit exists
            if (actor == null) {
                Log.error("Player " + player.getName() + " sent an invalid actor location");
                return;
            }


            // Make sure it belongs to them
            //if (actor.getCastle() != player.getCurrentCastle())
            //{	Log.error("Player " + player.getName() + " tried to use a unit that's not their's.");
            //	return;
            //}


            // Do it
            Log.game(player.getName() + ": " + actor.performAction(action, tmpTarget));

            if (player == player1) {
                team1Actions++;
                tmpActorLocation = invert(actorLocation);
                tmpTarget = invert(target);

                // enemies
                player2.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                player4.getUser().sendAction(action, tmpActorLocation, tmpTarget);

                // ally
                player3.getUser().sendAction(action, actorLocation, target);
            }
            if (player == player2) {
                team2Actions++;

                // enemies
                player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                player3.getUser().sendAction(action, tmpActorLocation, tmpTarget);

                // ally
                player4.getUser().sendAction(action, actorLocation, target);
            }
            if (player == player3) {
                team1Actions++;
                tmpActorLocation = invert(actorLocation);
                tmpTarget = invert(target);

                // enemies
                player2.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                player4.getUser().sendAction(action, tmpActorLocation, tmpTarget);

                // ally
                player1.getUser().sendAction(action, actorLocation, target);
            }
            if (player == player4) {
                team2Actions++;

                // enemies
                player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                player3.getUser().sendAction(action, tmpActorLocation, tmpTarget);

                // ally
                player2.getUser().sendAction(action, actorLocation, target);
            }
            return;
        }

        // Everything else
        switch (action) {

            case Action.DEPLOY:

                Castle playerCastle = getCastle(player);
                Castle teamCastle = getTeamCastle(player);
                Unit unit = playerCastle.getUnit(actorLocation);

                // move the unit to the right castle
                playerCastle.remove(unit);
                unit.setCastle(teamCastle);
                teamCastle.add(unit);

                // No unit there?
                if (unit == null) {
                    Log.error("Player " + player.getName() + " attempted to deploy a non-existent unit: " + actorLocation);
                    return;
                }

                // Make sure it's placed in a valid location
                Vector targets = unit.getCastleTargets();
                if (!validateTarget(targets, tmpTarget)) {
                    Log.error("Player " + player.getName() + " attempted to deploy to an invalid location.");
                    return;
                }

                // send to unit to the other guy
                if (player == player1) {
                    tmpTarget = invert(target);

                    // ally
                    player3.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), target);

                    // enemies
                    player2.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);
                    player4.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);

                    // do it
                    Log.game(player.getName() + ": " + whiteCastle.deploy(Unit.TEAM_1, unit, target));
                } else if (player == player2) {
                    // ally
                    player4.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), target);

                    // enemies
                    player1.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);
                    player3.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);

                    // do it
                    Log.game(player.getName() + ": " + redCastle.deploy(Unit.TEAM_1, unit, tmpTarget));
                }
                if (player == player3) {
                    tmpTarget = invert(target);

                    // ally
                    player1.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), target);

                    // enemies
                    player2.getUser().sendAction(Action.DEPLOY_ENEMY_ALLY, unit.getID(), tmpTarget);
                    player4.getUser().sendAction(Action.DEPLOY_ENEMY_ALLY, unit.getID(), tmpTarget);

                    // do it
                    Log.game(player.getName() + ": " + whiteCastle.deploy(Unit.TEAM_2, unit, target));
                } else if (player == player4) {
                    // ally
                    player2.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), target);

                    // enemies
                    player1.getUser().sendAction(Action.DEPLOY_ENEMY_ALLY, unit.getID(), tmpTarget);
                    player3.getUser().sendAction(Action.DEPLOY_ENEMY_ALLY, unit.getID(), tmpTarget);

                    // do it
                    Log.game(player.getName() + ": " + redCastle.deploy(Unit.TEAM_2, unit, tmpTarget));
                }
                break;


            case Action.END_TURN:
                turns++;

                // move the turn to the next guy
                if (player == player1) {
                    sendMessage(player2.getChatName() + " is now playing");
                    sendMessage(player3.getChatName() + " will be next");

                    whiteCastle.refresh(Unit.TEAM_1);
                    redCastle.startTurn(Unit.TEAM_1);
                    currentCastle = redCastle;

                    player1.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);

                }

                // move the turn to the next guy
                if (player == player2) {
                    sendMessage(player3.getChatName() + " is now playing");
                    sendMessage(player4.getChatName() + " will be next");

                    redCastle.refresh(Unit.TEAM_1);
                    whiteCastle.startTurn(Unit.TEAM_2);
                    currentCastle = whiteCastle;

                    player1.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.START_TURN_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                }

                // move the turn to the next guy
                if (player == player3) {
                    sendMessage(player4.getChatName() + " is now playing");
                    sendMessage(player1.getChatName() + " will be next");

                    whiteCastle.refresh(Unit.TEAM_2);
                    redCastle.startTurn(Unit.TEAM_2);
                    currentCastle = redCastle;

                    player1.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.START_TURN_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.REFRESH_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                }

                // move the turn to the next guy
                if (player == player4) {
                    sendMessage(player1.getChatName() + " is now playing");
                    sendMessage(player2.getChatName() + " will be next");

                    redCastle.refresh(Unit.TEAM_2);
                    whiteCastle.startTurn(Unit.TEAM_1);
                    currentCastle = whiteCastle;

                    player1.getUser().sendAction(Action.REFRESH_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.REFRESH_ENEMY_ALLY, Action.NOTHING, Action.NOTHING);
                    player3.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player4.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                }

                break;

            case Action.OFFER_DRAW:
                break;

            case Action.SURRENDER:
                disconnect(player);
                break;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Make sure a taget is within the tagetting vector
    /////////////////////////////////////////////////////////////////
    private boolean validateTarget(Vector targets, short target) {
        for (int i = 0; i < targets.size(); i++) {
            Short location = (Short) targets.elementAt(i);
            if (location.byteValue() == target) return true;
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Print castle
    /////////////////////////////////////////////////////////////////
    private void printCastles() {
        String output = "";
        Vector<UndeployedUnit> barracks;

        Log.game("Team 1! Castle for: " + player1.getName());
        barracks = player1Castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
        output = "";
        Log.game("Team 1! Castle for: " + player3.getName());
        barracks = player3Castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
        output = "";

        Log.game("Team 2! Castle for: " + player2.getName());
        barracks = player2Castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
        Log.game("Team 2! Castle for: " + player4.getName());
        barracks = player4Castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);

    }


    /////////////////////////////////////////////////////////////////
    // get the current player
    /////////////////////////////////////////////////////////////////
    public Player getCurrentPlayer() {
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Get player
    /////////////////////////////////////////////////////////////////
    public Player getPlayer(Castle castle) {
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Game over man
    /////////////////////////////////////////////////////////////////
    public boolean over() {
        return over;
    }


    /////////////////////////////////////////////////////////////////
    // Banish
    /////////////////////////////////////////////////////////////////
    public void banish(Unit unit) {
        if (unit.getCastle() == whiteCastle) {
            whiteCastle.clear();
            if (unit.getTeam() == Unit.TEAM_1) {
                player1Castle.add(Unit.getUnit(unit.getID(), player1Castle));
            } else if (unit.getTeam() == Unit.TEAM_2) {
                player3Castle.add(Unit.getUnit(unit.getID(), player3Castle));
            }
        }
        if (unit.getCastle() == redCastle) {
            redCastle.clear();
            if (unit.getTeam() == Unit.TEAM_1) {
                player2Castle.add(Unit.getUnit(unit.getID(), player2Castle));
            } else if (unit.getTeam() == Unit.TEAM_2) {
                player4Castle.add(Unit.getUnit(unit.getID(), player4Castle));
            }
        }

    }


    /////////////////////////////////////////////////////////////////
    // Get player's castle
    /////////////////////////////////////////////////////////////////
    public Castle getCastle(Player player) {
        if (player == player1) {
            return player1Castle;
        } else if (player == player2) {
            return player2Castle;
        } else if (player == player3) {
            return player3Castle;
        } else if (player == player4) {
            return player4Castle;
        }
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // get the combo castle
    /////////////////////////////////////////////////////////////////
    public Castle getTeamCastle(Player player) {
        if (player == player1 || player == player3) {
            return whiteCastle;
        } else {
            return redCastle;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get player's castle
    /////////////////////////////////////////////////////////////////
    public Random random() {
        return random;
    }

    public int getDelay() {
        return rebuilding ? 0 : 500;
    }

}
