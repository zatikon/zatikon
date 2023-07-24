///////////////////////////////////////////////////////////////////////
// Name: CooperativeButton
// Desc: Play the computer, together
// Date: 8/24/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class CooperativeButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean inside = false;
    private final RosterText rosterText;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CooperativeButton(LeoContainer parent, int x, int y, int width, int height) {
        super(x, y, width, height);
        rosterText = new RosterText(parent,
                "Enter a queue to play in a cooperative game with another player against the Artificial Opponent.",
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
            } else { //Client.getNetManager().initializeGame();
                //Client.getGameData().initialize();
                Client.getNetManager().requestCooperative();
                Client.getGameData().screenLoading("Searching for an ally, please wait...");
            }
            return true;
        } catch (Exception e) {
            System.out.println("CooperativeButton.clickAt(): " + e);
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
            g.drawImage(Client.getImages().getImage(Constants.IMG_COOPERATIVE), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            g.drawImage(Client.getImages().getImage(Constants.IMG_COOPERATIVE_RED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        }
        //g.drawImage(Client.getImages().getImage(Constants.IMG_COOPERATIVE_TEXT), getScreenX(), getScreenY(), mainFrame);
        drawText(g, "Cooperative", getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);
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
