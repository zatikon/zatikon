///////////////////////////////////////////////////////////////////////
//	Name:	ClientNewAccountDialog
//	Desc:	The account creation dialog box
//	Date:	2/7/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.LoginAttempt;
import leo.shared.LoginResponse;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ClientNewAccountDialog extends JDialog
        implements ActionListener, KeyListener, WindowListener, Runnable {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 175;
    private static final int WIDTH = 220;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private Thread runner;
    private ClientNetManager clientNetManager;
    private LoginResponse loginResponse;
    private final JTextField serverField;
    private final JTextField nameField;
    private final JPasswordField passwordField1;
    private final JPasswordField passwordField2;
    private final JTextField emailField;
    private final Button createButton;
    private final Button cancelButton;
    private final ClientLoginDialog cdl;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientNewAccountDialog(ClientLoginDialog newCdl) {
        super(new JFrame(), "Create new account");

        JPanel panel = new JPanel();
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        cdl = newCdl;

        // Create the components
        serverField = new JTextField();
        serverField.addActionListener(this);
        serverField.setText(Client.settings().getServer());
        nameField = new JTextField();
        nameField.addActionListener(this);
        nameField.addKeyListener(this);
        nameField.setDocument(new TextFieldLimiter(12));
        passwordField1 = new JPasswordField();
        passwordField1.addActionListener(this);
        passwordField1.addKeyListener(this);
        passwordField2 = new JPasswordField();
        passwordField2.addActionListener(this);
        passwordField2.addKeyListener(this);
        emailField = new JTextField();
        emailField.addActionListener(this);
        emailField.addKeyListener(this);
        createButton = new Button("Create");
        createButton.addActionListener(this);
        cancelButton = new Button("Cancel");
        cancelButton.addActionListener(this);

        // Add the components
        panel.setLayout(new GridLayout(6, 2));
        panel.add(new Label("Server:"));
        panel.add(serverField);
        panel.add(new Label("Name:"));
        panel.add(nameField);
        panel.add(new Label("Password:"));
        panel.add(passwordField1);
        panel.add(new Label("(again)"));
        panel.add(passwordField2);
        panel.add(new Label("Email address:"));
        panel.add(emailField);
        panel.add(createButton);
        panel.add(cancelButton);

        add(panel);

        //buttonPanel.add(createButton);
        //buttonPanel.add(cancelButton);

        //setLayout(new BorderLayout());
        //add(panel, BorderLayout.CENTER);
        //add(buttonPanel, BorderLayout.SOUTH);

        // Set the attributes
        setSize(WIDTH + getInsets().left, HEIGHT + getInsets().top);
        //setModal(true);
        setResizable(false);
        addWindowListener(this);
        validate();
        //pack();

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();
        //setLocation(
        //	(int) dim.getWidth()/2  - ((WIDTH+getInsets().left)/2),
        //	(int) dim.getHeight()/2 - ((HEIGHT+getInsets().top)/2));

        setLocation(
                (int) dim.getWidth() / 2 - ((getWidth() + getInsets().left) / 2),
                (int) dim.getHeight() / 2 - ((getHeight() + getInsets().top) / 2));

        // Show the frame
        enableControls();
        panel.setBorder(BorderFactory.createTitledBorder("Create a new account"));
        setVisible(true);
        nameField.requestFocus();
        cdl.setVisible(false);
    }


    /////////////////////////////////////////////////////////////////
    // enable controls
    /////////////////////////////////////////////////////////////////
    private boolean enableControls() {
        boolean ok = nameField.getText().length() <= 12;

        // Check the username
        ok = ok && validUsername(nameField.getText());

        // Check the password
        ok = ok && validPassword(new String(passwordField1.getPassword()));

        // See if passwords match
        ok = ok && new String(passwordField1.getPassword()).equals(new String(passwordField2.getPassword()));

        ok = ok && validEmail(emailField.getText());

        createButton.setEnabled(ok);
        return ok;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public boolean validEmail(String address) {
        String lowered = address.toLowerCase();
        return lowered.matches(".+@.+\\.[a-z]{2,}");
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        enableControls();
        if (e.getSource() == serverField) {
            nameField.requestFocus();
        }

        if (e.getSource() == nameField) {
            passwordField1.requestFocus();
        }

        if (e.getSource() == passwordField1) {
            passwordField2.requestFocus();
        }

        if (e.getSource() == passwordField2) {
            emailField.requestFocus();
        }

        if (e.getSource() == emailField) {
            login();
        }

        if (e.getSource() == createButton) {
            login();
        }

        if (e.getSource() == cancelButton) {
            cdl.setVisible(true);
            this.dispose();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Try to log in
    /////////////////////////////////////////////////////////////////
    private void login() {
        Client.serverName = serverField.getText();
        String username = nameField.getText();
        String password = new String(passwordField1.getPassword());
        String password2 = new String(passwordField2.getPassword());
//        Newsletter news = new Newsletter(emailField.getText());

        ClientStatusDialog status = new ClientStatusDialog("Connecting to server");

        // Check the username
        if (!validUsername(username)) return;

        // Check the password
        if (!validPassword(password)) return;

        // See if passwords match
        if (!password.equals(password2)) {
            alert("Passwords do not match.");
            passwordField1.setText("");
            passwordField2.setText("");
            passwordField1.requestFocus();
            return;
        }

        // Attempt a connection to the server
        try {
            Client.setName(username);
            Client.setPassword(password);
            status.setText("Attempting to contact server...");
            LoginAttempt loginAttempt =
//                    new LoginAttempt(username.toLowerCase(), password, emailField.getText(), LoginAttempt.NEW_ACCOUNT, Client.VERSION, Newsletter.answer());
                    new LoginAttempt(username.toLowerCase(), password, emailField.getText(), LoginAttempt.NEW_ACCOUNT, Client.PROTOCOL_VERSION, false);
            clientNetManager = new ClientNetManager(Client.shouldUseTls());
            loginResponse = clientNetManager.connect(loginAttempt);
        } catch (Exception e) {
            status.dispose();
            alert("Unable to reach the server.");
            return;
        } finally {
            status.dispose();
        }


        if (loginResponse.getResponse() == LoginResponse.FAIL_OLD_VERSION) {
            alert("Please download the latest version from zatikon.com");
            Client.shutdown();
            return;
        }


        if (loginResponse.getResponse() == LoginResponse.FAIL_ACCOUNT_EXISTS) {
            alert("That name already exists.");
            nameField.setText("");
            passwordField1.setText("");
            passwordField2.setText("");
            nameField.requestFocus();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_NOT_EXIST) {
            alert("Name does not exist.");
            nameField.setText("");
            passwordField1.setText("");
            passwordField2.setText("");
            nameField.requestFocus();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_INCORRECT_DATA) {
            alert("Data was recieved incorrectly. Please try again.");
            passwordField2.requestFocus();
            return;
        }


        if (loginResponse.getResponse() == LoginResponse.CREATE_SUCCESSFUL) {
            this.dispose();
            runner = new Thread(this, "ClientNewAccountDialogThread");
            runner.start();
        }

    }


    /////////////////////////////////////////////////////////////////
    // Start your loading
    /////////////////////////////////////////////////////////////////
    public void run() {
        Client.setNetManager(clientNetManager);
        Client.startChat(loginResponse.getChatID());
        //clientNetManager.start();
        GameFrame gameFrame = new GameFrame();
        gameFrame.startGameMode();
    }


    /////////////////////////////////////////////////////////////////
    // Valid username?
    /////////////////////////////////////////////////////////////////
    private boolean validUsername(String username) {
        // Too long
        if (username.length() > 12) {    //alert("Name can only be up to 12 characters long.");
            //nameField.setText("");
            //passwordField1.setText("");
            //passwordField2.setText("");
            //nameField.requestFocus();
            return false;
        }

        // Too short
        if (username.length() < 3) {    //alert("Name must be at least 3 characters long.");
            //nameField.setText("");
            //passwordField1.setText("");
            //passwordField2.setText("");
            //nameField.requestFocus();
            return false;
        }

        // Contains non-letters
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetterOrDigit(username.charAt(i))) {
                alert("Name can only contain letters or numbers.");
                nameField.setText("");
                //passwordField1.setText("");
                //passwordField2.setText("");
                nameField.requestFocus();
                return false;
            }
        }

        // Just right
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // valid password?
    /////////////////////////////////////////////////////////////////
    private boolean validPassword(String password) {
        // Too short
        //alert("Password must be at least 3 characters long.");
        //passwordField1.setText("");
        //passwordField2.setText("");
        //passwordField1.requestFocus();
        return password.length() >= 3;

        // Just right
    }


    /////////////////////////////////////////////////////////////////
    // Alert the user
    /////////////////////////////////////////////////////////////////
    private void alert(String message) {
        ClientMessageDialog cmd = new ClientMessageDialog(message);
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
        cdl.setVisible(true);
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
