///////////////////////////////////////////////////////////////////////
// Name: ScoresBox
// Desc: High scores
// Date: 10/9/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ScoresBox extends Dialog
        implements ActionListener, WindowListener, MouseListener, KeyListener {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private final Button okButton;
    private final Panel buttonPanel;
    //private JLabel label;
    private final JEditorPane label;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ScoresBox(String message) {
        super(new Frame(), "");

        // Create the components
        //label = new JLabel(message);
        label = new JEditorPane("text/html", message);
        label.addMouseListener(this);
        okButton = new Button("Ok");
        okButton.addActionListener(this);
        okButton.addKeyListener(this);
        buttonPanel = new Panel(new GridLayout(1, 3));
        buttonPanel.add(new Label(""));
        buttonPanel.add(okButton);
        buttonPanel.add(new Label(""));

        // Add the components
        setLayout(new BorderLayout());
        add(label, "Center");
        add(buttonPanel, "South");


        // Set the attributes
        setModal(true);
        setResizable(false);
        addWindowListener(this);

        // Show the frame
        validate();
        pack();

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();
        setLocation(
                (int) dim.getWidth() / 2 - (getWidth() / 2),
                (int) dim.getHeight() / 2 - (getHeight() / 2));

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
