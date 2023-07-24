///////////////////////////////////////////////////////////////////////
// Name: EventHeal
// Desc: Heal a damaged unit reactively
// Date: 3/11/2009 - Gabe Jones 
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventHeal implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = WITNESS_DAMAGE;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final int priority = 350;
    private int charges = 1;
    private final String detail = Strings.EVENT_HEAL_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventHeal(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        // initialize the values
        Unit damager = source;
        Unit victim = target;
        short inflicted = val1;

        // some tests
        if (charges <= 0) return Event.OK;
        if (damager.getCastle() == owner.getCastle()) return Event.OK;
        if (victim.getCastle() != owner.getCastle()) return Event.OK;
        if (inflicted <= 0) return Event.OK;
        if (!victim.getOrganic(owner)) return Event.OK;
        if (!victim.targetable(owner)) return Event.OK;
        if (owner.isStunned()) return Event.OK;
        if (owner.isDead()) return Event.OK;
        if (victim.isDead()) return Event.OK;
        if (victim.getLife() < 1) return Event.OK;
        if (!victim.isWounded()) return Event.OK;

        // decrement
        charges--;

        // heal'm
        victim.heal(owner);

        // visual effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), victim.getLocation(), Constants.IMG_STAR);

        // all done
        return Event.OK;
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
        return Strings.EVENT_HEAL_2;
    }

    public String getRangeDescription() {
        return Strings.EVENT_HEAL_3;
    }

    public String getCostDescription() {
        return charges + Strings.EVENT_HEAL_4;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_HEAL_5;
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
