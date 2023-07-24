///////////////////////////////////////////////////////////////////////
// Name: EventCounter
// Desc: Duelist's counter attack
// Date: 9/28/2010 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventCounter implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_ACTION;
    private final short type = Action.ATTACK;
    private int charges = 2;
    private boolean can_counter = false;
    private final Unit owner;
    private final int priority = 200;
    private final String detail = Strings.EVENT_COUNTER_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventCounter(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit victim = target;
        short actionType = val1;

        if (victim != owner) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;
        if (owner.isStunned()) return Event.OK;
        if (charges < 1) return Event.OK;
        //if (recursive > 1) return Event.OK;
        if (!can_counter) return Event.OK;

        // the cost
        charges--;

        // riposte!
        if (owner.getAttack() != null && BattleField.inRange(owner.getAttack(), source.getLocation())) {
            owner.noCost(true);
            owner.getAttack().perform(source.getLocation());
            owner.noCost(false);
        }
        //charges++;

        return Event.CANCEL;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        can_counter = true;
        charges = 2;
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        can_counter = false;
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
        return Strings.EVENT_COUNTER_2;
    }

    public String getRangeDescription() {
        return Strings.EVENT_COUNTER_3;
    }

    public String getCostDescription() {
        return Strings.EVENT_COUNTER_4;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_COUNTER_5;
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
