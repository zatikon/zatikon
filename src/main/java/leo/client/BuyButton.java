///////////////////////////////////////////////////////////////////////
// Name: BuyButton
// Desc: Buy a unit
// Date: 4/30/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.server.Balance;
import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class BuyButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int HEIGHT = 25;
    public static final int WIDTH = 80;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit unit;
    private boolean initialized = false;
    private final EditCastlePanel panel;
    private int price = 0;
    private boolean confirming = false;
    private int delay = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public BuyButton(LeoComponent board, EditCastlePanel newPanel) {
        super(
                EditCastlePanel.MARGIN + ((board.getWidth() - (EditCastlePanel.MARGIN * 3)) / 2) + (EditCastlePanel.MARGIN / 2),
                board.getHeight() - (HEIGHT * 2) - (EditCastlePanel.MARGIN * 4) + 10,
                ((board.getWidth() - (EditCastlePanel.MARGIN * 3)) / 2) - (EditCastlePanel.MARGIN / 2),
                HEIGHT);

        //super(
        // (board.getWidth() - ((EditCastlePanel.MARGIN*4) + WIDTH)) + EditCastlePanel.MARGIN,
        // board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN*2),
        // WIDTH + (EditCastlePanel.MARGIN),
        // HEIGHT);
        panel = newPanel;
    }


    //////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Unit newUnit) {
        confirming = false;
        delay = 0;
        if (newUnit == null) {
            initialized = false;
            return;
        }
        unit = newUnit;
        price = Balance.getUnitBuyPrice(unit.getID());
        initialized = true;
    }


    //////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (confirming)
            return "Are you sure?";
        else
            return "Buy for " + price;

    }


    /////////////////////////////////////////////////////////////////
    // Click it. Click it good.
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (!initialized) return false;
        if (!Client.access(unit.accessLevel())) return true;
        if (Client.getGold() < price) return true;

        // Confirm
        if (delay > 0) return false;

        Client.getImages().playSound(Constants.SOUND_BUTTON);

        if (!confirming) {
            confirming = true;
            delay = 10;
            return true;
        }

        // Sell it
        Client.getUnits()[unit.getID()]++;
        Client.setGold(Client.getGold() - price);
        Client.getNetManager().sendAction(Action.CHOOSE_UNIT, unit.getID(), Action.NOTHING);
        Client.getImages().playSound(Constants.SOUND_GOLD);

        // clean the ui
        panel.setSelectedUnit(true, null);
        panel.initialize(true);

        return true;
    }


    //////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (delay > 0) delay--;
        if (!initialized) return;
        if (!Client.access(unit.accessLevel())) return;
        if (unit == null) return;
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        Image img;
        if (delay > 0 || Client.getGold() < price) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD_ALERT);
        } else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD_HIGHLIGHT);
        } else {
            img = Client.getImages().getImage(Constants.IMG_EDIT_ADD);
        }
        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        g.setColor(Color.white);
        g.drawString(getMessage(), atX, atY);
    }


}
