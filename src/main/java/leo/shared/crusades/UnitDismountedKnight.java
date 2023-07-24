///////////////////////////////////////////////////////////////////////
// Name: UnitDismountedKnight
// Desc: An Dismounted Knight
// Date: 12/2/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDismountedKnight extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDismountedKnight(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.KNIGHT;
        name = Strings.UNIT_DISMOUNTED_KNIGHT_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_DISMOUNTED_KNIGHT;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
