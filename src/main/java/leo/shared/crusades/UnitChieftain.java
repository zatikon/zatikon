///////////////////////////////////////////////////////////////////////
// Name: UnitChieftain
// Desc: A chieftain
// Date: 4/18/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitChieftain extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitChieftain(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.CHIEFTAIN;
        category = Unit.NATURE;
        name = Strings.UNIT_CHIEFTAIN_1;
        actions = new Vector<Action>();
        damage = 6;
        armor = 1;
        life = 6;
        lifeMax = 6;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 1, Action.ATTACK_MELEE);
        deployCost = 3;
        castleCost = 300;
        organic = true;
        appearance = Constants.IMG_CHIEFTAIN;

        // add the walk stopper
        EventGoad eg = new EventGoad(this);
        add((Event) eg);
        add((Action) eg);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
