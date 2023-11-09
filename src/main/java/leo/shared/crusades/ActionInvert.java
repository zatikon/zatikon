///////////////////////////////////////////////////////////////////////
//	Name:	ActionInvert
//	Desc:	Kill it
//	Date:	5/19/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Vector;


public class ActionInvert implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private short range;
    //private short damage;
    private final short cost;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionInvert(Unit newOwner) {
        cost = 1;
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        //damage = 4;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the victim
        Unit victim = owner.getBattleField().getUnitAt(target);

        // invert it!
        invert(victim);

        return "" + owner.getName() + Strings.ACTION_INVERT_1 + victim.getName();
    }


    /////////////////////////////////////////////////////////////////
    // Inversion processor
    /////////////////////////////////////////////////////////////////
    private void invert(Unit victim) {
        // Kill the victim
        victim.die(false, owner);

        // BOOM!
        owner.getCastle().getObserver().areaEffect(owner.getLocation(), victim.getLocation(), Action.AOE_EXPLOSION, victim);

        // Get the targets
        Vector<Short> targets =
                owner.getBattleField().getArea(
                        owner,
                        victim.getLocation(),
                        (byte) 1,
                        false,
                        true,
                        true,
                        true, TargetType.BOTH, owner.getCastle());

        // Hit the targets
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victimer = owner.getBattleField().getUnitAt(location.byteValue());
            if (victimer != null) {
                if (victimer instanceof Conjuration) {
                    invert(victimer);
                } else {

                    short result = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victimer, Action.ATTACK, Event.NONE, Event.OK);
                    if (result == Event.OK) {
                        result = victimer.damage(owner, (byte) 4);
                        owner.getCastle().getObserver().text(victimer.getName() + Strings.ACTION_INVERT_2 + result);
                        owner.getCastle().getObserver().attack(owner, victimer, result, Action.ATTACK_NONE);
                        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victimer, Action.ATTACK, Event.NONE, Event.OK);
                    }
                }
            }
        }
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
    private Vector<Short> getConjurations() {
        Vector<Short> targets = new Vector<Short>();
        Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit tmp = it.next();
            if (tmp instanceof Conjuration && tmp.getCastle() == owner.getCastle())
                targets.add(new Short(tmp.getLocation()));
        }
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return getConjurations();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getConjurations();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_INVERT_3 + 4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ANYWHERE_WITHIN;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return "1" + Strings.COSTS;
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
        return Strings.ACTION_INVERT_4;
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
        return Action.ATTACK;
    }

    public String getDetail() {
        return Strings.ACTION_INVERT_5;
    }
}
