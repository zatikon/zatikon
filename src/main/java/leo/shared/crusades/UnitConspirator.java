///////////////////////////////////////////////////////////////////////
// Name: UnitConspirator
// Desc: 
// Date: 9/29/2010 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitConspirator extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit mark = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitConspirator(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.CONSPIRATOR.value();
        category = Unit.COMMANDERS;
        name = Strings.UNIT_CONSPIRATOR_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 0;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_CONSPIRATOR;

        // regain actions
        EventEfficiency ee = new EventEfficiency(this);
        add((Event) ee);
        add((Action) ee);

        // Add the actions
        actions.add(move);
        //actions.add(attack);

        // mark the target
        actions.add(new ActionMark(this));

    }


    /////////////////////////////////////////////////////////////////
    // Some mark getting and setting (Actually the conspirator shouldn't be immune to marked target...)
    /////////////////////////////////////////////////////////////////
 /*public boolean targetable(Unit looker)
 { if (looker == getMark())
   return false;
  return super.targetable(looker);
 }*/


    /////////////////////////////////////////////////////////////////
    // Some mark getting and setting
    /////////////////////////////////////////////////////////////////
    public void setMark(Unit newUnit) {
        mark = newUnit;
    }

    public Unit getMark() {
        if (mark == null || !mark.getOrganic(this) || !mark.targetable(this))
            return null;
        else
            return mark;
    }

    public Unit getLink() {
        if (mark == null || mark.isDead()) return null;
        return getMark();
    }
}
