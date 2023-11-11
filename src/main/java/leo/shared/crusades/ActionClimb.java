///////////////////////////////////////////////////////////////////////
// Name: ActionClimb
// Desc: Climb the wall
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class ActionClimb implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private short remaining;
    private final short range;
    private short max;
    private final short cost;
    private final String detail = Strings.ACTION_CLIMB_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionClimb(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        cost = 1;
        range = 1;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the wall unit
        Unit wall = owner.getCastle().getBattleField().getUnitAt(target);

        // Store the old location
        short oldLocation = owner.getLocation();

        // check for prevention
        if (owner.getCastle().getBattleField().event(Event.PREVIEW_MOVE, owner, null, oldLocation, target, Event.OK) == Event.CANCEL)
            return "";

        // set the new location
        owner.setLocation(target);

        // Remove the old unit
        wall.die(false, owner);
        owner.die(false, owner);

        // Put a wall mason
        UnitWallMason wallMason = new UnitWallMason(wall.getCastle());
        wallMason.setWall(wall);
        wallMason.setMason(owner);
        wallMason.setLocation(oldLocation);
        wallMason.setLocation(target);
        wallMason.setBattleField(wall.getCastle().getBattleField());
        wall.getCastle().getBattleField().add(wallMason);
        wall.getCastle().addOut(owner.getTeam(), wallMason);

        owner.getCastle().getObserver().selectUnit(wallMason);

        wallMason.getCastle().getBattleField().event(Event.WITNESS_MOVE, wallMason, null, oldLocation, target, Event.OK);

        // Generate a nifty effect
        // owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_MASK);
        owner.getCastle().getObserver().unitEffect(wallMason, Action.EFFECT_WALL);

        return "" + owner.getName() + Strings.ACTION_CLIMB_2 + wall.getName();

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
        Vector<Short> targets =
                owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, false, true, TargetType.FRIENDLY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, false, true, TargetType.FRIENDLY, owner.getCastle());
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
            Unit unit = owner.getBattleField().getUnitAt(byter.byteValue());
            if (unit != null && unit.getID() != Unit.WALL) removes.add(byter);
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_CLIMB_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "1" + Strings.RANGE;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return "1" + Strings.ACTION;
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
        return Strings.ACTION_CLIMB_4;
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
        return Action.MOVE;
    }

    public String getDetail() {
        return detail;
    }
}
