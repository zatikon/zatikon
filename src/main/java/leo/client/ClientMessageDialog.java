///////////////////////////////////////////////////////////////////////
// Name: ClientMessageDialog
// Desc: A dialog box containing a message
// Date: 2/8/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import java.awt.*;
import java.awt.event.*;


public class ClientMessageDialog extends Dialog
        implements ActionListener, WindowListener, MouseListener, KeyListener {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 120;
    private static final int WIDTH = 220;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private final Button okButton;
    private final Panel buttonPanel;
    private final TextArea textArea;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientMessageDialog(String message) {
        super(new Frame(), "");

        int width = WIDTH + getInsets().left;
        int height = HEIGHT + getInsets().top;

        // Create the components
        textArea = new TextArea(message, 4, 0, TextArea.SCROLLBARS_NONE);
        textArea.setEditable(false);
        textArea.enableInputMethods(false);
        textArea.setFocusable(false);
        textArea.addMouseListener(this);
        okButton = new Button("Ok");
        okButton.addActionListener(this);
        okButton.addKeyListener(this);
        buttonPanel = new Panel(new GridLayout(1, 3));
        buttonPanel.add(new Label(""));
        buttonPanel.add(okButton);
        buttonPanel.add(new Label(""));

        // Add the components
        setLayout(new BorderLayout());
        add(textArea, "Center");
        add(buttonPanel, "South");


        // Set the attributes
        setSize(width, height);
        setModal(true);
        setResizable(false);
        addWindowListener(this);

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();
        setLocation(
                (int) dim.getWidth() / 2 - (width / 2),
                (int) dim.getHeight() / 2 - (height / 2));

        // Show the frame
        validate();
        setVisible(true);
        okButton.requestFocus();
    }


    /////////////////////////////////////////////////////////////////
    // Close the dialog
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == okButton) { //Client.getImages().playSound(Constants.SOUND_BUTTON);
            this.dispose();
        }
    }

    public void windowClosing(WindowEvent e) {
        this.dispose();
    }

    public void mousePressed(MouseEvent e) {
        this.dispose();
    }

    public void keyPressed(KeyEvent e) {
        this.dispose();
    }


    /////////////////////////////////////////////////////////////////
    // Stubs
    /////////////////////////////////////////////////////////////////
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

    public void mouseClicked(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

}
