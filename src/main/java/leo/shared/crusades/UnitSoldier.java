///////////////////////////////////////////////////////////////////////
// Name: UnitSoldier
// Desc: A soldier
// Date: 4/24/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitSoldier extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    public short training;
    public static short NONE = 0;
    public static short OFFENSE = 1;
    public static short DEFENSE = 2;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitSoldier(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.SOLDIER;
        name = Strings.UNIT_SOLDIER_1;
        actions = new Vector<Action>();
        damage = 3;
        armor = 1;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 0, (byte) 1, TargetType.LOCATION_LINE, (byte) 1);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_SOLDIER;
        training = NONE;

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }

    public int getAppearance() {
        if (training == OFFENSE) return Constants.IMG_SOLDIER_O;
        else if (training == DEFENSE) return Constants.IMG_SOLDIER_D;
        else return Constants.IMG_SOLDIER;
    }

    public void recieveTraining(short trainingType) {
        boolean hadArcane = removeArcane();

        //undo previous training, if any.
        if (training == DEFENSE && trainingType != DEFENSE) {
            armor--;
            lifeMax--;
            if (life > lifeMax) {
                life--;
            }
            training = NONE;
        }
        if (training == OFFENSE && trainingType != OFFENSE) {
            actionsLeft--;
            actionsMax--;
            damage--;
            training = UnitSoldier.NONE;
        }

        //apply the proper training.
        if (trainingType == OFFENSE) {
            actionsLeft++;
            actionsMax++;
            damage++;
            training = UnitSoldier.OFFENSE;
        }
        if (trainingType == DEFENSE) {
            armor++;
            life++;
            lifeMax++;
            training = UnitSoldier.DEFENSE;
        }

        if (hadArcane)
            addArcane();
    }

    /////////////////////////////////////////////////////////
    // Methods to deal with changing cost of Arcane relic
    //    effect based on current max actions
    /////////////////////////////////////////////////////////
    private boolean removeArcane() {
        Vector<Action> actions = getActions();
        Iterator it = actions.iterator();
        while (it.hasNext()) {
            Action a = (Action) it.next();
            if (a instanceof ActionArcane) {
                actions.remove(a);
                actions.remove(attack);
                return true;
            }
        }
        return false;
    }

    private void addArcane() {
        attack = new ActionArcane(this);
        actions.add(attack);
    }


}
