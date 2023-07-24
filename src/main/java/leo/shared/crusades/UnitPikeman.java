///////////////////////////////////////////////////////////////////////
// Name: UnitPikeman
// Desc: A pikeman
// Date: 6/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitPikeman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitPikeman(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.PIKEMAN;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_PIKEMAN_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 2);
        deployCost = 1;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_PIKEMAN;

        ActionTrait reach = new ActionTrait(this,
                Strings.UNIT_PIKEMAN_2,
                Strings.UNIT_PIKEMAN_3,
                "",
                "");
        reach.setDetail(Strings.UNIT_PIKEMAN_4);
        add(reach);

        EventVigilant ev = new EventVigilant(this);
        add((Event) ev);
        add((Action) ev);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
