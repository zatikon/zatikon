///////////////////////////////////////////////////////////////////////
// Name: EditNewUnitControl
// Desc: The board showing new units
// Date: 7/7/2003 (Happy birthday Mom) - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.util.Vector;


public class EditNewUnitControl extends EditCastleBoard {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int buttons = 0;
    private Vector units;
    private int position = 0;
    private int category = -1;
    private Unit selectedUnit = null;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditNewUnitControl(EditCastlePanel panel, String newMessage, int x, int y, int width, int height) {
        super(panel, newMessage, x, y, width, height, Constants.IMG_EDIT_UNITS_PANEL);
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the control
    /////////////////////////////////////////////////////////////////
 /*public void initialize(int category)
 { clear();
  units = Unit.getUnits(category, panel.getCastle());
  for (int i = 0; i < units.size(); i++)
  { Unit unit = (Unit) units.elementAt(i);
   if (Client.getUnits()[unit.getID()] > 0)
    add(new NewUnitButton(true, unit, this, panel, true));
   else
    add(new NewUnitButton(true, unit, this, panel, false));
  }
 }*/

    public void initialize(int cat, boolean resetPos) //, int position)
    {
        if (resetPos)
            position = 0;
        category = cat;
        clear();
        units = Unit.getUnits(category, panel.getCastle());
        int numUnitButtons = 9;
        if (!atTop()) {
            --numUnitButtons;
            add(new EditNewUnitScrollButton(this, panel, true));
        }
        if (!atBottom()) --numUnitButtons;
        for (int i = position; i < units.size() && i < position + numUnitButtons; i++) {
            Unit unit = (Unit) units.elementAt(i);

            if (Client.getUnits()[unit.getID()] > 0)
                add(new NewUnitButton(true, unit, this, panel, true));
            else
                add(new NewUnitButton(true, unit, this, panel, false));
        }
        if (!atBottom()) add(new EditNewUnitScrollButton(this, panel, false));
    }

    public void moveUp() {
        --position;
        --position;
        if (!atTop())
            ++position;
        initialize(category, false);
    }

    public void moveDown() {
        if (atTop()) ++position;
        ++position;
        initialize(category, false);
    }

    private boolean atTop() {
        return position == 0;
    }

    private boolean atBottom() {
        return position + 7 >= units.size() - 1 || units.size() <= 9;
    }

    public void setSelectedUnit(Unit unit) {
        if (unit != null)
            selectedUnit = unit;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public int getButtonPosition() {
        return buttons * (ArmyButton.getButtonHeight() + MARGIN) + Client.FONT_HEIGHT + (MARGIN * 3);
    }


    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public void add(LeoComponent newLeoComponent) {
        super.add(newLeoComponent);
        buttons++;
    }
 
 /*public void draw(Graphics2D g, Frame mainFrame) 
 {
	  int atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(getMessage())/2);
	  int atY = getScreenY() + Constants.OFFSET + Client.FONT_HEIGHT - 2;
	
	  if (image >= 0)
	  { Image img = Client.getImages().getImage(image);
	   g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
	  }
	
	  g.setColor(Color.black);
	  g.drawString(getMessage(), atX, atY);
	  
	  if (components.size() <= 9) {
		  for (int i = 0; i < components.size(); i++)
		  { LeoComponent component = (LeoComponent) components.elementAt(i);
		   if (component != null) component.draw(g, mainFrame);
		  }
	  }
 }*/


    /////////////////////////////////////////////////////////////////
    // Clear
    /////////////////////////////////////////////////////////////////
    public void clear() {
        buttons = 0;
        super.clear();
    }
}
