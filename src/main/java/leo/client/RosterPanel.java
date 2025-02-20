///////////////////////////////////////////////////////////////////////
// Name: RosterPanel
// Desc: The panel that manages armies
// Date: 6/15/2003 - Gabe Jones
//   10/15/2010 - David Schwartz   
//   10/15/2010 - Added MirroredRandom Button to menu
//   12/2/2010 - Offset buttons so that they fit on screen nicely
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;


public class RosterPanel extends LeoContainer {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final int MARGIN = 6;
    private static final Color GOLD = new Color(255, 255, 175);
    private final RosterText rosterText;
    private boolean inside = false;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private boolean first = true;
    private boolean noob = false;
    private String msgText = "";


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RosterPanel(boolean isNoob) {
        super(0,
                0,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT);

        noob = isNoob;

        int xPos = 699;
        int yPos = 37;
        int ySpacing = 50;
        int buttonWidth = 91;
        int buttonHeight = 17;

        BlogButton bb = new BlogButton(xPos, yPos, buttonWidth, buttonHeight);
        add(bb);
        GuideButton gb = new GuideButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
        add(gb);
        ForumButton fb = new ForumButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
        add(fb);
        ExitButton eb = new ExitButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
        add(eb);
        yPos = 276;
        if(!noob) {
            ScoresButton sb = new ScoresButton(xPos, yPos, buttonWidth, buttonHeight);
            add(sb);
            AccountButton ab = new AccountButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
            add(ab);
            ReferButton rb = new ReferButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
            add(rb);
            CreditsButton cb = new CreditsButton(xPos, yPos += ySpacing, buttonWidth, buttonHeight);
            add(cb);
        }
        yPos = 495;
        ySpacing = 36;

        SettingsButton tb = new SettingsButton(xPos, yPos, buttonWidth, buttonHeight, Constants.IMG_TEAM_ICONS_BUTTON, "teamIcon");
        add(tb);

        int smallButtonWidth = 17;
        SettingsButton mb = new SettingsButton(xPos + smallButtonWidth + 1, yPos += ySpacing, buttonWidth - smallButtonWidth - smallButtonWidth - 2, buttonHeight, Constants.IMG_MUTE, "soundButton");
        add(mb);
        SettingsButton mib = new SettingsButton(xPos, yPos, smallButtonWidth, buttonHeight, Constants.IMG_MINUS_BUTTON, "soundMinus");
        add(mib);
        SettingsButton psb = new SettingsButton(xPos + buttonWidth - smallButtonWidth, yPos, smallButtonWidth, buttonHeight, Constants.IMG_PLUS_BUTTON, "soundPlus");
        add(psb);

        SettingsButton msb = new SettingsButton(xPos + smallButtonWidth + 1, yPos += ySpacing, buttonWidth - smallButtonWidth - smallButtonWidth - 2, buttonHeight, Constants.IMG_MUSIC, "musicButton");
        add(msb);
        SettingsButton mmb = new SettingsButton(xPos, yPos, smallButtonWidth, buttonHeight, Constants.IMG_MINUS_BUTTON, "musicMinus");
        add(mmb);
        SettingsButton pmb = new SettingsButton(xPos + buttonWidth - smallButtonWidth, yPos, smallButtonWidth, buttonHeight, Constants.IMG_PLUS_BUTTON, "musicPlus");
        add(pmb);

        // Create the buttons
        int buttonX = 5; //(Constants.SCREEN_WIDTH / 7) - 1;
        //int buttonHeight = 154;
        //int buttonY = Constants.SCREEN_HEIGHT - buttonHeight;

        // Play
        LaunchGameButton computerButton = new LaunchGameButton(
                this, buttonX, 69, 38, 38, Constants.IMG_SINGLE_RED, isNoob ? "Start Here!" : "Single player",
                "Play a single player game against the Artificial Opponent using your presently configured army. Each game you win, the difficulty increases. Every two you lose, it decreases.", false, "");
        add(computerButton);

        // Coop
        LaunchGameButton coopButton = new LaunchGameButton(
                this, buttonX, 111, 38, 38, Constants.IMG_COOPERATIVE_RED, "Cooperative",
                "Enter a queue to play in a cooperative game with another player against the Artificial Opponent.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(coopButton);

        // Play 2v2
        LaunchGameButton teamButton = new LaunchGameButton(
                this, buttonX, 153, 38, 38, Constants.IMG_TEAM, "2 vs 2",
                "Enter a queue to play a 2 vs 2 team game using your presently configured armies. At least four players must be in queue for the match to begin.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(teamButton);

        // play Constructed
        LaunchGameButton playButton = new LaunchGameButton(
                this, buttonX, 195, 38, 38, Constants.IMG_CONSTRUCTED_RED, "Constructed",
                "Enter a queue to play another player in a one-on-one match using your presently configured armies.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(playButton);

        // Duel with Random Armies
        LaunchGameButton duelButton = new LaunchGameButton(
                this, buttonX, 237, 38, 38, Constants.IMG_RANDOM_RED, "Random",
                "Enter a queue to play another player in a one-on-one match using randomly generated armies.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(duelButton);

        // Duel with Mirrored Random Armies
        LaunchGameButton mirroredButton = new LaunchGameButton(
                this, buttonX, 279, 38, 38, Constants.IMG_RANDOM_RED, "Mirrored Random",
                "Enter a queue to play another player in a one-on-one match using the same randomly generated armies.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(mirroredButton);

        // edit army
        LaunchGameButton editButton = new LaunchGameButton(
                this, buttonX, 321, 38, 38, Constants.IMG_EDIT_RED, "Edit Army",
                "Enter the army editor, where you can configure your army and buy and sell units.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(editButton);

        // Army Archive
        LaunchGameButton archiveButton = new LaunchGameButton(
                this, buttonX, 363, 38, 38, Constants.IMG_ARCHIVE_RED, "Army Archive",
                "Save and load army configurations.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(archiveButton);

        // Buy Unit
        LaunchGameButton buyButton = new LaunchGameButton(
                this, buttonX, 405, 38, 38, Constants.IMG_BUY_RED, "Buy New Unit",
                "Buy a randomly selected, new unit for your army for 100 gold.", Client.standalone || noob, Client.standalone ? "ONLINE ONLY." : "COMPLETE THE TUTORIAL FIRST!");
        add(buyButton);

        rosterText = new RosterText(this, "Wins: " + Client.getWins() + " Losses: " + Client.getLosses(), 4, 455, 186, 142);
    }

    /////////////////////////////////////////////////////////////////
    // Get the display string
    /////////////////////////////////////////////////////////////////
    public String getMessage() {
        if (noob) return "Welcome to Zatikon!";

        //if (Client.demo() || Client.getRating() < 1)
        if (Client.getRating() < 1)
            return "Gold: " + Client.getGold();
        else
            return ("Gold: " + Client.getGold() + "   Rating:" + Client.getRating() + ", #" + Client.getRank());
    }


    /////////////////////////////////////////////////////////////////
    // Get the display string
    /////////////////////////////////////////////////////////////////
    public String getCount() {
        return "Players online: " + Client.getPlayerCount();
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

    public void setMsgText(String newText) {
        msgText = newText;
    }
    /*
    public boolean clickAt(int x, int y) {
        Logger.info("click");
        if (Client.getGameData().newRecruit()) {
            Client.getGameData().clearNewRecruit();
        }
        if (super.clickAt(x, y)) {
            return true;
        }
        return false;
    }*/
    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        checkGold();

        if (first) {
            if (Client.needEmail()) {
                MissingEmail mie = new MissingEmail(Client.getFrame());
            }
            first = false;
            Client.getNetManager().sendByte(Action.NO_REFERRAL);
        }

        g.drawImage(Client.getImages().getImage(Constants.IMG_MENU), getScreenX(), getScreenY(), 800, 600, mainFrame);

        g.setFont(Client.getFontBold());
        drawText(g, Client.getName(), getScreenX() + 70, getScreenY() + 30, Color.yellow);

        g.setFont(Client.getFont());
        drawText(g, "Gold: " + Client.getGold(), getScreenX() + 70, getScreenY() + 45, GOLD);

        //g.setFont(Client.getFont());
        drawText(g, "Rating: " + Client.getRating() + (Client.getRank() != 0 ? ", #" + Client.getRank() : ""), getScreenX() + 70, getScreenY() + 58, GOLD);    

        //Client.getWins()
        //g.setFont(Client.getFontBig());
        //String text = getMessage();

        //int atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(text)/2);
        //int atY = getScreenY() + Constants.OFFSET + 20;

        //g.setColor(Color.black);
        //g.drawString(text, atX+1, atY);
        //g.drawString(text, atX-1, atY);
        //g.drawString(text, atX, atY+1);
        //g.drawString(text, atX, atY-1);
        //g.setColor(Color.white);

        //g.drawString(text, atX, atY);
        g.setFont(Client.getFont());


        if (Client.getGameData().gainGold() > 0) {
            g.setFont(Client.getFontBig());
            String text = "You have gained " +
                    Client.getGameData().gainGold() + " gold";

            int atX = getScreenX() + (getWidth() / 2) - (g.getFontMetrics().stringWidth(text) / 2);
            int atY = getScreenY() + getHeight() - 5;
            //int atY = Client.getGameData().getHiddenUnitStats().getScreenY();

            g.setColor(Color.black);
            g.drawString(text, atX + 1, atY);
            g.drawString(text, atX - 1, atY);
            g.drawString(text, atX, atY + 1);
            g.drawString(text, atX, atY - 1);
            g.setColor(Color.yellow);

            g.drawString(text, atX, atY);
            g.setFont(Client.getFont());
        }


        if (Client.getGameData().newRecruit()) {
            //Client.getGameData().getHiddenUnitStats().draw(g, mainFrame);

            g.setFont(Client.getFontBig());
            String text = "You have recruited a new " + Client.getGameData().getHiddenUnitStats().getName();

            //int atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(text)/2);
            //int atY = Client.getGameData().getHiddenUnitStats().getScreenY();
            //int atY = getY() + getHeight();
            //int atY = getY();

            //g.setColor(Color.black);
            //g.drawString(text, atX+1, atY);
            //g.drawString(text, atX-1, atY);
            //g.drawString(text, atX, atY+1);
            //g.drawString(text, atX, atY-1);
            //g.setColor(Color.yellow.brighter());

            //g.drawString(text, atX, atY);
            //g.setFont(Client.getFont());
        }

        //profile area top left of screen
        if ((Client.getGameData().getMouseX() >= getScreenX() && Client.getGameData().getMouseX() <= getScreenX() + 180) &&
                (Client.getGameData().getMouseY() >= getScreenY() && Client.getGameData().getMouseY() <= getScreenY() + 65)) {
            rosterText.setText("Wins:" + Client.getWins() + " Losses:" + Client.getLosses());
            rosterText.draw(g, mainFrame);

            if (!inside) {
                Client.getImages().playSound(Constants.SOUND_MOUSEOVER);
            }
            inside = true;
            //g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            //g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
            if(msgText != "") {
                rosterText.setText(msgText);
                rosterText.draw(g, mainFrame);

                //g.setFont(Client.getFont());
                //g.setColor(Color.white);
                //g.drawString(msgText, 4 + 20, 505 + 13);
                //msgText = "";
            }
        }

        super.draw(g, mainFrame);
    }


    /////////////////////////////////////////////////////////////////
    // Draw some text
    /////////////////////////////////////////////////////////////////
    public void drawText(Graphics2D g, String text, int atX, int atY, Color color) {
        // draw the outline
        g.setColor(Color.black);
        g.drawString(text, atX + 1, atY);
        g.drawString(text, atX - 1, atY);
        g.drawString(text, atX, atY + 1);
        g.drawString(text, atX, atY - 1);

        // set the right color
        g.setColor(color);

        // draw it
        g.drawString(text, atX, atY);

    }
}
