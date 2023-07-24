///////////////////////////////////////////////////////////////////////
// Name: AnimationLightning
// Desc: An animation for displaying streaks of lightning
// Date: 8/21/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;


public class AnimationLightning implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private short duration = 15;
    private final short source;
    private final short location;
    private final BasicStroke stroke = new BasicStroke(2);

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationLightning(short newSource, short newLocation) {
        source = newSource;
        location = newLocation;
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

        Stroke tmp = g.getStroke();
        //g.setColor(Color.blue);
        g.setColor(Color.cyan);
        g.setStroke(stroke);

        int slopeX = 0;
        int slopeY = 0;
        int targetX = BattleField.getX(location);
        int targetY = BattleField.getY(location);
        short myX = BattleField.getX(source);
        short myY = BattleField.getY(source);
        //int lastX = (myX*Constants.SQUARE_SIZE) + surface.getScreenX() + Client.getRandom().nextInt(Constants.SQUARE_SIZE);
        //int lastY = (myY*Constants.SQUARE_SIZE) + surface.getScreenY() + Client.getRandom().nextInt(Constants.SQUARE_SIZE);

        int lastX = (myX * Constants.SQUARE_SIZE) + surface.getScreenX() + (Constants.SQUARE_SIZE / 2);
        int lastY = (myY * Constants.SQUARE_SIZE) + surface.getScreenY() + (Constants.SQUARE_SIZE / 2);


        if (targetX > myX) slopeX = 1;
        if (targetX < myX) slopeX = -1;
        if (targetY > myY) slopeY = 1;
        if (targetY < myY) slopeY = -1;

        do {
            myX += slopeX;
            myY += slopeY;
            int x = (myX * Constants.SQUARE_SIZE) + surface.getScreenX() + Client.getRandom().nextInt(Constants.SQUARE_SIZE);
            int y = (myY * Constants.SQUARE_SIZE) + surface.getScreenY() + Client.getRandom().nextInt(Constants.SQUARE_SIZE);
            g.drawLine(x, y, lastX, lastY);
            lastX = x;
            lastY = y;
        } while (BattleField.getLocation(myX, myY) != location);

        g.setColor(Color.black);
        g.setStroke(tmp);

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
