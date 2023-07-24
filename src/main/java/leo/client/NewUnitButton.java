///////////////////////////////////////////////////////////////////////
// Name: NewUnitButton
// Desc: Select a new unit
// Date: 7/7/2003 (Happy Bithday Mom) - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class NewUnitButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////
    private static final Color lightBlue = Color.blue.brighter();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Unit unit;
    private final EditCastlePanel panel;
    private final EditNewUnitControl control;
    private final boolean adding;
    private final boolean active;
    private int doubleClick = 0;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 1) + (EditNewUnitControl.MARGIN * 2) + 1;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public NewUnitButton(boolean isAdding, Unit newUnit, EditNewUnitControl castlePanel, EditCastlePanel newPanel, boolean isActive) {
        super(EditNewUnitControl.MARGIN, castlePanel.getButtonPosition(), castlePanel.getWidth() - (EditNewUnitControl.MARGIN * 4), DeployButton.getButtonHeight() + 3);
        unit = newUnit;
        panel = newPanel;
        control = castlePanel;
        unit.refresh();
        //adding = panel.getCastle().getUnit(unit) == 100;
        adding = isAdding;
        active = isActive;
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) { //if (!active) return false;

        if (doubleClick > 0) {
            if ((adding && panel.getCastle().getValue() + unit.getCastleCost() > Constants.MAX_ARMY_SIZE)) {
                return false;
            }
            if (adding && Client.getGameData().getRemaining(unit) < 1) {
                return false;
            }
            if (!Client.access(unit.accessLevel())) {
                return false;
            }

            if (adding) {
                panel.getCastle().add(unit);
            }

            panel.setSelectedUnit(adding, null);
            panel.initialize(adding);
        } else {
            doubleClick = 5;
            panel.setSelectedUnit(adding, unit);
        }
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        return true;

    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int plus = 1;

        if (doubleClick > 0)
            doubleClick--;
        //Unit selected = panel.getSelectedUnit();
        Unit selected = control.getSelectedUnit();

        Image img;

        if (selected == unit || (selected != null && unit != null && selected.getName() == unit.getName())) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_SELECTED);
        } else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_HIGHLIGHT);
        } else if (!active) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_DISABLED);
        } else {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT);
        }

        g.drawImage(img, getScreenX(), getScreenY(), mainFrame);

        if (!Client.access(unit.accessLevel())) {
            g.setColor(Color.red.brighter());
        } else {
            g.setColor(Color.white);
        }

        if (adding) {
            g.drawString("" +
                    Client.getGameData().getRemaining(unit)
                    + " " +
                    unit.getName(), getScreenX() + EditNewUnitControl.MARGIN, getScreenY() + plus + Client.FONT_HEIGHT + EditNewUnitControl.MARGIN);
            g.drawString("Cost: " + unit.getCastleCost(), getScreenX() + EditNewUnitControl.MARGIN + 175, getScreenY() + plus + Client.FONT_HEIGHT + EditNewUnitControl.MARGIN);

        } else {
            g.drawString(unit.getName(), getScreenX() + EditNewUnitControl.MARGIN, getScreenY() + plus + Client.FONT_HEIGHT + EditNewUnitControl.MARGIN);
            g.drawString("Cost: " + unit.getCastleCost(), getScreenX() + EditNewUnitControl.MARGIN + 175, getScreenY() + plus + Client.FONT_HEIGHT + EditNewUnitControl.MARGIN);
        }
    }
}
