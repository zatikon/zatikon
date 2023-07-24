///////////////////////////////////////////////////////////////////////
// Name: UnitFootman
// Desc: A footman
// Date: 5/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitFootman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitFootman(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.FOOTMAN;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_FOOTMAN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionRush(this, (byte) 3);
        deployCost = 0;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_FOOTMAN;

        // Add the event
        EventInterference interference = new EventInterference(this);
        add((Event) interference);
        add((Action) interference);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
