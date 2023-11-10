///////////////////////////////////////////////////////////////////////
// Name: ServerGame
// Desc: The game on the server
// Date: 5/11/2003 - Gabe Jones
//    10/15/2010 - David Schwartz
//   Updates: Implemented redrawArmy
//    8/17/2011 Accounts for redrawn armies when resynching - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.game;

// imports

import leo.server.*;
import leo.server.observers.ServerObserver;
import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class ServerGame implements Game {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MATCH = 0;
    public static final int DUEL = 1;
    private static final int DUEL_SIZE = 1250;
    private static final int DUEL_NORMALIZE = 2;
    private static final int TACTICIAN_ODDS = 5;
    private static final int GENERAL_ODDS = 10;
    private static final int COMMANDER_INC = 5;
    private static final int MIN_TURNS = 4;
    private static final int MIN_ACTIONS = 4;
    private final Server server;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean over = false;
    private final Player player1;
    private final Player player2;
    private Castle castle1;
    private Castle castle2;
    private Castle currentCastle;
    private BattleField battleField;
    private ServerObserver observer;
    private boolean player1Draw = false;
    private boolean player2Draw = false;
    private final int type;
    private int turns;
    private int gameType;
    private int player1Actions;
    private int player2Actions;
    private final boolean rated;
    private final boolean mirroredRandom;
    private int repicksP1;
    private int repicksP2;
    private final long seed;
    private Random random;
    private boolean rebuilding = false;
    private final Vector<GameAction> actions = new Vector<GameAction>();
    private Castle savedCastle1;      // Save random game castles in case of rematch
    private Castle savedCastle2;
    private boolean rematch = false;  // Is the game a rematch of a previous random game?


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ServerGame(Server server, Player newPlayer1, Player newPlayer2, int newType, boolean isRated, boolean mirrRandom, boolean isRematch) {
        this.server = server;
        // get a new seed
        seed = server.getSeed();

        // set some variables
        player1 = newPlayer1;
        player2 = newPlayer2;
        type = newType;
        rated = isRated;
        mirroredRandom = mirrRandom;
        rematch = isRematch;

        // initialize the server state
        initialize();

        // Log it
        printCastles();

        // send the player stuff
        sendInitialization();
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the game
    /////////////////////////////////////////////////////////////////
    public void initialize() {
        turns = 0;
        player1Actions = 0;
        player2Actions = 0;

        // No repicks if a rematch
        if (!rematch) {
            repicksP1 = 2;
            repicksP2 = 2;
        } else {
            repicksP1 = 0;
            repicksP2 = 0;
        }

        // set the random to the constructed seed
        random = new Random(seed);

        if (type == ServerGame.MATCH) {
            player1.initializeGame(Action.GAME_CONSTRUCTED);
            player2.initializeGame(Action.GAME_CONSTRUCTED);
            gameType = Action.GAME_CONSTRUCTED;
        } else if (type == ServerGame.DUEL && mirroredRandom == false) {
            Castle randomCastle1 = generateCastle(player1);
            Castle randomCastle2 = generateCastle(player2);
            player1.initializeGame(randomCastle1);
            player2.initializeGame(randomCastle2);
            gameType = Action.GAME_RANDOM;
        } else if (type == ServerGame.DUEL && mirroredRandom == true) {
            Castle randomCastle1 = generateCastle(player1);
            player1.initializeGame(randomCastle1);
            player2.initializeGame(randomCastle1);
            gameType = Action.GAME_MIRRORED_RANDOM;
        }


        castle1 = player1.getCurrentCastle();
        castle2 = player2.getCurrentCastle();
        savedCastle1 = new Castle();
        savedCastle2 = new Castle();

        // Save the castles in case of a rematch if it is a random game
        if (type == ServerGame.DUEL) {
            Vector<UndeployedUnit> uunits = castle1.getBarracks();
            Iterator it = uunits.iterator();
            while (it.hasNext()) {
                UndeployedUnit uunit = (UndeployedUnit) it.next();
                for (int i = 0; i < uunit.count(); ++i)
                    savedCastle1.add(Unit.getUnit(uunit.getID(), savedCastle1));
            }
            uunits = castle2.getBarracks();
            it = uunits.iterator();
            while (it.hasNext()) {
                UndeployedUnit uunit = (UndeployedUnit) it.next();
                for (int i = 0; i < uunit.count(); ++i)
                    savedCastle2.add(Unit.getUnit(uunit.getID(), savedCastle2));
            }
        }

        currentCastle = castle1;
        battleField = new BattleField(castle1, castle2);
        castle1.setBattleField(battleField);
        castle2.setBattleField(battleField);
        castle1.setLocation(BattleField.getLocation((byte) 5, (byte) 10));
        castle2.setLocation(BattleField.getLocation((byte) 5, (byte) 0));
        observer = new ServerObserver(this);
        castle1.setObserver(observer);
        castle2.setObserver(observer);
        player1.getUser().startGame(this);
        player2.getUser().startGame(this);
        castle1.refresh(Unit.TEAM_1);
        castle2.refresh(Unit.TEAM_1);
    }


    /////////////////////////////////////////////////////////////////
    // Send initializaions
    /////////////////////////////////////////////////////////////////
    public void sendInitialization() {
        // Send the users
        // temp if
        if (!rebuilding) {
            // Wipe their game data
            player1.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.NEW_GAME, Action.NOTHING, Action.NOTHING);

            // send the opponent
            player1.getUser().sendOpponent(player2);
            player2.getUser().sendOpponent(player1);
        }

        // send the castles
        player1.getUser().sendCastle(castle1);
        player2.getUser().sendCastle(castle2);

        // Start the game
        if (!rebuilding) {
            // Offset random Redraw button
            if (gameType == Action.GAME_RANDOM && !rematch) {
                player1.getUser().sendAction(Action.START_GAME, Action.SET_RANDOM, Action.NOTHING);
                player2.getUser().sendAction(Action.START_GAME, Action.SET_RANDOM, Action.NOTHING);
            } else {
                player1.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);
                player2.getUser().sendAction(Action.START_GAME, Action.NOTHING, Action.NOTHING);

            }
        }
  
  /*if(gameType == Action.GAME_CONSTRUCTED){
   player1.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
   player2.getUser().sendAction(Action.SET_CONSTRUCTED, Action.NOTHING, Action.NOTHING);
  }
  else if(gameType == Action.GAME_RANDOM){
   player1.getUser().sendAction(Action.SET_RANDOM, Action.NOTHING, Action.NOTHING);
   player2.getUser().sendAction(Action.SET_RANDOM, Action.NOTHING, Action.NOTHING);
  }
  else if(gameType == Action.GAME_MIRRORED_RANDOM){
   player1.getUser().sendAction(Action.SET_MIRRORED_RANDOM, Action.NOTHING, Action.NOTHING);
   player2.getUser().sendAction(Action.SET_MIRRORED_RANDOM, Action.NOTHING, Action.NOTHING);
  }*/
        player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);

        if (player1.getUser().isClosed()) {
            Log.game("Player " + player1.getChatName() + " left after pairing but before game began");
            over = true;
            player2.getUser().endGame();
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            //player2.win(player2, false);
        } else if (player2.getUser().isClosed()) {
            Log.game("Player " + player2.getChatName() + " left after pairing but before game began");
            over = true;
            player1.getUser().endGame();
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            //player1.win(player2, false);
        }

        // No repicks if it is a rematch
        if (rematch) {
            player1.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
            player1.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Send text
    /////////////////////////////////////////////////////////////////
    public void sendText(Player sender, String message) {
        if (sender == player1)
            player2.getUser().sendText(message);
        else if (sender == player2)
            player1.getUser().sendText(message);
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    private boolean validGame() {
        return turns > MIN_TURNS && player1Actions > MIN_ACTIONS && player2Actions > MIN_ACTIONS;
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame(Castle winner) {
        over = true;
        player1.getUser().endGame();
        player2.getUser().endGame();

        if (validGame()) {
            // Save the castles in case of a rematch if it is a random game
            if (type == ServerGame.DUEL) {
                player1.clearPrevRandomCastle();
                Vector<UndeployedUnit> uunits = savedCastle1.getBarracks();
                Iterator it = uunits.iterator();
                while (it.hasNext()) {
                    UndeployedUnit uunit = (UndeployedUnit) it.next();
                    for (int i = 0; i < uunit.count(); ++i)
                        player1.addToPrevRandomCastle(Unit.getUnit(uunit.getID(), player1.getPrevRandomCastle()));
                }
                player2.clearPrevRandomCastle();
                uunits = savedCastle2.getBarracks();
                it = uunits.iterator();
                while (it.hasNext()) {
                    UndeployedUnit uunit = (UndeployedUnit) it.next();
                    for (int i = 0; i < uunit.count(); ++i)
                        player2.addToPrevRandomCastle(Unit.getUnit(uunit.getID(), player2.getPrevRandomCastle()));
                }
                player1.getUser().allowRematch(player2);
                player2.getUser().allowRematch(player1);
            }

            // Update the scores
            if (winner == castle1) {
                if (rated) updateElo(player1, player2, true);
                player1.win(server, player2, rated);
                player2.lose(server, player1, rated);
                Log.game(player1.getName() + " defeated " + player2.getName());
                server.sendText(null, "*** " + player1.getChatName() + " defeated " + player2.getChatName() + " ***");
            }

            if (winner == castle2) {
                if (rated) updateElo(player1, player2, false);
                player2.win(server, player1, rated);
                player1.lose(server, player2, rated);
                Log.game(player2.getName() + " defeated " + player1.getName());
                server.sendText(null, "*** " + player2.getChatName() + " defeated " + player1.getChatName() + " ***");
            }
        } else
            Log.game(player1.getName() + " and " + player2.getName() + " in a weird game");


        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
  
  /*if(validGame()){
	  server.sendRating(player1);
	  server.sendRating(player2);
  }*/

        try {
            Thread.sleep(1000);
        } catch (Exception ev) {
        }
    }

    public void updateElo(Player p1, Player p2, boolean p1won) {
        int p1oldrank = p1.getRank();
        int p2oldrank = p2.getRank();
        int oldelo1 = p1.getEloRating();
        int oldelo2 = p2.getEloRating();
        String result1 = p1.getChatName() + "(" + p1.getEloRating() + " -> ";
        String result2 = p2.getChatName() + "(" + p2.getEloRating() + " -> ";
        //// ACTUAL ELO SCORING START
        float winWeightP1 = (float) Math.pow(10.0, (((double) (p1.getEloRating())) / 400));
        float winWeightP2 = (float) Math.pow(10.0, (((double) (p2.getEloRating())) / 400));
        float expectedWinChanceP1 = winWeightP1 / (winWeightP1 + winWeightP2);
        float expectedWinChanceP2 = winWeightP2 / (winWeightP1 + winWeightP2);
        int gameResultP1 = 0;
        int gameResultP2 = 0;
        if (p1won) gameResultP1 = 1;
        else gameResultP2 = 1;
        int scoreAdjustP1 = Math.round((p1.getEloKfactor()) * (gameResultP1 - expectedWinChanceP1));
        int scoreAdjustP2 = Math.round((p2.getEloKfactor()) * (gameResultP2 - expectedWinChanceP2));
        p1.setEloRating(p1.getEloRating() + scoreAdjustP1);
        p2.setEloRating(p2.getEloRating() + scoreAdjustP2);
        //// ACTUAL ELO SCORING END
	 /*result1 += (p1.getEloRating()+")");
	 result2 += (p2.getEloRating()+")");
	 System.out.println(result1 + "\n" + result2);
	 int p1newrank = p1.getNewRank();
	 int p2newrank = p2.getNewRank();
	 int shift1 = Math.abs(oldelo1-p1.getEloRating());
	 int shift2 = Math.abs(oldelo2-p2.getEloRating());
	 System.out.println(p1.getChatName()+"'s rating changed by "+shift1);
	 System.out.println(p2.getChatName()+"'s rating changed by "+shift2);
	 int shifterror = Math.abs(shift1-shift2);
	 System.out.println("The shift error is: "+shifterror);
	 if(shifterror != 0){System.out.println("LOOK HERE FOLK, WE CAUGHT OURSELVES A LIVE ONE");}
	 System.out.println(p1.getChatName()+": Rank went from "+p1oldrank+" to "+p1newrank);
	 System.out.println(p2.getChatName()+": Rank went from "+p2oldrank+" to "+p2newrank);*/
    }

    /////////////////////////////////////////////////////////////////
    // Draw game
    /////////////////////////////////////////////////////////////////
    public void drawGame() {
        over = true;
        if (validGame()) {
            Log.game("Match between " + player1.getName() + " and " + player2.getName() + " ends in a draw.");
            player1.draw(player2);
            player2.draw(player1);
            server.sendText(null, "*** " + "Match between " + player1.getName() + " and " + player2.getName() + " ends in a draw" + " ***");
        } else
            Log.game(player1.getName() + " and " + player2.getName() + " in a weird game");

        player1.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        player2.getUser().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);

        try {
            Thread.sleep(1000);
        } catch (Exception ev) {
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
            player2.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(castle2);
        }
        if (player == player2) {
            player1.getUser().sendAction(Action.ENEMY_LEFT, Action.NOTHING, Action.NOTHING);
            endGame(castle1);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Resynch the game
    /////////////////////////////////////////////////////////////////
    public void resynch() {
        if (rebuilding) return;

        Log.game("Resynching");

        try {
            rebuilding = true;
            over = true;

            // send inform the players
            // temp version is sendInitialization
            player1.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.RESYNCH, Action.NOTHING, Action.NOTHING);

            Thread.sleep(100);

            player1.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.RESYNCH_READY, Action.NOTHING, Action.NOTHING);

            // reinitialize
            over = false;
            initialize();

            // Log it
            printCastles();

            sendInitialization();

            for (int i = 0; i < actions.size(); i++) {
                GameAction action = actions.elementAt(i);
                processAction(
                        action.getPlayer(),
                        action.getAction(),
                        action.getActor(),
                        action.getTarget());
            }
            player1Draw = false;
            player2Draw = false;
            rebuilding = false;
            player1.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);
            player2.getUser().sendAction(Action.END_RESYNCH, Action.NOTHING, Action.NOTHING);

        } catch (Exception e) {
            Log.error("ServerGame.resynch " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Interpret the action
    /////////////////////////////////////////////////////////////////
    public void interpretAction(Player player, short action, short actorLocation, short target) throws Exception {
        try {
            if (rebuilding) return;
            processAction(player, action, actorLocation, target);
        } catch (Exception e) {
            throw e;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Interpret the action, for real this time
    /////////////////////////////////////////////////////////////////
    private void processAction(Player player, short action, short actorLocation, short target) throws Exception {
        if (action == Action.NOTHING) return;

        if (rebuilding) {
            player.getUser().sendAction(action, actorLocation, target);
        }
        if (!rebuilding) //else
        {
            actions.add(new GameAction(player, action, actorLocation, target));
        }

        if (player.getCurrentCastle() != currentCastle && action != Action.OFFER_DRAW && action != Action.SURRENDER) {
            Log.error(player.getName() + " attempted an action: " + action + " out of turn.");
            return;
        }
        short tmpActorLocation = actorLocation;
        short tmpTarget = target;
        if (player == player2) {
            tmpActorLocation = BattleField.getLocation((byte) (10 - BattleField.getX(actorLocation)), (byte) (10 - BattleField.getY(actorLocation)));
            tmpTarget = BattleField.getLocation((byte) (10 - BattleField.getX(target)), (byte) (10 - BattleField.getY(target)));
        }

        // Unit actions
        if (action < 30) {
            Unit actor = battleField.getUnitAt(tmpActorLocation);

            // Make sure the unit exists
            if (actor == null) {
                Log.error("Player " + player.getName() + " sent an invalid actor location.");
                return;
            }

            // Make sure it belongs to them
            if (actor.getCastle() != player.getCurrentCastle()) {
                Log.error("Player " + player.getName() + " tried to use a unit that's not theirs.");
                return;
            }

            // Do it
            if (!rebuilding)
                Log.game(player.getName() + ": " + actor.performAction(action, tmpTarget));
            else
                actor.performAction(action, tmpTarget);


            // Send it to the other player
            if (player == player1) {
                player1Actions++;
                tmpActorLocation = BattleField.getLocation((byte) (10 - BattleField.getX(actorLocation)), (byte) (10 - BattleField.getY(actorLocation)));
                tmpTarget = BattleField.getLocation((byte) (10 - BattleField.getX(target)), (byte) (10 - BattleField.getY(target)));
                player2.getUser().sendAction(action, tmpActorLocation, tmpTarget);
            }
            if (player == player2) {
                player2Actions++;
                player1.getUser().sendAction(action, tmpActorLocation, tmpTarget);
            }
            return;
        }

        // Everything else
        switch (action) {
            case Action.DEPLOY:
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

                // Send it to the other player
                if (player == player1) { //tmpActorLocation = BattleField.getLocation((byte) (10 - BattleField.getX(actorLocation)), (byte) (10 - BattleField.getY(actorLocation)));
                    tmpTarget = BattleField.getLocation((byte) (10 - BattleField.getX(target)), (byte) (10 - BattleField.getY(target)));
                    player2.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);
                    repicksP1 = 0;
                    player1.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                }

                if (player == player2) {
                    player1.getUser().sendAction(Action.DEPLOY_ENEMY, unit.getID(), tmpTarget);
                    repicksP2 = 0;
                    player1.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                }
                break;

            //re-draws the random army, can be done up to two times
            case Action.REDRAW_ARMY:
                if (rematch == true) {
                    Log.error("Player " + player.getName() + " trying to redraw army when playing a rematch");
                    break;
                }
                if (gameType == Action.GAME_RANDOM && turns <= 1) {
                    //System.out.println("ServerGame: Action.REDRAW_ARMY");
                    if (player == player1 && repicksP1 > 0) {
                        castle1.clear();
                        redrawCastle(castle1, player1);
                        savedCastle1 = new Castle();
                        if (type == ServerGame.DUEL) {
                            Vector<UndeployedUnit> uunits = castle1.getBarracks();
                            Iterator it = uunits.iterator();
                            while (it.hasNext()) {
                                UndeployedUnit uunit = (UndeployedUnit) it.next();
                                savedCastle1.add(Unit.getUnit(uunit.getID(), savedCastle1));
                            }
                        }
                        player1.getUser().newCastle(castle1);
                        // Log it
                        printCastles();
                        repicksP1--;
                        if (repicksP1 == 0) {
                            player1.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                            player2.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                        }
                    } else if (player == player2 && repicksP2 > 0) {
                        castle2.clear();
                        redrawCastle(castle2, player2);
                        savedCastle2 = new Castle();
                        if (type == ServerGame.DUEL) {
                            Vector<UndeployedUnit> uunits = castle2.getBarracks();
                            Iterator it = uunits.iterator();
                            while (it.hasNext()) {
                                UndeployedUnit uunit = (UndeployedUnit) it.next();
                                savedCastle2.add(Unit.getUnit(uunit.getID(), savedCastle2));
                            }
                        }
                        player2.getUser().newCastle(castle2);
                        // Log it
                        printCastles();
                        repicksP2--;
                        if (repicksP2 == 0) {
                            player1.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                            player2.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                        }
                    }
                } else {
                    Logger.warn("Cannot re-draw army");
                }
                break;

            case Action.END_TURN:
                //System.out.println("Servergame: Action.END_TURN");
                turns++;
                if (gameType == Action.GAME_RANDOM && turns == 2 && !rematch) {
                    player1.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.DISABLE_REPICK_P1, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.DISABLE_REPICK_P2, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.MOVE_PANEL, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.MOVE_PANEL, Action.NOTHING, Action.NOTHING);
                }
                if (player == player1) {
                    player1.getCurrentCastle().refresh(Unit.TEAM_1);
                    castle2.startTurn(Unit.TEAM_1);
                    currentCastle = castle2;

                    player1.getUser().sendAction(Action.P2_TURN, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.P2_TURN, Action.NOTHING, Action.NOTHING);

                    player1.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);

                    if (!rebuilding) Log.game(player2.getName() + "'s turn");
                }

                if (player == player2) {
                    // wrap it up for player 2
                    player2.getCurrentCastle().refresh(Unit.TEAM_1);
                    castle1.startTurn(Unit.TEAM_1);
                    currentCastle = castle1;


                    player1.getUser().sendAction(Action.P1_TURN, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.P1_TURN, Action.NOTHING, Action.NOTHING);

                    player2.getUser().sendAction(Action.REFRESH, Action.NOTHING, Action.NOTHING);
                    player2.getUser().sendAction(Action.START_TURN_ENEMY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.REFRESH_ENEMY, Action.NOTHING, Action.NOTHING);
                    player1.getUser().sendAction(Action.START_TURN, Action.NOTHING, Action.NOTHING);

                    if (!rebuilding) Log.game(player1.getName() + "'s turn");
                }
                break;

            case Action.OFFER_DRAW:
                if (player == player1) {
                    player1Draw = true;
                    player2.getUser().sendAction(Action.OFFER_DRAW, Action.NOTHING, Action.NOTHING);
                }

                if (player == player2) {
                    player2Draw = true;
                    player1.getUser().sendAction(Action.OFFER_DRAW, Action.NOTHING, Action.NOTHING);
                }

                if (player1Draw && player2Draw) {
                    drawGame();
                }

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


        barracks = castle1.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
        output = "";

        Log.game("Castle for: " + player2.getName());

        barracks = castle2.getBarracks();
        for (int i = 0; i < barracks.size(); i++) {
            UndeployedUnit unit = barracks.elementAt(i);
            output = output + unit.getUnit().getName() + "(" + unit.count() + "), ";
        }
        Log.game(output);
    }


    /////////////////////////////////////////////////////////////////
    // Generate a random castle for match mode
    /////////////////////////////////////////////////////////////////
    public Castle generateCastle(Player player) {
        Castle castle = new Castle();

        // If it is a rematch, use the saved castles from the last game
        if (rematch) {
            Vector<UndeployedUnit> uunits = player.getPrevRandomCastle().getBarracks();
            Iterator it = uunits.iterator();
            while (it.hasNext()) {
                UndeployedUnit uunit = (UndeployedUnit) it.next();
                for (int i = 0; i < uunit.count(); ++i)
                    castle.add(Unit.getUnit(uunit.getID(), castle));
            }
            return castle;
        }

        // Otherwise calculate new castle
        int commanderWeight = 0;

        // add the general
        castle.add(Unit.getUnit(UnitType.GENERAL, castle));
        castle.add(Unit.getUnit(UnitType.GATE_GUARD, castle));

        int commanders = 1;
        while (castle.getValue() < DUEL_SIZE) {
            Unit unit = Unit.getUnit(((byte) random.nextInt(UnitType.UNIT_COUNT)), castle);

            if (random.nextInt(TACTICIAN_ODDS + commanderWeight) == 0) {
                unit = Unit.getUnit(UnitType.TACTICIAN, castle);
                commanderWeight += COMMANDER_INC;
            }

            // only one gateguard
            if (unit != null && unit.getID() == UnitType.GATE_GUARD) {
                unit = null;
            }

            // make sure they're not flooded with commanders
            if (unit != null && (
                    unit.getID() == UnitType.GENERAL ||
                            unit.getID() == UnitType.TACTICIAN ||
                            unit.getID() == UnitType.COMMAND_POST ||
                            unit.getID() == UnitType.SERGEANT
            )) {
                if (commanders >= 3) unit = null;
            }

            if (unit != null && (unit.getID() == UnitType.RELIC_GIFT_UNIT))
                unit = null;

            // Weight the smaller units
            if (unit != null && unit.getCastleCost() != 1001 && random.nextInt((unit.getCastleCost() / 50) + DUEL_NORMALIZE) == 0) {
                if (player.access(unit.accessLevel()) && DUEL_SIZE - castle.getValue() >= unit.getCastleCost())
                    castle.add(unit);

                switch (unit.getID()) {
                    case UnitType.TACTICIAN:
                    case UnitType.GENERAL:
                    case UnitType.COMMAND_POST:
                    case UnitType.SERGEANT:
                        commanders++;
                        break;
                }

            }
        }
        return castle;
    }

    /////////////////////////////////////////////////////////////////
    // Fill a castle with random units
    /////////////////////////////////////////////////////////////////
    public Castle redrawCastle(Castle castle, Player player) {
        int commanderWeight = 0;

        // add the general
        castle.add(Unit.getUnit(UnitType.GENERAL, castle));
        castle.add(Unit.getUnit(UnitType.GATE_GUARD, castle));

        int commanders = 1;
        while (castle.getValue() < DUEL_SIZE) {
            Unit unit = Unit.getUnit(((byte) random.nextInt(UnitType.UNIT_COUNT)), castle);

            if (random.nextInt(TACTICIAN_ODDS + commanderWeight) == 0) {
                unit = Unit.getUnit(UnitType.TACTICIAN, castle);
                commanderWeight += COMMANDER_INC;
            }

            // only one gateguard
            if (unit != null && unit.getID() == UnitType.GATE_GUARD) {
                unit = null;
            }

            // make sure they're not flooded with commanders
            if (unit != null && (
                    unit.getID() == UnitType.GENERAL ||
                            unit.getID() == UnitType.TACTICIAN ||
                            unit.getID() == UnitType.COMMAND_POST ||
                            unit.getID() == UnitType.SERGEANT
            )) {
                if (commanders >= 3) unit = null;
            }

            if (unit != null && (unit.getID() == UnitType.RELIC_GIFT_UNIT))
                unit = null;

            // Weight the smaller units
            if (unit != null && unit.getCastleCost() != 1001 && random.nextInt((unit.getCastleCost() / 50) + DUEL_NORMALIZE) == 0) {
                if (player.access(unit.accessLevel()) && DUEL_SIZE - castle.getValue() >= unit.getCastleCost())
                    castle.add(unit);

                switch (unit.getID()) {
                    case UnitType.TACTICIAN:
                    case UnitType.GENERAL:
                    case UnitType.COMMAND_POST:
                    case UnitType.SERGEANT:
                        commanders++;
                        break;
                }

            }
        }
        return castle;
    }

    /////////////////////////////////////////////////////////////////
    // get the current player
    /////////////////////////////////////////////////////////////////
    public Player getCurrentPlayer() {
        return player1.getCurrentCastle() == currentCastle ? player1 : player2;
    }


    /////////////////////////////////////////////////////////////////
    // Get player
    /////////////////////////////////////////////////////////////////
    public Player getPlayer(Castle castle) {
        return castle == castle1 ? player1 : player2;
    }


    /////////////////////////////////////////////////////////////////
    // Game over man
    /////////////////////////////////////////////////////////////////
    public boolean over() {
        return over;
    }


    /////////////////////////////////////////////////////////////////
    // Get the random
    /////////////////////////////////////////////////////////////////
    public Random random() {
        return random;
    }

    public int getDelay() {
        return rebuilding ? 0 : 500;
    }

    public void banish(Unit unit) {
    }

    public int getGameType() {
        return gameType;
    }

}
