///////////////////////////////////////////////////////////////////////
// Name: UnitLycanthrope
// Desc: A shapeshifter
// Date: 4/27/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitLycanthrope extends Unit implements Lycan {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action wolf;
    private final Action werewolf;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLycanthrope(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = Unit.LYCANTHROPE;
        category = Unit.SHAPESHIFTERS;
        name = Strings.UNIT_LYCANTHROPE_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_LYCANTHROPE;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a werewolf
        werewolf = new ActionWerewolf(this);
        actions.add(werewolf);

        // Turn into a Wolf
        wolf = new ActionLycanWolf(this);
        actions.add(wolf);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLycanthrope(Castle newCastle, boolean real) {
        castle = newCastle;

        // Initialize
        id = Unit.LYCANTHROPE;
        name = Strings.UNIT_LYCANTHROPE_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 150;
        organic = true;
        appearance = Constants.IMG_LYCANTHROPE;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a werewolf
        werewolf = new ActionWerewolf(this, false);
        actions.add(werewolf);

        // Turn into a Wolf
        wolf = new ActionLycanWolf(this, false);
        actions.add(wolf);
    }


    public Action getWolf() {
        return wolf;
    }

    public Action getWerewolf() {
        return werewolf;
    }
}
