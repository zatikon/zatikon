///////////////////////////////////////////////////////////////////////
// Name: CreditsButton
// Desc: Credits button
// Date: 8/14/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class CreditsButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CreditsButton(int x, int y, int width, int height) {
        super(x, y, width, height);
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        //CreditsFrame cf = new CreditsFrame();
        String credits = "<table>" +
                "<tr><td>Zatikon " + Client.VERSION + "</td><td></td></tr>" +
                "<tr><td>Chronic Logic</td><td></td></tr>" +
                "<tr><td></td><td></td></tr>" +
                "<tr><td>Programming and Design</td><td>Art</td></tr>" +
                "<tr><td>Gabe Jones</td><td>Alex Biskner</td></tr>" +
                "<tr><td></td><td>Samuel Goldberg</td></tr>" +

                "<tr><td>Producer</td><td>Sean Madden</td></tr>" +
                "<tr><td>Josiah Pisciotta</td><td>Julian Noble</td></tr>" +
                "<tr><td></td><td>Amber Okamura</td></tr>" +

                "<tr><td>Additional Programming</td><td></td></tr>" +
                "<tr><td>William Cole</td><td>Audio</td></tr>" +
                "<tr><td>Linus Foster</td><td>Tony Porter</td></tr>" +
                "<tr><td>Daniel Healy</td><td></td></tr>" +
                "<tr><td>Alexander McCaleb</td><td>Zatikon FOSS project</td></tr>" +
                "<tr><td>Julian Noble</td><td>Lukky513</td></tr>" +
                "<tr><td>David Schwartz</td><td></td></tr>" +
                "<tr><td>Josiah Pisciotta</td><td></td></tr>" +
                "<tr><td>Lukky513</td><td></td></tr>" +
                "</table>";
        Client.getGameData().showCredits(credits);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {

        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
            g.drawImage(Client.getImages().getImage(Constants.IMG_CREDITS_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        else
            g.drawImage(Client.getImages().getImage(Constants.IMG_CREDITS), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);

    }
}
