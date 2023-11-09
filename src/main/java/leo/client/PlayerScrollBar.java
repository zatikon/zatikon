///////////////////////////////////////////////////////////////////////
// Name: PlayerScrollBar
// Desc: Scroll bar for player list window
// Date: 8/26/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.*;


public class PlayerScrollBar extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final PlayerPanel parent;
    private final PlayerList players;
    private final int UPPER_BOUND = 48;
    private final int LOWER_BOUND = 530;
    private int location = UPPER_BOUND;
    private boolean mouseControl = false;
    private final int X = 205;
    private int offset = 0;
    private int mouseX = 0;
    private int mouseY = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PlayerScrollBar(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;
        players = parent.getPlayerList();
    }


    // doesn't use clickat because the object is technically at 0,0
    public boolean clickAt(int x, int y) {
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Functions for controlling bar movement
    /////////////////////////////////////////////////////////////////

    // makes sure it doesn't go out of bounds
    private void bounds() {
        if (location >= LOWER_BOUND) location = LOWER_BOUND;
        if (location <= UPPER_BOUND) location = UPPER_BOUND;
        return;
    }

    // for moving the list when bar is dragged
    private void dragBar() {
        int playerLocation = players.getLocation();
        int playerLength = players.getLength();
        int playerMaxLength = players.getMaxLength();

        if (playerMaxLength >= playerLength) {
            players.setLocation(0);
            return;
        }

        players.setLocation((int) (((double) (location - UPPER_BOUND) / (double) (LOWER_BOUND - UPPER_BOUND)) * (double) (playerLength - playerMaxLength)));
    }

    // for moving the bar automatically with the text
    private void moveBar() {
        int playerLocation = players.getLocation();
        int playerLength = players.getLength();
        int playerMaxLength = players.getMaxLength();

        if (playerMaxLength >= playerLength) {
            location = UPPER_BOUND;
            return;
        }

        location = (int) ((double) LOWER_BOUND - (((double) (playerLength - (playerLocation + playerMaxLength)) / (double) (playerLength - playerMaxLength)) * (double) (LOWER_BOUND - UPPER_BOUND)));
    }

    /////////////////////////////////////////////////////////////////
    // Mouse Events
    /////////////////////////////////////////////////////////////////
    public void mousePressed() {
        if (iswithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            offset = Client.getGameData().getMouseY() - location - getScreenY();
            mouseY = Client.getGameData().getMouseY() - offset;
            mouseControl = true;
        }
    }

    public void mouseReleased() {
        mouseControl = false;
        mouseY = location;
    }

    public void mouseDragged(int x, int y) {
        mouseX = x;
        mouseY = y - offset;
    }

    // special isWithing for scroll bar
    private boolean iswithin(int x, int y) {
        return ((x >= getScreenX() + X && x <= (getScreenX() + X + getWidth())) && (y >= getScreenY() + location && y <= (getScreenY() + location + getHeight())));
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            if (mouseControl) {
                //g.drawString("mouseY = " + mouseY, getScreenX()+120, getScreenY()+500);
                location = mouseY - getScreenY();
                bounds();
                dragBar();
            } else {
                moveBar();
            }
            g.drawImage(Client.getImages().getImage(Constants.IMG_SCROLL_BAR), getScreenX() + X, getScreenY() + location, getWidth(), getHeight(), mainFrame);
        } catch (Exception e) {
            Logger.error("PlayerScrollBar.draw(): " + e);
        }
    }
}
