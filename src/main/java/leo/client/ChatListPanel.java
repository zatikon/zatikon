///////////////////////////////////////////////////////////////////////
// Name: ChatListPanel
// Desc: A panel of deploy buttons
// Date: 7/23/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Vector;


public class ChatListPanel extends JPanel {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private JList list;
    private final ChatList parent;
    Vector getUsers;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatListPanel(ChatList newParent, Vector users) {
        getUsers = users;
        parent = newParent;
        setLayout(new BorderLayout());

        // Build it
        JPanel listPanel = createListPanel(users);
        JPanel bottomPanel = createBottomPanel();
        //add(bottomPanel, BorderLayout.SOUTH);
        add(listPanel, BorderLayout.CENTER);

    }


    /////////////////////////////////////////////////////////////////
    // Update/repaint/whatever
    /////////////////////////////////////////////////////////////////
    public void update() {
        createListPanel(getUsers);
        list.repaint();
        list.setCellRenderer(new UserListCellRenderer());
        if (parent != null) parent.repaint();
    }


    /////////////////////////////////////////////////////////////////
    // The list panel
    /////////////////////////////////////////////////////////////////
    private JPanel createListPanel(Vector vector) {
        JPanel panel = new JPanel();

        // The JList
        list = new JList(vector);

//        if (Client.applet() == null) {
//            list.setFixedCellWidth(WIDTH - 30);
//            list.setFixedCellHeight(30);
//            list.setVisibleRowCount(15);
//        }

        // Mouse listener
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Client.getImages().playSound(Constants.SOUND_BUTTON);
                    int index = list.locationToIndex(e.getPoint());
                    ChatPlayer player = Client.getPlayers().elementAt(index);
                    MessageFrame mf = player.getMessageFrame();
                    mf.setVisible(true);
                    mf.focus();
                }
            }
        };
        list.addMouseListener(mouseListener);

        // The scroll pane
        JScrollPane scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the custom renderer
        list.setCellRenderer(new UserListCellRenderer());

        // bundle it up
        panel.add(scrollPane);
        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // The bottom panel
    /////////////////////////////////////////////////////////////////
    private JPanel createBottomPanel() {
        JPanel panel = new JPanel();
        return panel;
    }

}
