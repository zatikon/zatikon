///////////////////////////////////////////////////////////////////////
//	Name:	Patcher
//	Desc:	Patch away
//	Date:	6/1/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.util.patcher;


import org.tinylog.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.JarFile;

public class Patcher extends Frame {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String USER_DIR = System.getProperty("user.home") + "/zatikon";
    private static final String XML_URL = "http://zatikon.com/versions.xml";
    private static final String JAR = "JAR";
    private static final String FILE = "FILE_NAME";
    private static final String VERSION = "VERSION";
    private static final String URL = "URL";
    private static final String TARGET = "SAVE_TARGET";
    private static final int X = 225;
    private static final int Y = 75;
    private static final String CLIENT = USER_DIR + "/zatikon.jar";
    private static final String MAIN_CLASS = "leo.client.Client";
    private static final String MAIN_METHOD = "main";
    private static final long serialVersionUID = 1L;


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static String message = "";
    private static Frame frame = null;


    /////////////////////////////////////////////////////////////////
    // Draw the box
    /////////////////////////////////////////////////////////////////
    public void paint(Graphics g) {
        g.setColor(Color.orange.brighter());
        g.fillRect(0, 0, X, Y);
        //g.setColor(Color.orange);
        //g.drawRect(2,2,X-5,Y-5);
        g.setColor(Color.orange);
        g.drawRect(1, 1, X - 3, Y - 3);
        g.setColor(Color.black);
        g.drawRect(0, 0, X - 1, Y - 1);
        g.drawString(Patcher.getMessage(), 5, (Y / 2) + 5);
        g.drawString("Zatikon auto-updater", 5, 15);
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Patcher() {
        super();

        Dimension dim = getToolkit().getScreenSize();
        setLocation(
                (int) dim.getWidth() / 2 - (X / 2),
                (int) dim.getHeight() / 2 - (Y / 2));

        setSize(X, Y);
        setUndecorated(true);
    }


    /////////////////////////////////////////////////////////////////
    // Appearance related methods
    /////////////////////////////////////////////////////////////////
    public static void setMessage(String newMessage) {
        message = newMessage;
        frame.repaint();
    }

    public static String getMessage() {
        return message;
    }

    public static Frame getFrame() {
        return frame;
    }

    public static void setFrame(Frame newFrame) {
        frame = newFrame;
    }


    /////////////////////////////////////////////////////////////////
    // The main function.  Here it begins
    /////////////////////////////////////////////////////////////////
    public static void main(String[] args) {

        // Check for the zatikon directory
        try {
            File file = new File(USER_DIR);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Logger.error(e);
        }

        Patcher patcher = new Patcher();
        Patcher.setFrame(patcher);
        patcher.setVisible(true);
        try {
            Patcher.setMessage("Checking for updates...");

            DocumentBuilder docb = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = docb.parse(XML_URL);

            NodeList nl = doc.getElementsByTagName(JAR);
            for (int i = 0; i < nl.getLength(); i++) {
                Node node = nl.item(i);
                NamedNodeMap att = node.getAttributes();

                String jarFile = att.getNamedItem(FILE).getNodeValue();
                String remVersion = att.getNamedItem(VERSION).getNodeValue();
                String fromURL = att.getNamedItem(URL).getNodeValue();
                String target = att.getNamedItem(TARGET).getNodeValue();
                String locVersion = getVersion(jarFile);
                float remoteVersion = Float.parseFloat(remVersion);
                float localVersion = Float.parseFloat(locVersion);

                if (remoteVersion > localVersion) download(fromURL, target);
            }

        } catch (Exception e) {
            Logger.error(e);
        } finally {
            patcher.dispose();
        }
        try {
            patcher.run();
        } catch (Exception e) {
            Logger.error(e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get a manifest version from a file
    /////////////////////////////////////////////////////////////////
    private static String getVersion(String name) {
        try {
            JarFile jar = new JarFile(USER_DIR + "/" + name);
            Attributes att = jar.getManifest().getMainAttributes();
            String value = att.getValue("Manifest-Version");
            jar.close();

            return value;
        } catch (Exception e) {
            Logger.error(e);
            return "0";
        }
    }


    /////////////////////////////////////////////////////////////////
    // Download a file
    /////////////////////////////////////////////////////////////////
    private static void download(String fromURL, String toFile) {
        try {
            File file = new File(USER_DIR + "/" + toFile);
            if (file.exists()) file.delete();

            URL url = new URL(fromURL);
            URLConnection con = url.openConnection();

            // How big
            float contentSize = con.getContentLength();

            // Open the streams
            InputStream is = con.getInputStream();
            FileOutputStream os = new FileOutputStream(USER_DIR + "/" + toFile);

            // Get the data
            int b = -1;
            float count = 0;
            int oldPerc = 0;
            while ((b = is.read()) != -1) {
                os.write(b);
                count++;

                int perc = 0;
                if (contentSize > 0) {
                    float quo = contentSize / count;
                    if (quo > 0) perc = (int) (100 / quo);
                }
                //if (perc > oldPerc)
                if (perc % 2 == 0 && perc != oldPerc) {
                    Patcher.setMessage("Downloading " + toFile + ": " + perc + "%");
                    oldPerc = perc;
                }
            }

            // Close the streams
            is.close();

        } catch (Exception e) {
            Logger.error(e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Run the jar
    /////////////////////////////////////////////////////////////////
    private void run() throws Exception {
        try {
            // Create a url linking to jar
            URL[] urls = new URL[1];
            //urls[0] = getClass().getClassLoader().getResource(CLIENT);
            urls[0] = new URL("file:" + CLIENT);

            // Load the jar
            URLClassLoader loader = new URLClassLoader(urls);

            // Load the main class out of the jar
            Class cl = loader.loadClass(MAIN_CLASS);
            Object obj = cl.newInstance();

            // Run the main method
            String[] arg = new String[0];
            Method method = cl.getMethod(MAIN_METHOD, arg.getClass());
            method.invoke(obj, new Object[]{arg});

        } catch (Exception e) {
            Logger.error(e);
            throw e;
        }
    }

}
