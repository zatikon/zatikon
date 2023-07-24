///////////////////////////////////////////////////////////////////////
// Name: EventHealPotion
// Desc: Heal a damaged unit reactively
// Date: 3/23/2009 - Gabe Jones 
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventHealPotion implements Event, Action, Potion {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = WITNESS_DAMAGE;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final int priority = 300;
    private final String detail = Strings.EVENT_HEAL_POTION_1;
    private final short targetType = TargetType.NONE;
    private final short cost = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventHealPotion(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        // initialize the values
        Unit damager = source;
        Unit victim = target;
        short inflicted = val1;

        // some tests
        if (victim != owner) return Event.OK;
        if (damager.getCastle() == owner.getCastle()) return Event.OK;
        if (inflicted <= 0) return Event.OK;
        if (!owner.getOrganic(owner)) return Event.OK;
        if (owner.isStunned()) return Event.OK;
        if (owner.isDead()) return Event.OK;
        if (!owner.isWounded()) return Event.OK;

        // heal'm
        owner.heal(owner);

        // visual effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), owner.getLocation(), Constants.IMG_STAR);

        // remove the potion
        owner.remove((Event) this);
        owner.remove((Action) this);

        // all done
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
        return Strings.EVENT_HEAL_POTION_2;
    }

    public String getRangeDescription() {
        return Strings.EVENT_HEAL_POTION_3;
    }

    public String getCostDescription() {
        return Strings.EVENT_HEAL_POTION_4;
    }


    /////////////////////////////////////////////////////////////////
    // Descriptions
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        owner.heal(owner);

        // visual effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), owner.getLocation(), Constants.IMG_STAR);

        // pay the price
        owner.remove((Event) this);
        owner.remove((Action) this);
        owner.getCastle().deductCommands(this, (byte) 1);
        owner.deductActions(cost);

        return "" + owner.getName() + Strings.EVENT_HEAL_POTION_5;
    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        if (!owner.isWounded()) return 0;

        return 1;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_HEAL_POTION_6;
    }

    public Vector<Short> getTargets() {
        return new Vector<Short>();
    }

    public Vector<Short> getClientTargets() {
        return getTargets();
    }

    public short getMax() {
        return (byte) 0;
    }

    public short getCost() {
        return cost;
    }

    public short getRange() {
        return (byte) 0;
    }

    public short getTargetType() {
        return targetType;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public boolean passive() {
        return false;
    }

    public short getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }
}
