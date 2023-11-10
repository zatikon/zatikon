///////////////////////////////////////////////////////////////////////
// Name: ActionBanish
// Desc: Send unit back to the castle
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionBanish implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short max;
    private final short cost;
    private final String detail = Strings.ACTION_BANISH_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionBanish(
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
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_BANISH_2;
        }

        boolean summoned = victim instanceof UnitSummon || (victim instanceof UnitWolf && ((UnitWolf) victim).isSummoned());

        // Add to the castle
        if (!summoned)
            victim.getCastle().add(Unit.getUnit(victim.getID(), victim.getCastle()));

        // Out of the field...
        victim.die(false, owner);

        // Generate a nifty effect
        if (!summoned) {
            if (target != victim.getCastle().getLocation())
                owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_BANISH);
            if (owner.getID() == UnitType.SHAMAN.value())
                owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_MASK);
            else
                owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_POOF);

            // Warn the client about an addition
            owner.getCastle().getObserver().castleAddition(victim);
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_BANISH_3 + victim.getName();

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
        Logger.error(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, false, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_BANISH_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        if (getRange() <= 1)
            return "1" + Strings.RANGE;
        else
            return Strings.ANYWHERE_WITHIN + getRange();
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
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_BANISH_5;
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
        return Action.SPELL;
    }

    public String getDetail() {
        return detail;
    }
}
