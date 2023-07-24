///////////////////////////////////////////////////////////////////////
// Name: OkButton
// Desc: Ok to continue
// Date: 6/27/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class OkButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public OkButton(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    /////////////////////////////////////////////////////////////////
    // Click code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        //Client.restart();
        Client.getGameData().screenRoster();
        Client.getImages().playMusic();
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth("Ok") / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        g.setColor(Color.white);
        g.fillRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.setColor(Color.black);

        // If the mouse is within the bounds, darken
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            g.drawRect(getScreenX() - 1, getScreenY() - 1, getWidth() + 1, getHeight() + 1);
        }

        g.drawRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.drawRect(getScreenX() + 1, getScreenY() + 1, getWidth() - 3, getHeight() - 3);
        g.drawString("Ok", atX, atY);
    }
}
