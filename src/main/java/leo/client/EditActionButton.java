///////////////////////////////////////////////////////////////////////
// Name: EditActionButton
// Desc: A basic leopold container
// Date: 7/7/2003 - Gabe Jones
//		 12/2/2010 - Tony Schwartz
//		 Added code to make all buttons with only one line of text half the size
//		 of the regular button.
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditActionButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int FONT_SPACE = Client.FONT_HEIGHT + 3;
    private static final int MARGIN = 1;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action action;
    private final Unit unit;
    private int oldX;
    private int oldY;
    private boolean showingHidden = false;
    private final boolean showHidden;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight(Action newAction) {
        if (newAction.getCostDescription().length() < 1) {
            //System.out.println(((Client.FONT_HEIGHT * 2) + (EditCastlePanel.MARGIN * 5))/2);
            return ((Client.FONT_HEIGHT * 2) + (EditCastlePanel.MARGIN * 5)) / 2;        //22
        } else {
            //System.out.println(((Client.FONT_HEIGHT * 2) + (EditCastlePanel.MARGIN * 5)));
            return (Client.FONT_HEIGHT * 2) + (EditCastlePanel.MARGIN * 5);   //    20+25
        }
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditActionButton(boolean newShowHidden, Unit newUnit, Action newAction, EditButtonPanel buttonPanel) {
        super(0, buttonPanel.getButtonPosition(), buttonPanel.getWidth(), EditActionButton.getButtonHeight(newAction));
        action = newAction;
        unit = newUnit;
        showHidden = newShowHidden;
    }

    /////////////////////////////////////////////////////////////////
// Access the button's action
/////////////////////////////////////////////////////////////////
    public Action getAction() {
        return action;
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int plus = 1;

        Image img;
        if (action.getRangeDescription().length() < 1 && action.getCostDescription().length() < 1)
            img = Client.getImages().getImage(Constants.IMG_DEPLOY);
        else if (action.passive())
            img = Client.getImages().getImage(Constants.IMG_ACTION_DISABLE);
        else if (unit.getAttack() == action)
            img = Client.getImages().getImage(Constants.IMG_ACTION_DISABLE);
        else if (unit.getMove() == action)
            img = Client.getImages().getImage(Constants.IMG_ACTION_DISABLE);
        else
            img = Client.getImages().getImage(Constants.IMG_ACTION_DISABLE);

        if (action.getRangeDescription().length() < 1 && action.getCostDescription().length() < 1)
            g.drawImage(img, getScreenX(), getScreenY(), 238, 24, mainFrame);
        else
            g.drawImage(img, getScreenX(), getScreenY(), 238, 49, mainFrame);

        float alpha = action.passive() ? 0.15f : 0.35f;
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Image color = Client.getImages().getImage(Action.TYPE_COLOR[action.getType()]);
        if (action.getCostDescription().length() < 1)
            g.drawImage(color, getScreenX() + 4, getScreenY() + 4, 229, 21, mainFrame);
        else
            g.drawImage(color, getScreenX() + 4, getScreenY() + 4, 229, 40, mainFrame);
        g.setComposite(original);

        //if (action.getRangeDescription().length() < 1 && action.getCostDescription().length() < 1)
        //g.drawImage(img, getScreenX(), getScreenY(), 238, 24, mainFrame);
        if (!action.passive()) {
            if (action.getRangeDescription().length() < 1 && action.getCostDescription().length() < 1) ;
            else
                g.drawImage(Client.getImages().getImage(Constants.IMG_ACTION_OVERLAY), getScreenX(), getScreenY(), 238, 49, mainFrame);
        }

        g.setColor(Color.white);

        if (action.getRangeDescription().length() < 1) {
            if (action.getCostDescription().length() < 1) {
                //g.drawImage(img, getScreenX(), getScreenY(), 238, 24, mainFrame);
                g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + ((getHeight() / 2) + (Client.FONT_HEIGHT / 2)));

            } else {
                g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + Client.FONT_HEIGHT + SideBoard.MARGIN);
                g.drawString(action.getCostDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + (Client.FONT_HEIGHT + SideBoard.MARGIN) * 2);
            }
        } else {
            g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + MARGIN + FONT_SPACE);
            g.drawString(action.getRangeDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + MARGIN + (FONT_SPACE) * 2);
            g.drawString(action.getCostDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + plus + MARGIN + (FONT_SPACE) * 3);
        }
        if (showHidden &&
                isWithin(Client.getGameData().getMouseX(),
                        Client.getGameData().getMouseY())) {


            if (Client.getGameData().getMouseX() == oldX &&
                    Client.getGameData().getMouseY() == oldY)
                showingHidden = true;

            if (showingHidden) {
                Client.getGameData().getHiddenUnitStats().initialize(action);
                Client.getGameData().getHiddenUnitStats().draw(g, mainFrame);
            }
            oldX = Client.getGameData().getMouseX();
            oldY = Client.getGameData().getMouseY();
        } else
            showingHidden = false;


    }
}
