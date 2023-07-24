///////////////////////////////////////////////////////////////////////
// Name: CancelButton
// Desc: Cancel out of a game
// Date: 7/27/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;


public class CancelButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean pushed = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CancelButton(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (pushed) return true;
        pushed = true;
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        Client.setComputing(false);
        Client.getNetManager().sendAction(Action.CANCEL);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the text
    /////////////////////////////////////////////////////////////////
    private String getText() {
        return pushed ? "Cancelling..." : "Cancel";
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getText()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        if (pushed)
            g.setColor(Color.red);
        else
            g.setColor(Color.white);

        g.fillRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.setColor(Color.black);

        // If the mouse is within the bounds, darken
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && !pushed) {
            g.drawRect(getScreenX() - 1, getScreenY() - 1, getWidth() + 1, getHeight() + 1);
        }

        g.drawRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.drawRect(getScreenX() + 1, getScreenY() + 1, getWidth() - 3, getHeight() - 3);
        g.drawString(getText(), atX, atY);
    }
}
