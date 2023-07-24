///////////////////////////////////////////////////////////////////////
// Name: EditNewUnitScrollButton
// Desc: Scroll to more units in a category
// Date: 7/7/2011 (Happy Bithday Gabe's Mom)
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditNewUnitScrollButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Static properties
    /////////////////////////////////////////////////////////////////
    private static final Color lightBlue = Color.blue.brighter();


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit unit;
    private final EditCastlePanel panel;
    private final EditNewUnitControl control;
    private boolean direction = false;


    /////////////////////////////////////////////////////////////////
    // Get the button size
    /////////////////////////////////////////////////////////////////
    public static int getButtonHeight() {
        return (Client.FONT_HEIGHT * 1) + (EditNewUnitControl.MARGIN * 2) + 1;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditNewUnitScrollButton(EditNewUnitControl castlePanel, EditCastlePanel newPanel, boolean dir) {
        super(EditNewUnitControl.MARGIN, castlePanel.getButtonPosition(), castlePanel.getWidth() - (EditNewUnitControl.MARGIN * 4), DeployButton.getButtonHeight() + 3);
        panel = newPanel;
        control = castlePanel;
        //unit.refresh();
        //adding = panel.getCastle().getUnit(unit) == 100;
        direction = dir;
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (direction) {
            control.moveUp();
        } else {
            control.moveDown();
        }

        Client.getImages().playSound(Constants.SOUND_BUTTON);
        return true;

    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {

        //Unit selected = panel.getSelectedUnit();

        Image img;
        if (direction) img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_UP);
        else img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_DOWN);

        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            if (direction) img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_UP_HIGHLIGHT);
            else img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_DOWN_HIGHLIGHT);
        }

  /*if (selected == unit)
  { img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_SELECTED);
  }
  else if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()))
  { img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_HIGHLIGHT);
  }
  else if (!active)
  { img = Client.getImages().getImage(Constants.IMG_NEW_UNIT_DISABLED);
  }
  else
  { img = Client.getImages().getImage(Constants.IMG_NEW_UNIT);
  }*/

        g.drawImage(img, getScreenX(), getScreenY(), mainFrame);


        g.setColor(Color.white);
    }
}
