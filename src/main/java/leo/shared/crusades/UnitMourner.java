///////////////////////////////////////////////////////////////////////
// Name: UnitMourner
// Desc: A mourner
// Date: 10/9/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitMourner extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMourner(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.MOURNER.value();
        category = Unit.CULTISTS;
        name = Strings.UNIT_MOURNER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionDeath(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_MOURNER;

        EventLament el = new EventLament(this);
        add((Event) el);
        add((Action) el);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
