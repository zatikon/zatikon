///////////////////////////////////////////////////////////////////////
// Name: UnitSergeant
// Desc: A sergeant
// Date: 12/27/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSergeant extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ActionTrait free;
    private Unit rally = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSergeant(Castle newCastle) {
        castle = newCastle;

        // forbidden!
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.SERGEANT;
        category = Unit.COMMANDERS;
        name = Strings.UNIT_SERGEANT_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_SERGEANT;

        //EventLeader el = new EventLeader(this);
        //add((Event) el);
        //add((Action) el);

        // Add the actions
        actions.add(move);
        actions.add(attack);
        actions.add(new ActionRally(this));

        free = new ActionTrait(this, Strings.UNIT_SERGEANT_2, Strings.UNIT_SERGEANT_3, "");
        free.setDetail(Strings.UNIT_SERGEANT_4);
    }


    /////////////////////////////////////////////////////////////////
    // Rally sets/gets
    /////////////////////////////////////////////////////////////////
    public void setRally(Unit newUnit) {
        endRally();
        rally = newUnit;
        rally.setFreelyActs(true);
        rally.getActions().add(0, free);
    }

    public void endRally() {
        if (rally != null) {
            rally.remove(free);
            rally.setFreelyActs(false);
            rally = null;
        }
    }

    public Unit getRally() {
        if (rally != null && (!rally.getOrganic(this) || !rally.targetable(this))) {
            endRally();
        }

        if (rally == this || rally == null || rally.isDead())
            return null;
        else
            return rally;
    }

    public Unit getLink() {
        return getRally();
    }


    /////////////////////////////////////////////////////////////////
    // Start a new truce
    /////////////////////////////////////////////////////////////////
    public void removed() {
        endRally();
    }


}
