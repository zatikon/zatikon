///////////////////////////////////////////////////////////////////////
//	Name:	ActionHydraAttack
//	Desc:	Kill it
//	Date:	4/27/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionHydraAttack implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitHydra owner;
    private final short targetType;
    private final short range;
    private short used;
    private final String detail = Strings.ACTION_HYDRA_ATTACK_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionHydraAttack(
            Unit newOwner,
            short newTargetType,
            short newRange) {
        owner = (UnitHydra) newOwner;
        targetType = newTargetType;
        range = newRange;

        // temp
        used = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) {
            System.out.println(Strings.INVALID_ACTION);
            return "";
        }

        // The costs
        used++;
        owner.deductActions((byte) 0);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);
        if (victim == null) return Strings.INVALID_ACTION;

        // get permission to strike
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
        short damage = outcome;

        if (outcome == Event.END) {
            return "";
        }

        // if it's ok, hit it
        if (outcome == Event.OK) {
            damage = victim.damage(owner, owner.getDamage());
        }

        // show it
        owner.getCastle().getObserver().attack(owner, victim, damage, Action.ATTACK_MELEE);

        // stop here if it's done
        if (outcome == Event.CANCEL) {
            return owner.getName() + Strings.ACTION_HYDRA_ATTACK_2 + getName();
        }

        // send the witness event
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);

        // all done
        return "" + owner.getName() + Strings.ACTION_HYDRA_ATTACK_3 + victim.getName() + Strings.ACTION_HYDRA_ATTACK_4 + damage;

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        // Deployed yet?
        if (!owner.deployed()) return false;

        // If no target, all is well
        if (getTargetType() == TargetType.NONE) return true;

        // Search for the target
        Vector<Short> targets = getTargets();
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            if (location.byteValue() == target) return true;
        }

        // Bad bad monkey
        //System.out.println(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_HYDRA_ATTACK_5;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return getRange() + Strings.RANGE;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        String text;
        text =
                Strings.ACTION_HYDRA_ATTACK_6 +
                        getRemaining() + "/" +
                        getMax() +
                        Strings.ACTION_HYDRA_ATTACK_7;
        return text;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;
        return (byte) (getMax() - used);
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        used = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_HYDRA_ATTACK_8;
    }

    public short getMax() {
        return (byte) (6 - owner.getMissingHeads());
    }

    public short getCost() {
        return 0;
    }

    public short getRange() {
        return range;
    }

    public short getTargetType() {
        return targetType;
    }

    public Unit getOwner() {
        return owner;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public boolean passive() {
        return false;
    }

    public short getType() {
        return Action.ATTACK;
    }

    public String getDetail() {
        return detail;
    }
}
