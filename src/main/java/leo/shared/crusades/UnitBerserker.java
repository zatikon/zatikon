///////////////////////////////////////////////////////////////////////
// Name: UnitBerserker
// Desc: A berserker
// Date: 10/11/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitBerserker extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private ActionTrait trait = null;
    private boolean dying = false;
    private short oldLifeMax = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitBerserker(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.BERSERKER.value();
        category = Unit.NATURE;
        name = Strings.UNIT_BERSERKER_1;
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
        castleCost = 100;
        organic = true;
        appearance = Constants.IMG_BERSERKER;
        canWin = true;

        // Add the ability description
        trait = new ActionTrait(this, Strings.UNIT_BERSERKER_2, Strings.UNIT_BERSERKER_3, "", "");
        trait.setDetail(Strings.UNIT_BERSERKER_4);
        actions.add(trait);

        // Add the actions
        actions.add(move);
        actions.add(attack);

    }


    /////////////////////////////////////////////////////////////////
    // Damage. Show 0 if he's already doomed
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short amount) {
        if (dying) {
            return super.damage(source, (byte) 0);
        } else {
            return super.damage(source, amount);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Die... sort of
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death) {
            if (!dying) {
                dying = true;
                canWin = false;
                life = 0;
                oldLifeMax = lifeMax;
                lifeMax = 0;
                appearance = Constants.IMG_DEAD_BERSERKER;
                trait.setDescription(Strings.UNIT_BERSERKER_5);
                trait.setCostDescription(Strings.UNIT_BERSERKER_6);
            }
        } else
            super.die(death, source);
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (dying) {
            getCastle().getObserver().abilityUsed(getLocation(), getLocation(), Constants.IMG_DEATH);
            getCastle().getObserver().death(this);
            super.die(true, this);
        }
    }

    public boolean isDying() {
        return dying;
    }

    public void stopDying() {
        dying = false;
        canWin = true;
        lifeMax = oldLifeMax;
        life = lifeMax;
        appearance = Constants.IMG_BERSERKER;
        trait.setDescription(Strings.UNIT_BERSERKER_3);
        trait.setCostDescription("");
    }
}
