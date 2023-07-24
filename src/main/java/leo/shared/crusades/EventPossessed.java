///////////////////////////////////////////////////////////////////////
// Name: EventPossessed
// Desc: You shall all serve the master
// Date: 3/19/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventPossessed implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DEATH;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final int priority = 10; //150;
    private final String detail = Strings.EVENT_POSSESSED_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventPossessed(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;

        if (murderer != owner) return Event.OK;
        if (!corpse.getOrganic(owner)) return Event.OK;
        if (corpse.getCastle() == owner.getCastle()) return Event.OK;
        //if (murderer.isDead()) return Event.OK;
        if (!corpse.targetable(owner)) return Event.OK;

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, murderer, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(corpse, Action.EFFECT_FIZZLE);
            return Event.OK;
        }

        if (corpse.getOrganic(corpse) && murderer.getOrganic(murderer))
            murderer.setMurderer();
        if (corpse.getOrganic(corpse))
            corpse.getCastle().addGraveyard(corpse);

        owner.getCastle().getBattleField().event(Event.WITNESS_DEATH, corpse, murderer, Event.TRUE, Event.NONE, Event.OK);

        // Switch sides
        corpse.getCastle().removeOut(corpse);
        if (!corpse.getCastle().getGraveyard().remove(corpse)) {
            corpse.getBattleField().remove(corpse);
            return Event.CANCEL;
        }

        corpse.getCastle().removeOut(corpse);
        corpse.setCastle(owner.getCastle());
        owner.getCastle().addOut(owner.getTeam(), corpse);
        corpse.stun();
        corpse.setLife(corpse.getLifeMax());

        // show it
        UnitGhost tmp = new UnitGhost(owner.getCastle());
        tmp.setLocation(owner.getLocation());
        owner.getCastle().getObserver().attack(corpse, tmp, corpse.getLocation(), Action.ATTACK_SPIRIT);

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
        return Strings.EVENT_POSSESSED_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_POSSESSED_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_POSSESSED_4;
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
