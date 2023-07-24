///////////////////////////////////////////////////////////////////////
// Name: ChatList
// Desc: A panel of deploy buttons
// Date: 7/23/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;


public class ChatList extends JFrame implements WindowListener {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 550;
    private static final int WIDTH = 200;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final ChatListPanel panel;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatList(Vector users) {
        setTitle("Player List");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        addWindowListener(this);
        setLayout(new BorderLayout());

        // Build it
        panel = new ChatListPanel(this, users);
        add(panel, BorderLayout.CENTER);

        // Show it
        Dimension dim = getToolkit().getScreenSize();
        validate();
        pack();
        setLocation(
                (int) dim.getWidth() - (WIDTH + getInsets().left + getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).right),
                (int) dim.getHeight() - (HEIGHT + getInsets().top + getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).bottom));

        setVisible(false);
        //remove(panel);
    }

 /*public void repaint(Graphics g) {
  if (!haveToRefresh)
   return;
  
  remove(panel);
  setTitle("Player List");
  setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
  setSize(WIDTH,HEIGHT);
  setResizable(false);
  addWindowListener(this);
  setLayout(new BorderLayout());
 
  // Build it
  panel = new ChatListPanel(panel);
  add(panel, BorderLayout.CENTER);
  
  // Show it
  Dimension dim = getToolkit().getScreenSize();
  validate();
  pack();
  setLocation(
   (int) dim.getWidth()  - (WIDTH+getInsets().left+getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).right),
   (int) dim.getHeight() - (HEIGHT+getInsets().top+getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).bottom));
  
  setVisible(true);
 }*/

    /////////////////////////////////////////////////////////////////
    // Update/repaint/whatever
    /////////////////////////////////////////////////////////////////
    //public void update()
    //{ panel.update();
    //}


    /////////////////////////////////////////////////////////////////
    // Update/repaint/whatever
    /////////////////////////////////////////////////////////////////
    public ChatListPanel panel() {
        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // Iconify the sucker
    /////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {
        setState(Frame.ICONIFIED);
    }

    public void refresh() {
        if (!isVisible()) {
            setVisible(true);
        }

        if (getState() == Frame.ICONIFIED) {
            setState(Frame.NORMAL);
        }
        panel.update();
    }

    /////////////////////////////////////////////////////////////////
    // Window event stubs
    /////////////////////////////////////////////////////////////////
    public void windowDeactivated(WindowEvent e) {
        //refreshStuff();
    }

    public void windowActivated(WindowEvent e) {
        //System.out.println("|| _-_-_-_-_ Activated _-_-_-_-_ ||");
        refresh();
    }

    public void windowDeiconified(WindowEvent e) {
        //refreshStuff();
    }

    public void windowIconified(WindowEvent e) {
        //refreshStuff();
    }

    public void windowClosed(WindowEvent e) {
        //refreshStuff();
    }

    public void windowOpened(WindowEvent e) {
        //refreshStuff();
    }

}
