///////////////////////////////////////////////////////////////////////
// Name: GameFrame
// Desc: The game frame for the client
// Date: 5/6/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.Optional;


public class GameFrame extends Frame implements MouseListener, MouseMotionListener, KeyListener, WindowListener, MouseWheelListener {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private GraphicsDevice device;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public GameFrame() {
        super(Client.TITLE,
                GraphicsEnvironment.getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration());

        try {

            // Temporary stuff to initialize
            Client.restart();

            device =
                    GraphicsEnvironment.getLocalGraphicsEnvironment()
                            .getDefaultScreenDevice();
            DisplayMode mode = getDesiredDisplayMode(device);

            if (Client.getFullScreen() && mode != null) {
                setUndecorated(true);
                //device.setFullScreenWindow(this);

            } else { // Set the position to the center of the screen
                setResizable(false);
                Dimension dim = getToolkit().getScreenSize();
                setLocation(
                        (int) dim.getWidth() / 2 - (Constants.WINDOW_WIDTH / 2),
                        (int) dim.getHeight() / 2 - (Constants.WINDOW_HEIGHT / 2));


            }
            if (device.isDisplayChangeSupported() && mode != null) { //device.setDisplayMode(mode);
            }
            this.setVisible(true);
            //this.setSize(Constants.WINDOW_WIDTH,Constants.WINDOW_HEIGHT);
            this.setSize(Constants.WINDOW_WIDTH + getInsets().left + getInsets().right, Constants.WINDOW_HEIGHT + getInsets().top + getInsets().bottom);
            Client.setInsets(
                    getInsets().left + Patches.INSET_LEFT,
                    getInsets().top + Patches.INSET_TOP
            );
            createBufferStrategy(2);
            addMouseListener(this);
            addMouseWheelListener(this);
            addMouseMotionListener(this);
            requestFocus();
            //Client.startRendering();
            Client.getNetManager().start();
            Client.setGameFrame(this);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Prepare for game
    /////////////////////////////////////////////////////////////////
    public void startGameMode() {
        addKeyListener(this);
        addWindowListener(this);
        Client.startRendering();
        setIgnoreRepaint(true);
        BufferCapabilities caps = new BufferCapabilities(new ImageCapabilities(false),
                new ImageCapabilities(false),
                null);

        // acceleration == trouble
        // at some point it would be good to rewrite the UI.
//        BufferCapabilities accelCaps = new BufferCapabilities(new ImageCapabilities(true),
//                new ImageCapabilities(true),
//                BufferCapabilities.FlipContents.UNDEFINED);

        try {
            this.createBufferStrategy(2, caps);
        } catch (AWTException e) {
            e.printStackTrace();
        }

        ClientRenderer clientRenderer = new ClientRenderer(getBufferStrategy(), this);
        try {
            Client.getGameData().screenRoster();
        } catch (Exception e) {
            Logger.error("GameFrame.startGameMode() " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the desired display mode
    /////////////////////////////////////////////////////////////////
    public DisplayMode getDesiredDisplayMode(GraphicsDevice graphicsDevice) {
        DisplayMode[] displayModes = graphicsDevice.getDisplayModes();
        DisplayMode desiredMode = null;
        for (int i = 0; i < displayModes.length; i++) {
            if (displayModes[i].getRefreshRate() > 0)
                if (
                        (
                                (displayModes[i].getWidth() == Constants.WINDOW_WIDTH) &&
                                        (displayModes[i].getHeight() == Constants.WINDOW_HEIGHT)
                        )
                                &&
                                (
                                        (desiredMode == null) ||
                                                (desiredMode.getBitDepth() < displayModes[i].getBitDepth()) ||
                                                (desiredMode.getRefreshRate() < displayModes[i].getRefreshRate() &&
                                                        displayModes[i].getRefreshRate() <= 80))
                ) {
                    desiredMode = displayModes[i];
                }
        }
        //if (desiredMode != null)
        //System.out.println(""
        // + desiredMode.getWidth()  + ", "
        // + desiredMode.getHeight() + ", "
        // + desiredMode.getBitDepth() + ", "
        // + desiredMode.getRefreshRate());
        return desiredMode;
    }


    /////////////////////////////////////////////////////////////////
    // When the mouse is clicked
    /////////////////////////////////////////////////////////////////
    public void mousePressed(MouseEvent e) {
        Client.getGameData().getPlayerPanel().mousePressed();
        try {

            while (Client.drawing()) {
                Client.setComputing(false);
                Thread.sleep(5);
            }

            Client.setComputing(true);


            Client.getGameData().click();
            if (e.getButton() == MouseEvent.BUTTON1)
                Client.getGameData().clickAt(e.getX(), e.getY());
            else
                Client.getGameData().setSelectedUnit(null);
            Client.getGameData().click();


            Client.setComputing(false);

        } catch (Exception ex) {
            Logger.error("GameFrame.mousePressed() " + ex);
            Client.setComputing(false);
        }
    }


    /////////////////////////////////////////////////////////////////
    // When the mouse is moved
    /////////////////////////////////////////////////////////////////
    public void mouseMoved(MouseEvent e) {
        Client.getGameData().setMouseLoc(e.getX(), e.getY());
    }


    /////////////////////////////////////////////////////////////////
    // When a key is pressed
    /////////////////////////////////////////////////////////////////
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        Client.getGameData().getPlayerPanel().keyPressed(code);
        switch (code) {
            case KeyEvent.VK_ESCAPE:
                //Client.shutdown();
                break;
        }
    }


    /////////////////////////////////////////////////////////////////
    // When a key is typed
    /////////////////////////////////////////////////////////////////
    public void keyTyped(KeyEvent e) {
        char code = e.getKeyChar();
        //if (Client.getGameData().playing()) return;
        switch (code) {
            case KeyEvent.VK_ENTER:
                if (Client.getMessageBuffer().toString().length() > 0) {
                    String message = Client.getGameData().getPlayerPanel().keyEnter();
                    if (message == "") return;
                    Client.getChat().broadcast(message);
                    Client.addText(Client.getName() + ": " + message);
                    Client.getMessageBuffer().delete(0, Client.getMessageBuffer().length());
                    Client.getGameData().getPlayerPanel().keyEnter();
                }
                break;

            case KeyEvent.VK_BACK_SPACE:
                Client.getGameData().getPlayerPanel().keyBack();
                break;

            default:
                Client.getGameData().getPlayerPanel().keyTyped(code);
                Client.getMessageBuffer().append(e.getKeyChar());
        }
    }


    /////////////////////////////////////////////////////////////////
    // When a key is released
    /////////////////////////////////////////////////////////////////
    public void keyReleased(KeyEvent e) {
    }


    /////////////////////////////////////////////////////////////////
    // Close the app
    /////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {
        Client.shutdown();
    }


    /////////////////////////////////////////////////////////////////
    // Stubs
    /////////////////////////////////////////////////////////////////
    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
        Client.getGameData().getPlayerPanel().mouseReleased();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
        Client.getGameData().getPlayerPanel().mouseDragged(e.getX(), e.getY());
    }

    public void windowOpened(WindowEvent e) {
    }

    public void windowClosed(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        if (e.getWheelRotation() < 0)
            Client.getGameData().getPlayerPanel().up();
        else
            Client.getGameData().getPlayerPanel().down();

    }


}
