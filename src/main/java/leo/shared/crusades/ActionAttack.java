///////////////////////////////////////////////////////////////////////
// Name: ActionAttack
// Desc: Kill it
// Date: 5/17/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionAttack implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String message = null;
    private final Unit owner;
    private final short targetType;
    private short range;
    private short remaining;
    private final short max;
    private final short cost;
    private int attackType = Action.ATTACK_MELEE;
    private final String detail = Strings.ACTION_ATTACK_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionAttack(
            Unit newOwner,
            short newMax,
            short newCost,
            short newTargetType,
            short newRange) {
        max = newMax;
        remaining = max;
        cost = newCost;
        owner = newOwner;
        targetType = newTargetType;
        range = newRange;

        // temp
        remaining = max;
    }

    /////////////////////////////////////////////////////////////////
    // Constructor for non-melee
    /////////////////////////////////////////////////////////////////
    public ActionAttack(
            Unit newOwner,
            short newMax,
            short newCost,
            short newTargetType,
            short newRange,
            int newAttackType) {
        max = newMax;
        remaining = max;
        cost = newCost;
        owner = newOwner;
        targetType = newTargetType;
        range = newRange;

        // temp
        remaining = max;
        attackType = newAttackType;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) {
            Logger.error(Strings.INVALID_ACTION);
            return Strings.INVALID_ACTION;
        }

        // The costs
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);

        // get the victim
        Unit victim = owner.getBattleField().getUnitAt(target);
        if (victim == null) return Strings.INVALID_ACTION;


        // get permission to strike
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
        short damage = outcome;

        if (outcome == Event.END) {
            return "";
        }

        if (outcome == Event.OK) {
            // hurt it
            damage = victim.damage(owner, owner.getDamage());
        }

        // if it stopped, we're done
        if (outcome == Event.CANCEL || outcome == Event.PARRY) {
            // show it
            owner.getCastle().getObserver().
                    attack(owner, victim, damage,
                            attackType);

            return owner.getName() + Strings.ACTION_ATTACK_2 + getName();
        }


        // send the witness event
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);

        // show it
        owner.getCastle().getObserver().
                attack(owner, victim, damage,
                        attackType);

        // finished
        return "" + owner.getName() + Strings.ACTION_ATTACK_3 + victim.getName();
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
        return message == null ? Strings.ACTION_ATTACK_4 : message;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        if (getTargetType() == TargetType.UNIT_AREA)
            return Strings.ACTION_ATTACK_5 + getRange();
        else
            return getRange() + Strings.RANGE;

    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        String text;

        text = Strings.COSTS + getCost() + Strings.ACTION;

        return text;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        if (getMax() <= 0)
            return (byte) (owner.getActionsLeft() / getCost());
        else
            return remaining;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        remaining = max;
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void setDescription(String newMessage) {
        message = newMessage;
    }

    public void setRange(short newRange) {
        range = newRange;
    }

    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_ATTACK_6;
    }

    public short getMax() {
        return max;
    }

    public short getCost() {
        return cost;
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
