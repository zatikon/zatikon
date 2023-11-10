///////////////////////////////////////////////////////////////////////
// Name: UnitDuelist
// Desc: 
// Date: 9/15/2010 - Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDuelist extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    public Vector damagedunits;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDuelist(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.DUELIST.value();
        category = Unit.SOLDIERS;
        name = Strings.UNIT_DUELIST_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 1);
        deployCost = 2;
        castleCost = 250;
        organic = true;
        appearance = Constants.IMG_DUELIST;

        damagedunits = new Vector<Unit>();

  /*ActionTrait reach = new ActionTrait(this,
                        Strings.UNIT_DUELIST_2,
                        Strings.UNIT_DUELIST_3,
                        "",
                        "");
  reach.setDetail(Strings.UNIT_DUELIST_4);
  add(reach);*/

        EventParry ep = new EventParry(this, 2);
        add((Event) ep);
        add((Action) ep);

        EventDuelist ed = new EventDuelist(this);
        add((Event) ed);
        add((Action) ed);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Skills
        //actions.add(new ActionRush(this, (byte) 4));
    }

    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        if (damagedunits.contains(source)) {
            getCastle().getObserver().unitDamaged(source, this, (byte) 0);
            return (byte) 0;
        }
        return super.damage(source, amount);
    }

}