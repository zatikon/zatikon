///////////////////////////////////////////////////////////////////////
//	Name:	Newsletter
//	Desc:	The newsletter popup
//	Date:	9/15/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Newsletter extends JDialog implements ActionListener {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static boolean answer = false;
    private final Button yes;
    private final Button no;
    private final String email;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Newsletter(String newEmail) {
        email = newEmail;
        JPanel panel = new JPanel();

        yes = new Button("Yes");
        yes.addActionListener(this);
        no = new Button("No thanks");
        no.addActionListener(this);
        JPanel buttons = new JPanel();
        buttons.setLayout(new GridLayout(1, 2));
        buttons.add(yes);
        buttons.add(no);

        panel.setLayout(new GridLayout(2, 1));
        panel.add(new JLabel(" Would you like to subscribe to the Chronic Logic newsletter? "));
        panel.add(buttons);
        setLayout(new GridLayout(1, 1));
        add(panel);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setResizable(false);

        // Show it
        Dimension dim = getToolkit().getScreenSize();
        validate();

        setModal(true);
        pack();

        setLocation(
                (int) dim.getWidth() / 2 -
                        (getWidth() / 2),
                (int) dim.getHeight() / 2 -

                        (getHeight() / 2));

        setVisible(true);
    }


    /////////////////////////////////////////////////////////////////
    // Get the answer
    /////////////////////////////////////////////////////////////////
    public static boolean answer() {
        return answer;
    }


    /////////////////////////////////////////////////////////////////
    // Press the button
    /////////////////////////////////////////////////////////////////
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == no) {
            answer = false;
            dispose();
        }

        if (e.getSource() == yes) {
            answer = true;
            dispose();
        }

    }


}
