///////////////////////////////////////////////////////////////////////
// Name: EventParry
// Desc: Swordsman's parry/riposte
// Date: 3/10/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventParry implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_ACTION;
    private final short type = Action.ATTACK;
    private int charges;
    private final int maxcharges;
    private final Unit owner;
    private final int priority = 205;
    private final String detail = Strings.EVENT_PARRY_1;
    private short parryRange = -1;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventParry(Unit newOwner, int amount) {
        owner = newOwner;
        maxcharges = amount;
        charges = amount;
    }

    public void setParryRange(short r) {
        parryRange = r;
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


        // the cost
        charges--;

        // Check if within range of parry effect
        if (parryRange >= 0) {
            ActionAttack checker = new ActionAttack(owner, (byte) 0, (byte) 1, TargetType.UNIT_AREA, parryRange);
            if (!BattleField.inRange(checker, source.getLocation())) {
                return Event.PARRY;
            }
        }

        // riposte!
        if (owner.getAttack() != null && BattleField.inRange(owner.getAttack(), source.getLocation())) {
            owner.noCost(true);
            owner.getAttack().perform(source.getLocation());
            owner.noCost(false);
        }

        return Event.PARRY;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        charges = maxcharges;
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
        return Strings.EVENT_PARRY_2;
    }

    public String getRangeDescription() {
        if (parryRange < 0)
            return Strings.EVENT_PARRY_3;
        else
            return Strings.EVENT_PARRY_6 + parryRange;
    }

    public String getCostDescription() {
        return charges + "/" + maxcharges + Strings.EVENT_PARRY_4;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_PARRY_5;
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
