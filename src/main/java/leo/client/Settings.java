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

import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.io.*;
import java.util.Base64;


public class Settings {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String server = "zatikon.chroniclogic.com";
    private String username = "";
    private boolean musicstate = false;
    private boolean soundstate = false;
    private String password = "";
    private boolean teamIconsState = false;
    private Short musicvolume = 5;
    private Short soundvolume = 5;

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
            config.set("teamIconsState", teamIconsState);
            config.set("musicvolume", musicvolume);
            config.set("soundvolume", soundvolume);
            // Write obfuscated password
            byte[] obfuscatedPassword = xorObfuscate(password.getBytes());
            String base64Password = Base64.getEncoder().encodeToString(obfuscatedPassword);
            config.set("password", base64Password);

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
                teamIconsState = config.getOrElse("teamIconsState", teamIconsState);
                musicvolume = config.getShortOrElse("musicvolume", musicvolume);
                soundvolume = config.getShortOrElse("soundvolume", soundvolume);

                // Read obfuscated value
                String passwordBase64 = config.getOrElse("password", password);
                byte[] passwordBytes = Base64.getDecoder().decode(passwordBase64);
                password = new String(xorObfuscate(passwordBytes), StandardCharsets.UTF_8);
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
    
    public Short getMusicVolume() {
        return musicvolume;
    }

    public void setMusicVolume(Short state) {
        musicvolume = state;
    }

    public boolean getSoundState() {
        return soundstate;
    }

    public void setSoundState(boolean state) {
        soundstate = state;
    }

    public Short getSoundVolume() {
        return soundvolume;
    }
    
    public void setSoundVolume(Short state) {
        soundvolume = state;
    }      

    public boolean getTeamIconsState() {
        return teamIconsState;
    }

    public void setTeamIconsState(boolean state) {
        teamIconsState = state;
    }

    public String getUserPassword() {
        return password;
    }

    public void setUserPassword(String newPassword) {
        password = newPassword;
    }

    private static byte[] xorObfuscate(byte[] bytes) {
        byte[] key = HexFormat.of().parseHex("752a386c4247c7a583676bb57d6fbf1d84675ef0af968878c48a318d3850c1e7026c0fc0ec8057754aee949ae023097cb3a9");
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] ^= key[i];
        }
        return bytes;
    }
}
