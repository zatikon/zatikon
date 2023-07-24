///////////////////////////////////////////////////////////////////////
// Name: UnitFanatic
// Desc: A fanatic
// Date: 11/4/2008 (election day) - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitFanatic extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitFanatic(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.FANATIC;
        category = Unit.CULTISTS;
        name = Strings.UNIT_FANATIC_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 3;
        lifeMax = 3;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 2);
        deployCost = 2;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_FANATIC;

        // swap spots
        EventZeal ez = new EventZeal(this);
        add((Event) ez);
        add((Action) ez);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}

