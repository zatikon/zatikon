///////////////////////////////////////////////////////////////////////
//	Name:	Log
//	Desc:	Log file
//	Date:	2/15/2009 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports

import org.tinylog.Logger;
import org.tinylog.configuration.Configuration;

import java.nio.file.Paths;

public class Log {
    /////////////////////////////////////////////////////////////////
    // Log an error
    /////////////////////////////////////////////////////////////////
    public static void error(String text) {
        Logger.error(text);
    }


    /////////////////////////////////////////////////////////////////
    // Log activity
    /////////////////////////////////////////////////////////////////
    public static void activity(String text) {
        Logger.debug("ACTIVITY: " + text);
    }


    /////////////////////////////////////////////////////////////////
    // Log game actions
    /////////////////////////////////////////////////////////////////
    public static void game(String text) {
        Logger.info("GAME: " + text);
    }


    /////////////////////////////////////////////////////////////////
    // Log game actions
    /////////////////////////////////////////////////////////////////
    public static void alert(String text) {
        Logger.warn(text);
    }


    /////////////////////////////////////////////////////////////////
    // Chat
    /////////////////////////////////////////////////////////////////
    public static void chat(String text) {
        Logger.info("CHAT: " + text);
    }


    /////////////////////////////////////////////////////////////////
    // System
    /////////////////////////////////////////////////////////////////
    public static void system(String text) {
        Logger.info("SYSTEM: " + text);
    }


    public static void setup(boolean logToTerminal, String logFile) {
        if (Configuration.isFrozen()) {
            Logger.warn("Logger is already initialized. Skipping setup.");

            return;
        }

        if (logToTerminal) {
            Configuration.set("writerConsole", "console");
            Configuration.set("writerConsole.level", "info");
        }

        var parent = Paths.get(logFile).getParent();
        if (parent != null) {
            var succeeded = parent.toFile().mkdirs();
        }

        Configuration.set("writerFile", "file");
        Configuration.set("writerFile.file", logFile);
        Configuration.set("writerFile.append", "true");
    }
}
