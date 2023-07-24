///////////////////////////////////////////////////////////////////////
//      Name:   ScriptFireArcher
//      Desc:   Attacks units
//      Date:   10/16/2008 - Gabe Jones
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
import leo.shared.crusades.UnitFireArcher;

import java.util.Vector;


public class ScriptFireArcher extends ScriptRusher {
    private final UnitFireArcher fireArcher;
    private final Action fireball;
    private final Unit unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScriptFireArcher(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle) {
        super(newUnit, newGame, newCastle, newEnemyCastle);
        fireArcher = (UnitFireArcher) newUnit;
        unit = newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
        fireball = fireArcher.getFireball();
    }


    /////////////////////////////////////////////////////////////////
    // Do stuff
    /////////////////////////////////////////////////////////////////
    public void perform() {
        int tries = 0;
        while (tries < 9) {
            fireball(fireball);
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

        targets.add(new Short(target));

        // Hit the targets
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victim = unit.getBattleField().getUnitAt(location.byteValue());
            if (victim != null) {
                if (victim.getCastle() == unit.getCastle()) return false;
                enemies++;
            }
        }
        return enemies > 0;
    }


    /////////////////////////////////////////////////////////////////
    // interpret action
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptFireArcher.interpretAction " + e);
        }
    }

}
