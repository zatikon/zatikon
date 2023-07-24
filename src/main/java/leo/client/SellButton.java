///////////////////////////////////////////////////////////////////////
// Name: SellButton
// Desc: Sell a unit
// Date: 11/something/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.server.Balance;
import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class SellButton extends LeoComponent {

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
    public SellButton(LeoComponent board, EditCastlePanel newPanel) {
        super(
                EditCastlePanel.MARGIN,
                //board.getHeight() - (HEIGHT*2) - (EditCastlePanel.MARGIN*3),
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

        initialized = Client.getGameData().getRemaining(unit) >= 1;

        if (Client.getUnits()[unit.getID()] <= Unit.startingCount(unit.getID()))
            initialized = false;

        //initialized = (panel.getCastle().getUnit(unit) == 100);
        price = Balance.getUnitSellPrice(unit.getID());
    }


    //////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (confirming)
            return "Are you sure?";
        else
            return "Sell for " + price;

    }


    /////////////////////////////////////////////////////////////////
    // Click it. Click it good.
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (!initialized) return false;

        // Confirm
        if (delay > 0) return false;

        Client.getImages().playSound(Constants.SOUND_BUTTON);

        if (!confirming) {
            confirming = true;
            delay = 10;
            return true;
        }

        // Sell it
        Client.getNetManager().sendAction(Action.SELL_UNIT, unit.getID(), Action.NOTHING);
        Client.getUnits()[unit.getID()]--;
        Client.getImages().playSound(Constants.SOUND_GOLD);

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
        if (unit == null) return;
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        Image img;
        if (delay > 0) {
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
