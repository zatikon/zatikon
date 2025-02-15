package leo.server;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileNotFoundAction;
import leo.shared.Constants;
import org.tinylog.Logger;

import java.nio.file.Paths;


public class Config {
    private String logFile = Paths.get(Constants.LOCAL_DIR, "log", "zatikon-server.log").toString();
    private boolean logToTerminal = true;

    private static final String DEFAULT_SETTINGS_PATH = "./zatikon-server.toml";
    private String path = null;

    public Config() {
        this(DEFAULT_SETTINGS_PATH);
    }

    public Config(String path) {
        this.path = path;

        load();
    }

    public void save() {
        try {
            FileConfig config = FileConfig.of(path);

            config.load(); // To merge with existing keys
            config.set("log.file", logFile);
            config.set("log.terminal", logToTerminal);
            config.save();
            config.close();
        } catch (Exception e) {
            Logger.error("Config.save(): " + e);
        }
    }

    public void load() {
        try (FileConfig config = FileConfig.builder(path).onFileNotFound(FileNotFoundAction.READ_NOTHING).build()) {
            config.load();
            if (!config.isEmpty()) {
                logFile = config.getOrElse("log.file", logFile);
                logToTerminal = config.getOrElse("log.terminal", logToTerminal);
            } else {
                Logger.info("No config file");
            }
        } catch (Exception e) {
            Logger.error("Config.load(): " + e);
        }
    }

    public String getLogFile() {
        return logFile;
    }

    public boolean isLogToTerminal() {
        return logToTerminal;
    }
}
