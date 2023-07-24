///////////////////////////////////////////////////////////////////////
// Name: ActionWerewolf
// Desc: Transform into a werewolf
// Date: 4/27/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionWerewolf implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionWerewolf(Unit newOwner) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 0;
        hiddenUnit = new UnitWerewolf(owner.getCastle(), false);
    }

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionWerewolf(Unit newOwner, boolean real) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 0;
        hiddenUnit = null;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Transform into a werewolf
        owner.die(false, owner);

        // Put a werewolf in it's place
        Unit werewolf = new UnitWerewolf(owner.getCastle());
        werewolf.setID(owner.getID());
        werewolf.setLocation(owner.getLocation());
        werewolf.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(werewolf);
        owner.getCastle().addOut(owner.getTeam(), werewolf);
        werewolf.grow(owner.getBonus());
        if (owner.getSoulmate() != null) {
            if (owner.getSoulmate() instanceof UnitSycophant) {
                ((UnitSycophant) owner.getSoulmate()).setMaster(werewolf, false);
                owner.getSoulmate().setSoulmate(werewolf);
                werewolf.setSoulmate(owner.getSoulmate());
            }
        }

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(
                owner, Action.EFFECT_FADE);
        werewolf.setHidden(true);
        owner.getCastle().getObserver().unitEffect(
                werewolf, Action.EFFECT_FADE_IN);

        owner.getCastle().getObserver().playSound(Constants.SOUND_WOLF);

        // The cost
        owner.remove(this);
        owner.deductActions(cost);
        //owner.getCastle().deductCommands(this, (byte)1);

        owner.getBattleField().event(Event.WITNESS_ACTION, werewolf, werewolf, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_WEREWOLF_1;

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
    public String getDescription() {
        return Strings.ACTION_WEREWOLF_2;
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
        return Strings.ACTION_WEREWOLF_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if (!owner.deployed()) return 0;

        return (byte) 1;
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
        return Strings.ACTION_WEREWOLF_4;
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
