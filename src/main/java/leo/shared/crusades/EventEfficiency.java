///////////////////////////////////////////////////////////////////////
// Name: EventEfficiency
// Desc: Gain actions is mark dies
// Date: 3/7/2009 - Gabe Jones
//       9/30/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventEfficiency implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_DEATH;
    private final short type = Action.SKILL;
    private final Unit owner;
    private final int priority = 100;
    private final String detail;
    private final String description;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventEfficiency(UnitBountyHunter newOwner) {
        owner = newOwner;
        detail = Strings.EVENT_EFFICIENCY_1;
        description = Strings.EVENT_EFFICIENCY_2;
    }

    public EventEfficiency(UnitConspirator newOwner) {
        owner = newOwner;
        detail = Strings.EVENT_EFFICIENCY_5;
        description = Strings.EVENT_EFFICIENCY_6;
    }

    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;
        boolean death = val1 == Event.TRUE;

        if (detail == Strings.EVENT_EFFICIENCY_1 && murderer != owner) return Event.NONE;
        /* Only the bounty hunter gets E_E_1 as the detail, so this restricts the "owner must be the murderer" limitation to just the bounty hunter.*/
        if (corpse != owner.getMark()) return Event.NONE;
        if (!death) return Event.NONE;
        if (!corpse.getOrganic(owner)) return Event.NONE;
        if ((owner instanceof UnitConspirator) && !murderer.getOrganic(owner)) return Event.NONE;

        // ready to go
        murderer.setActions(murderer.getActionsMax());
        owner.setMark(null);


        return Event.NONE;
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
        return description;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_EFFICIENCY_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_EFFICIENCY_4;
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
