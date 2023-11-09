///////////////////////////////////////////////////////////////////////
// Name: ActionToad
// Desc: Convert unit into a toad
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class ActionToad implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private short max;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionToad(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        range = 4;
        cost = 1;
        hiddenUnit = new UnitToad(owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Remove the old unit
        Unit old = owner.getCastle().getBattleField().getUnitAt(target);

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, old, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(old, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_TOAD_1;
        }

        // get rid of it
        short oldID = old.getID();
        old.die(false, owner);

        // Put a toad in it's place
        Unit toad = new UnitToad(old.getCastle());
        toad.setLocation(target);
        toad.setBattleField(old.getCastle().getBattleField());
        old.getCastle().getBattleField().add(toad);
        old.getCastle().addOut(old.getTeam(), toad);
        toad.grow(old.getBonus());
        toad.refresh();
        toad.setID(oldID);

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(
                old, Action.EFFECT_FADE);
        toad.setHidden(true);
        owner.getCastle().getObserver().unitEffect(
                toad, Action.EFFECT_FADE_IN);

        // Poof!
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_WHEEL);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, toad, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_TOAD_2 + old.getName();

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

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
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_TOAD_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ANYWHERE_WITHIN + getRange();
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_TOAD_4;
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
    // Refine the targets
    /////////////////////////////////////////////////////////////////
    private void refineTargets(Vector<Short> targets) {
        Stack<Short> removes = new Stack<Short>();
        Iterator<Short> it = targets.iterator();
        while (it.hasNext()) {
            Short byter = it.next();
            Unit unit = owner.getBattleField().getUnitAt(byter.byteValue());
            if (unit != null && unit instanceof UnitToad) removes.add(byter);
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_TOAD_5;
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
        return hiddenUnit;
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
