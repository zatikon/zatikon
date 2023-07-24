///////////////////////////////////////////////////////////////////////
// Name: TimeOutPanel
// Desc: The panel displayed after idle time-out
// Date: 8/3/2011 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class TimeOutPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String text;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TimeOutPanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));

        setText("Idle time out, Zatikon has disconnected");

        // Button to leave
        add(new CloseButton(
                (getWidth() / 2) - 150,
                getHeight() - 35,
                100,
                25));

        // Button to log back in
        add(new ReconnectButton(
                (getWidth() / 2) + 50,
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
