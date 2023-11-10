///////////////////////////////////////////////////////////////////////
// Name: UnitCatapult
// Desc: A catapult
// Date: 7/30/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitCatapult extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitCatapult(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.CATAPULT;
        category = Unit.SIEGE;
        name = Strings.UNIT_CATAPULT_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        //attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 5, Action.ATTACK_STONE);
        attack = new ActionObliterate(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 5);
        deployCost = 3;
        castleCost = 150;
        organic = false;
        appearance = Constants.IMG_CATAPULT;

        add(new ActionInorganic(this));

        ActionTrait vaulted = new ActionTrait(this,
                Strings.UNIT_CATAPULT_2,
                Strings.UNIT_CATAPULT_3,
                "",
                "");
        vaulted.setDetail(Strings.UNIT_CATAPULT_4);
        add(vaulted);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
