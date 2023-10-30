///////////////////////////////////////////////////////////////////////
//	Name:	ChatServer
//	Desc:	The chat server
//	Date:	8/6/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Log;
import leo.shared.network.SocketProvider;

import java.net.ServerSocket;
import java.net.Socket;


public class ChatServer implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final int port;
    private final Thread runner;
    private final boolean useTls;
    private final Server server;
    private ServerSocket serverSocket;
    private boolean ready = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ChatServer(Server server, int newPort, boolean useTls) {
        this.server = server;
        this.port = newPort;
        this.useTls = useTls;
        runner = new Thread(this, "ChatServerThread");
        runner.start();
    }


    /////////////////////////////////////////////////////////////////
    // The main loop
    /////////////////////////////////////////////////////////////////
    public void run() {
        // Create the server socket
        try {
            serverSocket = SocketProvider.newServerSocket(port, useTls);
        } catch (Exception e) {
            Log.error("Unable to start the chat server. Exiting.");
            Log.error(e.toString());
            System.exit(0);
        }

        Log.system("Starting chat server at " + serverSocket.getLocalSocketAddress());

        ready = true;

        // Loop indefinitely, accepting socket connections
        int cycle = 0;
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Log.system("Chat login received at: " + socket.getInetAddress());
                ChatUser user = new ChatUser(server, socket);
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }

    public boolean isReady() {
        return ready;
    }
}
