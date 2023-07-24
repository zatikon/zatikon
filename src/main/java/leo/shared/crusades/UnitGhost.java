///////////////////////////////////////////////////////////////////////
// Name: UnitGhost
// Desc: A ghost
// Date: 9/13/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGhost extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGhost(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.POSSESSED;
        name = Strings.UNIT_GHOST_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_GHOST;

        // Add the ability description
        //ActionTrait intangible = new ActionTrait(this, Strings.UNIT_GHOST_2, Strings.UNIT_GHOST_3, "", "");
        //intangible.setDetail(Strings.UNIT_GHOST_7);
        //add(intangible);

        // Add the ability description
        //ActionTrait undying = new ActionTrait(this, Strings.UNIT_GHOST_4, Strings.UNIT_GHOST_5,"", "");
        //undying.setDetail(Strings.UNIT_GHOST_6);
        //add(undying);

        add(new ActionInorganic(this));

        // Add the ability description
        EventUndying eu = new EventUndying(this);
        add((Event) eu);
        add((Action) eu);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

}
