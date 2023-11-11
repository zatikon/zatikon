///////////////////////////////////////////////////////////////////////
//	Name:	ActionSuicide
//	Desc:	Kill it
//	Date:	5/30/2004 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Strings;
import leo.shared.TargetType;
import leo.shared.Unit;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionSuicide implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitWillWisps owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short max;
    private final short cost;
    private final String detail = Strings.ACTION_SUICIDE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionSuicide(
            UnitWillWisps newOwner,
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

        // temp
        remaining = max;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) {
            Logger.error(Strings.INVALID_ACTION);
            return Strings.INVALID_ACTION;
        }

        // The costs
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the target
        Unit victim = owner.getBattleField().getUnitAt(target);
        if (victim == null) return Strings.INVALID_ACTION;

        owner.vol.kaboom(target);

        // get permission to strike
		/*short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                short damage = outcome;		

		if (outcome == Event.END)
		{	
		       return "";
		}

		// if its ok, hit it
		if (outcome == Event.OK)
		{
			damage = victim.damage(owner, owner.getDamage());
		}

		owner.getCastle().getObserver().attack(owner,victim,damage,Action.ATTACK_WISP);

		// stop here if it's done
		if (outcome == Event.CANCEL)
		{
			owner.die(true, owner);
			return owner.getName() + Strings.ACTION_SUICIDE_2 + getName();
		}

		// send the witness event
		owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);

		// all done
		*/
        owner.die(true, owner);
        return "" + owner.getName() + Strings.ACTION_SUICIDE_3 + victim.getName() + Strings.ACTION_SUICIDE_4;
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
        //System.out.println(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_SUICIDE_5;
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
        String text;
        text =
                Strings.COSTS +
                        getCost() +
                        Strings.ACTION_SUICIDE_6;
        return text;

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
        return Strings.ACTION_SUICIDE_7;
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
        return Action.ATTACK;
    }

    public String getDetail() {
        return detail;
    }
}
