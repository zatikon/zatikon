///////////////////////////////////////////////////////////////////////
//	Name:	ReferFriend
//	Desc:	Refer a friend
//	Date:	9/16/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ReferFriend extends JDialog
        implements ActionListener, KeyListener, WindowListener {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;
    private static final int HEIGHT = 100;
    private static final int WIDTH = 325;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private final JTextField emailField;
    private final Button send;
    private final Button cancel;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ReferFriend(Frame frame) {
        super(frame, "Refer a Friend");

        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());

        // Create the text row
        textPanel.add(new Label("Email:"), BorderLayout.WEST);
        emailField = new JTextField();
        emailField.addActionListener(this);
        emailField.addKeyListener(this);
        textPanel.add(emailField, BorderLayout.CENTER);

        // The button row
        send = new Button("Send");
        send.addActionListener(this);
        cancel = new Button("Cancel");
        cancel.addActionListener(this);
        buttonPanel.add(send);
        buttonPanel.add(cancel);

        // Add the components
        panel.setLayout(new GridLayout(2, 1));
        panel.add(textPanel);
        panel.add(buttonPanel);

        add(panel);

        // Set the attributes
        setSize(WIDTH + getInsets().left, HEIGHT + getInsets().top);
        setModal(true);
        setResizable(false);
        addWindowListener(this);
        validate();
        //pack();

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();

        setLocation(
                (int) dim.getWidth() / 2 - ((getWidth() + getInsets().left) / 2),
                (int) dim.getHeight() / 2 - ((getHeight() + getInsets().top) / 2));

        // Show the frame
        enableControls();
        panel.setBorder(BorderFactory.createTitledBorder("Earn 5000 gold if your friend registers"));
        setVisible(true);
        emailField.requestFocus();
    }


    /////////////////////////////////////////////////////////////////
    // enable controls
    /////////////////////////////////////////////////////////////////
    private boolean enableControls() {
        boolean ok = true;

        ok = ok && validEmail(emailField.getText());

        send.setEnabled(ok);
        return ok;
    }


    /////////////////////////////////////////////////////////////////
    // valid email
    /////////////////////////////////////////////////////////////////
    public boolean validEmail(String address) {
        String lowered = address.toLowerCase();
        return lowered.matches(".+@.+\\.[a-z]{2,4}");
    }


    /////////////////////////////////////////////////////////////////
    // do something
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        enableControls();

        if (e.getSource() == emailField) {
            send();
        }

        if (e.getSource() == send) {
            send();
        }

        if (e.getSource() == cancel) {
            this.dispose();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Try to log in
    /////////////////////////////////////////////////////////////////
    private void send() {
        Client.getNetManager().referFriend(emailField.getText());
        dispose();
    }


    /////////////////////////////////////////////////////////////////
    // Release a key
    /////////////////////////////////////////////////////////////////
    public void keyReleased(KeyEvent e) {
        enableControls();
    }


    /////////////////////////////////////////////////////////////////
    // Close the app
    /////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {
        this.dispose();
    }


    /////////////////////////////////////////////////////////////////
    // Stubs
    /////////////////////////////////////////////////////////////////
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
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

}
