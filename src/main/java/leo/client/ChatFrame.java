///////////////////////////////////////////////////////////////////////
// Name: ChatFrame
// Desc: The message frame
// Date: 8/13/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class ChatFrame extends JFrame implements WindowListener {


    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 550;
    private static final int WIDTH = 350;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ChatPanel chatPanel;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatFrame() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        setTitle("Zatikon Chat");

        setLayout(new BorderLayout());

        chatPanel = new ChatPanel();
        add(chatPanel, BorderLayout.CENTER);

        setSize(WIDTH, HEIGHT);

        validate();

        //Dimension dim = getToolkit().getScreenSize();
        //setLocation(
        // (int) dim.getWidth()/2  - ((getWidth()+getInsets().left)/2),
        // (int) dim.getHeight()/2 - ((getHeight()+getInsets().right)/2));

        setLocation(
                getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).left,
                HEIGHT - getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).bottom);
    }


    /////////////////////////////////////////////////////////////////
    // Bump it into view
    /////////////////////////////////////////////////////////////////
    public void bump() {
        if (!isVisible()) {
            setVisible(true);
        }

        if (getState() == Frame.ICONIFIED) {
            setState(Frame.NORMAL);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Iconify the sucker
    /////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {
        setState(Frame.ICONIFIED);
    }


    /////////////////////////////////////////////////////////////////
    // Get the panel
    /////////////////////////////////////////////////////////////////
    public ChatPanel getChatPanel() {
        return chatPanel;
    }


    /////////////////////////////////////////////////////////////////
    // Event stubs
    /////////////////////////////////////////////////////////////////
    public void windowDeactivated(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowOpened(WindowEvent e) {
    }

}
