///////////////////////////////////////////////////////////////////////
// Name: UnitKnight
// Desc: A horseman
// Date: 6/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Iterator;
import java.util.Vector;


public class UnitKnight extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitKnight(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.KNIGHT;
        category = Unit.HORSEMEN;
        name = Strings.UNIT_KNIGHT_1;
        actions = new Vector<Action>();
        damage = 5;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE, (byte) 3);
        attack = new ActionAttack(this, (byte) 0, (byte) 1, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 3;
        castleCost = 200;
        organic = true;
        appearance = Constants.IMG_KNIGHT;

        add(new ActionMounted(this));

        ActionTrait dismount = new ActionTrait(this,
                Strings.UNIT_KNIGHT_2,
                Strings.UNIT_KNIGHT_3,
                "",
                Strings.UNIT_KNIGHT_4);

        dismount.setHiddenUnit(new UnitDismountedKnight(getCastle()));
        add(dismount);

        // Add the actions
        actions.add(move);
        actions.add(attack);
    }


    /////////////////////////////////////////////////////////////////
    // force a false death
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        // Check if any units (eg Conspirator or Bounty Hunter) have marked the Knight
        //    Marks will be passed along after the knight dismounts
        Vector<Unit> markers = null;
        if (death) {
            markers = new Vector<Unit>();
            Vector<Unit> allUnits = getCastle().getBattleField().getUnits();
            Iterator it = allUnits.iterator();
            while (it.hasNext()) {
                Unit checker = (Unit) it.next();
                if (checker.getMark() == this)
                    markers.add(checker);
            }
        }

        // Die
        super.die(false, source);

        // Dismount
        if (death)
            dismount(markers);
    }


    /////////////////////////////////////////////////////////////////
    // Death event trigger
    /////////////////////////////////////////////////////////////////
    public void dismount(Vector<Unit> markers) {
        // Put a footman in it's place
        Unit footman = new UnitDismountedKnight(getCastle());
        // Pass along any marks on the knight
        Iterator it = markers.iterator();
        while (it.hasNext()) {
            Unit marker = (Unit) it.next();
            marker.setMark(footman);
        }
        // Setup the Dismounted Knight
        footman.setID(getID());
        footman.unDeploy();
        footman.setHidden(true);
        footman.setLocation(getLocation());
        footman.setBattleField(getCastle().getBattleField());
        footman.getCastle().getBattleField().add(footman);
        footman.getCastle().addOut(getTeam(), footman);
        footman.grow(getBonus());

        // effect
        getCastle().getObserver().unitEffect(footman, Action.EFFECT_FADE_IN);

        // Check for victory
        Castle castley = getCastle().getBattleField().getCastle1();
        if (getCastle() == castley)
            castley = getCastle().getBattleField().getCastle2();

        if (footman.getLocation() == castley.getLocation()) {
            castley.getObserver().endGame(getCastle());
        }
    }

}
