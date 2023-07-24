///////////////////////////////////////////////////////////////////////
// Name: UnitDragon
// Desc: Big nasty dragon
// Date: 4/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDragon extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action fireball;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDragon(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.DRAGON;
        category = Unit.WYRMS;
        name = Strings.UNIT_DRAGON_1;
        actions = new Vector<Action>();
        damage = 8;
        armor = 2;
        life = 7;
        lifeMax = 7;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_DRAGON;

        add(new ActionFlight(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Good stuff
        fireball = new ActionFireball(this, (byte) 0, (byte) 2, TargetType.ANY_AREA, (byte) 2, (byte) 4, Unit.DRAGON);
        actions.add(fireball);
    }

    public Action getFireball() {
        return fireball;
    }


}
