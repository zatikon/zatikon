///////////////////////////////////////////////////////////////////////
//	Name:	LoginServer
//	Desc:	The login server
//	Date:	5/8/2003 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Constants;
import leo.shared.Log;
import leo.shared.network.SocketProvider;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;


public class LoginServer implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final int port;
    private final Thread runner;
    private final boolean useTls;
    private ServerSocket serverSocket;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public LoginServer(int newPort, boolean useTls) {
        this.port = newPort;
        this.useTls = useTls;
        runner = new Thread(this);
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
            Log.error("Unable to start the login server. Exiting.");
            Log.error(e.toString());
            System.exit(0);
        }

        Log.system("Starting login server at " + serverSocket.getLocalSocketAddress());
        System.err.println(Constants.SERVER_READY_MESSAGE);

        var pid = ProcessHandle.current().pid();
        var pidFile = new File(Constants.READY_PID_FILE);
        var succeeded = new File(pidFile.getParent()).mkdirs();

        try {
            var fileSucceeded = pidFile.createNewFile();

            var pidWriter = new BufferedWriter(new FileWriter(pidFile));
            pidWriter.write(String.format("%d\n", pid));
            pidWriter.flush();
            pidWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Loop indefinately, accepting socket connections
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                Log.system("Connection received at: " + socket.getInetAddress());
                User user = new User(socket);
                Thread.sleep(10);
            } catch (Exception e) {
            }
        }
    }
}
