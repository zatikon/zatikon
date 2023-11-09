///////////////////////////////////////////////////////////////////////
// Name: ActionGate
// Desc: Summon a gate
// Date: 5/19/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionGate implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitConjurer owner;
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
    public ActionGate(UnitConjurer newOwner) {
        owner = newOwner;
        targetType = TargetType.LOCATION_AREA;
        range = 4;
        cost = 1;
        hiddenUnit = new UnitGate(owner.getCastle());

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Temporary code to move around
        UnitGate gate = new UnitGate(owner.getCastle());
        gate.setParent(owner);
        owner.getCastle().checkPowerUpPickup(gate, owner.getCastle().getBattleField().getUnitAt(target));
        gate.setLocation(target);
        gate.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(gate);
        owner.getCastle().addOut(owner.getTeam(), gate);

        // Assigns the summon to the owners summoning manager
        owner.getSummonManager().recieveSummon(gate);

        // Generate a nifty effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_POOF);

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (gate.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return gate.getName() + Strings.ACTION_GATE_1;
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, gate, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_GATE_2;

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        if (!owner.deployed()) return false;

        // Checks against summon flooding.
        if (!(owner.getSummonManager().canSummon())) return false;

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
        Vector<Short> targs = owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, getRange(), false, false, false, TargetType.MOVE, owner.getCastle());
        targs.remove((Short) owner.getCastle().getBattleField().getCastle1().getLocation());
        targs.remove((Short) owner.getCastle().getBattleField().getCastle2().getLocation());
        return targs;
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
        return Strings.ACTION_GATE_3;
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
        return Strings.ACTION_GATE_4;
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
        return Strings.ACTION_GATE_5;
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
