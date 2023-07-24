///////////////////////////////////////////////////////////////////////
// Name: EditCastlePanel
// Desc: The panel that manages armies
// Date: 7/02/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Castle;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditCastlePanel extends LeoContainer {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MARGIN = 5;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final EditCastleControl myCastle;
    private Unit selectedUnit = null;
    private int category = -1;
    private final EditNewUnitControl newUnits;
    private final EditCastleBoard statsBoard;
    private final EditCastleStats stats;
    private final EditButtonPanel buttons;
    private final EditAddButton addButton;
    private final EditGoldPanel goldPanel;
    private final SellButton sellButton;
    private final BuyButton buyButton;
    private final EditCategoryControl categoryControl;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditCastlePanel() {

        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));

        // Create the controls
        int controlX = (getWidth() / 3);
        int controlHeight = getHeight() - (MARGIN * 2);
        int controlY = MARGIN;

        // Play
        myCastle = new EditCastleControl(
                this,
                "Your army",
                MARGIN,
                controlY,
                controlX - (MARGIN * 2),
                controlHeight);

        add(myCastle);

        // Catagories of units
        categoryControl = new EditCategoryControl(
                this,
                "Unit categories",
                controlX + MARGIN,
                controlY,
                controlX - (MARGIN * 2),
                (int) (controlHeight * 0.6) - MARGIN);

        add(categoryControl);

        // Units
        newUnits = new EditNewUnitControl(
                this,
                "Available units",
                controlX + MARGIN,
                (int) (controlHeight * 0.6) + MARGIN,
                controlX - (MARGIN * 2),
                (int) (controlHeight * 0.4));

        add(newUnits);

        // Stats
        statsBoard = new EditCastleBoard(
                this,
                "",
                controlX * 2 + MARGIN,
                controlY,
                controlX - (MARGIN * 2),
                ((controlHeight / 4) * 3) - MARGIN,
                Constants.IMG_EDIT_STAT_PANEL);
        stats = new EditCastleStats(statsBoard);
        statsBoard.add(stats);
        buttons = new EditButtonPanel(statsBoard, stats);
        addButton = new EditAddButton(statsBoard, this);
        goldPanel = new EditGoldPanel(statsBoard, this);
        sellButton = new SellButton(statsBoard, this);
        buyButton = new BuyButton(statsBoard, this);
        statsBoard.add(addButton);
        statsBoard.add(goldPanel);
        statsBoard.add(sellButton);
        statsBoard.add(buyButton);
        statsBoard.add(buttons);
        add(statsBoard);


        // End
        LeoContainer end = new LeoContainer(
                controlX * 2 + (MARGIN) - 3,
                ((controlHeight / 4) * 3) + MARGIN - 6,
                controlX - (MARGIN * 2),
                controlHeight / 4);


        int endX = MARGIN;
        int endHeight = ((end.getHeight() - (MARGIN * 2)) / 4);
        int endY = (end.getHeight() / 4);

        EditSaveButton save = new EditSaveButton(
                0,
                MARGIN,
                end.getWidth(),
                endHeight);
        EditSellExtraButton extra = new EditSellExtraButton(
                this,
                0,
                endY + MARGIN,
                end.getWidth(),
                endHeight);
        EditClearButton clear = new EditClearButton(
                this,
                0,
                endY * 2 + MARGIN,
                end.getWidth(),
                endHeight);
        EditCancelButton cancel = new EditCancelButton(
                0,
                (endY * 3) + MARGIN,
                end.getWidth(),
                endHeight);

        end.add(cancel);
        end.add(clear);
        end.add(extra);
        end.add(save);
        add(end);

    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(boolean adding) {
        selectedUnit = null;
        //category = -1;
        myCastle.initialize();

        stats.initialize(null);

        buttons.initialize(null, true);

        //categoryControl.initialize();
        newUnits.initialize(category, false);

        addButton.initialize(adding, null);

        sellButton.initialize(null);
        buyButton.initialize(null);

    }


    /////////////////////////////////////////////////////////////////
    // Set the selected unit
    /////////////////////////////////////////////////////////////////
    public void setSelectedUnit(boolean adding, Unit unit) {
        selectedUnit = unit;
        stats.initialize(unit);
        buttons.initialize(unit, true);
        addButton.initialize(adding, unit);
        sellButton.initialize(unit);
        buyButton.initialize(unit);
        newUnits.setSelectedUnit(unit);
    }


    /////////////////////////////////////////////////////////////////
    // Get the selected unit
    /////////////////////////////////////////////////////////////////
    public Unit getSelectedUnit() {
        return selectedUnit;
    }


    /////////////////////////////////////////////////////////////////
    // Set the selected category
    /////////////////////////////////////////////////////////////////
    public void setSelectedCategory(int newCategory) {
        category = newCategory;
        newUnits.initialize(category, true);
    }


    /////////////////////////////////////////////////////////////////
    // Get the selected unit
    /////////////////////////////////////////////////////////////////
    public int getSelectedCategory() {
        return category;
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle
    /////////////////////////////////////////////////////////////////
    public Castle getCastle() {
        return Client.getGameData().getArmy();
    }


    /////////////////////////////////////////////////////////////////
    // Draw the frame
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        Image img = Client.getImages().getImage(Constants.IMG_BACK_PANEL_EDIT);
        g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
        super.draw(g, mainFrame);

    }
}
