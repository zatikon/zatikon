///////////////////////////////////////////////////////////////////////
// Name: AnimationTruce
// Desc: The truce
// Date: 5/25/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;
import leo.shared.Unit;
import leo.shared.crusades.UnitNone;

import java.awt.*;

public class AnimationTruce implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properites
    /////////////////////////////////////////////////////////////////
    private final Unit unit;
    private final Unit enemy;

    private int delay;
    private final short location;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationTruce(Unit newUnit) {
        unit = newUnit;
        location = unit.getLocation();
        if (unit.getCastle() == Client.getGameData().getMyCastle()) {
            enemy = new UnitNone(Client.getGameData().getEnemyCastle());
        } else {
            enemy = new UnitNone(Client.getGameData().getMyCastle());
        }
        delay = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        --delay;
        if (delay > 0)
            return;
        // For the middle
        int x = ((BattleField.getX(unit.getLocation()) - 0) * Constants.SQUARE_SIZE) + unit.readStepX() + surface.getScreenX();
        int y = ((BattleField.getY(unit.getLocation()) - 0) * Constants.SQUARE_SIZE) + unit.readStepY() + surface.getScreenY();

        if (!unit.isDead()) {
            g.drawImage(Client.getImages().getImage(Constants.IMG_WHITE_FLAG), x, y, frame);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return unit.targetable(enemy);
    }


    public void setDelay(int d) {
        delay = d;
    }

    public short getLocation() {
        return location;
    }

    public void setDirection(short b) {
        return;
    }
}
