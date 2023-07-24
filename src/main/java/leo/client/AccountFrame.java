///////////////////////////////////////////////////////////////////////
// Name: AccountFrame
// Desc: Account Frame
// Date: 8/14/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class AccountFrame extends JDialog implements KeyListener, ActionListener {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final JPasswordField oldPassword = new JPasswordField();
    private final JPasswordField newPassword = new JPasswordField();
    private final JPasswordField newPasswordAgain = new JPasswordField();
    private JButton saveButton;

    // registration
    private JTextField block1;
    private final JTextField block2 = new JTextField();
    private final JTextField block3 = new JTextField();
    private final JTextField block4 = new JTextField();
    private JButton submitButton;

    private static String lastKey = "";

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AccountFrame(Frame frame) {
        super(frame);

        setLayout(new GridLayout(1, 2));

        //if (Client.demo())
        add(createRegistrationPanel());

        add(createPasswordPanel());

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        Dimension dim = getToolkit().getScreenSize();
        validate();
        pack();
        setLocation(
                (int) dim.getWidth() / 2 -
                        (getWidth() / 2),
                (int) dim.getHeight() / 2 -
                        (getHeight() / 2));
        setModal(true);
        if (Client.demo()) paste(lastKey);
        setVisible(true);
    }


    /////////////////////////////////////////////////////////////////
    // Make the password panel
    /////////////////////////////////////////////////////////////////
    private JPanel createRegistrationPanel() {
        JPanel panel = new JPanel();

        submitButton = new JButton("Submit");
        submitButton.setEnabled(false);
        submitButton.addActionListener(this);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());

        block1 = new KeycodeField(this);

        block1.setFont(new Font("Monospaced", Font.PLAIN, 12));
        block1.setDocument(new TextFieldLimiter(4));
        block1.setColumns(4);
        block1.addKeyListener(this);
        buttonPanel.add(block1);
        buttonPanel.add(new JLabel("-"));
        block2.setFont(new Font("Monospaced", Font.PLAIN, 12));
        block2.setDocument(new TextFieldLimiter(4));
        block2.setColumns(4);
        block2.addKeyListener(this);
        buttonPanel.add(block2);
        buttonPanel.add(new JLabel("-"));
        block3.setFont(new Font("Monospaced", Font.PLAIN, 12));
        block3.setDocument(new TextFieldLimiter(4));
        block3.setColumns(4);
        block3.addKeyListener(this);
        buttonPanel.add(block3);
        buttonPanel.add(new JLabel("-"));
        block4.setFont(new Font("Monospaced", Font.PLAIN, 12));
        block4.setDocument(new TextFieldLimiter(4));
        block4.setColumns(4);
        block4.addKeyListener(this);
        block4.addActionListener(this);
        buttonPanel.add(block4);

        panel.setLayout(new BorderLayout());

