///////////////////////////////////////////////////////////////////////
// Name: EditSellExtraButton
// Desc: Sell excess units
// Date: 7/7/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.*;

import java.awt.*;


public class EditSellExtraButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static boolean processing = false;
    private EditCastlePanel panel;
    private boolean confirming = false;
    private int delay = 0;
    private boolean done = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditSellExtraButton(EditCastlePanel newPanel, int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        // Confirm
        if (delay > 0) return false;

        if (!confirming) {
            confirming = true;
            delay = 10;
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        }
        // make sure we're not doing it twice in a row
        if (processing) return true;
        processing = true;

        Client.setComputing(false);
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        // don't keep playing the gold sound
        boolean played = false;
        short[] units = Client.getUnits();

        // Sell it
        for (short i = 0; i < UnitType.UNIT_COUNT.value(); i++) {
            Castle tmp = new Castle();
            Unit unit = Unit.getUnit(i, tmp);
            while (unit != null && unit.getCastleCost() != 1001 && unit.getCastleCost() * units[i] > Constants.MAX_ARMY_SIZE) {

                if (!played) {
                    // let the graphics show
                    Client.getGameData().screenMessage("Selling extra units...");

                    Client.getImages().playSound(Constants.SOUND_GOLD);
                    played = true;
                }

                Client.getNetManager().sendAction(Action.SELL_UNIT, i, Action.NOTHING);
                units[i]--;
            }
        }
        //Client.getGameData().screenEditCastle();

        // refresh it
        //panel.setSelectedUnit(true, null);
        //panel.initialize(true);

        // all done
        processing = false;

        // display no results
        confirming = true;
        done = true;
        delay = 1000000;

        // go back to the edit screen
        if (played) Client.getGameData().screenEditCastle();
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (done)
            return "You have no extra units";
        else if (confirming)
            return "Are you sure?";
        else
            return "Sell All Extra Units";
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (delay > 0) delay--;
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        Image img;

        // If the mouse is within the bounds, darken
        if (delay > 0) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD_ALERT);
        } else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON_HIGHLIGHT);
        } else {
            img = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON);
        }
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        g.setColor(Color.white);
        g.drawString(getMessage(), atX, atY);
    }
}
