///////////////////////////////////////////////////////////////////////
// Name: UnitAbjurer
// Desc: An abjurer
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitAbjurer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action banish;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAbjurer(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.ABJURER.value();
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_ABJURER_1;
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
        appearance = Constants.IMG_ABJURER;


        // Add the actions
        actions.add(move);

        banish = new ActionBanish(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 5);

        // Add the protection
        actions.add(banish);
        actions.add(new ActionWard(this));
        actions.add(new ActionSeal(this));
    }

    public Action getBanish() {
        return banish;
    }

}
