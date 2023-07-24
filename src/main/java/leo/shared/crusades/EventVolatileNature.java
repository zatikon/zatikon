///////////////////////////////////////////////////////////////////////
// Name: EventVolatileNature
// Desc: Explode upon death
// Date: 
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventVolatileNature implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DEATH;
    private short type = Action.SPELL;
    private final UnitWillWisps owner;
    private final int priority = 350;
    private final String detail = Strings.EVENT_VOLATILE_NATURE_1;
    //private short type;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventVolatileNature(UnitWillWisps newOwner) {
        owner = newOwner;
        // needed for boom
        type = (byte) 1; // test?
    }

    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        //if (!validate(target.getLocation())) return Event.OK;
        if (source != owner) return Event.OK;
        if (target == owner) return Event.OK;
        //
        kaboom(owner.getLocation());
        return Event.OK;
    }


    public void kaboom(short loc) {
        short damage = 4; // sets the power of the Lich Bomb
        Vector<Short> targets; //Prepare a place to store the targets of the AOE.
        BattleField field = owner.getBattleField(); // store a reference to the battlefield (to make reading the while loop's code easier)
        Vector<Unit> aoeVictims = new Vector<Unit>(); // Stores information for the AOE animation
        Vector<Short> damages; // More info for the AoE
        damages = new Vector<Short>(); // Resets information for Aoe animation
        targets = field.getArea(owner, loc, (byte) 1, false, true, true, true, TargetType.BOTH, owner.getCastle());
        for (int i = 0; i < targets.size(); i++) {
            short aoeLoc = targets.elementAt(i);
            Unit victim = field.getUnitAt(aoeLoc);
            if (victim != null) {
                // get permission
                short result = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                if (result == Event.OK) {
                    // strike
                    result = victim.damage(owner, damage);
                    if (victim != owner) {
                        owner.getCastle().getObserver().attack(owner, victim, result, Action.ATTACK_NONE);
                        if (victim.getID() != Unit.NONE)
                            owner.getCastle().getObserver().death(victim);
                    }
                    // store the damage dealt into the total (used for lifegain)
                    // play a little animation to let the player known that units are getting fragged.
                    damages.add(result);// stores damage result to use for the observer
                    aoeVictims.add(victim); // stores damage result to use for the observer
                    // send the witness event
                    owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                }
            }

        }
        // Attack animation
        owner.getCastle().getObserver().fireball(loc, loc, Constants.IMG_EXPLOSION_1, aoeVictims, damages, type);
        owner.getBattleField().event(Event.WITNESS_ACTION, owner, owner, getType(), Event.NONE, Event.OK);
    }

    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
        //return owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 1, false, false, false);
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getTargets();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.EVENT_VOLATILE_NATURE_3;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.EVENT_VOLATILE_NATURE_4;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
 /*public short getRemaining()
 { if (owner.noCost()) return 1;

  if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

  return (byte) (owner.getActionsLeft()/getCost());
 }*/


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_VOLATILE_NATURE_2;
    }

    public short getMax() {
        return 0;
    }

    public short getRange() {
        return 0;
    }

    public Unit getOwner() {
        return owner;
    }

    public boolean passive() {
        return false;
    }

    public short getType() {
        return Action.ATTACK;
    }

    public String getDetail() {
        return detail;
    }

    public int getPriority() {
        return priority;
    }

    public short getEventType() {
        return eventType;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public short getTargetType() {
        return (byte) 0;
    }

    public short getCost() {
        return (byte) 0;
    }

    public short getRemaining() {
        return (byte) 0;
    }

    public String perform(short target) {
        return "";
    }
}
