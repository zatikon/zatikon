///////////////////////////////////////////////////////////////////////
// Name: UnitCommandPost
// Desc: A commandPost
// Date: 4/24/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared.crusades;

// imports

import leo.shared.*;

import java.util.Vector;


public class UnitCommandPost extends Unit {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int plans = 0;
    private final Action plan;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public UnitCommandPost(Castle newCastle) {
        castle = newCastle;

        // access level
        accessLevel = Unit.CRUSADES;

        // Initialize
        id = Unit.COMMAND_POST;
        category = Unit.STRUCTURES;
        name = Strings.UNIT_COMMAND_POST_1;
        actions = new Vector<Action>();
        damage = 0;
        armor = 2;
        life = 5;
        lifeMax = 5;
        actionsLeft = 2;
        actionsMax = 2;
        move = null;
        attack = null;
        deployCost = 4;
        castleCost = 100;
        organic = false;
        appearance = Constants.IMG_COMMAND_POST;
        opaque = true;

        add(new ActionInorganic(this));


        // Add the ability description
        ActionTrait logistics = new ActionTrait(this, Strings.UNIT_COMMAND_POST_2, Strings.UNIT_COMMAND_POST_3, Strings.UNIT_COMMAND_POST_4);
        logistics.setDetail(Strings.UNIT_COMMAND_POST_5);
        add(logistics);

        actions.add(new ActionShowPlans(this));

        // Planning
        plan = new ActionPlan(this);
        add(plan);
        actions.add(new ActionUsePlans(this));
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        if (plan != null && plan.getRemaining() > 0) {
            plan.perform(getLocation());
        }
        if (plan != null && plan.getRemaining() > 0) {
            plan.perform(getLocation());
        }

        super.refresh();
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() + 1));
        getCastle().setLogistics((byte) (getCastle().getLogistics() + 1));
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
        getCastle().setCommandsMax((byte) (getCastle().getCommandsMax() - 1));
        getCastle().setLogistics((byte) (getCastle().getLogistics() - 1));
    }


    /////////////////////////////////////////////////////////////////
    // Get and set the plans
    /////////////////////////////////////////////////////////////////
    public int getPlans() {
        return plans;
    }

    public void setPlans(int newPlans) {
        plans = newPlans;
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        short range = 2;
        Vector<Short> targets =
                getCastle().getBattleField().getArea(
                        this,
                        getCastle().getLocation(),
                        range,
                        true,
                        false,
                        false,
                        false, TargetType.BOTH, getCastle());
        getCastle().getBattleField().addBonusCastleTargets(this, targets, getCastle());
        return targets;
    }

    public int getDeployRange() {
        return 2;
    }

}
