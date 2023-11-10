///////////////////////////////////////////////////////////////////////
// Name: UnitTemplar
// Desc: A templar
// Date: 7/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitTemplar extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean indestructible = false;
    private Action movementTmp;
    private ActionTrait trait = null;
    private final Action heal;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitTemplar(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = UnitType.TEMPLAR;
        category = Unit.CLERGY;
        name = Strings.UNIT_TEMPLAR_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        movementTmp = move;
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_TEMPLAR;

        // add the succor event
        EventSuccor es = new EventSuccor(this);
        add((Event) es);
        add((Action) es);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        // Add the invincible power
        actions.add(new ActionInvincible(this));
        heal = new ActionHeal(this, (byte) 0, (byte) 1, TargetType.UNIT_AREA, (byte) 2);
        actions.add(heal);

        // Create the indestructible trait
        trait = new ActionTrait(this, Strings.UNIT_TEMPLAR_2, Strings.UNIT_TEMPLAR_3, Strings.UNIT_TEMPLAR_4);
        trait.setDetail(Strings.UNIT_TEMPLAR_5);
        trait.setType(Action.SPELL);
    }


    /////////////////////////////////////////////////////////////////
    // Die, nothing happens
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death && indestructible) return;
        super.die(death, source);
    }


    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        if (!indestructible) {
            return super.damage(source, amount);
        } else {
            getCastle().getObserver().unitDamaged(source, this, (byte) 0);
            return (byte) 0;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the organic trait
    /////////////////////////////////////////////////////////////////
    public boolean getOrganic() {
        return !indestructible;
    }


    /////////////////////////////////////////////////////////////////
    // How much energy?
    /////////////////////////////////////////////////////////////////
    public boolean getIndestructible() {
        return indestructible;
    }


    /////////////////////////////////////////////////////////////////
    // Set energy
    /////////////////////////////////////////////////////////////////
    public void toggleIndestructible() {
        if (indestructible) {
            move = movementTmp;
            getActions().add(0, move);
            remove(trait);
            indestructible = false;
            appearance = Constants.IMG_TEMPLAR;
            stun();
        } else {
            remove(move);
            getActions().add(0, trait);
            movementTmp = move;
            move = null;
            appearance = Constants.IMG_TEMPLAR_AURA;
            indestructible = true;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Targetable no more
    /////////////////////////////////////////////////////////////////
    public boolean targetable(Unit looker) {
        if (indestructible) return false;
        else
            return super.targetable(looker);
    }


    /////////////////////////////////////////////////////////////////
    // Get the heal action
    /////////////////////////////////////////////////////////////////
    public Action getHeal() {
        return heal;
    }

}
