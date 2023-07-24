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


public class UnitArchDemon extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitArchDemon(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.ARCHDEMON;
        name = Strings.UNIT_ARCH_DEMON_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_ARCH_DEMON;

        add(new ActionFlight(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
