///////////////////////////////////////////////////////////////////////
// Name: UnitFireArcher
// Desc: A fire archer
// Date: 6/11/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitFireArcher extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action fireball;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitFireArcher(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.FIRE_ARCHER;
        category = Unit.ARCHERS;
        name = Strings.UNIT_FIRE_ARCHER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_FIRE_ARCHER;

        // Add the actions
        actions.add(move);

        // Good stuff
        fireball = new ActionFireball(this, (byte) 0, (byte) 1, TargetType.ANY_AREA, (byte) 3, (byte) 3, Unit.FIRE_ARCHER);
        actions.add(fireball);

    }

    public Action getFireball() {
        return fireball;
    }


}
