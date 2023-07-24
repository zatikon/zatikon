///////////////////////////////////////////////////////////////////////
// Name: CastlePanel
// Desc: A panel of deploy buttons
// Date: 6/3/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.UndeployedUnit;

import java.awt.*;
import java.util.Vector;


public class CastlePanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MARGIN = 4;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int buttons = 0;
    private boolean initialized = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    CastlePanel(SideBoard sideBoard) {
        super(
                CastlePanel.MARGIN,
                CastlePanel.MARGIN,
                sideBoard.getWidth() - (CastlePanel.MARGIN * 2),
                600 - (Constants.OFFSET * 2));
    }


    /////////////////////////////////////////////////////////////////
    // Initialize the control
    /////////////////////////////////////////////////////////////////
    public void initialize() {
        clear();
        Vector units = Client.getGameData().getMyCastle().getBarracks();
        int max = units.size() > 20 ? 20 : units.size();
        for (int i = 0; i < max; i++) {
            UndeployedUnit unit = (UndeployedUnit) units.elementAt(i);
            add(new DeployButton(unit, this));
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public int getButtonPosition() {
        return buttons * (DeployButton.getButtonHeight() + CastlePanel.MARGIN);
    }


    /////////////////////////////////////////////////////////////////
    // Get button position
    /////////////////////////////////////////////////////////////////
    public void add(LeoComponent newLeoComponent) {
        super.add(newLeoComponent);
        buttons++;
    }


    //////////////////////////////////////////////////////////////////
    // Draw
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if (!initialized || Client.getGameData().castleChanged()) {
            initialize();
            initialized = true;
        }
        super.draw(g, mainFrame);
    }


    /////////////////////////////////////////////////////////////////
    // Clear
    /////////////////////////////////////////////////////////////////
    public void clear() {
        buttons = 0;
        super.clear();
    }
}
