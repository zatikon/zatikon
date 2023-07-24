///////////////////////////////////////////////////////////////////////
// Name: AnimationBuild
// Desc: A wall is built
// Date: 7/28/2011 - Julian Noble
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

public class AnimationBuild implements Animation {

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
    private final float alpha;
    boolean ally;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationBuild(Unit newVictim) {
        victim = newVictim;
        location = victim.getLocation();

        appearance = victim.getAppearance();
        ally = victim.getCastle() == Client.getGameData().getMyCastle();

        alpha = 0.0f;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
        //alpha+=0.1f;
        //if (alpha > 1) alpha = 1;

        // For the middle
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + victim.getStepX() + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + victim.getStepY() + surface.getScreenY();

        //int size = Constants.SQUARE_SIZE;

        Composite original = g.getComposite();
        //g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));

        //g.drawImage(Client.getImages().getImage(appearance), x, y, frame);
        g.drawImage(Client.getImages().getImage(Constants.IMG_BUILD_01 + duration), x, y, frame);

        g.setComposite(original);

        duration++;
    }


    /////////////////////////////////////////////////////////////////
    // Duration expired?
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        if (duration > 33) {
            victim.setHidden(false);
            return true;
        } else {
            return false;
        }
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
