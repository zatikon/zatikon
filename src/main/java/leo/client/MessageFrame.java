///////////////////////////////////////////////////////////////////////
// Name: MessageFrame
// Desc: The message frame
// Date: 7/23/2008 - Gabe Jones
//     10/15/2010 - Tony Schwartz
//   Updates: Added MirroredRandom mode
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;

public class MessageFrame extends JFrame implements ActionListener, MouseListener, WindowListener {


    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int WIDTH = 350;
    private static final int HEIGHT = 200;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int gameType = leo.shared.Action.GAME_NONE;
    private static int position = 0;
    private final ChatPlayer player;
    private final JTextField chatInput;
    private final JTextPane chatOutput;
    private final StyledDocument doc;
    private JMenu menu;
    private final JMenuItem constructed = new JMenuItem("Constructed");
    private final JMenuItem random = new JMenuItem("Random");
    private final JMenuItem mirroredRandom = new JMenuItem("Mirrored Random");
    private final JMenuItem cooperative = new JMenuItem("Cooperative");
    private JMenuItem rematchRandom = new JMenuItem("Replay Last Random");
    private final JMenuItem block = new JMenuItem("Mute Player");
    private final JMenuItem unblock = new JMenuItem("Un-Mute Player");
    private JPanel inviteStatus;
    private JButton acceptButton;
    private JButton cancelButton;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public MessageFrame(ChatPlayer newPlayer) {
        // Set the player
        player = newPlayer;

        setTitle("Chatting with: " + player.getName());

        if (!Client.getName().matches(player.getName())) {
            inviteStatus = new JPanel();

            JMenuBar bar = new JMenuBar();

            menu = new JMenu("Invite [Unranked]");

            constructed.addActionListener(this);
            menu.add(constructed);
            random.addActionListener(this);
            menu.add(random);
            mirroredRandom.addActionListener(this);
            menu.add(mirroredRandom);
            cooperative.addActionListener(this);
            menu.add(cooperative);

            bar.add(menu);

            JMenu options = new JMenu("Options");
            block.addActionListener(this);
            options.add(block);
            unblock.addActionListener(this);
            options.add(unblock);

            bar.add(options);

            setJMenuBar(bar);

            acceptButton = new JButton("Accept");
            cancelButton = new JButton("Cancel");
            acceptButton.addActionListener(this);
            cancelButton.addActionListener(this);

        }

        // Input field
        chatInput = new JTextField();
        chatInput.setEditable(true);
        chatInput.setDocument(new TextFieldLimiter(200));

        // The output pane
        chatOutput = new JTextPane();
        chatOutput.setEditable(false);
        doc = chatOutput.getStyledDocument();

        // Styles
        Style def = StyleContext.getDefaultStyleContext().
                getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);

        // events
        chatInput.addActionListener(this);
        chatOutput.addMouseListener(this);
        addMouseListener(this);
        addWindowListener(this);

        // some scrolly stuff
        JScrollPane scroll = new JScrollPane(chatOutput, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Put it all together
        setLayout(new BorderLayout());
        add(chatInput, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
        setSize(WIDTH, HEIGHT);

        //Dimension dim = getToolkit().getScreenSize();
        //setLocation(
        // (int) dim.getWidth()/2  - ((getWidth()+getInsets().left)/2),
        // (int) dim.getHeight()/2 - ((getHeight()+getInsets().right)/2));

        setLocation(position + getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).left, position + getToolkit().getScreenInsets(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()).top);
        position += 25;

        validate();
    }


