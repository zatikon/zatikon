///////////////////////////////////////////////////////////////////////
// Name: ActionWallWalk
// Desc: Walk along the wall
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


public class ActionWallWalk implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitWallMason owner;
    private final short targetType;
    private short remaining;
    private final short range;
    private short max;
    private final short cost;
    private final String detail = Strings.ACTION_WALL_WALK_1;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionWallWalk(UnitWallMason newOwner) {
        owner = newOwner;
        targetType = TargetType.LOCATION_LINE;
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

        // Get the wall
        Unit wall = owner.getCastle().getBattleField().getUnitAt(target);

        short oldLocation = owner.getLocation();
        short oldLife = owner.getLife();

        if (wall != null) {
            owner.setLife(wall.getLife());
            wall.die(false, owner);
            owner.setLocation(target);
            owner.getCastle().getBattleField().move(owner, oldLocation, target);
            owner.getCastle().getObserver().unitEffect(owner, Action.EFFECT_WALL);
        } else {
            if (owner.getCastle().getBattleField().event(Event.PREVIEW_MOVE, owner, null, oldLocation, target, Event.OK) == Event.CANCEL) {
                return "";
            }

            owner.setLocation(target);

            owner.die(false, owner);
            Unit mason = owner.getCastle().getBattleField().getUnitAt(target);
            mason.setLocation(oldLocation);
            mason.setLocation(target);
            mason.getCastle().getBattleField().event(Event.WITNESS_MOVE, mason, null, oldLocation, target, Event.OK);
            owner.getCastle().getObserver().selectUnit(mason);
        }

        // Leave behind the wall
        Unit newWall = new UnitWall(owner.getCastle());
        newWall.setLife(oldLife);
        newWall.setLocation(oldLocation);
        newWall.setBattleField(owner.getCastle().getBattleField());
        newWall.getCastle().getBattleField().add(newWall);
        newWall.getCastle().addOut(owner.getWall().getTeam(), newWall);


        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (owner.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return owner.getName() + Strings.ACTION_MOVE_2;
        }

        // Generate a nifty effect
        // owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_MASK);

        return "" + owner.getName() + Strings.ACTION_WALL_WALK_2;
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
        Vector<Short> targets = owner.getBattleField().getTargets(owner, TargetType.UNIT_AREA, getRange(), false, false, true, TargetType.FRIENDLY, owner.getCastle());
        targets.addAll(owner.getBattleField().getTargets(owner, TargetType.LOCATION_LINE, getRange(), false, false, false, TargetType.BOTH, owner.getCastle()));
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getBattleField().getTargets(owner, (byte) (TargetType.UNIT_AREA - 6), getRange(), false, false, true, TargetType.FRIENDLY, owner.getCastle());
        targets.addAll(owner.getBattleField().getTargets(owner, TargetType.LOCATION_LINE, getRange(), false, false, false, TargetType.BOTH, owner.getCastle()));
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
        return Strings.ACTION_WALL_WALK_3;
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
        return Strings.ACTION_WALL_WALK_4;
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
