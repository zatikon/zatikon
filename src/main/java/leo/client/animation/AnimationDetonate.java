///////////////////////////////////////////////////////////////////////
// Name: AnimationDetonate
// Desc: A nasty, juicy pop
// Date: 12/26/2007 - Gabe Jones
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


public class AnimationDetonate implements Animation {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private short duration = 0;
    private final short source;
    private final short location;
    private boolean finished = false;
    private final Unit victim;
    private int scale = 10;

    private int delay = 0;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public AnimationDetonate(short newSource, short newLocation, Unit newVictim) {
        source = newSource;
        location = newLocation;
        victim = newVictim;
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
            case 0:
                drawBloat(g, frame, surface);
                break;
            case 1:
                Client.getImages().playSound(Constants.SOUND_DETONATE);
                drawBloat(g, frame, surface);
                break;
            case 2:
                drawBloat(g, frame, surface);
                break;

            case 3:
                drawPop(g, frame, surface, duration - 3);
                break;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                drawPop(g, frame, surface, duration - 3);
                break;

            case 18:
                finished = true;
                break;
        }
        duration++;
    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void drawBloat(Graphics2D g, Frame frame, LeoComponent surface) {
        // Get the x and y for the splat
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX() - (scale / 2);
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY() - (scale / 2);

        int size = Constants.SQUARE_SIZE + scale;

        scale += 15;

        int appearance = (victim.getAppearance() +
                (victim.getCastle() ==
                        Client.getGameData().getMyCastle() ?
                        0 :
                        1));

        g.drawImage(Client.getImages().getImage(appearance), x, y, size, size, frame);

    }


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    public void drawPop(Graphics2D g, Frame frame, LeoComponent surface, int frameCount) {
        // The x and y for the source
        int sx = (BattleField.getX(source) * Constants.SQUARE_SIZE) + surface.getScreenX() + Constants.SQUARE_SIZE / 2;
        int sy = (BattleField.getY(source) * Constants.SQUARE_SIZE) + surface.getScreenY() + Constants.SQUARE_SIZE / 2;

        // For the middle
        int x = (BattleField.getX(location) * Constants.SQUARE_SIZE) + surface.getScreenX();
        int y = (BattleField.getY(location) * Constants.SQUARE_SIZE) + surface.getScreenY();

        g.setColor(Color.black);
        g.drawLine(sx, sy, x + Constants.SQUARE_SIZE / 2, y + Constants.SQUARE_SIZE / 2);

        g.drawImage(Client.getImages().getImage(Constants.IMG_DETONATE_1 + frameCount), x - Constants.SQUARE_SIZE, y - Constants.SQUARE_SIZE, frame);
    }


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    public boolean finished() {
        return finished;
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
