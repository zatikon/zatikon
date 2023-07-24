///////////////////////////////////////////////////////////////////////
// Name: ActionButton
// Desc: An action button
// Date: 5/27/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.TargetType;
import leo.shared.Unit;

import java.awt.*;


public class ActionButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Static properties constants
    /////////////////////////////////////////////////////////////////
    private static final int FONT_SPACE = Client.FONT_HEIGHT + 3;
    private static final int MARGIN = 3;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Action action;
    private int oldX;
    private int oldY;
    private boolean showingHidden = false;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 2) + (SideBoard.MARGIN * 3);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ActionButton(Action newAction, ButtonPanel buttonPanel) {
        super(0, buttonPanel.getButtonPosition(), buttonPanel.getWidth(), ActionButton.getButtonHeight());
        action = newAction;
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        try {
    
   /*if (Client.getGameData().getSelectedUnit().getCastle() == Client.getGameData().getEnemyCastle() || Client.getGameData().getSelectedUnit().getTeam() != Unit.TEAM_1)
   { return false;
   }*/

            if (Client.getGameData().getSelectedUnit().getCastle() != Client.getGameData().getEnemyCastle() &&
                    Client.getGameData().getSelectedUnit().getTeam() == Unit.TEAM_1 &&
                    !Client.getGameData().getSelectedUnit().ready()) {
                return false;
            }


            if (!Client.getGameData().myTurn()) {
                return false;
            }


            if (action.getRemaining() <= 0) {
                return false;
            }

            if ((Client.getGameData().getSelectedUnit().getCastle() == Client.getGameData().getEnemyCastle() ||
                    Client.getGameData().getSelectedUnit().getTeam() != Unit.TEAM_1) &&
                    action.getTargetType() == TargetType.NONE) {
                return false;
            }

            if (action.getTargetType() == TargetType.NONE) {

                // Send the action
                Client.getNetManager().sendAction(
                        action.getOwner().getAction(action),
                        action.getOwner().getLocation(),
                        Action.NOTHING);

                // Do the action myself
                action.perform(Action.NOTHING);

                // Clear the selection
                //Client.getGameData().setSelectedUnit(action.getOwner());
                if (Client.getGameData().getSelectedUnit().isDead())
                    Client.getGameData().setSelectedUnit(null);
                else
                    Client.getGameData().setSelectedUnit(Client.getGameData().getSelectedUnit());
                return true;
            }

            if (Client.getGameData().getSelectedUnit().getMove() == action ||
                    Client.getGameData().getSelectedUnit().getAttack() == action)
                Client.getGameData().setSelectedAction(null);
            else
                Client.getGameData().setSelectedAction(action);

            if (Client.getGameData().getSelectedUnit().isDead())
                Client.getGameData().setSelectedUnit(null);

            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;

        } catch (Exception e) {
            System.out.println("ActionButton.clickAt(): " + e);
            return true;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        boolean strongColor = false;

        // by default, its an unselected button
        Action selectedAction = Client.getGameData().getSelectedAction();
        Unit selectedUnit = Client.getGameData().getSelectedUnit();
        int img = Constants.IMG_ACTION;

        if (selectedUnit != null) {
            // check for the default move and attack actions
            if (selectedAction == null) {
                if (action == selectedUnit.getAttack() ||
                        action == selectedUnit.getMove())
                    img = Constants.IMG_ACTION_HIGHLIGHT;
            }

            // check if its the selected action
            if (selectedAction == action) {
                img = Constants.IMG_ACTION_HIGHLIGHT;
            }

            // check for mouseover
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
                img = Constants.IMG_ACTION_HIGHLIGHT;
            }

   /*if (selectedUnit.getCastle() != Client.getGameData().getMyCastle())
   { img = Constants.IMG_ACTION_DISABLE;
   }*/


            if (selectedUnit.getCastle() == Client.getGameData().getMyCastle()) {
                if (selectedUnit.getTeam() != Unit.TEAM_1) {
                    img = Constants.IMG_ACTION_DISABLE;
                }

                if (!selectedUnit.ready()) {
                    img = Constants.IMG_ACTION_DISABLE;
                }
            }
        }

        // check for disable
        if (action.getRemaining() <= 0) {
            img = Constants.IMG_ACTION_DISABLE;
        }

        // check for turn
        if (!Client.getGameData().myTurn()) {
            img = Constants.IMG_ACTION_DISABLE;
        }

        // check for passive
        if (action.passive()) {
            img = Constants.IMG_ACTION_PASSIVE;
        }

        // get it and draw it
        Image def = Client.getImages().getImage(img);
        g.drawImage(def, getScreenX(), getScreenY(), mainFrame);

        // do the color
        float alpha = img == Constants.IMG_ACTION_HIGHLIGHT ? 0.7f : 0.15f;
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
        Image color = Client.getImages().getImage(Action.TYPE_COLOR[action.getType()]);
        g.drawImage(color, getScreenX() + 4, getScreenY() + 4, getWidth() - 4, getHeight() - 4, mainFrame);
        g.setComposite(original);

        if (img == Constants.IMG_ACTION_HIGHLIGHT)
            g.drawImage(Client.getImages().getImage(Constants.IMG_ACTION_OVERLAY), getScreenX(), getScreenY(), mainFrame);

        // do some text
        g.setColor(Color.white);
        if (action.getRangeDescription().length() < 1) {
            if (action.getCostDescription().length() < 1) {
                g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + ((getHeight() / 2) + (Client.FONT_HEIGHT / 2)));
            } else {
                g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + Client.FONT_HEIGHT + SideBoard.MARGIN);
                g.drawString(action.getCostDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + (Client.FONT_HEIGHT + SideBoard.MARGIN) * 2);
            }
        } else {
            g.drawString(action.getDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + MARGIN + FONT_SPACE);
            g.drawString(action.getRangeDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + MARGIN + (FONT_SPACE) * 2);
            g.drawString(action.getCostDescription(), getScreenX() + SideBoard.MARGIN, getScreenY() + MARGIN + (FONT_SPACE) * 3);
        }

        // draw the hidden unit
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {

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
