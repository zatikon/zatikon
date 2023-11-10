///////////////////////////////////////////////////////////////////////
// Name: UnitWizard
// Desc: 
// Date: 7/15/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWizard extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWizard(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.WIZARD;
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_WIZARD_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_AREA, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 3, Action.ATTACK_MAGIC_BALL);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_WIZARD;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // The good stuff
        //actions.add(new ActionHaste(this, (byte) 0, (byte) 2, TargetType.UNIT_AREA, (byte) 1));
        actions.add(new ActionSwitch(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 3));
        actions.add(new ActionFly(this));

    }


}
