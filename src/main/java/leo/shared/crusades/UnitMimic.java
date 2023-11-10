///////////////////////////////////////////////////////////////////////
// Name: UnitMimic
// Desc: A Mimic
// Date: 6/17/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitMimic extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMimic(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.MIMIC;
        category = Unit.SHAPESHIFTERS;
        name = Strings.UNIT_MIMIC_1;
        actions = new Vector<Action>();
        damage = 2;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_MIMIC;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Move the castle
        actions.add(new ActionDuplicate(this));
    }


}
