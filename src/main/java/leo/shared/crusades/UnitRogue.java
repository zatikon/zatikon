///////////////////////////////////////////////////////////////////////
// Name: UnitRogue
// Desc: A rogue
// Date: 9/1/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitRogue extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitRogue(Castle newCastle) {
        castle = newCastle;

        // dodge first
        eventPriority = 10;

        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.ROGUE;
        category = Unit.SCOUTS;
        name = Strings.UNIT_ROGUE_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_ROGUE;

        // dodgey
        EventDodge ed = new EventDodge(this);
        add((Event) ed);
        add((Action) ed);

        EventStun es = new EventStun(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
