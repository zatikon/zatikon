///////////////////////////////////////////////////////////////////////
// Name: ActionSpotter
// Desc: Assign a spotter
// Date: 5/27/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionSpotter implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitLongbowman owner;
    private final short targetType;
    private final short range;
    private final short cost;
    private final String detail = Strings.ACTION_SPOTTER_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionSpotter(Unit newOwner) {
        cost = 1;
        owner = (UnitLongbowman) newOwner;
        targetType = TargetType.UNIT_AREA;
        range = 4;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);

        // Watch the unit
        owner.setSpotter(victim);

        // audio + visual
        owner.getCastle().getObserver().playSound(Constants.SOUND_BOWPULL);
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_CLASH_1);

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);

        return "" + victim.getName() + Strings.ACTION_SPOTTER_2 + owner.getName();

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
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.FRIENDLY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, false, TargetType.FRIENDLY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_SPOTTER_3;
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
        return Strings.ACTION_SPOTTER_4;
    }

    public short getMax() {
        return 0;
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
        return Action.SKILL;
    }

    public String getDetail() {
        return detail;
    }
}
