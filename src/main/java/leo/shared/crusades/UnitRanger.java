///////////////////////////////////////////////////////////////////////
// Name: UnitRanger
// Desc: An ranger
// Date: 7/8/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitRanger extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitRanger(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.RANGER;
        category = Unit.SCOUTS;
        name = Strings.UNIT_RANGER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 3, Action.ATTACK_ARROW);
        deployCost = 0;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_RANGER_WOLF_RANGED;
        freelyActs = true;

        ActionTrait selfReliant = new ActionTrait(this, Strings.UNIT_RANGER_2, Strings.UNIT_RANGER_3, "");
        selfReliant.setDetail(Strings.UNIT_RANGER_4);
        add(selfReliant);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Add the ability to swap weapons
        actions.add(new ActionWeaponSwitch(this, getAttack()));

        // Add special wolf action
        actions.add(new ActionWolf(this));
    }


    public int getMaxRange() {
        return 3;
    }


}
