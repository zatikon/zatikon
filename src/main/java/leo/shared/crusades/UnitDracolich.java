///////////////////////////////////////////////////////////////////////
// Name: UnitDracolich
// Desc: A dracolich
// Date: 4/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDracolich extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDracolich(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.DRACOLICH;
        category = Unit.WYRMS;
        name = Strings.UNIT_DRACOLICH_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_DRACOLICH;

        // Add despair
        ActionTrait despair = new ActionTrait(this, Strings.UNIT_DRACOLICH_2, Strings.UNIT_DRACOLICH_3, "");
        despair.setDetail(Strings.UNIT_DRACOLICH_4);
        add(despair);

        // raise the dead
        EventTombLord etl = new EventTombLord(this);
        add((Event) etl);
        add((Action) etl);

        // steal life from the living
        EventVampire ev = new EventVampire(this);
        add((Event) ev);
        add((Action) ev);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        Castle tmpCastle;
        if (getCastle() == getCastle().getBattleField().getCastle1())
            tmpCastle = getCastle().getBattleField().getCastle2();
        else
            tmpCastle = getCastle().getBattleField().getCastle1();

        tmpCastle.setCommandsMax((byte) (tmpCastle.getCommandsMax() - 1));
        tmpCastle.setCommandsLeft((byte) (tmpCastle.getCommandsLeft() - 1));
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
        Castle tmpCastle;
        if (getCastle() == getCastle().getBattleField().getCastle1())
            tmpCastle = getCastle().getBattleField().getCastle2();
        else
            tmpCastle = getCastle().getBattleField().getCastle1();

        tmpCastle.setCommandsMax((byte) (tmpCastle.getCommandsMax() + 1));
        tmpCastle.setCommandsLeft((byte) (tmpCastle.getCommandsLeft() + 1));
    }
}
