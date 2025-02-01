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
                "<tr><td>Zatikon " + Client.VERSION + "</td></tr>" +
                "<tr><td>Chronic Logic</td></tr>" +
                "<tr><td></td></tr>" + // Empty row for spacing

                "<tr><td>Programming and Design</td></tr>" +
                "<tr><td>Gabe Jones</td></tr>" +
                "<tr><td></td></tr>" +

                "<tr><td>Producer</td></tr>" +
                "<tr><td>Josiah Pisciotta</td></tr>" +
                "<tr><td></td></tr>" +

                "<tr><td>Additional Programming</td></tr>" +
                "<tr><td>William Cole</td></tr>" +
                "<tr><td>Linus Foster</td></tr>" +
                "<tr><td>Daniel Healy</td></tr>" +
                "<tr><td>Alexander McCaleb</td></tr>" +
                "<tr><td>Julian Noble</td></tr>" +
                "<tr><td>David Schwartz</td></tr>" +
                "<tr><td>Josiah Pisciotta</td></tr>" +
                "<tr><td>Lukky513</td></tr>" +
                "<tr><td></td></tr>" +

                "<tr><td>Art</td></tr>" +
                "<tr><td>Alex Biskner</td></tr>" +
                "<tr><td>Samuel Goldberg</td></tr>" +
                "<tr><td>Sean Madden</td></tr>" +
                "<tr><td>Julian Noble</td></tr>" +
                "<tr><td>Amber Okamura</td></tr>" +
                "<tr><td></td></tr>" +

                "<tr><td>Audio</td></tr>" +
                "<tr><td>Tony Porter</td></tr>" +
                "<tr><td></td></tr>" +

                "<tr><td>Zatikon FOSS project</td></tr>" +
                "<tr><td>Lukky513</td></tr>" +
                "<tr><td></td></tr>" +
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
