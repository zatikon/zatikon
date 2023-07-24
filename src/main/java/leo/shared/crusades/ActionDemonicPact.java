///////////////////////////////////////////////////////////////////////
// Name: ActionDemonicPact
// Desc: Transform into devil
// Date: 8/16/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionDemonicPact implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitDiabolist owner;
    private final short targetType;
    private final short cost;
    private final String detail = Strings.ACTION_DEMONIC_PACT_1;
    private final short hp;
    private final short actions;
    private final short armor;
    private final short damage;
    private final Unit hiddenUnit;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionDemonicPact(UnitDiabolist newOwner) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 1;
        hp = 4;
        actions = 3;
        armor = 1;
        damage = 5;
        hiddenUnit = new UnitDiabolist(owner.getCastle(), false);
        hiddenUnit.setAppearance(Constants.IMG_DEVIL);
        hiddenUnit.setArmor((byte) (hiddenUnit.getBaseArmor() + armor));
        hiddenUnit.setLifeMax((byte) (hiddenUnit.getLifeMax() + hp));
        hiddenUnit.setLife((byte) (hiddenUnit.getLife() + hp));
        hiddenUnit.setActionsMax((byte) (hiddenUnit.getActionsMax() + actions));
        hiddenUnit.setActions((byte) (hiddenUnit.getActionsLeft() + actions));
        hiddenUnit.setDamage(damage);
        ActionTrait hunger = new ActionTrait(owner, Strings.ACTION_DEMONIC_PACT_2, Strings.ACTION_DEMONIC_PACT_3, Strings.ACTION_DEMONIC_PACT_4);
        hunger.setDetail(Strings.ACTION_DEMONIC_PACT_5);
        hiddenUnit.add(hunger);
        ActionAttack attack = new ActionAttack(hiddenUnit, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        hiddenUnit.add(attack);
        Vector<Action> actions = hiddenUnit.getActions();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.elementAt(i) instanceof ActionDetonate) {
                actions.remove(actions.elementAt(i));
                break;
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        // one time thing, remove it
        owner.remove(this);

        // change picture
        owner.setAppearance(Constants.IMG_DEVIL);

        // Adjust the new armor
        owner.setArmor((byte) (owner.getBaseArmor() + armor));

        // Adjust the new damage
        owner.setDamage((byte) (owner.getDamage() + damage));

        // Adjust the new hp
        owner.setLifeMax((byte) (owner.getLifeMax() + hp));
        owner.setLife((byte) (owner.getLife() + hp));

        // Adjust the new actions
        owner.setActionsMax((byte) (owner.getActionsMax() + actions));
        owner.setActions((byte) (owner.getActionsLeft() + actions));

        // set devil boolean so that it knows to lose souls every turn
        owner.setDevil(true);

        // pay the price
        owner.getCastle().deductCommands(this, (byte) 1);
        owner.deductActions(cost);

        // This is where it describes the souls thing
        ActionTrait hunger = new ActionTrait(owner, Strings.ACTION_DEMONIC_PACT_2, Strings.ACTION_DEMONIC_PACT_3, Strings.ACTION_DEMONIC_PACT_4);
        hunger.setDetail(Strings.ACTION_DEMONIC_PACT_5);
        owner.add(hunger);

        ActionAttack attack = new ActionAttack(owner, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        owner.add(attack);

        Vector<Action> actions = owner.getActions();
        for (int i = 0; i < actions.size(); i++) {
            if (actions.elementAt(i) instanceof ActionDetonate) {
                actions.remove(actions.elementAt(i));
                break;
            }
        }

        owner.getBattleField().event(Event.WITNESS_ACTION, owner, owner, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_DEMONIC_PACT_6;
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
        return Strings.ACTION_DEMONIC_PACT_7;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return Strings.ACTION_DEMONIC_PACT_8;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return "Costs 1 " + Strings.ACTION;
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
        return Strings.ACTION_DEMONIC_PACT_9;
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
        return hiddenUnit;
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
