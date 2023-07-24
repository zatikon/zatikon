///////////////////////////////////////////////////////////////////////
// Name: UnitMartyr
// Desc: A martyr
// Date: 8/29/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitMartyr extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMartyr(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // low priority
        eventPriority = 2;

        // Initialize
        id = Unit.MARTYR;
        category = Unit.CULTISTS;
        name = Strings.UNIT_MARTYR_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_MARTYR;

        // Add the ability description
        //EventRetribution er = new EventRetribution(this);
        //add((Event) er);
        //add((Action) er);
        ActionTrait er = new ActionTrait(this, Strings.EVENT_RETRIBUTION_3, Strings.EVENT_RETRIBUTION_2, "");
        er.setDetail(Strings.EVENT_RETRIBUTION_1);
        er.setType(Action.SPELL);
        add(er);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

    /////////////////////////////////////////////////////////////////
    // Punishment
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death == false) {
            super.die(death, source);
            return;
        }
        if (getCastle() == source.getCastle()) {
            super.die(death, source);
            return;
        }

        Unit murderer = source;

        // get the outcome
        short outcome = getBattleField().event(Event.PREVIEW_ACTION, this, murderer, Action.SPELL, Event.NONE, Event.OK);
        short result = Event.OK;
        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            getCastle().getObserver().unitEffect(murderer, Action.EFFECT_FIZZLE);
            //return;
        } else {
            if (murderer.targetable(this) && murderer.getOrganic(this)) {
                // Hardcoding Possessed Event, may be able to just call the Preview death events for the murdering unit
                //if (murderer.getID() instanceof UnitPossessed)
                //  ((UnitPossessed)murderer).possessEvent().perform(this, murderer, Event.NONE, Event.NONE, Event.NONE);

                // Call events that the dying unit has for previewing my death
                Vector<Event> previewDeathEvents = murderer.getEvents(Event.PREVIEW_DEATH);
                if (previewDeathEvents != null) {
                    Iterator it = previewDeathEvents.iterator();
                    while (it.hasNext()) {
                        Event otherside = (Event) it.next();
                        result = otherside.perform(this, murderer, Event.NONE, Event.NONE, Event.OK);
                        if (result == Event.CANCEL || result == Event.DEAD || result == Event.END || result == Event.PARRY)
                            break;
                    }
                }

                setDead(true); // to prevent infinite recursive martyr v martyr slaying
                // kill it!
                if (!murderer.isDead())
                    murderer.die(true, this);
                setDead(false);

                if (murderer.isDead())
                    getCastle().getObserver().death(murderer);
            }
        }
        if (result != Event.CANCEL && result != Event.PARRY) {
            super.die(death, source);
        }
        return;
    }
}
