///////////////////////////////////////////////////////////////////////
// Name: PopText
// Desc: Popup text
// Date: 6/7/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;
import java.util.Vector;


public class PopText extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int MARGIN = 13;
    private static final int TAB = 20;
    private static final int RIGHT = 135;
    private static final int MARGIN_TEXT = MARGIN * 2;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Vector<String> lines = null;
    private int lineCount = 1;
    private final String text;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PopText(LeoContainer newParent, String newText) {
        super(newParent.getWidth() / 3, newParent.getHeight() / 3, newParent.getWidth() / 3, newParent.getHeight() / 3);
        text = newText;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            if (lines == null) {
                lines = new SplitString(text, getWidth() - (TAB * 2), g).getStrings();
                lineCount = lines.size();
            }

            Image img = Client.getImages().getImage(Constants.IMG_STAT_PANEL);
            g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

            // Draw the title
            g.setColor(Color.white);
            g.setFont(Client.getFontBold());

            // set back to normal font font land
            g.setFont(Client.getFont());
            g.setColor(Color.white);

            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.elementAt(i), getScreenX() + TAB, getScreenY() + MARGIN_TEXT + (i * 15));
            }

        } catch (Exception e) {
        }
    }


    public int getHeight() {
        return (MARGIN * 2) + (lineCount * 15);
    }
}
