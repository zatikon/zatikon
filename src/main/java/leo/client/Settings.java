///////////////////////////////////////////////////////////////////////
//	Name:	Settings
//	Desc:	Client settings
//	Date:	10/6/2008 - Gabe Jones
//	TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import org.tinylog.Logger;
import com.electronwill.nightconfig.core.file.FileConfig;

import java.io.*;


public class Settings {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String server = "zatikon.chroniclogic.com";
    private String username = "";
    private boolean musicstate = false;
    private boolean soundstate = false;

    private static final String SETTINGS_PATH = Constants.LOCAL_DIR + "/settings.toml";

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Settings() {
        load(SETTINGS_PATH);
    }
    public Settings(String path) {
        load(path);
    }

    /////////////////////////////////////////////////////////////////
    // Save
    /////////////////////////////////////////////////////////////////
    public void save() {
        if (Client.isWeb()) return;
        try {
            FileConfig config = FileConfig.of(SETTINGS_PATH);
            config.load(); // This isn't really required, this maintains the keys of the current config, even if they don't exist.
            config.set("server", server);
            config.set("username", username);
            config.set("musicstate", musicstate);
            config.set("soundstate", soundstate);
            config.save();
            config.close();
        } catch (Exception e) {
            Logger.error("Settings.save(): " + e);
        }
    }

    public void load(String path) {
        try {
            if (Client.isWeb()) return;

            FileConfig config = FileConfig.of(path);
            config.load();
            if (!config.isEmpty()) {
                server = config.getOrElse("server", server);
                username = config.getOrElse("username", username);
                musicstate = config.getOrElse("musicstate", musicstate);
                soundstate = config.getOrElse("soundstate", soundstate);
                config.close();
            } else {
                Logger.info("No settings file");
            }
        } catch (Exception e) {
            Logger.error("Settings(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Gets/Sets
    /////////////////////////////////////////////////////////////////
    public String getUsername() {
        return username;
    }

    public void setUsername(String newName) {
        username = newName;
    }

    public String getServer() {
        return server;
    }

    public void setServer(String newServer) {
        server = newServer;
    }

    public boolean getMusicState() {
        return musicstate;
    }

    public void setMusicState(boolean state) {
        musicstate = state;
    }

    public boolean getSoundState() {
        return soundstate;
    }

    public void setSoundState(boolean state) {
        soundstate = state;
    }
}
