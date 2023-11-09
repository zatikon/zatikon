///////////////////////////////////////////////////////////////////////
// Name: ActionMove
// Desc: Move the unit around
// Date: 5/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionWyvernMove implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitWyvern owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short max;
    private final short cost;
    private final String detail = Strings.ACTION_MOVE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionWyvernMove(
            UnitWyvern newOwner,
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
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        //short res = owner.getBattleField().event(Event.PREVIEW_MOVE, owner, null, getType(), Event.NONE, Event.OK);
        //if (res == Event.CANCEL) return "";

        // do the move
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);
        owner.setMeal(owner.getBattleField().getUnitAt(target));
        short loc = owner.getLocation();
        short res = owner.getCastle().getBattleField().move(owner, owner.getLocation(), target);
        if (res == Event.CANCEL) return "";

        // The cost
        //short outcomeMove = owner.getBattleField().event(Event.PREVIEW_MOVE, owner, null, owner.getLocation(), target, Event.OK);


        // ************* //


        if (!owner.isDead()) {
            // Check for victory
            Castle castle = owner.getBattleField().getCastle1();
            if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

            if (owner.getLocation() == castle.getLocation()) {
                castle.getObserver().endGame(owner.getCastle());
                return owner.getName() + Strings.ACTION_MOVE_2;
            }
        }

        return "" + owner.getName() + Strings.ACTION_MOVE_3 + (BattleField.getX(target) + 1) + ", " + (BattleField.getY(target) + 1);
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
        Vector<Short> targets = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.ENEMY, owner.getCastle());
        //if (!owner.canWin())
        Vector<Short> mtargets = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, false, false, TargetType.MOVE, owner.getCastle());
        if (!owner.canWin()) removeCastle(targets);
        for (int i = 0; i < mtargets.size(); i++) {
            if (!targets.contains(mtargets.get(i))) targets.add(mtargets.get(i));
        }
        removeCastle(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Remove castle targets
    /////////////////////////////////////////////////////////////////
    public void removeCastle(Vector<Short> targets) {
        // Check for victory
        Castle mycastle = owner.getBattleField().getCastle1();
        Castle castle = owner.getBattleField().getCastle2();
        if (owner.getCastle() == castle) {
            castle = mycastle;
            mycastle = owner.getBattleField().getCastle2();
        }

        for (int i = 0; i < targets.size(); i++) {
            Short byter = targets.elementAt(i);
            if (!owner.canWin() && byter.byteValue() == castle.getLocation()) {
                targets.remove(byter);
            } else if (byter.byteValue() == mycastle.getLocation()) {
                targets.remove(byter);
            }
        }
    }

    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getTargets();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() { //String addon = " and devours unit.";
        if (getTargetType() == TargetType.LOCATION_LINE_JUMP)
            return Strings.ACTION_MOVE_4;
        else
            return Strings.ACTION_MOVE_5;
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
        String text;
        if (getCost() > 0) {
            text =
                    Strings.COSTS +
                            getCost();

            if (getCost() == 1) text = text + Strings.ACTION;
            if (getCost() > 1) text = text + Strings.ACTION;
        } else {
            text =
                    Strings.ACTION_MOVE_6 +
                            getRemaining() + "/" +
                            getMax() +
                            Strings.ACTION_MOVE_7;
        }
        return text;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((!owner.freelyActs() && owner.getCastle().getCommandsLeft() <= 0) || !owner.deployed()) return 0;

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
        return Strings.ACTION_MOVE_8;
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
