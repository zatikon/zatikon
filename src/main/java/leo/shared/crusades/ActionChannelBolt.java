///////////////////////////////////////////////////////////////////////
//	Name:	ActionChannelBolt
//	Desc:	Bolt it with energy
//	Date:	4/26/2004 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionChannelBolt implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitChanneler owner;
    private final String detail = Strings.ACTION_CHANNEL_BOLT_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionChannelBolt(Unit newOwner) {
        owner = (UnitChanneler) newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) {
            Logger.error(Strings.INVALID_ACTION);
            return "";
        }

        owner.deductActions((byte) 0);
        owner.setEnergy(owner.getEnergy() - getCost());
        owner.getCastle().deductCommands(this, (byte) 1);

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);
        if (victim == null) return Strings.INVALID_ACTION;

        // get permission to strike
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
        short damage = outcome;

        if (outcome == Event.END) {
            return "";
        }

        // if its ok, hit it
        if (outcome == Event.OK) {
            damage = victim.damage(owner, (byte) 3);
        }

        owner.getCastle().getObserver().attack(owner, victim, damage, Action.ATTACK_CHANNEL_BOLT);

        // stop here if it's done
        if (outcome == Event.CANCEL) {
            return owner.getName() + Strings.ACTION_CHANNEL_BOLT_2 + getName();
        }

        // send the witness event
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);

        // The costs
        return "" + owner.getName() + Strings.ACTION_CHANNEL_BOLT_3 + victim.getName();

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        // Deployed yet?
        if (!owner.deployed()) return false;

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
        return Strings.ACTION_CHANNEL_BOLT_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the range description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ACTION_CHANNEL_BOLT_5 + getRange();
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_CHANNEL_BOLT_6;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        if (owner.getEnergy() > 100)
            return (byte) 100;
        else
            return (byte) (owner.getEnergy());

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
        return Strings.ACTION_CHANNEL_BOLT_7;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return 1;
    }

    public short getRange() {
        return 3;
    }

    public short getTargetType() {
        return TargetType.UNIT_AREA;
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
