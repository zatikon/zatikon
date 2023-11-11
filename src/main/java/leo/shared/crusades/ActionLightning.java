///////////////////////////////////////////////////////////////////////
//	Name:	ActionLightning
//	Desc:	Kill it
//	Date:	8/21/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class ActionLightning implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short max;
    private final short cost;
    //private short damage = 5;
    private final String detail = Strings.ACTION_LIGTNING_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionLightning(
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
        if (!validate(target)) {
            Logger.error(Strings.INVALID_ACTION);
            return Strings.INVALID_ACTION;
        }

        // The costs
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);

        // show it
        owner.getCastle().getObserver().lightning(owner.getLocation(), target);

        int slopeX = 0;
        int slopeY = 0;
        int targetX = BattleField.getX(target);
        int targetY = BattleField.getY(target);
        short myX = BattleField.getX(owner.getLocation());
        short myY = BattleField.getY(owner.getLocation());

        if (targetX > myX) slopeX = 1;
        if (targetX < myX) slopeX = -1;
        if (targetY > myY) slopeY = 1;
        if (targetY < myY) slopeY = -1;

        while (BattleField.getLocation(myX, myY) != target) {
            myX += slopeX;
            myY += slopeY;
            Unit unit = owner.getBattleField().getUnitAt(myX, myY);
            if (unit != null && unit.targetable(owner)) {

                // get permission
                short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, unit, Action.ATTACK, Event.NONE, Event.OK);

                if (outcome == Event.END) {
                    return "";
                }

                if (outcome == Event.OK) {
                    // hurt it
                    outcome = unit.damage(owner, (byte) 5);

                    // send the attacked event
                    owner.getBattleField().event(Event.WITNESS_ACTION, owner, unit, Action.ATTACK, Event.NONE, Event.OK);
                }

                // show it
                owner.getCastle().getObserver().attack(owner, unit, outcome, Action.ATTACK_NONE);
                if (unit.isDead())
                    owner.getCastle().getObserver().death(unit);
            }
        }

        return "" + owner.getName() + Strings.ACTION_LIGTNING_2;

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
        Vector<Short> targets = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.BOTH, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.BOTH, owner.getCastle());
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
            Short byter = it.next();
            if (owner.getLocation() == byter.byteValue()) removes.add(byter);
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return 5 + Strings.ACTION_LIGTNING_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ACTION_LIGTNING_4 + getRange() + Strings.ACTION_LIGTNING_5;
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
        return Strings.ACTION_LIGTNING_6;
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
