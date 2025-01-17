///////////////////////////////////////////////////////////////////////
// Name: PlayerList
// Desc: List of players (new chat interface)
// Date: 8/23/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.*;
import java.util.Vector;


public class PlayerList extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    private final int WIDTH = 227;
    private final int HEIGHT = 450;
    private final int MAX_LENGTH = 16;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private final PlayerPanel parent;
    private int location;
    Vector<ChatPlayer> players;
    private int xx = 0;
    private int yy = 0;
    private int selection;
    private int clicked;
    Color cornflowerBlue = new Color(0.292156863f, 0.484313725f, 1.0f, 1.0f);

    String clientName = "";
    String playerName = "";

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PlayerList(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;

        players = Client.getPlayers();
        location = 0;
        clicked = 0;
    }

    /////////////////////////////////////////////////////////////////
    // Public Methods
    /////////////////////////////////////////////////////////////////

    public void updateList() {
        players = Client.getPlayers();
    }

    public void up() {
        if (location > 0) location--;
    }

    public void down() {
        if (location < (players.size() - MAX_LENGTH)) location++;
    }


    public void keyPressed(int pressedKey) {
        switch (pressedKey) {
            case 38: // up KeyEvent.VK_UP
                up();
                break;
            case 40: // down KeyEvent.VK_DOWN
                down();
                break;
            default:
                break;
        }
    }

    public boolean clickAt(int x, int y) {
        xx = x - getScreenX();
        yy = y - getScreenY();
        selection = (y - 32 - getScreenY()) / 35;

        // if double clicked
        if (clicked > 0 && selection < (players.size() - location)) {
            ChatPlayer player = players.elementAt(selection + location);
            // check your name and selected player's name
            clientName = Client.getName().toLowerCase();
            playerName = player.getName().toLowerCase();
            // dont message yourself
            if (!clientName.equals(playerName)) {
                MessageFrame mf = player.getMessageFrame();
                mf.setVisible(true);
                mf.focus();
            }
        }
        clicked = 5;
        return true;
    }

    /////////////////////////////////////////////////////////////////
    // Gets and Sets
    /////////////////////////////////////////////////////////////////

    public int getLocation() {
        return location;
    }

    public int getLength() {
        return players.size();
    }

    public int getMaxLength() {
        return MAX_LENGTH;
    }

    public Vector<ChatPlayer> getPlayers() {
        return players;
    }

    public void setLocation(int newLocation) {
        location = newLocation;
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        try {
            if (location > players.size()) {
                location = 0;
            }
            // count down from when the last click was
            if (clicked > 0)
                clicked--;
            // check to see if they selected a possible player
            //  if they have draw a rectangle with a border
            if (selection < (players.size() - location)) {
                Composite original = g.getComposite();
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                g.setColor(cornflowerBlue);
                g.fillRect(getScreenX() + 4, getScreenY() + 32 + (selection * 35), 196, 35);
                g.setComposite(original);
                g.drawRect(getScreenX() + 4, getScreenY() + 32 + (selection * 35), 196, 35);
            }

            // draw frame and scroll bar frame
            g.drawImage(Client.getImages().getImage(Constants.IMG_PLAYERS), getScreenX(), getScreenY(), getWidth() + 20, getHeight(), mainFrame);
            g.drawImage(Client.getImages().getImage(Constants.IMG_SCROLL_PLAYER), getScreenX() + 203, getScreenY() + 31, 21, 563, mainFrame);
            g.setFont(Client.getFont());
            g.setColor(Color.white);

            // draw every players icon and name + rank
            int line = 0;
            for (int index = location; (index < players.size() && index < (location + MAX_LENGTH)); index++) {
                String playerNameRating = players.elementAt(index).toString().substring(0, 1).toUpperCase() + players.elementAt(index).toString().substring(1);
                String playerName = players.elementAt(index).getName().substring(0, 1).toUpperCase() + players.elementAt(index).getName().substring(1);
                 
                //check for hover on each player
                if (Client.getGameData().getMouseX() > getScreenX() + 3 && Client.getGameData().getMouseX() < getScreenX() + 200 && Client.getGameData().getMouseY() > getScreenY() + 34 + (35 * line) && Client.getGameData().getMouseY() < getScreenY() + 68 + (35 * line)) {
                    Client.getGameData().getRosterPanel().setMsgText(playerName + ", " + players.elementAt(index).getWinsLosses());
                }

                g.drawString(playerNameRating, getScreenX() + 45, getScreenY() + 54 + (35 * line));
                g.drawImage(players.elementAt(index).getIcon(), getScreenX() + 10, getScreenY() + 35 + (35 * line), 30, 30, mainFrame);
                line++;
            }
            // Debug info
            //g.drawString("x= " + xx + " y= " + yy, getScreenX() + 45, getScreenY() + 25 + (35*line));
            //g.drawString("clientName= " + clientName, getScreenX() + 45, getScreenY() + 205);
            //g.drawString("playerName= " + playerName, getScreenX() + 45, getScreenY() + 220);
        } catch (Exception e) {
            Logger.error("PlayerList.draw(): " + e);
        }
    }
}
