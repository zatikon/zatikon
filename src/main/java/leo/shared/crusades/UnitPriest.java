///////////////////////////////////////////////////////////////////////
// Name: UnitPriest
// Desc: A priest
// Date: 6/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitPriest extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitPriest(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.PRIEST;
        category = Unit.CLERGY;
        name = Strings.UNIT_PRIEST_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionConvert(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 1);
        deployCost = 2;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_PRIEST;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Heal
        actions.add(new ActionHeal(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1));
    }


}
