///////////////////////////////////////////////////////////////////////
// Name: UnitPortal
// Desc: A portal
// Date: 5/16/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitPortal extends UnitSummon implements Conjuration {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitPortal(Castle newCastle) {
        super();
        castle = newCastle;

        // Initialize
        //id   = Unit.PORTAL;
        name = Strings.UNIT_PORTAL_1;
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
        appearance = Constants.IMG_PORTAL;
        canWin = false;

        add(new ActionInorganic(this));

        // Add the ability description
        ActionTrait trait = new ActionTrait(this, Strings.UNIT_PORTAL_2, Strings.UNIT_PORTAL_3, "", Strings.UNIT_PORTAL_4);
        trait.setType(Action.SPELL);
        trait.setDetail(Strings.UNIT_PORTAL_5);
        add(trait);
        add(new ActionSummon(this));
    }


    /////////////////////////////////////////////////////////////////
    // Die, return to castle
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        if (death) {
            death(source);
        }

        // Ok, die now
        super.die(death, source);
    }


    /////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////
    private void death(Unit murderer) {
        if (!murderer.getOrganic(this)) return;
        if (!murderer.targetable(this)) return;

        // get the outcome
        short outcome = getBattleField().event(Event.PREVIEW_ACTION, this, murderer, Action.SPELL, Event.NONE, Event.OK);

        // if its a fizzle, end
        if (outcome == Event.CANCEL) {
            getCastle().getObserver().unitEffect(murderer, Action.EFFECT_FIZZLE);
            return;
        }

        // Generate a nifty effect
        //getCastle().getObserver().unitEffect(this, Action.EFFECT_FADE);
        //getCastle().getObserver().unitEffect(murderer, Action.EFFECT_FADE);
        //murderer.setHidden(true);

        murderer.setLocation(getLocation());
        murderer.stun();

        //getCastle().getObserver().unitEffect(murderer, Action.EFFECT_FADE_IN);
        getCastle().getObserver().playSound(Constants.SOUND_POOF);
    }


}
