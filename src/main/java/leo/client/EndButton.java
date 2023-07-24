///////////////////////////////////////////////////////////////////////
// Name: EndButton
// Desc: The button that ends your turn
// Date: 6/5/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class EndButton extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EndButton(EndPanel endPanel) {
        super(
                SideBoard.MARGIN,
                (SideBoard.MARGIN * 2) + Client.FONT_HEIGHT,
                endPanel.getWidth() - (SideBoard.MARGIN * 2),
                100 - ((SideBoard.MARGIN * 3) + Client.FONT_HEIGHT) - (20 + 4));
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (!Client.getGameData().myTurn()) return false;

        Client.getGameData().endTurn();

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (!Client.getGameData().myTurn()) return;
        try {

            int minutes = Client.getGameData().getTimer().getTime() / 60;
            int seconds = Client.getGameData().getTimer().getTime() % 60;
            String time;

            time = "End Turn (" + minutes + ":";
            if (seconds < 10)
                time = time + "0";

            time = time + seconds + ")";

            int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(time) / 2);
            int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

            //g.setColor(Color.white);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            g.setColor(Color.white);

            // If the mouse is within the bounds, darken
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) { //g.setColor(Color.black);
                //g.drawRect(getScreenX()-1, getScreenY()-1, getWidth()+2, getHeight()+2);

                Image image = Client.getImages().getImage(Constants.IMG_END_TURN);
                g.drawImage(image, getScreenX() + 2, getScreenY() + 2, mainFrame);

            }

            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);

            // Urgent!!!
            if (minutes < 1 && seconds < 20)
                g.setColor(Color.red);

            g.drawString(time, atX, atY);


        } catch (Exception e) {
        }
    }
}
