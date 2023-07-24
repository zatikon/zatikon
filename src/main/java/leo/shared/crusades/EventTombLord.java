///////////////////////////////////////////////////////////////////////
// Name: EventTombLord
// Desc: Bring dead units back
// Date: 3/7/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventTombLord implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DEATH;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final int priority = 20; //160;
    private final String detail = Strings.EVENT_TOMB_LORD_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventTombLord(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;

        if (!corpse.getOrganic(owner)) return Event.OK;
        if (corpse.getCastle() == owner.getCastle()) return Event.OK;
        //if (murderer.isDead()) return Event.OK;
        if (!corpse.targetable(owner)) return Event.OK;
        if (corpse.erased()) return Event.OK;

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

        corpse.setCastle(owner.getCastle());
        owner.getCastle().addOut(owner.getTeam(), corpse);
        corpse.stun();
        corpse.setLife(corpse.getLifeMax());
        if (corpse.isDying())
            corpse.stopDying();

        // Generate a nifty effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), corpse.getLocation(), Constants.IMG_TOMB_LORD_1);

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
        return Strings.EVENT_TOMB_LORD_3;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_TOMB_LORD_4;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_TOMB_LORD_5;
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
