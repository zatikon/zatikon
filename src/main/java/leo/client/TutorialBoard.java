///////////////////////////////////////////////////////////////////////
// Name: TutorialBoard
// Desc: Displays Tutorial events to the client. A child of mainBoard.
// Date: 12/9/2010 - Created [Dan Healy]
// Date: 4/19/2011 - Added close window button [Julian Noble]
// Date: 4/21/2011 - Added index buttons [Julian Noble]
// Date: 4/28/2011 - Added image display for messages [Julian Noble]
// Date: 5/3/2011  - Added second msg array for second line of text
//                   [Julian Noble]
// Note: *MUST be last thing added to the mainBoard in ClientGameData.
//       *Click-blocking rectangles are commented out, they need work.
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Action;
import leo.shared.Constants;

import java.awt.*;

//final private short test = 1;

public class TutorialBoard extends LeoContainer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    // Navigation
    private final TutorialButton prev;
    private final TutorialButton jump_prev;
    private final TutorialButton next;
    private final TutorialButton jump_next;
    private final TutorialButton close;

    // Tip Index
    private int tipIndex = 0;
    private final TutorialButton ti_one;
    private final TutorialButton ti_two;
    private final TutorialButton ti_three;
    private final TutorialButton ti_four;
    private final TutorialButton ti_five;
    private final TutorialButton ti_six;
    private TutorialButton ti_seven;

    private boolean closed = false;
    private boolean browsing = false;
    private int message = 0;
    private final int windowX = getScreenX() + 85;
    private final int windowY = getScreenY() + 70;
    private final int windowW = 490;
    private final int windowH = 440;
    private final int offset = -4;
    private final int stage = 0;

    // First line of messages
    private final String[] messages = {

            // BLANK [0]
            " ",

            // START [1-4]
            "Welcome to Zatikon!",
            "Let's go over the basics.",
            "Start by clicking one of the buttons in the upper-right",
            "Blue squares will appear around your castle. Click one.",

            // DEPLOY [5-7]
            "This is called \"deploying.\"",
            "Units can't be used the turn they are deployed.",
            "Deploy another unit.",

            // 2ND DEPLOY [8-16]
            "Deploying units uses commands.",
            "Every turn you have 5 commands.",
            "Each unit's deply \"cost\" is specified to the right of its name.",
            "A unit with a deploy cost of \"3\" uses 3 commands to deploy.",
            "Tip:  It's a good idea to deploy your general first.                 ",
            "Press \"End Turn\" when you are out of commands.",
            "It will then be your opponent's turn.",
            "To win, you must move a unit onto your opponent's castle.",
            "Don't leave your castle defenseless,",

            // ENEMY END TURN [17-20]
            "Your next turn is about to begin,",
            "Try moving a unit.",
            "To move a unit, click on it, then click on a blue square.",
            "Keep in mind that some units can move more than one square at a time.",

            // MOVEMENT [21-33]
            "You moved a unit!",
            "Movement costs commands for most units.",
            "Click on a unit and it's stats will appear in the upper-right corner.",
            "Units have four unique stats:",
            "\"Actions\" determines how many actions a unit can perform per turn.",
            "\"Life\" determines how much damage a unit can sustain before dying.",
            "\"Power\" determines how much damage",
            "\"Armor\" is subtracted from incoming",
            "Below that are the unit's \"Abilities.\"",
            "By default, a unit's selected abilities are movement and attack.",
            "Some units just have move and attack,",
            "The actions cost of each ability is specified.",
            "Keep in mind your limited commands and",

            // END TURN [34-37]
            "After you win this game, you'll be able to customize your army.",
            "Every time you defeat an AI opponent or play against another player,",
            "Gold is used to unlock new units and buy",
            "Victories against AI will increase AI difficulty.",
            " "

    };

    // Second line of messages
    private final String[] messages2 = {

            // BLANK
            " ",

            // START
            " ",
            " ",
            "corner of the screen.",
            " ",

            // DEPLOY
            " ",
            "That's why it's colored grey.",
            " ",

            // 2ND DEPLOY
            " ",
            " ",
            " ",
            " ",
            "      You'll notice that it lowers the deploy cost of all units by 1.",
            " ",
            " ",
            " ",
            "as your opponent has the same goal.",

            // ENEMY END TURN
            "and you'll be able to use your units.",
            " ",
            " ",
            " ",

            // MOVEMENT
            " ",
            " ",
            " ",
            "Actions, Life, Power, and Armor.",
            " ",
            " ",
            "a unit can deal with its basic attack.",
            "damage to determine how much Life is lost.",
            " ",
            "Blue squares indicate move zones, while Red ones indicate attack zones.",
            "but some have other abilities, such as spells.",
            " ",
            "actions for each unit when moving and attacking.",

            // END TURN
            " ",
            "you'll be rewarded with gold",
            "duplicates of already owned units.",
            "Two defeats in a row will decrease difficulty. Good Luck!",
            " "

    };

    // 0 for full screen block, 1 for clicking
    private final int[] config = {
            0,                            // BLANK
            0, 0, 0, 0,                   // START
            0, 0, 0,                      // DEPLOY
            0, 0, 0, 0, 0, 0,             // 2ND DEPLOY
            0, 0, 0,
            0, 0, 0, 0,                   // ENEMY END TURN
            0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0,           // MOVEMENT
            0, 0, 0, 0,                    // END TURN
            1
    };
    private int last = 0;

    // Smallest index in each group
    private final int[] msgBase = {
            0,                            // BLANK
            1, 1, 1, 1,                   // START
            5, 5, 5,                      // DEPLOY
            8, 8, 8, 8, 8, 8,             // 2ND DEPLOY
            8, 8, 8,
            17, 17, 17, 17,               // ENEMY END TURN
            21, 21, 21, 21, 21, 21, 21,
            21, 21, 21, 21, 21, 21,     // MOVEMENT
            34, 34, 34, 34,               // END TURN
            38
    };

    // Largest index in each group
    private final int[] msgMax = {
            0,                            // BLANK
            4, 4, 4, 4,                   // START
            7, 7, 7,                      // DEPLOY
            16, 16, 16, 16, 16, 16,       // 2ND DEPLOY
            16, 16, 16,
            20, 20, 20, 20,               // ENEMY END TURN
            33, 33, 33, 33, 33, 33, 33,
            33, 33, 33, 33, 33, 33,     // MOVEMENT
            37, 37, 37, 37,               // END TURN
            38
    };

    // For determining which index buttons to show
    private final int[] tiGroup = {
            0,                            // BLANK
            1, 1, 1, 1,                   // START
            2, 2, 2,                      // DEPLOY
            3, 3, 3, 3, 3, 3,             // 2ND DEPLOY
            3, 3, 3,
            4, 4, 4, 4,                   // ENEMY END TURN
            5, 5, 5, 5, 5, 5, 5,
            5, 5, 5, 5, 5, 5,           // MOVEMENT
            6, 6, 6, 6,                   // END TURN
            7
    };

    // Images that are displayed in the center of the tutorial board
    //  each int corresponds with the message of the same index
    // [images should be 425 pixels wide and 275 pixels tall]
    private final int[] msgImg = {
            // BLANK
            0,
            // START
            Constants.IMG_TUTORIAL_01,
            Constants.IMG_TUTORIAL_01,
            Constants.IMG_TUTORIAL_03,
            Constants.IMG_TUTORIAL_04,
            // DEPLOY
            Constants.IMG_TUTORIAL_05,
            Constants.IMG_TUTORIAL_06,
            Constants.IMG_TUTORIAL_07,
            // 2ND DEPLY
            Constants.IMG_TUTORIAL_08,
            Constants.IMG_TUTORIAL_08,
            Constants.IMG_TUTORIAL_10,
            Constants.IMG_TUTORIAL_11,
            Constants.IMG_TUTORIAL_12,
            Constants.IMG_TUTORIAL_13,
            Constants.IMG_TUTORIAL_14,
            Constants.IMG_TUTORIAL_15,
            Constants.IMG_TUTORIAL_16,
            // ENEMY END TURN
            Constants.IMG_TUTORIAL_17,
            Constants.IMG_TUTORIAL_18,
            Constants.IMG_TUTORIAL_19,
            Constants.IMG_TUTORIAL_20,
            // MOVEMENT
            Constants.IMG_TUTORIAL_21,
            Constants.IMG_TUTORIAL_22,
            Constants.IMG_TUTORIAL_23,
            Constants.IMG_TUTORIAL_24,
            Constants.IMG_TUTORIAL_25,
            Constants.IMG_TUTORIAL_26,
            Constants.IMG_TUTORIAL_27,
            Constants.IMG_TUTORIAL_28,
            Constants.IMG_TUTORIAL_29,
            Constants.IMG_TUTORIAL_29,
            Constants.IMG_TUTORIAL_31,
            Constants.IMG_TUTORIAL_32,
            Constants.IMG_TUTORIAL_33,
            // END TURN
            Constants.IMG_TUTORIAL_34,
            Constants.IMG_TUTORIAL_35,
            Constants.IMG_TUTORIAL_36,
            Constants.IMG_TUTORIAL_01,

            0
    };

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public TutorialBoard() {
        super(Constants.OFFSET, Constants.OFFSET, Constants.SCREEN_WIDTH - (Constants.OFFSET * 2), Constants.SCREEN_HEIGHT - (Constants.OFFSET * 2));
        int atX = windowX + (windowW / 2) - 40;
        int atY = windowY + (windowH / 2) + (Client.FONT_HEIGHT / 2) + 15 + Constants.OFFSET;
        prev = new TutorialButton((atX - 17) + offset, atY + 100, 30, 30, 0);
        jump_prev = new TutorialButton((atX - 17) + offset, atY + 100, 30, 30, 1);
        next = new TutorialButton(atX + 23 + offset, atY + 100, 30, 30, 2);
        jump_next = new TutorialButton(atX + 23 + offset, atY + 100, 30, 30, 3);
        close = new TutorialButton((atX + 63) + offset, atY + 100, 30, 30, 4);
        ti_one = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 10, 30, 30, 5);
        ti_two = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 50, 30, 30, 6);
        ti_three = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 90, 30, 30, 7);
        ti_four = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 130, 30, 30, 8);
        ti_five = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 170, 30, 30, 9);
        ti_six = new TutorialButton(getScreenX() + offset + 10, getScreenY() + offset + 210, 30, 30, 10);
    }

    /////////////////////////////////////////////////////////////////
    // Set the message and prev/next buttons
    /////////////////////////////////////////////////////////////////
    public void setMessage(int index, int first) {
        if (first == 1) {
            browsing = false;
        }
        if (!closed & browsing) {
            if (index == message + 1) message = index;
            browsing = false;
        } else if (!closed & !browsing) {
            if (index >= 0 && index <= 38) message = index;
            else System.out.println("Invalid tutorial message!");
        }
        if (index > last) {
            last = msgMax[index];
        }
        if (!closed & index != 0) {
            clear();
            if ((message == msgMax[message]) && (msgMax[message] < msgMax[last]))
                add(jump_next);
            else if (message < msgMax[message])
                add(next);
            if ((message == msgBase[message]) && (message != 1))
                add(jump_prev);
            else if (message > msgBase[message])
                add(prev);
            add(close);
        } else {
            closed = true;
            clear();
        }
        dehighlight();
        highlight();
        if (message == 38)
            clear();
    }

    public void addIndex(int index) {
        if (index != 0) tipIndex = tiGroup[index];
        if (tipIndex == 0) return;
        if (tipIndex > 0) add(ti_one);
        if (tipIndex > 1) add(ti_two);
        if (tipIndex > 2) add(ti_three);
        if (tipIndex > 3) add(ti_four);
        if (tipIndex > 4) add(ti_five);
        if (tipIndex > 5) add(ti_six);
        if (tipIndex > 6) clear();
        return;
    }


    /////////////////////////////////////////////////////////////////
    // Activate the component
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        if (super.clickAt(x, y)) {
            return true;
        }
        //if the click is not within the components (buttons) of this container:
        boolean eatclick = true; //eatclick turns false if within allowed areas

        if (closed) return false;

        switch (config[last]) {
            case 0:
                return true;

            case 1:
                if (x < windowX || x > windowW + windowX
                        || y < windowY || y > windowH + windowY) {
                    eatclick = false;
                    break;
                }
        }
        return eatclick;
  /*
  switch (configuration[message]) //such as the below regions
  { case 0: //Configuration A: full screen block
    //(testX >= x && testX <= (x+w)) && (testY >= y && testY <= (y+h));
    break;
   case 1: //Configuration B: buttons available
    if ((x >= (getScreenX()+600)) && (y <= (getScreenY()+150)))
     eatclick = false;
    break;
   case 2: //Configuration C: castle+buttons avail
    if (((x >= (getScreenX()+600)) && (y <= (getScreenY()+150)))
    || ((x >= (getScreenX()+220) && x <= (getScreenX()+380)) && (y >= (getScreenY()+435))))
     eatclick = false;
    break;
   /*case 3: //Configuration D: make as many cases as necessary
    
    break;* /
   default:
    break;
  }
  /*System.out.println("given x:"+x+" given y:"+y);
  System.out.println("real:\nx:"+getScreenX()+" y:"+getScreenY()+" w:"+getWidth()+" h:"+getHeight());
  * /
  return eatclick;*/
    }


    /////////////////////////////////////////////////////////////////
    // Go to the previous message
    /////////////////////////////////////////////////////////////////
    public void prev() {
        setMessage(message - 1, 1);
        browsing = true;
    }

    /////////////////////////////////////////////////////////////////
    // Go to the next message
    /////////////////////////////////////////////////////////////////
    public void next() {
        setMessage(message + 1, 1);
        browsing = true;
    }


    /////////////////////////////////////////////////////////////////
    // Close current tutorial message
    /////////////////////////////////////////////////////////////////
    public void close() {
        closed = true;
        dehighlight();
        clear();
        Client.getNetManager().sendAction(Action.CHAT_TUTORIAL_CLOSED, Action.NOTHING, Action.NOTHING);
    }

    public void setClosed(boolean state) {
        closed = state;
    }

    /////////////////////////////////////////////////////////////////
    // Indexing
    /////////////////////////////////////////////////////////////////
    public void ti(int message) {
        closed = false;
        setMessage(msgBase[message], 1);
        browsing = true;
    }

    /////////////////////////////////////////////////////////////////
    // Dehighlight
    /////////////////////////////////////////////////////////////////
    public void dehighlight() {
        prev.highlighted = false;
        next.highlighted = false;
        close.highlighted = false;
        ti_one.highlighted = false;
        ti_two.highlighted = false;
        ti_three.highlighted = false;
        ti_four.highlighted = false;
        ti_five.highlighted = false;
        ti_six.highlighted = false;
    }

    /////////////////////////////////////////////////////////////////
    // Highlight
    /////////////////////////////////////////////////////////////////
    public void highlight() {
        int index_group = tiGroup[message];
        if (!closed) {
            switch (index_group) {
                case 1:
                    ti_one.highlighted = true;
                    break;
                case 2:
                    ti_two.highlighted = true;
                    break;
                case 3:
                    ti_three.highlighted = true;
                    break;
                case 4:
                    ti_four.highlighted = true;
                    break;
                case 5:
                    ti_five.highlighted = true;
                    break;
                case 6:
                    ti_six.highlighted = true;
                    break;
                default:
                    break;
            }
        }
    }
 /*
 public short getByte()
 {
   return test;
 }*/

    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame mainFrame) {
        // If they haven't closed it, display the following
        if (!closed) {
            //if (message < msgMax[message] && message < last) add(next);

            Image img;
            g.setColor(Color.black);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .85f));
            if (config[last] == 0) {
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .5f));
                g.fillRect(getScreenX(), getScreenY(), 1000, 1000);
                g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .7f));
            }
            img = Client.getImages().getImage(Constants.IMG_TUTORIAL_BACK);
            g.drawImage(img, windowX - 5 - 100, windowY - 100, mainFrame);

            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
            //g.setFont(Client.getFontBig());
            String text_line1 = messages[message];
            String text_line2 = messages2[message];

            // If there is an image for the message, show it
            if (msgImg[message] != 0) {
                img = Client.getImages().getImage(msgImg[message]);
                g.fillRect(windowX + 30, windowY + 23, 429, 279);
                g.drawImage(img, windowX + 32, windowY + 25, mainFrame);
            }

            // Show the border
            img = Client.getImages().getImage(Constants.IMG_TUTORIAL_BORDER);
            g.drawImage(img, windowX - 5 - 100, windowY - 100, mainFrame);

            int atX1 = windowX + (windowW / 2) - (g.getFontMetrics().stringWidth(text_line1) / 2);
            int atY1 = windowY + (windowH / 2) + (20 / 2) + 100;

            int atX2 = windowX + (windowW / 2) - (g.getFontMetrics().stringWidth(text_line2) / 2);
            int atY2 = windowY + (windowH / 2) + (20 / 2) + 115;
 
     /* (This is commented out because it needs more work)
     //Shade the click-blocking regions - actual pic will be highlighting clickable regions
     //In other words, delete all this and use the clickat numbers for pic placement. 
     Image image = Client.getImages().getImage(Constants.IMG_BORDER);
     int x = getScreenX(); int y = getScreenY(); int w = getWidth(); int h = getHeight();
     switch (configuration[message])
     { case 0: //Configuration A: full screen block
       g.drawImage(image, x, y, w, h, mainFrame);
       break;
      case 1: //Configuration B: buttons available
       g.drawImage(image, x, y, w-200, y+150, mainFrame);
       g.drawImage(image, x, y+150, w, h-150, mainFrame);
       break;
      case 2: //Configuration C: castle+buttons avail
       g.drawImage(image, x, y, x+220, h, mainFrame);
       g.drawImage(image, x+220, y, 600-150, 435, mainFrame);
       g.drawImage(image, x+380, y+150, w-380, h-150, mainFrame);
      /*case 3 //A//Configuration D: make as many cases as necessary
        g.drawImage(image, x, y, x+220, h, mainFrame);
        break* /
     }
     */

            // Print each line of the message as white with
            //  a black border
            g.setColor(Color.black);

            g.drawString(text_line1, atX1 + 1, atY1);
            g.drawString(text_line1, atX1 - 1, atY1);
            g.drawString(text_line1, atX1, atY1 + 1);
            g.drawString(text_line1, atX1, atY1 - 1);

            g.drawString(text_line2, atX2 + 1, atY2);
            g.drawString(text_line2, atX2 - 1, atY2);
            g.drawString(text_line2, atX2, atY2 + 1);
            g.drawString(text_line2, atX2, atY2 - 1);

            g.setColor(Color.white);

            g.drawString(text_line1, atX1, atY1);
            g.drawString(text_line2, atX2, atY2);

            g.setFont(Client.getFont());

        }
        addIndex(0);
        super.draw(g, mainFrame);
    }

}
