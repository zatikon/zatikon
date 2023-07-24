///////////////////////////////////////////////////////////////////////
// Name: ChatInput
// Desc: Chat input field
// Date: 8/18/2011 - Julian Noble
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ChatInput extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private final int CHAR_MAX = 200;
    private final int WIDTH = 210;
    private final int HEIGHT = 24;

    private final PlayerPanel parent;
    private final StringBuffer messageBuffer;
    private int cursor;
    private int cursorBlink;
    private int begin;
    private int end;

    private String lastMessage;

    // 242 x 23
    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatInput(int x, int y, int width, int height, PlayerPanel newParent) {
        super(x, y, width, height);
        parent = newParent;
        messageBuffer = new StringBuffer();
        cursor = 0;
        begin = 0;
        end = 0;
        cursorBlink = 0;

        lastMessage = "";
    }

    public void keyPressed(int pressedKey) {
        switch (pressedKey) {
            case 37: // left KeyEvent.VK_LEFT
                if (cursor > 0) {
                    cursorBlink = 0;
                    cursor--;
                    if (cursor < begin) begin--;
                }
                break;
            case 39: // right KeyEvent.VK_RIGHT
                if (cursor < messageBuffer.length()) {
                    cursorBlink = 0;
                    cursor++;
                    if (cursor > end) end++;
                }
                break;
            default:
                break;
        }
    }

    public void key(int typedKey) {
        if (!(typedKey >= 32 && typedKey <= 126) || messageBuffer.length() >= CHAR_MAX) return;
        char inputKey = (char) typedKey;
        messageBuffer.insert(cursor, inputKey);
        cursor++;
        end++;
        cursorBlink = 0;
    }

    public void back() {
        if (cursor > 0) {
            cursorBlink = 0;
            messageBuffer.deleteCharAt(cursor - 1);
            cursor--;
            if (end < messageBuffer.length()) {
                end++;
            } else {
                end--;
                if (begin > 0) begin--;
            }
        }
    }

    public String enter() {
        lastMessage = messageBuffer.toString();
        messageBuffer.delete(0, messageBuffer.length());
        cursorBlink = 0;
        cursor = 0;
        begin = 0;
        end = 0;
        return lastMessage;
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        return true;
    }


    private void resize(Graphics2D g) {
        g.setFont(Client.getFont());
        g.setColor(Color.white);
        FontMetrics fontMetrics = g.getFontMetrics();
        if (fontMetrics.stringWidth(messageBuffer.toString().substring(begin, end)) > WIDTH) {
            if (cursor >= end) {
                begin++;
                resize(g);
            } else {
                end--;
                resize(g);
            }
        } else {
            return;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.drawImage(Client.getImages().getImage(Constants.IMG_CHAT_LINE), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        g.setFont(Client.getFont());
        g.setColor(Color.white);
        resize(g);
        g.drawString(messageBuffer.toString().substring(begin, end), getScreenX() + 6, getScreenY() + 16);
        if (cursorBlink < 10) {
            String section = messageBuffer.toString();
            FontMetrics fontMetrics = g.getFontMetrics();
            g.drawString("|", getScreenX() + 5 + fontMetrics.stringWidth(section.substring(begin, cursor)), getScreenY() + 15);
        }
   
   /* //Debug stuff
   String section = messageBuffer.toString();
   FontMetrics fontMetrics = g.getFontMetrics();
   g.drawString("Begin: " + begin, getScreenX()+5,getScreenY()-10);
   g.drawString("End: " + end, getScreenX()+5+fontMetrics.stringWidth(section.substring(begin,end)),getScreenY()-20);
   g.drawString("Cursor: " + cursor, getScreenX()+5+fontMetrics.stringWidth(section.substring(begin,cursor)),getScreenY()-30);
   g.drawString("Length: " + messageBuffer.length(), getScreenX()+5,getScreenY()-40);
   g.drawString("Last Message: " + lastMessage, getScreenX()+5,getScreenY()-50);*/

        cursorBlink++;
        if (cursorBlink > 20) cursorBlink = 0;
    }
}
