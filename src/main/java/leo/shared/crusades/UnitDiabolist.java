///////////////////////////////////////////////////////////////////////
// Name: UnitDiabolist
// Desc: A diabolist
// Date: 9/11/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDiabolist extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int souls = 1;
    private boolean devil = false;
    private ActionDetonate detonate;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDiabolist(Castle newCastle) {
        nonPactInit(newCastle);
        actions.add(new ActionDemonicPact(this));
    }

    /////////////////////////////////////////////////////////////////
    // Constructor for dealing with hidden unit stuff
    /////////////////////////////////////////////////////////////////
    public UnitDiabolist(Castle newCastle, boolean addpact) {
        nonPactInit(newCastle);
        if (addpact)
            actions.add(new ActionDemonicPact(this));
    }

    private void nonPactInit(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.DIABOLIST;
        category = Unit.BLACK_MAGIC_USERS;
        name = Strings.UNIT_DIABOLIST_1;
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
        appearance = Constants.IMG_DIABOLIST;


        // Reaper of souls
        EventReaper er = new EventReaper(this);
        //add((Event) er);
        add((Action) er);

        // Add the actions
        actions.add(move);

        // The fun stuff
        detonate = new ActionDetonate(this, (byte) 3, (byte) 4);
        actions.add(detonate);
        //actions.add(new ActionGhost(this));
        actions.add(new ActionSacrifice(this));
    }


    /////////////////////////////////////////////////////////////////
    // How much energy?
    /////////////////////////////////////////////////////////////////
    public int getSouls() {
        return souls;
    }


    /////////////////////////////////////////////////////////////////
    // Set energy
    /////////////////////////////////////////////////////////////////
    public void setSouls(int newSouls) {
        souls = newSouls;
    }

    /////////////////////////////////////////////////////////////////
    // Is it a devil yet?
    /////////////////////////////////////////////////////////////////
    public boolean isDevil() {
        return devil;
    }

    /////////////////////////////////////////////////////////////////
    // make it one
    /////////////////////////////////////////////////////////////////
    public void setDevil(boolean d) {
        devil = d;
    }

    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (devil) {
            souls--;
            if (souls <= 0) {
                super.die(true, this);
                if (isDead())
                    getCastle().getObserver().death(this);
            }
        }
    }


}
