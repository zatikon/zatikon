///////////////////////////////////////////////////////////////////////
// Name: AdFrame
// Desc: Ad Frame
// Date: 8/29/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class AdFrame extends JDialog {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AdFrame(Frame frame) {
        super(frame);

        JLabel ad = new JLabel(new ImageIcon(Client.getImages().getImage(Constants.IMG_AD)));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ad.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        ad.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
//                try {
//
//                    if (Client.applet() == null) {
                        Browser.open("http://www.chroniclogic.com/index.htm?zat_order.htm");
//                    } else {
//                        Client.applet().getAppletContext().showDocument(new java.net.URL("http://www.chroniclogic.com/index.htm?zat_order.htm"));
//                        dispose();
//                    }
//
//                } catch (Exception e) {
//                    System.out.println("AdFrame(): " + e);
//                }
            }
        });

        add(ad);

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
        setVisible(true);
    }

}
