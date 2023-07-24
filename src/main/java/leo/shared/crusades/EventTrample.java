///////////////////////////////////////////////////////////////////////
//	Name:	EventTrample
//	Desc:	Trample units along the way
//	Date:	3/7/2009 - Gabe Jones 
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventTrample implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = WITNESS_MOVE;
    private final short type = Action.ATTACK;
    private final Unit owner;
    private final int priority = 150;
    private final String detail = Strings.EVENT_TRAMPLE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventTrample(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit walker = source;
        short from = val1;
        short to = val2;
        if (source != owner) return Event.OK;

        int slopeX = 0;
        int slopeY = 0;
        int targetX = BattleField.getX(to);
        int targetY = BattleField.getY(to);
        short myX = BattleField.getX(from);
        short myY = BattleField.getY(from);

        if (targetX > myX) slopeX = 1;
        if (targetX < myX) slopeX = -1;
        if (targetY > myY) slopeY = 1;
        if (targetY < myY) slopeY = -1;

        while (BattleField.getLocation(myX, myY) != to) {
            myX += slopeX;
            myY += slopeY;
            Unit unit = owner.getBattleField().getUnitAt(myX, myY);
            if (unit != null && unit.getCastle() != owner.getCastle() && unit.targetable(owner)) {
                short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, unit, Action.ATTACK, Event.NONE, Event.OK);

                if (outcome != Event.OK) {
                    owner.getCastle().getObserver().attack(owner, unit, outcome, Action.ATTACK_OTHER);
                } else {
                    short result = unit.damage(owner, (byte) 6);
                    owner.getCastle().getObserver().text(unit.getName() + Strings.EVENT_TRAMPLE_2 + result);
                    owner.getCastle().getObserver().attack(owner, unit, result, Action.ATTACK_OTHER);
                    if (unit.isDead())
                        owner.getCastle().getObserver().death(unit);

                    // send the witness event
                    owner.getBattleField().event(Event.WITNESS_ACTION, owner, unit, Action.ATTACK, Event.NONE, Event.OK);
                }
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
        return Strings.EVENT_TRAMPLE_3 + 6;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_TRAMPLE_4;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_TRAMPLE_5;
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
