///////////////////////////////////////////////////////////////////////
// Name: TeamButton
// Desc: Team game
// Date: 4/3/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.image.ColorConvertOp;
import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;
import java.awt.image.RescaleOp;
import java.awt.*;


public class LaunchGameButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean inside = false;
    private RosterText rosterText;
    private BufferedImage disabledImg = null;
    private BufferedImage highlightedImg = null;
    private int img;
    private boolean disabled;
    private boolean tempDisabled;
    private String label;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LaunchGameButton(LeoContainer parent, int x, int y, int width, int height, int buttonImg, String buttonLabel, String buttonText, boolean buttonDisabled, String disabledText) {
        super(x, y, width, height);
        img = buttonImg;
        disabled = buttonDisabled;
        tempDisabled = buttonDisabled;
        label = buttonLabel;

        rosterText = new RosterText(parent,
            (tempDisabled ? disabledText + " " : "") + buttonText,
            4, 455, 186, 142);
    }

    /////////////////////////////////////////////////////////////////
    // Click code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if(tempDisabled) {
            return false;
        }
        try {
            if(label == "Buy New Unit") {
                if (Client.getGold() < 100) return false;
                Client.getImages().playSound(Constants.SOUND_BUTTON);
                Client.setState("buying new unit");
                Client.getNetManager().sendAction(Action.BUY_UNIT, Action.NOTHING, Action.NOTHING);
                Client.setState("home");
                return true;
            }

            Client.getImages().playSound(Constants.SOUND_BUTTON);

            if(label == "Edit Army") {
                Client.setState("edit army");
                Client.getGameData().screenEditCastle();
                return true;
            }

            if(label == "Army Archive") {
                Client.setState("army archive");
                CastleArchiveList cal = new CastleArchiveList();
                return true;
            }

            Client.setComputing(false);

            if (Client.getGameData().getArmy().depleted() && label != "Random" && label != "Mirror Random") {
                Client.getGameData().screenEditCastle();
            } else {
                if(Client.getServerWillShutDown() == true) {
                    Client.getGameData().screenLoading("Can't start a new game, the server is shutting down for an update.");
                } else if(label == "Single Player" || label == "Start Here!") {
                    Client.getNetManager().requestPractice();
                    Client.getGameData().screenLoading("Loading the Artificial Opponent...");
                } else if (label == "Cooperative") {
                    Client.getNetManager().requestCooperative();
                    Client.getGameData().screenLoading("Searching for an ally, please wait...");
                } else if(label == "2 vs 2") {
                    Client.getNetManager().sendAction(Action.TEAM);
                    Client.getGameData().screenTeamLoading();                                                                   
                } else if(label == "Constructed") {
                    Client.getNetManager().requestGame();
                    Client.getGameData().screenLoading("Searching for an opponent, please wait...");  
                } else if (label == "Random") {
                    Client.getNetManager().requestDuel();
                    Client.getGameData().screenLoading("Searching for an opponent, please wait...");                    
                } else if (label == "Mirrored Random") {
                    Client.getNetManager().requestMirrDuel();
                    Client.getGameData().screenLoading("Searching for an opponent, please wait...");
                }
            }
            return true;
        } catch (Exception e) {
            Logger.error("TeamButton.clickAt(): " + e);
            Client.shutdown();
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if(label.equals("Buy New Unit") && !disabled && Client.getGold() < 100) {
            tempDisabled = true;
        } else if(label.equals("Buy New Unit") && !disabled && tempDisabled) { //gold is more than 99 so set disabled to false
            tempDisabled = false;
        }


        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            rosterText.draw(g, mainFrame);
            if (!inside) {
                Client.getImages().playSound(Constants.SOUND_MOUSEOVER);
            }
            inside = true;
            if(tempDisabled) {
                createGrayscaleImage();
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                createHighlightedImage();
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        } else {
            inside = false;
            if(tempDisabled) {
                createGrayscaleImage();
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {            
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        }

        drawText(g, label, getScreenX() + getWidth() + 4, getScreenY() + getHeight() - 12);

    }

    public void createGrayscaleImage() {

        if(disabledImg != null) {
            return;
        }
        //create darkened and greyscale version of image
        disabledImg = new BufferedImage(
            Client.getImages().getImage(img).getWidth(null),
            Client.getImages().getImage(img).getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D gImg = disabledImg.createGraphics();
        gImg.drawImage(Client.getImages().getImage(img), 0, 0, null);
        gImg.dispose();

        // Create a grayscale color conversion operation
        ColorConvertOp grayscaleOp = new ColorConvertOp(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), 
            null
        );

        // Apply the grayscale conversion to the image
        grayscaleOp.filter(disabledImg, disabledImg);

        // Apply darkening
        RescaleOp darkenOp = new RescaleOp(0.5f, 0, null); // Scale by 0.5 to darken
        darkenOp.filter(disabledImg, disabledImg);
    }

    public void createHighlightedImage() {

        if(highlightedImg != null) {
            return;
        }
        //create darkened and greyscale version of image
        highlightedImg = new BufferedImage(
            Client.getImages().getImage(img).getWidth(null),
            Client.getImages().getImage(img).getHeight(null),
            BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D gImg = highlightedImg.createGraphics();
        gImg.drawImage(Client.getImages().getImage(img), 0, 0, null);
        gImg.dispose();

        // Apply darkening
        RescaleOp darkenOp = new RescaleOp(1.25f, 0, null); // Scale by 1.5 to brighten
        darkenOp.filter(highlightedImg, highlightedImg);
    }    

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void drawText(Graphics2D g, String text, int atX, int atY) {
        //if (inside)
        g.setFont(Client.getFontBold());
        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);

        if(tempDisabled)
            g.setColor(Color.gray);
        else if (inside)
            g.setColor(Color.yellow);
        else
            g.setColor(Color.white);

        g.drawString(text, atX, atY);

        // set it back
        g.setFont(Client.getFont());

    }

    /////////////////////////////////////////////////////////////////
    // Are the coordinates within this component
    /////////////////////////////////////////////////////////////////
    @Override
    public boolean isWithin(int testX, int testY) {
        return (testX >= getScreenX() && testX - 100 <= (getScreenX() + getWidth())) &&
                (testY >= getScreenY() && testY <= (getScreenY() + getHeight()));
    }


}
