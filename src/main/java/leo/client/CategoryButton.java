///////////////////////////////////////////////////////////////////////
// Name: CategoryButton
// Desc: Select a category of units
// Date: 7/7/2003 (Happy birthday Mom) - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class CategoryButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////
    private static final Color lightBlue = Color.blue.brighter();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean active = false;
    private final int category;
    private final EditCastlePanel panel;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 1) + (EditCategoryControl.MARGIN * 2) + 1;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CategoryButton(int newCategory, EditCategoryControl castlePanel, EditCastlePanel newPanel, boolean isActive) {
        super(EditCategoryControl.MARGIN, castlePanel.getButtonPosition(), castlePanel.getWidth() - (EditCategoryControl.MARGIN * 4), DeployButton.getButtonHeight() + 2);
        category = newCategory;
        panel = newPanel;
        active = isActive;
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        panel.setSelectedCategory(category);
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int plus = 2;
        int selected = panel.getSelectedCategory();

        Image img;

        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && selected != category) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_HIGHLIGHT);
        } else if (selected == category) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_SELECTED);
        } else if (!active) {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_DISABLED);
        } else {
            img = Client.getImages().getImage(Constants.IMG_NEW_UNIT);
        }
        g.drawImage(img, getScreenX(), getScreenY(), mainFrame);

        g.setColor(Color.white);
        g.drawString(Unit.getClassName(category), getScreenX() + plus + EditCategoryControl.MARGIN, getScreenY() + plus + Client.FONT_HEIGHT + EditCategoryControl.MARGIN);
    }
}
