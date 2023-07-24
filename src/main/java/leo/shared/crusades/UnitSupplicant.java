///////////////////////////////////////////////////////////////////////
// Name: UnitSupplicant
// Desc: A supplicant
// Date: 1/15/2010 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSupplicant extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSupplicant(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.SUPPLICANT;
        category = Unit.CULTISTS;
        name = Strings.UNIT_SUPPLICANT_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_SUPPLICANT;

        EventSacrifice es = new EventSacrifice(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
    }

}
