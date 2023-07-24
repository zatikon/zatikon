///////////////////////////////////////////////////////////////////////
// Name: EventSummoner
// Desc: Keeps track of summoned units.
// Date: 10/11/2010 - Linus Foster
// TODO:   The event does not recognize the death of a summon or an owner
//          it may be necessary to SUPER up the triggers in the classes
//          the summoner/summon.
//   12/1/2010 - Tony Schwarz
//   Got rid of extra text in max summons description so that its button
//   takes less space in the edit army screen
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventSummoner implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.WITNESS_DEATH;
    private final short type = Action.SPELL;
    private final Unit owner;
    private final Vector<Unit> summons;
    private int curSummons;
    private final int maxSummons;
    private final int priority = 300;
    private String detail = Strings.EVENT_SUMMONER_1;
    private String name = Strings.EVENT_SUMMONER_4;
    private String description = Strings.EVENT_SUMMONER_2;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventSummoner(Unit newOwner) {
        owner = newOwner;
        summons = new Vector<Unit>();
        curSummons = 0;
        // Later, this will be a switch statment based on the owner's unit type
        maxSummons = getSummonLimitNumber(owner.getID());
        //"This unit has the ability to summon other units, but can only have so many summoned at the same time. If this unit dies, so do everything it has summoned.";
        if (owner.getID() == Unit.BARRACKS) {
            name = Strings.UNIT_BARRACKS_6; // Troop Deployment
            detail = Strings.UNIT_BARRACKS_7; // Deployments Availible
            description = Strings.UNIT_BARRACKS_8; //
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;

        // If a summon dies:
        if (summons.contains(corpse) == true) {
            //System.out.println("It was only a minion.");
            curSummons--;
            summons.remove(corpse);
            // And a little animation
            Unit tmp = Unit.getUnit(corpse.getID(), owner.getCastle());
            tmp.setAppearance(corpse.getAppearance());
            tmp.setLocation(corpse.getLocation());
            owner.getCastle().getObserver().attack(owner, tmp, owner.getLocation(), Action.ATTACK_SPIRIT);
        }

        return Event.NONE;
    }

    public void summonerDies() {
        Unit victim;
        // Whie the summons vector isn't empty...
        while (!summons.isEmpty()) {
            victim = summons.firstElement();
            summons.remove(victim);
            // Remove the first element,
            victim.die(false, owner);
            // and kill it.
            //owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_FADE);
            //getCastle().getObserver().death(victim);
        }
        curSummons = 0;
    }

    public void recieveSummon(Unit newSummon) {
        summons.add(newSummon);
        curSummons++;
    }

    public void unlinkSummon(Unit oldSummon) {
        summons.remove(oldSummon);
        curSummons--;
    }

    public boolean canSummon() {
        return curSummons < maxSummons;
    }

    private int getSummonLimitNumber(short id) {
        switch (id) {
            case Unit.BARRACKS:
                return 3;
            case Unit.DRUID:
                return 3;
            case Unit.MAGUS:
                return 1;
            case Unit.NECROMANCER:
                return 4;
            case Unit.SUMMONER:
                return 2;
            case Unit.CONJURER:
                return 3;
        }
        return 1;
    }

    public Vector<Unit> getSummonList() {
        return summons;
    }

    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        Unit victim;
        int size = summons.size();
        int i = 0;
        Vector<Unit> removes = new Vector<Unit>();
        // While the summons vector isn't empty...
        while (i < size) {
            victim = summons.elementAt(i);
            if (victim.getCastle() != owner.getCastle())
                removes.add(victim);
            i++;
        }
        summons.removeAll(removes);
        while (!removes.isEmpty()) {
            victim = removes.firstElement();
            removes.remove(victim);
            // Remove the first element,
            victim.die(false, owner);
            curSummons--;
        }
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
        return (description + "(" + (maxSummons - curSummons) + "/" + maxSummons + ")");
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() { //return Strings.EVENT_SUMMONER_3;
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return name;
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

    //
    public int getCurSummons() {
        return curSummons;
    }

    public int getMaxSummons() {
        return maxSummons;
    }
}
