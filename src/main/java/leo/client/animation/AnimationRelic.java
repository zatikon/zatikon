///////////////////////////////////////////////////////////////////////
// Name: AnimationRelic
// Desc: A relic is deployed
// Date: 7/28/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.*;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class AnimationRelic implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properites
    /////////////////////////////////////////////////////////////////
    private short duration = 0;
    private final Unit victim;
    private final Unit relic;
    private final short location;
    private final int appearance;
    private float alpha, alpha2;
    private int scale;
    private double angle;
    private final AffineTransform transform = new AffineTransform();

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationRelic(Unit newRelic, Unit newVictim) {
        victim = newVictim;
        relic = newRelic;

        location = victim.getLocation();
        appearance = relic.getAppearance();

        alpha = 1.0f;
        alpha2 = 1.0f;
        scale = 0;
        angle = 0;
    }

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationRelic(Unit newRelic, Unit newVictim, int newDelay) {
        victim = newVictim;
        relic = newRelic;
        delay = newDelay;

        location = victim.getLocation();
        appearance = relic.getAppearance();

        alpha = 1.0f;
        alpha2 = 1.0f;
        scale = 0;
        angle = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        duration++;
        alpha -= 0.1f;
        scale += 5;
        angle += .1f;
        if (alpha < 0) alpha = 0;
        if (duration > 25) alpha2 -= .1f;
        if (alpha2 < 0) alpha2 = 0;

        // Location for the relic image
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + victim.getStepX() + surface.getScreenX() - (scale / 2);
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + victim.getStepY() + surface.getScreenY() - (scale / 2);

        // Location for the run image
        float offX = (BattleField.getX(location) * Constants.SQUARE_SIZE) + victim.getStepX() + surface.getScreenX() - 5;
        float offY = (BattleField.getY(location) * Constants.SQUARE_SIZE) + victim.getStepY() + surface.getScreenY() - 5;

        int size = Constants.SQUARE_SIZE + scale;

        Composite original = g.getComposite();


        // Change alpha
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha2));

        // Margin move
        transform.translate(offX, offY);

        // Rotate
        transform.rotate(angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);

        // Draw Rune
        g.drawImage(Client.getImages().getImage(Constants.IMG_RELIC), transform, frame);

        // Unrotate
        transform.rotate(-angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);

        // Move margin back
        transform.translate(-offX, -offY);


        if (relic.getID() != UnitType.RELIC_EXPLODE.value()) {
            // Change alpha
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

            // Draw relic
            g.drawImage(Client.getImages().getImage(appearance), x, y, size, size, frame);
        }

        // reset
        g.setComposite(original);
    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration > 35;
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
