///////////////////////////////////////////////////////////////////////
// Name: EditAddButton
// Desc: Add a new unit to the army
// Date: 7/7/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditAddButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int HEIGHT = 25;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit unit;
    private boolean adding;
    private boolean initialized = false;
    private final EditCastlePanel panel;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditAddButton(LeoComponent board, EditCastlePanel newPanel) { /*super(
   EditCastlePanel.MARGIN,
   board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN*3) + 5,
   //board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN*2),
   board.getWidth() - (EditCastlePanel.MARGIN*3),
   HEIGHT);*/
        super(
                EditCastlePanel.MARGIN,
                board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN * 3) + 7,
                ((board.getWidth() - (EditCastlePanel.MARGIN * 3)) / 2) - (EditCastlePanel.MARGIN / 2),
                HEIGHT);
        panel = newPanel;
    }


    //////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(boolean isAdding, Unit newUnit) {
        adding = isAdding;
        if (newUnit == null) {
            initialized = false;
            return;
        }
        initialized = true;
        unit = newUnit;
    }


    //////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (!Client.access(unit.accessLevel())) return (Unit.GAME_NAME[unit.accessLevel()] + " only");
        return adding ? "Add" : "Remove";
    }


    /////////////////////////////////////////////////////////////////
    // Click it. Click it good.
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (!initialized || (adding && panel.getCastle().getValue() + unit.getCastleCost() > Constants.MAX_ARMY_SIZE)) return false;
        if (adding && Client.getGameData().getRemaining(unit) < 1) return false;
        if (!Client.access(unit.accessLevel())) return false;

        Client.getImages().playSound(Constants.SOUND_BUTTON);

        if (adding)
            panel.getCastle().add(unit);
        else
            panel.getCastle().remove(unit);

        panel.setSelectedUnit(true, null);
        panel.initialize(true);

        return true;
    }


    //////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (!initialized) return;
        if (unit == null) return;
        if (Client.access(unit.accessLevel())) {
            if (adding && panel.getCastle().getValue() + unit.getCastleCost() > Constants.MAX_ARMY_SIZE) return;
            if (adding && Client.getGameData().getRemaining(unit) < 1) return;
        }
        Image img;

        // If the mouse is within the bounds, darken
        if (!Client.access(unit.accessLevel())) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD_ALERT);
        } else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD_HIGHLIGHT);
        } else {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD);
        }
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        g.setColor(Color.white);
        g.drawString(getMessage(), atX, atY);
    }
}
