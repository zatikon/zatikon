///////////////////////////////////////////////////////////////////////
// Name: Hero
// Desc: The extension for inquisition units
// Date: 2/16/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.inquisition;

// imports

import leo.shared.*;

import java.util.Vector;


public class Hero extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Hero(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.INQUISITION;

        // Initialize
        id = Unit.HERO;
        name = Strings.HERO_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 0;
        lifeMax = 0;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 0;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_DEMON;

        // Add the actions
        //actions.add(move);
        //actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Add a skill
    /////////////////////////////////////////////////////////////////
    public void addSkill(short newSkill) {
    }

}
