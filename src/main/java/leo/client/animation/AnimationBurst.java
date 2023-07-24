///////////////////////////////////////////////////////////////////////
// Name: AnimationBurst
// Desc: A burst of particles
// Date: 12/12/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;


public class AnimationBurst implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int X = 0;
    private static final int Y = 1;
    private static final int PLUS_X = 2;
    private static final int PLUS_Y = 3;
    private static final int VALUE_COUNT = 4;
    private static final int SPEED_RANGE = 20;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private short duration = 10;
    private final short location;
    private final int particleCount;
    private final int[][] bursts;
    private final int image;
    private float alpha = 1.0f;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationBurst(short newLocation, int newParticleCount, int newImage) {
        location = newLocation;
        particleCount = newParticleCount;
        image = newImage;

        // Intialize the bursts
        bursts = new int[particleCount][VALUE_COUNT];
        int targetX = BattleField.getX(location);
        int targetY = BattleField.getY(location);
        targetX = (targetX * Constants.SQUARE_SIZE);
        targetY = (targetY * Constants.SQUARE_SIZE);
        for (int i = 0; i < particleCount; i++) {
            bursts[i][X] = targetX;
            bursts[i][Y] = targetY;
            bursts[i][PLUS_X] = Client.getRandom().nextInt(SPEED_RANGE) - (SPEED_RANGE / 2);
            bursts[i][PLUS_Y] = Client.getRandom().nextInt(SPEED_RANGE) - (SPEED_RANGE / 2);
        }
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
        alpha -= 0.1f;
        if (alpha < 0) alpha = 0;

        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        // Update the coordinates
        for (int i = 0; i < particleCount; i++) {
            bursts[i][X] += bursts[i][PLUS_X];
            bursts[i][Y] += bursts[i][PLUS_Y];
            g.drawImage(
                    Client.getImages().getImage(image),
                    bursts[i][X] + surface.getScreenX(),
                    bursts[i][Y] + surface.getScreenY(),
                    frame);
        }
        g.setComposite(original);
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration < 0;
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
