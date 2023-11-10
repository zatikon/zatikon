///////////////////////////////////////////////////////////////////////
// Name: UnitCaptain
// Desc: A captain
// Date: 12/4/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitCaptain extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean on = true;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitCaptain(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.CAPTAIN;
        category = Unit.COMMANDERS;
        name = Strings.UNIT_CAPTAIN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 3, Action.ATTACK_ARROW);
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_CAPTAIN;

        // coordinate strikes
        EventCoordinate ec = new EventCoordinate(this);
        add((Event) ec);
        add((Action) ec);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    public void off() {
        on = false;
    }

    public void on() {
        on = true;
    }

    public boolean coordinate() {
        return on;
    }

}
