///////////////////////////////////////////////////////////////////////
// Name: ActionDescription
// Desc: A description of an action
// Date: 3/14/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;
import java.util.Vector;


public class ActionDescription extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int MARGIN = 25;
    private static final int TAB = 10;
    private static final int RIGHT = 135;
    private static final int MARGIN_TEXT = MARGIN * 2;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Action action;
    private Vector<String> lines = null;
    private int lineCount = 1;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionDescription(LeoContainer newParent) {
        super(EditCastlePanel.MARGIN, EditCastlePanel.MARGIN, newParent.getWidth() - (EditCastlePanel.MARGIN * 2), 1);
    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Action newAction) {
        action = newAction;
        lines = null;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {

            if (lines == null) {
                lines = new SplitString(action.getDetail(), getWidth() - (TAB * 2), g).getStrings();
                lineCount = lines.size();
            }


            Image img = Client.getImages().getImage(Constants.IMG_STAT_PANEL);
            g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

            // Draw the title
            g.setColor(Color.white);
            g.setFont(Client.getFontBold());


            g.setColor(Color.black);
            g.drawString(action.getName(), getScreenX() + TAB - 1, getScreenY() + MARGIN);
            g.drawString(action.getName(), getScreenX() + TAB - 3, getScreenY() + MARGIN);
            g.drawString(action.getName(), getScreenX() + TAB - 2, getScreenY() + MARGIN - 1);
            g.drawString(action.getName(), getScreenX() + TAB - 2, getScreenY() + MARGIN + 1);

            g.setColor(Color.white);
            g.drawString(action.getName(), getScreenX() + TAB - 2, getScreenY() + MARGIN);

            if (action.getType() != Action.OTHER) {
                // draw the type name
                g.setColor(Color.black);
                g.drawString("Type: " + Action.TYPE_NAME[action.getType()], getScreenX() + TAB + RIGHT + 1, getScreenY() + MARGIN);
                g.drawString("Type: " + Action.TYPE_NAME[action.getType()], getScreenX() + TAB + RIGHT - 1, getScreenY() + MARGIN);
                g.drawString("Type: " + Action.TYPE_NAME[action.getType()], getScreenX() + TAB + RIGHT, getScreenY() + MARGIN + 1);
                g.drawString("Type: " + Action.TYPE_NAME[action.getType()], getScreenX() + TAB + RIGHT, getScreenY() + MARGIN - 1);
                g.setColor(getColor(action.getType()));
                g.drawString("Type: " + Action.TYPE_NAME[action.getType()], getScreenX() + TAB + RIGHT, getScreenY() + MARGIN);
            }

            // set back to normal font font land
            g.setFont(Client.getFont());
            g.setColor(Color.white);

            for (int i = 0; i < lines.size(); i++) {
                g.drawString(lines.elementAt(i), getScreenX() + TAB, getScreenY() + MARGIN_TEXT + (i * 15));


            }

        } catch (Exception e) {
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the color
    /////////////////////////////////////////////////////////////////
    private Color getColor(int type) {
        switch (type) {
            case Action.ATTACK:
                return Color.red;

            case Action.MOVE:
                return Color.blue;

            case Action.SPELL:
                return Color.magenta;

            case Action.SKILL:
                return Color.green;

            case Action.OTHER:
                return Color.darkGray;
        }
        return Color.white;
    }

    public int getHeight() {
        return (MARGIN * 2) + (lineCount * 15);
    }
}
