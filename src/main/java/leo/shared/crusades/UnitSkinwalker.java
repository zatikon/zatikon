///////////////////////////////////////////////////////////////////////
// Name: UnitSkinwalker
// Desc: A skinwalker
// Date: 10/26/2006 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSkinwalker extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private ActionTrait trait;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSkinwalker(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.SKINWALKER;
        category = Unit.SHAPESHIFTERS;
        name = Strings.UNIT_SKINWALKER_1;
        actions = new Vector<Action>();
        damage = 2;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_SKINWALKER;

        // important! (not anymore)
        //skinwalking = true;

        actions.add(move);
        actions.add(attack);

        grow(Action.GROW_SKINWALKING);
    }
}
