///////////////////////////////////////////////////////////////////////
// Name: UnitLycanWolf
// Desc: Growl snarl growl
// Date: 4/27/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitLycanWolf extends Unit implements Lycan {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action wolf;
    private final Action werewolf;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLycanWolf(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.LYCANWOLF;
        name = Strings.UNIT_LYCAN_WOLF_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE_JUMP, (byte) 2);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_LYCANWOLF;

        // Pounce event
        EventPounce ep = new EventPounce(this);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a human
        wolf = new ActionLycanthrope(this);
        actions.add(wolf);

        // Turn into a werewolf
        werewolf = new ActionWerewolf(this);
        actions.add(werewolf);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitLycanWolf(Castle newCastle, boolean real) {
        castle = newCastle;

        // Initialize
        id = UnitType.LYCANWOLF;
        name = Strings.UNIT_LYCAN_WOLF_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE_JUMP, (byte) 2);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_LYCANWOLF;

        // Pounce event
        EventPounce ep = new EventPounce(this);
        add((Event) ep);
        add((Action) ep);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Turn into a human
        wolf = new ActionLycanthrope(this, false);
        actions.add(wolf);

        // Turn into a werewolf
        werewolf = new ActionWerewolf(this, false);
        actions.add(werewolf);
    }

    public Action getWolf() {
        return wolf;
    }

    public Action getWerewolf() {
        return werewolf;
    }


}
