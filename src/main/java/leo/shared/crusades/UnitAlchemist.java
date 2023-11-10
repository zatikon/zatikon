///////////////////////////////////////////////////////////////////////
// Name: UnitAlchemist
// Desc: An alchemist
// Date: 3/23/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitAlchemist extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAlchemist(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.ALCHEMIST.value();
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_ALCHEMIST_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_ALCHEMIST;


        // Add the actions
        actions.add(move);

        // Add the protection
        actions.add(new ActionBrew(this, Potion.HEAL));
        actions.add(new ActionBrew(this, Potion.STRENGTH));
        actions.add(new ActionBrew(this, Potion.ESCAPE));
        actions.add(new ActionBrew(this, Potion.LOVE));
    }
}
