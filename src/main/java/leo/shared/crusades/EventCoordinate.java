///////////////////////////////////////////////////////////////////////
//	Name:	EventCoordinate
//	Desc:	Coordinate ally attacks
//	Date:	12/4/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventCoordinate implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_ACTION;
    private final short type = Action.SKILL;
    private final UnitCaptain owner;
    private final int priority = 100;
    private boolean executing = false;
    private final String detail = Strings.EVENT_COORDINATE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventCoordinate(Unit newOwner) {
        owner = (UnitCaptain) newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        if (executing) return Event.OK;
        if (!owner.coordinate()) return Event.OK;

        Unit victim = target;
        short actionType = val1;

        // do some checks
        if (source != owner) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;

        // start the strike
        executing = true;

        // get the targets, and do more initialization
        Vector<Unit> units = owner.getCastle().getBattleField().getUnits(owner.getCastle());

        UnitCaptain cap = null;

        // loop through and attack
        for (int i = 0; i < units.size(); i++) {
            Unit ally = units.elementAt(i);
            if (ally == owner || ally.getTeam() != owner.getTeam()) continue;
            if (!ally.getOrganic(owner)) continue;

            if (ally instanceof UnitCaptain) cap = (UnitCaptain) ally;

            if (cap != null) cap.off();

            boolean oldState = ally.freelyActs();
            ally.setFreelyActs(true);

            if (ally.getAttack() != null && ally.getAttack().validate(victim.getLocation())) {
                ally.getAttack().perform(victim.getLocation());
            }

            ally.setFreelyActs(oldState);

            if (cap != null) cap.on();
        }

        // stop the strike
        executing = false;

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
        return Strings.EVENT_COORDINATE_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_COORDINATE_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_COORDINATE_4;
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
