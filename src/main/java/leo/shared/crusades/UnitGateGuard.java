///////////////////////////////////////////////////////////////////////
// Name: UnitGateGuard
// Desc: A gate guard
// Date: 8/28/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGateGuard extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGateGuard(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.GATE_GUARD;
        category = Unit.SOLDIERS;
        name = Strings.UNIT_GATE_GUARD_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 1;
        life = 5;
        lifeMax = 5;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMoveCastle(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 0;
        castleCost = 50;
        organic = true;
        appearance = Constants.IMG_GATE_GUARD;

        // special description
        ActionTrait sentry = new ActionTrait(this, Strings.UNIT_GATE_GUARD_2,
                Strings.UNIT_GATE_GUARD_3,
                "",
                "");
        sentry.setDetail(Strings.UNIT_GATE_GUARD_4);
        add(sentry);

        // take cover
        ActionTrait cover = new ActionTrait(this, Strings.UNIT_GATE_GUARD_5,
                Strings.UNIT_GATE_GUARD_6,
                Strings.UNIT_GATE_GUARD_7,
                Strings.UNIT_GATE_GUARD_8);
        cover.setDetail(Strings.UNIT_GATE_GUARD_9);
        add(cover);

        // Add the actions
        actions.add(move);
        actions.add(attack);

        if (castle != null)
            if (castle.isAI()) {
                life += 2;
                lifeMax += 2;
            }
    }


    /////////////////////////////////////////////////////////////////
    // Get the armor
    /////////////////////////////////////////////////////////////////
    public short getArmor() {
        short tmpArmor;
        if (getCastle().getLocation() == getLocation()) {
            armor++;
            tmpArmor = super.getArmor();
            armor--;
        } else {
            tmpArmor = super.getArmor();
        }
        return tmpArmor;
    }


    /////////////////////////////////////////////////////////////////
    // Add the castle as a deploy target
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() { //Vector targets = super.getCastleTargets();
        Vector<Short> targets = new Vector<Short>();
        Unit atCastle = getCastle().getBattleField().getUnitAt(getCastle().getLocation());
        if (atCastle == null)
            targets.add(new Short(getCastle().getLocation()));
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Inorganic when on castle
    /////////////////////////////////////////////////////////////////
    public boolean getOrganic(Unit looker) {
        if (looker.getCastle() == getCastle()) return organic;
        if (getCastle().getLocation() == getLocation()) {
            return false;
        }
        return organic;
    }

}
