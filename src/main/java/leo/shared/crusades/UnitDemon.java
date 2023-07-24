///////////////////////////////////////////////////////////////////////
// Name: UnitDemon
// Desc: An demon
// Date: 7/10/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDemon extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDemon(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.DEMON;
        name = Strings.UNIT_DEMON_1;
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
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_DEMON;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
