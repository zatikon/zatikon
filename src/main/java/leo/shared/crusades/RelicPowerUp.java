///////////////////////////////////////////////////////////////////////
// Name: RelicPowerUp
// Desc: A Relic used by AI. There are several different versions, each
//       has its own ID that corresponds to it, but they are all
//       contained within this one class.
// Date: 8/4/2011 - Julian Noble
// TODO: 
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Random;
import java.util.Vector;


public class RelicPowerUp extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    short power = Action.GROW_TOXIC;
    private Random random;


    // Note: names and details are indexed by the short value for each GROW action. For example, Action.GROW_TOXIC
    //       is short value 0, so names[0] is "Toxic Power Up" and details[0] is "Unit gains the ability to poison..."
    //       These much be changed if changes are made to Grow actions
    String[] names =
            {
                    "Toxic Power Up",
                    "Evasive Power Up",
                    "Resilient Power Up",
                    "Longshank Power Up",
                    "Mighty Power Up",
                    "Clockwork Power Up",
                    "Vampiric Power Up",
                    "Cunning Power Up",
                    "Epic Power Up",
                    "Arcane Power Up",
                    "Ascendant Power Up",
                    "Guardian Power Up",
                    "Vigilant Power Up",
                    "Zealous Power Up",
                    "Rampaging Power Up",
                    "Ruthless Power Up",
                    "Enraged Power Up"
            };

    String[] details =
            {
                    "Unit gains the ability to poison attacked enemies. Poisoned enemies lose 1 max life at the end of each of their turns. Unit also gains +1 max life.",
                    "Unit gains the ability to evade 1 skill or attack per turn. Unit also gains +1 max life.",
                    "Unit gains +3 max life, and +1 armor, with a maximum of 2 armor.",
                    "Unit gains the ability to move 1 space further. Unit also gains +2 max life.",
                    "Unit gains the ability to stun successfully attacked units. Unit also gains +1 max life, and +1 damage",
                    "Unit becomes inorganic and can't be the target of spells or skills. Unit also gains +2 max life, and +1 armor, with a maximum of 2 armor.",
                    "Unit becomes vampiric, gaining life and max life equal to damage dealt to enemies with successful attacks. Unit also gains +1 max life.",
                    "Unit gains +1 max life, and +1 actions.",
                    "Unit gains +2 max life, +2 damage, and +1 armor, with a maximum of 2 armor.",
                    "Unit gains an arcane attack, losing its original attack. The arcane attack costs actions equal to the unit's max action and has a range of 5. Unit also gains +1 max life, and damage is set to 5.",
                    "Unit gains +4 max life, +1 actions, and +2 armor, with a maximum of 2 armor.",
                    "Unit gains the ability to stop the first enemy movement within 4. The enemy unit still pays all costs of the movement. Unit also gains +2 max life.",
                    "Unit gains the ability to automatically attack enemies that move within attack range. Unit also gains +1 max life.",
                    "Unit gains the ability to trade places with the unit it attacks. Unit also gains +2 max life, and +1 armor, with a maximum of 2 armor.",
                    "Unit gains the ability to rampage, gaining 1 action with each kill until end of turn. Unit also gains +1 max life, and +2 damage.",
                    "Unit gains the ability to whirlwind, attacking all enemies within range. Only triggers on successful attacks. Unit also gains +1 max life.",
                    "Unit gains the ability to enrage, gaining +2 max life and +2 damage with each attack. All effects fade if the unit fails to attack on any of its turns."
            };

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RelicPowerUp(Castle newCastle, short newPower) {
        castle = newCastle;

        // power determines what kind of powerup it is and is used with grow to
        //  buff the unit in question
        power = newPower;

        // Initialize
        id = (byte) (Unit.POWERUP - newPower);
        name = names[power];
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 2;
        castleCost = Action.GROW_COST[power];
        isPowerUp = true;
        isRelic = true;
        organic = false;
        // image constants for powerups must remain in the same order as the GROW Actions as well as the unit IDs
        appearance = (Constants.IMG_POWERUP_TOXIC + (int) power);

        // This unit is indestructable
        ActionTrait indestructible = new ActionTrait(this, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_2);
        indestructible.setDetail(Strings.UNIT_SPIRIT_6);
        add(indestructible);

        // This unit is a powerup
        ActionTrait powerUp = new ActionTrait(this, name, name, Strings.RELIC_POWERUP_1);
        powerUp.setDetail(details[power]);
        add(powerUp);

        // This unit loses 1 life per turn
        ActionTrait slowDeath = new ActionTrait(this, Strings.RELIC_POWERUP_2, Strings.RELIC_POWERUP_3, "");
        slowDeath.setDetail(Strings.RELIC_POWERUP_4);
        add(slowDeath);
    }

    /////////////////////////////////////////////////////////////////
    // Random Constructor for AI
    /////////////////////////////////////////////////////////////////
 /*public RelicPowerUp(Castle newCastle)
 { 
  
  random = new Random();
  power = (byte)(random.nextInt((int)Action.GROW_AI_COUNT));
  
  castle = newCastle;

  // Initialize
  id   = Unit.POWERUP - power;
  name  = names[power];
  actions  = new Vector<Action>();
  damage  = 0;
  armor  = 0;
  life  = 4;
  lifeMax  = 4;
  actionsLeft  = 0;
  actionsMax = 0;
  move  = null;
  attack  = null;
  deployCost = 2;
  castleCost = Action.GROW_COST[power];
  isPowerUp = true;
  isRelic = true;
  organic  = false;
  // image constants for powerups must remain in the same order as the GROW Actions
  appearance = ( Constants.IMG_POWERUP_TOXIC + (int)power );

  // Add the ability descriptions
  ActionTrait indestructible = new ActionTrait(this, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_7, Strings.UNIT_SPIRIT_2);
  indestructible.setDetail(Strings.UNIT_SPIRIT_6);
  add(indestructible);
  
  ActionTrait powerUp = new ActionTrait(this, name, Strings.RELIC_POWERUP_1, "");
  powerUp.setDetail(details[power]);
  add(powerUp);
  
  ActionTrait slowDeath = new ActionTrait(this, Strings.RELIC_POWERUP_2, Strings.RELIC_POWERUP_3, "");
  slowDeath.setDetail(Strings.RELIC_POWERUP_4);
  add(slowDeath);
 }*/


    ////////////////////////////////////////////////////////////////
    //  Die slowly
    ////////////////////////////////////////////////////////////////
    public void refresh() {
        // generate effect
        getCastle().getObserver().abilityUsed(getLocation(), getLocation(), Constants.IMG_POOF);
        life--;
        if (life == 0)
            die(false, this);
    }

    /////////////////////////////////////////////////////////////////
    // Never drawn grayed out
    /////////////////////////////////////////////////////////////////
    public boolean ready() {
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Targetable no more
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        return false;
    }

    /////////////////////////////////////////////////////////////////
    // Damage reduced to 0
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        getCastle().getObserver().unitDamaged(source, this, (byte) 0);
        return (byte) 0;
    }


    /////////////////////////////////////////////////////////////////
    // Applies the relic's effects to the targeted unit
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short target) {

        Unit ally = castle.getBattleField().getUnitAt(target);
        // generate effect
        setLocation(target);
        this.getCastle().getObserver().imageDraw(this, target, appearance, 0);
        // give the unit the buff
        ally.grow(power);

    }

    /////////////////////////////////////////////////////////////////
    // Get Power
    /////////////////////////////////////////////////////////////////
    public short getPower() {
        return power;
    }


    public int getDeployRange() {
        return 2;
    }

    /////////////////////////////////////////////////////////////////
    // Can this target the given unit
    /////////////////////////////////////////////////////////////////
    public boolean canTarget(Unit target) {
        if (target.getCastle() == castle) {
            if (target.getOrganic(this) && target.targetable(this) && !target.boss()) {
                if (power == Action.GROW_ARCANE && (target instanceof UnitLancer))
                    return false;
                if (power == Action.GROW_ZEALOUS && ((target instanceof UnitGateGuard) || (target instanceof UnitLancer)))
                    return false;
                if (power == Action.GROW_TOXIC ||
                        power == Action.GROW_MIGHTY ||
                        power == Action.GROW_VAMPIRIC ||
                        power == Action.GROW_EPIC ||
                        power == Action.GROW_ARCANE ||
                        power == Action.GROW_VIGILANT ||
                        power == Action.GROW_ZEALOUS ||
                        power == Action.GROW_RAMPAGING ||
                        power == Action.GROW_RUTHLESS ||
                        power == Action.GROW_ENRAGED) {
                    return target.getBaseDamage() > 0;
                } else if (power == Action.GROW_CUNNING) {
                    return target.getActionsMax() > 0;
                } else {
                    return true;
                }
            }
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
