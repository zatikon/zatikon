///////////////////////////////////////////////////////////////////////
// Name: VersusPanel
// Desc: The panel that says who you're playing
// Date: 7/21/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class VersusPanel extends LeoComponent {


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public VersusPanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        String text;
        if (Client.getGameData().getEnemyName().equals("The Artificial Opponent"))
            text = "You will be playing against " + Client.getGameData().getEnemyName() + " (level: " + Client.getGameData().getEnemyRating() + ")";
        else if (Client.getGameData().getEnemyRating() > 0)
            text = "You will be playing against " + Client.getGameData().getEnemyName() + " (rank #" + Client.getGameData().getEnemyRating() + ")";
        else
            text = "You will be playing against " + Client.getGameData().getEnemyName() + " (unranked)";
        g.setFont(Client.getFontBig());

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (20 / 2);

        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);
        g.setColor(Color.yellow);

        g.drawString(text, atX, atY);
        g.setFont(Client.getFont());

    }
}
