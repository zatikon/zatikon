///////////////////////////////////////////////////////////////////////
// Name: ChatScrollBar
// Desc: Scroll bar for chat window
// Date: 8/25/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ChatScrollBar extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    private final int UPPER_BOUND = 48;
    private final int LOWER_BOUND = 508;
    private final int X = 205;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private final PlayerPanel parent;
    private final ChatBox chatbox;
    private int location = UPPER_BOUND;
    private boolean mouseControl = false;
    private int offset = 0;
    private int mouseX = 0;
    private int mouseY = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatScrollBar(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;
        chatbox = parent.getChatBox();
    }

    /////////////////////////////////////////////////////////////////
    // Public Methods
    /////////////////////////////////////////////////////////////////

    public boolean clickAt(int x, int y) {
        return true;
    }

    // Make sure the scroll bar doesn't go anywhere crazy
    private void bounds() {
        if (location >= LOWER_BOUND) location = LOWER_BOUND;
        if (location <= UPPER_BOUND) location = UPPER_BOUND;
        return;
    }

    // Switching into drag mode, where the scroll bar now controls the location in the text/player list
    private void dragBar() {
        int boxLocation = chatbox.getLocation();
        int boxLength = chatbox.getLength();
        int boxMaxLength = chatbox.getMaxLength();

        // If there isn't enough text to be scrolling through, just return
        if (boxMaxLength >= boxLength) {
            chatbox.setLocation(0);
            return;
        }

        // Basically, the scroll bar's relationship with the top and bottom is applied to the text ( rounding to the nearest line )
        chatbox.setLocation((int) (((double) (location - UPPER_BOUND) / (double) (LOWER_BOUND - UPPER_BOUND)) * (double) (boxLength - boxMaxLength)));
    }

    // When the position in the text/player list determines the scroll bar's position (aka when not dragging the scroll bar)
    private void moveBar() {
        int boxLocation = chatbox.getLocation();
        int boxLength = chatbox.getLength();
        int boxMaxLength = chatbox.getMaxLength();

        // Again, no need if there isn't enough text
        if (boxMaxLength >= boxLength) {
            location = UPPER_BOUND;
            return;
        }

        // Basically the reverse of the equation in dragBar(), the scroll bar's position is based on the position in the text/player list
        location = (int) ((double) LOWER_BOUND - (((double) (boxLength - (boxLocation + boxMaxLength)) / (double) (boxLength - boxMaxLength)) * (double) (LOWER_BOUND - UPPER_BOUND)));
    }

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

    // special function for iswithin because scrollbar object never technically moves
    private boolean iswithin(int x, int y) {
        return ((x >= getScreenX() + X && x <= (getScreenX() + X + getWidth())) && (y >= getScreenY() + location && y <= (getScreenY() + location + getHeight())));
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////

    public void draw(Graphics2D g, Frame mainFrame) {
        if (mouseControl) {
            //g.drawString("mouseY = " + mouseY, getScreenX()+120, getScreenY()+500);
            location = mouseY - getScreenY();
            bounds();
            dragBar();
        } else {
            moveBar();
        }
        g.drawImage(Client.getImages().getImage(Constants.IMG_SCROLL_BAR), getScreenX() + X, getScreenY() + location, getWidth(), getHeight(), mainFrame);
    }
}
