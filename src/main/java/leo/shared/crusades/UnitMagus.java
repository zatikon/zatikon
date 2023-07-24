///////////////////////////////////////////////////////////////////////
// Name: UnitMagus
// Desc: A Magus
// Date: 5/30/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitMagus extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action paralyze;
    private final Action wisp;
    private final Action spirit;
    private final EventSummoner summoner;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMagus(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.MAGUS;
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_MAGUS_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionParalyze(this, (byte) 5, (byte) 1);
        deployCost = 3;
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_MAGUS;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        paralyze = attack;

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);

        // Make will-o-the-wisps
        wisp = new ActionWillWisps(this);
        actions.add(wisp);

        // Become a spirit
        spirit = new ActionSpirit(this);
        actions.add(spirit);
    }

    // ai gets
    public Action getParalyze() {
        return paralyze;
    }

    public Action getWisp() {
        return wisp;
    }

    public Action getSpirit() {
        return spirit;
    }

    //
    public EventSummoner getSummonManager() {
        return summoner;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }
}
