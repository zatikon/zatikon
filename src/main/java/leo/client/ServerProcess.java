package leo.client;

import leo.server.Server;
import leo.shared.Constants;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

public class ServerProcess {
    public final Process process;
    private static ServerProcess instance = null;

    static class ServerNotReadyException extends RuntimeException {

    }

    private ServerProcess() {
        var cmd = ProcessHandle.current().info().command().orElse("java");

        // pass on the PID to the server
//            ProcessHandle.current().pid();

        String classpath = System.getProperty("java.class.path");

        ProcessBuilder processBuilder =
            new ProcessBuilder(
                    cmd,
                    "-cp",
                    classpath,
                    Server.class.getName(),
                    Constants.STANDALONE_ARG
            )
            .inheritIO();

        try {
            process = processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Runtime.getRuntime().addShutdownHook(new Thread(
                process::destroy
        ));

        System.err.println("Server process started");
    }

    public static ServerProcess getInstance() {
        if (instance == null) {
            instance = new ServerProcess();
        }

        return instance;
    }

    public void waitUntilReady(int timeoutMillis) {
        var tsStart = System.currentTimeMillis();
        var pidFile = new File(Constants.READY_PID_FILE);

        try {
            while (tsStart + timeoutMillis > System.currentTimeMillis() && !pidFile.exists()) {
                Thread.yield();
            }

            if (tsStart + timeoutMillis <= System.currentTimeMillis())
                throw new ServerNotReadyException();

            var pidFileReader = new BufferedReader(new FileReader(pidFile));

            while (tsStart + timeoutMillis > System.currentTimeMillis()) {
                if (pidFileReader.ready()) {
                    var line = pidFileReader.readLine();
                    if (Long.parseLong(line.strip()) == process.pid())
                        return;
                } else {
                    pidFileReader.close();
                    pidFileReader = new BufferedReader(new FileReader(pidFile));
                }
                Thread.yield();
            }

            throw new ServerNotReadyException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
