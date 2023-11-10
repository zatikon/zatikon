///////////////////////////////////////////////////////////////////////
// Name: UnitGolem
// Desc: A golem
// Date: 4/24/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGolem extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGolem(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.GOLEM.value();
        category = Unit.SIEGE;
        name = Strings.UNIT_GOLEM_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 9;
        lifeMax = 9;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionObliterate(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 200;
        organic = false;
        appearance = Constants.IMG_GOLEM;

        add(new ActionInorganic(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
