///////////////////////////////////////////////////////////////////////
// Name: ActionSerpent
// Desc: Summon a serpent
// Date: 6/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class ActionSerpent implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private final Unit hiddenUnit;
    private final String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionSerpent(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.LOCATION_AREA;
        range = 1;
        hiddenUnit = new UnitSerpent(owner.getCastle());

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Temporary code to move around
        Unit serpent = new UnitSerpent(owner.getCastle());
        serpent.setLocation(owner.getLocation());
        serpent.setLocation(target);
        serpent.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(serpent);
        owner.getCastle().addOut(owner.getTeam(), serpent);

        // Generate a nifty effect
        owner.getCastle().getObserver().unitDamaged(owner, owner, (byte) 1);
        owner.getCastle().getObserver().playSound(Constants.SOUND_SQUISH);

        // The cost
        owner.deductActions((byte) 0);
        owner.getCastle().deductCommands(this, (byte) 1);
        owner.setLife((byte) (owner.getLife() - 1));

        // Check for victory
        Castle castle = owner.getBattleField().getCastle1();
        if (owner.getCastle() == castle) castle = owner.getBattleField().getCastle2();

        if (serpent.getLocation() == castle.getLocation()) {
            castle.getObserver().endGame(owner.getCastle());
            return serpent.getName() + Strings.ACTION_SERPENT_1;
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, serpent, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_SERPENT_2;


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
        return Strings.ACTION_SERPENT_3;
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
        return Strings.ACTION_SERPENT_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || owner.getLife() <= 1 || !owner.deployed())
            return 0;

        return owner.getLife();
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
        return Strings.ACTION_SERPENT_5;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
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
