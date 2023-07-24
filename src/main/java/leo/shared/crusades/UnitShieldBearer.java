///////////////////////////////////////////////////////////////////////
// Name: UnitShieldBearer
// Desc: A shield bearer
// Date: 4/7/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitShieldBearer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitShieldBearer(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.SHIELD_BEARER;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_SHIELD_BEARER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_SHIELD_BEARER;

        // parry
        EventBlock eb = new EventBlock(this);
        add((Event) eb);
        add((Action) eb);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
