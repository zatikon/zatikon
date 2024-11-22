///////////////////////////////////////////////////////////////////////
//	Name:	ScriptMagus
//	Desc:	Run a magus
//	Date:	8/27/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.AI;
import leo.server.game.Game;
import leo.shared.Log;
import leo.shared.*;
import leo.shared.crusades.UnitMagus;

import java.util.Iterator;
import java.util.Vector;

public class ScriptMagus implements Script {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitMagus unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;
    private final Action paralyze;
    private final Action wisp;
    private final Action spirit;
    private int range = 1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptMagus(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle) {
        unit = (UnitMagus) newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
        paralyze = unit.getParalyze();
        wisp = unit.getWisp();
        spirit = unit.getSpirit();
    }


    /////////////////////////////////////////////////////////////////
    // Use unit
    /////////////////////////////////////////////////////////////////
    public void perform() {
        if (game.random().nextInt(20) == 0) range++;
        win();
        if (tooClose()) {
            spirit();
        }
        if (BattleField.getDistance(unit.getLocation(), unit.getCastle().getLocation()) <= range) {
            move();
        }
        if (!act(paralyze)) {
            act(wisp);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform an action
    /////////////////////////////////////////////////////////////////
    private boolean act(Action act) {
        if (game.over()) return false;

        if (!unit.ready()) return false;

        if (act.getRemaining() < 1) return false;

        Vector<Short> targets = act.getTargets();

        if (targets.size() < 1) return false;

        Short finalTarget;

        // Get a random target
        finalTarget = targets.elementAt(game.random().nextInt(targets.size()));

        // get ready to strike
        short action = unit.getAction(act);
        short location = unit.getLocation();

        // Make the unit do it
        Log.game("Artificial Opponent: " + unit.process(action, location, finalTarget.byteValue()));
        interpretAction(action, location, finalTarget.byteValue());

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Move
    /////////////////////////////////////////////////////////////////
    private boolean move() {
        if (game.over()) return false;

        if (!unit.ready()) return false;

        if (unit.getMove() == null) return false;

        if (unit.getMove().getRemaining() < 1) return false;

        // Get the move targets
        Vector<Short> moves = unit.getMove().getTargets();

        // If empty go home
        if (moves.size() < 1) return false;

        // Set the target
        short targetLocation = enemyCastle.getLocation();

        // Whats the closest possible range
        int closest = AI.getClosestRange(moves, targetLocation);

        // Refine to closest range
        moves = AI.refineRange(moves, closest, targetLocation);

        // Get the final target
        Short finalTarget = moves.elementAt(game.random().nextInt(moves.size()));
        short action = unit.getAction(unit.getMove());
        short location = unit.getLocation();

        // Make the unit do it
        Log.game("Artificial Opponent: " + unit.process(action, location, finalTarget.byteValue()));
        interpretAction(action, location, finalTarget.byteValue());

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Spirit
    /////////////////////////////////////////////////////////////////
    private void spirit() {
        if (game.over()) return;
        if (!unit.ready()) return;

        // Make the unit do it
        interpretAction(unit.getAction(spirit), unit.getLocation(), Action.NOTHING);
        unit.process(unit.getAction(spirit), unit.getLocation(), Action.NOTHING);
    }


    /////////////////////////////////////////////////////////////////
    // Try to win
    /////////////////////////////////////////////////////////////////
    private void win() {
        if (game.over()) return;

        if (!unit.ready()) return;

        if (unit.getMove() == null) return;

        if (unit.getMove().getRemaining() < 1) return;

        // Get the move targets
        Vector<Short> moves = unit.getMove().getTargets();

        Iterator<Short> it = moves.iterator();
        while (it.hasNext()) {
            Short loc = it.next();
            if (loc.byteValue() == enemyCastle.getLocation()) {
                // Get the final target
                short action = unit.getAction(unit.getMove());
                short location = unit.getLocation();

                // Make the unit do it
                Log.game("Artificial Opponent: " + unit.process(action, location, loc.byteValue()));
                interpretAction(action, location, loc.byteValue());
                return;
            }
        }
        return;
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private boolean tooClose() {
        Unit close = getClosestEnemy();
        if (close == null) return false;
        return (BattleField.getDistance(close.getLocation(), unit.getLocation()) <= 3);
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private Unit getClosestEnemy() {
        Vector<Short> units = getEnemyLocations();
        if (units.size() < 1) return null;

        int closest = AI.getClosestRange(units, unit.getLocation());
        units = AI.refineRange(units, closest, unit.getLocation());
        Short location = units.elementAt(game.random().nextInt(units.size()));
        return unit.getCastle().getBattleField().getUnitAt(location.byteValue());
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private Vector<Short> getEnemyLocations() {
        BattleField bf = castle.getBattleField();
        Vector<Short> units = new Vector<Short>();
        Iterator<Unit> it = bf.getUnits().iterator();

        while (it.hasNext()) {
            Unit tmp = it.next();
            if (tmp.getCastle() == enemyCastle && tmp.targetable(unit))
                units.add(tmp.getLocation());
        }
        return units;
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptMagus.interpretAction " + e);
        }
    }
}
