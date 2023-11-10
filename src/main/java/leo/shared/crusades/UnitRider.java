///////////////////////////////////////////////////////////////////////
// Name: UnitRider
// Desc: An ultra-light horseman
// Date: 7/15/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitRider extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitRider(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.RIDER;
        category = Unit.HORSEMEN;
        name = Strings.UNIT_RIDER_1;
        actions = new Vector<Action>();
        damage = 2;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE, (byte) 5);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_RIDER;

        add(new ActionMounted(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
