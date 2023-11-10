///////////////////////////////////////////////////////////////////////
// Name: UnitLongbowman
// Desc: A longbowman
// Date: 5/27/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitLongbowman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit spotter = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLongbowman(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.LONGBOWMAN.value();
        category = Unit.ARCHERS;
        name = Strings.UNIT_LONGBOWMAN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 2;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_LONGBOWMAN;

        // add barrage
        EventBarrage eb = new EventBarrage(this);
        add((Event) eb);
        add((Action) eb);

        // Add the actions
        actions.add(move);
        actions.add(new ActionSpotter(this));
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public void setSpotter(Unit newUnit) {
        spotter = newUnit;
    }

    public Unit getSpotter() {
        if (spotter == null || spotter.isDead() || !spotter.getOrganic(this) || !spotter.targetable(this))
            return null;
        else
            return spotter;
    }

    public Unit getLink() {
        return getSpotter();
    }
}
