///////////////////////////////////////////////////////////////////////
// Name: EventLament
// Desc: Mourn the dead 
// Date: 3/9/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class EventLament implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_DEATH;
    private final short type = Action.SPELL;
    private int charges = 1;
    private final Unit owner;
    private final int priority = 100;
    private final String detail = Strings.EVENT_LAMENT_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventLament(Unit newOwner) {
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
        if (corpse.getCastle() != owner.getCastle()) return Event.NONE;

        charges--;

        Castle castle = owner.getCastle();
        Vector units = castle.getBattleField().getUnits();
        Iterator it = units.iterator();
        boolean used = false;
        while (it.hasNext()) {
            Unit victim = (Unit) it.next();
            if (victim.getCastle() == castle && death) {
                if (victim.getOrganic(owner) && victim.targetable(owner)) {
                    victim.raiseLife((byte) 1);

                    // Generate a nifty effect
                    castle.getObserver().abilityUsed(
                            owner.getLocation(), victim.getLocation(), Constants.IMG_EYE);
                    used = true;
                }
            }
        }
        if (used) castle.getObserver().playSound(Constants.SOUND_EYE);

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
        return Strings.EVENT_LAMENT_2;
    }

    public String getRangeDescription() {
        return Strings.EVENT_LAMENT_3;
    }

    public String getCostDescription() {
        return charges + Strings.EVENT_LAMENT_4;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_LAMENT_5;
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
