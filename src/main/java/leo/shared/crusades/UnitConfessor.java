///////////////////////////////////////////////////////////////////////
// Name: UnitConfessor
// Desc: A confessor
// Date: 3/18/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitConfessor extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitConfessor(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.LEGIONS;

        // Initialize
        id = UnitType.CONFESSOR.value();
        category = Unit.CULTISTS;
        name = Strings.UNIT_CONFESSOR_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 0;
        life = 3;
        lifeMax = 3;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 2;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_CONFESSOR;

        // Describe his holy death
        ActionTrait penance = new ActionTrait(this, Strings.UNIT_CONFESSOR_2, Strings.UNIT_CONFESSOR_3, "");
        penance.setType(Action.SPELL);
        penance.setDetail(Strings.UNIT_CONFESSOR_4);
        add(penance);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // the murderers!
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets(short type) {
        Vector<Short> targets = new Vector<Short>();
        if (!(type == TargetType.UNIT_LINE ||
                type == TargetType.UNIT_LINE_JUMP ||
                type == TargetType.UNIT_AREA ||
                type == TargetType.ANY_LINE ||
                type == TargetType.ANY_LINE_JUMP ||
                type == TargetType.ANY_AREA)) {
            return targets;
        }

        Vector<Unit> units = castle.getBattleField().getUnits();
        Iterator<Unit> it = units.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();

            if (victim.getCastle() != castle &&
                    victim.getOrganic(this) &&
                    victim.targetable(this) &&
                    victim.isMurderer()) {
                targets.add(new Short(victim.getLocation()));
            }
        }
        return targets;
    }
}
