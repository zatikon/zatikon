///////////////////////////////////////////////////////////////////////
// Name: UnitQuartermaster
// Desc: A quartermaster
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitQuartermaster extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitQuartermaster(Castle newCastle) {
        castle = newCastle;

        // very high event priority
        eventPriority = 20;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.QUARTERMASTER;
        category = Unit.COMMANDERS;
        name = Strings.UNIT_QUARTERMASTER_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_QUARTERMASTER;

        // Add the ability description
        ActionTrait supplies = new ActionTrait(this, Strings.UNIT_QUARTERMASTER_2, Strings.UNIT_QUARTERMASTER_3, Strings.UNIT_QUARTERMASTER_4);
        supplies.setType(Action.SKILL);
        supplies.setDetail(Strings.UNIT_QUARTERMASTER_5);
        add(supplies);

        ActionTrait logistics = new ActionTrait(this, Strings.UNIT_QUARTERMASTER_6, Strings.UNIT_QUARTERMASTER_7, "");
        logistics.setDetail(Strings.UNIT_QUARTERMASTER_8);
        add(logistics);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Enter and exit
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().setLogistics((byte) (getCastle().getLogistics() + 1));
    }

    public void removed() {
        getCastle().setLogistics((byte) (getCastle().getLogistics() - 1));
    }


    /////////////////////////////////////////////////////////////////
    // End turn
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (castle.getBattleField() == null) return;
        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                if (victim.getOrganic(this) && victim.rested() && victim.targetable(this)) {
                    if (victim.heal(this)) {
                        // Generate a nifty effect
                        getCastle().getObserver().abilityUsed(getLocation(), victim.getLocation(), Constants.IMG_MIRACLE);

                    }
                }
            }
        }
    }
}
