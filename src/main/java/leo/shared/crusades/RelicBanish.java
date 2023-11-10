///////////////////////////////////////////////////////////////////////
// Name: RelicBanish
// Desc: A Relic (one use per battle) that banishes the target
// Date: 10/04/2010 - Linus Foster
// TODO: 
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class RelicBanish extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RelicBanish(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.RELIC_BANISH.value();
        category = Unit.STRUCTURES;
        name = Strings.RELIC_BANISH_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 0;
        lifeMax = 0;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 50;
        isRelic = true;
        organic = false;
        appearance = Constants.IMG_RELIC_BANISH;

        ActionTrait relicDeploy = new ActionTrait(this, Strings.RELIC_BASE_1, Strings.RELIC_BASE_2, Strings.RELIC_BASE_3);
        relicDeploy.setType(Action.SKILL);
        relicDeploy.setDetail(Strings.RELIC_BASE_4);
        add(relicDeploy);

        ActionTrait relicEffect = new ActionTrait(this, Strings.RELIC_BANISH_2, Strings.RELIC_BANISH_3, Strings.RELIC_BANISH_4);
        relicEffect.setType(Action.SKILL);
        relicEffect.setDetail(Strings.RELIC_BANISH_5);
        add(relicEffect);

    }


    /////////////////////////////////////////////////////////////////
    // Applies the relic's effects to the targeted unit
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short target) {
        Unit victim = castle.getBattleField().getUnitAt(target);
        // generate effect
        setLocation(target);
        this.getCastle().getObserver().imageDraw(this, target, appearance, 0);

        victim.grow(Action.GROW_RELIC_BANISH); // Also uses the A.I.'s grow properties
    }


    public int getDeployRange() {
        return 2;
    }

    /////////////////////////////////////////////////////////////////
    // Can this target the given unit
    /////////////////////////////////////////////////////////////////
    public boolean canTarget(Unit target) {
        if (target.getCastle() == castle) {
            return target.getOrganic(this) && target.targetable(this) && !target.boss();
        }
        return false;
    }

    /////////////////////////////////////////////////////////////////
    // Get deploy targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        short range = 2;
        Vector<Short> locations = getCastle().getBattleField().getArea(
                this,
                getCastle().getLocation(),
                range,
                false,
                true,
                false,
                false, TargetType.BOTH, getCastle());

        Vector<Short> removers = new Vector<Short>();
        Iterator<Short> targs = locations.iterator();
        while (targs.hasNext()) {
            short targ = targs.next();
            Unit victim = this.getCastle().getBattleField().getUnitAt(targ);
            if (victim == null) continue;
            if (!canTarget(victim))
                removers.add(targ);
        }
        locations.removeAll(removers);

        return locations;
    }
}
