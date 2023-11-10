///////////////////////////////////////////////////////////////////////
// Name: UnitWarElephant
// Desc: A war elephant. rawr.
// Date: 11/4/2008 (election day) - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWarElephant extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    // trample damage
    private final short trample = 6;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWarElephant(Castle newCastle) {
        castle = newCastle;

        // Forbidden from the demo
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.WAR_ELEPHANT.value();
        category = Unit.HORSEMEN;
        name = Strings.UNIT_WAR_ELEPHANT_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 2;
        life = 7;
        lifeMax = 7;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 4);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 2, Action.ATTACK_ARROW);
        deployCost = 3;
        castleCost = 300;
        organic = true;
        appearance = Constants.IMG_WAR_ELEPHANT;

        add(new ActionMounted(this));

        EventTrample et = new EventTrample(this);
        add((Event) et);
        add((Action) et);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
