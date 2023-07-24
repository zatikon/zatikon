///////////////////////////////////////////////////////////////////////
// Name: ActionDamn
// Desc: Summon a nastly little demon from any unit
// Date: 7/8/2011
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class ActionDamn implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitSummoner owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private short max;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////////////////
    public ActionDamn(UnitSummoner newOwner) {
        owner = newOwner;
        targetType = TargetType.UNIT_AREA;
        range = 2;
        cost = 1;
        hiddenUnit = new UnitDemon(owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Remove the old unit
        Unit old = owner.getCastle().getBattleField().getUnitAt(target);

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, old, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(old, Action.EFFECT_FIZZLE);
            return "" + owner.getName() + Strings.ACTION_DAMN_1;
        }

        // do it
        short oldID = old.getID();
        //owner.getSummonManager().unlinkSummon(old); // Removes the imp/demon from the Summoner's summon manager
        old.die(false, owner);
        UnitDemon demon = new UnitDemon(owner.getCastle());
        // new unit based on old unit type
        //if (oldID == Unit.DEMON){ demon = new UnitArchDemon(owner.getCastle()); }// Demon -> Archdemon
        //else { demon = new UnitDemon(owner.getCastle()); } // If not already a demon, MAKE A DEMON
        //demon.setID(oldID);
        demon.setParent(owner);
        demon.setLocation(target);
        demon.setBattleField(owner.getCastle().getBattleField());
        demon.getCastle().getBattleField().add(demon);
        demon.getCastle().addOut(owner.getTeam(), demon);
        demon.grow(old.getBonus());
        owner.getSummonManager().recieveSummon(demon); // Add the new demon/archdemon to the summon manager

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(
                old, Action.EFFECT_FADE);
        demon.setHidden(true);
        demon.getCastle().getObserver().unitEffect(
                demon, Action.EFFECT_FADE_IN);

        // Animation
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_SUMMON_1);

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (demon.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return demon.getName() + Strings.ACTION_DAMN_2;
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, old, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_DAMN_3 + old.getName();

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        //if (!owner.deployed()) return false;

        //if (owner.getBattleField().getUnitAt(target).getID() != Unit.IMP ) return false;

        // If no target, all is well
        if (getTargetType() == TargetType.NONE) return true;

  /*Unit victim = owner.getBattleField().getUnitAt(target);
  if (victim == owner) return false;*/

        // Search for the target
        Vector<Short> targets = getTargets();
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            if ((location.byteValue() == target || (owner.getBattleField().getUnitAt(target).getID() == Unit.IMP && owner.getBattleField().getUnitAt(target).getOrganic(owner))) && summonOverride(owner.getBattleField().getUnitAt(target)))
                return true;
        }

        // Bad bad monkey
        //System.out.println(Strings.INVALID_ACTION);
        return false;
    }

    // Asks if summoner could summon, OR if the target unit is already one of the summoner's summons.
    public boolean summonOverride(Unit target) {
        return (owner.getSummonManager().canSummon() || owner.getSummonManager().getSummonList().contains(target));
    }

    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, targetType, range, false, true, false, TargetType.BOTH, owner.getCastle());
        Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == owner.getCastle() && victim instanceof UnitImp) {
                if (victim.getOrganic(victim) && summonOverride(victim))
                    targets.add(new Short(victim.getLocation()));
            }
        }

        Vector<Short> removers = new Vector<Short>();
        Iterator<Short> targs = targets.iterator();
        while (targs.hasNext()) {
            short targ = targs.next();
            Unit victim = owner.getCastle().getBattleField().getUnitAt(targ);
            if (victim == null) continue;
            if (!summonOverride(victim))
                removers.add(targ);
        }
        targets.removeAll(removers);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, (byte) (getTargetType() - 6), range, false, true, false, TargetType.BOTH, owner.getCastle());
        Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == owner.getCastle() && victim instanceof UnitImp) {
                if (victim.getOrganic(victim))
                    targets.add(new Short(victim.getLocation()));
            }
        }

        Vector<Short> removers = new Vector<Short>();
        Iterator<Short> targs = targets.iterator();
        while (targs.hasNext()) {
            short targ = targs.next();
            Unit victim = owner.getCastle().getBattleField().getUnitAt(targ);
            if (victim == null) continue;
            if (!summonOverride(victim))
                removers.add(targ);
        }
        targets.removeAll(removers);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_DAMN_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ACTION_DAMN_5;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_DAMN_6;
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
        return Strings.ACTION_DAMN_7;
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
