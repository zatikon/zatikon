///////////////////////////////////////////////////////////////////////
// Name: AnimationBlast
// Desc: Green blast from the channeler
// Date: 12/13/2007 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.BattleField;
import leo.shared.Constants;
import leo.shared.Unit;

import java.awt.*;
import java.awt.geom.QuadCurve2D;


public class AnimationBlast implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private final static int DURATION = 15;
    private int duration;
    private final short source;
    private final short location;
    private final short damage;
    private final Unit victim;
    private AnimationImage ghost = null;
    private QuadCurve2D.Float curve = null;
    private QuadCurve2D.Float bcurve = null;
    private int x1, y1, cx, cy, x2, y2;
    private int bx1, by1, bcx, bcy, bx2, by2;
    private final BasicStroke stroke = new BasicStroke(4);

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationBlast(short newSource, short newLocation, short newDamage, Unit newVictim) {
        source = newSource;
        location = newLocation;
        damage = newDamage;
        victim = newVictim;

        // How long should this animation play
        duration = DURATION;

        if (victim.isDead()) {
            int appearance = (victim.getAppearance() +
                    (victim.getCastle() ==
                            Client.getGameData().getMyCastle() ?
                            0 :
                            1));


            ghost = new AnimationImage(location, appearance);
            Client.getGameData().insert(ghost);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        switch (duration) {
            case DURATION:
                Client.getImages().playSound(Constants.SOUND_START_CHANNEL);
                break;
        }

        duration--;
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

            bx1 = x1;
            by1 = y1;
            bx2 = x2;
            by2 = y2;
            bcx = cx;
            bcy = cy;

            bcurve = new QuadCurve2D.Float(x1, y1, cx, cy, x2, y2);
            curve = new QuadCurve2D.Float(bx1, by1, bcx, bcy, bx2, by2);
        }

        // random drift
        cx += (Client.getRandom().nextInt(40) - 20);
        cy += (Client.getRandom().nextInt(40) - 20);
        x2 += (Client.getRandom().nextInt(4) - 2);
        y2 += (Client.getRandom().nextInt(4) - 2);
        curve.setCurve(x1, y1, cx, cy, x2, y2);

        bcx += (Client.getRandom().nextInt(40) - 20);
        bcy += (Client.getRandom().nextInt(40) - 20);
        bx2 += (Client.getRandom().nextInt(4) - 2);
        by2 += (Client.getRandom().nextInt(4) - 2);
        bcurve.setCurve(bx1, by1, bcx, bcy, bx2, by2);

        Stroke tmp = g.getStroke();
        g.setStroke(stroke);

        g.setColor(Color.green);
        g.draw(curve);
        g.draw(bcurve);

        g.setStroke(tmp);

        g.drawImage(
                Client.getImages().getImage(Constants.IMG_GREEN_BALL),
                x1 - (Constants.SQUARE_SIZE / 2),
                y1 - (Constants.SQUARE_SIZE / 2),
                frame);

        g.drawImage(
                Client.getImages().getImage(Constants.IMG_GREEN_BALL),
                x2 - (Constants.SQUARE_SIZE / 2),
                y2 - (Constants.SQUARE_SIZE / 2),
                frame);

        g.drawImage(
                Client.getImages().getImage(Constants.IMG_GREEN_BALL),
                bx2 - (Constants.SQUARE_SIZE / 2),
                by2 - (Constants.SQUARE_SIZE / 2),
                frame);

        // If done, make the splat
        if (finished()) {
            if (ghost != null) {
                ghost.finish();
                Client.getGameData().insert(new AnimationDeath(victim));
            }

            Client.getGameData().add(new AnimationBurst(location, 8, Constants.IMG_GREEN_BALL));
            Client.getGameData().add(new AnimationDamage(source, location, damage, victim, Constants.SOUND_END_CHANNEL));
        }
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
