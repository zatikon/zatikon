///////////////////////////////////////////////////////////////////////
// Name: UnitWallMason
// Desc: Wall with a mason on top
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitWallMason extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit mason = null;
    private Unit wall = null;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitWallMason(Castle newCastle) {
        castle = newCastle;

        // Initialize
        id = UnitType.WALL_MASON.value();
        name = Strings.UNIT_WALL_MASON_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 4;
        lifeMax = 4;
        actionsLeft = 2;
        actionsMax = 2;
        move = new ActionWallWalk(this);
        attack = null;
        deployCost = 1;
        castleCost = 1001;
        organic = false;
        appearance = Constants.IMG_WALL_MASON;

        // start out ready
        deployed = true;

        add(new ActionInorganic(this));

        // wacky
        actions.add(move);

        // Make more wallness
        actions.add(new ActionBuild(this));
    }


    /////////////////////////////////////////////////////////////////
    // Set the mason
    /////////////////////////////////////////////////////////////////
    public void setMason(Unit newMason) {
        mason = newMason;
        actionsLeft = mason.getActionsLeft();
    }


    /////////////////////////////////////////////////////////////////
    // Set the wall
    /////////////////////////////////////////////////////////////////
    public void setWall(Unit newWall) {
        wall = newWall;
        life = wall.getLife();
    }


    /////////////////////////////////////////////////////////////////
    // Die, free the captive
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        super.die(death, source);
        setHidden(true);
        mason.setActions(getActionsLeft());
        mason.setLocation(getLocation());
        mason.setLocation(getLocation());
        mason.setBattleField(getCastle().getBattleField());
        getCastle().getBattleField().add(mason);
        getCastle().addOut(mason.getTeam(), mason);
        mason.unDie();
    }

    public Unit getWall() {
        return wall;
    }

    public int getAppearance() {
        if (readStepX() != 0 || readStepY() != 0)
            return Constants.IMG_MASON;
        else
            return super.getAppearance();
    }

}
