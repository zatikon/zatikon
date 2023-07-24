///////////////////////////////////////////////////////////////////////
// Name: AnimationMelee
// Desc: Melee attack animation (sorta)
// Date: 11/29/2007 - Gabe Jones
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

public class AnimationMelee implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private short duration = 5;
    private final short source;
    private final short location;
    private final short amount;
    private final String amountDesc;
    private int delay = 4;
    private boolean shown = false;
    private final Unit victim;
    private AnimationImage ghost = null;

    private int animDelay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationMelee(short newSource, short newLocation, short newAmount, Unit newVictim) {
        source = newSource;
        location = newLocation;
        amount = newAmount;
        amountDesc = "" + amount;
        victim = newVictim;

        if (victim.isDead()) {
            int appearance = (victim.getAppearance() +
                    (victim.getCastle() ==
                            Client.getGameData().getMyCastle() ?
                            0 :
                            1));


            ghost = new AnimationImage(location, appearance, victim);
            Client.getGameData().insert(ghost);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (animDelay > 0) {
            --animDelay;
            return;
        }

        if (delay > 0) {
            delay--;
            return;
        }

        duration--;

        if (!shown) {
            if (ghost != null) {
                ghost.finish();
                Client.getGameData().
                        insert(new AnimationDeath(victim));
            }
            Client.getGameData().add(new AnimationDamage(source, location, amount, victim));
            shown = true;
        }

        // The x and y for the source
        int sx = (BattleField.getX(source) * Constants.SQUARE_SIZE) + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
        int sy = (BattleField.getY(source) * Constants.SQUARE_SIZE) + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

        // Get the x and y for the splat
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();

        g.setColor(Color.black);
        g.drawLine(sx, sy, x + Constants.SQUARE_SIZE / 2, y + Constants.SQUARE_SIZE / 2);

    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return duration < 0;
    }

    public void setDelay(int d) {
        animDelay = d;
    }

    public short getLocation() {
        return location;
    }

    public void setDirection(short b) {
        return;
    }
}
