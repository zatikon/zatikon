///////////////////////////////////////////////////////////////////////
// Name: ActionGhost
// Desc: Summon a ghost
// Date: 9/13/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionGhost implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitDiabolist owner;
    private final short targetType;
    private final short range;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionGhost(Unit newOwner) {
        owner = (UnitDiabolist) newOwner;
        targetType = TargetType.LOCATION_AREA;
        range = 1;
        cost = 1;

        hiddenUnit = new UnitGhost(owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Temporary code to move around
        Unit ghost = new UnitGhost(owner.getCastle());
        ghost.setLocation(target);
        ghost.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(ghost);
        owner.getCastle().addOut(owner.getTeam(), ghost);

        // Generate a nifty effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_WHEEL);

        // The cost
        owner.deductActions(cost);
        owner.setSouls(owner.getSouls() - 2);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, ghost, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_GHOST_1 + (BattleField.getX(target) + 1) + ", " + (BattleField.getY(target) + 1);

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
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 1, false, false, false, TargetType.BOTH, owner.getCastle());
        removeCastle(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Remove castle targets
    /////////////////////////////////////////////////////////////////
    public void removeCastle(Vector<Short> targets) {
        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        for (int i = 0; i < targets.size(); i++) {
            Short byter = targets.elementAt(i);
            if (byter.byteValue() == castle.getLocation()) {
                targets.remove(byter);
                return;
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
    public String getDescription() {
        return Strings.ACTION_GHOST_2;
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
        return Strings.ACTION_GHOST_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed() || owner.getSouls() < 2)
            return 0;

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
        return Strings.ACTION_GHOST_4;
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
