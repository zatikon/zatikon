///////////////////////////////////////////////////////////////////////
//	Name:	RosterText
//	Desc:	Popup text
//	Date:	3/7/2010 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import java.awt.*;
import java.util.Vector;


public class RosterText extends LeoComponent {

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
    private String text;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RosterText(LeoContainer newParent, String newText, int x, int y, int width, int height) {
        super(x, y, width, height);
        text = newText;
        setParent(newParent);
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

            // Draw the title
            //g.setColor(Color.white);
            //g.setFont(Client.getFontBold());

            // set back to normal font font land
            g.setFont(Client.getFont());
            g.setColor(Color.white);

            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.elementAt(i), getScreenX() + TAB, getScreenY() + MARGIN_TEXT + (i * 13));
            }

        } catch (Exception e) {
        }
    }

    public void setText(String newText) {
        text = newText;
        lines = null;
    }


    public int getHeight() {
        return (MARGIN * 2) + (lineCount * 15);
    }
}
