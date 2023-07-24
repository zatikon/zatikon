///////////////////////////////////////////////////////////////////////
// Name: ActionWeaponSwitch
// Desc: Switch between melee/range
// Date: 8/22/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionWeaponSwitch implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short cost;
    private final Action attackRange;
    private final Action attackMelee;
    private boolean rangedMode = true;
    private final short powerDifference;
    private final String detail = Strings.ACTION_WEAPON_SWITCH_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionWeaponSwitch(Unit newOwner, Action oldAttack) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 1;
        powerDifference = 2;
        attackRange = oldAttack;
        attackMelee = new ActionAttack(owner, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Switch to melee or range
        if (rangedMode) {
            owner.getCastle().getObserver().playSound(Constants.SOUND_UNSHEATH);

            if (owner.getAppearance() == Constants.IMG_RANGER_WOLF_RANGED) {
                owner.setAppearance(Constants.IMG_RANGER_WOLF_MELEE);
            } else {
                owner.setAppearance(Constants.IMG_RANGER_MELEE);
            }

            // Remove this
            owner.remove(attackRange);

            // Add the new attack
            owner.add(attackMelee);
            owner.setAttack(attackMelee);

            // Adjust the new power
            owner.setDamage((byte) (owner.getBaseDamage() + powerDifference));

            // Done
            rangedMode = false;

        } else {
            owner.getCastle().getObserver().playSound(Constants.SOUND_SHEATH);

            if (owner.getAppearance() == Constants.IMG_RANGER_WOLF_MELEE) {
                owner.setAppearance(Constants.IMG_RANGER_WOLF_RANGED);
            } else {
                owner.setAppearance(Constants.IMG_RANGER_RANGED);
            }

            // Remove this
            owner.remove(attackMelee);

            // Add the new attack
            owner.add(attackRange);
            owner.setAttack(attackRange);

            // Adjust the new power
            owner.setDamage((byte) (owner.getBaseDamage() - powerDifference));

            // Done
            rangedMode = true;
        }

        owner.getCastle().deductCommands(this, (byte) 1);
        owner.deductActions(cost);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, owner, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_WEAPON_SWITCH_2 + (rangedMode ? Strings.ACTION_WEAPON_SWITCH_3 : Strings.ACTION_WEAPON_SWITCH_4);
    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        return owner.deployed();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return new Vector<Short>();
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getTargets();
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_WEAPON_SWITCH_5 + (rangedMode ? Strings.ACTION_WEAPON_SWITCH_6 : Strings.ACTION_WEAPON_SWITCH_7);
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return "Costs 1" + Strings.ACTION;
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        return (byte) (owner.getActionsLeft() / getCost());
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_WEAPON_SWITCH_8;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return cost;
    }

    public short getRange() {
        return 0;
    }

    public short getTargetType() {
        return targetType;
    }

    public Unit getOwner() {
        return owner;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public boolean passive() {
        return false;
    }

    public short getType() {
        return Action.SKILL;
    }

    public String getDetail() {
        return detail;
    }
}
