///////////////////////////////////////////////////////////////////////
// Name: UnitShieldMaiden
// Desc: A shield maiden
// Date: 5/25/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitShieldMaiden extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit protecting = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitShieldMaiden(Castle newCastle) {
        castle = newCastle;

        // special thing to make block work right
        eventPriority = 4;

        // Initialize
        id = Unit.SHIELD_MAIDEN;
        category = Unit.CLERGY;
        name = Strings.UNIT_SHIELD_MAIDEN_1;
        actions = new Vector<Action>();
        damage = 2;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_SHIELD_MAIDEN;

        // Damage redirecting effect
        EventProtect ep = new EventProtect(this);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Set the protect target
        actions.add(new ActionProtect(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 1));
    }


    /////////////////////////////////////////////////////////////////
    // Get/Set Protecting
    /////////////////////////////////////////////////////////////////
    public Unit getProtecting() {
        if (protecting == null || !protecting.getOrganic(this) || protecting.isDead() || !protecting.targetable(this))
            return null;
        else
            return protecting;
    }

    public void setProtecting(Unit newUnit) {
        protecting = newUnit;
    }

    public Unit getLink() {
        return getProtecting();
    }

}
