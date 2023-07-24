///////////////////////////////////////////////////////////////////////
// Name: AnimationImager
// Desc: An animation for displaying non-damage icons
// Date: 2/17/2009 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client.animation;

// imports

import leo.client.LeoComponent;
import leo.shared.Unit;

import java.awt.*;


public class AnimationImager extends AnimationImage {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private Unit trigger = null;
    private int duration = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationImager(short newLocation, int newImage, Unit newUnit, Unit newTrigger, int dur) {
        // PASS IN DURATION OF -1 FOR INFINITE ANIM
        super(newLocation, newImage, newUnit);
        trigger = newTrigger;
        duration = dur;
    }

    public void draw(Graphics2D g, Frame frame, LeoComponent surface) {
        if (duration > 0) {
            --duration;
            super.draw(g, frame, surface);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        if (trigger != null)
            return duration == 0 || (trigger.readStepX() == 0 && trigger.readStepY() == 0);
        else
            return duration == 0;
    }
}
