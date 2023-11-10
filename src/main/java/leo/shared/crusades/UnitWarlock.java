///////////////////////////////////////////////////////////////////////
// Name: UnitWarlock
// Desc: 
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWarlock extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action fireball;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWarlock(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.WARLOCK.value();
        category = Unit.BLACK_MAGIC_USERS;
        name = Strings.UNIT_WARLOCK_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_AREA, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 5, Action.ATTACK_MAGIC_BALL);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_WARLOCK;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Good stuff
        fireball = new ActionFireball(this, (byte) 0, (byte) 1, TargetType.ANY_AREA, (byte) 4, (byte) 4, UnitType.WARLOCK.value());
        actions.add(fireball);
        actions.add(new ActionLightning(this, (byte) 0, (byte) 1, TargetType.ANY_LINE_JUMP, (byte) 6));

    }

    public Action getFireball() {
        return fireball;
    }
}
