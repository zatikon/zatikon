///////////////////////////////////////////////////////////////////////
//	Name:	TextBoard
//	Desc:	A board full of text
//	Date:	7/9/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import java.awt.*;
import java.util.Vector;


public class TextBoard extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MARGIN = 8;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Vector<String> lines = new Vector<String>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TextBoard(int newX, int newY, int newWidth, int newHeight) {
        super(newX, newY, newWidth, newHeight);
        add("Opponent's turn");
    }


    /////////////////////////////////////////////////////////////////
    // Add a message
    /////////////////////////////////////////////////////////////////
    public void add(String message) {
        if (message.length() < 25)
            lines.add(message);
        else {
            int i = 24;
            for (; i > 0; --i) {
                if (message.charAt(i) == ' ')
                    break;
            }
            String m1 = message.substring(0, i);
            String m2 = message.substring(i);
            if (m2.length() < 3)
                lines.add(message);
            else {
                lines.add(m1);
                add(m2);
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Clear the board
    /////////////////////////////////////////////////////////////////
    public void clear() {
        lines.clear();
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.setColor(Color.white);
        //g.fillRect(getScreenX(), getScreenY(), getWidth(), getHeight());
        //g.setColor(Color.black);
        //g.drawRect(getScreenX(), getScreenY(), getWidth()-1, getHeight()-1);
        //g.drawRect(getScreenX()+1, getScreenY()+1, getWidth()-3, getHeight()-3);

        //g.setColor(Color.gray);
        for (int i = 0; i < lines.size(); i++) {
            String message = lines.elementAt(i);
            g.drawString(
                    message,
                    getScreenX() + TextBoard.MARGIN,
                    getScreenY() + ((TextBoard.MARGIN + Client.FONT_HEIGHT) * (i + 1)));
        }
        super.draw(g, mainFrame);
    }
}
