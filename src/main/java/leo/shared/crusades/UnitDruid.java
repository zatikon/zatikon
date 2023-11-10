///////////////////////////////////////////////////////////////////////
// Name: UnitDruid
// Desc: A Druid
// Date: 4/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
//Bear properties have been commented out for compiling
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDruid extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action purge;
    private final Action wolf;
    private final Action serpent;
    private final Action bear;
    private final EventSummoner summoner;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDruid(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.DRUID;
        category = Unit.NATURE;
        name = Strings.UNIT_DRUID_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 250;
        organic = true;
        appearance = Constants.IMG_DRUID;

        // Add the actions
        actions.add(move);


        // Purge
        purge = new ActionPurge(this, (byte) 4);
        actions.add(purge);

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);

        // Pets
        wolf = new ActionWolves(this);
        actions.add(wolf);
        serpent = new ActionSerpents(this);
        actions.add(serpent);
        bear = new ActionBears(this);
        actions.add(bear);
    }

    // ai gets
    public Action getPurge() {
        return purge;
    }

    public Action getWolf() {
        return wolf;
    }

    public Action getSerpent() {
        return serpent;
    }

    public Action getBear() {
        return bear;
    }

    public EventSummoner getSummonManager() {
        return summoner;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }
}
