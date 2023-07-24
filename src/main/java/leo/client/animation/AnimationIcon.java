///////////////////////////////////////////////////////////////////////
// Name: AnimationIcon
// Desc: An animation for displaying non-damage icons
// Date: 7/11/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;


public class AnimationIcon implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final int maxDuration;
    private final short source;
    private final short location;
    private final int image;
    private float alpha;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationIcon(short newSource, short newLocation, int newImage) {
        source = newSource;
        location = newLocation;
        image = newImage;
        alpha = 0.0f;
        duration = 30;
        maxDuration = 30;
    }

    public AnimationIcon(short newSource, short newLocation, int newImage, int newDuration) {
        source = newSource;
        location = newLocation;
        image = newImage;
        alpha = 0.0f;
        duration = newDuration;
        maxDuration = newDuration;
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

        if (alpha < 1 && duration > (maxDuration - 10)) alpha += 0.1f;
        if (duration < 10) alpha -= 0.1f;
        if (alpha > 1) alpha = 1.0f;
        if (alpha < 0) alpha = 0.0f;

        // The x and y for the source
        int sx = (BattleField.getX(source) * Constants.SQUARE_SIZE) + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
        int sy = (BattleField.getY(source) * Constants.SQUARE_SIZE) + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

        // Get the x and y for the splat
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();

        g.setColor(Color.black);
        //g.drawLine(sx, sy, x+Constants.SQUARE_SIZE/2, y+Constants.SQUARE_SIZE/2);

        // alpha
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.drawImage(Client.getImages().getImage(image), x, y, frame);

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
