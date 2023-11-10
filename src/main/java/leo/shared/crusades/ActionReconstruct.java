///////////////////////////////////////////////////////////////////////
// Name: ActionReconstruct
// Desc: Reconstruct units
// Date: 12/23/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class ActionReconstruct implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short cost;
    private final String detail = Strings.ACTION_RECONSTRUCT_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionReconstruct(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 1;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Raise
        Castle castle = owner.getCastle();
        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                if (!victim.getOrganic(owner) && victim.getID() > UnitType.POWERUP) {
                    if (victim.heal(owner)) {
                        // Generate a nifty effect
                        castle.getObserver().abilityUsed(owner.getLocation(), victim.getLocation(), Constants.IMG_POOF);
                    }
                }
            }
        }

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        return "" + owner.getName() + Strings.ACTION_RECONSTRUCT_2;

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
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
    public String getDescription() {
        return Strings.ACTION_RECONSTRUCT_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "";
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
        return Strings.ACTION_RECONSTRUCT_4;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return cost;
    }

    public short getRange() {
        return 0;
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
        return Action.OTHER;
    }

    public String getDetail() {
        return detail;
    }
}
