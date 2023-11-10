///////////////////////////////////////////////////////////////////////
// Name: UnitWerewolf
// Desc: A shapeshifter
// Date: 4/27/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWerewolf extends Unit implements Lycan {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action wolf;
    private final Action werewolf;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWerewolf(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.WEREWOLF;
        name = Strings.UNIT_WEREWOLF_1;
        actions = new Vector<Action>();
        damage = 6;
        armor = 2;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_WEREWOLF;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a human
        werewolf = new ActionLycanthrope(this);
        actions.add(werewolf);

        // Turn into a Wolf
        wolf = new ActionLycanWolf(this);
        actions.add(wolf);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWerewolf(Castle newCastle, boolean real) {
        castle = newCastle;

        // Initialize
        id = UnitType.WEREWOLF;
        name = Strings.UNIT_WEREWOLF_1;
        actions = new Vector<Action>();
        damage = 6;
        armor = 2;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_WEREWOLF;

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a human
        werewolf = new ActionLycanthrope(this, false);
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
