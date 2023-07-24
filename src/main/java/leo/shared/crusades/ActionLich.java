///////////////////////////////////////////////////////////////////////
// Name: ActionLich
// Desc: Transform into a lich
// Date: 8/1/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class ActionLich implements Action {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final UnitNecromancer owner;
    private final short targetType;
    private final short cost;
    private final Unit hiddenUnit;
    private final String detail = "";
    //needed for lichbomb
    private final short type;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionLich(UnitNecromancer newOwner) {
        owner = newOwner;
        targetType = TargetType.NONE;
        cost = 1;
        hiddenUnit = new UnitLich(owner.getCastle());
        // needed for lichbomb
        type = (byte) 3; // test?
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public String perform(short target) {
        if (!validate(target)) return Strings.INVALID_ACTION;

        // Get the old ID
        short oldID = owner.getID();

        //
        // Put the miniondeath, attack loop, and lifegain counter here
        short damage = 5; // sets the power of the Lich Bomb
        short totalDamage = 0; // Sets up a variable to store the total damage done by the Lich Bomb (used for lifegain)
        Vector<Unit> manager = owner.getSummonManager().getSummonList(); // Grabs the summon manager so that ActionLich has access to the necromancer's summons.
        Vector<Short> targets; //Prepare a place to store the targets of the AOE.
        BattleField field = owner.getBattleField(); // store a reference to the battlefield (to make reading the while loop's code easier)
        Vector<Unit> aoeVictims; // Stores information for the AOE animation
        Vector<Short> damages; // More info for the AoE

        while (!manager.isEmpty()) // For each unit in the summoning manager...
        {
            aoeVictims = new Vector<Unit>();        // Resets information for Aoe animation
            damages = new Vector<Short>();           // Resets information for Aoe animation
            Unit summon = manager.firstElement();   // Pop a unit off of the summon stack (part one)
            manager.remove(summon);                 // Pop a unit off of the summon stack (part two)
            short aoeTarget = summon.getLocation();  // Get the origin location of the AOE attack
            summon.die(false, summon);               // Kill the summon so the lich doesn't gain life from damage dealt to it
            targets = field.getArea(owner, aoeTarget, (byte) 1, false, true, true, true, TargetType.BOTH, owner.getCastle());
            // Bomb code stolen from ActionFireball
            // Hit the targets
            for (int i = 0; i < targets.size(); i++) {
                short aoeLoc = targets.elementAt(i);
                Unit victim = field.getUnitAt(aoeLoc);
                if (victim != null) {
                    // make sure it doesn't damage its own summons to gain life from it
                    if (!manager.contains(victim)) {
                        // get permission
                        short result = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                        if (result == Event.OK) {
                            // strike
                            result = victim.damage(owner, damage);
                            owner.getCastle().getObserver().attack(victim, victim, result, Action.ATTACK_NONE);
                            // store the damage dealt into the total (used for lifegain)
                            if (result > victim.getLifeMax()) result = victim.getLifeMax();
                            totalDamage += result;
                            // play a little animation to let the player known that units are getting fragged.
                            damages.add(result);// stores damage result to use for the observer
                            aoeVictims.add(victim); // stores damage result to use for the observer
                            // send the witness event
                            owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, Action.ATTACK, Event.NONE, Event.OK);
                        }
                        //victims.add(victim); // build up the lists // May still be needed for the
                    }
                }
            }
            // Attack animation
            owner.getCastle().getObserver().fireball(owner.getLocation(), summon.getLocation(), Constants.IMG_EXPLOSION_1, aoeVictims, damages, type);
        }
        // end lichbomb

//Transform into a lich
        owner.die(false, owner);

        // Put a lich in it's place
        Unit lich = new UnitLich(owner.getCastle());
        lich.setID(oldID);
        lich.setLocation(owner.getLocation());
        lich.setBattleField(owner.getCastle().getBattleField());
        owner.getCastle().getBattleField().add(lich);
        owner.getCastle().addOut(owner.getTeam(), lich);
        lich.grow(owner.getBonus());

        owner.getCastle().getObserver().unitEffect(owner, Action.EFFECT_FADE);
        lich.setHidden(true);
        owner.getCastle().getObserver().unitEffect(lich, Action.EFFECT_FADE_IN);

        owner.getCastle().getObserver().playSound(Constants.SOUND_SHAPESHIFT);

        // Generate a nifty effect
        //owner.getCastle().getObserver().abilityUsed(owner.getLocation(), owner.getLocation(), Constants.IMG_WHEEL);

        // GIVE HIM HIS LIFE
        lich.setLifeMax((byte) (lich.getLifeMax() + totalDamage));
        lich.setLife((byte) (lich.getLife() + totalDamage));


        // The cost
        owner.remove(this);
        owner.deductActions(cost);
        owner.getCastle().deductCommands(this, (byte) 1);

        owner.getBattleField().event(Event.WITNESS_ACTION, lich, lich, getType(), Event.NONE, Event.OK);

        return "" + owner.getName() + Strings.ACTION_LICH_1;

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
        //return owner.getCastle().getBattleField().getTargets(owner, TargetType.LOCATION_AREA, (byte) 1, false, false, false);
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
        return Strings.ACTION_LICH_2;
    }


    /////////////////////////////////////////////////////////////////
    // Get the description
    /////////////////////////////////////////////////////////////////
    public String getRangeDescription() {
        return "The lich detonates all minions.";
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost description
    /////////////////////////////////////////////////////////////////
    public String getCostDescription() {
        return Strings.ACTION_LICH_3;
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
        return Strings.ACTION_LICH_4;
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
