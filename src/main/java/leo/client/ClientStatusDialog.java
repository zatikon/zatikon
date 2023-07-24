///////////////////////////////////////////////////////////////////////
//	Name:	ClientStatusDialog
//	Desc:	A dialog box containing a message
//	Date:	2/8/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import java.awt.*;


public class ClientStatusDialog extends Dialog {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int HEIGHT = 60;
    private static final int WIDTH = 200;
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private final Label label;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientStatusDialog(String title) {
        super(new Frame(), title);

        int width = WIDTH + getInsets().left;
        int height = HEIGHT + getInsets().top;

        // Create the components
        label = new Label("");
        setLayout(new BorderLayout());
        add(label, "Center");

        // Set the attributes
        setSize(width, height);
        setResizable(false);

        // Set the position to the center of the screen
        Dimension dim = getToolkit().getScreenSize();
        setLocation(
                (int) dim.getWidth() / 2 - (width / 2),
                (int) dim.getHeight() / 2 - (height / 2));

        // Show the frame
        validate();
    }


    /////////////////////////////////////////////////////////////////
    // Set the text
    /////////////////////////////////////////////////////////////////
    public void setText(String message) {
        setVisible(true);
        label.setText(message);
        repaint();
    }
}
