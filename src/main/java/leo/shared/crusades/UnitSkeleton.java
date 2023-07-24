///////////////////////////////////////////////////////////////////////
// Name: UnitSkeleton
// Desc: A skeleton
// Date: 8/1/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSkeleton extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSkeleton(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.SKELETON;
        name = Strings.UNIT_SKELETON_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_SKELETON;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
