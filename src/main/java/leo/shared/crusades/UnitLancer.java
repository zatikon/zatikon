///////////////////////////////////////////////////////////////////////
// Name: UnitLancer
// Desc: A horseman and then some
// Date: 4/26/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitLancer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLancer(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.LANCER.value();
        category = Unit.HORSEMEN;
        name = Strings.UNIT_LANCER_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 0;
        actionsMax = 0;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE, (byte) 4);
        attack = null;
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_LANCER;

        add(new ActionMounted(this));

        // add the overpower
        EventJoust ej = new EventJoust(this);
        add((Event) ej);
        add((Action) ej);

        // Add the actions
        actions.add(move);
    }


}
