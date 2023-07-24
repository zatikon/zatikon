///////////////////////////////////////////////////////////////////////
// Name: EndGamePanel
// Desc: The panel that heralds the end of game
// Date: 6/15/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Constants;

import java.awt.*;


public class EndGamePanel extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private String message = "";
    private boolean victory = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public EndGamePanel() {
        super(Constants.OFFSET,
                Constants.OFFSET,
                Constants.SCREEN_WIDTH - (Constants.OFFSET * 2),
                Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));
        int atX = getScreenX() + (getWidth() / 2) - 40;
        int atY = getScreenY() + (getHeight() / 2) + (Client.FONT_HEIGHT / 2) + 15 + Constants.OFFSET;
        OkButton okButton = new OkButton(atX, atY, 80, 30);
        add(okButton);
    }


    /////////////////////////////////////////////////////////////////
    // Set the message
    /////////////////////////////////////////////////////////////////
    public void setMessage(String newMessage) {
        message = newMessage;
    }

    /////////////////////////////////////////////////////////////////
    // Set the Victory
    /////////////////////////////////////////////////////////////////
    public void setVictory(boolean newVictory) {
        victory = newVictory;
    }

    /////////////////////////////////////////////////////////////////
    // Check gold
    /////////////////////////////////////////////////////////////////
    public void checkGold() {
        if (Client.lastGold() < Client.getGold() && Client.lastGold() != -1) {
            Client.getGameData().gainGold(Client.getGold() - Client.lastGold());
            Client.getImages().playSound(Constants.SOUND_GOLD);
        }
        Client.lastGold(Client.getGold());
    }

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        g.setFont(Client.getFontBig());
        String text = message;

        int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
        int atY = getScreenY() + (getHeight() / 2) + (20 / 2);

        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);
        g.setColor(Color.yellow);

        g.drawString(text, atX, atY);
        g.setFont(Client.getFont());

        super.draw(g, mainFrame);
   /*
  Image img;
  
  //checkGold();
  
  if (victory == false)
    img = Client.getImages().getImage(Constants.IMG_END_GAME_DEFEAT);
  else
    img = Client.getImages().getImage(Constants.IMG_END_GAME_VICTORY);
  
  g.drawImage(img, getScreenX(), getScreenY(), mainFrame);
  
  
  g.setFont(Client.getFontBig());
  String text = message;

  int atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(text)/2);
  int atY = getScreenY() + (getHeight()/2) + (20/2);

  g.setColor(Color.black);
  g.drawString(text, atX+1, atY);
  g.drawString(text, atX-1, atY);
  g.drawString(text, atX, atY+1);
  g.drawString(text, atX, atY-1);
  g.setColor(Color.yellow);

  g.drawString(text, atX, atY);
  
  text = "Gold: " + Client.lastGold() + "";
  atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(text)/2);
  atY = getScreenY() + (getHeight()/2) + (20/2) - 15;
  
  g.setColor(Color.black);
  g.drawString(text, atX+1, atY);
  g.drawString(text, atX-1, atY);
  g.drawString(text, atX, atY+1);
  g.drawString(text, atX, atY-1);
  g.setColor(Color.yellow);

  g.drawString(text, atX, atY);
  
  g.setFont(Client.getFont());

  super.draw(g, mainFrame);
  */
    }

}
