///////////////////////////////////////////////////////////////////////
// Name: ActionArcane
// Desc: Long-range attack given by Arcane Relic
// Date: 8/12/2011 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////

package leo.shared.crusades;

// imports

import leo.shared.Action;
import leo.shared.TargetType;
import leo.shared.Unit;


public class ActionArcane extends ActionAttack {

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionArcane(Unit owner) {
        super(owner, (byte) 0, owner.getActionsMax(), TargetType.UNIT_AREA, (byte) 5, Action.ATTACK_MAGIC_BALL);
    }

}

