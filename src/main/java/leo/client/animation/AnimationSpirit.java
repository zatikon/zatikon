///////////////////////////////////////////////////////////////////////
// Name: AnimationSpirit
// Desc: A spirit floating somewhere
// Date: 7/19/2008 - Gabe Jones
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
import java.awt.geom.AffineTransform;


public class AnimationSpirit implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final short source;
    private final short location;
    private int x;
    private int y;
    private float stepX;
    private float stepY;
    private final int image;
    private final AffineTransform transform = new AffineTransform();
    private Unit victim;
    private Unit attacker;
    private final AnimationImage ghost = null;
    private final int speed;
    private int imageOwner;
    private final float alpha = 0.5f;
    private final boolean vicIsAlly;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationSpirit(short newSource, short newLocation, int newImage, boolean isAlly) {
        source = newSource;
        location = newLocation;
        //image = Client.getImages().getGrayedImage(newImage, true);
        image = newImage;
        speed = 6;
        vicIsAlly = isAlly;
        if (source == location) return;

        //speed = Constants.STEP_SPEED;

        // Build coordinates
        float x1 = BattleField.getX(source);
        float y1 = BattleField.getY(source);
        float x2 = BattleField.getX(location);
        float y2 = BattleField.getY(location);

        // The greatest distance
        int distance = BattleField.getDistance(source, location);

        float tmpDistance = distance;

        float tmpSpeed = speed;

        // Calculate the movement increment
        float tmpStepX = x2 - x1;
        float tmpStepY = y2 - y1;
        tmpStepX *= tmpSpeed;
        tmpStepY *= tmpSpeed;
        tmpStepX /= tmpDistance;
        tmpStepY /= tmpDistance;

        stepX = tmpStepX;
        stepY = tmpStepY;

        // Initialize starting location
        x = (int) (x1 * Constants.SQUARE_SIZE);
        y = (int) (y1 * Constants.SQUARE_SIZE);
        transform.translate(x, y);

        // How long should this animation play
        duration = calculateDuration(distance, (int) (Math.abs(stepX) > Math.abs(stepY) ? Math.abs(stepX) : Math.abs(stepY)));
    }


    /////////////////////////////////////////////////////////////////
    // Calculate the duration
    /////////////////////////////////////////////////////////////////
    private int calculateDuration(int length, int step) {
        return (length * Constants.SQUARE_SIZE) / step;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        --delay;
        if (delay > 0)
            return;
        if (source == location) return;
        duration--;

        // Margins
        int offX = surface.getScreenX();
        int offY = surface.getScreenY();

        // Take a step
        transform.translate(stepX, stepY);

        // Margin move
        transform.translate(offX, offY);

        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.drawImage(Client.getImages().getGrayedImage(image, vicIsAlly), transform, frame);
        g.setComposite(original);

        // Unmargin
        transform.translate(-offX, -offY);

        // If done, well, nothing. Placeholder.
        if (finished()) {
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration <= 0 || source == location;
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
