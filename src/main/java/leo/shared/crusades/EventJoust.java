///////////////////////////////////////////////////////////////////////
//	Name:	EventJoust
//	Desc:	Perforate units along the way
//	Date:	4/26/2009 - Gabe Jones 
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventJoust extends ActionAttack implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = WITNESS_MOVE;
    private final short type = Action.ATTACK;
    private final Unit owner;
    private final int priority = 90;
    private final String detail = Strings.EVENT_JOUST_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventJoust(Unit newOwner) {
        super(newOwner, (byte) 0, (byte) 100, TargetType.UNIT_AREA, (byte) 5);
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        Unit walker = source;
        short from = val1;
        short to = val2;
        if (source != owner) return Event.OK;
        if (owner.isDead()) return Event.OK;
        int slopeX = 0;
        int slopeY = 0;
        int targetX = BattleField.getX(to);
        int targetY = BattleField.getY(to);
        short myX = BattleField.getX(from);
        short myY = BattleField.getY(from);

        if (targetX > myX) slopeX = 1;
        if (targetX < myX) slopeX = -1;
        if (targetY > myY) slopeY = 1;
        if (targetY < myY) slopeY = -1;

        // the big list
        Vector<Short> targets = new Vector<Short>();

        while (BattleField.getLocation(myX, myY) != to) {
            myX += slopeX;
            myY += slopeY;

            // Get the targets
            Vector<Short> tmpTargets =
                    owner.getBattleField().getArea(
                            owner,
                            BattleField.getLocation(myX, myY),
                            (byte) 1,
                            false,
                            true,
                            true,
                            true, TargetType.ENEMY, owner.getCastle());

            addTargets(targets, tmpTargets);
        }

        // no cost
        owner.noCost(true);


        // loop through and beat them down
        for (int i = 0; i < targets.size(); i++) {
            Short tmp = targets.elementAt(i);
            Unit unit = owner.getBattleField().getUnitAt(tmp.byteValue());
            if (unit != null) {
                perform(tmp.byteValue());
            }
        }

        // restore the unit
        owner.noCost(false);

        return Event.OK;
    }


    /////////////////////////////////////////////////////////////////
    // Add targets
    /////////////////////////////////////////////////////////////////
    private void addTargets(Vector<Short> targets, Vector<Short> newTargets) {
        for (int i = 0; i < newTargets.size(); i++) {
            Short tmp = newTargets.elementAt(i);
            removeDuplicate(targets, tmp);
            targets.add(tmp);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Remove the dupes
    /////////////////////////////////////////////////////////////////
    private void removeDuplicate(Vector<Short> targets, Short location) {
        for (int i = 0; i < targets.size(); i++) {
            Short tmp = targets.elementAt(i);
            if (tmp.byteValue() == location.byteValue()) {
                targets.removeElementAt(i);
                return;
            }
        }
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
        return Strings.EVENT_JOUST_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_JOUST_3;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_JOUST_4;
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
