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


public class MusicButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public MusicButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        Client.musicOff(!Client.musicOff());
        if (Client.musicOff() && !Client.mute()) {
            Client.getImages().pauseMusic();
        } else if (!Client.mute()) {
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
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUSIC_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else if (Client.mute() || Client.musicOff())
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUSIC_OFF), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_MUSIC), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
