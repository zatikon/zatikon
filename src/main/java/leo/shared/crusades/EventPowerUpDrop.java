///////////////////////////////////////////////////////////////////////
// Name: EventPowerUpDrop
// Desc: Units with powerups drop them upon death
// Date: 8/4/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Unit;

import java.util.Vector;


public class EventPowerUpDrop implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_DEATH;
    private final short type = Action.OTHER;
    private final Unit owner;
    private final int priority = 5;
    private final String detail = "Drops a power up that contains this unit's enhancement upon death.";
    private short power = Action.GROW_TOXIC;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventPowerUpDrop(Unit newOwner, short newPower) {
        owner = newOwner;
        power = newPower;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit corpse = source;
        Unit murderer = target;

        if (corpse != owner) return Event.OK;
        //if (!corpse.isDead()) return Event.OK;
        //if (murderer.isDead()) return Event.OK;

        short loc = corpse.getLocation();
        //owner.setLocation((byte)-1);
        //owner.setLocation((byte)-1);
        // create and place new powerup where the owner died
        Unit powerUp = new RelicPowerUp(murderer.getCastle(), power);
        if (loc != corpse.getCastle().getLocation() && loc != murderer.getCastle().getLocation() &&
                corpse.getCastle().getBattleField().getUnitsAt(loc).size() <= 1)
        //(source.getCastle().getBattleField().getUnitAt(loc) == null || source.getCastle().getBattleField().getUnitAt(loc).isDead()))
        {
            powerUp.setLocation(loc);
            powerUp.setBattleField(murderer.getCastle().getBattleField());
            murderer.getCastle().getBattleField().add(powerUp);
            murderer.getCastle().addOut(murderer.getTeam(), powerUp);
        }
        //owner.setLocation(loc);

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
        return "Drops power up on death";
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return "Empowered";
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
