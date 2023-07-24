///////////////////////////////////////////////////////////////////////
// Name: PlayButton
// Desc: Play the game already
// Date: 6/27/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class PlayButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean inside = false;
    private final RosterText rosterText;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PlayButton(LeoContainer parent, int x, int y, int width, int height) {
        super(x, y, width, height);
        rosterText = new RosterText(parent,
                "Enter a queue to play another player in a one-on-one match using your presently configured armies.",
                4, 455, 186, 142);
    }

    /////////////////////////////////////////////////////////////////
    // Click code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        try {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.setComputing(false);
            if (Client.getGameData().getArmy().depleted()) {
                Client.getGameData().screenEditCastle();
            } else {
                //Client.getGameData().initialize();
                Client.getNetManager().requestGame();
                Client.getGameData().screenLoading("Searching for an opponent, please wait...");
            }
            return true;
        } catch (Exception e) {
            System.out.println("PlayButton().clickAt(): " + e);
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
            g.drawImage(Client.getImages().getImage(Constants.IMG_CONSTRUCTED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            g.drawImage(Client.getImages().getImage(Constants.IMG_CONSTRUCTED_RED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        }
        //g.drawImage(Client.getImages().getImage(Constants.IMG_CONSTRUCTED_TEXT), getScreenX(), getScreenY(), mainFrame);
        drawText(g, "Constructed",  getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);
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
