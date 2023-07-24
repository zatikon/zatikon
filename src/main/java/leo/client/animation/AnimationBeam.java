///////////////////////////////////////////////////////////////////////
// Name: AnimationBeam
// Desc: Purple beam from the channeler
// Date: 7/29/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;

import java.awt.*;
import java.awt.geom.QuadCurve2D;


public class AnimationBeam implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private int duration;
    private final short source;
    private final short location;
    private QuadCurve2D.Float curve = null;
    private final QuadCurve2D.Float bcurve = null;
    private int x1, y1, cx, cy, x2, y2;
    private int bx1, by1, bcx, bcy, bx2, by2;
    private final BasicStroke stroke = new BasicStroke(1);
    private final Color purple;
    private float alpha = 1.0f;
    private final float alphaFade;

    private int delay = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationBeam(short newSource, short newLocation, Color newColor, float newAlphaFade) {
        source = newSource;
        location = newLocation;
        purple = newColor;
        alphaFade = newAlphaFade;

        // How long should this animation play
        duration = 20;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        alpha -= alphaFade;
        duration--;
        if (alpha < 0) alpha = 0;
        if (curve == null) {
            // The x and y for the source
            x1 = (BattleField.getX(source) * Constants.SQUARE_SIZE)
                    + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
            y1 = (BattleField.getY(source) * Constants.SQUARE_SIZE)
                    + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

            // Get the x and y for the splat
            x2 = (BattleField.getX(location) * Constants.SQUARE_SIZE)
                    + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
            y2 = (BattleField.getY(location) * Constants.SQUARE_SIZE)
                    + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

            cx = (x1 - x2) / 2;
            cy = (y1 - y2) / 2;
            cx = x2 + cx;
            cy = y2 + cy;

            curve = new QuadCurve2D.Float(x1, y1, cx, cy, x2, y2);
        }

        // random drift
        cx += (Client.getRandom().nextInt(20) - 10);
        cy += (Client.getRandom().nextInt(20) - 10);
        //x2+=(Client.getRandom().nextInt(4) - 2);
        //y2+=(Client.getRandom().nextInt(4) - 2);
        curve.setCurve(x1, y1, cx, cy, x2, y2);

        Stroke tmp = g.getStroke();
        g.setStroke(stroke);
        g.setColor(purple);
        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        g.draw(curve);

        g.setStroke(tmp);
        g.setColor(Color.black);
        g.setComposite(original);
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration < 0;
    }

    public void setDelay(int d) {
        delay = d;
    }

    public short getLocation() {
        return location;
    }

    public void setDirection(short b) {
        return;
    }
}
