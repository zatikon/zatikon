///////////////////////////////////////////////////////////////////////
// Name: UnitLich
// Desc: An lich
// Date: 8/1/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitLich extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLich(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.LICH.value();
        name = Strings.UNIT_LICH_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 4, Action.ATTACK_MAGIC_BALL);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_LICH;

        // steal life from the living
        EventVampire ev = new EventVampire(this);
        add((Event) ev);
        add((Action) ev);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


}
