///////////////////////////////////////////////////////////////////////
// Name: UnitGeomancer
// Desc: A Geomancer
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGeomancer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGeomancer(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.GEOMANCER.value();
        category = Unit.NATURE;
        name = Strings.UNIT_GEOMANCER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_GEOMANCER;

        // Add the actions
        actions.add(move);

        // Move the castle
        actions.add(new ActionEntomb(this));
        actions.add(new ActionCastle(this));
    }


}
