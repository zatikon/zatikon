///////////////////////////////////////////////////////////////////////
//	Name:	EventSting
//	Desc:	Hit the walkers
//	Date:	10/20/2010 - Linus Foster
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventSting implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_MOVE;
    private final short type = Action.ATTACK;
    private final int charges = 1;
    private final Unit owner;
    private final int priority = 100;
    private final String detail = Strings.EVENT_STING_1;
    private final ActionAttack modAttack;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventSting(Unit newOwner) {
        owner = newOwner;
        modAttack = new ActionAttack(owner, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 1);
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {    //ActionAttack modAttack = //(ActionAttack)owner.getAttack(); //By convention, this should be a safe conversion...
        Unit walker = source;
        short from = val1;
        short to = val2;

        if (walker == null) return Event.OK;
        if (owner.isStunned()) return Event.OK;
        if (owner.isDead()) return Event.OK;
        if (walker.isDead()) return Event.OK;
        if (walker.getCastle() == owner.getCastle()) return Event.OK;
        //if (owner.getAttack() == null) return Event.OK;

        short oldRange = modAttack.getRange(); // Store old range
        modAttack.setRange((byte) 2); // Increase range to 2 for this attack
        EventPoison tempPoison = new EventPoison(owner);
        owner.add((Event) tempPoison); // Give it the poison property for this attack
        // attack!
        if (BattleField.inRange(modAttack, to)) {
            boolean oldState = owner.freelyActs();
            boolean otherState = owner.noCost();
            //short oldDamage = owner.getDamage();
            owner.setFreelyActs(true);
            owner.noCost(true);
            //owner.setDamage((byte)2);
            //
            //owner.getAttack().perform(to);
            modAttack.perform(to);
            //
            owner.setFreelyActs(oldState);
            owner.noCost(otherState);
            //owner.setDamage((byte)oldDamage);
        }
        modAttack.setRange(oldRange); // restore old range
        owner.remove((Event) tempPoison); // Take away the poison property

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
        return Strings.EVENT_STING_2;
    }

    public String getRangeDescription() {
        return "Range 2";
    }

    public String getCostDescription() {
        return Strings.EVENT_STING_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_STING_4;
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
