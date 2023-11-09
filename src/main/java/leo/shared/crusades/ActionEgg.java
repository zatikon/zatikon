///////////////////////////////////////////////////////////////////////
// Name: ActionEgg
// Desc: Lay an egg
// Date: 7/14/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionEgg implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitWyvern owner;
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
    public ActionEgg(Unit newOwner) {
        owner = (UnitWyvern) newOwner;
        targetType = TargetType.LOCATION_AREA;
        range = 1;
        cost = 0;
        hiddenUnit = new UnitEgg(owner.getCastle(), false);

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Temporary code to move around
        Unit egg = new UnitEgg(owner.getCastle());
        egg.setLocation(owner.getLocation());
        egg.setLocation(target);
        //egg.unDeploy();
        egg.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(egg);
        owner.getCastle().addOut(owner.getTeam(), egg);
        //if(owner.getBonus().contains(Action.GROW_RELIC_CLOCKWORK)){egg.grow(Action.GROW_RELIC_CLOCKWORK)}
//  egg.setStorage(owner.getBonus());  // TODO - broken, commented out
        egg.grow(owner.getBonus());

        // Generate a nifty effect
        owner.getCastle().getObserver().playSound(Constants.SOUND_SQUISH);
        owner.feed(false);

        // The cost
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (egg.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return egg.getName() + Strings.ACTION_EGG_1;
        }

        return "" + owner.getName() + Strings.ACTION_EGG_2;

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

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
        return owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 1, false, false, false, TargetType.BOTH, owner.getCastle());
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
        return Strings.ACTION_EGG_3;
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
        return Strings.ACTION_EGG_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if (!owner.fed()) return 0;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        return 1;
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
        return Strings.ACTION_EGG_5;
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
        return Action.SKILL;
    }

    public String getDetail() {
        return detail;
    }
}
