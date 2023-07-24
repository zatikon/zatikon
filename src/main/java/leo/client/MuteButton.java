///////////////////////////////////////////////////////////////////////
// Name: MuteButton
// Desc: Mute button
// Date: 8/29/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class MuteButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public MuteButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        Client.mute(!Client.mute());
        if (Client.mute() && !Client.musicOff()) {
            Client.getImages().pauseMusic();
        } else if (!Client.musicOff()) {
            Client.getImages().resumeMusic();
        }
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUTE_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else if (Client.mute())
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUTED), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUTE), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
