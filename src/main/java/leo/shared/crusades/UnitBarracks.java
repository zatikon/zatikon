///////////////////////////////////////////////////////////////////////
// Name: UnitBarracks
// Desc: A barracks
// Date: 4/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBarracks extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final EventSummoner summoner;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBarracks(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.BARRACKS;
        category = Unit.STRUCTURES;
        name = Strings.UNIT_BARRACKS_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 1;
        actionsMax = 1;
        move = null;
        attack = null;
        deployCost = 4;
        castleCost = 150;
        organic = false;
        appearance = Constants.IMG_BARRACKS;
        opaque = true;

        add(new ActionInorganic(this));

        // Add the ability description
        ActionTrait deployment = new ActionTrait(this, Strings.UNIT_BARRACKS_2, Strings.UNIT_BARRACKS_3, Strings.UNIT_BARRACKS_4);
        deployment.setDetail(Strings.UNIT_BARRACKS_5);
        add(deployment);

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);


        // Soldiers
        actions.add(new ActionSoldier(this));
        actions.add(new ActionSoldierPromoteOffense(this));
        actions.add(new ActionSoldierPromoteDefense(this));

    }

    public EventSummoner getSummonManager() {
        return summoner;
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        short range = 2;
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
        return 2;
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    @Override
    public Vector<Short> getBonusCastleTargets(Unit looker) {
        if (looker.getCastle() != getCastle()) return null;
        if (!(looker.getCategory() == Unit.SOLDIERS || looker.getCategory() == Unit.COMMANDERS)) return null;

        // Get the targets
        Vector<Short> targets = getBattleField().getArea(
                this,
                getLocation(),
                (byte) 1,
                true,
                false,
                true,
                true, TargetType.BOTH, getCastle());

        return targets;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }

}
