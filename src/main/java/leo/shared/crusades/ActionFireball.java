///////////////////////////////////////////////////////////////////////
// Name: ActionFireball
// Desc: Kill it
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionFireball implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short max;
    private final short cost;
    private final short damage;
    private final short type;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionFireball(
            Unit newOwner,
            short newMax,
            short newCost,
            short newTargetType,
            short newRange,
            short newDamage,
            short newType) {
        max = newMax;
        remaining = max;
        cost = newCost;
        owner = newOwner;
        targetType = newTargetType;
        range = newRange;
        damage = newDamage;
        type = newType;

        // temp
        remaining = max;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        owner.deductActions(cost);
        remaining--;
        owner.getCastle().deductCommands(this, (byte) 1);

        Vector<Unit> victims = new Vector<Unit>();
        Vector<Short> damages = new Vector<Short>();

        // Get the targets
        Vector<Short> targets =
                owner.getBattleField().getArea(
                        owner,
                        target,
                        (byte) 1,
                        false,
                        true,
                        true,
                        true, TargetType.BOTH, owner.getCastle());

        short loc = owner.getLocation();

        // Hit the targets
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victim = owner.getBattleField().getUnitAt(location.byteValue());
            if (victim != null) {
                // get permission
                short result = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);

                if (result == Event.OK) {
                    // strike
                    result = victim.damage(owner, damage);

                    // send the witness event
                    owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                }

                // build up the lists
                victims.add(victim);
                damages.add(new Short(result));
            }
        }

        // Tell the observer
        owner.getCastle().getObserver().fireball(loc, target, Constants.IMG_EXPLOSION_1, victims, damages, type);

        // describe
        if (type == UnitType.DRAGON.value())
            return "" + owner.getName() + Strings.ACTION_FIREBALL_1;
        else if (type == UnitType.FIRE_ARCHER.value())
            return "" + owner.getName() + Strings.ACTION_FIREBALL_2;
        else
            return "" + owner.getName() + Strings.ACTION_FIREBALL_3;
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
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_FIREBALL_4 + damage;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ANYWHERE_WITHIN + getRange();
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        String text;
        text = Strings.COSTS + getCost() + Strings.ACTION;
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
        return Strings.ACTION_FIREBALL_5;
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
        return Strings.ACTION_FIREBALL_6 + damage + Strings.ACTION_FIREBALL_7;
    }
}
