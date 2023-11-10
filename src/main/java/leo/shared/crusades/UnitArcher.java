///////////////////////////////////////////////////////////////////////
// Name: UnitArcher
// Desc: An archer
// Date: 6/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitArcher extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitArcher(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.ARCHER.value();
        category = Unit.ARCHERS;
        name = Strings.UNIT_ARCHER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 4, Action.ATTACK_ARROW);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_ARCHER;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
