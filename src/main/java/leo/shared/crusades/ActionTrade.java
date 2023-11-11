///////////////////////////////////////////////////////////////////////
// Name: ActionTrade
// Desc: Trade this unit for another
// Date: 10/25/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class ActionTrade implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitChangeling owner;
    private final short targetType;
    private final short range;
    private final short cost;
    private final String detail = Strings.ACTION_TRADE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionTrade(Unit newOwner, short newRange) {
        targetType = TargetType.UNIT_AREA;
        cost = 2;
        owner = (UnitChangeling) newOwner;
        range = newRange;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) {
            Logger.error(Strings.INVALID_ACTION);
            return Strings.INVALID_ACTION;
        }

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);
        Unit me = owner;

        Castle ownerCastle = me.getCastle();
        Castle victimCastle = victim.getCastle();
        short ownerTeam = me.getTeam();
        short victimTeam = victim.getTeam();

        // The costs
        me.deductActions(cost);
        ownerCastle.deductCommands(this, (byte) 1);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_TRADE_2;
        }

        // Switch sides
        ownerCastle.removeOut(me);
        me.setCastle(victimCastle);
        victimCastle.addOut(victimTeam, me);

        victimCastle.removeOut(victim);
        victim.setCastle(ownerCastle);
        ownerCastle.addOut(ownerTeam, victim);
        //victim.reDeploy();
        if (victim.getSummonManager() != null)
            victim.getSummonManager().summonerDies();
        if (victim.getSoulmate() != null) {
            victim.getSoulmate().die(false, owner);
            if (victim.getSoulmate().isDead())
                owner.getCastle().getObserver().death(victim.getSoulmate()); // if they're dead, show it
            victim.die(false, owner);
            if (victim.isDead()) owner.getCastle().getObserver().death(victim); // if they're dead, show it
        }

        // Trade places
        short oldLocation = me.getLocation();
        victim.setLocation(me.getLocation());
        me.setLocation(target);

        owner.trade();

        // Generate a nifty effect
        me.getCastle().getObserver().abilityUsed(oldLocation, target, Constants.IMG_POOF);
        me.getCastle().getObserver().abilityUsed(oldLocation, oldLocation, Constants.IMG_POOF);

        owner.getBattleField().event(Event.WITNESS_ACTION, me, victim, getType(), Event.NONE, Event.OK);

        // done
        me.refresh();
        return "" + victim.getName() + Strings.ACTION_TRADE_3 + owner.getName();

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
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, targetType, range, false, true, false, TargetType.ENEMY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, (byte) (getTargetType() - 6), range, false, true, false, TargetType.ENEMY, owner.getCastle());
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
            if (unit != null && (unit.getCastle() == getOwner().getCastle() || unit instanceof UnitChangeling))
                removes.add(byter);
        }

        while (removes.size() > 0)
            targets.remove(removes.pop());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_TRADE_4;
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
        return "2" + Strings.ACTION;
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
        return Strings.ACTION_TRADE_5;
    }

    public short getCost() {
        return cost;
    }

    public short getMax() {
        return 0;
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
        return Action.SPELL;
    }

    public String getDetail() {
        return detail;
    }
}
