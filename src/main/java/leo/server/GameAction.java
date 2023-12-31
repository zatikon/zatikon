///////////////////////////////////////////////////////////////////////
//	Name:	GameAction
//	Desc:	A backed up game action
//	Date:	2/23/09
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

public class GameAction {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Player player = null;
    private final short action;
    private final short actor;
    private final short target;


    /////////////////////////////////////////////////////////////////
    // Construcor
    /////////////////////////////////////////////////////////////////
    public GameAction(Player newPlayer, short newAction, short newActor, short newTarget) {
        player = newPlayer;
        action = newAction;
        actor = newActor;
        target = newTarget;
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public Player getPlayer() {
        return player;
    }

    public short getAction() {
        return action;
    }

    public short getActor() {
        return actor;
    }

    public short getTarget() {
        return target;
    }


}
