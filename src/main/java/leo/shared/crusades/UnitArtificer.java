///////////////////////////////////////////////////////////////////////
// Name: UnitArtificer
// Desc: An artificer
// Date: 12/2/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitArtificer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action mechanize;
    private final Action reconstruct;
    private final Action fortify;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitArtificer(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.ARTIFICER.value();
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_ARTIFICER_1;
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
        appearance = Constants.IMG_ARTIFICER;


        // Add the actions
        actions.add(move);

        mechanize = new ActionMechanize(this);
        fortify = new ActionFortify(this);
        reconstruct = new ActionReconstruct(this);

        // Add the protection
        actions.add(mechanize);
        actions.add(fortify);
        actions.add(reconstruct);

    }

    public Action getMechanize() {
        return mechanize;
    }

    public Action getFortify() {
        return fortify;
    }

}
