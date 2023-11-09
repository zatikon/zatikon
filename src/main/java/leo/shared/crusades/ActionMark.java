///////////////////////////////////////////////////////////////////////
// Name: ActionMark
// Desc: Mark a unit for death
// Date: 4/1/2009 - Gabe Jones
//       10/8/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Vector;


public class ActionMark implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner; // revert this to bounty hunter if it fails
    private final short targetType;
    private final short range;
    private final short cost;
    private final String detail;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionMark(Unit newOwner) {
        cost = 1;
        owner = newOwner; // this too
        targetType = TargetType.UNIT_AREA;
        // Bounty Hunter's range is 4, this is a quickfix to make the Conspirator's range 5 (4 after patch)
        if (owner.getID() == Unit.CONSPIRATOR) {
            range = 4;
        } else {
            range = 4;
        }
        //Check if owner is Bounty Hunter or Conspirator
        if (owner.getID() == Unit.CONSPIRATOR) {
            detail = Strings.ACTION_MARK_6;
        } else {
            detail = Strings.ACTION_MARK_1;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SKILL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FIZZLE);
            return owner.getName() + Strings.ACTION_MARK_2;
        }

        // Watch the unit
        owner.setMark(victim);
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_TARGET_01);
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_MARK_3 + victim.getName();

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

        if (!owner.getBattleField().getUnitAt(target).getOrganic(owner.getBattleField().getUnitAt(target)))
            return false;

        if (owner.getID() == Unit.CONSPIRATOR) {
            // Won't let you target summons or doppelganger twin because skill doesn't activate on them because of false death,
            //   so it would be pointless to target them
            if (owner.getBattleField().getUnitAt(target) == null || owner.getBattleField().getUnitAt(target).getID() == Unit.NONE)
                return false;
        }

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
        Vector<Short> targs = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.ENEMY, owner.getCastle());
        if (owner.getID() == Unit.CONSPIRATOR) {
            Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit victim = it.next();
                if (victim.getID() == Unit.NONE) // Conspirator won't target summons or doppelganger twin since wouldn't have effect anyways
                    targs.remove((Short) victim.getLocation());
            }
        }
        return targs;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targs = owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, false, TargetType.ENEMY, owner.getCastle());
        if (owner.getID() == Unit.CONSPIRATOR) {
            Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit victim = it.next();
                if (victim.getID() == Unit.NONE) // Conspirator won't target summons or doppelganger twin since wouldn't have effect anyways
                    targs.remove((Short) victim.getLocation());
            }
        }
        return targs;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_MARK_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        if (getRange() <= 1)
            return getRange() + Strings.RANGE;
        else
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
        return Strings.ACTION_MARK_5;
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
