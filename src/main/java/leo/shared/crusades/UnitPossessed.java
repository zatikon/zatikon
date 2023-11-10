///////////////////////////////////////////////////////////////////////
// Name: UnitPossessed
// Desc: The posessed
// Date: 3/19/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitPossessed extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    EventPossessed ep;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitPossessed(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // low priority
        eventPriority = 2;

        // Initialize
        id = UnitType.POSSESSED.value();
        category = Unit.CULTISTS;
        name = Strings.UNIT_POSSESSED_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_POSSESSED;

        // the black aura
        //haunted  = true;

        // Add the ability description
        ActionTrait host = new ActionTrait(this, Strings.UNIT_POSSESSED_2, Strings.UNIT_POSSESSED_3, "", Strings.UNIT_POSSESSED_4);
        host.setDetail(Strings.UNIT_POSSESSED_5);
        Unit hiddenUnit = new UnitGhost(getCastle());
        host.setHiddenUnit(hiddenUnit);
        add(host);

        // Add the ability description
        ep = new EventPossessed(this);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);
        //actions.add(new ActionSuicidal(this));
    }

    public EventPossessed possessEvent() {
        return ep;
    }

    /////////////////////////////////////////////////////////////////
    // Die, create ghost
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        super.die(false, source);
        if (death) {
            UnitGhost ghost = new UnitGhost(getCastle());
            ghost.setLocation(getLocation());
            ghost.setBattleField(getCastle().getBattleField());
            ghost.getCastle().getBattleField().add(ghost);
            ghost.getCastle().addOut(getTeam(), ghost);
            ghost.grow(getBonus());
            ghost.refresh();

            // Animation
            getCastle().getObserver().abilityUsed(ghost.getLocation(), ghost.getLocation(), Constants.IMG_POOF);
        }
    }
}
