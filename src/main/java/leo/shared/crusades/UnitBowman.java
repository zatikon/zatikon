///////////////////////////////////////////////////////////////////////
// Name: UnitBowman
// Desc: A bowman
// Date: 6/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBowman extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean overwatch = false;
    private final ActionOverwatch overwatchAction;
    private final EventVigilant vigilant;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBowman(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.BOWMAN.value();
        category = Unit.ARCHERS;
        name = Strings.UNIT_BOWMAN_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE_JUMP, (byte) 3, Action.ATTACK_ARROW);
        deployCost = 1;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_BOWMAN;

        // create the vigilant event for later use
        vigilant = new EventVigilant(this);

        // end overwatch when it walks
        EventEndOverwatch eeo = new EventEndOverwatch(this);
        add(eeo);

        overwatchAction = new ActionOverwatch(this);

        ActionTrait vaulted = new ActionTrait(this, Strings.UNIT_BOWMAN_2, Strings.UNIT_BOWMAN_3, "", "");
        vaulted.setDetail(Strings.UNIT_BOWMAN_4);
        add(vaulted);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        actions.add(overwatchAction);
    }


    /////////////////////////////////////////////////////////////////
    // Enter overwatch
    /////////////////////////////////////////////////////////////////
    public void enterOverwatch() {
        overwatch = true;
        getActions().remove(overwatchAction);
        getActions().add(0, vigilant);
        add((Event) vigilant);
    }


    /////////////////////////////////////////////////////////////////
    // Enter overwatch
    /////////////////////////////////////////////////////////////////
    public void endOverwatch() {
        if (!overwatch) return;
        overwatch = false;
        getActions().add(overwatchAction);
        remove((Event) vigilant);
        remove((Action) vigilant);
    }


    /////////////////////////////////////////////////////////////////
    // overwatch appearance
    /////////////////////////////////////////////////////////////////
    public int getAppearance() {
        return overwatch ? Constants.IMG_OBOWMAN : Constants.IMG_BOWMAN;
    }


}
