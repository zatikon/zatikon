///////////////////////////////////////////////////////////////////////
// Name: AnimationImages
// Desc: An animation for displaying non-damage icons
// Date: 7/27/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;


public class AnimationImages implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final int maxDuration;
    private final short source;
    private final short location;
    private final int image;
    private int offset = 0;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationImages(short newSource, short newLocation, int newImage, int newDuration, int newOffset) {
        source = newSource;
        location = newLocation;
        image = newImage;
        maxDuration = newDuration;
        offset = newOffset;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        // Get the x and y for the splat
        int x = ((BattleField.getX(location) + offset) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = ((BattleField.getY(location) + offset) * Constants.SQUARE_SIZE) + surface.getScreenY();

        g.drawImage(Client.getImages().getImage(image + duration), x, y, frame);
        duration++;
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration >= maxDuration;
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
