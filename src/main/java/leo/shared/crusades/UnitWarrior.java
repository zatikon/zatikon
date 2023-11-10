///////////////////////////////////////////////////////////////////////
// Name: UnitWarrior
// Desc: A warrior
// Date: 7/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWarrior extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWarrior(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.WARRIOR.value();
        category = Unit.SOLDIERS;
        name = Strings.UNIT_WARRIOR_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_WARRIOR;

        // hit everything nearby
        EventWhirlwind ew = new EventWhirlwind(this);
        add((Event) ew);
        add((Action) ew);

        // gain actions as things die
        EventRampage er = new EventRampage(this);
        add((Event) er);
        add((Action) er);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
