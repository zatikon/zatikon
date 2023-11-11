///////////////////////////////////////////////////////////////////////
// Name: ActionSkeleton
// Desc: Summon a skeleton
// Date: 8/1/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Vector;


public class ActionSkeleton implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitNecromancer owner;
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
    public ActionSkeleton(UnitNecromancer newOwner) {
        owner = newOwner;
        targetType = TargetType.LOCATION_AREA;
        range = 3;
        cost = 1;
        hiddenUnit = new UnitSkeleton(owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Temporary code to move around
        UnitSkeleton skeleton = new UnitSkeleton(owner.getCastle());
        skeleton.setParent(owner);
        owner.getCastle().checkPowerUpPickup(skeleton, owner.getCastle().getBattleField().getUnitAt(target));
        skeleton.setLocation(target);
        skeleton.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(skeleton);
        owner.getCastle().addOut(owner.getTeam(), skeleton);

        // Assigns the summon to the owners summoning manager
        owner.getSummonManager().recieveSummon(skeleton);

        // Animation
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_SUMMON_BLUE_1);

        // The cost
        // Check for free grave action
        Vector<Short> graves = owner.getBattleField().getGraves();
        if (graves.contains(target)) {
            owner.getBattleField().removeGrave(target);
        } else {
            owner.deductActions(cost);
        }

        owner.getCastle().deductCommands(this, (byte) 1);

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (skeleton.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return skeleton.getName() + Strings.ACTION_SKELETON_1;
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, skeleton, getType(), Event.NONE, Event.OK);


        return "" + owner.getName() + Strings.ACTION_SKELETON_2;

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
        boolean freeOnly = (owner.getActionsLeft() <= 0);
        Vector<Short> targets = owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 3, false, false, false, TargetType.MOVE, owner.getCastle());
        Vector<Short> graves = owner.getBattleField().getGraves();
        Vector<Short> locations = new Vector<Short>();
        Vector<Unit> units = owner.getCastle().getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (!victim.isPowerUp())
                locations.add(new Short(victim.getLocation()));
        }
        //targets.removeAll(graves);
        if (freeOnly) targets.clear();
        targets.addAll(graves);
        targets.removeAll(locations);
        return targets;
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
        return Strings.ACTION_SKELETON_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return ""; //getRange() + Strings.RANGE;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return ""; //Strings.ACTION_SKELETON_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed() || !owner.getSummonManager().canSummon())
            return 0;

        if (getMax() <= 0)
            return ((owner.getBattleField().getGraves().size() > 0) ? (byte) 1 : (byte) (owner.getActionsLeft() / getCost()));
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
        return Strings.ACTION_SKELETON_5;
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
