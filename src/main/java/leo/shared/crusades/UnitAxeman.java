///////////////////////////////////////////////////////////////////////
// Name: UnitAxeman
// Desc: A axeman
// Date: 10/2/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitAxeman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAxeman(Castle newCastle) {
        castle = newCastle;

        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.AXEMAN;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_AXEMAN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_AXEMAN;

        // cleave armor
        EventCleave ec = new EventCleave(this);
        add((Event) ec);
        add((Action) ec);

        // slayer
        EventSlay es = new EventSlay(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
