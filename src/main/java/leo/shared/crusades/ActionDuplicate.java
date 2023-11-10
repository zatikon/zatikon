///////////////////////////////////////////////////////////////////////
// Name: ActionDuplicate
// Desc: Become target creature
// Date: 4/17/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Stack;
import java.util.Vector;


public class ActionDuplicate implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private short max;
    private final short cost;
    private final String detail = Strings.ACTION_DUPLICATE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionDuplicate(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        range = 6;
        cost = 2;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the target
        Unit victim = owner.getCastle().getBattleField().getUnitAt(target);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_DUPLICATE_2;
        }


        // Get the new unit
        Unit newForm = Unit.getUnit(victim.getID(), owner.getCastle());
        newForm.add(new ActionUnduplicate(newForm));
        newForm.setID(UnitType.MIMIC);
        owner.die(false, owner);

        // Put the new unit in the old unit's place
        newForm.setLocation(owner.getLocation());
        newForm.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(newForm);
        owner.getCastle().addOut(owner.getTeam(), newForm);
        newForm.grow(owner.getBonus());

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(
                owner, Action.EFFECT_FADE);
        newForm.setHidden(true);
        owner.getCastle().getObserver().unitEffect(
                newForm, Action.EFFECT_FADE_IN);

        owner.getCastle().getObserver().playSound(Constants.SOUND_SHAPESHIFT);

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        owner.getBattleField().event(Event.WITNESS_ACTION, newForm, victim, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_DUPLICATE_3 + victim.getName();

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
    // Refine targets
    /////////////////////////////////////////////////////////////////
    private void refineTargets(Vector<Short> targets) {
        Stack<Short> removes = new Stack<Short>();
        for (int i = 0; i < targets.size(); i++) {
            Short byter = targets.elementAt(i);
            Unit unit = owner.getBattleField().getUnitAt(byter.byteValue());
            if (unit != null && (unit.getCastle() == getOwner().getCastle() || unit.getID() == UnitType.MIMIC || unit.getID() == UnitType.NONE))
                removes.add(byter);
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_DUPLICATE_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the range description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ANYWHERE_WITHIN + getRange();
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_DUPLICATE_5;
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
        return Strings.ACTION_DUPLICATE_6;
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
