///////////////////////////////////////////////////////////////////////
// Name: UnitHealer
// Desc: A Healer
// Date: 7/15/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitHealer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final int heals = 1;
    private final Action heal;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitHealer(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.HEALER;
        category = Unit.CLERGY;
        name = Strings.UNIT_HEALER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 2;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_HEALER;

        // Triggered heal
        EventHeal eh = new EventHeal(this);
        add((Event) eh);
        add((Action) eh);

        // Add the actions
        actions.add(move);

        // Gift of life
        heal = new ActionLife(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 2);
        actions.add(heal);
    }


    /////////////////////////////////////////////////////////////////
    // Get the heal action
    /////////////////////////////////////////////////////////////////
    public Action getHeal() {
        return heal;
    }
}
