///////////////////////////////////////////////////////////////////////
// Name: UnitScout
// Desc: A scout
// Date: 7/16/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitScout extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitScout(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.SCOUT;
        category = Unit.SCOUTS;
        name = Strings.UNIT_SCOUT_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_SCOUT;

        // Special ability, usable the turn it comes into play
        //deployed = true;

        // dodgey
        EventDodge ed = new EventDodge(this);
        add((Event) ed);
        add((Action) ed);

        // poisonous
        EventPoison ep = new EventPoison(this);
        add((Event) ep);
        add((Action) ep);

        // skirmish
        EventSkirmish ek = new EventSkirmish(this);
        add((Event) ek);
        add((Action) ek);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        short range = 3;
        Vector<Short> targets =
                getCastle().getBattleField().getArea(
                        this,
                        getCastle().getLocation(),
                        range,
                        true,
                        false,
                        false,
                        false, TargetType.BOTH, getCastle());
        getCastle().getBattleField().addBonusCastleTargets(this, targets, getCastle());
        return targets;
    }

    public int getDeployRange() {
        return 3;
    }
}