    /////////////////////////////////////////////////////////////////
    // Show text in the box
    /////////////////////////////////////////////////////////////////
    public void showText(String bold, String text) {
        try {

            doc.insertString(doc.getLength(), bold,
                    doc.getStyle("bold"));

            doc.insertString(doc.getLength(), text + "\n",
                    doc.getStyle("regular"));

            chatOutput.setCaretPosition(doc.getLength());
        } catch (Exception e) {
            System.out.println("MessageFrame.showText(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Focus on the right component
    /////////////////////////////////////////////////////////////////
    public void disable(String message) {
        try {
            doc.insertString(doc.getLength(), message,
                    doc.getStyle("bold"));

            chatOutput.setCaretPosition(doc.getLength());
            chatInput.setEnabled(false);
            setJMenuBar(null);
            remove(inviteStatus);
            validate();
        } catch (Exception e) {
            System.out.println("MessageFrame.disable(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Focus on the right component
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent ae) {

        if (ae.getSource() == chatInput) {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            String text = chatInput.getText();
            showText(Client.getName() + ": ", text);
            chatInput.setText("");
            Client.getChat().whisper(player, text);
        } else if (ae.getSource() == block) {
            Client.block(player.getName());
            showText("You have muted " + player.getName(), "");
        } else if (ae.getSource() == unblock) {
            Client.unblock(player.getName());
            showText("You have un-muted " + player.getName(), "");
        } else if (ae.getSource() == constructed) {

            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }

            Client.getChat().invite(player, leo.shared.Action.GAME_CONSTRUCTED);
            showText("You've invited " + player.getName() + " to play constructed", "");
            invited();
        } else if (ae.getSource() == random) {
            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }

            Client.getChat().invite(player, leo.shared.Action.GAME_RANDOM);
            showText("You've invited " + player.getName() + " to play random", "");
            invited();
        } else if (ae.getSource() == mirroredRandom) {
            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }

            Client.getChat().invite(player, leo.shared.Action.GAME_MIRRORED_RANDOM);
            System.out.println("Case MirroredRandom");
            showText("You've invited " + player.getName() + " to play mirrored random", "");
            invited();
        } else if (ae.getSource() == cooperative) {

            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }

            Client.getChat().invite(player, leo.shared.Action.GAME_COOPERATIVE);
            showText("You've invited " + player.getName() + " to play cooperative", "");
            invited();
        } else if (ae.getSource() == rematchRandom) {
            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }

            Client.getChat().invite(player, leo.shared.Action.GAME_REMATCH_RANDOM);
            showText("You've invited " + player.getName() + " to play a rematch of your last random game", "");
            invited();
        } else if (ae.getSource() == cancelButton) {
            Client.getChat().invite(player, leo.shared.Action.GAME_NONE);
            remove(inviteStatus);
            showText("Invitation to play with " + player.getName() + " cancelled", "");
            validate();
        } else if (ae.getSource() == acceptButton) {
            if (!Client.load()) {
                showText("Try again when game is finished loading...", "");
                return;
            }
            remove(inviteStatus);
            validate();
            Client.getChat().accept(player, gameType);

        }
    }


    /////////////////////////////////////////////////////////////////
    // Invite another
    /////////////////////////////////////////////////////////////////
    private void invited() {
        remove(inviteStatus);
        inviteStatus = new JPanel();
        inviteStatus.add(new JLabel("Invite sent, awaiting response..."));
        inviteStatus.add(cancelButton);
        add(inviteStatus, BorderLayout.NORTH);
        validate();
    }


    /////////////////////////////////////////////////////////////////
    // Get accept
    /////////////////////////////////////////////////////////////////
    public void accept() {
        remove(inviteStatus);
        validate();
    }


    /////////////////////////////////////////////////////////////////
    // Get invite
    /////////////////////////////////////////////////////////////////
    public void invite(int newGameType) {
        remove(inviteStatus);
        gameType = newGameType;
        if (gameType == leo.shared.Action.GAME_NONE) {
            //showText(player.getName() + " has cancelled the invite","");
            validate();
            return;
        }

        String message = player.getName() + " has invited you to play ";
        switch (gameType) {
            case leo.shared.Action.GAME_RANDOM:
                message += "random";
                break;
            case leo.shared.Action.GAME_MIRRORED_RANDOM:
                message += "mirrored random";
                break;
            case leo.shared.Action.GAME_CONSTRUCTED:
                message += "constructed";
                break;
            case leo.shared.Action.GAME_COOPERATIVE:
                message += "cooperative";
                break;
            case leo.shared.Action.GAME_REMATCH_RANDOM:
                message += " a rematch of your last random game";
                break;

        }
        showText(message, "");
        inviteStatus = new JPanel();
        inviteStatus.add(new JLabel("Accept invitation?"));
        inviteStatus.add(acceptButton);
        inviteStatus.add(cancelButton);
        add(inviteStatus, BorderLayout.NORTH);
        validate();
        bump();
    }


    /////////////////////////////////////////////////////////////////
    // Focus on the right component
    /////////////////////////////////////////////////////////////////
    public void focus() {
        chatInput.requestFocus();
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
    // Allow players to request a rematch of a random game they just played
    /////////////////////////////////////////////////////////////////
    public void addRematchFunction() {
        rematchRandom = new JMenuItem("Replay Last Random");
        rematchRandom.addActionListener(this);
        menu.add(rematchRandom);
    }

    public void removeRematchFunction() {
        menu.remove(rematchRandom);
        rematchRandom = null;
    }


    /////////////////////////////////////////////////////////////////
    // Event stubs
    /////////////////////////////////////////////////////////////////
    public void mouseClicked(MouseEvent e) {
        chatInput.requestFocus();
    }


    /////////////////////////////////////////////////////////////////
    // Event stubs
    /////////////////////////////////////////////////////////////////
    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void windowDeactivated(WindowEvent e) {
    }

    public void windowActivated(WindowEvent e) {
    }

    public void windowDeiconified(WindowEvent e) {
    }

    public void windowIconified(WindowEvent e) {
        System.out.println("ico");
    }

    public void windowClosed(WindowEvent e) {
        System.out.println("closed");
    }

    public void windowClosing(WindowEvent e) {
        if (inviteStatus != null) {
            Client.getChat().invite(player, leo.shared.Action.GAME_NONE);
            remove(inviteStatus);
        }
    }

    public void windowOpened(WindowEvent e) {
    }
}
