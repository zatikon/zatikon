///////////////////////////////////////////////////////////////////////
//	Name:	ActionMoveCastle
//	Desc:	Move the unit around, even on castle
//	Date:	8/28/2007 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.BattleField;
import leo.shared.Strings;
import leo.shared.Unit;

import java.util.Vector;


public class ActionMoveCastle extends ActionMove {
    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    private final String detail = Strings.ACTION_MOVE_CASTLE_1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionMoveCastle(
            Unit newOwner,
            short newMax,
            short newCost,
            short newTargetType,
            short newRange) {
        super(newOwner, newMax, newCost, newTargetType, newRange);
    }


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets() {
        Vector<Short> targets = super.getTargets();

        // Make sure the castle is a legal move. Empty square. Removed range limit for flying GateGuard
        if (getOwner().getCastle().getBattleField().getUnitAt(getOwner().getCastle().getLocation()) == null && BattleField.getDistance(getOwner().getLocation(), getOwner().getCastle().getLocation()) <= getRange()) {
            targets.add(getOwner().getCastle().getLocation());
        }
        return targets;
    }

    public String getDetail() {
        return detail;
    }
}
