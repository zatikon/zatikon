///////////////////////////////////////////////////////////////////////
// Name: StatPanel
// Desc: The panel displaying a unit's stats
// Date: 5/23/2003 - Gabe Jones
// Date: 7/6/2011  - Added stat icons with mouse over name [Julian Noble]
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class StatPanel extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public StatPanel(LeoContainer newParent) {
        super(SideBoard.MARGIN, SideBoard.MARGIN, newParent.getWidth() - (SideBoard.MARGIN * 2), 100);
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            Unit unit = Client.getGameData().getSelectedUnit();
            if (unit == null) return;

            // Draw the box
            //g.setColor(Color.lightGray);
            //g.fillRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.setColor(Color.black);
            //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
            //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);
            Image image = Client.getImages().getImage(Constants.IMG_STAT_PANEL);
            g.drawImage(image, getScreenX(), getScreenY(), mainFrame);

            int lines = 1;

            // The name
            g.setColor(
                    unit.getCastle() == Client.getGameData().getEnemyCastle() ?
                            Color.red :
                            Color.white);
            g.drawString(unit.getName(), getScreenX() + SideBoard.MARGIN, getScreenY() + SideBoard.MARGIN + Client.FONT_HEIGHT);
            g.setColor(Color.white);
            lines++;

            // The stats
            Image icon, back;
            int posX, posY, mouseX, mouseY;

            back = Client.getImages().getImage(Constants.IMG_DRAW);

            mouseX = Client.getGameData().getMouseX();
            mouseY = Client.getGameData().getMouseY();

            // Actions if applicable
            if (unit.getActionsMax() > 0) {
                icon = Client.getImages().getImage(Constants.IMG_ICON_ACTIONS);

                posX = getScreenX() + SideBoard.MARGIN + 5;
                posY = getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 2) && mouseX <= (posX + 15) && mouseY >= (posY - 2) && mouseY <= (posY + 15)) {
                    g.drawImage(back, posX - 110, posY - 3, mainFrame);

                    g.drawString("Actions",
                            getScreenX() + SideBoard.MARGIN - 95,
                            getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                }
    /*g.drawString("Actions",
    getScreenX()+SideBoard.MARGIN,
    getScreenY()+((SideBoard.MARGIN+Client.FONT_HEIGHT)*lines) );*/

                g.drawString("" + unit.getActionsLeft() + "/" + unit.getActionsMax(),
                        getScreenX() + SideBoard.MARGIN + 30,
                        getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Life
            if (unit.getLife() > 0) {
                icon = Client.getImages().getImage(Constants.IMG_ICON_LIFE);

                posX = getScreenX() + SideBoard.MARGIN + 5;
                posY = getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 2) && mouseX <= (posX + 15) && mouseY >= (posY - 2) && mouseY <= (posY + 15)) {
                    g.drawImage(back, posX - 110, posY - 3, mainFrame);

                    g.drawString("Life",
                            getScreenX() + SideBoard.MARGIN - 95,
                            getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                }
    
    /*g.drawString("Life",
    getScreenX()+SideBoard.MARGIN,
    getScreenY()+((SideBoard.MARGIN+Client.FONT_HEIGHT)*lines) );*/

                g.drawString("" + unit.getLife() + "/" + unit.getLifeMax(),
                        getScreenX() + SideBoard.MARGIN + 30,
                        getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Damage if applicable
            if (unit.getDamage() > 0) {
                icon = Client.getImages().getImage(Constants.IMG_ICON_POWER);

                posX = getScreenX() + SideBoard.MARGIN + 5;
                posY = getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 2) && mouseX <= (posX + 15) && mouseY >= (posY - 2) && mouseY <= (posY + 15)) {
                    g.drawImage(back, posX - 110, posY - 3, mainFrame);

                    g.drawString("Power",
                            getScreenX() + SideBoard.MARGIN - 95,
                            getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                }
    
    /*g.drawString("Power",
    getScreenX()+SideBoard.MARGIN,
    getScreenY()+((SideBoard.MARGIN+Client.FONT_HEIGHT)*lines) );*/

                g.drawString("" + unit.getDamage(),
                        getScreenX() + SideBoard.MARGIN + 30,
                        getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Armor if applicable
            if (unit.getArmor() > 0) {
                icon = Client.getImages().getImage(Constants.IMG_ICON_ARMOR);

                posX = getScreenX() + SideBoard.MARGIN + 5;
                posY = getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines) - 11;

                g.drawImage(icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 2) && mouseX <= (posX + 15) && mouseY >= (posY - 2) && mouseY <= (posY + 15)) {
                    g.drawImage(back, posX - 110, posY - 3, mainFrame);

                    g.drawString("Armor",
                            getScreenX() + SideBoard.MARGIN - 95,
                            getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                }
    /*g.drawString("Armor",
    getScreenX()+SideBoard.MARGIN,
    getScreenY()+((SideBoard.MARGIN+Client.FONT_HEIGHT)*lines) );*/

                g.drawString("" + unit.getArmor(),
                        getScreenX() + SideBoard.MARGIN + 30,
                        getScreenY() + ((SideBoard.MARGIN + Client.FONT_HEIGHT) * lines));
                lines++;
            }

            // Murderer
            if (unit.isMurderer()) {
                icon = Client.getImages().getImage(Constants.IMG_ICON_MURDERER);

                posX = getScreenX() + SideBoard.MARGIN + 150;
                posY = getScreenY() + SideBoard.MARGIN + Client.FONT_HEIGHT - 11;

                g.drawImage(icon, posX, posY, mainFrame);

                // Check for mouse over the icon
                if (mouseX >= (posX - 2) && mouseX <= (posX + 15) && mouseY >= (posY - 2) && mouseY <= (posY + 15)) {
                    g.drawImage(back, posX - 85, posY - 3, mainFrame);

                    g.drawString("Murderer",
                            posX - 75,
                            posY + 11);
                }

                g.setColor(Color.black);
            }
        } catch (Exception e) {
            System.out.println("StatPanel.draw(): " + e);
        }

    }

}
