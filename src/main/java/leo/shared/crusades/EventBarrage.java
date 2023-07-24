///////////////////////////////////////////////////////////////////////
//	Name:	EventBarrage
//	Desc:	Support fire
//	Date:	5/27/2009 - Gabe Jones 
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;


public class EventBarrage extends ActionAttack implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_ACTION;
    private final short type = Action.ATTACK;
    private final UnitLongbowman owner;
    private final int priority = 100;
    private final String detail = Strings.EVENT_BARRAGE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventBarrage(Unit newOwner) {
        super(newOwner, (byte) 0, (byte) 100, TargetType.UNIT_AREA, (byte) 11, Action.ATTACK_ARROW);
        owner = (UnitLongbowman) newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit victim = target;
        short actionType = val1;

        if (source != owner.getSpotter()) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;
        if (owner.isDead()) return Event.OK;
        if (owner.isStunned()) return Event.OK;
        if (victim.isDead()) return Event.OK;
        if (!victim.targetable(owner)) return Event.OK;

        // no cost
        owner.noCost(true);

        // attack
        perform(victim.getLocation());

        // restore the unit
        owner.noCost(false);

        return Event.OK;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
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
        return Strings.EVENT_BARRAGE_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_BARRAGE_3;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_BARRAGE_4;
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
