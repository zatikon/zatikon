///////////////////////////////////////////////////////////////////////
//	Name:	Game
//	Desc:	The game interface
//	Date:	11/9/2007
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.game;

// imports

import leo.server.Player;
import leo.shared.Castle;
import leo.shared.Unit;

import java.util.Random;


public interface Game {
    void sendText(Player sender, String message);

    void disconnect(Player player);

    void interrupt(Player player);

    void interpretAction(Player player, short action, short actorLocation, short target) throws Exception;

    boolean over();

    Random random();

    void resynch();

    int getDelay();

    void banish(Unit unit);

    void endGame(Castle winner);

    Castle getCurrentCastle();
}
