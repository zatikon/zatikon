///////////////////////////////////////////////////////////////////////
// Name: UnitWall
// Desc: Big brick wall
// Date: 7/29/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWall extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWall(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.WALL;
        name = Strings.UNIT_WALL_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_WALL;

        // start out ready
        deployed = true;

        add(new ActionInorganic(this));
        actions.add(new ActionCollapse(this));

    }


}
