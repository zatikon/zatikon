///////////////////////////////////////////////////////////////////////
// Name: AnimationTime
// Desc: Spinning clock thingy
// Date: 7/27/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class AnimationTime implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final short location;
    private int x;
    private int y;
    private final AffineTransform transform = new AffineTransform();
    private double angle;
    private float alpha;

    private int delay;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationTime(short newLocation, boolean noisy) {
        location = newLocation;

        if (noisy) Client.getImages().playSound(Constants.SOUND_WHEEL);

        // Build coordinates
        int x = BattleField.getX(location);
        int y = BattleField.getY(location);

        // Initialize starting location
        x = (x * Constants.SQUARE_SIZE);
        y = (y * Constants.SQUARE_SIZE);
        transform.translate(x, y);

        // Calculate the direction the arrow faces
        angle = 0;
        alpha = 0.0f;

        // How long should this animation play
        duration = 60;
        delay = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        duration--;
        if (alpha < 1 && duration > 30) alpha += 0.1f;
        if (duration < 20) alpha -= 0.05f;
        if (alpha > 1) alpha = 1.0f;
        if (alpha < 0) alpha = 0.0f;
        angle += 0.1f;

        // alpha
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 3 * (alpha / 4)));

        // Margins
        float offX = surface.getScreenX() - 5;
        float offY = surface.getScreenY() - 5;

        // Margin move
        transform.translate(offX, offY);

        g.drawImage(Client.getImages().getImage(Constants.IMG_TIME_1), transform, frame);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Rotate the thingy
        transform.rotate(-angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);

        // Put it on the screen
        g.drawImage(Client.getImages().getImage(Constants.IMG_TIME_2), transform, frame);

        // Unrotate
        transform.rotate(-angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);

        // draw image 2
        g.drawImage(Client.getImages().getImage(Constants.IMG_TIME_3), transform, frame);

        transform.rotate(angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);
        transform.rotate(angle, (Constants.SQUARE_SIZE + 10) / 2, (Constants.SQUARE_SIZE + 10) / 2);

        // Unmargin
        transform.translate(-offX, -offY);

        g.setComposite(original);

        if (finished()) {
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration <= 0;
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
