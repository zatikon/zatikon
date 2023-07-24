///////////////////////////////////////////////////////////////////////
// Name: EventInterference
// Desc: Footman's movement stopper
// Date: 3/6/2009 - Gabe Jones 
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventInterference implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = PREVIEW_MOVE;
    private final short type = Action.SKILL;
    private final Unit owner;
    private final int priority = 110;
    private final String detail = Strings.EVENT_INTERFERENCE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventInterference(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit walker = source;
        short from = val1;
        short to = val2;

        if (owner.isStunned()) return Event.OK;
        if (owner.isDead()) return Event.OK;
        if (walker.isDead()) return Event.OK;
        if (!walker.getOrganic(owner)) return Event.OK;
        if (!walker.targetable(owner)) return Event.OK;
        if (walker.getCastle() == owner.getCastle()) return Event.OK;
        if (BattleField.getDistance(owner.getLocation(), from) > 1) return Event.OK;

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, walker, Action.SKILL, Event.NONE, Event.OK);

        if (outcome == Event.CANCEL) {
            // the visual effect
            owner.getCastle().getObserver().abilityUsed(owner.getLocation(), walker.getLocation(), Constants.IMG_LOCKDOWN_1);

            // its all ok
            return Event.OK;
        }

        // the visual effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), walker.getLocation(), Constants.IMG_LOCKDOWN_1);
  /*owner.getCastle().getObserver().
   attack(owner,walker,(byte) -2,
   Action.ATTACK_MELEE);*/

        return Event.CANCEL;
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
        return Strings.EVENT_INTERFERENCE_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_INTERFERENCE_3;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_INTERFERENCE_4;
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
