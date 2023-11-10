///////////////////////////////////////////////////////////////////////
// Name: UnitConjurer
// Desc: A conjurer
// Date: 5/16/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitConjurer extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final EventSummoner summoner;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitConjurer(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.CONJURER.value();
        category = Unit.WHITE_MAGIC_USERS;
        name = Strings.UNIT_CONJURER_1;
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
        castleCost = 350;
        organic = true;
        appearance = Constants.IMG_CONJURER;

        // Add the actions
        actions.add(move);

        // Add the ability
        actions.add(new ActionInvert(this));

        // Summoning Manager
        summoner = new EventSummoner(this);
        add((Event) summoner);
        add((Action) summoner);

        // Add portals
        actions.add(new ActionPortal(this));
        actions.add(new ActionGate(this));
    }

    public EventSummoner getSummonManager() {
        return summoner;
    }

    public void die(boolean death, Unit source) {
        summoner.summonerDies();
        super.die(death, source);
    }
}
