///////////////////////////////////////////////////////////////////////
// Name: UnitBallista
// Desc: A ballista
// Date: 7/31/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBallista extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBallista(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.BALLISTA;
        category = Unit.SIEGE;
        name = Strings.UNIT_BALLISTA_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 6, Action.ATTACK_SPEAR);
        deployCost = 3;
        castleCost = 100;
        organic = false;
        appearance = Constants.IMG_BALLISTA;

        add(new ActionInorganic(this));

        // add the push event
        EventPush ep = new EventPush(this);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }
}
