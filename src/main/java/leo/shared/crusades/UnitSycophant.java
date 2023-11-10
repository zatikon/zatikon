///////////////////////////////////////////////////////////////////////
// Name: UnitSycophant
// Desc: A sycophant
// Date: 6/7/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitSycophant extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit master;
    private boolean newMaster = false;
    private final ActionTrait indestructible;
    private final ActionTrait hubris;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSycophant(Castle newCastle) {
        castle = newCastle;

        // access kevek
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.SYCOPHANT;
        category = Unit.COMMANDERS;
        name = Strings.UNIT_SYCOPHANT_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 2;
        lifeMax = 2;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = null;
        deployCost = 3;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_SYCOPHANT;

        // Indestructible
        indestructible = new ActionTrait(this, Strings.UNIT_SEAL_2, Strings.UNIT_SEAL_2, Strings.UNIT_SYCOPHANT_2);
        indestructible.setDetail(Strings.UNIT_SYCOPHANT_3);

        // Hubris
        hubris = new ActionTrait(this, Strings.UNIT_SYCOPHANT_4, Strings.UNIT_SYCOPHANT_5, "");
        hubris.setDetail(Strings.UNIT_SYCOPHANT_6);

        // Add the ability description
        ActionTrait codependent = new ActionTrait(this, Strings.UNIT_SYCOPHANT_7, Strings.UNIT_SYCOPHANT_8, "");
        codependent.setDetail(Strings.UNIT_SYCOPHANT_9);
        add(codependent);

        EventServile es = new EventServile(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
        actions.add(new ActionFlatter(this));
    }


    /////////////////////////////////////////////////////////////////
    // If it leaves play, the bonded dies
    /////////////////////////////////////////////////////////////////
    public void setMaster(Unit newUnit, boolean first) {
        master = newUnit;
        newMaster = true;
        master.getActions().add(0, hubris);
        if (first) {
            getActions().add(0, hubris);
            getActions().add(0, indestructible);
        }
    }

    public void setMaster(Unit newUnit) {
        master = newUnit;
        newMaster = true;
        master.getActions().add(0, hubris);
        getActions().add(0, hubris);
        getActions().add(0, indestructible);
    }

    public Unit getMaster() {
        if (master == null || master.isDead())
            return null;
        else
            return master;
    }

    public Unit getLink() {
        return getMaster();
    }


    /////////////////////////////////////////////////////////////////
    // Start over
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        super.startTurn();
        newMaster = false;
        remove(indestructible);
    }


    /////////////////////////////////////////////////////////////////
    // Immunities
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        if (newMaster && looker.getCastle() != getCastle()) return false;
        return super.targetable(looker);
    }


    /////////////////////////////////////////////////////////////////
    // If it leaves play, the bonded dies
    /////////////////////////////////////////////////////////////////
    public void removed() {
        if (getLink() != null) {
            getLink().die(true, this);
            if (master.isDead())
                getCastle().getObserver().death(master);
        }
    }


}
