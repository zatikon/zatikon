///////////////////////////////////////////////////////////////////////
// Name: UnitSwordsman
// Desc: A swordsman
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSwordsman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSwordsman(Castle newCastle) {
        castle = newCastle;

        // parry goes first
        eventPriority = 11;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.SWORDSMAN;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_SWORDSMAN_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_SWORDSMAN;

        // parry
        EventParry ep = new EventParry(this, 1);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
