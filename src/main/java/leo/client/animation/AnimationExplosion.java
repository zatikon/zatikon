///////////////////////////////////////////////////////////////////////
// Name: AnimationExplosion
// Desc: An animation for displaying a big fiery explosion
// Date: 8/20/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;


public class AnimationExplosion implements Animation {
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private short lead = 0;
    private short duration = 0;
    private final short source;
    private final short location;
    private final int image;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationExplosion(short newSource, short newLocation, int newImage) {
        source = newSource;
        location = newLocation;
        image = newImage;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        switch (lead++) {
            case 0:
                break;
            //return;
            case 1:
                break;
            //return;
        }
        duration++;


        // The x and y for the source
        int sx = (BattleField.getX(source) * Constants.SQUARE_SIZE) + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
        int sy = (BattleField.getY(source) * Constants.SQUARE_SIZE) + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

        // For the middle
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();

        //g.setColor(Color.black);
        //g.drawLine(sx, sy, x+Constants.SQUARE_SIZE/2, y+Constants.SQUARE_SIZE/2);

        g.drawImage(Client.getImages().getImage(image + duration), x - Constants.SQUARE_SIZE, y - Constants.SQUARE_SIZE, frame);

    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration >= 15;
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
