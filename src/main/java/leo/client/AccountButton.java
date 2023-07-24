///////////////////////////////////////////////////////////////////////
// Name: AccountButton
// Desc: Account button
// Date: 8/14/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class AccountButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AccountButton(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        AccountFrame af = new AccountFrame(Client.getFrame());
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
            g.drawImage(Client.getImages().getImage(Constants.IMG_ACCOUNT_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_ACCOUNT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
