///////////////////////////////////////////////////////////////////////
// Name: EventUndying
// Desc: returns possessed from the grave
// Date: 8/15/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventUndying implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DEATH;
    private final short type = Action.OTHER;
    private final Unit owner;
    private final int priority = 140;
    private final String detail = Strings.UNIT_GHOST_4;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventUndying(Unit newOwner) {
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
        if (murderer.isDead()) return Event.OK;
        if (!corpse.targetable(owner)) return Event.OK;

        // get the outcome
        short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, murderer, Action.OTHER, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            owner.getCastle().getObserver().unitEffect(corpse, Action.EFFECT_FIZZLE);
            return Event.OK;
        }

        owner.die(false, owner);
        UnitPossessed reborn = new UnitPossessed(owner.getCastle());
        reborn.setLocation(owner.getLocation());
        reborn.setBattleField(owner.getCastle().getBattleField());
        reborn.getCastle().getBattleField().add(reborn);
        reborn.getCastle().addOut(owner.getTeam(), reborn);
        reborn.grow(owner.getBonus());

        // Animation
        owner.getCastle().getObserver().abilityUsed(reborn.getLocation(), reborn.getLocation(), Constants.IMG_SUMMON_BLUE_1);

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
        return Strings.UNIT_GHOST_5;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.UNIT_GHOST_6;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.UNIT_GHOST_8;
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
