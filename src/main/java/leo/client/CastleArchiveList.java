///////////////////////////////////////////////////////////////////////
// Name: CastleArchiveList
// Desc: A list of castle archives
// Date: 8/6/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class CastleArchiveList extends JDialog implements ActionListener {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    //private static final int HEIGHT= 400;
    private static final int WIDTH = 150;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private JList<CastleArchive> list;
    private JTextField input;
    private JButton load;
    private JButton save;
    private short index;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CastleArchiveList() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setSize(WIDTH,HEIGHT);
        setResizable(false);
        setLayout(new BorderLayout());

        // Build it
        Panel listPanel = createListPanel(Client.getCastleArchives());
        Panel bottomPanel = createBottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
        add(listPanel, BorderLayout.CENTER);
        add(new JLabel("Save/Load an Army", SwingConstants.CENTER), BorderLayout.NORTH);

        // Show it
        Dimension dim = getToolkit().getScreenSize();
        validate();
        pack();
        setLocation(
                (int) dim.getWidth() / 2 -
                        (getWidth() / 2),
                (int) dim.getHeight() / 2 -
                        (getHeight() / 2));

        setModal(true);
        setVisible(true);
    }


    /////////////////////////////////////////////////////////////////
    // Update/repaint/whatever
    /////////////////////////////////////////////////////////////////
    public void update() { //list.repaint();
        //save.repaint();
        //load.repaint();
        //input.repaint();
        //repaint();
    }


    /////////////////////////////////////////////////////////////////
    // The list panel
    /////////////////////////////////////////////////////////////////
    private Panel createListPanel(CastleArchive[] castleArchive) {
        Panel panel = new Panel();

        // The JList
        list = new JList<>(castleArchive);
        list.setFixedCellWidth(WIDTH);
        list.setFixedCellHeight(14);
        list.setVisibleRowCount(10);

        // Mouse listener
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                index = (byte) list.locationToIndex(e.getPoint());
                CastleArchive ca = Client.getCastleArchives()[index];
                if (ca.size() >= 1) {
                    load.setEnabled(true);
                    save.setEnabled(true);
                    input.setEnabled(true);
                    input.setText(ca.toString());
                    input.requestFocus();
                    input.selectAll();
                } else {
                    load.setEnabled(false);
                    save.setEnabled(true);
                    input.setEnabled(true);
                    input.setText("");
                    input.requestFocus();
                    input.selectAll();
                }
                update();
            }
        };
        list.addMouseListener(mouseListener);

        // The scroll pane
        JScrollPane scrollPane = new JScrollPane(list, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // Add the custom renderer
        //list.setCellRenderer(new UserListCellRenderer());

        // bundle it up
        panel.add(scrollPane);
        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // The bottom panel
    /////////////////////////////////////////////////////////////////
    private Panel createBottomPanel() {
        Panel panel = new Panel();
        panel.setLayout(new GridLayout(2, 1));

        input = new JTextField();
        input.setEditable(true);
        input.setDocument(new TextFieldLimiter(20));
        input.setEnabled(false);

        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        load = new JButton("Load");
        load.setEnabled(false);
        load.addActionListener(this);

        save = new JButton("Save");
        save.setEnabled(false);
        save.addActionListener(this);

        buttonPanel.add(load);
        buttonPanel.add(save);

        panel.add(input);
        panel.add(buttonPanel);

        return panel;
    }


    /////////////////////////////////////////////////////////////////
    // Perform action
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == load) {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            dispose();
            Client.getGameData().screenMessage("Loading data...");
            Client.getGameData().queue();
            Client.getNetManager().sendAction(leo.shared.Action.LOAD_ARCHIVE, index, leo.shared.Action.NOTHING);
        }

        if (e.getSource() == save) {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            dispose();
            Client.getGameData().screenMessage("Saving data...");
            Client.getGameData().queue();
            Client.getNetManager().saveCastleArchive(index, input.getText());
        }

    }

}
