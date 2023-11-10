///////////////////////////////////////////////////////////////////////
// Name: UnitTower
// Desc: A tower
// Date: 4/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitTower extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitTower(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.TOWER.value();
        category = Unit.STRUCTURES;
        name = Strings.UNIT_TOWER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 2;
        actionsMax = 2;
        move = null;
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 4, Action.ATTACK_ARROW);
        deployCost = 4;
        castleCost = 150;
        organic = false;
        appearance = Constants.IMG_TOWER;
        opaque = true;

        add(new ActionInorganic(this));

        EventVigilant ev = new EventVigilant(this);
        add((Event) ev);
        add((Action) ev);

        // Add the attack
        actions.add(attack);
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
