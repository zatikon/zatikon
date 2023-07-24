///////////////////////////////////////////////////////////////////////
// Name: UnitSummon
// Desc: Contains specialized functions for summoned units.
//          All units which are summons should inherit from this.
// Date: 7/12/2011
//  TODO:
///////////////////////////////////////////////////////////////////////

package leo.shared.crusades;

// imports

import leo.shared.Event;
import leo.shared.Unit;


public class UnitSummon extends Unit {
    private Unit parent = null;


    public UnitSummon() {
        //id = Unit.NONE;
    }


    public void setParent(Unit p) {
        parent = p;
    }

    /////////////////////////////////////////////////////////////////
    // Deployed event, in case of reset (summons can't be reset)
    /////////////////////////////////////////////////////////////////
    public void entered() {
        if (id == Unit.NONE)
            die(false, this);
        else
            id = Unit.NONE;
    }

    // When banished or reset, die and return to owner
    public void die(boolean death, Unit source) {
    /*dead = true;
    if (!death) doomed = true;
    // preview the death
    if (death)
    {
     short result = getBattleField().event(Event.PREVIEW_DEATH, this, source, Event.NONE, Event.NONE, Event.NONE);
     if (result == Event.CANCEL)
     { if (!doomed) dead = false;
      return;
     }
    }*/
        if (parent == null) {
            System.out.println("Invalid summon death: no parent");
            return;
        }

        EventSummoner esum = parent.getSummonManager();

        if (esum == null) {
            System.out.println("Invalid summon death: no event summoner");
            return;
        }

        esum.perform(this, source, Event.NONE, Event.NONE, Event.NONE);

        this.setDead(true);
        getBattleField().remove(this);
        getCastle().removeOut(this);
        deathTrigger(death, source);

        getCastle().getObserver().death(this);

        // Killing summons doesn't make you a murderer
    
    /*if (death)
    { if (getOrganic(this)) getCastle().addGraveyard(this);
     if (getOrganic(this)) source.setMurderer();
     //getCastle().getObserver().death(this, source);
    }*/

        //short deathType = death ? Event.TRUE : Event.FALSE;

        // the death event
        //tmp.witnessDeath(deathType, source, this);
        //getBattleField().event(Event.WITNESS_DEATH, this, source, deathType, Event.NONE, Event.OK);

    }
}
