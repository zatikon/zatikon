///////////////////////////////////////////////////////////////////////
// Name: UnitWyvern
// Desc: Big nasty wyvern
// Date: 7/14/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWyvern extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean fed = false;
    private Unit meal = null;
    private EventWyvernMove broodmother = null;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWyvern(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.WYVERN;
        category = Unit.WYRMS;
        name = Strings.UNIT_WYVERN_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 2;
        life = 7;
        lifeMax = 7;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionWyvernMove(this, (byte) 1, (byte) 0, TargetType.ANY_AREA, (byte) 4);
        attack = null; //new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_WYVERN;

        add(new ActionFlight(this));
        //add(new ActionBroodmother(this));
        broodmother = new EventWyvernMove(this);
        add((Event) broodmother);
        add((Action) broodmother);
        //add(new ActionWyvernMove(this, (byte)1, (byte)0, TargetType.ANY_LINE_JUMP, (byte)4));
        //Temporarily disabled. Going to be replaced
  /*EventDevour ed = new EventDevour(this);
  add((Action) ed);
  add((Event) ed);*/
        actions.add(move);

        ActionAttack bite = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        actions.add(bite);

        EventSting sting = new EventSting(this);
        add((Action) sting);
        add((Event) sting);

        // Add the actions
        //actions.add(attack);
        //Same as Devour
        //actions.add(new ActionEgg(this));
    }

 /*public void setLocation(short newLocation) {
  broodmother.setEggPos(newLocation);
  super.setLocation(newLocation);
 }*/


    /////////////////////////////////////////////////////////////////
    // Has it fed?
    /////////////////////////////////////////////////////////////////
    public boolean fed() {
        return fed;
    }

    public void feed(boolean newState) {
        fed = newState;
    }

    public Unit getMeal() {
        return meal;
    }

    public void setMeal(Unit newMeal) {
        meal = newMeal;
    }

    /////////////////////////////////////////////////////////////////
    // Upon death or banish, lay the egg. (Actually we decided to remove this for now at least)
    /////////////////////////////////////////////////////////////////
 /*public void die(boolean death, Unit source)
 {   
  if(fed()){
  broodmother.layChild();
  feed(false);
  }

  // Ok, die now
  super.die(death, source);
 }*/
}
