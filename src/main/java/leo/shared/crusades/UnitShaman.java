///////////////////////////////////////////////////////////////////////
// Name: UnitShaman
// Desc: A shaman
// Date: 8/29/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitShaman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitShaman(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.SHAMAN;
        category = Unit.NATURE;
        name = Strings.UNIT_SHAMAN_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 2, Action.ATTACK_MAGIC_BALL);
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_SHAMAN;

        // add the walk stopper
        EventGuardian guardian = new EventGuardian(this);
        add((Event) guardian);
        add((Action) guardian);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
