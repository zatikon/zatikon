///////////////////////////////////////////////////////////////////////
// Name: ChatPlayer
// Desc: A chatting player
// Date: 9/2/2003 - Gabe Jones
//  10/19/2010 - Tony Schwartz
//   Added Mirrored Random Mode
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;

public class ChatPlayer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final int id;
    private final String name;
    private int rating;
    private int rank = -1;
    private MessageFrame messageFrame = null;
    private int icon = Constants.IMG_USER_ICON;
    private int wins;
    private int losses;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatPlayer(int newId, String newName, int newRating, int newRank, int newWins, int newLosses) {
        id = newId;
        name = newName;
        rating = newRating;
        rank = newRank;
        wins = newWins;
        losses = newLosses;
    }


    /////////////////////////////////////////////////////////////////
    // close chat
    /////////////////////////////////////////////////////////////////
    public void closeChat() {
        if (messageFrame != null) {
            messageFrame.disable(getName() + " has left the game.");
        }
    }


    /////////////////////////////////////////////////////////////////
    // close chat
    /////////////////////////////////////////////////////////////////
    public void killChat() {
        if (messageFrame != null) {
            messageFrame.dispose();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Receive text from this player
    /////////////////////////////////////////////////////////////////
    public void receive(String text) {
        getMessageFrame().showText(getName() + ": ", text);
        getMessageFrame().bump();
    }


    /////////////////////////////////////////////////////////////////
    // Get message frame
    /////////////////////////////////////////////////////////////////
    public MessageFrame getMessageFrame() {
        if (messageFrame == null) {
            messageFrame = new MessageFrame(this);
        }
        return messageFrame;
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public Image getIcon() {
        return Client.getImages().getImage(icon);
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getRating() {
        return rating;
    }

    public int getRank() {
        return rank;
    }

    public String getWinsLosses() {
        return "Wins:" + wins + " Losses:" + losses;
    }    

    public String toString() {
        if (getRating() > 0)
            return getName() + ", " + (rank == 0 ? "Unranked" : "#" + rank + " (" + getRating() + ")");
            //return getName() + ", " + "#" + rank + " (Rating: " + getRating() + ")";
        else
            return getName() + " (demo)";
    }


    /////////////////////////////////////////////////////////////////
    // Set rating
    /////////////////////////////////////////////////////////////////
    public void setRating(int newRating) {
        rating = newRating;
    }

    /////////////////////////////////////////////////////////////////
    // Set rank
    /////////////////////////////////////////////////////////////////
    public void setRank(int newRank) {
        rank = newRank;
    }

    /////////////////////////////////////////////////////////////////
    // Set icon state
    /////////////////////////////////////////////////////////////////
    public void setState(short newState) {
        switch (newState) {
            case Action.CHAT_CHATTING:
                icon = Constants.IMG_USER_ICON;
                break;

            case Action.CHAT_CRU:
                icon = Constants.IMG_USER_ICON_CRU;
                break;

            case Action.CHAT_LEG:
                icon = Constants.IMG_USER_ICON_LEG;
                break;

            case Action.CHAT_CRU_LEG:
                icon = Constants.IMG_USER_ICON_CRU_LEG;
                break;

   /*case Action.CHAT_EDITING:
    System.out.println("CHANGING THE ICON");
    icon = Constants.IMG_BATTLE_ICON;
    break;*/

            case Action.CHAT_FIGHTING_SINGLE:
                icon = Constants.IMG_SINGLE_ICON;
                break;

            case Action.CHAT_FIGHTING_CONS:
                icon = Constants.IMG_CONSTRUCTED_ICON;
                break;

            case Action.CHAT_WAITING_CONS:
                icon = Constants.IMG_WAITING_CONSTRUCTED_ICON;
                break;

            case Action.CHAT_FIGHTING_COOP:
                icon = Constants.IMG_COOPERATIVE_ICON;
                break;

            case Action.CHAT_WAITING_COOP:
                icon = Constants.IMG_WAITING_COOPERATIVE_ICON;
                break;

            case Action.CHAT_FIGHTING_RAND:
                icon = Constants.IMG_RANDOM_ICON;
                break;

            case Action.CHAT_WAITING_RAND:
                icon = Constants.IMG_WAITING_RANDOM_ICON;
                break;

            case Action.CHAT_FIGHTING_MIRR_RAND:
                icon = Constants.IMG_RANDOM_ICON;
                break;

            case Action.CHAT_FIGHTING_2V2:
                icon = Constants.IMG_TEAM_ICON;
                break;

            case Action.CHAT_WAITING_2V2:
                icon = Constants.IMG_WAITING_TEAM_ICON;
                break;

            case Action.CHAT_EDIT:
                icon = Constants.IMG_EDIT_ICON;
                break;

            case Action.IDLE:
                icon = Constants.IMG_IDLE_ICON;
                break;

            case Action.CHAT_DISABLE:
                icon = Constants.IMG_DISABLE;
                closeChat();
                break;
        }
    }

}
