///////////////////////////////////////////////////////////////////////
// Name: EventWyvernMove
// Desc: Devour the unit moved on top of
// Date: 7/8/2011
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class EventWyvernMove implements Event, Action {
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final short eventType = WITNESS_MOVE;
    private final short type = Action.ATTACK;
    private final UnitWyvern owner;
    private final int priority = 90;
    private final String detail = Strings.ACTION_BROODMOTHER_3;
    private short eggPos = 0;
    private int eggCounter = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EventWyvernMove(UnitWyvern newOwner) {
        owner = newOwner;
    }

    /////////////////////////////////////////////////////////////////
    // Perform the event
    /////////////////////////////////////////////////////////////////
    public short perform(Unit source, Unit target, short val1, short val2, short recursive) {
        if (source != owner) {
            return Event.OK;
        }
        if (owner.isDead()) return Event.OK;

        if (owner.fed()) {
            layChild();
            owner.feed(false);
        }

        eggPos = owner.getLocation();

        if (owner.getMeal() == null)
            return Event.OK;

        //if the owner was fed before moving, pop an egg.


        Unit victim = owner.getMeal();
        // get the outcome
        if (victim != null && !victim.isPowerUp()) { // If you're moving onto an occupied space...
            // See if they're immune to the death effect
            victim.setErased(true);
            short outcome = owner.getBattleField().event(Event.PREVIEW_ACTION, owner, victim, Action.SKILL, Event.NONE, Event.OK);
            if (outcome == Event.CANCEL || victim instanceof UnitBerserker || victim instanceof UnitKnight || victim instanceof UnitPossessed) {
                // If so, banish the target and do not feet the Wyvern
                victim.getCastle().add(Unit.getUnit(victim.getID(), victim.getCastle())); // Add to the castle
                victim.die(false, owner); // Out of the field...
                if (victim.getSoulmate() != null) {
                    victim.getSoulmate().die(false, owner);
                    if (victim.getSoulmate().isDead())
                        owner.getCastle().getObserver().death(victim.getSoulmate()); // if they're dead, show it
                }
                if (victim.getLocation() != victim.getCastle().getLocation()) {
                    owner.getCastle().getObserver().unitEffect(victim, Action.EFFECT_BANISH); // Generate a nifty effect
                }
                owner.getCastle().getObserver().abilityUsed(owner.getLocation(), victim.getLocation(), Constants.IMG_POOF);
                owner.getCastle().getObserver().castleAddition(victim); // Warn the client about an addition
                owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK);

            } else {// If not, kill the unit and feed the wyvern
                victim.die(true, owner); // Murder. Death.
                if (victim.getSoulmate() != null) {
                    victim.getSoulmate().die(false, owner);
                    if (victim.getSoulmate().isDead())
                        owner.getCastle().getObserver().death(victim.getSoulmate()); // if they're dead, show it
                }
                owner.getCastle().getObserver().abilityUsed(owner.getLocation(), victim.getLocation(), Constants.IMG_DEATH);// Generate a nifty effect
                if (victim.isDead()) owner.getCastle().getObserver().death(victim); // if they're dead, show it
                owner.getBattleField().event(Event.WITNESS_ACTION, owner, victim, getType(), Event.NONE, Event.OK); // send the witness event
                owner.feed(true); // feed the wyvern
                eggCounter = 3;
            }
        } else if (victim != null && victim.isPowerUp()) {
            victim.die(false, victim);
            victim.setTeam(owner.getTeam());
            victim.setCastle(owner.getCastle());

            // grow if it's targetable by the powerup
            if (victim.canTarget(owner)) {
                owner.grow(victim.getPower());
                // generate powerup image
                owner.getCastle().getObserver().imageDraw(victim, victim.getLocation(), victim.getAppearance(), 6);
            }
            // otherwise banish it
            else {
                owner.getCastle().add(victim);
                // generate banish effect
                owner.getCastle().getObserver().abilityUsed(victim.getLocation(), victim.getLocation(), Constants.IMG_POOF);
                owner.getCastle().getObserver().castleAddition(victim);
            }
        }

        return Event.OK;
    }

    /////////////////////////////////////////////////////////////////
    // Have a baby
    /////////////////////////////////////////////////////////////////
    public void layChild() {
        //Vector<Short> bonusPackage = (Vector<Short>)owner.getBonus().clone();

        if (eggCounter > 0) {
            UnitEgg egg = new UnitEgg(owner.getCastle());
            egg.setLocation(eggPos);
            //egg.setLocation(target);
            egg.unDeploy();
            egg.setCounter(eggCounter);
            egg.setBattleField(owner.getCastle().getBattleField());
            //egg.setStorage(bonusPackage);
            //if(owner.getBonus().contains(Action.GROW_RELIC_CLOCKWORK)){egg.grow(Action.GROW_RELIC_CLOCKWORK);} // The egg is clockwork if the parent is
            owner.getCastle().getBattleField().add(egg);
            owner.getCastle().addOut(owner.getTeam(), egg);
        } else {
            Unit wyvern = new UnitWyvern(owner.getCastle());
            wyvern.setID(owner.getID());
            wyvern.setLocation(owner.getLocation());
            wyvern.setBattleField(owner.getCastle().getBattleField());
            owner.getCastle().getBattleField().add(wyvern);
            owner.getCastle().addOut(owner.getTeam(), wyvern);
            //wyvern.grow(bonusPackage);
            wyvern.refresh();
        }
    }

    public void setEggPos(short pos) {
        eggPos = pos;
    }

    /////////////////////////////////////////////////////////////////
    // Validate
    /////////////////////////////////////////////////////////////////
    public boolean validate(short target) {
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getClientTargets() {
        return getTargets();
    }

    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        Vector<Short> targets = new Vector<Short>();
        return targets;
    }

    /////////////////////////////////////////////////////////////////
    // Refresh the event
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        eggPos = owner.getLocation();
        owner.setMeal(null);
    }


    /////////////////////////////////////////////////////////////////
    // Start turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        if (eggCounter > 0)
            --eggCounter;
        eggPos = owner.getLocation();
    }


    /////////////////////////////////////////////////////////////////
    // Get the event type
    /////////////////////////////////////////////////////////////////
    public short getEventType() {
        return eventType;
    }


    /////////////////////////////////////////////////////////////////
    // Get priority
    /////////////////////////////////////////////////////////////////
    public int getPriority() {
        return priority;
    }

    /////////////////////////////////////////////////////////////////
    // Descriptions
    /////////////////////////////////////////////////////////////////
    public String getDescription() {
        return Strings.ACTION_BROODMOTHER_1;
    }

    public String getRangeDescription() {
        return "";
    }

    public String getCostDescription() {
        return Strings.ACTION_BROODMOTHER_2;
    }

    /////////////////////////////////////////////////////////////////
    // Action stubs
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return Strings.ACTION_BROODMOTHER_4;
    }

    public String perform(short target) {
        return "";
    }

    public short getMax() {
        return 0;
    }

    public short getCost() {
        return 0;
    }

    public short getRemaining() {
        return 0;
    }

    public short getRange() {
        return 0;
    }

    public short getTargetType() {
        return TargetType.NONE;
    }

    public Unit getOwner() {
        return owner;
    }

    public Unit getHiddenUnit() {
        return null;
    }

    public boolean passive() {
        return true;
    }

    public short getType() {
        return type;
    }

    public String getDetail() {
        return detail;
    }

}

