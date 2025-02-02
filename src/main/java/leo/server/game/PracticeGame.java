///////////////////////////////////////////////////////////////////////
// Name: PracticeGame
// Desc: A single player experience
// Date: 11/9/2007 - Created [Unknown]
//       10/26/2010 - Extracted tutorial stuff to TutorialGame [Dan Healy]
//       11/5/2010 - Removed some more tutorial stuff [Dan Healy]
// TODO: Fix resynch after calling for a draw.
///////////////////////////////////////////////////////////////////////
package leo.server.game;

// Imports

import leo.server.*;
import leo.server.observers.PracticeObserver;
import leo.shared.*;
import org.tinylog.Logger;

import java.util.Random;
import java.util.Vector;


public class PracticeGame implements Game {

    private final Server server;
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean over = false;
    private AI ai;
    private final Player player1;
    private Castle castle1;
    private Castle castle2;
    private Castle currentCastle;
    private BattleField battleField;
    private PracticeObserver observer;
    private int turns = 0;
    private final long seed;
    private Random random;
    private boolean rebuilding;
    private int level;
    private final Vector<GameAction> actions = new Vector<GameAction>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PracticeGame(Server server, Player newPlayer1) {
        this.server = server;
        // get a new seed
        seed = server.getSeed();

        // set some variables
        player1 = newPlayer1;

        // Log it with the server
        server.gameListSingle().add(this);
        Logger.info("Added a singleplayer game to the game list. Current total: " + server.gameListSingle().size());

        // initialize the server state
        initialize();

        // Log it
        printCastles();

        // send the player stuff
        sendInitialization();
    }


    /////////////////////////////////////////////////////////////////
    // initialize
    /////////////////////////////////////////////////////////////////
    public void initialize() {
        turns = 0;

        random = new Random(seed);
        player1.initializeGame(Action.GAME_PRACTICE);

        castle1 = player1.getCurrentCastle();
        castle2 = new Castle();
        castle2.ai();
        currentCastle = castle1;

        battleField = new BattleField(castle1, castle2);
        castle1.setBattleField(battleField);
        castle2.setBattleField(battleField);
        castle1.setLocation(BattleField.getLocation((byte) 5, (byte) 10));
        castle2.setLocation(BattleField.getLocation((byte) 5, (byte) 0));
        observer = new PracticeObserver(this);
        castle1.setObserver(observer);
        castle2.setObserver(observer);
        player1.getUser().startGame(this);
        castle1.refresh(Unit.TEAM_1);
        castle2.refresh(Unit.TEAM_1);

        // Prepare the AI
        level = player1.getLevel();
        ai = new AI(level, this, battleField, castle2, castle1);
    }


    /////////////////////////////////////////////////////////////////
    // Send the inits
    /////////////////////////////////////////////////////////////////
    public void sendInitialization() {

        if (!rebuilding) {
            player1.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player1.getUser().sendComputer(level);
        }

        player1.getUser().sendCastle(player1.getCurrentCastle());

        // Start the game
        if (!rebuilding) {
            player1.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
        }
        //player1.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);

        player1.getUser().sendAction(Action.AI, Action.NOTHING, Action.NOTHING);
        player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
        player1.startTurn();

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
        server.gameListSingle().remove(this);
        Logger.info("Removed a singleplayer game from the game list. Current total: " + server.gameListSingle().size());
        if (validGame()) {
            // Update the scores
            if (winner == castle1) {
                server.sendText(null, player1.getChatName() + " defeated the Artificial Opponent(" + level + ")");
                player1.winAi(Balance.reward(ai.getLevel()));
            }

            if (winner == castle2) {
                server.sendText(null, "The Artificial Opponent(" + level + ") defeated " + player1.getChatName());
                player1.loseAi();
            }
        } else
            Log.alert(player1.getName() + " is doing weird things with the AI");

        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        player1.getUser().endGame();
    }


    /////////////////////////////////////////////////////////////////
    // Draw game
    /////////////////////////////////////////////////////////////////
    public void drawGame() {
        over = true;
        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        player1.getUser().endGame();

    }


    /////////////////////////////////////////////////////////////////
    // Interrupt
    /////////////////////////////////////////////////////////////////
    public void interrupt(Player player) {
    }


    /////////////////////////////////////////////////////////////////
    // Disconnect a player
    /////////////////////////////////////////////////////////////////
    public void disconnect(Player player) {
        endGame(castle2);
    }


    /////////////////////////////////////////////////////////////////
    // Resynch me
    /////////////////////////////////////////////////////////////////
    public void resynch() {
        if (rebuilding) return;

        Log.game("Resynching");

        try {
            over = true;
            rebuilding = true;

            // send inform the players
            // temp version is sendInitialization
            player1.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);
            Thread.sleep(2000);
            player1.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);

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

        } catch (Exception e) {
            Log.error("ServerGame.resynch " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Interpret the action
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

            if (player.getCurrentCastle() != currentCastle && action != Action.OFFER_DRAW && action != Action.SURRENDER) {
                Log.alert(player.getName() + " attempted an action: " + action + " out of turn.");
                return;
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
                if (actor.getCastle() != player.getCurrentCastle()) {
                    Log.error("Player " + player.getName() + " tried to use a unit that's not their's.");
                    return;
                }


                // Do it
                if (!rebuilding)
                    Log.game(player.getName() + ": " + actor.performAction(action, tmpTarget));
                else
                    actor.performAction(action, tmpTarget);

            } else {
                player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
            }

            return;
        }

        // Everything else
        switch (action) {
            case Action.GROW:
                Unit tmp = battleField.getUnitAt(tmpActorLocation);
                if (tmp != null)
                    tmp.grow(tmpTarget);
                else
                    Log.error("Null grow");
                player1.getUser().sendAction(Action.GROW, tmpActorLocation, tmpTarget);
                break;

            case Action.DEPLOY:

                if (player != null) {
                    Unit unit = player.getCurrentCastle().getUnit(actorLocation);

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

                    if (!rebuilding)
                        Log.game(player.getName() + ": " + player.getCurrentCastle().deploy(Unit.TEAM_1, unit, tmpTarget));
                    else
                        player.getCurrentCastle().deploy(Unit.TEAM_1, unit, tmpTarget);

                } else {
                    player1.getUser().sendAction(Action.DEPLOY_ENEMY, actorLocation, tmpTarget);
                }
                break;

            case Action.END_TURN:

                turns++;

                player1.getCurrentCastle().refresh(Unit.TEAM_1);
                castle2.startTurn(Unit.TEAM_1);
                currentCastle = castle2;

                player1.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                player1.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);

                if (!rebuilding) Log.game(player1.getName() + "'s Artificial Opponent's turn");

                ai.computeTurn();

                if (!over()) {
                    // back to the player
                    castle2.refresh(Unit.TEAM_1);
                    castle1.startTurn(Unit.TEAM_1);
                    currentCastle = castle1;

                    player1.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
                    player1.startTurn();
                    if (!rebuilding) Log.game(player1.getName() + "'s turn");
                }
                break;

            case Action.OFFER_DRAW:
                resynch();
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
        Log.game("Castle for: " + player1.getName());
        Vector<UndeployedUnit> barracks;

        barracks = castle1.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
    }


    /////////////////////////////////////////////////////////////////
    // get the current player
    /////////////////////////////////////////////////////////////////
    public Castle getCurrentCastle() { return currentCastle; }


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
    // Random
    /////////////////////////////////////////////////////////////////
    public Random random() {
        return random;
    }

    public int getDelay() {
        return rebuilding ? 0 : 500;
    }

    public void banish(Unit unit) {
    }
}
