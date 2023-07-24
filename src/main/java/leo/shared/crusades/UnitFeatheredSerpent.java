///////////////////////////////////////////////////////////////////////
// Name: UnitFeatheredSerpent
// Desc: A feathered serpent
// Date: 10/10/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitFeatheredSerpent extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitFeatheredSerpent(Castle newCastle) {
        castle = newCastle;

        // very high event priority
        eventPriority = 20;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.FEATHERED_SERPENT;
        category = Unit.WYRMS;
        name = Strings.UNIT_FEATHERED_SERPENT_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 0;
        actionsMax = 0;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = null;
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_FEATHERED_SERPENT;


        // the rebirth event
        EventRebirth er = new EventRebirth(this);
        add((Event) er);
        add((Action) er);

        EventSplendor es = new EventSplendor(this);
        add((Event) es);
        add((Action) es);

        // Add the ability description
        ActionTrait trait = new ActionTrait(this, Strings.UNIT_FEATHERED_SERPENT_2, Strings.UNIT_FEATHERED_SERPENT_3, "", Strings.UNIT_FEATHERED_SERPENT_4);
        trait.setDetail(Strings.UNIT_FEATHERED_SERPENT_5);
        trait.setType(Action.SPELL);
        add(trait);

        // flap flap flap
        add(new ActionFlight(this));

        // Add the actions
        actions.add(move);
    }


    /////////////////////////////////////////////////////////////////
    // End turn
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (castle.getBattleField() == null) return;
        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                if (victim.getOrganic(this) && victim.targetable(this)) {
                    if (victim.heal(this)) {
                        // Generate a nifty effect
                        getCastle().getObserver().abilityUsed(getLocation(), victim.getLocation(), Constants.IMG_STAR);

                    }
                }
            }
        }
    }


}
