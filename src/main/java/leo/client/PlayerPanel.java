///////////////////////////////////////////////////////////////////////
// Name: PlayerPanel
// Desc: Panel that has two tabs for chat and player list (new chat interface)
// Date: 8/26/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;
import java.util.Vector;


public class PlayerPanel extends LeoContainer {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean chatting = true;

    // chat properties
    private final ChatInput chatline;            // text input field
    private final ChatBox chatbox;               // box that displays text
    private final ChatScrollBar chatscroll;      // scroll bar
    private final ChatUpButton cUp;              // up arrow for scrolling
    private final ChatDownButton cDown;          // down arrow for scrolling

    // player list properties
    private final PlayerList players;            // contains list of players
    private final PlayerScrollBar playerscroll;  // scroll bar
    private final ChatUpButton pUp;              // up arrow for scrolling
    private final ChatDownButton pDown;          // down arrow for scrolling

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public PlayerPanel() {
        super(Constants.SCREEN_WIDTH,
                0,
                Constants.WINDOW_WIDTH - Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT);


        // tab for chat
        ChatButton chatb = new ChatButton(4, 2, 100, 28, this);
        add(chatb);

        // tab for player list
        ListButton listb = new ListButton(104, 2, 100, 28, this);
        add(listb);

        // First add everything for the chat
        chatline = new ChatInput(0, 574, 224, 26, this);
        chatbox = new ChatBox(0, 0, 204, 600, this);
        cUp = new ChatUpButton(205, 33, 16, 16, this);
        cDown = new ChatDownButton(205, 552, 16, 16, this);
        chatscroll = new ChatScrollBar(0, 0, 16, 44, this);
        add(chatline);
        add(chatbox);
        add(chatscroll);
        add(cUp);
        add(cDown);

        // Create everything for player list
        players = new PlayerList(0, 0, 204, 600, this);
        playerscroll = new PlayerScrollBar(0, 0, 16, 44, this);
        pUp = new ChatUpButton(205, 33, 16, 16, this);
        pDown = new ChatDownButton(205, 574, 16, 16, this);

    }

    /////////////////////////////////////////////////////////////////
    // Public methods
    /////////////////////////////////////////////////////////////////

    public void updateList() {
        players.updateList();
    }

    // used by chat tab, removes player list components and adds chat components
    //  sets the boolean chatting to true
    public void chatFocus() {
        if (!chatting) {
            add(chatline);
            add(chatbox);
            add(chatscroll);
            add(cUp);
            add(cDown);

            remove(players);
            remove(playerscroll);
            remove(pUp);
            remove(pDown);
            chatting = true;
        }
    }

    // used by player list tab, removes chat components and adds player list components
    //  sets the boolean chatting to false
    public void listFocus() {
        if (chatting) {
            add(players);
            add(playerscroll);
            add(pUp);
            add(pDown);

            remove(chatscroll);
            remove(chatline);
            remove(chatbox);
            remove(cUp);
            remove(cDown);
            chatting = false;
        }
    }

    // scroll up for current tab
    public void up() {
        if (chatting) {
            chatbox.up();
        } else
            players.up();
    }

    // scroll down for current tab
    public void down() {
        if (chatting) {
            chatbox.down();
        } else
            players.down();
    }

    // used for scrolling with arrows
    public void keyPressed(int key) {
        if (chatting) {
            chatline.keyPressed(key);
            chatbox.keyPressed(key);
        } else
            players.keyPressed(key);
    }

    // for input messages
    public void keyTyped(int key) {
        if (chatting) {
            chatline.key(key);
        }
    }

    // for backspace
    public void keyBack() {
        if (chatting)
            chatline.back();
    }

    // for enter, sends the message
    public String keyEnter() {
        if (chatting) {
            String message = chatline.enter();
            //chatbox.addMessage(message);
            return message;
        } else {
            // don't return anything if not in the chat window
            return "";
        }
    }

    // mouse functions for scrollbar mostly
    public void mousePressed() {
        if (chatting) {
            chatscroll.mousePressed();
        } else
            playerscroll.mousePressed();
    }

    public void mouseReleased() {
        if (chatting) {
            chatscroll.mouseReleased();
        } else
            playerscroll.mouseReleased();
    }

    public void mouseDragged(int x, int y) {
        if (chatting) {
            chatscroll.mouseDragged(x, y);
        } else
            playerscroll.mouseDragged(x, y);
    }

    /////////////////////////////////////////////////////////////////
    // Gets and sets
    /////////////////////////////////////////////////////////////////

    public ChatBox getChatBox() {
        return chatbox;
    }

    public PlayerList getPlayerList() {
        return players;
    }

    public void getMessage(String message) {
        chatbox.addMessage(message);
    }

    public Vector<ChatPlayer> getPlayers() {
        return players.getPlayers();
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.drawImage(Client.getImages().getImage(Constants.IMG_PLAYERPANEL), getScreenX(), getScreenY(), 224, 600, mainFrame);
        super.draw(g, mainFrame);
    }

}
