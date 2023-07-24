///////////////////////////////////////////////////////////////////////
// Name: ArmyButton
// Desc: Select a unit in my army
// Date: 6/3/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.UndeployedUnit;
import leo.shared.Unit;

import java.awt.*;


public class ArmyButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////
    private static final Color lightBlue = Color.blue.brighter();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit unit;
    private final UndeployedUnit undy;
    private final EditCastlePanel panel;
    private int doubleClick = 0;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 1) + (EditCastleControl.MARGIN * 2) + 1;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ArmyButton(UndeployedUnit newUndy, EditCastleControl castlePanel, EditCastlePanel newPanel) {
        super(EditCastleControl.MARGIN, castlePanel.getButtonPosition(), castlePanel.getWidth() - (EditCastleControl.MARGIN * 4), DeployButton.getButtonHeight() + 3);

        undy = newUndy;

        unit = undy.getUnit();

        panel = newPanel;

        unit.refresh();
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        Client.setComputing(false);
        if (doubleClick > 0) {
            panel.getCastle().remove(unit);
            panel.setSelectedUnit(false, null);
            panel.initialize(false);
        } else {
            panel.setSelectedUnit(false, unit);
            doubleClick = 5;
        }
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int push = 1;
        Image img;

        if (doubleClick > 0) doubleClick--;
        Unit selected = panel.getSelectedUnit();

        if (selected == unit) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_SELECTED);
        } else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && panel.getSelectedUnit() != unit) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_HIGHLIGHT);
        } else {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT);
        }

        g.drawImage(img, getScreenX(), getScreenY(), mainFrame);

        g.setColor(Color.white);
        String und = undy.count() > 1 ? undy.count() + " " : "";
        g.drawString(und + unit.getName(), getScreenX() + push + EditCastleControl.MARGIN, getScreenY() + push + Client.FONT_HEIGHT + EditCastleControl.MARGIN);
        g.drawString("Cost: " + unit.getCastleCost() * undy.count(), getScreenX() + push + EditCastleControl.MARGIN + 175, getScreenY() + push + Client.FONT_HEIGHT + EditCastleControl.MARGIN);
    }
}
