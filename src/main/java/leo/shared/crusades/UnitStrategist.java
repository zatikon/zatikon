///////////////////////////////////////////////////////////////////////
// Name: UnitStrategist
// Desc: A Stragegist
// Date: 3/19/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitStrategist extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final EventStratagem es;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitStrategist(Castle newCastle) {
        castle = newCastle;

        // access kevek
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.STRATEGIST.value();
        category = Unit.COMMANDERS;
        name = Strings.UNIT_STRATEGIST_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_STRATEGIST;

        // Add the ability description
        es = new EventStratagem(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
    }


    /////////////////////////////////////////////////////////////////
    // Start the turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        super.startTurn();
        short extras = es.getExtras();
        getCastle().setCommandsLeft((byte) (getCastle().getCommandsLeft() + extras));
    }
}
