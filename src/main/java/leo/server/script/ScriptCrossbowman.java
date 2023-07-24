///////////////////////////////////////////////////////////////////////
//      Name:   ScriptInterceptor
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
import leo.shared.Unit;
import leo.shared.crusades.UnitCrossbowman;

public class ScriptCrossbowman extends ScriptInterceptor {
    private final UnitCrossbowman crossbowman;
    private final Game game;

    public ScriptCrossbowman(Unit newUnit, Game newGame, Castle newCastle, Castle newEnemyCastle, boolean newCanRandom) {
        super(newUnit, newGame, newCastle, newEnemyCastle, newCanRandom);
        crossbowman = (UnitCrossbowman) newUnit;
        game = newGame;
    }


    public void perform() {
        if (crossbowman.getAttack() == null) {
            reload();
        }
        super.perform();
    }

    /////////////////////////////////////////////////////////////////
    // Spirit
    /////////////////////////////////////////////////////////////////
    private void reload() {
        if (game.over()) return;
        if (!crossbowman.ready()) return;

        // Make the unit do it
        interpretAction(crossbowman.getAction(crossbowman.getReload()), crossbowman.getLocation(), Action.NOTHING);
        //crossbowman.process(crossbowman.getAction(crossbowman.getReload()), crossbowman.getLocation(),Action.NOTHING);
        Log.game("Artificial Opponent: " + crossbowman.process(crossbowman.getAction(crossbowman.getReload()), crossbowman.getLocation(), Action.NOTHING));
    }


    private void interpretAction(short action, short actor, short target) {
        try {
            game.interpretAction(null, action, actor, target);
            Thread.sleep(game.getDelay());
        } catch (Exception e) {
            Log.error("ScriptCrossbowman.interpretAction " + e);
        }
    }

}
