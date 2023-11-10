///////////////////////////////////////////////////////////////////////
// Name: UnitTactician
// Desc: A tactician
// Date: 7/18/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitTactician extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitTactician(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.TACTICIAN.value();
        category = Unit.COMMANDERS;
        name = Strings.UNIT_TACTICIAN_1;
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
        appearance = Constants.IMG_TACTICIAN;

        // Add the ability description
        ActionTrait tactics = new ActionTrait(this, Strings.UNIT_TACTICIAN_2, Strings.UNIT_TACTICIAN_3, "");
        tactics.setDetail(Strings.UNIT_TACTICIAN_4);
        add(tactics);

        ActionTrait contingency = new ActionTrait(this, Strings.UNIT_TACTICIAN_5, Strings.UNIT_TACTICIAN_6, Strings.UNIT_TACTICIAN_7);
        contingency.setDetail(Strings.UNIT_TACTICIAN_8);
        add(contingency);

        // Add the actions
        actions.add(move);

        // Add the strategy ability
        //actions.add(new ActionStrategy(this));
    }


    /////////////////////////////////////////////////////////////////
    // It died
    /////////////////////////////////////////////////////////////////
    public void deathTrigger(boolean death, Unit source) {
        if (death) {
            getCastle().setCommandsLeft((byte) (getCastle().getCommandsLeft() + 3));
        }
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() + 1));
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() - 1));
    }


}
