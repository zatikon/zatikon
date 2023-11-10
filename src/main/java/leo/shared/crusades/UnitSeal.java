///////////////////////////////////////////////////////////////////////
// Name: UnitSeal
// Desc: The gate seal
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSeal extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int duration = 2;
    private ActionTrait durationDescription;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSeal(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.SEAL.value();
        name = Strings.UNIT_SEAL_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 0;
        lifeMax = 0;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_SEAL;

        // Add the ability description
        ActionTrait indestructible = new ActionTrait(this, Strings.UNIT_SEAL_2, Strings.UNIT_SEAL_2, Strings.UNIT_SEAL_3);
        indestructible.setDetail(Strings.UNIT_SEAL_4);
        add(indestructible);

        durationDescription = new ActionTrait(this, Strings.UNIT_SEAL_5, Strings.UNIT_SEAL_6, "");
        durationDescription.setDetail(Strings.UNIT_SEAL_7);
        actions.add(durationDescription);
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
    // Die on the begining of the turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        super.startTurn();
        duration--;
        if (duration < 1) {
            getBattleField().remove(this);
            getCastle().removeOut(this);
        } else {
            actions.remove(durationDescription);
            durationDescription = new ActionTrait(this, Strings.UNIT_SEAL_5, Strings.UNIT_SEAL_8, "");
            actions.add(durationDescription);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        short inflicted = (byte) (amount - armor);
        if (inflicted < 0) inflicted = 0;
        getCastle().getObserver().unitDamaged(source, this, inflicted);
        return inflicted;
    }


}
