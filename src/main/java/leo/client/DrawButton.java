///////////////////////////////////////////////////////////////////////
// Name: DrawButton
// Desc: The draw button
// Date: 8/29/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;


public class DrawButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    private boolean pressed = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public DrawButton(EndPanel endPanel) {
        super(
                SideBoard.MARGIN,
                100 - (20 + SideBoard.MARGIN),
                (endPanel.getWidth() / 2) - (SideBoard.MARGIN + 2),
                20);
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (Client.getGameData().getEnemyCastle().isAI() && !Client.standalone) return false;
        if (!Client.getGameData().myTurn()) return false;
        if (pressed) return false;
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        if(Client.standalone) {
            if(Client.getGameData().getTimer().getjustUnpaused() == false) {
                Client.getGameData().getTimer().togglePaused();                
            }
            return true;
        } else {
            Client.getGameData().setMyDraw();
            if (Client.getGameData().drawOffered())
                Client.addText("You have accepted the draw");
            else
                Client.addText("You have offered " + Client.getGameData().getEnemyName() + " a draw.");

            Client.getNetManager().sendAction(Action.OFFER_DRAW, (byte) 0, (byte) 0);
            pressed = true;
            if (Client.getGameData().getMyDraw() && Client.getGameData().drawOffered())
                Client.getGameData().getMyCastle().getObserver().drawGame();
            return true;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            String message;
            if(Client.standalone)
                message = Client.getGameData().getTimer().getPaused() ? "Unpause" : "Pause";
            else
                message = Client.getGameData().drawOffered() ? "Accept Draw" : "Offer Draw";

            int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(message) / 2);
            int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

            //g.setColor(Color.red);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            g.setColor(Color.black);

            // If the mouse is within the bounds, darken
            if (!pressed && isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) { //g.setColor(Color.black);
                //g.drawRect(getScreenX()-1, getScreenY()-1, getWidth()+2, getHeight()+2);
                Image image = Client.getImages().getImage(Constants.IMG_DRAW);
                g.drawImage(image, getScreenX() + 2, getScreenY() + 2, mainFrame);
            }

            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);


            if (pressed)
                g.setColor(Color.red);
            else
                g.setColor(Color.white);

            g.drawString(message, atX, atY);


        } catch (Exception e) {
        }
    }
}
