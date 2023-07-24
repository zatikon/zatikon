///////////////////////////////////////////////////////////////////////
//	Name:	AnimationOtherDamage
//	Desc:	Etc damage. No animation
//	Date:	11/29/2007 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.Client;
import leo.client.LeoComponent;
import leo.shared.Unit;

import java.awt.*;

public class AnimationOtherDamage implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private final short source;
    private final short location;
    private final short amount;
    private final String amountDesc;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationOtherDamage(short newSource, short newLocation, short newAmount, Unit victim) {
        source = newSource;
        location = newLocation;
        amount = newAmount;
        amountDesc = "" + amount;

        if (victim.isDead()) {
            Client.getGameData().
                    insert(new AnimationDeath(victim));
        }
        Client.getGameData().add(new AnimationDamage(source, location, amount, victim));
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (delay > 0) {
            --delay;
            return;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return true;
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
