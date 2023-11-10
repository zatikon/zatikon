///////////////////////////////////////////////////////////////////////
//	Name:	ScriptRusher
//	Desc:	A castle rusher
//	Date:	11/12/2007 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.AI;
import leo.server.game.Game;
import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;

public class ScriptRusher implements Script {
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


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptRusher(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle) {
        unit = newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
    }


    /////////////////////////////////////////////////////////////////
    // Use unit
    /////////////////////////////////////////////////////////////////
    public void perform() {
        for (int i = 0; i < 3; i++) {
            win();

            if (!attack(true)) {
                if (!move()) {
                    attack(false);
                }
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Attack
    /////////////////////////////////////////////////////////////////
    private boolean attack(boolean randomy) {
        if (game.over()) return false;

        if (!unit.ready()) return false;

        // Half the time, ignore a target
        if (randomy && game.random().nextInt(2) == 0) return false;

        if (unit.getAttack() == null) return false;

        if (unit.getAttack().getRemaining() < 1) return false;

        Vector<Short> targets = unit.getAttack().getTargets();

        if (targets.size() < 1) return false;

        // Get the final target
        Short finalTarget = targets.elementAt(game.random().nextInt(targets.size()));
        short action = unit.getAction(unit.getAttack());
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

        if (unit.getID() == UnitType.GATE_GUARD && unit.getLocation() == castle.getLocation()) return false;

        // Get the move targets
        Vector<Short> moves = unit.getMove().getTargets();

        // If empty go home
        if (moves.size() < 1) return false;

        // Set the target
        short targetLocation = enemyCastle.getLocation();
        if (unit.getID() == UnitType.GATE_GUARD) targetLocation = castle.getLocation();

        // Whats the closest possible range
        int closest = AI.getClosestRange(moves, targetLocation);

        // Make sure we're gaining ground
        if (game.random().nextInt(4) != 0 && closest >= BattleField.getDistance(unit.getLocation(), enemyCastle.getLocation())) {
            return false;
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
            Log.error("ScriptRusher.interpretAction(" + unit.getName() + ") " + e);
        }
    }
}
