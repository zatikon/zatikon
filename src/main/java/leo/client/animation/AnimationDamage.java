///////////////////////////////////////////////////////////////////////
// Name: AnimationDamage
// Desc: An animation for displaying damage
// Date: 6/21/2003 - Gabe Jones
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
import java.awt.geom.AffineTransform;


public class AnimationDamage implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private short duration = 40;
    private final short maxdur = duration;
    private final short source;
    private final short location;
    private final short amount;
    private final String amountDesc;
    private float alpha = 1.0f;
    private final float angle = Client.getRandom().nextFloat();
    private int x, y, sx, sy, startX, startY = -1;
    private boolean start;
    private final AffineTransform transform = new AffineTransform();
    private final boolean organic;
    private int delay = 0;
    private short direction = 0;
    //                          0      1      2     3     4      5      6      7
    private final short[] directions = {0, -2, 1, -1, 2, -2, 1, 1, -2, -2, -1, 1, -1, 0, -1, -1};


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationDamage(short newSource, short newLocation, short newAmount, Unit victim) {
        source = newSource;
        location = newLocation;
        amount = newAmount;
        amountDesc = "" + amount;
        start = true;
        organic = victim.getOrganic(victim);

        if (amount <= 0) {
            if (amount == 0)
                Client.getImages().playSound(Constants.SOUND_TING);
            else if (amount == -2)
                Client.getImages().playSound(Constants.SOUND_ORG_HIT);
            else if (amount == leo.shared.Event.DEAD) {
                if (organic) {
                    Client.getImages().playSound(Constants.SOUND_SQUISH);
                } else {
                    Client.getImages().playSound(Constants.SOUND_INORG_DEATH);
                }
            }
        } else if (organic) {
            if (victim.isDead())
                Client.getImages().playSound(Constants.SOUND_ORG_DEATH);
            else
                Client.getImages().playSound(Constants.SOUND_ORG_HIT);
        } else {
            if (victim.isDead())
                Client.getImages().playSound(Constants.SOUND_INORG_DEATH);
            else
                Client.getImages().playSound(Constants.SOUND_INORG_HIT);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationDamage(short newSource, short newLocation, short newAmount, Unit victim, short sound) {
        source = newSource;
        location = newLocation;
        amount = newAmount;
        amountDesc = "" + amount;
        start = true;
        organic = victim.getOrganic(victim);

        if (sound >= 0)
            Client.getImages().playSound(sound);

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {

        if (delay > 0) {
            delay--;
            return;
        }

        if (duration < (maxdur - 30)) alpha -= 0.1f;
        if (alpha < 0) alpha = 0.0f;

        if (start) {
            // The x and y for the source
            sx = (BattleField.getX(source) * Constants.SQUARE_SIZE) + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
            sy = (BattleField.getY(source) * Constants.SQUARE_SIZE) + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

            // Get the x and y for the splat
            x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
            y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();

            startX = x;
            startY = y;

            start = false;
        } else {
            if (duration < (maxdur)) {
                x += directions[direction * 4];
                y += directions[direction * 4 + 1];
            }
        }

        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        if (amount == leo.shared.Event.CANCEL) {
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 3*(alpha/4)));
            g.drawImage(Client.getImages().getImage(Constants.IMG_MPBACK), x, y, frame);
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.drawImage(Client.getImages().getImage(Constants.IMG_MISS), x, y, frame);

        } else if (amount == leo.shared.Event.PARRY) {
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 3*(alpha/4)));
            g.drawImage(Client.getImages().getImage(Constants.IMG_MPBACK), x, y, frame);
            //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
            g.drawImage(Client.getImages().getImage(Constants.IMG_PARRY), x, y, frame);

        } else {
            // splat animation (5 frames)
            if (duration > (maxdur - 5)) {
                // translate the margin
                transform.translate(startX, startY);
                // rotate the image
                transform.rotate(angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);
                // draw the image (either organic splat or inorganic splat)
                if (organic)
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SPLAT_ORGANIC_1 + (maxdur - duration)), transform, frame);
                else
                    g.drawImage(Client.getImages().getImage(Constants.IMG_SPLAT_INORGANIC_1 + (maxdur - duration)), transform, frame);
                // derotate
                transform.rotate(-angle, Constants.SQUARE_SIZE / 2, Constants.SQUARE_SIZE / 2);
                // demargin
                transform.translate(-startX, -startY);
            }

            // if damage was dealt, draw numbers
            if (amount >= 0) {
                //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 3*(alpha/4)));
                g.drawImage(Client.getImages().getImage(Constants.IMG_DAMBACK), x, y, frame);
                //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

                g.setFont(Client.getFontBig());
                int atX = x + (Constants.SQUARE_SIZE / 2) - (g.getFontMetrics().stringWidth(amountDesc) / 2);
                int atY = y + (Constants.SQUARE_SIZE / 2) + (Client.FONT_HEIGHT / 2);
                // black outline
                g.setColor(Color.black);
                g.drawString(amountDesc, atX + 1, atY + 1);
                g.drawString(amountDesc, atX - 1, atY - 1);
                g.drawString(amountDesc, atX - 1, atY + 1);
                g.drawString(amountDesc, atX + 1, atY - 1);
                // yellow center
                g.setColor(Color.yellow);
                g.drawString(amountDesc, atX, atY);
                // change font back
                g.setFont(Client.getFont());
            }
        }
        g.setComposite(original);

        // decrement duration
        duration--;
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

    public void setDirection(short dir) {
        direction = (byte) (((int) dir) % 3);
    }

}
