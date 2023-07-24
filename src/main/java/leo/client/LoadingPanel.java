///////////////////////////////////////////////////////////////////////
// Name: LoadingPanel
// Desc: The panel that says wait
// Date: 6/15/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class LoadingPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    private String text;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LoadingPanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));

        add(new CancelButton(
                (getWidth() / 2) - 50,
                getHeight() - 35,
                100,
                25));
    }


    /////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    private String getMessage() {
        return text;
    }


    /////////////////////////////////////////////////////////////////
    // Set the message
    /////////////////////////////////////////////////////////////////
    public void setText(String newText) {
        text = newText;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.setFont(Client.getFontBig());
        String text = getMessage();

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (20 / 2);

        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);
        g.setColor(Color.yellow);

        g.drawString(text, atX, atY);
        g.setFont(Client.getFont());

        super.draw(g, mainFrame);
    }
}
