///////////////////////////////////////////////////////////////////////
// Name: TeamStartButton
// Desc: Start the game
// Date: 9/13/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;


public class TeamStartButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final TeamLoadingPanel parent;
    private String clientName;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TeamStartButton(int x, int y, int width, int height, TeamLoadingPanel newParent) {
        super(x, y, width, height);
        parent = newParent;
        clientName = Client.getName().toLowerCase();
        clientName = clientName;
    }

    /////////////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        int t1 = parent.getTeam1Size();
        int t2 = parent.getTeam2Size();
        if (t1 == 2 && t2 == 2 && parent.contains(clientName)) {
            Client.getNetManager().sendAction(Action.START_TEAM_GAME, Action.NOTHING, Action.NOTHING);
            parent.clearTeams();
        }
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the text
    /////////////////////////////////////////////////////////////////
    private String getText() {
        return "";
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        //g.drawString(clientName, getScreenX() - 10, getScreenY() - 30);

        int t1 = parent.getTeam1Size();
        int t2 = parent.getTeam2Size();
        if (t1 == 2 && t2 == 2) {
            // If the mouse is within the bounds
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && parent.contains(clientName)) {
                g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM_HIGHLIGHT), getScreenX() - 1, getScreenY(), 130, 130, mainFrame);
            } else
                g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM), getScreenX() - 1, getScreenY(), 130, 130, mainFrame);
        } else {
            g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM_NOT_READY), getScreenX(), getScreenY(), 130, 130, mainFrame);
            switch (t1) {
                case 1:
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SWORD_1), getScreenX() - 53, getScreenY() + 15, 39, 93, mainFrame);
                    break;
                case 2:
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SWORD_2), getScreenX() - 53, getScreenY() + 15, 39, 93, mainFrame);
                    break;
                default:
                    break;
            }
            switch (t2) {
                case 1:
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SWORD_1), getScreenX() + getWidth() + 12, getScreenY() + 15, 39, 93, mainFrame);
                    break;
                case 2:
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SWORD_2), getScreenX() + getWidth() + 12, getScreenY() + 15, 39, 93, mainFrame);
                    break;
                default:
                    break;
            }
        }

    }

}
