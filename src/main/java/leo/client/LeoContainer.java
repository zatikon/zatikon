///////////////////////////////////////////////////////////////////////
// Name: LeoContainer
// Desc: A basic leopold container
// Date: 5/23/2003 - Created [Gabe Jones]
//       12/9/2010 - clickAt checks for the tutorial first  [Dan Healy]
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import java.awt.*;
import java.util.Vector;
//import org.tinylog.Logger;

public class LeoContainer extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Vector<LeoComponent> components = new Vector<LeoComponent>();
    //private Iterator<LeoComponent> iterator = components.Iterator<LeoComponent>();


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LeoContainer(int newX, int newY, int newWidth, int newHeight) {
        super(newX, newY, newWidth, newHeight);
    }


    /////////////////////////////////////////////////////////////////
    // Remove the components
    /////////////////////////////////////////////////////////////////
    public void clear() {
        components.clear();
    }


    /////////////////////////////////////////////////////////////////
    // Activate the component
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        //Logger.info(this.getClass().getSimpleName() + " (" + this + ") calling clickAt at (" + x + ", " + y + ")");
        if (!components.isEmpty()) {
            LeoComponent last = components.lastElement();
            if (last instanceof TutorialBoard) {
                if (last.clickAt(x, y))
                    return true;
            }
        }
        //If the last element is not the tutorial (or if it is but click is false):
        for (int i = 0; i < components.size(); i++) {
            LeoComponent component = components.elementAt(i);
            //Logger.info("checking in " + component);
            if (component != null && !(component instanceof TutorialBoard) && component.isWithin(x, y)) {
                //Logger.info("found passing to " + component);
                return component.clickAt(x, y);
            }
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        for (int i = 0; i < components.size(); i++) {
            LeoComponent component = components.elementAt(i);
            if (component != null) component.draw(g, mainFrame);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Add a component
    /////////////////////////////////////////////////////////////////
    public void add(LeoComponent newComponent) {
        components.add(newComponent);
        newComponent.setParent(this);
    }


    /////////////////////////////////////////////////////////////////
    // Remove a component
    /////////////////////////////////////////////////////////////////
    public void remove(LeoComponent oldComponent) {
        components.remove(oldComponent);
    }


    public Vector<LeoComponent> getComponents() {
        return components;
    }
}