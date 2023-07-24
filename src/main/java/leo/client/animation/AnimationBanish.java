///////////////////////////////////////////////////////////////////////
// Name: AnimationBanish
// Desc: Banish the unit to the castle
// Date: 7/3/2008 - Gabe Jones
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

public class AnimationBanish implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final short source;
    private final short location;
    private final int x;
    private final int y;
    private int stepX;
    private int stepY;
    private final int image;
    private final AffineTransform transform = new AffineTransform();
    private double angle;
    private final Unit victim;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationBanish(Unit newVictim, short newSource, short newLocation) {
        victim = newVictim;
        location = newLocation;

        image = (victim.getAppearance() +
                (victim.getCastle() ==
                        Client.getGameData().getMyCastle() ?
                        0 :
                        1));


        source = newSource;

        // Build coordinates
        int x1 = BattleField.getX(source);
        int y1 = BattleField.getY(source);
        int x2 = BattleField.getX(location);
        int y2 = BattleField.getY(location);

        // The greatest distance
        int distance = BattleField.getDistance(source, location);

        // Calculate the movement increment
        stepX = x2 - x1;
        stepY = y2 - y1;
        stepX *= (Constants.STEP_SPEED * 2);
        stepY *= (Constants.STEP_SPEED * 2);
        stepX /= distance;
        stepY /= distance;

        // Initialize starting location
        x = (x1 * Constants.SQUARE_SIZE);
        y = (y1 * Constants.SQUARE_SIZE);
        transform.translate(x, y);

        // Calculate the direction the arrow faces
        angle = 0;

        // How long should this animation play
        duration = calculateDuration(distance, (Math.abs(stepX) > Math.abs(stepY) ? Math.abs(stepX) : Math.abs(stepY)));

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
        duration--;
        angle += 0.4f;

        // Margins
        int offX = surface.getScreenX();
        int offY = surface.getScreenY();

        // Take a step
        transform.translate(stepX, stepY);

        // Margin move
        transform.translate(offX, offY);

        // Rotate the arrow
        transform.rotate(angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // Put it on the screen
        g.drawImage(Client.getImages().getImage(image), transform, frame);

        // Unrotate
        transform.rotate(-angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // Unmargin
        transform.translate(-offX, -offY);

        if (finished()) {
            victim.setHidden(false);
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
