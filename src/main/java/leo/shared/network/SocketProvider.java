package leo.shared.network;

import leo.shared.Log;

import javax.net.ServerSocketFactory;
import javax.net.SocketFactory;
import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.util.Optional;

public class SocketProvider {

    private static String getKeystorePassword() throws Exception {
        var keystorePasswordOpt = Optional.ofNullable(System.getProperty("keystore.password"));
        var keystorePasswordFileOpt = Optional.ofNullable(System.getProperty("keystore.passwordFile"));

        if (keystorePasswordFileOpt.isEmpty() && keystorePasswordOpt.isEmpty()) {
            Log.error("Either keystore.password or keystore.passwordFile property is required!");
            throw new IllegalArgumentException("Missing keystore password");
        }

        if (keystorePasswordFileOpt.isPresent()) {
            return new BufferedReader(new InputStreamReader(new FileInputStream(
                    keystorePasswordFileOpt.get()
            ))).readLine().strip();
        } else {
            return keystorePasswordOpt.get();
        }
    }
    private static ServerSocketFactory getServerSocketFactory() throws Exception {
        // by example https://docs.oracle.com/en/java/javase/11/security/sample-code-illustrating-secure-socket-connection-client-and-server.html
        // surely there are things that can be improved

        var keystorePathOpt = Optional.ofNullable(System.getProperty("keystore.path"));

        var sslContext = SSLContext.getInstance("TLS");
        var keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        var keyStore = KeyStore.getInstance("PKCS12");

        if (keystorePathOpt.isEmpty()) {
            Log.error("Missing keystore.path property!");
            throw new IllegalArgumentException("Couldn't locate keystore");
        }

        var keystorePassword = getKeystorePassword().toCharArray();
        var keystorePath = keystorePathOpt.get();

        Log.system("Loading keystore from " + keystorePath);
        keyStore.load(new FileInputStream(keystorePath), keystorePassword);


        keyManagerFactory.init(keyStore, keystorePassword);
        sslContext.init(keyManagerFactory.getKeyManagers(), null, null);

        return sslContext.getServerSocketFactory();

    }

    private static ServerSocket getSslServerSocket(int port) throws Exception {
        var ssf = getServerSocketFactory();
        return ssf.createServerSocket(port);
    }
    private static SocketFactory getSocketFactory() throws Exception {
        var truststorePathOpt = Optional.ofNullable(System.getProperty("truststore.path"));
        if (truststorePathOpt.isPresent()) {
            String truststorePath = truststorePathOpt.get();
            return getSocketFactoryForPath(truststorePath);
        } else {
            return (SSLSocketFactory) SSLSocketFactory.getDefault();
        }
    }

    private static SocketFactory getSocketFactoryForPath(String truststorePath) throws Exception {
        // TODO passwordless truststore should be possible, but not very important
        var passphrase = "123456".toCharArray();

        var sslContext = SSLContext.getInstance("TLS");
        var trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        var keyStore = KeyStore.getInstance("PKCS12");

        InputStream trustStoreStream;
        trustStoreStream = new FileInputStream(truststorePath);
        Log.system("Loading truststore from provided path: " + truststorePath);

        keyStore.load(trustStoreStream, passphrase);

        trustManagerFactory.init(keyStore);

        sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

        return sslContext.getSocketFactory();
    }

    private static SSLSocket getSslSocket(String server, int port) throws Exception {
        var factory = getSocketFactory();
        var socket = (SSLSocket) factory.createSocket(server, port);

        socket.startHandshake();

        return socket;
    }


    public static Socket newSocket(String server, int port, boolean useTls) throws Exception {
        if (useTls) {
            return getSslSocket(server, port);
        } else {
            return new Socket(server, port);
        }
    }

    public static ServerSocket newServerSocket(int port, boolean useTls) throws Exception {
        if (useTls) {
            return getSslServerSocket(port);
        } else {
            return new ServerSocket(port);
        }
    }

}
