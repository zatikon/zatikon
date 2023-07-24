///////////////////////////////////////////////////////////////////////
// Name: EditGoldPanel
// Desc: Displays the player's gold while on the edit castle screen.
// Date: 7/7/20011
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditGoldPanel extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int HEIGHT = 25;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final EditCastlePanel panel;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditGoldPanel(LeoComponent board, EditCastlePanel newPanel) { /*super(
   EditCastlePanel.MARGIN,
   board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN*3) + 5,
   //board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN*2),
   board.getWidth() - (EditCastlePanel.MARGIN*3),
   HEIGHT);*/
        super(
                EditCastlePanel.MARGIN + ((board.getWidth() - (EditCastlePanel.MARGIN * 3)) / 2) + (EditCastlePanel.MARGIN / 2),
                board.getHeight() - HEIGHT - (EditCastlePanel.MARGIN * 3) + 7,
                ((board.getWidth() - (EditCastlePanel.MARGIN * 3)) / 2) - (EditCastlePanel.MARGIN / 2),
                HEIGHT);
        panel = newPanel;
    }


    //////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(boolean isAdding, Unit newUnit) {
    }


    //////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        return "Gold: " + Client.getGold();
    }


    /////////////////////////////////////////////////////////////////
    // Click it. Click it good.
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        //Client.setGold(1000000);
        return true;
    }


    //////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        Image img = Client.getImages().getImage(Constants.IMG_EDIT_GOLD);

        g.drawImage(img, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        g.setColor(Color.white);
        g.drawString(getMessage(), atX, atY);
    }
}
