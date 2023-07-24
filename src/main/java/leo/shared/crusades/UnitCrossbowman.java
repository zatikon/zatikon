///////////////////////////////////////////////////////////////////////
// Name: UnitCrossbowman
// Desc: An crossbowman
// Date: 8/22/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitCrossbowman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action reload;
    private final ActionCrossbow crossbow;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitCrossbowman(Castle newCastle) {
        castle = newCastle;

        // forbidden!
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.CROSSBOWMAN;
        category = Unit.ARCHERS;
        name = Strings.UNIT_CROSSBOWMAN_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        crossbow = new ActionCrossbow(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 4);
        attack = crossbow;
        deployCost = 1;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_CROSSBOWMAN;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        reload = crossbow.getReload();
    }


    public Action getReload() {
        return reload;
    }

    public int getMaxRange() {
        return 4;
    }

}
