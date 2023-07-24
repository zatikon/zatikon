///////////////////////////////////////////////////////////////////////
// Name: HiddenUnitStats
// Desc: Display the stats for a hidden unit
// Date: 5/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class HiddenUnitStats extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static final int MARGIN = 5;
    private static final int WIDTH = 250;
    private static final int HEIGHT = 400;
    private static final int X = (Constants.SCREEN_WIDTH / 2) - (WIDTH / 2);
    private static final int Y = (Constants.SCREEN_HEIGHT / 2) - (HEIGHT / 2);
    private final EditCastleStats stats;
    private final EditCastleBoard statsBoard;
    private final EditCastleBoard actionBoard;
    private final EditButtonPanel buttons;
    private final ActionDescription description;
    private Action action;
    private Unit unit;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public HiddenUnitStats(LeoContainer newParent) {
        super(X, Y, WIDTH, HEIGHT);

        // Stats
        statsBoard = new EditCastleBoard(
                null,
                "",
                MARGIN * 2,
                MARGIN * 2,
                //WIDTH - (MARGIN*2),
                WIDTH,
                HEIGHT - (MARGIN * 2),
                -1);
        //Constants.IMG_EDIT_POPUP_PANEL);

        actionBoard = new EditCastleBoard(
                null,
                "",
                MARGIN * 2,
                MARGIN * 2,
                //WIDTH - (MARGIN*2),
                WIDTH,
                HEIGHT - (MARGIN * 2),
                -1);
        //Constants.IMG_EDIT_POPUP_PANEL);

        stats = new EditCastleStats(statsBoard);
        statsBoard.add(stats);
        buttons = new EditButtonPanel(statsBoard, stats);
        statsBoard.add(buttons);
        add(statsBoard);

        description = new ActionDescription(actionBoard);
        actionBoard.add(description);
    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Action newAction) {
        if (action == newAction)
            return;
        clear();
        unit = null;
        action = newAction;

        if (action.getHiddenUnit() != null) {
            initialize(action.getHiddenUnit());
        } else {
            description.initialize(action);
            add(actionBoard);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Unit newUnit) {
        clear();
        unit = newUnit;
        stats.initialize(unit);
        buttons.initialize(unit, false);
        add(statsBoard);
    }


    /////////////////////////////////////////////////////////////////
    // Get the name
    /////////////////////////////////////////////////////////////////
    public String getName() {
        return unit == null ? action.getName() : unit.getName();
    }


    /////////////////////////////////////////////////////////////////
    // Draw it
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) { //if (unit == null) return;
        super.draw(g, mainFrame);
    }
}
