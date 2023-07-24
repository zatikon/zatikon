///////////////////////////////////////////////////////////////////////
// Name: ChatFrame
// Desc: The message panel for the chat frame
// Date: 8/13/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports
//import netscape.javascript.*;

import leo.shared.Constants;

import javax.swing.*;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ChatPanel extends JPanel implements ActionListener, MouseListener {


    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final JTextField chatInput;
    private final JTextPane chatOutput;
    private final StyledDocument doc;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatPanel() {

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

        // some scrolly stuff
        JScrollPane scroll = new JScrollPane(chatOutput, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Put it all together
        setLayout(new BorderLayout());
        add(chatInput, BorderLayout.SOUTH);
        add(scroll, BorderLayout.CENTER);
        //setSize(WIDTH, Client.getChatList().getHeight());
        //setSize(WIDTH, HEIGHT);

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
            System.out.println("ChatPanel.showText(): " + e);
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
        } catch (Exception e) {
            System.out.println("ChatPanel.disable(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Focus on the right component
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent ae) {
        Client.getImages().playSound(Constants.SOUND_BUTTON);
        String text = chatInput.getText();

//        if (text.startsWith("###")) {
//            try {
//
//                showText("Performing announcement test: ", text);
//                String announce = "javascript:alert('" + text + "')";
//                Client.applet().getAppletContext().showDocument(new java.net.URL(announce));
//                showText("Test done!", "");
//                return;
//            } catch (Exception e) {
//            }
//        }

        showText(Client.getName() + ": ", text);
        chatInput.setText("");
        Client.getChat().broadcast(text);
    }


    /////////////////////////////////////////////////////////////////
    // Focus on the right component
    /////////////////////////////////////////////////////////////////
    public void focus() {
        chatInput.requestFocus();
    }


    /////////////////////////////////////////////////////////////////
    // Clicky
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

}
