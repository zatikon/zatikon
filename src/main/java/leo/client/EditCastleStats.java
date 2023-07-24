///////////////////////////////////////////////////////////////////////
// Name: EditCastleStats
// Desc: The panel displaying a unit's stats
// Date: 5/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class EditCastleStats extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Unit unit;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EditCastleStats(LeoContainer newParent) {
        super(EditCastlePanel.MARGIN, EditCastlePanel.MARGIN, newParent.getWidth() - (EditCastlePanel.MARGIN * 2), 100);
    }


    /////////////////////////////////////////////////////////////////
    // Initialize
    /////////////////////////////////////////////////////////////////
    public void initialize(Unit newUnit) {
        unit = newUnit;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {

            if (unit == null) return;

            Image img = Client.getImages().getImage(Constants.IMG_EDIT_STAT_BOX);
            g.drawImage(img, getScreenX(), getScreenY(), mainFrame);

            int lines = 1;
            g.setColor(Color.white);
            g.drawString(unit.getName(), getScreenX() + EditCastlePanel.MARGIN, getScreenY() + EditCastlePanel.MARGIN + Client.FONT_HEIGHT);
            lines++;

            // Draw the appearance
            int iconX = getScreenX() + getWidth() - ((EditCastlePanel.MARGIN * 4) + Constants.SQUARE_SIZE + 1);
            int iconY = getScreenY() + (EditCastlePanel.MARGIN * 2);
            g.setColor(Color.white);
            g.fillRect(iconX, iconY, Constants.SQUARE_SIZE, Constants.SQUARE_SIZE);
            g.setColor(Color.black);
            g.drawRect(iconX - 1, iconY - 1, Constants.SQUARE_SIZE + 1, Constants.SQUARE_SIZE + 1);
            g.drawRect(iconX - 2, iconY - 2, Constants.SQUARE_SIZE + 3, Constants.SQUARE_SIZE + 3);
            Image image = Client.getImages().getImage(unit.getAppearance());
            g.drawImage(image, iconX, iconY, mainFrame);

            g.setColor(Color.white);

            // Draw stats

            Image stat_icon, back;
            int posX, posY, mouseX, mouseY;

            back = Client.getImages().getImage(Constants.IMG_DRAW);

            mouseX = Client.getGameData().getMouseX();
            mouseY = Client.getGameData().getMouseY();

            // Actions if applicable
            if (unit.getActionsMax() > 0) {
                stat_icon = Client.getImages().getImage(Constants.IMG_ICON_ACTIONS);

                posX = getScreenX() + EditCastlePanel.MARGIN + 5;
                posY = getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(stat_icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 1) && mouseX <= (posX + 14) && mouseY >= (posY - 1) && mouseY <= (posY + 13)) {
                    g.drawImage(back, posX + 55, posY - 3, mainFrame);
                    g.drawString("Actions", posX + 65, posY + 11);
                }
    
    /*g.drawString("Actions",
    getScreenX()+EditCastlePanel.MARGIN,
    getScreenY()+((EditCastlePanel.MARGIN+Client.FONT_HEIGHT)*lines) );*/
                g.drawString("" + unit.getActionsLeft() + "/" + unit.getActionsMax(),
                        getScreenX() + EditCastlePanel.MARGIN + 30,
                        getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Life
            if (unit.getLife() > 0) {
                stat_icon = Client.getImages().getImage(Constants.IMG_ICON_LIFE);

                posX = getScreenX() + EditCastlePanel.MARGIN + 5;
                posY = getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(stat_icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 1) && mouseX <= (posX + 14) && mouseY >= (posY - 1) && mouseY <= (posY + 13)) {
                    g.drawImage(back, posX + 55, posY - 3, mainFrame);
                    g.drawString("Life", posX + 65, posY + 11);
                }
    
     /*g.drawString("Life",
      getScreenX()+EditCastlePanel.MARGIN,
      getScreenY()+((EditCastlePanel.MARGIN+Client.FONT_HEIGHT)*lines) );*/
                g.drawString("" + unit.getLife() + "/" + unit.getLifeMax(),
                        getScreenX() + EditCastlePanel.MARGIN + 30,
                        getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Damage if applicable
            if (unit.getDamage() > 0) {
                stat_icon = Client.getImages().getImage(Constants.IMG_ICON_POWER);

                posX = getScreenX() + EditCastlePanel.MARGIN + 5;
                posY = getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(stat_icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 1) && mouseX <= (posX + 14) && mouseY >= (posY - 1) && mouseY <= (posY + 13)) {
                    g.drawImage(back, posX + 55, posY - 3, mainFrame);
                    g.drawString("Power", posX + 65, posY + 11);
                }
    
    /*g.drawString("Power",
    getScreenX()+EditCastlePanel.MARGIN,
    getScreenY()+((EditCastlePanel.MARGIN+Client.FONT_HEIGHT)*lines) );*/
                g.drawString("" + unit.getDamage(),
                        getScreenX() + EditCastlePanel.MARGIN + 30,
                        getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Armor if applicable
            if (unit.getArmor() > 0) {
                stat_icon = Client.getImages().getImage(Constants.IMG_ICON_ARMOR);

                posX = getScreenX() + EditCastlePanel.MARGIN + 5;
                posY = getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(stat_icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 1) && mouseX <= (posX + 14) && mouseY >= (posY - 1) && mouseY <= (posY + 13)) {
                    g.drawImage(back, posX + 55, posY - 3, mainFrame);
                    g.drawString("Armor", posX + 65, posY + 11);
                }
    
    /*g.drawString("Armor",
    getScreenX()+EditCastlePanel.MARGIN,
    getScreenY()+((EditCastlePanel.MARGIN+Client.FONT_HEIGHT)*lines) );*/
                g.drawString("" + unit.getArmor(),
                        getScreenX() + EditCastlePanel.MARGIN + 30,
                        getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Deploy
            String deployAdd = "";
            if (unit.getDeployRange() > 1) {
                deployAdd = "  (range " + unit.getDeployRange() + ")";
            }

            stat_icon = Client.getImages().getImage(Constants.IMG_ICON_DEPLOY);

            posX = getScreenX() + EditCastlePanel.MARGIN + 5;
            posY = getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

            g.drawImage(stat_icon, posX, posY, mainFrame);

            // Check for mouse over the icon
            if (mouseX >= (posX - 1) && mouseX <= (posX + 14) && mouseY >= (posY - 1) && mouseY <= (posY + 13)) {
                g.drawImage(back, posX + 55, posY - 3, mainFrame);
                g.drawString("Deploy", posX + 65, posY + 11);
            }
   
  /*g.drawString("Deploy",
   getScreenX()+EditCastlePanel.MARGIN,
   getScreenY()+((EditCastlePanel.MARGIN+Client.FONT_HEIGHT)*lines) );*/
            g.drawString("" + unit.getDeployCost() + deployAdd,
                    getScreenX() + EditCastlePanel.MARGIN + 30,
                    getScreenY() + ((EditCastlePanel.MARGIN + Client.FONT_HEIGHT) * lines));
            lines++;
        } catch (Exception e) {
            System.out.println("EditCastleStats.draw(): " + e);
        }

    }

}
