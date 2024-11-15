///////////////////////////////////////////////////////////////////////
//      Name:   ScriptDragon
//      Desc:   Attacks units
//      Date:   11/12/2007 - Gabe Jones
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.game.Game;
import leo.shared.Log;
import leo.shared.Action;
import leo.shared.Castle;
import leo.shared.TargetType;
import leo.shared.Unit;
import leo.shared.crusades.UnitDragon;

import java.util.Vector;


public class ScriptDragon extends ScriptRusher {
    private final UnitDragon dragon;
    private final Action fireball;
    private final Unit unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptDragon(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle) {
        super(newUnit, newGame, newCastle, newEnemyCastle);
        dragon = (UnitDragon) newUnit;
        unit = newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
        fireball = dragon.getFireball();
    }


    /////////////////////////////////////////////////////////////////
    // Do stuff
    /////////////////////////////////////////////////////////////////
    public void perform() {
        int tries = 0;
        while (!fireball(fireball) && tries < 3) {
            tries++;
        }

        super.perform();
    }


    /////////////////////////////////////////////////////////////////
    // Fireball!
    /////////////////////////////////////////////////////////////////
    private boolean fireball(Action act) {
        if (game.over()) return false;
        if (!unit.ready()) return false;

        if (act.getRemaining() < 1) return false;
        Vector<Short> targets = act.getTargets();
        if (targets.size() < 1) return false;
        Short finalTarget;

        // Get a random target
        finalTarget = targets.elementAt(game.random().nextInt(targets.size()));

        if (!goodLocation(finalTarget)) return false;

        // get ready to strike
        short action = unit.getAction(act);
        short location = unit.getLocation();

        // Make the unit do it
        //unit.process(action, location, finalTarget.byteValue());
        Log.game("Artificial Opponent: " + unit.process(action, location, finalTarget.byteValue()));
        interpretAction(action, location, finalTarget.byteValue());

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Is this a good spot?
    /////////////////////////////////////////////////////////////////
    private boolean goodLocation(short target) {
        int enemies = 0;
        Vector<Short> targets =
                unit.getBattleField().getArea(
                        unit,
                        target,
                        (byte) 1,
                        false,
                        true,
                        true,
                        true, TargetType.BOTH, unit.getCastle());

        targets.add(target);

        // Hit the targets
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victim = unit.getBattleField().getUnitAt(location.byteValue());
            if (victim != null) {
                if (victim.getCastle() == unit.getCastle()) return false;
                enemies++;
            }
        }
        return enemies > 1;
    }


    /////////////////////////////////////////////////////////////////
    // interpret action
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptDragon.interpretAction " + e);
        }
    }

}
