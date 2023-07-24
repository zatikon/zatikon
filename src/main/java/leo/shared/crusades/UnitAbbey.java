///////////////////////////////////////////////////////////////////////
// Name: UnitAbbey
// Desc: An abbey
// Date: 12/28/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitAbbey extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAbbey(Castle newCastle) {
        castle = newCastle;

        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.ABBEY;
        category = Unit.STRUCTURES;
        name = Strings.UNIT_ABBEY_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 1;
        actionsMax = 1;
        move = null;
        attack = null;
        deployCost = 2;
        castleCost = 100;
        organic = false;
        appearance = Constants.IMG_ABBEY;
        opaque = true;

        add(new ActionInorganic(this));

        // Add the ability description
        ActionTrait monastery = new ActionTrait(this, Strings.UNIT_ABBEY_2, Strings.UNIT_ABBEY_3, Strings.UNIT_ABBEY_4);
        monastery.setDetail(Strings.UNIT_ABBEY_5);
        add(monastery);

        // Salvation
        actions.add(new ActionSalvation(this));
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


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    @Override
    public Vector<Short> getBonusCastleTargets(Unit looker) {
        if (looker.getCastle() != getCastle()) return null;
        if (!(looker.getCategory() == Unit.CULTISTS || looker.getCategory() == Unit.CLERGY)) return null;

        // Get the targets
        Vector<Short> targets = getBattleField().getArea(
                this,
                getLocation(),
                (byte) 1,
                true,
                false,
                true,
                true, TargetType.BOTH, getCastle());

        return targets;
    }
}
