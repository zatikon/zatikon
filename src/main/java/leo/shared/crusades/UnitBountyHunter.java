///////////////////////////////////////////////////////////////////////
// Name: UnitBountyHunter
// Desc: A bounty hunter
// Date: 4/1/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBountyHunter extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit mark = null;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBountyHunter(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.BOUNTY_HUNTER;
        category = Unit.SCOUTS;
        name = Strings.UNIT_BOUNTY_HUNTER_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 2, Action.ATTACK_THROW_SPEAR);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_BOUNTY_HUNTER;

        // immunity to marked target
        ActionTrait relentless = new ActionTrait(this, Strings.UNIT_BOUNTY_HUNTER_2,
                Strings.UNIT_BOUNTY_HUNTER_3,
                "",
                "");
        relentless.setDetail(Strings.UNIT_BOUNTY_HUNTER_4);
        add(relentless);

        // regain actions
        EventEfficiency ee = new EventEfficiency(this);
        add((Event) ee);
        add((Action) ee);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // mark the target
        actions.add(new ActionMark(this));

    }


    /////////////////////////////////////////////////////////////////
    // Some mark getting and setting
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        if (looker == getMark())
            return false;
        return super.targetable(looker);
    }


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