//        if (!Client.isWeb() || Client.applet() != null) {
//
//            String text = "<html><head></head><body><a href=\"http://www.chroniclogic.com/index.htm?zat_order.htm\">Order a Registration Key</a></body></html>";
//            JLabel hyperlink = new JLabel(text, JLabel.CENTER);
//            hyperlink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
//
//            hyperlink.addMouseListener(new MouseAdapter() {
//                public void mouseClicked(MouseEvent me) {
//                    try {
//
//                        if (Client.applet() == null) {
//                            Browser.open("http://www.chroniclogic.com/index.htm?zat_order.htm");
//                        } else {
//                            Client.applet().getAppletContext().showDocument(new java.net.URL("http://www.chroniclogic.com/index.htm?zat_order.htm"));
//                        }
//
//
//                    } catch (Exception e) {
//                        System.out.println("AccountButton.createRegistrationPanel(): " + e);
//                    }
//                }
//            });
//
//            panel.add(hyperlink, BorderLayout.CENTER);
//        }
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(submitButton, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createTitledBorder("Enter Your Registration Key"));
        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // Make the password panel
    /////////////////////////////////////////////////////////////////
    private JPanel createPasswordPanel() {
        JPanel panel = new JPanel();

        saveButton = new JButton("Save");
        saveButton.setEnabled(false);
        saveButton.addActionListener(this);

        panel.setLayout(new BorderLayout());
        JPanel fields = new JPanel();
        fields.setLayout(new GridLayout(3, 2));

        fields.add(new JLabel("Old Password  "));
        fields.add(oldPassword);

        fields.add(new JLabel("New Password  "));
        fields.add(newPassword);
        newPassword.addKeyListener(this);

        fields.add(new JLabel("(Again)"));
        fields.add(newPasswordAgain);
        newPasswordAgain.addKeyListener(this);
        newPasswordAgain.addActionListener(this);

        panel.add(fields, BorderLayout.CENTER);
        panel.add(saveButton, BorderLayout.SOUTH);

        panel.setBorder(BorderFactory.createTitledBorder("Change Your Password"));
        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // enable submit
    /////////////////////////////////////////////////////////////////
    private void enableSubmit() {
        submitButton.setEnabled(
                block1.getText().length() == 4 &&
                        block2.getText().length() == 4 &&
                        block3.getText().length() == 4 &&
                        block4.getText().length() == 4
        );

    }


    /////////////////////////////////////////////////////////////////
    // press a key
    /////////////////////////////////////////////////////////////////
    public void keyReleased(KeyEvent e) {
        if (e.getSource() == block1) {
            block1.setText(block1.getText().toUpperCase());
            if (block1.getText().length() == 4)
                block2.requestFocus();
            enableSubmit();
        } else if (e.getSource() == block2) {
            block2.setText(block2.getText().toUpperCase());
            if (block2.getText().length() == 4)
                block3.requestFocus();
            enableSubmit();
        } else if (e.getSource() == block3) {
            block3.setText(block3.getText().toUpperCase());
            if (block3.getText().length() == 4)
                block4.requestFocus();
            enableSubmit();
        } else if (e.getSource() == block4) {
            block4.setText(block4.getText().toUpperCase());
            enableSubmit();
        }


        String pass1 = new String(newPassword.getPassword());
        String pass2 = new String(newPasswordAgain.getPassword());
        saveButton.setEnabled(pass1.contentEquals(pass2) && pass1.length() >= 3);
    }


    /////////////////////////////////////////////////////////////////
    // click a button
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == saveButton || e.getSource() == newPasswordAgain) {
            if (!saveButton.isEnabled()) return;
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            String oldPw = new String(oldPassword.getPassword());
            String newPw = new String(newPassword.getPassword());
            Client.getNetManager().sendNewPassword(oldPw, newPw);
            dispose();
        }

        if (e.getSource() == submitButton || e.getSource() == block4) {
            if (!submitButton.isEnabled()) return;
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            String code =
                    block1.getText() + "-" +
                            block2.getText() + "-" +
                            block3.getText() + "-" +
                            block4.getText();
            lastKey = code;
            Client.getNetManager().register(code);
            dispose();
        }

    }


    /////////////////////////////////////////////////////////////////
    // Handle a paste
    /////////////////////////////////////////////////////////////////
    public void paste(String text) {
        String key;
        key = text.toUpperCase();
        key = stripGarbage(key);
        if (key.length() >= 4) {
            block1.setText(key.substring(0, 4));
            block1.requestFocus();
        }
        if (key.length() >= 8) {
            block2.setText(key.substring(4, 8));
            block2.requestFocus();
        }
        if (key.length() >= 12) {
            block3.setText(key.substring(8, 12));
            block3.requestFocus();
        }
        if (key.length() >= 16) {
            block4.setText(key.substring(12, 16));
            block4.requestFocus();
        }

        enableSubmit();
    }


    /////////////////////////////////////////////////////////////////
    // Drop the garbage
    /////////////////////////////////////////////////////////////////
    private static String stripGarbage(String s) {
        String good =
                "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String result = "";
        for (int i = 0; i < s.length(); i++) {
            if (good.indexOf(s.charAt(i)) >= 0)
                result += s.charAt(i);
        }
        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Stubs
    /////////////////////////////////////////////////////////////////
    public void keyTyped(KeyEvent e) {
    }

    public void keyPressed(KeyEvent e) {
    }

}
