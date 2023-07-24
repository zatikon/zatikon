///////////////////////////////////////////////////////////////////////
// Name: TeamLoadingPanel
// Desc: While waiting in the team game lobby, players pick teams
// Date: 9/8/2011 - W. Fletcher Cole
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;


public class TeamLoadingPanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    private String text;
    private final TeamSelectButton team1button;   // Pick team 1
    private final TeamSelectButton team2button;   // Pick team 2
    private final TeamStartButton teamStart;      // Start the game
    private String T1P1;
    private String T1P2;    // Names of the players
    private String T2P1;
    private String T2P2;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TeamLoadingPanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));

        // Cancel from this screen
        add(new CancelButton(
                (getWidth() / 2) - 50,
                getHeight() - 65,
                100,
                25));

        int w = (((Constants.SCREEN_WIDTH - (Constants.OFFSET * 2)) / 3) - (10));
        int h = ((((Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2)) - (10)) / 4) - (10)) / 4;
        int atX = getScreenX() + (getWidth() / 2) - 375;
        int atY = getScreenY() + (getHeight() / 2) - 40;

        // Select/De-select team 1
        team1button = new TeamSelectButton(
                atX + (w / 2) - 100 / 2,
                atY + 80,
                100,
                25,
                Unit.TEAM_1,
                this);
        atX += 495;

        // Select/De-select team 2
        team2button = new TeamSelectButton(
                atX + (w / 2) - 100 / 2,
                atY + 80,
                100,
                25,
                Unit.TEAM_2,
                this);

        // Button to start the game
        teamStart = new TeamStartButton(
                getScreenX() + (getWidth() / 2) - 68,
                getScreenY() + getHeight() / 2 - 55,
                130,
                130,
                this);

        add(team1button);
        add(team2button);
        add(teamStart);

        String T1P1 = null;
        String T1P2 = null;
        String T2P1 = null;
        String T2P2 = null;

        setText("");
    }


    /////////////////////////////////////////////////////////////////
    // Clear the teams
    /////////////////////////////////////////////////////////////////
    public void clearTeams() {
        String T1P1 = null;
        String T1P2 = null;
        String T2P1 = null;
        String T2P2 = null;
    }


    /////////////////////////////////////////////////////////////////
    // Put a player on a team
    /////////////////////////////////////////////////////////////////
    public void setTeam(String name, short position, short team) {
        if (team == Unit.TEAM_1) {
            if (position == 1)
                T1P1 = name;
            else if (position == 2)
                T1P2 = name;
        } else if (team == Unit.TEAM_2) {
            if (position == 1)
                T2P1 = name;
            else if (position == 2)
                T2P2 = name;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Check if a player is already listed
    /////////////////////////////////////////////////////////////////
    public boolean contains(String name) {
        return ((T1P1.substring(0, T1P1.indexOf(',')).toLowerCase()).equals(name) ||
                (T1P2.substring(0, T1P2.indexOf(',')).toLowerCase()).equals(name) ||
                (T2P1.substring(0, T2P1.indexOf(',')).toLowerCase()).equals(name) ||
                (T2P2.substring(0, T2P2.indexOf(',')).toLowerCase()).equals(name));
    }


    /////////////////////////////////////////////////////////////////
    // Get the message
    /////////////////////////////////////////////////////////////////
    private String getMessage() {
        return text;
    }


    /////////////////////////////////////////////////////////////////
    // Set the message
    /////////////////////////////////////////////////////////////////
    public void setText(String newText) {
        text = newText;
    }


    /////////////////////////////////////////////////////////////////
    // Number of players listed on team 1
    /////////////////////////////////////////////////////////////////
    public int getTeam1Size() {
        if (T1P1 == null || T1P2 == null) return 0;
        else if (T1P1 == "" && T1P2 == "") return 0;
        else if (T1P1 != "" && T1P2 != "") return 2;
        else return 1;
    }


    /////////////////////////////////////////////////////////////////
    // Number of players listed on team 2
    /////////////////////////////////////////////////////////////////
    public int getTeam2Size() {
        if (T2P1 == null || T2P2 == null) return 0;
        else if (T2P1 == "" && T2P2 == "") return 0;
        else if (T2P1 != "" && T2P2 != "") return 2;
        else return 1;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.setFont(Client.getFontBig());
        String text = getMessage();

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (20 / 2);

        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);
        g.setColor(Color.yellow);

        g.drawString(text, atX, atY);
        g.setFont(Client.getFont());

        int w = (((Constants.SCREEN_WIDTH - (Constants.OFFSET * 2)) / 3) - (10));
        int h = ((((Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2)) - (10)) / 4) - (10)) / 4;
        atX = getScreenX() + (getWidth() / 2) - 375;
        atY = getScreenY() + (getHeight() / 2) - 40;
        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_BOX), atX - 20, atY - 15, 324, 132, mainFrame);
        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_PLAYER), atX, atY, w, h, mainFrame);
        if (T1P1 != null && T1P1 != "") {
            g.drawString((T1P1.substring(0, 1).toUpperCase() + T1P1.substring(1)),
                    atX + (w / 2) - g.getFontMetrics().stringWidth(T1P1) / 2,
                    atY + (h / 2) + (Client.FONT_HEIGHT / 2));
        }
        atX += 495;

        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_BOX), atX - 50, atY - 15, 324, 132, mainFrame);
        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_PLAYER), atX, atY, w, h, mainFrame);
        if (T2P1 != null && T2P1 != "") {
            g.drawString((T2P1.substring(0, 1).toUpperCase() + T2P1.substring(1)),
                    atX + (w / 2) - g.getFontMetrics().stringWidth(T2P1) / 2,
                    atY + (h / 2) + (Client.FONT_HEIGHT / 2));
        }
        atY += 40;

        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_PLAYER), atX, atY, w, h, mainFrame);
        if (T2P2 != null && T2P2 != "") {
            g.drawString((T2P2.substring(0, 1).toUpperCase() + T2P2.substring(1)),
                    atX + (w / 2) - g.getFontMetrics().stringWidth(T2P2) / 2,
                    atY + (h / 2) + (Client.FONT_HEIGHT / 2));
        }
        atX -= 495;

        g.drawImage(Client.getImages().getImage(Constants.IMG_TEAMS_PLAYER), atX, atY, w, h, mainFrame);
        if (T1P2 != null && T1P2 != "") {
            g.drawString((T1P2.substring(0, 1).toUpperCase() + T1P2.substring(1)),
                    atX + (w / 2) - g.getFontMetrics().stringWidth(T1P2) / 2,
                    atY + (h / 2) + (Client.FONT_HEIGHT / 2));
        }

        super.draw(g, mainFrame);
    }


    //////////////////////////////////////////////////////////////////
    // unPush: No longer have this team selected
    //////////////////////////////////////////////////////////////////
    public void unPushTeam1() {
        team1button.unPush();
    }

    public void unPushTeam2() {
        team2button.unPush();
    }
}
