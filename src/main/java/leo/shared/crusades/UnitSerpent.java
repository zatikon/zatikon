///////////////////////////////////////////////////////////////////////
// Name: UnitSerpent
// Desc: A serpent
// Date: 6/10/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSerpent extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSerpent(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.SERPENT;
        name = Strings.UNIT_SERPENT_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_SERPENT;

        // do the venom
        EventPoison ep = new EventPoison(this);
        add((Event) ep);
        add((Action) ep);

        // fast reflexes
        EventVigilant ev = new EventVigilant(this);
        add((Event) ev);
        add((Action) ev);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
