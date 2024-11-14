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


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RosterPanel(boolean isNoob) {
        super(0,
                0,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT);

        noob = true;

        MuteButton mb = new MuteButton(718, 500, 54, 34);
        add(mb);
        MusicButton msb = new MusicButton(718, 545, 54, 34);
        add(msb);

        // Play
        ComputerButton computerButton = new ComputerButton(this, 25, 101, 38, 38, true);
        add(computerButton);

        rosterText = null;
    }


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public RosterPanel() {
        super(0,
                0,
                Constants.SCREEN_WIDTH,
                Constants.SCREEN_HEIGHT);
        //super(Constants.OFFSET,
        // Constants.OFFSET,
        // Constants.SCREEN_WIDTH - (Constants.OFFSET*2),
        // Constants.SCREEN_HEIGHT - (Constants.OFFSET*2));


        ForumButton fb = new ForumButton(699, 37, 91, 17);
        add(fb);
        BlogButton bb = new BlogButton(699, 87, 91, 17);
        add(bb);
        GuideButton gb = new GuideButton(699, 137, 91, 17);
        add(gb);
        ExitButton eb = new ExitButton(699, 187, 91, 17);
        add(eb);

        ScoresButton sb = new ScoresButton(699, 276, 91, 17);
        add(sb);
        AccountButton ab = new AccountButton(699, 326, 91, 17);
        add(ab);
        ReferButton rb = new ReferButton(699, 376, 91, 17);
        add(rb);
        CreditsButton cb = new CreditsButton(699, 426, 91, 17);
        add(cb);

        MuteButton mb = new MuteButton(718, 500, 54, 34);
        add(mb);
        MusicButton msb = new MusicButton(718, 545, 54, 34);
        add(msb);


        // Create the buttons
        int buttonX = 5; //(Constants.SCREEN_WIDTH / 7) - 1;
        int buttonHeight = 154;
        int buttonY = Constants.SCREEN_HEIGHT - buttonHeight;

        // Play team
        TeamButton teamButton = new TeamButton(
                this,
                buttonX,
                153,
                38,
                38);
        add(teamButton);

        // Play
        ComputerButton computerButton = new ComputerButton(
                this,
                buttonX,
                69,
                38,
                38,
                false);
        add(computerButton);

        // play button
        PlayButton playButton = new PlayButton(
                this,
                buttonX,
                195,
                38,
                38);
        add(playButton);

        // Duel with Random Armies
        PlayDuelButton playDuelButton = new PlayDuelButton(
                this,
                buttonX,
                237,
                38,
                38);
        add(playDuelButton);

        // Duel with Mirrored Random Armies
        mirroredRandomButton mirroredRandom = new mirroredRandomButton(
                this,
                buttonX,
                280,
                38,
                38);
        add(mirroredRandom);

        // Coop
        CooperativeButton cooperativeButton = new CooperativeButton(
                this,
                buttonX,
                111,
                38,
                38);
        add(cooperativeButton);

        // edit army
        EditButton editButtonPlayer = new EditButton(
                this,
                buttonX,
                323,
                38,
                38);
        add(editButtonPlayer);

        // Quit
        ArchiveButton archiveButton = new ArchiveButton(
                this,
                buttonX,
                368,
                38,
                38);
        add(archiveButton);

        // Refresh
        NewButton newButton = new NewButton(
                this,
                buttonX,
                411,
                38,
                38);
        add(newButton);

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
            Client.getGameData().getHiddenUnitStats().draw(g, mainFrame);

            g.setFont(Client.getFontBig());
            String text = "You have recruited a new " +
                    Client.getGameData().getHiddenUnitStats().getName();

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

        if ((Client.getGameData().getMouseX() >= getScreenX() && Client.getGameData().getMouseX() <= getScreenX() + 180) &&
                (Client.getGameData().getMouseY() >= getScreenY() && Client.getGameData().getMouseY() <= getScreenY() + 65)) {
            rosterText.draw(g, mainFrame);

            if (!inside) {
                Client.getImages().playSound(Constants.SOUND_MOUSEOVER);
            }
            inside = true;
            //g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM_HIGHLIGHT), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
        } else {
            inside = false;
            //g.drawImage(Client.getImages().getImage(Constants.IMG_TEAM), getScreenX(), getScreenY(), getWidth(), getHeight(), mainFrame);
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
