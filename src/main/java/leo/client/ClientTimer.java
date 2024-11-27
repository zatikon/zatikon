///////////////////////////////////////////////////////////////////////
//	Name:	ClientTimer
//	Desc:	The client timer
//	Date:	7/23/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

public class ClientTimer implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Thread runner;
    private boolean active = true;
    private int time = 0;
    private boolean paused = false;
    private boolean justUnpaused = false;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientTimer(int count) {
        time = count;
        runner = new Thread(this, "ClientTimerThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // The main loop
    /////////////////////////////////////////////////////////////////
    public void run() {
        // Loop indefinately, accepting socket connections
        while (active() && !Client.shuttingDown()) {
            try {
                if(!Client.standalone || paused == false) {
                    if(Client.standalone && justUnpaused == true) 
                        justUnpaused = false;
                    if (time > 0 && active())
                        time--;
                    else {
                        if (active() && Client.getGameData().getCastlePlaying() == Client.getGameData().getMyCastle()) {
                            Client.getGameData().endTurn();
                            end();
                        }
                    }
                }
                if (active()) Thread.sleep(1000);
            } catch (Exception e) {
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // active
    /////////////////////////////////////////////////////////////////
    private boolean active() {
        return (active && Client.getGameData() != null && Client.getGameData().playing());
    }

    /////////////////////////////////////////////////////////////////
    // Get the time
    /////////////////////////////////////////////////////////////////
    public int getTime() {
        return time;
    }

    public void togglePaused() {
        if(paused == true)
            justUnpaused = true;
        paused = !paused;
    }

    public boolean getPaused() {
        return paused;
    }

    public boolean getjustUnpaused() {
        return justUnpaused;
    }     

    /////////////////////////////////////////////////////////////////
    // End
    /////////////////////////////////////////////////////////////////
    public void end() {
        active = false;
    }

}
