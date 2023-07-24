///////////////////////////////////////////////////////////////////////
// Name: AnimationRise
// Desc: I don't know what the hell this does anymore
// Date: 7/29/2008 - Gabe Jones
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

public class AnimationRise implements Animation {
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
    private final int image2;
    private final AffineTransform transform = new AffineTransform();
    private double angle;
    private float alpha;

    private int delay = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationRise(short newSource, short newLocation, int newImage, int newImage2, boolean silent) {
        source = newSource;
        location = newLocation;
        image = newImage;
        image2 = newImage2;

        if (source == location) {
            duration = 0;
            Client.getImages().playSound(Constants.SOUND_JUDGEMENT);
            Client.getGameData().add(new AnimationImages(location, location, Constants.IMG_BURST_1, 20, 0));
            return;
        }

        if (!silent) Client.getImages().playSound(Constants.SOUND_EYE);

        // Build coordinates
        int x1 = BattleField.getX(source);
        int y1 = BattleField.getY(source);
        int x2 = BattleField.getX(location);
        int y2 = BattleField.getY(location);

        // The greatest distance
        int distance = BattleField.getDistance(source, location);
        float tmpDistance = distance;
        float tmpSpeed = 6;

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
        x = (x1 * Constants.SQUARE_SIZE);
        y = (y1 * Constants.SQUARE_SIZE);
        transform.translate(x, y);

        // Calculate the direction the arrow faces
        angle = 0;
        alpha = 0.0f;

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
        if (delay > 0) {
            --delay;
            return;
        }
        if (source == location) return;
        duration--;
        if (alpha < 1) alpha += 0.15f;
        if (alpha > 1) alpha = 1.0f;
        angle += 0.2f;

        // alpha
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Margins
        float offX = surface.getScreenX();
        float offY = surface.getScreenY();

        // Take a step
        transform.translate(stepX, stepY);

        // Margin move
        transform.translate(offX, offY);

        // spotlight test
        transform.translate(-Constants.SQUARE_SIZE, -Constants.SQUARE_SIZE);
        g.drawImage(Client.getImages().getImage(Constants.IMG_SPOTLIGHT), transform, frame);
        transform.translate(Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);


        // Rotate the thingy
        transform.rotate(angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // Put it on the screen
        g.drawImage(Client.getImages().getImage(image), transform, frame);

        // Unrotate
        transform.rotate(-angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // draw image 2
        g.drawImage(Client.getImages().getImage(image2), transform, frame);

        // Unmargin
        transform.translate(-offX, -offY);

        g.setComposite(original);

        if (finished()) {
            Client.getImages().playSound(Constants.SOUND_JUDGEMENT);
            Client.getGameData().add(new AnimationImages(location, location, Constants.IMG_BURST_1, 20, 0));
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
