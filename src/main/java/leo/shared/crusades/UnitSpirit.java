///////////////////////////////////////////////////////////////////////
// Name: UnitSpirit
// Desc: A sprit
// Date: 5/30/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSpirit extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSpirit(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.SPIRIT.value();
        name = Strings.UNIT_SPIRIT_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 0;
        lifeMax = 0;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_SPIRIT;
        canWin = false;

        // Add the ability description
        ActionTrait indestructible = new ActionTrait(this, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_2);
        indestructible.setDetail(Strings.UNIT_SPIRIT_6);
        add(indestructible);

        ActionTrait intangible = new ActionTrait(this, Strings.UNIT_SPIRIT_3, Strings.UNIT_SPIRIT_4, "");
        intangible.setDetail(Strings.UNIT_SPIRIT_5);
        add(intangible);

        // Add the actions
        actions.add(move);
        actions.add(attack);

    }


    /////////////////////////////////////////////////////////////////
    // Die, nothing happens
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
    }


    /////////////////////////////////////////////////////////////////
    // Targetable no more
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        getCastle().getObserver().unitDamaged(source, this, (byte) 0);
        return (byte) 0;
    }


}
