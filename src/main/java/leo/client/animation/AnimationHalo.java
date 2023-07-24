///////////////////////////////////////////////////////////////////////
// Name: AnimationHalo
// Desc: The halo
// Date: 5/15/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;
import leo.shared.Unit;
import leo.shared.crusades.UnitArchangel;

import java.awt.*;
import java.util.Vector;

public class AnimationHalo implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properites
    /////////////////////////////////////////////////////////////////
    private final UnitArchangel angel;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationHalo(Unit newAngel) {
        angel = (UnitArchangel) newAngel;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        Vector<Unit> wards = angel.getWards();

        for (int i = 0; i < wards.size(); i++) {
            Unit unit = wards.elementAt(i);

            // For the middle
            int x = ((BattleField.getX(unit.getLocation()) - 0) * Constants.SQUARE_SIZE) + unit.readStepX() + surface.getScreenX();
            int y = ((BattleField.getY(unit.getLocation()) - 0) * Constants.SQUARE_SIZE) + unit.readStepY() + surface.getScreenY();
            y -= (Constants.SQUARE_SIZE - 12) / 2;

            if (!unit.isDead()) {
                Composite original = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.drawImage(Client.getImages().getImage(Constants.IMG_BURST_1), x, y, frame);
                g.setComposite(original);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return angel.isDead();
    }

    public void setDelay(int d) {
        delay = d;
    }

    public short getLocation() {
        return angel.getLocation();
    }

    public void setDirection(short b) {
        return;
    }
}
