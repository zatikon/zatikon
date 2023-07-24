///////////////////////////////////////////////////////////////////////
//	Name:	Reconnector
//	Desc:	The reconnector
//	Date:	3/1/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.LoginAttempt;
import leo.shared.LoginResponse;


public class Reconnector implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Components
    /////////////////////////////////////////////////////////////////
    private Thread runner;
    private int attempts = 0;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Reconnector() {
        if (Client.shuttingDown()) return;
        runner = new Thread(this);
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // Run the sucker
    /////////////////////////////////////////////////////////////////
    public void run() {
        reconnect();
    }


    /////////////////////////////////////////////////////////////////
    // Reconnect
    /////////////////////////////////////////////////////////////////
    private void reconnect() {
        if (attempts >= 10 || !Client.load()) {
            die();
        }
        attempts++;
        try {
            Thread.sleep(2000);
            LoginAttempt la = new LoginAttempt(
                    Client.getName().toLowerCase(),
                    Client.getPassword(),
                    LoginAttempt.EXISTING_ACCOUNT,
                    Client.VERSION);
            ClientNetManager clientNetManager = new ClientNetManager(Client.shouldUseTls());
            LoginResponse lr = clientNetManager.connect(la);

            if (lr.getResponse() != LoginResponse.LOGIN_SUCCESSFUL) {
                die();
            }
            Client.reconnect();
            Client.setNetManager(clientNetManager);
            Client.startChat(lr.getChatID());
            Client.getGameData().reconnect();
            Client.getNetManager().start();
        } catch (Exception e) {
            Client.getGameData().screenMessage("Reconnect attempt " + attempts + " failed...");
            reconnect();
        }
    }

    /////////////////////////////////////////////////////////////////
    // Kill the app
    /////////////////////////////////////////////////////////////////
    private void die() {
        try {
            Client.getGameData().screenMessage("Reconnect failed.  Zatikon is closing...");
            Thread.sleep(2000);
            System.exit(0);
        } catch (Exception e) {
        }

    }

}
