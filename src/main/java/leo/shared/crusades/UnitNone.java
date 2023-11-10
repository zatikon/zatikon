///////////////////////////////////////////////////////////////////////
// Name: UnitNone
// Desc: Nothing
// Date: 2/16/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Vector;


public class UnitNone extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitNone(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.NONE.value();
        name = Strings.UNIT_NONE_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 0;
        lifeMax = 0;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 0;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_DOPPELGANGER;

    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        Logger.error(Strings.UNIT_NONE_2);
        die(false, this);
    }


    /////////////////////////////////////////////////////////////////
    // Die
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        super.die(false, source);
    }

}
