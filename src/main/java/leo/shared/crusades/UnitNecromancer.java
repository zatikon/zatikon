///////////////////////////////////////////////////////////////////////
// Name: UnitNecromancer
// Desc: A necromancer
// Date: 8/1/2003 - Gabe Jones
//       8/15/2011 - added vector to store dead unit locations - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitNecromancer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action death;
    private final Action skeleton;
    private final Action zombie;
    private final EventSummoner summoner;

    // graves stores the locations of where units died - used by skeleton and zombie
    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitNecromancer(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.NECROMANCER;
        category = Unit.BLACK_MAGIC_USERS;
        name = Strings.UNIT_NECROMANCER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionDeath(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 3);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_NECROMANCER;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // build actions
        death = attack;
        skeleton = new ActionSkeleton(this);
        zombie = new ActionZombie(this);

        // Add the lich
        actions.add(new ActionLich(this));

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);

        // This unit can summon at a range
        ActionTrait reanimator = new ActionTrait(this, "Reanimator", "Summon Skeleton or Zombie:", "3 range cost 1 action", "or anywhere cost 0 actions");
        reanimator.setDetail("This unit can either summon within 3 for 1 action, or reanimate the corpse of another dead unit with unlimited range for no actions. A corpse cannot be reanimated more than once. There can be more than one corpse on a square.");
        add(reanimator);

        // Skeletons
        actions.add(skeleton);

        // Zombies
        actions.add(zombie);

        //graves = new Vector<Short>();
        //EventShallowGraves esg = new EventShallowGraves(this);
        //add((Event)esg);

    }

    // AI gets
    public Action getDeath() {
        return death;
    }

    public Action getSkeleton() {
        return skeleton;
    }

    public Action getZombie() {
        return zombie;
    }

    //
    public EventSummoner getSummonManager() {
        return summoner;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }

    public boolean getGraves() {
        return true;
    }
}
