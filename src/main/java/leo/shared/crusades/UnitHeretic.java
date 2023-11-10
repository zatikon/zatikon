///////////////////////////////////////////////////////////////////////
// Name: UnitHeretic
// Desc: A Heretic
// Date: 10/23/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitHeretic extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    boolean used = false;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitHeretic(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.CRUSADES;

        // low priority
        eventPriority = 3;

        // Initialize
        id = UnitType.HERETIC;
        category = Unit.CULTISTS;
        name = Strings.UNIT_HERETIC_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 0;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_HERETIC;

        // retribution
        ActionTrait trait = new ActionTrait(this, Strings.UNIT_HERETIC_2, Strings.UNIT_HERETIC_3, Strings.UNIT_HERETIC_4);
        trait.setType(Action.SPELL);
        trait.setDetail(Strings.UNIT_HERETIC_5);
        add(trait);

        // stun attack
        EventStun es = new EventStun(this);
        add((Event) es);
        add((Action) es);

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

        Vector units = getCastle().getBattleField().getUnits();
        Iterator it = units.iterator();
        if (!used) {
            while (it.hasNext()) {
                Unit vic = (Unit) it.next();
                if (vic.getCastle() != getCastle()) {
                    if (vic.getOrganic(this) && vic.targetable(this)) {
                        if (!(vic == source && source instanceof UnitScout)) {

                            // get the outcome
                            short outcome = getBattleField().event(Event.PREVIEW_ACTION, this, vic, Action.SPELL, Event.NONE, Event.OK);

                            // if its a fizzle, end
                            if (outcome == Event.CANCEL) {
                                getCastle().getObserver().unitEffect(vic, Action.EFFECT_FIZZLE);
                            } else {
                                vic.lowerLife((byte) 1, this);

                                // Generate a nifty effect
                                getCastle().getObserver().abilityUsed(getLocation(), vic.getLocation(), Constants.IMG_FACE);
                                castle.getObserver().playSound(Constants.SOUND_WHEEL);
                                used = true;
                            }
                        }
                    }
                }
            }
        }
        super.die(death, source); //return;
    }

    public boolean isDying() {
        return used;
    }

    public void stopDying() {
        used = false;
    }

}

