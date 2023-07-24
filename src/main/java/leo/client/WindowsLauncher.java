package leo.client;

import leo.server.Server;
import leo.shared.Constants;

import java.util.Arrays;

public class WindowsLauncher {
    /*
    This exists, because the code for starting the local standalone server tries to get the
    binary from the information about the client process; normally that's some form of `java`,
    but not when the game is started from .exe
    */

    public static void main(String[] args) {
        if (Arrays.asList(args).contains(Constants.STANDALONE_ARG)) {
            Server.main(args);
        } else {
            Client.main(args);
        }
    }
}
