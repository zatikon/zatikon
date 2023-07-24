///////////////////////////////////////////////////////////////////////
// Name: UnitImp
// Desc: An imp
// Date: 7/10/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitImp extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitImp(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.IMP;
        name = Strings.UNIT_IMP_1;
        actions = new Vector<Action>();
        damage = 2;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_IMP;

        add(new ActionFlight(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
