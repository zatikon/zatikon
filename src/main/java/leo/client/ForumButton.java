///////////////////////////////////////////////////////////////////////
// Name: ForumButton
// Desc: Forum button
// Date: 3/8/2010 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ForumButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ForumButton(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);

//        try {
//            if (Client.applet() == null) {
                Browser.open("https://www.chroniclogic.com/smf/");
//            } else {
//                Client.applet().getAppletContext().showDocument(new java.net.URL("http://www.pontifex2.com/smf/index.php"));
//            }
//
//        } catch (Exception e) {
//            System.out.println("ForumButton.clickAt(): " + e);
//        }

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
            g.drawImage(Client.getImages().getImage(Constants.IMG_FORUM_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_FORUM), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
