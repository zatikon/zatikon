///////////////////////////////////////////////////////////////////////
// Name: UserListCellRenderer
// Desc: Render the cell
// Date: 7/23/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;


public class UserListCellRenderer extends DefaultListCellRenderer {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // This is the only method defined by ListCellRenderer.  We just
    // reconfigure the Jlabel each time we're called.
    /////////////////////////////////////////////////////////////////
    public Component getListCellRendererComponent
    (JList list,
     Object value,   // value to display
     int index,      // cell index
     boolean iss,    // is the cell selected
     boolean chf)    // the list and the cell have the focus
    {

        // The DefaultListCellRenderer class will take care of
        // the JLabels text property, it's foreground and background
        // colors, and so on.
        super.getListCellRendererComponent(list, value, index, iss, chf);

        //ImageIcon icon = new ImageIcon(Client.getImages().getImage(Constants.IMG_USER_ICON));

        // We additionally set the JLabels icon property here.
        ImageIcon icon = new ImageIcon(Client.getPlayers().elementAt(index).getIcon());

        setIcon(icon);

        return this;
    }
}
