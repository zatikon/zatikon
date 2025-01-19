///////////////////////////////////////////////////////////////////////
// Name: ClientLoginDialog
// Desc: The login dialog
// Date: 2/7/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import leo.shared.LoginAttempt;
import leo.shared.LoginResponse;
import org.tinylog.Logger;

import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Optional;


public class ClientLoginDialog extends Dialog
        implements ActionListener, WindowListener, ItemListener, Runnable {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 138;
    private static final int WIDTH = 357;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private Thread runner;
    private ClientNetManager clientNetManager;
    private LoginResponse loginResponse;
    private String username;
    private String password;
    private final Panel mainPanel;
    //private ImagePanel imagePanel;
    private final Panel loginPanel;
    private final TextField serverField;

    private final TextField userField;
    private final TextField passwordField;
    private final Panel serverPanel;

    private final Checkbox standaloneCheckbox;
    private final Panel userPanel;
    private final Panel passwordPanel;
    private final Button loginButton;
    private final Checkbox savePasswordCheckbox;
    private final Panel buttonPanel;
    private final Button createButton;
    private short loginType = LoginAttempt.EXISTING_ACCOUNT;
    //private Checkbox fullScreenBox;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientLoginDialog() {
        super(new Frame(), "" + Client.TITLE + Client.VERSION + Client.CREDITS);

        // Create the components
        //fullScreenBox = new Checkbox("Fullscreen mode", false);
        //fullScreenBox.addItemListener(this);
        mainPanel = new Panel(new BorderLayout());
        loginPanel = new Panel(new GridLayout(6, 1));

        //imagePanel = new ImagePanel("helmet.jpg");
        serverField = new TextField();
        serverField.addActionListener(this);

        serverField.setText(Client.settings().getServer());

        userField = new TextField();
        userField.addActionListener(this);

        userField.setText(Client.settings().getUsername());

        passwordField = new TextField();
        passwordField.addActionListener(this);
        passwordField.setEchoChar('*');
        serverPanel = new Panel(new GridLayout(1, 2));
        userPanel = new Panel(new GridLayout(1, 2));
        passwordPanel = new Panel(new GridLayout(1, 2));

        serverPanel.add(new Label("Server address"));
        serverPanel.add(serverField);

        standaloneCheckbox = new Checkbox("Standalone mode?");
        standaloneCheckbox.addItemListener(this);

        userPanel.add(new Label("Name:"));
        userPanel.add(userField);
        passwordPanel.add(new Label("Password:"));
        passwordPanel.add(passwordField);
        loginButton = new Button("Login");
        loginButton.addActionListener(this);
        savePasswordCheckbox = new Checkbox("Remember password?");
        savePasswordCheckbox.addItemListener(this);
        createButton = new Button("Create new account");
        createButton.addActionListener(this);
        buttonPanel = new Panel(new GridLayout(1, 2));
        buttonPanel.add(loginButton);
        buttonPanel.add(createButton);

        String password = Client.settings().getUserPassword();
        if (!Objects.equals(password, "")) {
            passwordField.setText(password);
            savePasswordCheckbox.setState(true);
            Client.setRememberPassword(true);
        }

        // Add the components to the form
        loginPanel.add(serverPanel);
        loginPanel.add(standaloneCheckbox);
        loginPanel.add(userPanel);
        loginPanel.add(passwordPanel);
        loginPanel.add(savePasswordCheckbox);
        loginPanel.add(buttonPanel);
        //loginPanel.add(fullScreenBox);
        //mainPanel.add(imagePanel,  "Center");
        mainPanel.add(loginPanel, "Center");
        add(mainPanel);

        // Set the attributes
        setSize(WIDTH + getInsets().left, HEIGHT + getInsets().top);
        setResizable(false);
        addWindowListener(this);

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();
        setLocation(
                (int) dim.getWidth() / 2 - ((WIDTH + getInsets().left) / 2),
                (int) dim.getHeight() / 2 - ((HEIGHT + getInsets().right) / 2));

        // Show the frame
        validate();
        //setVisible(true);
        userField.requestFocus();
    }


    /////////////////////////////////////////////////////////////////
    // Action performed
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        Logger.trace("ACTION!");

        if (e.getSource() == userField) {
            passwordField.requestFocus();
        }

        if (e.getSource() == passwordField) {
            login(false);
        }

        if (e.getSource() == loginButton) {
            login(false);
        }

        if (e.getSource() == createButton) {
            ClientNewAccountDialog ccnad = new ClientNewAccountDialog(this);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Try to log in
    /////////////////////////////////////////////////////////////////
    private void login(boolean skip) {
        var standalone = standaloneCheckbox.getState();

        if (standalone) {
            Client.standalone = true;
            Client.serverName = "localhost";
            username = Constants.LOCAL_USER_NAME;
            password = Constants.LOCAL_USER_PASSWORD;
        } else {
            Client.serverName = serverField.getText();
            username = userField.getText();
            password = passwordField.getText();
        }

        if (skip) {
            // Check the username
            if (!validUsername(username)) return;

            // Check the password
            if (!validPassword(password)) return;
        }

        if (standalone) {
            Client.startLocalServer();
            loginType = LoginAttempt.EXISTING_OR_NEW;
        }

        Logger.info("Connecting to server");
        ClientStatusDialog status = new ClientStatusDialog("Connecting to server");

        // Attempt a connection to the server
        try {
            Logger.info("Attempting login");
            status.setText("Attempting to contact server...");
            LoginAttempt loginAttempt =
                    new LoginAttempt(username.toLowerCase(), password, "fb", loginType, Client.PROTOCOL_VERSION, false);
            clientNetManager = new ClientNetManager(!standalone);
            loginResponse = clientNetManager.connect(loginAttempt);
        } catch (Exception e) {
            status.dispose();
            alert("Unable to reach the server: " + e);
            return;
        } finally {
            status.dispose();
        }

        if (loginResponse.getResponse() == LoginResponse.SSL_ERROR) {
            Logger.debug("SSL ERROR ALERT");
            alert("Unable to reach the server, " + loginResponse.getMsg());
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_OLD_VERSION) {
            alert("Please download the latest version from https://www.chroniclogic.com/zat_download.htm");
            Client.shutdown();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_WRONG_PASSWORD) {
            alert("Wrong password entered.");
            passwordField.setText("");
            passwordField.requestFocus();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_NOT_EXIST) {
            alert("Name does not exist.");
            userField.setText("");
            passwordField.setText("");
            userField.requestFocus();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_INCORRECT_DATA) {
            alert("Data was received incorrectly. Please try again.");
            passwordField.requestFocus();
            return;
        }

        if (loginResponse.getResponse() == LoginResponse.FAIL_ALREADY_LOGGED_IN) {
            alert("That account is already logged in.");
            userField.setText("");
            passwordField.setText("");
            userField.requestFocus();
            return;
        }

        Logger.debug("Done with failure checks");

        if (loginResponse.getResponse() == LoginResponse.LOGIN_SUCCESSFUL) {
            // Enclosed in a try in case the server goes down while
            // the player is fetching their characters
            try {
                this.dispose();
                runner = new Thread(this, "ClientLoginDialogThread");
                runner.start();
            } catch (Exception e) {
            }
        }

    }


    /////////////////////////////////////////////////////////////////
    // Run the sucker
    /////////////////////////////////////////////////////////////////
    public void run() {
        Logger.debug("Starting client setup");
        Client.setName(username);
        Client.setPassword(password);
        Client.setNetManager(clientNetManager);
        try {
            Client.startChat(loginResponse.getChatID());
        } catch (Exception e) {
            Logger.error("ClientLoginDialog: " + e);
            Logger.error("loginResponse.getChatID(): " + loginResponse.getChatID());
        }
        GameFrame gameFrame = new GameFrame();
        gameFrame.startGameMode();
    }


    /////////////////////////////////////////////////////////////////
    // valid username?
    /////////////////////////////////////////////////////////////////
    private boolean validUsername(String username) {
        // Too long
        if (username.length() > 20) {
            alert("Name can only be up to 20 characters long.");
            userField.setText("");
            passwordField.setText("");
            userField.requestFocus();
            return false;
        }

        // Too short
        if (username.length() < 3) {
            alert("Name must be at least 3 characters long.");
            userField.setText("");
            passwordField.setText("");
            userField.requestFocus();
            return false;
        }

        // Contains non-letters
        for (int i = 0; i < username.length(); i++) {
            if (!Character.isLetterOrDigit(username.charAt(i))) {
                alert("Name can only contain letters or numbers.");
                userField.setText("");
                passwordField.setText("");
                userField.requestFocus();
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
        // Too long
        if (password.length() > 100) {
            alert("Password can only be up to 100 characters long.");
            passwordField.setText("");
            passwordField.requestFocus();
            return false;
        }

        // Too short
        if (password.length() < 3) {
            alert("Password must be at least 3 characters long.");
            passwordField.setText("");
            passwordField.requestFocus();
            return false;
        }

        // Just right
        return true;
    }


    /////////////////////////////////////////////////////////////////
    // Alert the user
    /////////////////////////////////////////////////////////////////
    private void alert(String message) {
        ClientMessageDialog cmd = new ClientMessageDialog(message);
    }


    /////////////////////////////////////////////////////////////////
    // Close the app
    /////////////////////////////////////////////////////////////////
    public void windowClosing(WindowEvent e) {
        Client.shutdown();
    }


    /////////////////////////////////////////////////////////////////
    // CheckBox
    /////////////////////////////////////////////////////////////////
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() == standaloneCheckbox) {
            boolean remote = !standaloneCheckbox.getState();
            serverField.setEnabled(remote);
            userField.setEnabled(remote);
            passwordField.setEnabled(remote);
            createButton.setEnabled(remote);
            savePasswordCheckbox.setEnabled(remote);
        }

        if (e.getSource() == savePasswordCheckbox) {
            Client.setRememberPassword(savePasswordCheckbox.getState());
        }



        //if (e.getSource() == fullScreenBox)
        //{ //fullScreenBox.setState(!fullScreenBox.getState());
        //Client.setFullScreen(fullScreenBox.getState());
        //}
    }


    /////////////////////////////////////////////////////////////////
    // Set the login type
    /////////////////////////////////////////////////////////////////
    public void autoLogin(String server, String un, String pw) {
        loginType = LoginAttempt.EXISTING_OR_NEW;
        serverField.setText(server);
        userField.setText(un);
        passwordField.setText(pw);
        login(true);
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


}
