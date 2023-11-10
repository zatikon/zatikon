///////////////////////////////////////////////////////////////////////
// Name: UnitDoppelganger
// Desc: A doppelganger
// Date: 10/25/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitDoppelganger extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ActionTrait trait;
    private final Action actionTwin;
    private UnitDoppelganger twin = null;
    private short oldId = UnitType.DOPPELGANGER.value();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitDoppelganger(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.DOPPELGANGER.value();
        category = Unit.SHAPESHIFTERS;
        name = Strings.UNIT_DOPPELGANGER_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 1;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_DOUBLE_DOP;

        trait = new ActionTrait(this, Strings.UNIT_DOPPELGANGER_2, Strings.UNIT_DOPPELGANGER_3, "");
        trait.setDetail(Strings.UNIT_DOPPELGANGER_4);

        Event pass = new EventPass(this);
        add(pass);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        actionTwin = new ActionTwin(this);
        actions.add(actionTwin);

    }


    /////////////////////////////////////////////////////////////////
    // Get/Set Twin
    /////////////////////////////////////////////////////////////////
    public Unit getTwin() {
        return twin;
    }


    public void setTwin(UnitDoppelganger newTwin) {
        oldId = id;
        if (!isSkinwalking()) id = UnitType.NONE.value();
        twin = newTwin;
        actions.add(0, trait);
        appearance = Constants.IMG_DOPPELGANGER;
    }

    public Unit getLink() {
        return getTwin();
    }


    /////////////////////////////////////////////////////////////////
    // Something has died, event
    /////////////////////////////////////////////////////////////////
    public void twinDeath(Unit corpse) {
        if (corpse == twin) {
            twin = null;
            setID(oldId);
            UnitDoppelganger tmp = new UnitDoppelganger(getCastle());
            tmp.setLocation(corpse.getLocation());
            getCastle().getObserver().attack(this, tmp, getLocation(), Action.ATTACK_SPIRIT);
            actions.remove(trait);
            setAppearance(Constants.IMG_DOUBLE_DOP);

            if (corpse.isSkinwalking()) {
                die(false, this);
                getCastle().getObserver().unitEffect(this, Action.EFFECT_FADE);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        if (id == UnitType.NONE.value()) die(false, this);
    }


    /////////////////////////////////////////////////////////////////
    // force a false death
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (id == UnitType.NONE.value()) {
            super.die(false, source);
        } else {
            super.die(death, source);
        }
    }

    public Action getActionTwin() {
        return actionTwin;
    }
}
