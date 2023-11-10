///////////////////////////////////////////////////////////////////////
// Name: EventSacrifice
// Desc: The dead return 
// Date: 1/15/2010 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventSacrifice implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_DEATH;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final int priority = 110;
    private final String detail = Strings.EVENT_SACRIFICE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventSacrifice(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;
        boolean death = val1 == Event.TRUE;

        if (corpse == owner) return Event.NONE;
        if (!death) return Event.NONE;
        if (owner.isStunned()) return Event.NONE;
        if (!corpse.getOrganic(owner)) return Event.NONE;
        if (!corpse.targetable(owner)) return Event.NONE;
        if (corpse.getCastle() != owner.getCastle()) return Event.NONE;
        if (murderer != null && murderer.getCastle() == corpse.getCastle()) return Event.NONE;
        if (corpse.getID() == UnitType.NONE.value()) return Event.NONE;
        if (BattleField.getDistance(corpse.getLocation(), owner.getLocation()) > 2) return Event.NONE;

        // yank it from the graveyard. if it's not there, no risen
        if (!corpse.getCastle().getGraveyard().remove(corpse)) return Event.NONE;

        // do it
        short oldID = corpse.getID();
        short loc = owner.getLocation();
        owner.die(false, owner);

        // rebirth
        Unit reborn = Unit.getUnit(oldID, owner.getCastle());
        reborn.setLocation(loc);
        reborn.setBattleField(owner.getCastle().getBattleField());
        reborn.getCastle().getBattleField().add(reborn);
        reborn.getCastle().addOut(owner.getTeam(), reborn);
        reborn.refresh();

        // Generate a nifty effect
        owner.getCastle().getObserver().unitEffect(owner, Action.EFFECT_FADE);
        reborn.setHidden(true);
        reborn.getCastle().getObserver().unitEffect(reborn, Action.EFFECT_FADE_IN);
        owner.getCastle().getObserver().playSound(Constants.SOUND_SHAPESHIFT);

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
        return Strings.EVENT_SACRIFICE_2;
    }

    public String getRangeDescription() {
        return Strings.EVENT_SACRIFICE_3;
    }

    public String getCostDescription() {
        return Strings.EVENT_SACRIFICE_4;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_SACRIFICE_5;
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
