///////////////////////////////////////////////////////////////////////
//	Name:	ActionRush
//	Desc:	Kill it
//	Date:	11/29/2007 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionRush implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit owner;
    private final short targetType;
    private final short range;
    private final short cost;
    private final String detail = Strings.ACTION_RUSH_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionRush(
            Unit newOwner,
            short newRange) {
        owner = newOwner;
        range = newRange;
        cost = 1;
        targetType = TargetType.UNIT_LINE;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        short outcome = Event.CANCEL;

        // The costs
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        // Get the targets
        Unit victim = owner.getBattleField().getUnitAt(target);
        if (victim == null) return Strings.INVALID_ACTION;

        short newLocation = owner.getLocation();

        if (BattleField.getDistance(owner.getLocation(), victim.getLocation()) > 1) {
            // Move to the closest location
            newLocation = getClosestLocation(victim);
            short oldLocation = owner.getLocation();
            if (owner.getCastle().getBattleField().move(owner, oldLocation, newLocation) == Event.CANCEL)
                return "";
        }

        short damage = 0;

        if (!owner.isDead() && owner.getLocation() == newLocation) {
            // get permission to strike
            outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
            damage = outcome;

            if (outcome == Event.END) {
                return "";
            }

            // if its ok, hit it
            if (outcome == Event.OK) {
                // Hurt it
                damage = victim.damage(owner, owner.getDamage());
            }

            owner.getCastle().getObserver().
                    attack(owner, victim, damage,
                            Action.ATTACK_MELEE);

            // stop here if it's done
            if (outcome != Event.CANCEL) {
                // send the witness event
                owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
            }
        }


        if (!owner.isDead()) {
            // Check for victory
            Castle castle = owner.getBattleField().getCastle1();
            if (owner.getCastle() == castle)
                castle = owner.getBattleField().getCastle2();
            if (owner.getLocation() == castle.getLocation()) {
                castle.getObserver().endGame(owner.getCastle());
                return owner.getName() + Strings.ACTION_RUSH_2;
            }
        }


        if (outcome == Event.CANCEL) {
            return owner.getName() + Strings.ACTION_RUSH_3 + getName();
        }

        return "" + owner.getName() + Strings.ACTION_RUSH_4 + victim.getName() + Strings.ACTION_RUSH_5 + damage;

    }


    /////////////////////////////////////////////////////////////////
    // Get the closest location
    /////////////////////////////////////////////////////////////////
    private short getClosestLocation(Unit victim) {
        short target = victim.getLocation();
        short slopeX = 0;
        short slopeY = 0;
        short targetX = BattleField.getX(target);
        short targetY = BattleField.getY(target);
        short myX = BattleField.getX(owner.getLocation());
        short myY = BattleField.getY(owner.getLocation());

        if (targetX > myX) slopeX = 1;
        if (targetX < myX) slopeX = -1;
        if (targetY > myY) slopeY = 1;
        if (targetY < myY) slopeY = -1;

        while (BattleField.getLocation((byte) (myX + slopeX), (byte) (myY + slopeY)) != target) {
            myX += slopeX;
            myY += slopeY;
        }
        return BattleField.getLocation(myX, myY);

		/*
		short byteLoc = owner.getLocation();
		Iterator<Short> it = targets.iterator();
		while(it.hasNext())
		{	Short loc = it.next();
			if (BattleField.getDistance(victim.getLocation(), loc.byteValue()) <= 1)
				byteLoc = loc.byteValue();
		}
		return byteLoc;*/
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
        System.out.println(Strings.INVALID_ACTION);
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        return owner.getBattleField().getTargets(owner, getTargetType(), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return owner.getBattleField().getTargets(owner, (byte) (getTargetType() - 6), getRange(), false, true, true, TargetType.ENEMY, owner.getCastle());
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_RUSH_6;
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
        return Strings.ACTION_RUSH_7;
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
            return getRemaining();
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
        return Strings.ACTION_RUSH_8;
    }

    public short getMax() {
        return (byte) 0;
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
        return Action.ATTACK;
    }

    public String getDetail() {
        return detail;
    }
}
