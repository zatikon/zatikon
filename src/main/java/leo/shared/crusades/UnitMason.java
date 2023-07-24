///////////////////////////////////////////////////////////////////////
// Name: UnitMason
// Desc: A Mason
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitMason extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMason(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.MASON;
        category = Unit.SIEGE;
        name = Strings.UNIT_MASON_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 2;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_MASON;

        // Add the actions
        actions.add(move);

        // Build walls
        actions.add(new ActionBuild(this));

        actions.add(new ActionClimb(this));

    }


}
