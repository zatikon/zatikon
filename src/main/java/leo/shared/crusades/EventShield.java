///////////////////////////////////////////////////////////////////////
// Name: EventShield
// Desc: Absorb the shield
// Date: 3/9/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Constants;
import leo.shared.Event;
import leo.shared.Unit;


public class EventShield implements Event {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DAMAGE;
    private final UnitAcolyte owner;
    private final int priority = 100;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventShield(UnitAcolyte newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        // initialize the values
        Unit damager = source;
        Unit victim = target;
        short inflicted = recursive;

        // do some tests
        if (owner.isStunned()) return inflicted;
        if (inflicted <= 0) return inflicted;
        if (owner.getShield() == null) return inflicted;
        if (victim != owner.getShield()) return inflicted;
        if (damager.getCastle() == owner.getCastle()) return inflicted;

        // do it
        owner.setShield(null);

        // show it
        owner.getCastle().getObserver().abilityUsed(victim.getLocation(), victim.getLocation(), Constants.IMG_STAR);

        // wipe the damage
        return (byte) 0;
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
}
