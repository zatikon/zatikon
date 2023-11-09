///////////////////////////////////////////////////////////////////////
// Name: ActionPoison
// Desc: Poison an enemy
// Date: 7/19/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class ActionPoison implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private final short cost;
    private final String detail = Strings.ACTION_POISON_1;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionPoison(Unit newOwner, short newRange) {
        targetType = TargetType.UNIT_LINE;
        cost = 1;
        owner = newOwner;
        range = newRange;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the victim
        Unit victim = owner.getBattleField().getUnitAt(target);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SKILL, Event.NONE, Event.OK);

        if (outcome == Event.END) {
            return "";
        }

        if (outcome == Event.OK) {
            victim.poison();

            // Generate a nifty effect
            owner.getCastle().getObserver().attack(owner, victim, Event.DEAD, Action.ATTACK_BLOWGUN);
        }

        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().attack(owner, victim, outcome, Action.ATTACK_BLOWGUN);
            return owner.getName() + Strings.ACTION_POISON_2 + getName();
        }

        // send the witness event
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.SKILL, Event.NONE, Event.OK);

        return owner.getName() + Strings.ACTION_POISON_3 + victim.getName();
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
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, targetType, range, false, true, false, TargetType.ENEMY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, (byte) (getTargetType() - 6), range, false, true, false, TargetType.ENEMY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Refine targets
    /////////////////////////////////////////////////////////////////
    private void refineTargets(Vector<Short> targets) {
        Stack<Short> removes = new Stack<Short>();
        Iterator<Short> it = targets.iterator();
        while (it.hasNext()) {
            Short loc = it.next();
            Unit dude = owner.getBattleField().getUnitAt(loc);
            if (dude != null && dude.isPoisoned())
                removes.add(loc);

        }
        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_POISON_4;
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
        return "Costs 1" + Strings.ACTION;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        return (byte) (owner.getActionsLeft() / getCost());
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
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
        return Strings.ACTION_POISON_5;
    }

    public short getCost() {
        return cost;
    }

    public short getMax() {
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
        return Action.SKILL;
    }

    public String getDetail() {
        return detail;
    }
}
