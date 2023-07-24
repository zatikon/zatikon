///////////////////////////////////////////////////////////////////////
// Name: UnitAcolyte
// Desc: An acolyte
// Date: 6/20/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitAcolyte extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit shield = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAcolyte(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.ACOLYTE;
        category = Unit.CLERGY;
        name = Strings.UNIT_ACOLYTE_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 2;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_ACOLYTE;

        // add the shielding event
        EventShield es = new EventShield(this);
        add(es);

        // Add the actions
        actions.add(move);

        // Heal
        actions.add(new ActionShield(this));
        actions.add(new ActionHeal(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1));
    }


    /////////////////////////////////////////////////////////////////
    // Some shield getting and setting
    /////////////////////////////////////////////////////////////////
    public void setShield(Unit newUnit) {
        shield = newUnit;
    }

    public Unit getShield() {
        if (shield == null || shield.isDead() || !shield.getOrganic(this) || !shield.targetable(this))
            return null;
        else
            return shield;
    }

    public Unit getLink() {
        return getShield();
    }
}
