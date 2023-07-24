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


public class GuideButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public GuideButton(int x, int y, int width, int height) {
        super(x, y, width, height);

    }

    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);

//        try {
//            if (Client.applet() == null) {
                Browser.open("http://www.chroniclogic.com/zat_guide.htm");
//            } else {
//                Client.applet().getAppletContext().showDocument(new java.net.URL("http://www.chroniclogic.com/zat_guide.htm"));
//            }
//
//        } catch (Exception e) {
//            System.out.println("GuideButton.clickAt(): " + e);
//        }

        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
            g.drawImage(Client.getImages().getImage(Constants.IMG_GUIDE_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_GUIDE), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
