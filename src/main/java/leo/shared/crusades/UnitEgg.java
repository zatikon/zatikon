///////////////////////////////////////////////////////////////////////
// Name: UnitEgg
// Desc: An egg
// Date: 7/14/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitEgg extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int duration = 3;
    private final ActionTrait durationDescription;
    private final String hatch = Strings.UNIT_EGG_1;
    private Vector<Short> storedBonus = new Vector<Short>();

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitEgg(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.EGG.value();
        name = Strings.UNIT_EGG_2;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 9;
        lifeMax = 9;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_EGG;

        add(new ActionInorganic(this));

        durationDescription = new ActionTrait(this, Strings.UNIT_EGG_3, hatch + duration + Strings.UNIT_EGG_4, "");
        durationDescription.setHiddenUnit(new UnitWyvern(getCastle()));
        actions.add(durationDescription);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitEgg(Castle newCastle, boolean nohidden) {
        castle = newCastle;

        // Initialize
        id = UnitType.EGG.value();
        name = Strings.UNIT_EGG_2;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_EGG;

        add(new ActionInorganic(this));

        durationDescription = new ActionTrait(this, Strings.UNIT_EGG_3, hatch + duration + Strings.UNIT_EGG_4, "");
        actions.add(durationDescription);
    }

    public void setStorage(Vector<Short> storage) {
        storedBonus = storage;
    }

    public void setCounter(int count) {
        duration = count;
    }

    public void startTurn() {
        super.startTurn();
        duration--;
        if (duration < 1) {
            die(false, this);
            Unit wyvern = new UnitWyvern(getCastle());
            //wyvern.setID(getID());
            wyvern.setLocation(getLocation());
            wyvern.setBattleField(getCastle().getBattleField());
            getCastle().getBattleField().add(wyvern);
            getCastle().addOut(getTeam(), wyvern);
            wyvern.grow(storedBonus);

            // Generate a nifty effect
            getCastle().getObserver().unitEffect(
                    this, Action.EFFECT_FADE);
            wyvern.setHidden(true);
            getCastle().getObserver().unitEffect(
                    wyvern, Action.EFFECT_FADE_IN);
            wyvern.refresh();
            getCastle().getObserver().playSound(Constants.SOUND_SQUISH);

        } else {
            durationDescription.setDescription(hatch + duration + Strings.UNIT_EGG_4);
        }
    }

    public void refresh() {
        super.refresh();

    }

    //public void setStorage( short storage ){ storedBonus = new Vector<Short>(); storedBonus.add(storage); }
}
