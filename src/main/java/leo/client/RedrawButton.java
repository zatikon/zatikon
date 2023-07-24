///////////////////////////////////////////////////////////////////////
// Name: RedrawButton
// Desc: The button repicks your starting random army for random mode
// Date: 10/21/2010 - Tony Schwartz
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class RedrawButton extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private int redraws;    //number of redraws left

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RedrawButton(EndPanel endPanel) {
        super(
                0,
                100,
                endPanel.getWidth() + SideBoard.MARGIN / 2, //200,
                50);
        redraws = 2;
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (!Client.getGameData().myTurn()) return false;
        if (Client.getGameData().getGameType() != Constants.RANDOM) return false;

        redraws--;
        Client.getGameData().redraw();

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (!Client.getGameData().myTurn()) return;
        if (Client.getGameData().getGameType() != Constants.RANDOM) {
            return;
        }

        //If it's player 1's turn and P1 repick is disabled, dont draw the repick button
        if (Client.getGameData().getCurrPlayer() == 1 && Client.getGameData().getDisableRepickP1())
            return;
        //If it's player 2's turn and P2 repick is disabled, dont draw the repick button
        if (Client.getGameData().getCurrPlayer() == 2 && Client.getGameData().getDisableRepickP2())
            return;

        try {

            String text = "Redraw Army  " + redraws + "/2";

            int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
            int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);
            //System.out.println("atX = " +atX + " atY =" + atY);

            //g.setColor(Color.white);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            g.setColor(Color.white);

            Image image1 = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON);
            g.drawImage(image1, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            // If the mouse is within the bounds, darken
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) { //g.setColor(Color.black);
                //g.drawRect(getScreenX()-1, getScreenY()-1, getWidth()+2, getHeight()+2);

                Image image = Client.getImages().getImage(Constants.IMG_EDIT_BUTTON_HIGHLIGHT);
                g.drawImage(image, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

            }

            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);


            g.drawString(text, atX, atY);


        } catch (Exception e) {
        }
    }
}
