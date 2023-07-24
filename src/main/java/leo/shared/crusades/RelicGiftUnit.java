///////////////////////////////////////////////////////////////////////
// Name: RelicGiftUnit
// Desc: A Relic (one use per battle) that gives a unit to another ally
//       For 2 vs. 2 or Coop only.
// Date: 10/12/2010 - Alexander McCaleb
// TODO: Works correctly, but still displays all friendly units as 
//       targets when performing action. Now limited to one time use
//   however, unit that is being gifted takes an additional 
//   turn to make the jump to the other team
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class RelicGiftUnit extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    /////////////////////////////////////////////////////////////////
    // Constructor: Same as any other relic with logical changes made
    /////////////////////////////////////////////////////////////////
    public RelicGiftUnit(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.RELIC_GIFT_UNIT;
        category = Unit.RELICS;
        name = Strings.RELIC_GIFT_UNIT_1;
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
        appearance = Constants.IMG_RELIC_GIFT_UNIT;

        ActionTrait relicDeploy = new ActionTrait(this, Strings.RELIC_BASE_1, Strings.RELIC_BASE_2, Strings.RELIC_BASE_3);
        relicDeploy.setType(Action.SKILL);
        relicDeploy.setDetail(Strings.RELIC_BASE_4);
        add(relicDeploy);

        ActionTrait relicEffect = new ActionTrait(this, Strings.RELIC_GIFT_UNIT_2, Strings.RELIC_GIFT_UNIT_3, Strings.RELIC_GIFT_UNIT_4);
        relicEffect.setType(Action.SKILL);
        relicEffect.setDetail(Strings.RELIC_GIFT_UNIT_5);
        add(relicEffect);

        ActionTrait relicReturn = new ActionTrait(this, Strings.RELIC_GIFT_UNIT_6, Strings.RELIC_GIFT_UNIT_7, Strings.RELIC_GIFT_UNIT_8);
        relicReturn.setType(Action.SKILL);
        relicReturn.setDetail(Strings.RELIC_GIFT_UNIT_9);
        add(relicReturn);

    }


    /////////////////////////////////////////////////////////////////
    // Applies the relic's effects to the targeted unit
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short target) {
        Unit ally = castle.getBattleField().getUnitAt(target);

        // generate effect
        this.getCastle().getObserver().attack(this, ally, (byte) 0, Action.ATTACK_RELIC);

        short t = ally.getTeam();
        if (t == TEAM_1)
            ally.setTeam(TEAM_2);
        else
            ally.setTeam(TEAM_1);
        //ally.getCastle().removeOut(ally);
        //ally.getCastle().add(0, ally);
        //ally.getCastle().deploy(ally.getTeam(), ally, target);
        ally.stun();

        //ally.grow(Action.GROW_RELIC_GIFT); //Implemented to use the A.I.'s grow properties, much like all the other unis/relics

        getCastle().add(Unit.getUnit(Unit.RELIC_GIFT_UNIT, getCastle()));
        getCastle().getObserver().castleAddition(this);
    }


    /////////////////////////////////////////////////////////////////
    // Get deploy targets. Similar to any other relic's implementation
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        Vector<Short> locations = new Vector<Short>();

        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                // Left over piece from older implementation. Might be useful for a revision however
                if (victim.getOrganic(this) && victim.targetable(this))// && (victim.getTeam() != ally/*&& victim.getAttack() != null && victim.getBaseDamage() > 0*/)
                {
                    locations.add(new Short(victim.getLocation()));
                }
            }
        }
        return locations;
    }
}
