///////////////////////////////////////////////////////////////////////
// Name: UnitArchangel
// Desc: Wrathful general of god
// Date: 5/2/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitArchangel extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Vector<Unit> wards = new Vector<Unit>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitArchangel(Castle newCastle) {
        castle = newCastle;

        // access
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = Unit.ARCHANGEL;
        category = Unit.CLERGY;
        name = Strings.UNIT_ARCHANGEL_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 6;
        lifeMax = 6;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 5;
        castleCost = 550;
        organic = true;
        appearance = Constants.IMG_ARCHANGEL;

        add(new ActionFlight(this));

        // Add the ability description
        ActionTrait undying = new ActionTrait(this, Strings.UNIT_ARCHANGEL_2, Strings.UNIT_ARCHANGEL_3, "", "");
        undying.setDetail(Strings.UNIT_ARCHANGEL_4);
        add(undying);

        //EventSmite es = new EventSmite(this);
        //add((Event) es);
        //add((Action) es);

        EventAegis ea = new EventAegis(this);
        add((Event) ea);
        add((Action) ea);

        // Add the actions
        actions.add(move);
        actions.add(attack);

    }


    /////////////////////////////////////////////////////////////////
    // show off splendorl
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().getObserver().unitEffect(this, Action.EFFECT_ANGEL);
    }


    /////////////////////////////////////////////////////////////////
    // round up the faithful
    /////////////////////////////////////////////////////////////////
    public Vector<Unit> getWards() {
        return wards;
    }


    /////////////////////////////////////////////////////////////////
    // Gather the faithful
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        super.refresh();
        if (castle.getBattleField() == null) return;
        wards.clear();

        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();
            if (victim.getCastle() == castle) {
                if (victim.getOrganic(this)) {
                    wards.add(victim);
                }
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Die, return to castle
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death) { // Add to the castle
            getCastle().add(Unit.getUnit(getID(), getCastle()));

            // Warn the client about an addition
            getCastle().getObserver().castleAddition(this);

            // show it
            getCastle().getObserver().attack(this, this, getCastle().getLocation(), Action.ATTACK_SPIRIT);
        }

        // Ok, die now
        super.die(false, source);
    }
}
