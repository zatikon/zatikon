///////////////////////////////////////////////////////////////////////
// Name: UnitZombie
// Desc: A zombie. Wants to eat your... ham.
// Date: 8/1/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitZombie extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitZombie(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.ZOMBIE;
        name = Strings.UNIT_ZOMBIE_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_ZOMBIE;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
