///////////////////////////////////////////////////////////////////////
// Name: ActionInvincible
// Desc: Templar toggles invincibility
// Date: 8/23/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionInvincible implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitTemplar owner;
    private final String detailOn = Strings.ACTION_INVINCIBLE_1;
    private final String detailOff = Strings.ACTION_INVINCIBLE_2;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionInvincible(Unit newOwner) {
        owner = (UnitTemplar) newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        owner.toggleIndestructible();

        // Generate a nifty effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), owner.getLocation(), Constants.IMG_STAR);

        // The costs
        owner.deductActions((byte) 1);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, owner, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + (owner.getIndestructible() ? Strings.ACTION_INVINCIBLE_3 : Strings.ACTION_INVINCIBLE_4);
    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        // Deployed yet?
        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return new Vector<Short>();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return (owner.getIndestructible() ? Strings.ACTION_INVINCIBLE_5 : Strings.ACTION_INVINCIBLE_6);
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return (owner.getIndestructible() ? Strings.ACTION_INVINCIBLE_7 : Strings.ACTION_INVINCIBLE_8);
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.getLocation() == owner.getCastle().getLocation()) return 0;

        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        return owner.getActionsLeft();

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
        return Strings.ACTION_INVINCIBLE_9;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return 1;
    }

    public short getRange() {
        return 0;
    }

    public short getTargetType() {
        return TargetType.NONE;
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
        return owner.getIndestructible() ? detailOff : detailOn;
    }
}
