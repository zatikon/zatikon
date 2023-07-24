///////////////////////////////////////////////////////////////////////
//	Name:	GameApplet
//	Desc:	Facebook gateway
//	Date:	3/8/2010 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;


//public class GameApplet extends JApplet {
//
//    //////////////////////////////////////////////////////////////////////
//    // Begin execution here
//    //////////////////////////////////////////////////////////////////////
//    @Override
//    public void init() {
//        String userid = getParameter("userid");
//        String passkey = getParameter("passkey");
//        Client.init(this, userid, passkey);
//    }
//
//
//    //////////////////////////////////////////////////////////////////////
//    // If someone navigates away from the page, this happens
//    //////////////////////////////////////////////////////////////////////
//    @Override
//    public void destroy() {
//        super.destroy();
//        if (!Client.shuttingDown()) {
//            Client.shutdown();
//        }
//
//    }
//
//    public void pack() {
//        Container cp = getContentPane();
//        Dimension d = cp.getLayout().preferredLayoutSize(cp);
//        setSize((int) d.getWidth(), (int) d.getHeight());
//    }
//}
