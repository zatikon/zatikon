///////////////////////////////////////////////////////////////////////
// Name: ChatBox
// Desc: Chat output field (new chat interface)
// Date: 8/19/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;
import java.util.Vector;


public class ChatBox extends LeoComponent {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    private final int WIDTH = 190;
    private final int HEIGHT = 450;
    private final int MAX_LENGTH = 35;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private final PlayerPanel parent;
    private int location;
    Vector<String> messages;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////

    public ChatBox(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;

        messages = new Vector<String>(); // All messages since login
        location = 0;                    // current position in viewing messages
    }

    /////////////////////////////////////////////////////////////////
    // Public Methods
    /////////////////////////////////////////////////////////////////

    public void addMessage(String message) {
        if (message.length() > 0) {
            messages.add(message);
            // if the client is viewing the buttom most chunk of text, automatically scroll
            //  down when new text is added
            if (messages.size() == location + MAX_LENGTH + 1) location++;
        }
    }

    // insert message at specific points in text
    public void insertMessage(String message, int index) {
        if (message.length() > 0) {
            messages.insertElementAt(message, index);
            if (messages.size() == location + MAX_LENGTH + 1) location++;
        }
    }

    // scroll up, if possible
    public void up() {
        if (location > 0) location--;
    }

    // scroll down, if possible
    public void down() {
        if (location < (messages.size() - MAX_LENGTH)) location++;
    }

    // function for if up or down arrows are pressed (scrolls)
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
        return true;
    }

    // used for text bounding
    // finds the index of the space after each word
    private Vector<Integer> findWords(String text) {
        Vector<Integer> words = new Vector<Integer>();
        for (int i = 1; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                words.add(i - 1);
            }
        }
        words.add(text.length() - 1);
        return words;
    }

    // text bounding:
    //  does it by words unless there are really long words
    //  then it does it by characters
    //  makes direct adjjustments to the message vector
    private void textBind(Graphics2D g) {
        g.setFont(Client.getFont());
        g.setColor(Color.white);
        FontMetrics fontMetrics = g.getFontMetrics();
        for (int i = 0; i < messages.size(); i++) {
            if (fontMetrics.stringWidth(messages.elementAt(i)) > WIDTH) {
                String old = messages.elementAt(i);
                Vector<Integer> words = findWords(old);
                if (words.size() < old.length() / 10) {
                    for (int sub = 15; sub < old.length(); sub++) {
                        if (fontMetrics.stringWidth(old.substring(0, sub)) > WIDTH) {
                            messages.setElementAt(old.substring(0, sub), i);
                            insertMessage(old.substring(sub), i + 1);
                            sub = 1000;
                        }
                    }
                } else {
                    for (int word = 1; word < words.size(); word++) {
                        int index = words.elementAt(word);
                        if (fontMetrics.stringWidth(old.substring(0, index)) > WIDTH) {
                            messages.setElementAt(old.substring(0, (words.elementAt(word - 1) + 1)), i);
                            insertMessage(old.substring(words.elementAt(word - 1) + 1), i + 1);
                            word = 1000;
                        }
                    }
                }
            }

        }
    }

    /////////////////////////////////////////////////////////////////
    // Gets and Sets
    /////////////////////////////////////////////////////////////////

    public int getLocation() {
        return location;
    }

    public int getLength() {
        return messages.size();
    }

    public int getMaxLength() {
        return MAX_LENGTH;
    }

    public Vector<String> getChat() {
        return messages;
    }

    public void setLocation(int newLocation) {
        location = newLocation;
    }

    public void setChat(Vector<String> chat) {
        messages = chat;
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.drawImage(Client.getImages().getImage(Constants.IMG_CHAT), getScreenX(), getScreenY(), getWidth() + 20, getHeight(), mainFrame);
        g.drawImage(Client.getImages().getImage(Constants.IMG_SCROLL), getScreenX() + 203, getScreenY() + 30, 21, 542, mainFrame);
        g.setFont(Client.getFont());
        g.setColor(Color.white);
        textBind(g);
        int line = 0;
        // Draw the text from location to the end of message or to ( location + MAX_LENGTH )
        for (int index = location; (index < messages.size() && index < (location + MAX_LENGTH)); index++) {
            String message = messages.elementAt(index);
            g.drawString(message, getScreenX() + 6, getScreenY() + 50 + (15 * line));
            line++;
        }

    }
}
