///////////////////////////////////////////////////////////////////////
// Name: EventWatchMove
// Desc: Watch movement for truce break
// Date: 5/25/09
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;


public class EventWatch implements Event {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType;
    private final UnitDiplomat owner;
    private final int priority = 1000;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventWatch(Unit newOwner, short newEventType) {
        owner = (UnitDiplomat) newOwner;
        eventType = newEventType;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit walker = source;
        short from = val1;
        short to = val2;

        if (owner.getLink() != walker) return Event.OK;

        // if its a move, make sure its a retreat
        if (eventType == Event.PREVIEW_MOVE) {
            Castle enemyCastle = owner.getBattleField().getCastle1();
            if (owner.getCastle() == enemyCastle) enemyCastle = owner.getBattleField().getCastle2();

            short distanceFrom = BattleField.getDistance(enemyCastle.getLocation(), from);
            short distanceTo = BattleField.getDistance(enemyCastle.getLocation(), to);
            if (distanceFrom < distanceTo) return Event.OK;
        }

        owner.endTruce();
        owner.getCastle().getObserver().playSound(Constants.SOUND_RALLY);

        return Event.OK;
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
