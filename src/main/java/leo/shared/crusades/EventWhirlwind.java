///////////////////////////////////////////////////////////////////////
//	Name:	EventWhirlwind
//	Desc:	Attack everything in sight
//	Date:	3/10/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.Event;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class EventWhirlwind implements Event, Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = Event.PREVIEW_ACTION;
    private final short type = Action.ATTACK;
    private final Unit owner;
    private final int priority = 900;
    private boolean executing = false;
    private final String detail = Strings.EVENT_WHIRLWIND_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventWhirlwind(Unit newOwner) {
        owner = newOwner;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        if (executing) return Event.OK;

        Unit victim = target;
        short actionType = val1;
        boolean noZeal = owner.getNoZeal();
        Short ownerOldLocation = owner.getLocation();
        Short ownerNewLocation = null;

        // do some checks
        if (source != owner) return Event.OK;
        if (actionType != Action.ATTACK) return Event.OK;

        // start the whirlwind
        executing = true;

        // get the targets, and do more initialization
        Vector<Short> targets = owner.getAttack().getTargets();

        //remove the victim from targets as we will attack that first
        if (targets.contains(victim.getLocation())) {
            targets.remove((Short) victim.getLocation());
            }

        //reorder the targets to be clockwise from target
        short[] offsets = {
            -11,                  // Above
            -10,              // Above-Right
            1,                           // Right
            12,               // Below-Right
            11,                   // Below
            10,               // Below-Left
            -1,                          // Left
            -12               // Above-Left
        };

        Vector<Short> orderedTargets = new Vector<>();
        boolean startFound = false;
        short currentIndex = 0;
        short startIndex = 0;

        //where to start attack and move clockwise from
        short firstTarget = (short) (victim.getLocation() - ownerOldLocation);

        //loop through surrounding squares to find start point
        for (short i = 0; i < offsets.length; i++) {
            if(!startFound) {
                if (offsets[i] == firstTarget) {
                    startIndex = i;
                    startFound = true;
                }
            }
            // Once start square is found check if there is a target that matches each square
            if(startFound) {
                for (short j = 0; j < targets.size(); j++) {
                    if (offsets[i] == (short)(targets.elementAt(j) - ownerOldLocation)) {
                        // Place the target in the new array in the correct order
                        orderedTargets.add(targets.elementAt(j));
                        break;
                    }
                }

                if(i + 1 == offsets.length) {
                    i = -1;
                }
                if(i + 1 == startIndex) {
                    break;
                }
            }                       
        }

        owner.noCost(true);

        //always attack the victim first
        owner.getAttack().perform(victim.getLocation());

        //do not do Zeal event on any other attacks
        owner.setNoZeal(true);

        //if owner moved because of zeal get new location and set location back for attacks
        ownerNewLocation = owner.getLocation();
        if(ownerOldLocation != ownerNewLocation)
            owner.setLocation(ownerOldLocation);

        // loop through and beat them down
        for (int i = 0; i < orderedTargets.size(); i++) {
            Short tmp = orderedTargets.elementAt(i);
            Unit unit = owner.getBattleField().getUnitAt(tmp.byteValue());

            if (unit != null && owner.getAttack() != null) {
                owner.getAttack().perform(tmp.byteValue());
            }
        }

        if(ownerOldLocation != ownerNewLocation)
            owner.setLocation(ownerNewLocation);

        // restore the unit
        owner.noCost(false);

        //Set noZeal back to what it was 
        owner.setNoZeal(noZeal);
        // stop the whirlwind
        executing = false;

        return Event.END;
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
        return Strings.EVENT_WHIRLWIND_2;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.EVENT_WHIRLWIND_3;
    }


    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.EVENT_WHIRLWIND_4;
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
