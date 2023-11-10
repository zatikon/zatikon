///////////////////////////////////////////////////////////////////////
// Name: UnitToad
// Desc: A toad
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitToad extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitToad(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.TOAD;
        name = Strings.UNIT_TOAD_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_TOAD;

        // Add the actions
        actions.add(move);
    }


}
