package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitMilitia extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitMilitia(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.MILITIA;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_MILITIA_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 4;
        lifeMax = 4;
        actionsLeft = 3;
        actionsMax = 3;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 0;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_MILITIA;

        //add(new ActionFlight(this));

        // Add the ability description
        ActionTrait undying = new ActionTrait(this, Strings.UNIT_MILITIA_2, Strings.UNIT_MILITIA_3, Strings.UNIT_MILITIA_4);
        undying.setDetail(Strings.UNIT_MILITIA_5);
        add(undying);

        // Add the ability description
        ActionTrait haste = new ActionTrait(this, Strings.UNIT_MILITIA_6, Strings.UNIT_MILITIA_7, Strings.UNIT_MILITIA_8);
        haste.setDetail(Strings.UNIT_MILITIA_9);
        add(haste);

        ActionTrait cumulative = new ActionTrait(this, "Cumulative", "Increase militia deploy cost", "Triggers on deploy");
        cumulative.setDetail("The deploy cost of this unit increases by 1 with each deployed militia until the end of turn.");
        add(cumulative);

        //EventSmite es = new EventSmite(this);
        //add((Event) es);
        //add((Action) es);

        //EventAegis ea = new EventAegis(this);
        //add((Event) ea);
        //add((Action) ea);

        // Add the actions
        actions.add(move);
        actions.add(attack);

    }


    /////////////////////////////////////////////////////////////////
    // show off splendorl
    /////////////////////////////////////////////////////////////////
    public void entered() {
        //refresh();
        // getCastle().getObserver().unitEffect(this, Action.EFFECT_ANGEL);
    }

    /////////////////////////////////////////////////////////////////
    // predeploy processor
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short location) {
        getCastle().setMilitia((byte) 1);
    }

    /////////////////////////////////////////////////////////////////
    // Get the deploy cost
    /////////////////////////////////////////////////////////////////
    public short getDeployCost() {
        short deploy = (byte) (deployCost - getCastle().getLogistics());
        deploy += getCastle().getMilitia();
        return deploy < 0 ? 0 : deploy;
    }

    /////////////////////////////////////////////////////////////////
    // Gather the faithful
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
    }


    /////////////////////////////////////////////////////////////////
    // Die, return to castle
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        boolean deathType = death;
        if (deathType) {
            // Checks if there are fewer units on your field.
            Castle yourCastle;
            Castle theirCastle;
            if (getCastle() == getCastle().getBattleField().getCastle1()) {
                yourCastle = getCastle().getBattleField().getCastle1();
                theirCastle = getCastle().getBattleField().getCastle2();
            } else {
                yourCastle = getCastle().getBattleField().getCastle2();
                theirCastle = getCastle().getBattleField().getCastle1();
            }
            int yourNum = yourCastle.getUnitsOut().size();
            yourNum--; // Ensures that the currently dying militia is not counted
   /*int theirNum = theirCastle.getUnitsOut().size();
   if(yourNum<theirNum){
    
    // Add to the castle
    // Unit.getUnit(getID(), getCastle()) -> this
     getCastle().add(this);
  // Checks if there are fewer units on your field.
  /* Castle yourCastle;
   Castle theirCastle;
   if (getCastle() == getCastle().getBattleField().getCastle1())
    { yourCastle = getCastle().getBattleField().getCastle1();
     theirCastle = getCastle().getBattleField().getCastle2(); }
   else{
    yourCastle = getCastle().getBattleField().getCastle2();
   theirCastle = getCastle().getBattleField().getCastle1(); 
   }
   int yourNum = yourCastle.getUnitsOut().size();
   yourNum--; // Ensures that the currently dying militia is not counted */
            int theirNum = theirCastle.getUnitsOut().size();
            if (yourNum < theirNum) {

                deathType = false;
                // Add to the castle
                getCastle().add(Unit.getUnit(getID(), getCastle()));

                // Warn the client about an addition
                getCastle().getObserver().castleAddition(this);

                // show it
                getCastle().getObserver().attack(this, this, getCastle().getLocation(), Action.ATTACK_SPIRIT);
            }

        }

        // Ok, die now
        super.die(deathType, source);
    }

    /////////////////////////////////////////////////////////////////
    // Get deploy targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        Vector<Short> locations = new Vector<Short>();
        Vector<Unit> units = castle.getBattleField().getUnits();

        // Add area around castle
        Vector<Short> area = new Vector<Short>();
        area = getCastle().getBattleField().getArea(
                this,
                getCastle().getLocation(),
                (byte) 1,
                true,
                false,
                false,
                false,
                TargetType.BOTH,
                getCastle());

        Iterator<Short> itb = area.iterator();
        short spot;

        while (itb.hasNext()) {
            spot = itb.next();
            if (!locations.contains(spot))
                locations.add(spot);
        }

        // Add area around all allied units
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle && !(victim instanceof UnitMilitia) && !(victim instanceof RelicPowerUp)) {
                area = getCastle().getBattleField().getArea(
                        this,
                        victim.getLocation(),
                        (byte) 1,
                        true,
                        false,
                        false,
                        false,
                        TargetType.MOVE,
                        getCastle());

                itb = area.iterator();

                while (itb.hasNext()) {
                    spot = itb.next();
                    if (!locations.contains(spot))
                        locations.add(spot);
                }
            }
        }
        return locations;
    }

}