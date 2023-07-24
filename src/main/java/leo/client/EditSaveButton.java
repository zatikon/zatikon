///////////////////////////////////////////////////////////////////////
// Name: EditSaveButton
// Desc: Save and exit
// Date: 7/7/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class EditSaveButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditSaveButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        Client.setComputing(false);
        Client.getGameData().screenMessage("Please wait while uploading army...");
        Client.getNetManager().sendCastle();
        Client.getNetManager().getArmyUnits();
        Client.getGameData().screenRoster();
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        return "Save and Exit";
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        Image img;

        // If the mouse is within the bounds, darken
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON_HIGHLIGHT);
        } else {
            img = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON);
        }
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        g.setColor(Color.white);
        g.drawString(getMessage(), atX, atY);
    }
}
