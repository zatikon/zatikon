///////////////////////////////////////////////////////////////////////
// Name: TeamIconsButton
// Desc: Toggle team icons button
// Date: 12/26/2024 - Josiah
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;
import java.awt.image.ColorConvertOp;
import java.awt.image.BufferedImage;
import java.awt.color.ColorSpace;
import java.awt.image.RescaleOp;
import java.awt.*;


public class SettingsButton extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////   
    private BufferedImage highlightedImg = null;
    private BufferedImage disabledImg = null;
    private BufferedImage disabledHighlightedImg = null;
    private int img;
    private String label;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public SettingsButton(int x, int y, int width, int height, int buttonImg, String name) {
        super(x, y, width, height);
        img = buttonImg;
        label = name;
        createHighlightedImage();
        createGrayscaleImage();
        createHighlightedGrayscaleImage();
    }


    /////////////////////////////////////////////////////////////////
    // Temporary code
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if(label == "teamIcon") {
            Client.toggleShowTeamIcons();
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } else if (label == "soundButton") {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.mute(!Client.mute());
            if(!Client.mute())
                Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;            
        } else if (label == "soundPlus" && Client.getSoundVolume() < 5) {
            Client.setSoundVolume((short) (Client.getSoundVolume() + 1));
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } else if (label == "soundMinus" && Client.getSoundVolume() > 1) {
            Client.setSoundVolume((short) (Client.getSoundVolume() - 1));
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } else if (label == "musicButton") {
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            Client.musicOff(!Client.musicOff());
            if (Client.musicOff()) {
                Client.getImages().pauseMusic();
            } else {
                Client.getImages().resumeMusic();
            }            
            return true;
        } else if (label == "musicPlus" && Client.getMusicVolume() < 5) {
            Client.setMusicVolume((short) (Client.getMusicVolume() + 1));
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } else if (label == "musicMinus" && Client.getMusicVolume() > 1) {
            Client.setMusicVolume((short) (Client.getMusicVolume() - 1));
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        }            
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        if(label == "teamIcon") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(Client.getShowTeamIcons()) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        } else if (label == "soundButton") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
                if(!Client.mute())
                    g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
                else
                    g.drawImage(disabledHighlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(!Client.mute()) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }            
        } else if (label == "soundPlus") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && Client.getSoundVolume() < 5) {
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(Client.getSoundVolume() < 5) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        } else if (label == "soundMinus") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && Client.getSoundVolume() > 1) {
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(Client.getSoundVolume() > 1) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        } else if (label == "musicButton") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY())) {
                if(!Client.musicOff())
                    g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
                else
                    g.drawImage(disabledHighlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);                    
            } else if(!Client.musicOff()) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }                             
        } else if (label == "musicPlus") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && Client.getMusicVolume() < 5) {
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(Client.getMusicVolume() < 5) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }
        } else if (label == "musicMinus") {
            if (isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && Client.getMusicVolume() > 1) {
                g.drawImage(highlightedImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else if(Client.getMusicVolume() > 1) {
                g.drawImage(Client.getImages().getImage(img), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            } else {
                g.drawImage(disabledImg, getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            }                
        }
    }

    public void createGrayscaleImage() {
        if(disabledImg != null) {
            return;
        }
        //create darkened and greyscale version of image
        disabledImg = new BufferedImage(Client.getImages().getImage(img).getWidth(null), Client.getImages().getImage(img).getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D gImg = disabledImg.createGraphics();
        gImg.drawImage(Client.getImages().getImage(img), 0, 0, null);
        gImg.dispose();
        /*
        // Create a grayscale color conversion operation
        ColorConvertOp grayscaleOp = new ColorConvertOp(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), 
            null
        );

        // Apply the grayscale conversion to the image
        grayscaleOp.filter(disabledImg, disabledImg); */

        // Apply darkening
        RescaleOp darkenOp = new RescaleOp(0.5f, 0, null); // Scale by 0.5 to darken
        darkenOp.filter(disabledImg, disabledImg);
    }

    public void createHighlightedImage() {
        if(highlightedImg != null) {
            return;
        }
        //create highlighted version of image
        highlightedImg = new BufferedImage(Client.getImages().getImage(img).getWidth(null), Client.getImages().getImage(img).getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D gImg = highlightedImg.createGraphics();
        gImg.drawImage(Client.getImages().getImage(img), 0, 0, null);
        gImg.dispose();

        // Apply darkening
        RescaleOp darkenOp = new RescaleOp(1.5f, 0, null); // Scale by 1.5 to brighten
        darkenOp.filter(highlightedImg, highlightedImg);
    }

    public void createHighlightedGrayscaleImage() {
        if(disabledHighlightedImg != null) {
            return;
        }
        //create darkened and greyscale version of image
        disabledHighlightedImg = new BufferedImage(Client.getImages().getImage(img).getWidth(null), Client.getImages().getImage(img).getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D gImg = disabledHighlightedImg.createGraphics();
        gImg.drawImage(Client.getImages().getImage(img), 0, 0, null);
        gImg.dispose();
        
        /*
        // Create a grayscale color conversion operation
        ColorConvertOp grayscaleOp = new ColorConvertOp(
            ColorSpace.getInstance(ColorSpace.CS_GRAY), 
            null
        );

        // Apply the grayscale conversion to the image
        grayscaleOp.filter(disabledHighlightedImg, disabledHighlightedImg);
        */
        
        // Apply darkening
        RescaleOp darkenOp = new RescaleOp(0.75f, 0, null); // Scale by 0.5 to darken
        darkenOp.filter(disabledHighlightedImg, disabledHighlightedImg);
    }  
}
