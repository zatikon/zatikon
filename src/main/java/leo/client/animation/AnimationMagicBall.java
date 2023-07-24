///////////////////////////////////////////////////////////////////////
// Name: AnimationMagicBall
// Desc: An animation for shooting a purple magic ball
// Date: 12/18/2007 - Gabe Jones
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

public class AnimationMagicBall implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final short source;
    private final short location;
    private int x;
    private int y;
    private int stepX;
    private int stepY;
    private final int image;
    private final AffineTransform transform = new AffineTransform();
    private double angle;
    private final short damage;
    private final Unit victim;
    private AnimationImage ghost = null;
    private int animationStep = 0;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationMagicBall(short newSource, short newLocation, short newDamage, Unit newVictim) {
        source = newSource;
        location = newLocation;
        damage = newDamage;
        victim = newVictim;
        image = Constants.IMG_MAGIC_BALL_1;

        if (source == location) {
            duration = 0;
            return;
        }

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
        stepX *= (Constants.STEP_SPEED * 3);
        stepY *= (Constants.STEP_SPEED * 3);
        stepX /= distance;
        stepY /= distance;

        // Initialize starting location
        x = (x1 * Constants.SQUARE_SIZE);
        y = (y1 * Constants.SQUARE_SIZE);
        transform.translate(x, y);

        // Calculate the direction the arrow faces
        angle = Math.atan2((y2 - y1), (x2 - x1));

        // How long should this animation play
        duration = calculateDuration(distance, (Math.abs(stepX) > Math.abs(stepY) ? Math.abs(stepX) : Math.abs(stepY)));

        if (victim.isDead()) {
            int appearance = (victim.getAppearance() +
                    (victim.getCastle() ==
                            Client.getGameData().getMyCastle() ?
                            0 :
                            1));


            ghost = new AnimationImage(location, appearance);
            Client.getGameData().insert(ghost);
        }

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
        if (source != location) {
            duration--;

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
            g.drawImage(Client.getImages().getImage(image + animationStep), transform, frame);

            // Unrotate
            transform.rotate(-angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

            // Unmargin
            transform.translate(-offX, -offY);

            // Update the animation
            if (animationStep < 5) animationStep++;
        }

        // If done, make the splat
        if (finished()) {
            if (ghost != null) {
                ghost.finish();
                Client.getGameData().insert(new AnimationDeath(victim));


            }
            Client.getGameData().add(new AnimationBurst(location, 5, Constants.IMG_PURPLE_BALL));
            Client.getGameData().add(new AnimationDamage(source, location, damage, victim));
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
