///////////////////////////////////////////////////////////////////////
// Name: DeployButton
// Desc: Trigger a deploy via me
// Date: 6/3/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.UndeployedUnit;
import leo.shared.Unit;

import java.awt.*;


public class DeployButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int HEIGHT = 23;


    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////
    private static final Color lightBlue = Color.blue.brighter();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit unit;
    private final UndeployedUnit undy;
    private int oldX;
    private int oldY;
    private boolean showingHidden = false;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 1) + (CastlePanel.MARGIN * 2) + 1;
        //return HEIGHT;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public DeployButton(UndeployedUnit newUndy, CastlePanel castlePanel) {
        super(0, castlePanel.getButtonPosition(), castlePanel.getWidth(), HEIGHT);
        undy = newUndy;
        unit = undy.getUnit();
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (Client.getGameData().getMyCastle().getCommandsLeft() >= unit.getDeployCost()) {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.getGameData().setDeployingUnit(unit);
            return true;
        } else {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        Unit selected = Client.getGameData().getDeployingUnit();

        Image def = Client.getImages().getImage(Constants.IMG_DEPLOY);
        g.drawImage(def, getScreenX(), getScreenY(), mainFrame);

        if (selected == unit) { //g.setColor(lightBlue);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            Image img = Client.getImages().getImage(Constants.IMG_DEPLOY_SELECTED);
            g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
        }

        // If there's no left, gray it out
        if (Client.getGameData().getMyCastle().getCommandsLeft() < unit.getDeployCost()) { //g.setColor(Color.lightGray);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            Image img = Client.getImages().getImage(Constants.IMG_DEPLOY_GREY);
            g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
        } else {
            // If the mouse is within the bounds, darken
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) { //g.setColor(Color.black);
                //g.drawRect(getScreenX()-1, getScreenY()-1, getWidth()+1, getHeight()+1);

                if (selected != unit) {
                    Image img = Client.getImages().getImage(Constants.IMG_DEPLOY_HIGHLIGHT);
                    g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
                }

                if (Client.getGameData().getMouseX() == oldX &&
                        Client.getGameData().getMouseY() == oldY)
                    showingHidden = true;


                if (showingHidden) {
                    Client.getGameData().getHiddenUnitStats().initialize(unit);
                    Client.getGameData().getHiddenUnitStats().draw(g, mainFrame);
                }
                oldX = Client.getGameData().getMouseX();
                oldY = Client.getGameData().getMouseY();
            } else
                showingHidden = false;
        }

        g.setColor(Color.white);
        //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
        //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);
        String und = undy.count() > 1 ? undy.count() + " " : "";
        g.drawString(und + unit.getName(), getScreenX() + CastlePanel.MARGIN + 2, getScreenY() + Client.FONT_HEIGHT + CastlePanel.MARGIN + 2);
        g.drawString("Cost: " + unit.getDeployCost(), getScreenX() + CastlePanel.MARGIN + 125, getScreenY() + Client.FONT_HEIGHT + CastlePanel.MARGIN + 2);
    }
}
