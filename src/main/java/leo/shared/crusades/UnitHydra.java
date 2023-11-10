///////////////////////////////////////////////////////////////////////
// Name: UnitHydra
// Desc: A hydra
// Date: 6/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitHydra extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    //private Action serpent;
    private int heads;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitHydra(Castle newCastle) {
        castle = newCastle;
        heads = 6;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.HYDRA.value();
        category = Unit.WYRMS;
        name = Strings.UNIT_HYDRA_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionHydraAttack(this, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_HYDRA;

        // Add the ability description
        ActionTrait regeneration = new ActionTrait(this, Strings.UNIT_HYDRA_2, Strings.UNIT_HYDRA_3, Strings.UNIT_HYDRA_4);
        regeneration.setDetail(Strings.UNIT_HYDRA_5);
        add(regeneration);

        // heads
        ActionTrait heads = new ActionTrait(this, Strings.UNIT_HYDRA_6, Strings.UNIT_HYDRA_7, "");
        heads.setDetail(Strings.UNIT_HYDRA_8);
        add(heads);

        // Poisons melee attackers
        EventToxidermis et = new EventToxidermis(this, (byte) 1);
        add((Event) et);
        add((Action) et);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        //serpent = new ActionSerpent(this);
        //actions.add(serpent);
    }

    /////////////////////////////////////////////////////////////////
    // Die, nothing happens
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death && (heads > 1)) {
            heads--;
            heal(this);
            this.stun();
            return;
        }
        super.die(death, source);
    }

    /////////////////////////////////////////////////////////////////
    // Regeneration
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (heads < 6) heads++;
    }


    /////////////////////////////////////////////////////////////////
    // Get missing heads
    /////////////////////////////////////////////////////////////////
    public short getMissingHeads() {
        return (byte) (6 - heads);
    }


    /////////////////////////////////////////////////////////////////
    // Get heads
    /////////////////////////////////////////////////////////////////
    public short getHeads() {
        return (byte) heads;
    }


    /////////////////////////////////////////////////////////////////
    // Get appearance
    /////////////////////////////////////////////////////////////////
    public int getAppearance() {
        return (appearance + (getMissingHeads() * 2));
    }


    public Action getSerpent() {
        return null;
    }
}
