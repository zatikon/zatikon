///////////////////////////////////////////////////////////////////////
//	Name:	ClientBlink
//	Desc:	For flashing the taskbar representation of a JFrame
//	Date:	10/1/2010 - W. Fletcher Cole
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public final class ChatBlink {

    /**
     * Alerter object used for alerts on windows
     */
    static WindowAlerter alerter;

    /**
     * Force a window to flash if not focused
     */
    public static void AlertOnWindow(JFrame frm) {
        try {
            if (alerter == null) {
                alerter = new WindowAlerter();
            }
            alerter.flash(frm);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static class WindowAlerter {
        java.awt.Dialog d;

        WindowAlerter() {

        }

        /**
         * It flashes the window's taskbar icon if the window is not focused.
         * <br/> The flashing "stops" when the window becomes focused.
         **/
        public void flash(final Window w) {
            d = new java.awt.Dialog((JFrame) w);
            d.setUndecorated(true);
            d.setSize(0, 0);
            d.setModal(false);

            d.addWindowFocusListener(new WindowAdapter() {

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    w.requestFocus();
                    d.setVisible(false);
                    super.windowGainedFocus(e);
                    //w.removeWindowFocusListener(w.getWindowFocusListeners()[0]);
                    //d.removeWindowFocusListener(d.getWindowFocusListeners()[0]);
                }
            });
            w.addWindowFocusListener(new WindowAdapter() {

                @Override
                public void windowGainedFocus(WindowEvent e) {
                    d.setVisible(false);
                    super.windowGainedFocus(e);
                    //w.removeWindowFocusListener(w.getWindowFocusListeners()[0]);
                    //d.removeWindowFocusListener(d.getWindowFocusListeners()[0]);
                }
            });

            if (!w.isFocused()) {
                //if (d.isVisible())
                {
                    d.setVisible(false);
                }
                d.setLocation(0, 0);
                d.setLocationRelativeTo(w);
                d.setVisible(true);
            }
        }
    }
}
