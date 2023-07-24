///////////////////////////////////////////////////////////////////////
// Name: AnimationFireball
// Desc: An animation for displaying non-damage icons
// Date: 11/27/2007 - Gabe Jones
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
import java.util.Iterator;
import java.util.Vector;

public class AnimationFireball implements Animation {
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
    private final Vector<Short> damages;
    private final Vector<Unit> victims;
    private final Vector<AnimationImage> ghosts = new Vector<AnimationImage>();
    private final Vector<Unit> ghostUnits = new Vector<Unit>();
    private final int speed;
    private int delay = 4;

    private int animDelay = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationFireball(short newSource, short newLocation, Vector<Short> newDamages, int newImage, Vector<Unit> newVictims, int newSpeed) {
        source = newSource;
        location = newLocation;
        damages = newDamages;
        image = newImage;
        victims = newVictims;
        speed = newSpeed;

        if (source == location) {
            duration = 0;

            // Add the explosion
            Client.getImages().playSound(Constants.SOUND_EXPLOSION);
            Client.getGameData().add(new AnimationExplosion(source, location, Constants.IMG_EXPLOSION_1));
            for (int i = 0; i < victims.size(); i++) {
                Unit victim = victims.elementAt(i);
                Short damage = damages.elementAt(i);
                Client.getGameData().add(new AnimationDamage(victim.getLocation(), victim.getLocation(), damage.byteValue(), victim, (byte) -1));
            }
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
        stepX *= speed;
        stepY *= speed;
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

        Iterator<Unit> it = victims.iterator();
        while (it.hasNext()) {
            Unit victim = it.next();

            if (victim.isDead()) {
                int appearance = (victim.getAppearance() +
                        (victim.getCastle() ==
                                Client.getGameData().getMyCastle() ?
                                0 :
                                1));
                AnimationImage ghost = new AnimationImage(victim.getLocation(), appearance, victim);
                ghosts.add(ghost);
                ghostUnits.add(victim);
                Client.getGameData().insert(ghost);
            }
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
        if (animDelay >= 0) {
            animDelay--;
            return;
        }

        if (source == location) return;
        if (delay >= 0) {
            delay--;
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

        // Rotate the arrow
        transform.rotate(angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // Put it on the screen
        g.drawImage(Client.getImages().getImage(image), transform, frame);

        // Unrotate
        transform.rotate(-angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);

        // Unmargin
        transform.translate(-offX, -offY);

        // If done, make the splat
        if (finished()) {
            // Add the explosion
            Client.getImages().playSound(Constants.SOUND_EXPLOSION);
            Client.getGameData().add(new AnimationExplosion(source, location, Constants.IMG_EXPLOSION_1));

            for (int i = 0; i < ghosts.size(); i++) {
                AnimationImage ghost = ghosts.elementAt(i);
                Unit victim = ghostUnits.elementAt(i);
                if (ghost != null) {
                    ghost.finish();
                    Client.getGameData().insert(new AnimationDeath(victim));
                }
            }
            for (int i = 0; i < victims.size(); i++) {
                Unit victim = victims.elementAt(i);
                Short damage = damages.elementAt(i);
                Client.getGameData().add(new AnimationDamage(victim.getLocation(), victim.getLocation(), damage.byteValue(), victim, (byte) -1));
            }


        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration <= 0;
    }

    public void setDelay(int d) {
        animDelay = d;
    }

    public short getLocation() {
        return location;
    }

    public void setDirection(short b) {
        return;
    }
}
