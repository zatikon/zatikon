///////////////////////////////////////////////////////////////////////
// Name: AnimationFade
// Desc: A unit dies
// Date: 6/26/2008 - Gabe Jones
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

public class AnimationFade implements Animation {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Properites
    /////////////////////////////////////////////////////////////////
    private short duration = 0;
    private final Unit victim;
    private final short location;
    private final int appearance;
    private float alpha;
    private final boolean ally;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationFade(Unit newVictim) {
        victim = newVictim;
        location = victim.getLocation();

        appearance = victim.getAppearance();

        ally = victim.getCastle() == Client.getGameData().getMyCastle();
        alpha = 1.0f;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        duration++;
        alpha -= 0.1f;
        if (alpha < 0) alpha = 0;

        // For the middle
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + victim.getStepX() + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + victim.getStepY() + surface.getScreenY();

        int size = Constants.SQUARE_SIZE;

        Composite original = g.getComposite();
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        //g.drawImage(Client.getImages().getImage(appearance), x, y, frame);
        g.drawImage(Client.getImages().getGrayedImage(appearance, ally), x, y, frame);

        g.setComposite(original);
    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration > 10;
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
