///////////////////////////////////////////////////////////////////////
//	Name:	EventWhirlwind
//	Desc:	Attack everything in sight
//	Date:	3/10/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventWhirlwind implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_ACTION;
    private final short type = Action.ATTACK;
    private final Unit owner;
    private final int priority = 900;
    private boolean executing = false;
    private final String detail = Strings.EVENT_WHIRLWIND_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventWhirlwind(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        if (executing) return Event.OK;

        Unit victim = target;
        short actionType = val1;

        // do some checks
        if (source != owner) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;

        // start the whirlwind
        executing = true;

        // get the targets, and do more initialization
        Vector<Short> targets = owner.getAttack().getTargets();
        owner.noCost(true);

        // loop through and beat them down
        for (int i = 0; i < targets.size(); i++) {
            Short tmp = targets.elementAt(i);
            Unit unit = owner.getBattleField().getUnitAt(tmp.byteValue());
            if (unit != null && owner.getAttack() != null) {
                owner.getAttack().perform(tmp.byteValue());
            }
        }

        // restore the unit
        owner.noCost(false);

        // stop the whirlwind
        executing = false;

        return Event.END;
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
        return Strings.EVENT_WHIRLWIND_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_WHIRLWIND_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_WHIRLWIND_4;
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
