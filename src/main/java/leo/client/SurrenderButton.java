///////////////////////////////////////////////////////////////////////
// Name: SurrenderButton
// Desc: The surrender button
// Date: 8/29/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;


public class SurrenderButton extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    private boolean pressed = false;
    private boolean confirming = false;
    private int delay = 0;
    private int timer = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public SurrenderButton(EndPanel endPanel) {
        super(
                (endPanel.getWidth() / 2) + 2,
                100 - (20 + SideBoard.MARGIN),
                (endPanel.getWidth() / 2) - (SideBoard.MARGIN + (2 - 1)),
                20);
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) { // Confirm
        if (delay > 0) return false;

        if (!confirming) {
            confirming = true;
            delay = 10;
            timer = 100;
            return true;
        }

        if (!Client.getGameData().myTurn()) return false;
        if (pressed) return false;
        pressed = true;
        Client.getNetManager().sendAction(Action.SURRENDER, (byte) 0, (byte) 0);
        Client.getGameData().getMyCastle().getObserver().endGame(Client.getGameData().getEnemyCastle());
        return true;
    }


    //////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (confirming)
            return "You sure?";
        else
            return "Surrender";

    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            if (delay > 0) delay--;
            if (timer > 0) {
                timer--;
            } else {
                confirming = false;
            }


            int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getMessage()) / 2);
            int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

            //g.setColor(Color.red);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.setColor(Color.black);

            // If the mouse is within the bounds, darken
            if (!pressed && isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) { //g.setColor(Color.black);
                //g.drawRect(getScreenX()-1, getScreenY()-1, getWidth()+2, getHeight()+2);

                Image image = Client.getImages().getImage(Constants.IMG_SURRENDER);
                g.drawImage(image, getScreenX() + 2, getScreenY() + 2, mainFrame);

            }

            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);

            if (pressed || delay > 0)
                g.setColor(Color.red);
            else
                g.setColor(Color.white);

            g.drawString(getMessage(), atX, atY);


        } catch (Exception e) {
        }
    }
}
