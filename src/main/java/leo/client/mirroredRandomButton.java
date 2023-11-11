///////////////////////////////////////////////////////////////////////
// Name: mirroredRandomButton
// Desc: Play the game with same random army
// Date: 10/13/2010 - Tony Schwartz
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.*;


public class mirroredRandomButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean inside = false;
    private final RosterText rosterText;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public mirroredRandomButton(LeoContainer parent, int x, int y, int width, int height) {
        super(x, y, width, height);
        rosterText = new RosterText(parent,
                "Enter a queue to play another player in a one-on-one match using the same randomly generated armies.",
                4, 455, 186, 142);
    }

    /////////////////////////////////////////////////////////////////
    // Click code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        try {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.setComputing(false);
            //Client.getGameData().initialize();
            Client.getNetManager().requestMirrDuel();
            Client.getGameData().screenLoading("Searching for an opponent, please wait...");
            return true;
        } catch (Exception e) {
            Logger.error("mirroredRandomButton.clickAt(): " + e);
            Client.shutdown();
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            rosterText.draw(g, mainFrame);
            if (!inside) {
                Client.getImages().playSound(Constants.SOUND_MOUSEOVER);
            }
            inside = true;
            g.drawImage(Client.getImages().getImage(Constants.IMG_RANDOM), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            g.drawImage(Client.getImages().getImage(Constants.IMG_RANDOM_RED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        }
        //g.drawImage(Client.getImages().getImage(Constants.IMG_RANDOM_TEXT), getScreenX(), getScreenY(), mainFrame);
        drawText(g, "Mirrored Random", getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void drawText(Graphics2D g, String text, int atX, int atY) {
        //if (inside)
        g.setFont(Client.getFontBold());
        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);

        if (inside)
            g.setColor(Color.yellow);
        else
            g.setColor(Color.white);

        g.drawString(text, atX, atY);

        // set it back
        g.setFont(Client.getFont());

    }


    /////////////////////////////////////////////////////////////////
    // Are the coordinates within this component
    /////////////////////////////////////////////////////////////////
    @Override
    public boolean isWithin(int testX, int testY) {
        return (testX >= getScreenX() && testX - 100 <= (getScreenX() + getWidth())) &&
                (testY >= getScreenY() && testY <= (getScreenY() + getHeight()));
    }

}
