///////////////////////////////////////////////////////////////////////
// Name: UnitGeneral
// Desc: A general
// Date: 7/18/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGeneral extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGeneral(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.GENERAL.value();
        category = Unit.COMMANDERS;
        name = Strings.UNIT_GENERAL_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_GENERAL;

        // Add the ability description
        ActionTrait commanding = new ActionTrait(this, Strings.UNIT_GENERAL_2, Strings.UNIT_GENERAL_3, Strings.UNIT_GENERAL_4);
        commanding.setDetail(Strings.UNIT_GENERAL_5);
        add(commanding);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() + 1));
        getCastle().setLogistics((byte) (getCastle().getLogistics() + 1));
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() - 1));
        getCastle().setLogistics((byte) (getCastle().getLogistics() - 1));
    }


}
