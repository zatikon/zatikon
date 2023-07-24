///////////////////////////////////////////////////////////////////////
// Name: UnitEnchanter
// Desc: An enchanter
// Date: 7/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitEnchanter extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitEnchanter(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.ENCHANTER;
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_ENCHANTER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 3, Action.ATTACK_MAGIC_BALL);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_ENCHANTER;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // The good stuff
        actions.add(new ActionStunball(this, (byte) 0, (byte) 1, TargetType.ANY_AREA, (byte) 4, (byte) 4));
        actions.add(new ActionEmpower(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 1));
        //actions.add(new ActionArmor(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 1));

    }


}
