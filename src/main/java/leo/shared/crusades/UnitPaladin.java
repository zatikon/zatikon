///////////////////////////////////////////////////////////////////////
// Name: UnitPaladin
// Desc: A paladin
// Date: 8/29/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitPaladin extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitPaladin(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.PALADIN.value();
        category = Unit.CLERGY;
        name = Strings.UNIT_PALADIN_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_PALADIN;

        // Add the ability description
        ActionTrait sanctuary = new ActionTrait(this, Strings.UNIT_PALADIN_2, Strings.UNIT_PALADIN_3, "");
        sanctuary.setType(Action.OTHER);
        sanctuary.setDetail(Strings.UNIT_PALADIN_4);
        add(sanctuary);

        // Describe his holy death
        ActionTrait grace = new ActionTrait(this, Strings.UNIT_PALADIN_5, Strings.UNIT_PALADIN_6, Strings.UNIT_PALADIN_7);
        grace.setType(Action.SPELL);
        grace.setDetail(Strings.UNIT_PALADIN_8);
        add(grace);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Death event trigger
    /////////////////////////////////////////////////////////////////
    public void deathTrigger(boolean death, Unit source) {
        Vector units = castle.getBattleField().getUnits();
        Iterator it = units.iterator();
        boolean used = false;
        while (it.hasNext()) {
            Unit victim = (Unit) it.next();
            if (victim.getCastle() == castle && death) {
                if (victim.getOrganic(this)) {
                    victim.raiseLife((byte) 1);

                    // Generate a nifty effect
                    castle.getObserver().abilityUsed(getLocation(), victim.getLocation(), Constants.IMG_EYE);
                    used = true;
                }
            }
        }
        if (used) castle.getObserver().playSound(Constants.SOUND_EYE);
    }


    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short directDamage(Unit source, short amount) {
        short inflicted = (byte) (amount - getArmor());
        if (inflicted < 0) inflicted = 0;
        if (inflicted > 1) inflicted = 1;
        life -= inflicted;
        if (life <= 0) die(true, source);

        return inflicted;
    }
}
