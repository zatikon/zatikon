///////////////////////////////////////////////////////////////////////
// Name: ListButton
// Desc: List button
// Date: 8/18/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ListButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final PlayerPanel parent;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ListButton(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        parent.listFocus();
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        Color c = g.getColor();
        g.setColor(Color.white);
        int numplayers = parent.getPlayerList().getLength();
        g.drawString("(" + numplayers + ")", getScreenX() + getWidth() - 25, getScreenY() + getHeight() - 10);
        g.setColor(c);
        g.drawImage(Client.getImages().getImage(Constants.IMG_PLAYERS_BUTTON), getScreenX() - 8, getScreenY(), getWidth(), getHeight(), mainFrame);
    }
}
