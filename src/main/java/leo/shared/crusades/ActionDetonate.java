///////////////////////////////////////////////////////////////////////
// Name: ActionDetonate
// Desc: Kill it
// Date: 9/13/2004 - Gabe Jones
//       12/14/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionDetonate implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private final short damage;
    private final short cost;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionDetonate(
            Unit newOwner,
            short newRange,
            short newDamage) {
        cost = 1;
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        range = newRange;
        damage = newDamage;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) { //System.out.println("validate");
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The costs
        //System.out.println("the costs");
        owner.deductActions(cost);
        if (owner.getID() == UnitType.DIABOLIST.value())
            owner.setSouls(owner.getSouls() - 1);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the victim
        //System.out.println("get the victim");
        Unit victim = owner.getBattleField().getUnitAt(target);

        // get the outcome
        //System.out.println("get the outcome");
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        //System.out.println("if its a fizzle, end");
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_DETONATE_1;
        }


        // Dont credit them for the detonated unit
        //int tmpSouls = owner.getSouls();

        // Kill the victim
        //System.out.println("Kill the victim");
        victim.die(true, owner);
        //if (victim.isDead()) owner.getCastle().getObserver().death(victim);

        // Back to the tmp value
        //owner.setSouls(tmpSouls);

        // BOOM!
        //System.out.println("your head asplode");
        //owner.getCastle().getObserver().fireball(owner.getLocation(), target, Constants.IMG_FIREBALL);
        owner.getCastle().getObserver().areaEffect(owner.getLocation(), target, Action.AOE_DETONATE, victim);

        // Get the targets
        //System.out.println("get dem targets");
        Vector<Short> targets =
                owner.getBattleField().getArea(
                        owner,
                        target,
                        (byte) 1,
                        false,
                        true,
                        true,
                        true, TargetType.BOTH, owner.getCastle());


        // Hit the targets
        //System.out.println("hit dem targets");
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victimer = owner.getBattleField().getUnitAt(location.byteValue());
            if (victimer != null && location != target) {
                short result = victimer.damage(owner, damage);
                owner.getCastle().getObserver().text(victimer.getName() + Strings.ACTION_DETONATE_2 + result);
                owner.getCastle().getObserver().attack(owner, victimer, result, Action.ATTACK_NONE);

            }
        }

        // Hit the center
        //Unit victimer = owner.getBattleField().getUnitAt(target);
        //if (victimer != null)
        //{ short result = victimer.damage(owner, damage);
        // owner.getCastle().getObserver().text(victimer.getName() + Strings.ACTION_DETONATE_3 + result);
        // owner.getCastle().getObserver().attack(owner,victimer,result,Action.ATTACK_NONE);
        //}
        //System.out.println("observer stuff");
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);
        //System.out.println("finally!");
        return "" + owner.getName() + Strings.ACTION_DETONATE_4 + victim.getName();

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left & not planning on detonating yourself?
        //System.out.println("Any actions left & not at the same location");
        if (getRemaining() <= 0 && target != owner.getLocation()) return false;

        // Deployed yet?
        //System.out.println("Depolyed yet?");
        if (!owner.deployed()) return false;

        // If no target, all is well
        //System.out.println("If no target, all is well");
        if (getTargetType() == TargetType.NONE) return true;

        // Search for the target
        //System.out.println("Search for the target");
        Vector<Short> targets = getTargets();
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            if (location.byteValue() == target) return true;
        }

        // Bad bad monkey
        //System.out.println("Bad bad monkey");
        Logger.error(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, false, TargetType.BOTH, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_DETONATE_5 + damage;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ACTION_DETONATE_6 + getRange();
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_DETONATE_7;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() { //System.out.println("owner.noCost()");
        if (owner.noCost()) return 1;
        //System.out.println("DIABOLIST check");
        if (owner.getID() == UnitType.DIABOLIST.value() && owner.getSouls() < 1) return 0;
        //System.out.println("commands left <=0 && doesn't freely act or not depolyed");
        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;
        //System.out.println("ret actions/cost");
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
        return Strings.ACTION_DETONATE_8;
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
        return Action.SPELL;
    }

    public String getDetail() {
        return "The target of this evil spell dies by exploding, damaging each surrounding unit for " + damage + ". This affects enemies and allies.";
    }
}
