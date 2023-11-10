///////////////////////////////////////////////////////////////////////
// Name: UnitDiplomat
// Desc: A diplomat
// Date: 5/23/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDiplomat extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ActionTrait immune;
    private Unit truce = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDiplomat(Castle newCastle, boolean armistice) {
        castle = newCastle;

        // access kevek
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = armistice ? UnitType.DIPLOMAT.value() : UnitType.DIPLOMAT_USED.value();
        category = Unit.COMMANDERS;
        name = Strings.UNIT_DIPLOMAT_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 2;
        castleCost = armistice ? 200 : 1001;
        organic = true;
        appearance = Constants.IMG_DIPLOMAT;

        // Add the events
        Event ewm = new EventWatch(this, Event.PREVIEW_MOVE);
        Event epa = new EventWatch(this, Event.PREVIEW_ACTION);
        Event ewa = new EventWatch(this, Event.WITNESS_ACTION);
        add(ewm);
        add(epa);
        add(ewa);

        // Add the actions
        actions.add(move);
        actions.add(new ActionTruce(this));

        // add the armistice if this diplomat is unused
        if (armistice) actions.add(new ActionArmistice(this));

        immune = new ActionTrait(this, Strings.UNIT_DIPLOMAT_2, Strings.UNIT_DIPLOMAT_3, Strings.UNIT_DIPLOMAT_4);
        immune.setDetail(Strings.UNIT_DIPLOMAT_4);
    }


    /////////////////////////////////////////////////////////////////
    // End the truce
    /////////////////////////////////////////////////////////////////
    public void endTruce() {
        if (truce != null) {
            truce.remove(immune);
            truce.enemyTargetable(true);
            truce = null;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Start a new truce
    /////////////////////////////////////////////////////////////////
    public void startTruce(Unit newTruce) {
        endTruce();
        truce = newTruce;
        truce.enemyTargetable(false);
        truce.getActions().add(0, immune);
    }


    /////////////////////////////////////////////////////////////////
    // Start a new truce
    /////////////////////////////////////////////////////////////////
    public void removed() {
        endTruce();
    }


    /////////////////////////////////////////////////////////////////
    // Linked unit
    /////////////////////////////////////////////////////////////////
    public Unit getLink() {
        return truce;
    }
}
