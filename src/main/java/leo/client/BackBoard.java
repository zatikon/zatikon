///////////////////////////////////////////////////////////////////////
// Name: LeoContainer
// Desc: A basic leopold container
// Date: 5/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.*;


public class BackBoard extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Color color;
    private boolean queue = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public BackBoard(Color newColor, int newX, int newY, int newWidth, int newHeight) {
        super(newX, newY, newWidth, newHeight);
        color = newColor;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            Image image = null;
            if (Client.getGameData().playing())
                image = Client.getImages().getImage(Constants.IMG_BACK_PANEL_GAME);
            else
                image = Client.getImages().getImage(Constants.IMG_BACK_PANEL);

            g.drawImage(image, getScreenX(), getScreenY(), mainFrame);
            super.draw(g, mainFrame);
        } catch (Exception e) {
            Logger.error("BackBoard.draw(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get x with insets
    /////////////////////////////////////////////////////////////////
    public int getX() {
        return super.getX() + Client.getInsetX();
    }


    /////////////////////////////////////////////////////////////////
    // Get Y with insets
    /////////////////////////////////////////////////////////////////
    public int getY() {
        return super.getY() + Client.getInsetY();
    }


    public void clear() {
        super.clear();
        queue = false;
    }

    public boolean queueing() {
        return queue;
    }

    public void queue() {
        queue = true;
    }
}
