///////////////////////////////////////////////////////////////////////
// Name: ServerObserver
// Desc: The server game observer
// Date: 6/18/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server.observers;

// imports

import leo.shared.Log;
import leo.server.game.ServerGame;
import leo.shared.Castle;
import leo.shared.Observer;
import leo.shared.Unit;

import java.util.Vector;


public class ServerObserver implements Observer {


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ServerGame game;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ServerObserver(ServerGame newGame) {
        game = newGame;
    }


    /////////////////////////////////////////////////////////////////
    // Selected stub
    /////////////////////////////////////////////////////////////////
    public void selectUnit(Unit unit) {
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame(Castle winner) {
        game.endGame(winner);
    }


    /////////////////////////////////////////////////////////////////
    // unit effects
    /////////////////////////////////////////////////////////////////
    public void unitEffect(Unit target, int effect) {
    }


    /////////////////////////////////////////////////////////////////
    // Stub. Do nothing here.
    /////////////////////////////////////////////////////////////////
    public void attack(Unit attacker, Unit victim, short damage, int type) {
    }


    /////////////////////////////////////////////////////////////////
    // Stub. Do nothing here.
    /////////////////////////////////////////////////////////////////
    public void unitDamaged(Unit source, Unit damagedUnit, short amount) {
    }


    /////////////////////////////////////////////////////////////////
    // Play a sound
    /////////////////////////////////////////////////////////////////
    public void playSound(short sound) {
    }


    /////////////////////////////////////////////////////////////////
    // Draw an image
    /////////////////////////////////////////////////////////////////
    public void imageDraw(Unit unit, short location, int image, int duration) {
    }


    /////////////////////////////////////////////////////////////////
    // Ability used
    /////////////////////////////////////////////////////////////////
    public void abilityUsed(short source, short damagedUnit, int image) {
    }


    /////////////////////////////////////////////////////////////////
    // Castle addition
    /////////////////////////////////////////////////////////////////
    public void castleAddition(Unit newUnit) {
    }


    /////////////////////////////////////////////////////////////////
    // Castle refresh
    /////////////////////////////////////////////////////////////////
    public void castleRefresh(Castle castle) {
        game.getPlayer(castle).getUser().sendCastle(castle);
    }


    /////////////////////////////////////////////////////////////////
    // Area effect
    /////////////////////////////////////////////////////////////////
    public void areaEffect(short source, short destination, int type, Unit victim) {
    }


    /////////////////////////////////////////////////////////////////
    // Fireball!
    /////////////////////////////////////////////////////////////////
    public void fireball(short source, short destination, int image, Vector<Unit> victims, Vector<Short> damages, short type) {
    }


    /////////////////////////////////////////////////////////////////
    // Lightning!
    /////////////////////////////////////////////////////////////////
    public void lightning(short source, short destination) {
    }


    /////////////////////////////////////////////////////////////////
    // Text!
    /////////////////////////////////////////////////////////////////
    public void text(String text) {
        Log.game(game.getCurrentPlayer().getName() + ": " + text);
    }


    /////////////////////////////////////////////////////////////////
    // Something dies
    /////////////////////////////////////////////////////////////////
    public void death(Unit victim) {
    }


    /////////////////////////////////////////////////////////////////
    // Draw game
    /////////////////////////////////////////////////////////////////
    public void drawGame() {
    }


    public void allySurrendered() {
    }

    public void enemySurrendered() {
    }
}
