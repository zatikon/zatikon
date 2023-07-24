///////////////////////////////////////////////////////////////////////
// Name: UnitWitch
// Desc: A witch
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWitch extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action sicken;
    private final Action toad;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWitch(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.WITCH;
        category = Unit.BLACK_MAGIC_USERS;
        name = Strings.UNIT_WITCH_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionSicken(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 5);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_WITCH;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        sicken = attack;

        // Add the maladies
        toad = new ActionToad(this);
        actions.add(toad);
        actions.add(new ActionCurse(this));
    }

    public Action getSicken() {
        return sicken;
    }

    public Action getToad() {
        return toad;
    }


}
