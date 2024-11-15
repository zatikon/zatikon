///////////////////////////////////////////////////////////////////////
// Name: UnitGate
// Desc: A gate
// Date: 5/16/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitGate extends UnitSummon implements Conjuration {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitGate(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.GATE;
        name = Strings.UNIT_GATE_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 1;
        actionsMax = 1;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_GATE;
        canWin = false;

        add(new ActionInorganic(this));
        ActionTrait trait = new ActionTrait(this, Strings.UNIT_GATE_2, Strings.UNIT_GATE_3, "", Strings.UNIT_GATE_4);
        trait.setType(Action.OTHER);
        trait.setDetail(Strings.UNIT_GATE_5);
        add(trait);
        add(new ActionRecall(this));
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    @Override
    public Vector<Short> getBonusCastleTargets(Unit looker) {
        if (looker.getCastle() != getCastle()) return null;
        Vector<Short> targets = new Vector<Short>();
        targets.add(getLocation());
        return targets;
    }

}
