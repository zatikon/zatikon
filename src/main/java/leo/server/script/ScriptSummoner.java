///////////////////////////////////////////////////////////////////////
// Name: ScripSummoner
// Desc: Run a summoner
// Date: 8/27/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.AI;
import leo.server.game.Game;
import leo.shared.Log;
import leo.shared.*;
import leo.shared.crusades.*;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;

public class ScriptSummoner implements Script {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitSummoner unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;
    private final Action imp;
    private final Action demon;
    private final Action archDemon;
    private int range = 1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptSummoner(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle) {
        unit = (UnitSummoner) newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
        imp = unit.getImp();
        demon = unit.getDemon();
        archDemon = unit.getArchDemon();
    }


    /////////////////////////////////////////////////////////////////
    // Use unit
    /////////////////////////////////////////////////////////////////
    public void perform() {
        if (game.random().nextInt(20) == 0) range++;
        win();
        if (BattleField.getDistance(unit.getLocation(), unit.getCastle().getLocation()) <= range) {
            move();
        }
        if (game.random().nextInt(4) == 0 || !act(archDemon)) {
            if (!act(demon))
                act(imp);
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

        if (act instanceof ActionDamn) {
            refineTargets(targets);
        }

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
    // get rid of the wrong targets
    /////////////////////////////////////////////////////////////////
    private void refineTargets(Vector<Short> targets) {
        Stack<Short> removes = new Stack<Short>();
        Iterator<Short> it = targets.iterator();
        while (it.hasNext()) {
            Short byter = it.next();
            Unit tmp = unit.getBattleField().getUnitAt(byter.byteValue());
            if (tmp instanceof UnitDemon ||
                    tmp instanceof UnitGateGuard ||
                    tmp instanceof UnitArchDemon ||
                    (tmp.getCastle() == unit.getCastle() && !(tmp instanceof UnitImp) &&
                            (tmp.getCastleCost() != 1001))) {
                removes.add(byter);
            }
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // interpretAction wrapper
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptSummoner.interpretAction " + e);
        }
    }
}
