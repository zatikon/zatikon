///////////////////////////////////////////////////////////////////////
//	Name:	ClientRenderer
//	Desc:	The game renderer
//	Date:	5/24/2002 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import org.tinylog.Logger;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Date;


public class ClientRenderer implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final int FRAME_DELAY = 50;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private Graphics2D graphics;
    private final BufferStrategy bufferStrategy;
    private final GameFrame mainFrame;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientRenderer(BufferStrategy newBufferStrategy, GameFrame newMainFrame) {    // Initialize the colors

        // Get the default font
        //font = new Font("SansSerif", Font.PLAIN, 12);
        //System.out.println("Font selected: " + font.getFamily() + ": " + font.getSize());

        bufferStrategy = newBufferStrategy;
        mainFrame = newMainFrame;
        Client.setFrame(mainFrame);

        Client.getGameData().screenProgress();

        runner = new Thread(this, "ClientRendererThread");
        runner.start();

        initialize();
    }


    /////////////////////////////////////////////////////////////////
    // Main loop
    /////////////////////////////////////////////////////////////////
    public void run() {
        long t1, t2, dt;
        Date time;

        // Main game loop
        try {
            while (Client.rendering() && !Client.shuttingDown()) {    // get the time
                time = new Date();
                t1 = time.getTime();

                while (Client.computing() && !Client.shuttingDown()) {
                    Client.setDraw(false);
                    Thread.sleep(5);
                }

                // Render the frame
                Client.setDraw(true);
                renderFrame();
                Client.setDraw(false);

                // get the time again
                time = new Date();
                t2 = time.getTime();

                // compare times
                dt = t2 - t1;

                //if (dt > FRAME_DELAY) System.out.println("System lagging @: " + (dt-FRAME_DELAY));

                graphics.dispose();

                // slow down!
                Thread.sleep(
                        FRAME_DELAY - (int) dt < 0 ? 1 : FRAME_DELAY - (int) dt);
            }
        } catch (Exception e) {
            Client.setDraw(false);
            Logger.error("ClientRenderer.run(): " + e);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Start some stuff
    /////////////////////////////////////////////////////////////////
    private void initialize() {
        // Load the graphics
        Client.getImages().load();

        // get the army
        Client.getNetManager().getArmyUnits();

        Client.getImages().initialize();

        // start the sound track
        Client.getImages().playMusic();

        mainFrame.toFront();
        mainFrame.requestFocus();
        Client.load(true);
    }


    /////////////////////////////////////////////////////////////////
    // Render a frame
    /////////////////////////////////////////////////////////////////
    private void renderFrame() {
        // Get the drawing surface
        graphics = (Graphics2D) bufferStrategy.getDrawGraphics();

        // Set anti-aliasing on
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Set the font
        graphics.setFont(Client.getFont());

        if (Client.rendering()) Client.getGameData().getMainBoard().draw(graphics, mainFrame);

        // Show the scene
        bufferStrategy.show();

        // Kill the old graphics
        graphics.dispose();
    }

}
