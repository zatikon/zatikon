///////////////////////////////////////////////////////////////////////
// Name: UnitWillWisps
// Desc: A sprit
// Date: 5/30/2004 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWillWisps extends UnitSummon {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    EventVolatileNature vol;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWillWisps(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.WILL_O_THE_WISPS;
        name = Strings.UNIT_WILL_WISPS_1;
        actions = new Vector<Action>();
        damage = 4;
        armor = 0;
        life = 1;
        lifeMax = 1;
        actionsLeft = 1;
        actionsMax = 1;
        move = new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3);
        attack = new ActionSuicide(this, (byte) 1, (byte) 0, TargetType.UNIT_LINE, (byte) 1);
        deployCost = 1;
        castleCost = 1001;
        organic = true;
        appearance = Constants.IMG_WILL_O_THE_WISPS;
        canWin = false;

        ActionTrait intangible = new ActionTrait(this, Strings.UNIT_WILL_WISPS_2, Strings.UNIT_WILL_WISPS_3, "");
        intangible.setDetail(Strings.UNIT_WILL_WISPS_4);
        add(intangible);

        // Add the actions
        actions.add(move);
        actions.add(attack);
        vol = new EventVolatileNature(this);
        add((Event) vol);
        add((Action) vol);

    }

    public void die(boolean death, Unit source) {
        if (death) vol.perform(this, source, Event.NONE, Event.NONE, Event.NONE);
        super.die(death, source);
    }

}
