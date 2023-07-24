///////////////////////////////////////////////////////////////////////
// Name: TeamSelectButton
// Desc: Pick your side while in the team game lobby
// Date: 9/8/2011 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class TeamSelectButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean pushed = false;  // Is this team selected?
    private final short myTeam;             // The team for this button
    private final TeamLoadingPanel parent;
    private int clickDuration = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TeamSelectButton(int x, int y, int width, int height, short team, TeamLoadingPanel _parent) {
        super(x, y, width, height);
        myTeam = team;
        parent = _parent;
    }

    /////////////////////////////////////////////////////////////////
    // The button is clicked
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (clickDuration > 0) return true;
        clickDuration = 3;

        // If pushing on...
        if (!pushed) {
            pushed = true;
            // If player was already on the other team, move to this one
            switch (myTeam) {
                case Unit.TEAM_1:
                    parent.unPushTeam2();
                    break;
                case Unit.TEAM_2:
                    parent.unPushTeam1();
                    break;
                default:
                    break;
            }

            // Select this team
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.setComputing(false);
            Client.getNetManager().sendAction(Action.SELECT_TEAM, myTeam, Action.NOTHING);
        }

        // If pushing off...
        else {
            unPush();
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.setComputing(false);
            Client.getNetManager().sendAction(Action.SELECT_TEAM, Action.NOTHING, Action.NOTHING);
        }
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the text
    /////////////////////////////////////////////////////////////////
    private String getText() { //return pushed ? "Team " + myTeam + " Selected" : "Team " + myTeam + "";
        return "Team " + myTeam;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(getText()) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2);

        if (clickDuration > 0) {
            --clickDuration;
            g.setColor(Color.red);
        } else
            g.setColor(Color.white);

        g.fillRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.setColor(Color.black);

        // If the mouse is within the bounds, darken
        if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
            g.drawRect(getScreenX() - 1, getScreenY() - 1, getWidth() + 1, getHeight() + 1);
        }

        g.drawRect(getScreenX(), getScreenY(), getWidth() - 1, getHeight() - 1);
        g.drawRect(getScreenX() + 1, getScreenY() + 1, getWidth() - 3, getHeight() - 3);
        g.drawString(getText(), atX, atY);
    }


    //////////////////////////////////////////////////////////////////
    // unPush(): No longer have this team selected
    //////////////////////////////////////////////////////////////////
    public void unPush() {
        pushed = false;
    }
}

