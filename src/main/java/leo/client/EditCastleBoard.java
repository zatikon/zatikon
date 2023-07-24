///////////////////////////////////////////////////////////////////////
// Name: EditCastleBoard
// Desc: The board showing the contents of your castle
// Date: 7/02/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class EditCastleBoard extends LeoContainer {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MARGIN = 4;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final String message;
    protected EditCastlePanel panel;
    private final int image;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditCastleBoard(EditCastlePanel newPanel, String newMessage, int x, int y, int width, int height, int newImage) {
        super(x, y, width, height);
        message = newMessage;
        panel = newPanel;
        image = newImage;
    }


    /////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        return message;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + Constants.OFFSET + Client.FONT_HEIGHT - 2;

        if (image >= 0) {
            Image img = Client.getImages().getImage(image);
            g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
        }

        //g.setColor(Color.white);
        //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);

        // Neat gray bar
        //if (message.length() > 0)
        //{ g.setColor(Color.lightGray);
        // g.fillRect(getScreenX(), getScreenY(), getWidth()-1, Client.FONT_HEIGHT + (Constants.OFFSET*2));
        // g.setColor(Color.black);
        // g.drawRect(getScreenX(), getScreenY(), getWidth()-1, Client.FONT_HEIGHT + (Constants.OFFSET*2));
        //}

        g.setColor(Color.black);
        //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
        //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);
        g.drawString(getMessage(), atX, atY);
        super.draw(g, mainFrame);
    }
}
