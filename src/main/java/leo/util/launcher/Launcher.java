///////////////////////////////////////////////////////////////////////
//	Name:	Launcher
//	Desc:	Check for a new patcher, or launch the old one
//	Date:	5/23/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.util.launcher;

import org.tinylog.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.lang.reflect.InvocationTargetException;


public class Launcher {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final String USER_FOLDER = System.getProperty("user.home") + "/zatikon";
    private static final String PATCHER = USER_FOLDER + "/patcher.jar";
    private static final String PATCHER_PROTO = "proto.jar";
    private static final String PATCHER_NEW = USER_FOLDER + "/newpatcher.jar";
    private static final String MAIN_CLASS = "leo.util.patcher.Patcher";
    private static final String MAIN_METHOD = "main";


    /////////////////////////////////////////////////////////////////
    // Main module
    /////////////////////////////////////////////////////////////////
    public static void main(String[] args) {
        Launcher launcher = new Launcher();

        try {
            Logger.info("Check install");

            launcher.checkInstall();

            Logger.info("Check new");

            launcher.checkNew();

            Logger.info("Run");

            launcher.run();
        } catch (Exception e) {
            Logger.error(e);
            System.exit(0);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get a new version of the jar
    /////////////////////////////////////////////////////////////////
    public void checkInstall() throws Exception {
        try {
            File file = new File(USER_FOLDER);
            if (!file.exists()) {
                file.mkdir();
                copy(PATCHER_PROTO, PATCHER);
            }
        } catch (Exception e) {
            throw e;
        }

		/*try
		{	File file = new File(PATCHER);
			if (!file.exists())
			{	copy(PATCHER_PROTO, PATCHER);
			}
		} catch (Exception e)
		{	throw e;
		}*/
    }


    /////////////////////////////////////////////////////////////////
    // Get a new version of the jar
    /////////////////////////////////////////////////////////////////
    private void checkNew() throws Exception {
        try {
            File file = new File(PATCHER_NEW);
            if (file.exists()) {
                File oldFile = new File(PATCHER);
                if (oldFile.exists()) {
                    while (!oldFile.canWrite())
                        Thread.sleep(100);
                    oldFile.delete();
                }
                file.renameTo(new File(PATCHER));

                // place to store this code for later patcher use
                //System.out.println(Runtime.getRuntime().exec("java -jar launcher.jar"));
                //System.exit(0);
            }


        } catch (Exception e) {
            throw e;
        }
    }

    /////////////////////////////////////////////////////////////////
    // Copy a file
    /////////////////////////////////////////////////////////////////
    private void copy(String source, String target) throws Exception {
        try {
            FileChannel ic = new FileInputStream(source).getChannel();
            FileChannel oc = new FileOutputStream(target).getChannel();
            ic.transferTo(0, ic.size(), oc);
            ic.close();
            oc.close();
        } catch (Exception e) {
            throw e;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Run the jar
    /////////////////////////////////////////////////////////////////
    private void run() throws Exception {
        try {
            // Create a url linking to jar
            URL[] urls = new URL[1];
            //urls[0] = getClass().getClassLoader().getResource(PATCHER);
            //urls[0] = ClassLoader.getSystemClassLoader().getResource(PATCHER);
            //urls[0] = getClass().getResource(PATCHER);
            urls[0] = new URL("file:" + PATCHER);

            Logger.debug(urls[0]);

            // Load the jar
            URLClassLoader loader = new URLClassLoader(urls);

            // Load the main class out of the jar
            // Class cl = loader.loadClass(MAIN_CLASS);
            // Object obj = cl.newInstance();
            Class<?> cl = loader.loadClass(MAIN_CLASS);
            Object obj = cl.getDeclaredConstructor().newInstance();

            // Run the main method
            String[] arg = new String[0];
            Method method = cl.getMethod(MAIN_METHOD, arg.getClass());
            method.invoke(obj, new Object[]{arg});

        } catch (Exception e) {
            throw e;
        }
    }
}
