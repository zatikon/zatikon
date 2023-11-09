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

import java.io.*;


public class Settings {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String server = "zatikon.chroniclogic.com";
    private String username = "";
    private String musicstate = "";
    private String soundstate = "";

    private static final String SETTINGS_PATH = Constants.LOCAL_DIR + "/settings";

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Settings() {
        try {
            if (Client.isWeb()) return;

            File file = new File(SETTINGS_PATH);
            if (file.exists()) {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);

                server= dis.readUTF();
                username = dis.readUTF();
                musicstate = dis.readUTF();
                soundstate = dis.readUTF();
                fis.close();
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
        return musicstate.equals("ON");
    }

    public void setMusicState(boolean on) {
        if (on) musicstate = "ON";
        else musicstate = "FF";
    }

    public boolean getSoundState() {
        return soundstate.equals("ON");
    }

    public void setSoundState(boolean on) {
        if (on) soundstate = "ON";
        else soundstate = "FF";
    }


    /////////////////////////////////////////////////////////////////
    // Save
    /////////////////////////////////////////////////////////////////
    public void save() {
        if (Client.isWeb()) return;
        try {
            FileOutputStream fos = new FileOutputStream(SETTINGS_PATH);
            DataOutputStream dos = new DataOutputStream(fos);
            dos.writeUTF(server);
            dos.writeUTF(username);
            dos.writeUTF(musicstate);
            dos.writeUTF(soundstate);
            dos.close();
            fos.close();

        } catch (Exception e) {
            Logger.error("Settings.save(): " + e);
        }
    }
}
