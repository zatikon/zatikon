///////////////////////////////////////////////////////////////////////
// Name: AnimationDeath
// Desc: An unit dies
// Date: 11/16/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;

public class AnimationDeath implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properites
    /////////////////////////////////////////////////////////////////
    private short duration = 0;
    private final Unit victim;
    private int scale = 0;
    private final short location;
    private int appearance;
    private float alpha;
    private boolean cancelled = false;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationDeath(Unit newVictim) {
        victim = newVictim;
        location = victim.getLocation();

        if (victim.killed()) {
            cancelled = true;
            return;
        }
        victim.kill();

        if (victim.getAppearance() == Constants.IMG_WALL_MASON) victim.setAppearance(Constants.IMG_WALL);

        appearance = (victim.getAppearance() +
                (victim.getCastle() ==
                        Client.getGameData().getMyCastle() ?
                        0 :
                        1));

        alpha = 1.0f;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        if (cancelled) return;
        duration++;
        scale += 5;
        alpha -= 0.1f;
        if (alpha < 0) alpha = 0;

        // For the middle
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + victim.getStepX() + surface.getScreenX() - (scale / 2);
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + victim.getStepY() + surface.getScreenY() - (scale / 2);

        int size = Constants.SQUARE_SIZE + scale;

        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.drawImage(Client.getImages().getImage(appearance), x, y, size, size, frame);

        g.setComposite(original);
        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration > 10 || cancelled;
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
