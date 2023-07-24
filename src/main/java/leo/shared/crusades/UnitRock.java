///////////////////////////////////////////////////////////////////////
// Name: UnitRock
// Desc: Emtombed unit
// Date: 5/25/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitRock extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit captive;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitRock(Castle newCastle, Unit newCaptive) {
        castle = newCastle;
        captive = newCaptive;

        // Initialize
        id = Unit.ROCK;
        name = Strings.UNIT_ROCK_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 0;
        actionsMax = 0;
        move = null;
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_ROCK;

        add(new ActionInorganic(this));
        if (captive != null && captive.getID() != Unit.NONE) {
            ActionTrait captiveTrait = new ActionTrait(this, Strings.UNIT_ROCK_2, Strings.UNIT_ROCK_3 + captive.getName(), "", Strings.UNIT_ROCK_4);
            captiveTrait.setDetail(Strings.UNIT_ROCK_4);
            captiveTrait.setHiddenUnit(captive);

            add(captiveTrait);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Die, free the captive
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        super.die(death, source);
        captive.setLocation(getLocation());
        captive.setBattleField(getCastle().getBattleField());
        getCastle().getBattleField().add(captive);
        getCastle().addOut(captive.getTeam(), captive);
        captive.unDie();
        captive.stun();
    }

/*
 /////////////////////////////////////////////////////////////////
 // Refresh the captive too
 /////////////////////////////////////////////////////////////////
 public void refresh()
 { super.refresh();
  captive.refresh();
 }
*/

}
