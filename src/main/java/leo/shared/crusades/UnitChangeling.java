///////////////////////////////////////////////////////////////////////
// Name: UnitChangeling
// Desc: A changeling
// Date: 10/25/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;

public class UnitChangeling extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean invincible = false;
    private final ActionTrait invincibleTrait;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitChangeling(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.CHANGELING;
        category = Unit.SHAPESHIFTERS;
        name = Strings.UNIT_CHANGELING_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionTrade(this, (byte) 5);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_CHANGELING;

        invincibleTrait = new ActionTrait(this, Strings.UNIT_CHANGELING_2, Strings.UNIT_CHANGELING_3, Strings.UNIT_CHANGELING_4);
        invincibleTrait.setDetail("This unit is indestructible and immune to everything.");

        ActionTrait fey = new ActionTrait(this, Strings.UNIT_CHANGELING_5, Strings.UNIT_CHANGELING_6, Strings.UNIT_CHANGELING_7);
        fey.setDetail(Strings.UNIT_CHANGELING_8);
        add(fey);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Back to normal
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        super.startTurn();
        invincible = false;
        actions.remove(invincibleTrait);
    }


    /////////////////////////////////////////////////////////////////
    // Trade stuffs
    /////////////////////////////////////////////////////////////////
    public void trade() {
        invincible = true;
        actions.add(0, invincibleTrait);
    }


    /////////////////////////////////////////////////////////////////
    // Targetable no more
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        if (invincible) return false;
        return super.targetable(looker);
    }

}
