///////////////////////////////////////////////////////////////////////
//	Name:	EventPush
//	Desc:	Push your victim
//	Date:	3/10/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventPush implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_ACTION;
    private final short type = Action.SKILL;
    private final Unit owner;
    private final int priority = 100;
    private final String detail = Strings.EVENT_PUSH_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventPush(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit victim = target;
        short actionType = val1;

        if (source != owner) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;
        if (victim.isDead()) return Event.OK;

        // for accurate visual effects
        victim.setLocation(victim.getLocation());

        if (!victim.getOrganic(owner)) return Event.OK;

        short slopeX = 0;
        short slopeY = 0;
        short targetX = BattleField.getX(victim.getLocation());
        short targetY = BattleField.getY(victim.getLocation());
        short myX = BattleField.getX(owner.getLocation());
        short myY = BattleField.getY(owner.getLocation());

        if (targetX > myX) slopeX = (byte) 1;
        if (targetX < myX) slopeX = (byte) -1;
        if (targetY > myY) slopeY = (byte) 1;
        if (targetY < myY) slopeY = (byte) -1;

        short newX = (byte) (targetX + slopeX);
        short newY = (byte) (targetY + slopeY);

        if (newX < 0 || newY < 0) return Event.OK;
        if (newX >= 11 || newY >= 11) return Event.OK;

        short newLocation = BattleField.getLocation(newX, newY);

        if (owner.getBattleField().getUnitAt(newLocation) != null) {
            return Event.OK;
        }

        victim.setHidden(true);
        victim.setLocation(newLocation);

        // Check for victory
        if (!owner.isDead()) {
            if (victim.getLocation() == owner.getCastle().getLocation()) {
                victim.getCastle().getObserver().endGame(victim.getCastle());
                return Event.OK;
            }
        }

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
        return Strings.EVENT_PUSH_4;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_PUSH_2;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_PUSH_3;
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
