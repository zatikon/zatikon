///////////////////////////////////////////////////////////////////////
//	Name:	ScriptChangeling
//	Desc:	So the AI's changelings will know to use the unit it swaps with
//	Date:	9/15/2011 - W. Fletcher Cole
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.AI;
import leo.server.game.Game;
import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;

public class ScriptChangeling implements Script {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;
    private Unit hunting = null;
    private final boolean canRandom;
    private final AI ai;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptChangeling(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle, boolean newCanRandom, AI _ai) {
        unit = newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
        canRandom = newCanRandom;
        ai = _ai;
    }


    /////////////////////////////////////////////////////////////////
    // Use unit
    /////////////////////////////////////////////////////////////////
    public void perform() {
        if (hunting == null || hunting.isDead() || hunting.getCastle() == unit.getCastle()) {
            if (game.random().nextInt(4) == 0) {
                hunting = getRandomEnemy();
            } else {
                hunting = getClosestEnemy();
            }
        }

        for (int i = 0; i < 6; i++) {
            win();
            if (!attack(true && canRandom)) {
                if (!move()) {
                    attack(false);
                }
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Attack
    /////////////////////////////////////////////////////////////////
    private boolean attack(boolean randomly) {
        if (unit.getCastle() != castle) return false;

        if (game.over()) return false;

        if (!unit.ready()) return false;

        if (unit.getAttack() == null) return false;

        if (unit.getAttack().getRemaining() < 1) return false;

        Vector<Short> targets = unit.getAttack().getTargets();

        if (targets.size() < 1) return false;

        Short finalTarget;

        if (preyIsAvailable(targets)) {
            finalTarget = hunting.getLocation();
        } else {    // Continue the hunt
            if (randomly && game.random().nextBoolean())
                return false;

            // Get a random target
            finalTarget = targets.elementAt(game.random().nextInt(targets.size()));
        }

        // get ready to strike
        short action = unit.getAction(unit.getAttack());
        short location = unit.getLocation();

        // Make the unit do it
        Log.game("Artificial Opponent: " + unit.process(action, location, finalTarget.byteValue()));
        interpretAction(action, location, finalTarget.byteValue());


        //////////////////////////////////////////////////////////////////////////////////////
        // The unit that was swapped with should now have a chance to act
        //////////////////////////////////////////////////////////////////////////////////////
        Vector<Unit> tmp = new Vector<Unit>();
        tmp.add(unit.getCastle().getBattleField().getUnitAt(location));
        ai.cleanScripts(tmp);
        Script script = unit.getCastle().getBattleField().getUnitAt(location).getScript();
        if (script != null)
            script.perform();
        else
            Log.game("Changeling Script: null script for changed unit");
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Move
    /////////////////////////////////////////////////////////////////
    private boolean move() {
        if (unit.getCastle() != castle) return false;

        if (game.over()) return false;

        if (!unit.ready()) return false;

        if (unit.getMove() == null) return false;

        if (unit.getMove().getRemaining() < 1) return false;

        if (unit.getID() == UnitType.GATE_GUARD && unit.getLocation() == castle.getLocation()) return false;

        // Get the move targets
        Vector<Short> moves = unit.getMove().getTargets();

        // If empty go home
        if (moves.size() < 1) return false;

        // Set the target
        short targetLocation = hunting == null || hunting.isDead() ? enemyCastle.getLocation() : hunting.getLocation();

        // Whats the closest possible range
        int closest = AI.getClosestRange(moves, targetLocation);

        // Make sure we're gaining ground
        if (hunting != null && !hunting.isDead()) {
            if (game.random().nextInt(4) != 0 && closest >= BattleField.getDistance(unit.getLocation(), hunting.getLocation())) {
                return false;
            }
        }

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
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptInterceptor.interpretAction(" + unit.getName() + ") " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get random unit
    /////////////////////////////////////////////////////////////////
    private Unit getRandomEnemy() {
        Vector<Unit> units = getEnemyUnits();
        if (units.size() < 1) return null;

        return units.elementAt(game.random().nextInt(units.size()));
    }


    /////////////////////////////////////////////////////////////////
    // The available prey
    /////////////////////////////////////////////////////////////////
    private boolean preyIsAvailable(Vector<Short> targets) {
        if (hunting == null || hunting.isDead()) return false;
        Iterator<Short> it = targets.iterator();

        while (it.hasNext()) {
            Short enemy = it.next();
            if (enemy.byteValue() == hunting.getLocation())
                return true;
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the closest
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
    // Get enemy locations
    /////////////////////////////////////////////////////////////////
    private Vector<Short> getEnemyLocations() {
        BattleField bf = castle.getBattleField();
        Vector<Short> units = new Vector<Short>();
        Iterator<Unit> it = bf.getUnits().iterator();

        while (it.hasNext()) {
            Unit tmp = it.next();
            if (tmp.getCastle() == enemyCastle && tmp.targetable(unit))
                units.add(new Short(tmp.getLocation()));
        }
        return units;
    }


    /////////////////////////////////////////////////////////////////
    // Get enemy units
    /////////////////////////////////////////////////////////////////
    private Vector<Unit> getEnemyUnits() {
        BattleField bf = castle.getBattleField();
        Vector<Unit> units = new Vector<Unit>();
        Iterator<Unit> it = bf.getUnits().iterator();

        while (it.hasNext()) {
            Unit tmp = it.next();
            if (tmp.getCastle() == enemyCastle && tmp.targetable(unit))
                units.add(tmp);
        }
        return units;
    }

}
