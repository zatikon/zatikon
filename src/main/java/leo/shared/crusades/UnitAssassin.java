///////////////////////////////////////////////////////////////////////
// Name: UnitAssassin
// Desc: A assassin
// Date: 7/18/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitAssassin extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitAssassin(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.ASSASSIN;
        category = Unit.SCOUTS;
        name = Strings.UNIT_ASSASSIN_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionDeath(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_ASSASSIN;

        // Add the ability description
        ActionTrait disguise = new ActionTrait(this, Strings.UNIT_ASSASSIN_2, Strings.UNIT_ASSASSIN_3, Strings.UNIT_ASSASSIN_4);
        disguise.setType(Action.SKILL);
        disguise.setDetail(Strings.UNIT_ASSASSIN_5);
        add(disguise);

        // deploy goes boom
        ActionTrait smokebomb = new ActionTrait(this, Strings.UNIT_ASSASSIN_6, Strings.UNIT_ASSASSIN_7, Strings.UNIT_ASSASSIN_8);
        smokebomb.setType(Action.SKILL);
        smokebomb.setDetail(Strings.UNIT_ASSASSIN_9);
        add(smokebomb);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Poison enemies
        actions.add(new ActionPoison(this, (byte) 3));

    }


    /////////////////////////////////////////////////////////////////
    // undeploy what was once there
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short target) {
        Unit ally = castle.getBattleField().getUnitAt(target);
        ally.getCastle().add(Unit.getUnit(ally.getID(), ally.getCastle()));

        // Out of the field...
        ally.die(false, this);

        // Warn the client about an addition
        getCastle().getObserver().castleAddition(ally);

        // stunball
        getCastle().getObserver().abilityUsed(target, target, Constants.IMG_BIG_POOF);

        // Get the targets
        Vector<Short> targets =
                castle.getBattleField().getArea(
                        this,
                        target,
                        (byte) 1,
                        false,
                        true,
                        true,
                        false, TargetType.BOTH, getCastle());

        // Hit the targets
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            Unit victim = castle.getBattleField().getUnitAt(location.byteValue());

            if (victim != null && victim.getOrganic(this)) {

                short result = castle.getBattleField().event(Event.PREVIEW_ACTION, this, victim, Action.SKILL, Event.NONE, Event.OK);
                if (result == Event.OK) {
                    victim.stun();
                }
                if (result == Event.CANCEL) {
                    castle.getObserver().abilityUsed(getLocation(), victim.getLocation(), Constants.IMG_MISS);
                }
            }
        }

    }


    /////////////////////////////////////////////////////////////////
    // Get deploy targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        Vector<Short> locations = new Vector<Short>();

        Vector<Unit> units = castle.getBattleField().getUnits();

        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                if (victim.getOrganic(this) && victim.targetable(this)) {
                    locations.add(new Short(victim.getLocation()));
                }
            }
        }
        return locations;
    }
}
