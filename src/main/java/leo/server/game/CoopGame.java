///////////////////////////////////////////////////////////////////////
//	Name:	CoopGame
//	Desc:	Co operative game
//	Date:	8/23/2008
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.game;

// imports

import leo.server.*;
import leo.server.observers.CoopObserver;
import leo.shared.*;

import java.util.Random;
import java.util.Vector;


public class CoopGame implements Game {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean newbie = false;
    private boolean over = false;
    private AI ai;
    private final Player player1;
    private final Player player2;
    private Castle player1Castle;
    private Castle player2Castle;
    private Castle playerCastle;
    private Castle AICastle;
    private Castle currentCastle;
    private BattleField battleField;
    private CoopObserver observer;
    private int turns = 0;
    private final long seed;
    private Random random;
    private boolean rebuilding = false;
    private final Vector<GameAction> actions = new Vector<GameAction>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CoopGame(Player newPlayer1, Player newPlayer2) {
        // Get a new seed
        seed = Server.getSeed();

        // set some variables
        player1 = newPlayer1;
        player2 = newPlayer2;

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
        random = new Random(seed);

        player1.initializeGame(Action.GAME_PRACTICE);
        player2.initializeGame(Action.GAME_PRACTICE);

        player1Castle = player1.getCurrentCastle();
        player2Castle = player2.getCurrentCastle();
        player1Castle.setTeam(Unit.TEAM_1);
        player2Castle.setTeam(Unit.TEAM_2);
        playerCastle = new Castle();

        AICastle = new Castle();
        AICastle.ai();

        currentCastle = playerCastle;
        battleField = new BattleField(playerCastle, AICastle);
        playerCastle.setBattleField(battleField);
        AICastle.setBattleField(battleField);
        playerCastle.setLocation(BattleField.getLocation((byte) 5, (byte) 10));
        AICastle.setLocation(BattleField.getLocation((byte) 5, (byte) 0));
        observer = new CoopObserver(this);
        playerCastle.setObserver(observer);
        AICastle.setObserver(observer);
        player1.getUser().startGame(this);
        player2.getUser().startGame(this);
        playerCastle.refresh(Unit.TEAM_1);
        playerCastle.refresh(Unit.TEAM_2);
        AICastle.refresh(Unit.TEAM_1);

        // Prepare the AI
        newbie = player1.getLevel() < 15 || player2.getLevel() < 15;
        ai = new AI(player1.getLevel() + player2.getLevel(), this, battleField, AICastle, playerCastle);

    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game state
    /////////////////////////////////////////////////////////////////
    public void sendInitialization() {
        if (!rebuilding) {
            player1.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);

            // Send the users
            player1.getUser().sendComputer(player1.getLevel() + player2.getLevel());
            player2.getUser().sendComputer(player1.getLevel() + player2.getLevel());
        }

        player1.getUser().sendCastle(player1.getCurrentCastle());
        player2.getUser().sendCastle(player2.getCurrentCastle());


        if (!rebuilding) {
            player1.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
        }
        //player1.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
        //player2.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);


        // Start the game
        player1.getUser().sendAction(Action.AI, Action.NOTHING, Action.NOTHING);
        player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.AI, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);

