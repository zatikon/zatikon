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

import java.io.*;
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
    private ChatUser user = null;
    private boolean ready = false;
    private boolean running = true; // Control flag for the main loop

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
        while (running) {
            try {
                Socket socket = serverSocket.accept();
                Log.system("Chat login received at: " + socket.getInetAddress());
                user = new ChatUser(server, socket);
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // If interrupted (due to stop() being called), we exit the loop
                Thread.currentThread().interrupt(); // Restore interrupt status
                Log.system("ChatServer thread was interrupted and is stopping.");
            } catch (Exception e) {
                Log.error("ChatServer error: " + e);
            }
        }
    }

    public boolean isReady() {
        return ready;
    }

    /////////////////////////////////////////////////////////////////
    // Stop Method
    /////////////////////////////////////////////////////////////////
    public void stop() {
        Log.system("Stopping chat server...");
        running = false;
        try {
            if (user != null) user.close();
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Interrupts the accept() call
            }
        } catch (IOException e) {
            Log.error("Error closing server socket during stop: " + e.getMessage());
        }
        try {
            runner.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore interrupt status
        }
    }      
}
