///////////////////////////////////////////////////////////////////////
// Name: UnitChanneler
// Desc: A channeler
// Date: 4/26/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitChanneler extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int energy = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitChanneler(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.CHANNELER;
        category = Unit.NATURE;
        name = Strings.UNIT_CHANNELER_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionChannelBolt(this);
        deployCost = 2;
        castleCost = 250;
        organic = true;
        appearance = Constants.IMG_CHANNELER;

        // Add the energy read-out
        actions.add(new ActionEnergy(this));

        // Add the actions
        actions.add(move);
        actions.add(attack);

        actions.add(new ActionChannelBlast(this));

        //actions.add(new ActionChannel(this));
    }


    /////////////////////////////////////////////////////////////////
    // How much energy?
    /////////////////////////////////////////////////////////////////
    public int getEnergy() {
        return energy;
    }


    /////////////////////////////////////////////////////////////////
    // Set energy
    /////////////////////////////////////////////////////////////////
    public void setEnergy(int newEnergy) {
        energy = newEnergy;
    }


    /////////////////////////////////////////////////////////////////
    // refresh and channel
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (castle.getBattleField() == null) return;
        energy += 2;
        getCastle().getObserver().abilityUsed(getLocation(), getLocation(), Constants.IMG_POOF);
    }

}
