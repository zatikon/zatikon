///////////////////////////////////////////////////////////////////////
// Name: UnitBear
// Desc: A bear wants to eat your... ham.
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBear extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBear(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.BEAR;
        name = Strings.UNIT_BEAR_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_BEAR;

        EventStun es = new EventStun(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
