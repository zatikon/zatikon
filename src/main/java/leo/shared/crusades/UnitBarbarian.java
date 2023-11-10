///////////////////////////////////////////////////////////////////////
// Name: UnitBarbarian
// Desc: A barbarian
// Date: 3/21/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBarbarian extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBarbarian(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.BARBARIAN.value();
        category = Unit.NATURE;
        name = Strings.UNIT_BARBARIAN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_BARBARIAN;

        // add rage
        EventRage rage = new EventRage(this);
        add((Event) rage);
        add((Action) rage);

        // Add the actions
        actions.add(move);
        actions.add(attack);

    }
}
