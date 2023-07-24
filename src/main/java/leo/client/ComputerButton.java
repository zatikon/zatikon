///////////////////////////////////////////////////////////////////////
// Name: ComputerButton
// Desc: Play the computer
// Date: 11/12/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ComputerButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean inside = false;
    private boolean tut = false;
    private final RosterText rosterText;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ComputerButton(LeoContainer parent, int x, int y, int width, int height, boolean newTut) {
        super(x, y, width, height);
        tut = newTut;
        rosterText = new RosterText(parent,
                "Play a single player game against the Artificial Opponent using your presently configured army. Each game you win, the difficulty increases. Every two you lose, it decreases.",
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
                Client.getNetManager().requestPractice();
                Client.getGameData().screenLoading("Loading the Artificial Opponent...");
            }
            return true;
        } catch (Exception e) {
            System.out.println("ComputerButton.clickAt(): " + e);
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
            g.drawImage(Client.getImages().getImage(Constants.IMG_SINGLE), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            g.drawImage(Client.getImages().getImage(Constants.IMG_SINGLE_RED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        }
        if (!tut) { //g.drawImage(Client.getImages().getImage(Constants.IMG_SINGLE_TEXT), getScreenX(), getScreenY(), mainFrame);
            drawText(g, "Single Player", getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);
        } else {
            drawText(g, "Start Here!", getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);
        }
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
