///////////////////////////////////////////////////////////////////////
//      Name:   ScriptLycan
//      Desc:   Attacks units
//      Date:   11/12/2007 - Gabe Jones
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.script;

// imports

import leo.server.AI;
import leo.server.game.Game;
import leo.shared.Log;
import leo.shared.Action;
import leo.shared.BattleField;
import leo.shared.Castle;
import leo.shared.Unit;
import leo.shared.crusades.Lycan;
import leo.shared.crusades.UnitLycanWolf;
import leo.shared.crusades.UnitLycanthrope;
import leo.shared.crusades.UnitWerewolf;

import java.util.Iterator;
import java.util.Vector;

public class ScriptLycan extends ScriptInterceptor {
    private final Lycan lycan;
    private final Unit unit;
    private final Game game;
    private final Castle castle;
    private final Castle enemyCastle;

    public ScriptLycan(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle, boolean newCanRandom) {
        super(newUnit, newGame, newCastle, newEnemyCastle, newCanRandom);
        lycan = (Lycan) newUnit;
        unit = newUnit;
        game = newGame;
        castle = newCastle;
        enemyCastle = newEnemyCastle;
    }


    /////////////////////////////////////////////////////////////////
    // Do stuff
    /////////////////////////////////////////////////////////////////
    public void perform() {
        super.perform();
        if (unit instanceof UnitLycanthrope) {
            if (tooClose()) {
                shift(lycan.getWerewolf());
            } else {
                shift(lycan.getWolf());
            }
        } else if (unit instanceof UnitLycanWolf) {
            if (tooClose()) {
                shift(lycan.getWerewolf());
            }
        } else if (unit instanceof UnitWerewolf) {
            if (!tooClose()) {
                shift(lycan.getWolf());
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Shapeshift
    /////////////////////////////////////////////////////////////////
    private void shift(Action action) {
        if (game.over()) return;
        if (!unit.ready()) return;

        // Make the unit do it
        interpretAction(unit.getAction(action), unit.getLocation(), Action.NOTHING);
        //unit.process(unit.getAction(action), unit.getLocation(),Action.NOTHING);
        Log.game("Artificial Opponent: " + unit.process(unit.getAction(action), unit.getLocation(), Action.NOTHING));
    }


    /////////////////////////////////////////////////////////////////
    // interpret action
    /////////////////////////////////////////////////////////////////
    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptLycan.interpretAction(" + unit.getName() + ") " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // too close?
    /////////////////////////////////////////////////////////////////
    private boolean tooClose() {
        Unit close = getClosestEnemy();
        if (close == null) return false;
        return (BattleField.getDistance(close.getLocation(), unit.getLocation()) <= 3);
    }


    /////////////////////////////////////////////////////////////////
    // get closest enemy
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
    // where they at?
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


}
