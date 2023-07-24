///////////////////////////////////////////////////////////////////////
// Name: CreditsFrame
// Desc: Credits Frame
// Date: 8/14/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;

public class CreditsFrame extends JDialog {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CreditsFrame() {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(newLabel("Zatikon " + Client.VERSION, true));
        panel.add(newLabel("Chronic Logic LLC", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel(" Programming and Design ", true));
        panel.add(newLabel("Gabe Jones", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel(" Producer ", true));
        panel.add(newLabel("Josiah Pisciotta", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel(" Additional Programming ", true));
        panel.add(newLabel("William Cole", false));
        panel.add(newLabel("Linus Foster", false));
        panel.add(newLabel("Daniel Healy", false));
        panel.add(newLabel("Alexander McCaleb", false));
        panel.add(newLabel("Julian Noble", false));
        panel.add(newLabel("David Schwartz", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel(" Art ", true));
        panel.add(newLabel("Alex Biskner", false));
        panel.add(newLabel("Samuel Goldberg", false));
        panel.add(newLabel("Sean Madden", false));
        panel.add(newLabel("Julian Noble", false));
        panel.add(newLabel("Amber Okamura", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel("Audio", true));
        panel.add(newLabel("Tony Porter", false));
        panel.add(newLabel(" ", false));

        panel.add(newLabel("Zatikon FOSS project", true));
        panel.add(newLabel("Lukky513", false));
        panel.add(newLabel(" ", false));

        add(panel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);
        setBackground(Color.white);

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

        //ImageIcon icon = new ImageIcon(Client.getImages().getImage(Constants.IMG_USER_ICON));
        //setIcon(icon);
    }

    private JLabel newLabel(String text, boolean bold) {
        //newLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        String txt;

        if (bold) { //Font f = newLabel.getFont();
            //newLabel.setFont(f.deriveFont(f.getStyle() ^ Font.BOLD));
            txt = "<html><b>" + text + "</b></html>";
        } else {
            txt = "<html>" + text + "</html>";

        }

        JLabel newLabel = new JLabel(txt, JLabel.CENTER);
        return newLabel;
    }
}
