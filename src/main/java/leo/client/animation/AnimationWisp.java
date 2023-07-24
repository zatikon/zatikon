///////////////////////////////////////////////////////////////////////
// Name: AnimationWisp
// Desc: An animation for the will-o-the-wisps attack
// Date: 12/19/2007 - Gabe Jones
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

public class AnimationWisp implements Animation {
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
    private final short damage;
    private final Unit victim;
    private final Unit attacker;
    private AnimationImage ghost = null;
    private final int speed;
    private final int imageOwner;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationWisp(short newSource, short newLocation, short newDamage, Unit newAttacker, Unit newVictim) {
        source = newSource;
        location = newLocation;
        damage = newDamage;
        image = Constants.IMG_WILL_O_THE_WISPS;
        attacker = newAttacker;
        victim = newVictim;
        speed = Constants.STEP_SPEED;

        imageOwner = (attacker.getCastle() ==
                Client.getGameData().getMyCastle() ?
                0 :
                1);

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
        stepX *= speed;
        stepY *= speed;
        stepX /= distance;
        stepY /= distance;

        // Initialize starting location
        x = (x1 * Constants.SQUARE_SIZE);
        y = (y1 * Constants.SQUARE_SIZE);
        transform.translate(x, y);

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
        duration--;

        // Margins
        int offX = surface.getScreenX();
        int offY = surface.getScreenY();

        // Take a step
        transform.translate(stepX, stepY);

        // Margin move
        transform.translate(offX, offY);

        // Put it on the screen
        g.drawImage(Client.getImages().getImage(image + imageOwner), transform, frame);

        // Unmargin
        transform.translate(-offX, -offY);

        // If done, make the splat
        if (finished()) {
            if (ghost != null) {
                ghost.finish();
                Client.getGameData().insert(new AnimationDeath(victim));
            }
            Client.getGameData().add(
                    new AnimationBurst(
                            location,
                            5,
                            Constants.IMG_WHITE_BALL + imageOwner));
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
