///////////////////////////////////////////////////////////////////////
// Name: ActionBrew
// Desc: Brew a potion for the target
// Date: 3/23/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;
import org.tinylog.Logger;

import java.util.Iterator;
import java.util.Vector;


public class ActionBrew implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private short remaining;
    private final short cost;
    private final int potionType;
    private String detail;
    private String name;
    private String description;
    private final String detailHeal = Strings.ACTION_BREW_1;
    private final String detailStrength = Strings.ACTION_BREW_2;
    private final String detailLove = Strings.ACTION_BREW_3;

    private final String detailEscape = Strings.ACTION_BREW_4;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionBrew(Unit newOwner, int newType) {
        owner = newOwner;
        potionType = newType;
        targetType = TargetType.UNIT_LINE;
        range = 1;
        cost = 1;

        switch (potionType) {
            case Potion.HEAL:
                name = Strings.ACTION_BREW_5;
                detail = detailHeal;
                description = Strings.ACTION_BREW_6;
                break;

            case Potion.STRENGTH:
                name = Strings.ACTION_BREW_7;
                detail = detailStrength;
                description = Strings.ACTION_BREW_8;
                break;

            case Potion.LOVE:
                name = Strings.ACTION_BREW_9;
                detail = detailLove;
                description = Strings.ACTION_BREW_10;

                break;

            case Potion.ESCAPE:
                name = Strings.ACTION_BREW_11;
                detail = detailEscape;
                description = Strings.ACTION_BREW_12;
                break;
        }

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Some temporary kill it code
        Unit victim = owner.getBattleField().getUnitAt(target);

        // remove the old
        removePotion(victim);

        // in with the new
        switch (potionType) {
            case Potion.HEAL:
                EventHealPotion ep = new EventHealPotion(victim);
                victim.add((Event) ep);
                victim.add((Action) ep);
                break;

            case Potion.STRENGTH:
                EventStrength es = new EventStrength(victim);
                victim.getActions().add(0, es);
                victim.add((Event) es);
                break;

            case Potion.LOVE:
                victim.add(new ActionLovePotion(victim));
                owner.remove(this);
                break;

            case Potion.ESCAPE:
                victim.add(new ActionEscapePotion(victim));
                break;
        }

        // Generate a nifty effect
        owner.getCastle().getObserver().abilityUsed(owner.getLocation(), target, Constants.IMG_POOF);

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_BREW_13 + victim.getName();

    }


    /////////////////////////////////////////////////////////////////
    // Remove a potion
    /////////////////////////////////////////////////////////////////
    private void removePotion(Unit victim) {
        // loopy
        Vector<Action> actions = victim.getActions();
        Iterator<Action> it = actions.iterator();
        while (it.hasNext()) {
            Action act = it.next();
            if (act instanceof Potion) {
                victim.remove(act);
                return;
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        // Any actions left
        if (getRemaining() <= 0) return false;

        // Deployed yet?
        if (!owner.deployed()) return false;

        // If no target, all is well
        if (getTargetType() == TargetType.NONE) return true;

        // Search for the target
        Vector<Short> targets = getTargets();
        for (int i = 0; i < targets.size(); i++) {
            Short location = targets.elementAt(i);
            if (location.byteValue() == target) return true;
        }

        // Bad bad monkey
        Logger.error(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        Vector<Short> targets = owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, false, TargetType.FRIENDLY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        Vector<Short> targets = owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, false, TargetType.FRIENDLY, owner.getCastle());
        refineTargets(targets);
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Refine targets
    /////////////////////////////////////////////////////////////////
    private void refineTargets(Vector<Short> targets) {
        if (potionType == Potion.STRENGTH) {
            Vector<Short> removes = new Vector<Short>();
            Iterator it = targets.iterator();
            while (it.hasNext()) {
                Short loc = (Short) it.next();
                if (owner.getCastle().getBattleField().getUnitAt(loc) != null && owner.getCastle().getBattleField().getUnitAt(loc).getBaseDamage() <= 0)
                    removes.add(loc);
            }
            it = removes.iterator();
            while (it.hasNext())
                targets.remove((Short) it.next());
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return description;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return getRange() + Strings.RANGE;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        if (potionType == Potion.LOVE) {
            return Strings.ACTION_BREW_14;
        } else {
            return "1" + Strings.ACTION;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get remaining actions
    /////////////////////////////////////////////////////////////////
    public short getRemaining() {
        if (owner.noCost()) return 1;

        if ((owner.getCastle().getCommandsLeft() <= 0 && !owner.freelyActs()) || !owner.deployed()) return 0;

        if (getMax() <= 0)
            return (byte) (owner.getActionsLeft() / getCost());
        else
            return remaining;
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
        return name;
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return cost;
    }

    public short getRange() {
        return range;
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
        return Action.SPELL;
    }

    public String getDetail() {
        return detail;
    }
}
