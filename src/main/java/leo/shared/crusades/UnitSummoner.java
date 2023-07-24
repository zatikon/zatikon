///////////////////////////////////////////////////////////////////////
// Name: UnitSummoner
// Desc: An imp
// Date: 7/10/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSummoner extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private final Action archDemon;
    //private Action demon;
    private final Action imp;
    private final Action damn;
    private final EventSummoner summoner;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSummoner(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.SUMMONER;
        category = Unit.BLACK_MAGIC_USERS;
        name = Strings.UNIT_SUMMONER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        //attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_SUMMONER;

        // Add the actions
        actions.add(move);
        //actions.add(attack);
        //actions.add(attack);

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);

        // Demon! Demon!!!
        imp = new ActionImp(this);
        actions.add(imp);
        damn = new ActionDamn(this);
        actions.add(damn);
        //demon = new ActionDemon(this);
        //actions.add(demon);
        archDemon = new ActionArchDemon(this);
        actions.add(archDemon);
    }

    // ai gets
    public Action getImp() {
        return imp;
    }

    public Action getDemon() {
        return damn;
    }

    public Action getArchDemon() {
        return archDemon;
    }

    public EventSummoner getSummonManager() {
        return summoner;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }
}
