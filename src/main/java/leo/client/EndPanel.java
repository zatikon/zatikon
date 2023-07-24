///////////////////////////////////////////////////////////////////////
// Name: EndPanel
// Desc: The end of turn panel
// Date: 6/5/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class EndPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int HEIGHT = 200;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    EndPanel(SideBoard sideBoard) {
        super(
                SideBoard.MARGIN,
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2) - EndPanel.HEIGHT - SideBoard.MARGIN + 50,
                sideBoard.getWidth() - (SideBoard.MARGIN * 2),
                EndPanel.HEIGHT);
    }


    //////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            if (!Client.getGameData().myTurn()) {
                //tmp
                //super.draw(g, mainFrame);
                return;
            }

            //g.setColor(Color.lightGray);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.setColor(Color.black);
            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);

            Image image = Client.getImages().getImage(Constants.IMG_END_PANEL);

            g.drawImage(image, getScreenX(), getScreenY(), mainFrame);

            super.draw(g, mainFrame);
            g.setColor(Color.white);
            g.setFont(Client.getFontBold());
            g.drawString(
                    "Commands: " + Client.getGameData().getMyCastle().getCommandsLeft() + "/" + Client.getGameData().getMyCastle().getCommandsMax(),
                    getScreenX() + SideBoard.MARGIN,
                    getScreenY() + SideBoard.MARGIN + Client.FONT_HEIGHT + 2);
            g.setFont(Client.getFont());
        } catch (Exception e) {
        }
    }

}
