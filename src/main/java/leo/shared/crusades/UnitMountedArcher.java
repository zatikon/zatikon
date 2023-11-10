///////////////////////////////////////////////////////////////////////
// Name: UnitMountedArcher
// Desc: Mounted archer
// Date: 5/24/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitMountedArcher extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMountedArcher(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.MOUNTED_ARCHER;
        category = Unit.HORSEMEN;
        name = Strings.UNIT_MOUNTED_ARCHER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 3, Action.ATTACK_ARROW);
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_MOUNTED_ARCHER;

        add(new ActionMounted(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
