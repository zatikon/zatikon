///////////////////////////////////////////////////////////////////////
//	Name:	EditButtonPanel
//	Desc:	A panel of action buttons
//	Date:	5/23/2003 - Gabe Jones
//			12/2/2010 - Tony Schwartz
//			Changed getButtonPosition() to work with half sized buttons
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Unit;

import java.util.Vector;


public class EditButtonPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int buttons = 0;
    private final EditCastleStats statsBoard;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditButtonPanel(EditCastleBoard board, EditCastleStats newStatsBoard) {
        super(
                EditCastlePanel.MARGIN,
                //EditCastlePanel.MARGIN*3,
                4,
                board.getWidth() - (EditCastlePanel.MARGIN * 4),
                400);
        statsBoard = newStatsBoard;
    }


    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public int getButtonPosition() {
        return buttons * ((ActionButton.getButtonHeight() + EditCastlePanel.MARGIN) / 2);
    }


    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public void add(LeoComponent newLeoComponent) {
        super.add(newLeoComponent);
        if (newLeoComponent.getHeight() == 22)    //If the button is a half size button,
            buttons += 1;                            //Increase the button counter by 1, else 2
        else
            buttons += 2;
    }

/*
	//////////////////////////////////////////////////////////////////
	// Draw
	/////////////////////////////////////////////////////////////////
	public void draw(Graphics g, Frame mainFrame)
	{	super.draw(g, mainFrame);
		g.setColor(Color.cyan);
		g.drawRect(getScreenX(), getScreenY(), getWidth() -1, getHeight()-1);
	}
*/

    /////////////////////////////////////////////////////////////////
    // Clear
    /////////////////////////////////////////////////////////////////
    public void clear() {
        buttons = 0;
        super.clear();
    }


    /////////////////////////////////////////////////////////////////
    // Get y override
    /////////////////////////////////////////////////////////////////
    public int getY() {
        return super.getY() + statsBoard.getHeight() + EditCastlePanel.MARGIN;
    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Unit unit, boolean allowHidden) {
        clear();
        if (unit == null) return;

        Vector actions = unit.getActions();
        for (int i = 0; i < actions.size(); i++) {
            Action action = (Action) actions.elementAt(i);
            EditActionButton button = new EditActionButton(allowHidden, unit, action, this);
            add(button);
        }
    }
}
