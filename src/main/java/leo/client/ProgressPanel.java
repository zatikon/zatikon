///////////////////////////////////////////////////////////////////////
// Name: MessagePanel
// Desc: A panel that reads a simple message
// Date: 6/28/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class ProgressPanel extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final GradientPaint paint = new GradientPaint(0, 0, Color.black, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, Color.gray.darker().darker().darker());


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ProgressPanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.WINDOW_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.gray.darker().darker());
        Paint original = g.getPaint();
        g.setPaint(paint);
        g.fillRect(0, 0, Constants.WINDOW_WIDTH + Client.getInsetX(), Constants.SCREEN_HEIGHT + Client.getInsetY());
        g.setPaint(original);
        String text = Client.getImages().getProgress();
        g.setFont(Client.getFontBig());

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (20 / 2);

        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);
        g.setColor(Color.yellow);

        g.drawString(text, atX, atY);
        g.setFont(Client.getFont());

    }
}
