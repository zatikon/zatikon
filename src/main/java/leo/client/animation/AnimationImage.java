///////////////////////////////////////////////////////////////////////
// Name: AnimationImage
// Desc: An animation for displaying non-damage icons
// Date: 11/28/2007 - Gabe Jones
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


public class AnimationImage implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private boolean active = true;
    private final short location;
    private final int image;
    private Unit unit = null;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationImage(short newLocation, int newImage) {
        location = newLocation;
        image = newImage;
    }

    public AnimationImage(short newLocation, int newImage, Unit newUnit) {
        location = newLocation;
        image = newImage;
        unit = newUnit;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        //if (delay > 0) {
        //	 --delay;
        //return;
        //}
        // Get the x and y for the splat
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();
        if (unit != null) {
            x += unit.readStepX();
            y += unit.readStepY();
            unit.getStepX();
            unit.getStepY();
        }
        g.drawImage(Client.getImages().getImage(image), x, y, frame);


        if (unit != null && unit.isMechanical()) {
            int mappear = unit.getAppearance();
            Image mimage = unit.isStunned()
                    ? Client.getImages().getRotatedImage(mappear, unit.getCastle() == Client.getGameData().getMyCastle())
                    : Client.getImages().getGrayedImage(mappear, unit.getCastle() == Client.getGameData().getMyCastle());

            Composite original = g.getComposite();
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.6f));
            g.drawImage(mimage, x, y, frame);
            g.setComposite(original);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return !active;
    }


    /////////////////////////////////////////////////////////////////
    // Finish
    /////////////////////////////////////////////////////////////////
    public void finish() {
        active = false;
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
