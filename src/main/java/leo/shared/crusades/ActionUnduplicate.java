///////////////////////////////////////////////////////////////////////
// Name: ActionUndiplicate
// Desc: Return to mimic form
// Date: 8/24/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionUnduplicate implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = Strings.ACTION_UNDUPLICATE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionUnduplicate(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 1;
        hiddenUnit = new UnitMimic(owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Transform into a spirit
        owner.die(false, owner);

        // Revert to mimic
        Unit doppel = new UnitMimic(owner.getCastle());
        doppel.setLocation(owner.getLocation());
        doppel.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(doppel);
        owner.getCastle().addOut(owner.getTeam(), doppel);
        doppel.grow(owner.getBonus());

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(
                owner, Action.EFFECT_FADE);
        doppel.setHidden(true);
        owner.getCastle().getObserver().unitEffect(
                doppel, Action.EFFECT_FADE_IN);

        owner.getCastle().getObserver().playSound(Constants.SOUND_SHAPESHIFT);

        // The cost
        owner.remove(this);
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, doppel, doppel, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_UNDUPLICATE_2;

    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
        //return owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 1, false, false, false);
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
    public String getRangeDescription() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_UNDUPLICATE_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_UNDUPLICATE_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((!owner.freelyActs() && owner.getCastle().getCommandsLeft() <= 0) || !owner.deployed() || !owner.getOrganic(owner))
            return 0;

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
        return Strings.ACTION_UNDUPLICATE_5;
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
