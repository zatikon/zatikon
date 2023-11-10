///////////////////////////////////////////////////////////////////////
// Name: UnitCavalry
// Desc: A horseman
// Date: 6/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitCavalry extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitCavalry(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.CAVALRY;
        category = Unit.HORSEMEN;
        name = Strings.UNIT_CALVARY_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 1;
        life = 5;
        lifeMax = 5;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE, (byte) 4);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_CAVALRY;

        add(new ActionMounted(this));

        // add the overpower
        EventOverpower eo = new EventOverpower(this);
        add((Event) eo);
        add((Action) eo);

        // Add the actions
        actions.add(move);
        actions.add(attack);
        //actions.add(new ActionAttack(this, (byte) 0, (byte) 2, TargetType.UNIT_AREA, (byte) 3));
    }


}