        if (player1.getUser().isClosed()) {
            Log.game("Player " + player1.getChatName() + " left after pairing but before game began");
            over = true;
            player2.getUser().endGame();
            player2.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            //player2.win(player2, false);
        } else if (player2.getUser().isClosed()) {
            Log.game("Player " + player2.getChatName() + " left after pairing but before game began");
            over = true;
            player1.getUser().endGame();
            player1.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
            //player1.win(player2, false);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send text. TODO: have fun with this
    /////////////////////////////////////////////////////////////////
    public void sendText(Player sender, String message) {
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    private boolean validGame() {
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame(Castle winner) {
        over = true;
        player1.getUser().endGame();
        player2.getUser().endGame();

        if (validGame()) {
            // Update the scores
            if (winner == playerCastle) {
                long reward = Balance.COOP_BONUS + Balance.reward(ai.getLevel());
//                int reward = 125 + (ai.getLevel() / 2);
//                if (newbie) reward += 50;
                player1.win(reward);
                player2.win(reward);
            }

            if (winner == AICastle) {
                player1.lose();
                player2.lose();
            }
        } else
            Log.alert(player1.getName() + " and " + player2.getName() + " are doing weird things with the AI");

        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

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
        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        player1.getUser().endGame();
        player2.getUser().endGame();
    }


    /////////////////////////////////////////////////////////////////
    // Disconnect a player
    /////////////////////////////////////////////////////////////////
    public void interrupt(Player player) {
        if (player == player1) {
            player2.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
        }
        if (player == player2) {
            player1.getUser().sendAction(Action.DISCONNECT, Action.NOTHING, Action.NOTHING);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Disconnect a player
    /////////////////////////////////////////////////////////////////
    public void disconnect(Player player) {
        if (over) return;
        over = true;

        if (player == player1) {
            player2.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
        }
        if (player == player2) {
            player1.getUser().sendAction(Action.ALLY_LEFT, Action.NOTHING, Action.NOTHING);
        }
        endGame(AICastle);
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

            Thread.sleep(2000);

            player1.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);

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
    // Process the action
    /////////////////////////////////////////////////////////////////
    public void processAction(Player player, short action, short actorLocation, short target) throws Exception {
        if (player != null) {
            if (action == Action.NOTHING) return;

            if (rebuilding) {
                player.getUser().sendAction(action, actorLocation, target);
            } else {
                actions.add(new GameAction(player, action, actorLocation, target));
            }
        }

        short tmpActorLocation = actorLocation;
        short tmpTarget = target;

        // Unit actions
        if (action < 30) {
            Unit actor = battleField.getUnitAt(tmpActorLocation);

            if (player != null) {
                // Make sure the unit exists
                if (actor == null) {
                    Log.error("Player " + player.getName() + " sent an invalid actor location.");
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
                    player2.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                }
                if (player == player2) {
                    player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                }
                return;
            } else {
                player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
                player2.getUser().sendAction(action, tmpActorLocation, tmpTarget);
            }
            return;
        }

        // Everything else
        switch (action) {
            case Action.GROW:
                battleField.getUnitAt(tmpActorLocation).grow(tmpTarget);
                player1.getUser().sendAction(Action.GROW, tmpActorLocation, tmpTarget);
                player2.getUser().sendAction(Action.GROW, tmpActorLocation, tmpTarget);
                break;

            case Action.DEPLOY:

                if (player != null) {
                    //Unit unit = player.getCurrentCastle().getUnit(actorLocation);
                    Castle castle = getCastle(player);
                    Unit unit = castle.getUnit(actorLocation);

                    // No unit there?
                    if (unit == null) {
                        Log.error("Player " + player.getName() + " attempted to deploy a non-existent unit: " + actorLocation);
                        return;
                    }

                    // move the unit to the right castle
                    castle.remove(unit);
                    unit.setCastle(playerCastle);
                    playerCastle.add(unit);

                    // Make sure it's placed in a valid location
                    Vector targets = unit.getCastleTargets();
                    if (!validateTarget(targets, tmpTarget)) {
                        Log.error("Player " + player.getName() + " attempted to deploy to an invalid location.");
                        return;
                    }

                    // send to unit to the other guy
                    if (player == player1) {
                        player2.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), tmpTarget);
                        Log.game(player.getName() + ": " + playerCastle.deploy(Unit.TEAM_1, unit, tmpTarget));
                    } else if (player == player2) {
                        player1.getUser().sendAction(Action.DEPLOY_ALLY, unit.getID(), tmpTarget);
                        Log.game(player.getName() + ": " + playerCastle.deploy(Unit.TEAM_2, unit, tmpTarget));
                    }

                    //else
                    //{
                    //	Log.game(player.getName() + ": " + playerCastle.deploy(Unit.TEAM_1, unit, tmpTarget));
                    //}
                } else {
                    player1.getUser().sendAction(Action.DEPLOY_ENEMY, actorLocation, tmpTarget);
                    player2.getUser().sendAction(Action.DEPLOY_ENEMY, actorLocation, tmpTarget);
                }
                break;


            case Action.END_TURN:
                turns++;

                // move the turn to the next guy
                if (player == player1) {
                    playerCastle.refresh(Unit.TEAM_1);
                    playerCastle.startTurn(Unit.TEAM_2);

                    player1.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);

                    player2.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                }

                // give control to the AI
                if (player == player2) {

                    playerCastle.refresh(Unit.TEAM_2);
                    currentCastle = AICastle;
                    AICastle.startTurn(Unit.TEAM_1);

                    player1.getUser().sendAction(Action.REFRESH_ALLY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);

                    player1.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);

                    Log.game("Artificial Opponent's turn");
                    ai.computeTurn();

                    if (!over()) {
                        // back to the player
                        AICastle.refresh(Unit.TEAM_1);
                        playerCastle.startTurn(Unit.TEAM_1);
                        currentCastle = playerCastle;

                        player1.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                        player2.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                        player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                        player2.getUser().sendAction(Action.START_TURN_ALLY, Action.NOTHING, Action.NOTHING);
                    }
                }
                break;

            case Action.OFFER_DRAW:
                //resynch();
                //if (!rebuilding) throw new Exception("test");
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
        Log.game("Castle for: " + player1.getName());


        barracks = player1Castle.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
        output = "";

        Log.game("Castle for: " + player2.getName());

        barracks = player2Castle.getBarracks();
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
    // AI
    /////////////////////////////////////////////////////////////////
    public AI getAI() {
        return ai;
    }


    /////////////////////////////////////////////////////////////////
    // Banish
    /////////////////////////////////////////////////////////////////
    public void banish(Unit unit) {
        if (unit.getCastle() == playerCastle) {
            playerCastle.clear();
            if (unit.getTeam() == Unit.TEAM_1) {
                player1Castle.add(Unit.getUnit(unit.getID(), player1Castle));
            } else if (unit.getTeam() == Unit.TEAM_2) {
                player2Castle.add(Unit.getUnit(unit.getID(), player2Castle));
            }
        }
        playerCastle.clear();
    }


    /////////////////////////////////////////////////////////////////
    // Get player's castle
    /////////////////////////////////////////////////////////////////
    public Castle getCastle(Player player) {
        if (player == player1) return player1Castle;
        else return player2Castle;
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
