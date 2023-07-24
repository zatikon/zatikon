///////////////////////////////////////////////////////////////////////
// Name: TutorialButton
// Desc: Previous and Next buttons for the Tutorial
// Date: 12/9/2010 - Created [Dan Healy]
// Date: 4/21/2011 - Added Tip Index Buttons for accessing old tips 
//                   in-game [Julian Noble]
// Note: ONLY FOR USE WITHIN TUTORIAL BOARD
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class TutorialButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    String msg;
    int varient;
    int btnImg;
    int btnImg_Hl;
    boolean highlighted = false;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TutorialButton(int x, int y, int width, int height, int status) {
        super(x, y, width, height);
        varient = status;
        switch (status) {
            case 0: //previous
                msg = "";
                btnImg = Constants.IMG_PREV;
                btnImg_Hl = Constants.IMG_PREV_HIGHLIGHTED;
                break;
            case 1:
                msg = "";
                btnImg = Constants.IMG_JUMP_PREV;
                btnImg_Hl = Constants.IMG_JUMP_PREV_HIGHLIGHTED;
                break;
            case 2:
                msg = "";
                btnImg = Constants.IMG_NEXT;
                btnImg_Hl = Constants.IMG_NEXT_HIGHLIGHTED;
                break;
            case 3:
                msg = "";
                btnImg = Constants.IMG_JUMP_NEXT;
                btnImg_Hl = Constants.IMG_JUMP_NEXT_HIGHLIGHTED;
                break;
            case 4:
                msg = "";
                btnImg = Constants.IMG_CLOSE;
                btnImg_Hl = Constants.IMG_CLOSE_HIGHLIGHTED;
                break;
            case 5:
                msg = "1";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            case 6:
                msg = "2";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            case 7:
                msg = "3";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            case 8:
                msg = "4";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            case 9:
                msg = "5";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            case 10:
                msg = "6";
                btnImg = Constants.IMG_INDEX;
                btnImg_Hl = Constants.IMG_INDEX_HIGHLIGHTED;
                break;
            default:
                msg = "Error";
                btnImg = 0;
                btnImg_Hl = 0;
                break;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Click code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);

        switch (varient) {
            case 0:
                ((TutorialBoard) getParent()).prev();
                break;
            case 1:
                ((TutorialBoard) getParent()).prev();
                break;
            case 2:
                ((TutorialBoard) getParent()).next();
                break;
            case 3:
                ((TutorialBoard) getParent()).next();
                break;
            case 4:
                ((TutorialBoard) getParent()).close();
                break;
            case 5:
                ((TutorialBoard) getParent()).ti(1);
                break;
            case 6:
                ((TutorialBoard) getParent()).ti(5);
                break;
            case 7:
                ((TutorialBoard) getParent()).ti(8);
                break;
            case 8:
                ((TutorialBoard) getParent()).ti(17);
                break;
            case 9:
                ((TutorialBoard) getParent()).ti(21);
                break;
            case 10:
                ((TutorialBoard) getParent()).ti(35);
                break;
            default:
                break;
        }
        return true;
    }

    public boolean within(int x, int y) {
        return isWithin(x, y);
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(msg) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        // If button image exists, draw it
        if (btnImg != 0) {
            Image img;
            img = Client.getImages().getImage(btnImg);
            g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
        }
        // Otherwise draw default white box
        else {
            g.setColor(Color.white);
            g.fillRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        }
        g.setColor(Color.black);

        // If the mouse is within the bounds
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) || highlighted) {
            // If highlighted button image exists, draw it
            if (btnImg_Hl != 0) {
                Image img;
                img = Client.getImages().getImage(btnImg_Hl);
                g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
            }
            // Otherwise just darken outline
            else {
                g.drawRect(getScreenX() - 1, getScreenY() - 1, getWidth() + 1, getHeight() + 1);
            }
        }

        g.drawRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.drawRect(getScreenX() + 1, getScreenY() + 1, getWidth() - 3, getHeight() - 3);

        g.setColor(Color.black);
        g.drawString(msg, atX + 1, atY);
        g.drawString(msg, atX, atY + 1);
        g.drawString(msg, atX - 1, atY);
        g.drawString(msg, atX, atY - 1);

        g.setColor(Color.white);
        g.drawString(msg, atX, atY);
    }
}

