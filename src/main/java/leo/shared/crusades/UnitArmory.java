///////////////////////////////////////////////////////////////////////
// Name: UnitArmory
// Desc: An armory
// Date: 6/5/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitArmory extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitArmory(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.ARMORY.value();
        category = Unit.STRUCTURES;
        name = Strings.UNIT_ARMORY_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 4;
        castleCost = 400;
        organic = false;
        appearance = Constants.IMG_ARMORY;
        opaque = true;

        // Add the ability description
        add(new ActionInorganic(this));
        ActionTrait arsenal = new ActionTrait(this, Strings.UNIT_ARMORY_2, Strings.UNIT_ARMORY_3, Strings.UNIT_ARMORY_4, Strings.UNIT_ARMORY_5);
        arsenal.setDetail(Strings.UNIT_ARMORY_6);
        actions.add(arsenal);
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().addPermArmor((byte) 1);
        getCastle().addPermPower((byte) 1);
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
        getCastle().addPermArmor((byte) -1);
        getCastle().addPermPower((byte) -1);
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        short range = 2;
        Vector<Short> targets =
                getCastle().getBattleField().getArea(
                        this,
                        getCastle().getLocation(),
                        range,
                        true,
                        false,
                        false,
                        false, TargetType.BOTH, getCastle());
        getCastle().getBattleField().addBonusCastleTargets(this, targets, getCastle());
        return targets;
    }

    public int getDeployRange() {
        return 2;
    }

}
