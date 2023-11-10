///////////////////////////////////////////////////////////////////////
// Name: EventRebirth
// Desc: Bring dead units back
// Date: 3/7/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventRebirth implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_DEATH;
    private final short type = Action.SPELL;
    private int charges = 1;
    private final Unit owner;
    private final int priority = 100;
    private final String detail = Strings.EVENT_REBIRTH_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventRebirth(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;
        boolean death = val1 == Event.TRUE;

        if (!death) return Event.NONE;
        if (owner.isStunned()) return Event.NONE;
        if (charges < 1) return Event.NONE;
        if (!corpse.getOrganic(owner)) return Event.NONE;
        if (!corpse.targetable(owner)) return Event.NONE;
        if (corpse.getID() == UnitType.NONE.value()) return Event.NONE;
        if (corpse.getCastle() != owner.getCastle()) return Event.NONE;

        // yank it from the graveyard. if it's not there, no rebirth
        if (!corpse.getCastle().getGraveyard().remove(corpse)) return Event.NONE;
        //corpse.getCastle().getGraveyard().remove(corpse);

        // ok, all the tests are back. time to be reborn
        charges--;

        Unit newUnit = Unit.getUnit(corpse.getID(), owner.getCastle());
        newUnit.setTeam(corpse.getTeam());
        owner.getCastle().add(newUnit);

        // the visual effect
        owner.getCastle().getObserver().attack(owner, corpse, owner.getCastle().getLocation(), Action.ATTACK_SPIRIT);

        // warn the client something is up
        owner.getCastle().getObserver().castleAddition(newUnit);

        return Event.NONE;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        charges = 1;
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Get the event type
    /////////////////////////////////////////////////////////////////
    public short getEventType() {
        return eventType;
    }


    /////////////////////////////////////////////////////////////////
    // Get the owner
    /////////////////////////////////////////////////////////////////
    public Unit getOwner() {
        return owner;
    }


    /////////////////////////////////////////////////////////////////
    // Get priority
    /////////////////////////////////////////////////////////////////
    public int getPriority() {
        return priority;
    }


    /////////////////////////////////////////////////////////////////
    // Descriptions
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.EVENT_REBIRTH_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return charges + Strings.EVENT_REBIRTH_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_REBIRTH_4;
    }

    public String perform(short target) {
        return "";
    }

    public boolean validate(short target) {
        return false;
    }

    public Vector<Short> getTargets() {
        return new Vector<Short>();
    }

    public Vector<Short> getClientTargets() {
        return getTargets();
    }

    public short getRemaining() {
        return (byte) 0;
    }

    public short getMax() {
        return (byte) 0;
    }

    public short getCost() {
        return (byte) 0;
    }

    public short getRange() {
        return (byte) 0;
    }

    public short getTargetType() {
        return (byte) 0;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public boolean passive() {
        return true;
    }

    public short getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}
