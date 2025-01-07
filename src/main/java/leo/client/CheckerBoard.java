///////////////////////////////////////////////////////////////////////
// Name: CheckerBoard
// Desc: Render the map
// Date: 5/23/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.client.animation.Animation;
import leo.shared.*;
import org.tinylog.Logger;

import java.awt.*;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class CheckerBoard extends LeoComponent {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Frame mainFrame;
    private float alpha = 0.0f;
    private float alphaHalf = 0.0f;
    private float alphaMore = 0.0f;
    private float contraAlpha = 1.0f;
    private float contraHalf = 0.5f;
    private float contraMore = 0.25f;
    private final Font chatFont;
    private final Color chatColor = Color.green;
    private boolean oldTargetsEnemy = false;
    private Vector<Short> moveTargets = null;
    private Vector<Short> oldMoveTargets = null;
    private Vector<Short> enemMoveTargets = null;
    private short moveType;
    private short enemMoveType;
    private int typeMove;
    private short oldMoveType;
    private int oldTypeMove;
    private int enemTypeMove;
    private Vector<Short> attackTargets = null;
    private Vector<Short> oldAttackTargets = null;
    private Vector<Short> enemAttackTargets = null;
    private short attackType;
    private short enemAttackType;
    private int typeAttack;
    private short oldAttackType;
    private int oldTypeAttack;
    private int enemTypeAttack;
    private Vector<Short> actionTargets = null;
    private Vector<Short> oldActionTargets = null;
    private short actionType;
    private int type;
    private short oldActionType;
    private int oldType;
    private Vector<Short> castleTargets = null;
    private Vector<Short> relicTargets = null;
    private Vector<Short> oldCastleTargets = null;
    private Vector<Short> oldRelicTargets = null;
    private final Color gray = new Color(0.5f, 0.5f, 0.5f);
    private final Color linkColor = new Color(0.0f, 1.0f, 0.0f);
    private boolean clicked = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public CheckerBoard(int newX, int newY) {
        super(newX, newY, Constants.SQUARE_SIZE * Constants.BOARD_SIZE, Constants.SQUARE_SIZE * Constants.BOARD_SIZE);
        chatFont = new Font("SansSerif", Font.BOLD, 14);
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public boolean clickAt(int x, int y) {
        try {
            Client.getGameData().click();

            //debugging
   /*System.out.println("The following units are on the field:");
   Vector<Unit> unitsout = Client.getGameData().getBattleField().getUnits();
   Iterator it = unitsout.iterator();
   while(it.hasNext()) {
    Unit unitout = (Unit)it.next();
    System.out.println("    " + unitout.getName() + " at location " + 
      BattleField.getX(unitout.getLocation()) + ", " + BattleField.getY(unitout.getLocation()));
   }*/

            // Get the click location
            short clickX = (byte) ((x - getScreenX()) / Constants.SQUARE_SIZE);
            short clickY = (byte) ((y - getScreenY()) / Constants.SQUARE_SIZE);
            short location = BattleField.getLocation(clickX, clickY);

            if (Client.getGameData().getState() == Constants.STATE_UNIT)
                return unitClick(location);

            if (Client.getGameData().getState() == Constants.STATE_CASTLE)
                return castleClick(location);

            return false;
        } catch (Exception e) {
            Logger.error("CheckerBoard.clickAt(): " + e);
            Client.getGameData().click();
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Castle click
    /////////////////////////////////////////////////////////////////
    private boolean castleClick(short location) {
        try {
            if (Client.getGameData().getDeployingUnit() == null) {
                Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
                if (Client.getGameData().getSelectedUnit() != null)
                    Client.getImages().playSound(Constants.SOUND_BUTTON);
                return true;
            }

            Vector<Short> tmpCastleTargets = Client.getGameData().getDeployingUnit().getCastleTargets();
            int selection = -1;
            for (int i = 0; i < tmpCastleTargets.size(); i++) {
                Short tmpLocation = tmpCastleTargets.elementAt(i);
                if (tmpLocation.byteValue() == location)
                    selection = location;
            }

            // If they selected elsewhere...
            if (selection == -1) {
                Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
                if (Client.getGameData().getSelectedUnit() != null)
                    Client.getImages().playSound(Constants.SOUND_BUTTON);
                return true;
            }

            Unit unit = Client.getGameData().getBattleField().getUnitAt(location);
            if (unit != null && selection == -1) {
                Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
                Client.getImages().playSound(Constants.SOUND_BUTTON);
                return true;
            }

            if (!Client.getGameData().myTurn()) {
                return true;
            }

            short unitNumber = Client.getGameData().getMyCastle().getUnit(Client.getGameData().getDeployingUnit());
            Client.getGameData().getTextBoard().add(Client.getGameData().getMyCastle().deploy(Client.getGameData().getMyCastle().getTeam(), Client.getGameData().getDeployingUnit(), location));

            // Send the deploy action
            Client.getNetManager().sendAction(
                    Action.DEPLOY,
                    unitNumber,
                    location);

            //if (Client.getGameData().getDeployingUnit().deployed())
            // Client.getGameData().setSelectedUnit(Client.getGameData().getDeployingUnit());
            //else
            Client.getGameData().setSelectedUnit(null);
            Client.getGameData().getCastlePanel().initialize();
            Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } catch (Exception e) {
            System.out.println("CheckerBoard.castleClick" + e);
            Client.getGameData().click();
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Unit click
    /////////////////////////////////////////////////////////////////
    private boolean unitClick(short location) {
        try {
            // If no unit is selected
            if (Client.getGameData().getSelectedUnit() == null) {
                Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
                //if (Client.getGameData().getSelectedUnit() != null)
                // Client.getImages().playSound(Constants.SOUND_BUTTON);
                return true;
            }

            // If the selected action != null
            if (Client.getGameData().getSelectedUnit().getTeam() == Unit.TEAM_1 && Client.getGameData().myTurn() && Client.getGameData().getSelectedUnit().getCastle() == Client.getGameData().getMyCastle() && Client.getGameData().getSelectedAction() != null && Client.getGameData().getSelectedAction().getRemaining() > 0 && Client.getGameData().getSelectedUnit().ready()) {
                int targetType = Client.getGameData().getSelectedAction().getTargetType();

                // Get the targets
                Vector<Short> targets = Client.getGameData().getSelectedAction().getTargets();
                int selection = -1;
                for (int i = 0; i < targets.size(); i++) {
                    Short tmpLocation = targets.elementAt(i);
                    if (tmpLocation.byteValue() == location) {
                        if (targetType == TargetType.UNIT_LINE ||
                                targetType == TargetType.UNIT_LINE_JUMP ||
                                targetType == TargetType.UNIT_AREA) {
                            Unit somebody = Client.getGameData().getBattleField().getUnitAt(tmpLocation.byteValue());
                            if (somebody != null)
                                selection = tmpLocation.byteValue();
                        } else {
                            selection = tmpLocation.byteValue();
                        }
                    }
                }
    
    /*else if (Client.getGameData().getSelectedUnit.getCastle() != Client.getGameData().getMyCastle()) {
     
    }*/

                // If they selected elsewhere...
                if (selection == -1) {
                    Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
                    if (Client.getGameData().getSelectedUnit() != null)
                        Client.getImages().playSound(Constants.SOUND_BUTTON);

                    return true;
                }


                // Send the action
                Client.getNetManager().sendAction(
                        Client.getGameData().getSelectedUnit().getAction(Client.getGameData().getSelectedAction()),
                        Client.getGameData().getSelectedUnit().getLocation(),
                        location);

                // Perform the action
                Client.getGameData().getTextBoard().add(Client.getGameData().getSelectedAction().perform(location));
                Client.getGameData().setSelectedUnit(Client.getGameData().getSelectedUnit());
                //Client.getImages().playSound(Constants.SOUND_BUTTON);
                return true;
            }

            // See if we can move
            Action move = Client.getGameData().getSelectedUnit().getMove();
            if (Client.getGameData().getSelectedUnit().getTeam() == Unit.TEAM_1 && Client.getGameData().myTurn() && Client.getGameData().getSelectedUnit().getCastle() == Client.getGameData().getMyCastle() && move != null && move.getRemaining() > 0 && Client.getGameData().getSelectedUnit().ready()) {
                Vector<Short> targets = move.getTargets();
                for (int i = 0; i < targets.size(); i++) {
                    Short tmpLocation = targets.elementAt(i);
                    if (tmpLocation.byteValue() == location) {
                        // Send the action
                        Client.getNetManager().sendAction(
                                Client.getGameData().getSelectedUnit().getAction(move),
                                Client.getGameData().getSelectedUnit().getLocation(),
                                location);

                        Client.getGameData().getTextBoard().add(move.perform(location));
                        Client.getGameData().setSelectedUnit(Client.getGameData().getSelectedUnit());
                        //Client.getImages().playSound(Constants.SOUND_BUTTON);
                        return true;
                    }
                }

            }

            // See if we can attack
            Action attack = Client.getGameData().getSelectedUnit().getAttack();
            if (Client.getGameData().getSelectedUnit().getTeam() == Unit.TEAM_1 && Client.getGameData().myTurn() && Client.getGameData().getSelectedUnit().getCastle() == Client.getGameData().getMyCastle() && attack != null && attack.getRemaining() > 0 && Client.getGameData().getSelectedUnit().ready()) {
                Vector<Short> targets = attack.getTargets();
                for (int i = 0; i < targets.size(); i++) {
                    Short tmpLocation = targets.elementAt(i);
                    if (tmpLocation.byteValue() == location) {
                        Unit somebody = Client.getGameData().getBattleField().getUnitAt(tmpLocation.byteValue());
                        if (somebody != null) {
                            // Send the action
                            Client.getNetManager().sendAction(
                                    Client.getGameData().getSelectedUnit().getAction(attack),
                                    Client.getGameData().getSelectedUnit().getLocation(),
                                    location);

                            Client.getGameData().getTextBoard().add(attack.perform(location));
                            Client.getGameData().setSelectedUnit(Client.getGameData().getSelectedUnit());
                            //Client.getImages().playSound(Constants.SOUND_BUTTON);
                            return true;
                        }
                    }
                }
            }


            // Nothing's been found
            Client.getGameData().setSelectedUnit(Client.getGameData().getBattleField().getUnitAt(location));
            if (Client.getGameData().getSelectedUnit() != null)
                Client.getImages().playSound(Constants.SOUND_BUTTON);
            return true;
        } catch (Exception e) {
            System.out.println("CheckerBoard.unitClick(): " + e);
            Client.getGameData().click();
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Update Colors
    /////////////////////////////////////////////////////////////////
    private void fadeIn() {
    }


    /////////////////////////////////////////////////////////////////
    // Are there still old targets
    /////////////////////////////////////////////////////////////////
    private boolean oldTargets() {
        return oldAttackTargets != null
                || oldMoveTargets != null
                || oldActionTargets != null
                || oldCastleTargets != null
                || oldRelicTargets != null;
    }


    /////////////////////////////////////////////////////////////////
    // Update Colors
    /////////////////////////////////////////////////////////////////
    private void fadeOut() {
        if (alpha == 1.0f) {
            alpha = 0.0f;
            alphaHalf = 0.0f;
            alphaMore = 0.0f;
            contraAlpha = 1.0f;
            contraHalf = 0.5f;
            contraMore = 0.25f;
            oldAttackTargets = null;
            oldMoveTargets = null;
            oldActionTargets = null;
            oldCastleTargets = null;
            oldRelicTargets = null;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Clear from click
    /////////////////////////////////////////////////////////////////
    private void reinitialize() {
        alpha = 0.0f;
        alphaHalf = 0.0f;
        alphaMore = 0.0f;
        contraAlpha = 1.0f;
        contraHalf = 0.5f;
        contraMore = 0.25f;

        oldActionTargets = actionTargets;

        oldTargetsEnemy = moveTargets == null && attackTargets == null;

        if (moveTargets != null) {
            oldMoveTargets = moveTargets;
        } else {
            oldMoveTargets = enemMoveTargets;
        }
        if (attackTargets != null) {
            oldAttackTargets = attackTargets;
        } else {
            oldAttackTargets = enemAttackTargets;
        }
        oldCastleTargets = castleTargets;
        oldRelicTargets = relicTargets;

        if (!oldTargetsEnemy) {
            oldActionType = actionType;
            oldMoveType = moveType;
            oldAttackType = attackType;
            oldTypeAttack = typeAttack;
            oldTypeMove = typeMove;
            oldType = type;
        } else {
            oldActionType = actionType;
            oldMoveType = enemMoveType;
            oldAttackType = enemAttackType;
            oldTypeAttack = enemTypeAttack;
            oldTypeMove = enemTypeMove;
            oldType = type;
        }

        // Clear the selections
        actionTargets = null;
        moveTargets = null;
        enemMoveTargets = null;
        attackTargets = null;
        enemAttackTargets = null;
        castleTargets = null;
        relicTargets = null;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void draw(Graphics2D g, Frame newMainFrame) {
        mainFrame = newMainFrame;
        try {
            clicked = Client.getGameData().clicked();

            if (Client.getGameData().getSelectedUnit() != null && Client.getGameData().getSelectedUnit().isDead())
                Client.getGameData().setSelectedUnit(null);

            // reset the fade timers and target vectors
            if (clicked) {
                reinitialize();
                if (Client.getGameData().myTurn()) {
                    getUnitTargets();
                    getCastleTargets();
                }
            }

            // still fading
            if (alpha < 1.0f) {
                alpha += 0.2f;
                alphaHalf += 0.1f;
                alphaMore += 0.05f;

                contraAlpha -= 0.2f;
                contraHalf -= 0.1f;
                contraMore -= 0.05f;

                if (oldTargets())
                    fadeOut();
                else
                    fadeIn();
            }


            Image lightSquare = Client.getImages().getImage(Constants.IMG_LIGHT_TILE);
            Image darkSquare = Client.getImages().getImage(Constants.IMG_DARK_TILE);

            for (int x = 0; x < Constants.BOARD_SIZE; x++) {
                for (int y = 0; y < Constants.BOARD_SIZE; y++) {
                    Image img = ((x + y) % 2 == 0) ? lightSquare : darkSquare;
                    g.drawImage(img, x * Constants.SQUARE_SIZE + getScreenX(), y * Constants.SQUARE_SIZE + getScreenY(), mainFrame);
                }
            }

   /*
   g.setColor(Color.white);
   for (int x = 0; x < Constants.BOARD_SIZE; x++)
   {
    for (int y = 0; y < Constants.BOARD_SIZE; y++)
    { g.fillRect(x*Constants.SQUARE_SIZE+getScreenX(),y*Constants.SQUARE_SIZE+getScreenY(),Constants.SQUARE_SIZE,Constants.SQUARE_SIZE);
     g.setColor(
      g.getColor() == Color.white ?
       gray :
       Color.white); 
    }
    if (Constants.BOARD_SIZE % 2 == 0)
     g.setColor(
      g.getColor() == Color.white ?
       gray :
       Color.white); 
    
   }*/

            // Draw the castles
            short cx = BattleField.getX(Client.getGameData().getMyCastle().getLocation());
            short cy = BattleField.getY(Client.getGameData().getMyCastle().getLocation());
            Image cimage = Client.getImages().getImage(Constants.IMG_MY_CASTLE);
            g.drawImage(cimage, cx * Constants.SQUARE_SIZE + getScreenX(), cy * Constants.SQUARE_SIZE + getScreenY(), mainFrame);

            cx = BattleField.getX(Client.getGameData().getEnemyCastle().getLocation());
            cy = BattleField.getY(Client.getGameData().getEnemyCastle().getLocation());
            cimage = Client.getImages().getImage(Constants.IMG_ENEMY_CASTLE);
            g.drawImage(cimage, cx * Constants.SQUARE_SIZE + getScreenX(), cy * Constants.SQUARE_SIZE + getScreenY(), mainFrame);

            // Draw the animations
            g.setColor(Color.black);
            drawAnimations(g, mainFrame, Client.getGameData().getPreAnimations());

            // Put animated units on top
            Vector<Unit> units = Client.getGameData().getBattleField().getUnits();
            Stack<Unit> tops = new Stack<Unit>();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                if (unit == Client.getGameData().getSelectedUnit() || unit.readStepX() != 0 || unit.readStepY() != 0)
                    tops.push(unit);
            }
            while (!tops.empty()) {
                Unit unit = tops.pop();
                units.remove(unit);
                units.add(unit);
            }

            // Draw the units in the field
            for (int i = 0; i < units.size(); i++) {
                Unit unit = units.elementAt(i);
                Unit selection = Client.getGameData().getSelectedUnit();
                short x = BattleField.getX(unit.getLocation());
                short y = BattleField.getY(unit.getLocation());

                int appearance;

                // no art for enemy relics, powerups dont really care whos side they're on
                if (unit.isRelic()) {
                    appearance = unit.getAppearance();
                } else {
                    appearance = (unit.getAppearance() + (unit.getCastle() == Client.getGameData().getMyCastle() ? 0 : 1));
                }

                int stepX = unit.readStepX();
                int stepY = unit.readStepY();

                // is it usable?
    /*if ( unit.getCastle() == Client.getGameData().getMyCastle() && 
     (!unit.ready() || (unit.getTeam() == Unit.TEAM_2 && Client.getGameData().myTurn())) &&
     stepX == 0 &&
     stepY == 0)
    {
     appearance+=2;
     //image = Client.getImages().getGrayedImage(appearance);
    }
    Image image = unit.isStunned()
     ? Client.getImages().getRotatedImage(appearance)
     : Client.getImages().getImage(appearance);*/

                if (unit.getGraves()) {
                    Vector<Short> graves = unit.getBattleField().getGraves();
                    for (int grave = 0; grave < graves.size(); grave++) {
                        g.drawImage(Client.getImages().getImage(Constants.IMG_BONES),
                                (BattleField.getX(graves.elementAt(grave))) * Constants.SQUARE_SIZE + getScreenX(),
                                (BattleField.getY(graves.elementAt(grave))) * Constants.SQUARE_SIZE + getScreenY(),
                                mainFrame);
                    }
                }

                Image image = Client.getImages().getImage(appearance);
                if (unit.getCastle() == Client.getGameData().getMyCastle() &&
                        (!unit.ready() || (unit.getTeam() == Unit.TEAM_2 && Client.getGameData().myTurn())) &&
                        stepX == 0 &&
                        stepY == 0) {
                    image = Client.getImages().getGrayedImage(appearance, unit.getCastle() == Client.getGameData().getMyCastle());
                }
                if (unit.isStunned()) {
                    image = Client.getImages().getRotatedImage(appearance, unit.getCastle() == Client.getGameData().getMyCastle());
                }
    /*if (unit.isMechanical() && (unit.getCastle() != Client.getGameData().getMyCastle()))
    {
     image = Client.getImages().getGrayedImage(appearance + 1);
    }*/

                if (!unit.isHidden()) {

                    if (stepX == 0 && stepY == 0)
                    //if (true)
                    {
                        Composite toriginal = g.getComposite();
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, unit.getFade()));
                        if (unit.getCastle() == Client.getGameData().getMyCastle()) {
                            if (unit.getTeam() == Unit.TEAM_2) {
                                g.drawImage(Client.getImages().getImage(Constants.IMG_GREEN), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                            } else {
                                g.drawImage(Client.getImages().getImage(Constants.IMG_BLUE), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                            }
                        } else {
                            if (unit.getTeam() == Unit.TEAM_2) {
                                g.drawImage(Client.getImages().getImage(Constants.IMG_YELLOW), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                            }
                            else {
                                g.drawImage(Client.getImages().getImage(Constants.IMG_RED), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                            }
                        }
                        g.setComposite(toriginal);
                    }

                    if (unit == selection) {
                        // spotlight
                        g.drawImage(Client.getImages().getImage(Constants.IMG_SPOTLIGHT), (x - 1) * Constants.SQUARE_SIZE + getScreenX() + unit.readStepX(), (y - 1) * Constants.SQUARE_SIZE + getScreenY() + unit.readStepY(), mainFrame);
                    }

                    if (unit.haunted()) {
                        // blacklight
                        g.drawImage(Client.getImages().getImage(Constants.IMG_BLACKLIGHT), (x - 1) * Constants.SQUARE_SIZE + getScreenX() + unit.readStepX(), (y - 1) * Constants.SQUARE_SIZE + getScreenY() + unit.readStepY(), mainFrame);
                    }

                    g.drawImage(image, x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);

                    if (unit.isMechanical()) {
                        int mappear = unit.getAppearance();
                        Image mimage = unit.isStunned()
                                ? Client.getImages().getRotatedImage(mappear, unit.getCastle() == Client.getGameData().getMyCastle())
                                : Client.getImages().getGrayedImage(mappear, unit.getCastle() == Client.getGameData().getMyCastle());

                        Composite original = g.getComposite();
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        g.drawImage(mimage, x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                        g.setComposite(original);

                    }

                    if (unit.isPoisoned()) {
                        Composite original = g.getComposite();
                        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
                        g.drawImage(Client.getImages().getImage(Constants.IMG_DEATH), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                        g.setComposite(original);
                    }

                    // Draw gold border for buffed units
                    if (unit.boss()) {
                        g.drawImage(Client.getImages().getImage(Constants.IMG_BORDER), x * Constants.SQUARE_SIZE + getScreenX() + stepX, y * Constants.SQUARE_SIZE + getScreenY() + stepY, mainFrame);
                    }

                    //draw team icon
                    if(Client.getShowTeamIcons()) {
                        if (unit.getCastle() == Client.getGameData().getMyCastle()) {
                            if (unit.getTeam() == Unit.TEAM_2) {
                                g.drawImage(Client.getImages().getTeamIcon(2), x * Constants.SQUARE_SIZE + getScreenX() + stepX + Constants.SQUARE_SIZE - 15, y * Constants.SQUARE_SIZE + getScreenY() + stepY + Constants.SQUARE_SIZE - 15, mainFrame);
                            } else {
                                g.drawImage(Client.getImages().getTeamIcon(1), x * Constants.SQUARE_SIZE + getScreenX() + stepX + Constants.SQUARE_SIZE - 15, y * Constants.SQUARE_SIZE + getScreenY() + stepY + Constants.SQUARE_SIZE - 15, mainFrame);
                            }
                        } else {
                            if (unit.getTeam() == Unit.TEAM_2) {
                                g.drawImage(Client.getImages().getTeamIcon(4), x * Constants.SQUARE_SIZE + getScreenX() + stepX + Constants.SQUARE_SIZE - 15, y * Constants.SQUARE_SIZE + getScreenY() + stepY + Constants.SQUARE_SIZE - 15, mainFrame);
                            }
                            else {
                                g.drawImage(Client.getImages().getTeamIcon(3), x * Constants.SQUARE_SIZE + getScreenX() + stepX + Constants.SQUARE_SIZE - 15, y * Constants.SQUARE_SIZE + getScreenY() + stepY + Constants.SQUARE_SIZE - 15, mainFrame);
                            }
                        }
                    }                   
                }

                if (unit.getLink() != null) {
                    Unit tmp = unit.getLink();
                    short px = BattleField.getX(tmp.getLocation());
                    short py = BattleField.getY(tmp.getLocation());

                    if (unit == tmp) {
                        g.drawImage(Client.getImages().getImage(Constants.IMG_GREEN_BALL), px * Constants.SQUARE_SIZE + getScreenX() + unit.readStepX(), py * Constants.SQUARE_SIZE + getScreenY() + unit.readStepY(), mainFrame);
                    } else {

                        int cleft = Constants.SQUARE_SIZE / 2;
                        g.setColor(linkColor);
                        g.drawLine(x * Constants.SQUARE_SIZE + getScreenX() + cleft + unit.readStepX(), y * Constants.SQUARE_SIZE + getScreenY() + cleft + unit.readStepY(), px * Constants.SQUARE_SIZE + getScreenX() + cleft + tmp.readStepX(), py * Constants.SQUARE_SIZE + getScreenY() + cleft + tmp.readStepY());
                    }
                }


                // Is this unit selected?
                if (unit == selection) {
                    g.setColor(Color.black);
                    g.drawRect(x * Constants.SQUARE_SIZE + getScreenX() + unit.readStepX(), y * Constants.SQUARE_SIZE + getScreenY() + unit.readStepY(), Constants.SQUARE_SIZE - 1, Constants.SQUARE_SIZE - 1);
                    g.drawRect(x * Constants.SQUARE_SIZE + getScreenX() + 1 + unit.readStepX(), y * Constants.SQUARE_SIZE + getScreenY() + 1 + unit.readStepY(), Constants.SQUARE_SIZE - 3, Constants.SQUARE_SIZE - 3);

                }
                unit.getStepX();
                unit.getStepY();

            }

            drawOldTargets(g);

            // Draw the unit targets
            if (Client.getGameData().getState() == Constants.STATE_UNIT) {
                drawUnitTargets(g);
            }

            // Draw deploy targets greee
            if (Client.getGameData().getState() == Constants.STATE_CASTLE) drawCastleTargets(g);

            if (Client.getGameData().getSelectedUnit() != null && Client.getGameData().getSelectedUnit().getCastle() != Client.getGameData().getMyCastle()) {
                drawEnemyTargets(g);
            }

            // Draw the animations
            g.setColor(Color.black);
            drawAnimations(g, mainFrame, Client.getGameData().getAnimations());

            // Draw a black ring around it
            g.setColor(Color.black);
            g.drawRect(getScreenX(), getScreenY(), getWidth(), getHeight());
            g.drawRect(getScreenX() + 1, getScreenY() + 1, getWidth() - 2, getHeight() - 2);

            // Draw the chat text
            drawChat(g, mainFrame);
            clicked = false;
            Client.getGameData().unclick();

        } catch (Exception e) {
            Client.getGameData().click();
            System.out.println("CheckerBoard.draw(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Draw the chat text
    /////////////////////////////////////////////////////////////////
    public void drawChat(Graphics2D g, Frame mainFrame) { /*try
  { Font tmp = g.getFont();
   g.setFont(chatFont);
   g.setColor(chatColor);
   Vector<String> messages = Client.getText();
   

   for (int i = 0; i < messages.size(); i++)
   { String text = (String) messages.elementAt(i);
    int x = getScreenX() + 5;
    int y = getScreenY() + (Client.FONT_HEIGHT*(i*2)) + Constants.OFFSET + ((Client.FONT_HEIGHT + Constants.OFFSET)*2);

    g.setColor(Color.black);
    g.drawString(text, x+1, y);
    g.drawString(text, x-1, y);
    g.drawString(text, x, y+1);
    g.drawString(text, x, y-1);
    
    g.setColor(chatColor);
    g.drawString(text, x, y);
   }

   // Trim the buffer to fit
   while(g.getFontMetrics().stringWidth(Client.getMessageBuffer().toString()) > getWidth()-115)
    Client.getMessageBuffer().deleteCharAt(Client.getMessageBuffer().length()-1);

   while(Client.getText().size() > 10)
    Client.getText().removeElementAt(0);

   if (Client.getText().size() > 0 && Client.timeUp())
    Client.getText().clear();

   int atX = getScreenX() + (getWidth()/2) - (g.getFontMetrics().stringWidth(Client.getMessageBuffer().toString())/2);
   int atY = getScreenY() + (getHeight()/2) + (Client.FONT_HEIGHT/2);

   String text = Client.getMessageBuffer().toString();
   g.setColor(Color.black);
   g.drawString(text, atX+1, atY);
   g.drawString(text, atX-1, atY);
   g.drawString(text, atX, atY+1);
   g.drawString(text, atX, atY-1);
   g.setColor(chatColor);
   g.drawString(text, atX, atY);

   g.setFont(tmp);

  } catch (Exception e)
  { Client.getGameData().click();
  }*/
        return;
    }


    /////////////////////////////////////////////////////////////////
    // Draw the component
    /////////////////////////////////////////////////////////////////
    public void drawAnimations(Graphics2D g, Frame mainFrame, Vector<Animation> animations) {
        try {
            Vector<Animation> tmp = new Vector<Animation>();
            //Collections.reverse(animations);
            //Vector<Animation> animations = Client.getGameData().getAnimations();

            Iterator<Animation> id = animations.iterator();
            while (id.hasNext()) {
                tmp.add(id.next());
            }

            Iterator<Animation> it = tmp.iterator();
   
   /*if(!anim_wait){
     if(it.hasNext()){
       animation = it.next();
       anim_wait = true;
     }
   }
   if(animation != null )
     animation.draw(g, mainFrame, this);
   if(animation.finished()){
     animations.remove(animation);
     anim_wait = false;
   }*/

            while (it.hasNext()) {
                Animation animation = it.next();
                try {
                    animation.draw(g, mainFrame, this);
                } catch (Exception e) {
                    animations.remove(animation);
                    Logger.error("CheckerBoard.drawAnimations() animation.draw()" + e);
                }

                if (animation.finished()) {
                    Client.getGameData().animationDone(animation); //animations.remove(animation);
                }

            }

        } catch (Exception e) {
            Logger.error("CheckerBoard.drawAnimations(): " + e);
            Client.getGameData().click();
        }

    }


    /////////////////////////////////////////////////////////////////
    // Highlight old targets
    /////////////////////////////////////////////////////////////////
    private void drawOldTargets(Graphics2D g) {
        if (alpha >= 1.0f) return;

        float contraOldTarg = contraHalf;
        if (oldTargetsEnemy) contraOldTarg = contraMore;

        if (oldMoveTargets != null)
            highlight(g, oldMoveTargets, oldMoveType, Action.TYPE_COLOR[oldTypeMove], contraOldTarg, !oldTargetsEnemy);

        if (oldAttackTargets != null)
            highlight(g, oldAttackTargets, oldAttackType, Action.TYPE_COLOR[oldTypeAttack], contraOldTarg, !oldTargetsEnemy);

        if (oldActionTargets != null)
            highlight(g, oldActionTargets, oldActionType, Action.TYPE_COLOR[oldType], contraOldTarg, !oldTargetsEnemy);

        if (oldCastleTargets != null)
            highlight(g, oldCastleTargets, TargetType.CASTLE, Constants.IMG_BLUE, contraOldTarg, !oldTargetsEnemy);

        if (oldRelicTargets != null)
            highlight(g, oldRelicTargets, TargetType.UNIT_AREA, Constants.IMG_BLUE, contraOldTarg, !oldTargetsEnemy);
    }


    /////////////////////////////////////////////////////////////////
    // Highlight unit squares
    /////////////////////////////////////////////////////////////////
    private void drawUnitTargets(Graphics2D g) {
        if (!Client.getGameData().myTurn()) return;

        Unit selection = Client.getGameData().getSelectedUnit();
        if (selection == null) return;

        if (oldTargets()) return;

        try {
            if (moveTargets != null) {
                highlight(g, moveTargets, moveType, Action.TYPE_COLOR[typeMove], alphaHalf, true);
            }

            if (attackTargets != null) {
                highlight(g, attackTargets, attackType, Action.TYPE_COLOR[typeAttack], alphaHalf, true);
            }

            if (actionTargets != null) {
                highlight(g, actionTargets, actionType, Action.TYPE_COLOR[type], alphaHalf, !oldTargetsEnemy);
            }

        } catch (Exception e) {
            Client.getGameData().click();
            Logger.error("CheckerBoard.drawUnitTargets: " + e);
        }
    }

    private void drawEnemyTargets(Graphics2D g) {
        if (!Client.getGameData().myTurn()) return;

        Unit selection = Client.getGameData().getSelectedUnit();
        if (selection == null) return;

        if (oldTargets()) return;

        //enemMoveTargets = null;
        //enemAttackTargets = null;
        enemMoveType = (byte) 0;
        enemAttackType = (byte) 0;
        enemTypeMove = 0;
        enemTypeAttack = 0;
        if (selection.getMove() != null) {
            enemMoveTargets = selection.getMove().getTargets();
            enemMoveType = selection.getMove().getTargetType();
            enemTypeMove = selection.getMove().getType();
        }
        if (selection.getAttack() != null) {
            enemAttackTargets = selection.getAttack().getClientTargets();
            enemAttackType = selection.getAttack().getTargetType();
            enemTypeAttack = selection.getAttack().getType();
        }


        try {
            if (enemMoveTargets != null && actionTargets == null) {
                highlight(g, enemMoveTargets, enemMoveType, Action.TYPE_COLOR[enemTypeMove], alphaMore, false);
            }

            if (enemAttackTargets != null && actionTargets == null) {
                highlight(g, enemAttackTargets, enemAttackType, Action.TYPE_COLOR[enemTypeAttack], alphaMore, false);
            }

            if (actionTargets != null) {
                highlight(g, actionTargets, actionType, Action.TYPE_COLOR[type], alphaMore, false);
            }

        } catch (Exception e) {
            Client.getGameData().click();
            Logger.error("CheckerBoard.drawEnemyTargets: " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Acquire targets
    /////////////////////////////////////////////////////////////////
    private void getUnitTargets() {
        // Acquire new selections
        Unit selection = Client.getGameData().getSelectedUnit();
        if (selection != null) {

            Action selectedAction = Client.getGameData().getSelectedAction();
            if (selection.getTeam() == Unit.TEAM_2 || selection.getCastle() == Client.getGameData().getEnemyCastle()) {
                if (selectedAction != null) {
                    actionType = selectedAction.getTargetType();
                    type = selectedAction.getType();
                    actionTargets = selectedAction.getClientTargets();
                }
                return;
            }
            // If no action is selected, show move and attack
            if (selectedAction == null) {
                // Highlight the squares the unit can move to
                Action move = selection.getMove();
                if (move != null && move.getRemaining() > 0 && selection.ready()) {
                    moveType = selection.getMove().getTargetType();
                    typeMove = selection.getMove().getType();
                    moveTargets = move.getTargets();

                }

                // Highlight the squares the unit can attack at
                Action attack = selection.getAttack();
                if (attack != null && attack.getRemaining() > 0 && selection.ready()) {
                    attackType = selection.getAttack().getTargetType();
                    typeAttack = selection.getAttack().getType();
                    attackTargets = attack.getClientTargets();
                }

            } else {
                actionType = selectedAction.getTargetType();
                type = selectedAction.getType();
                actionTargets = selectedAction.getClientTargets();
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle's targets
    /////////////////////////////////////////////////////////////////
    private void getCastleTargets() {
        Unit selection = Client.getGameData().getDeployingUnit();
        if (selection != null)
            castleTargets = Client.getGameData().getDeployingUnit().getCastleTargets();
        if (selection != null && selection.isRelic() && selection.getDeployRange() > 1) {
            relicTargets = Client.getGameData().getBattleField().getArea(selection,
                    selection.getCastle().getLocation(),
                    (byte) selection.getDeployRange(),
                    true,
                    false,
                    false,
                    false, TargetType.BOTH, selection.getCastle());
        }
    }


    /////////////////////////////////////////////////////////////////
    // Highlight deploy squares
    /////////////////////////////////////////////////////////////////
    private void drawCastleTargets(Graphics2D g) // booo
    {
        try {

            // Highlight the deployable squares
            Unit selection = Client.getGameData().getDeployingUnit();
            if (selection != null && !oldTargets()) {
                highlight(g, castleTargets, TargetType.CASTLE, Constants.IMG_BLUE, alphaHalf, true);
            }
            if (selection != null && relicTargets != null && !oldTargets()) {
                highlight(g, relicTargets, TargetType.UNIT_AREA, Constants.IMG_BLUE, alphaHalf, false);
            }
        } catch (Exception e) {
            Logger.error("CheckerBoard.drawCastleTargets: " + e);
            Client.getGameData().click();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Some highlighted squares
    /////////////////////////////////////////////////////////////////
    private void highlight(Graphics2D g, Vector<Short> targets, int targetType, int colorImage, float colorAlpha, boolean mouseOverHighlight) {
        try {
            for (int i = 0; i < targets.size(); i++) {
                Short location = targets.elementAt(i);
                short x = BattleField.getX(location.byteValue());
                short y = BattleField.getY(location.byteValue());
                float drawAlpha = colorAlpha;
                int drawImage = colorImage;

                // Highlight the targets
                // If there's a unit there paint a solid square
                Unit somebody = Client.getGameData().getBattleField().getUnitAt(location.byteValue());
                //if (somebody == Client.getGameData().getSelectedUnit())
                // somebody = null;
                if (
                        somebody == null &&
                                (targetType == TargetType.UNIT_LINE ||
                                        targetType == TargetType.UNIT_LINE_JUMP ||
                                        targetType == TargetType.UNIT_AREA)) {
                    drawAlpha *= 2;
                    drawImage += 1;
                    Composite original = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, drawAlpha));
                    Image cimage = Client.getImages().getImage(drawImage);
                    g.drawImage(cimage, x * Constants.SQUARE_SIZE + getScreenX(), y * Constants.SQUARE_SIZE + getScreenY(), mainFrame);
                    g.setComposite(original);

                } else {
                    short clickX = (byte) ((Client.getGameData().getMouseX() - getScreenX()) / Constants.SQUARE_SIZE);
                    short clickY = (byte) ((Client.getGameData().getMouseY() - getScreenY()) / Constants.SQUARE_SIZE);
                    short clickLocation = BattleField.getLocation(clickX, clickY);
                    float solidAlpha = drawAlpha;
                    if (mouseOverHighlight && isWithin(Client.getGameData().getMouseX(), Client.getGameData().getMouseY()) && clickLocation == location.byteValue())
                        solidAlpha += 0.3f;
                    Composite original = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, solidAlpha));
                    Image cimage = Client.getImages().getImage(drawImage);
                    g.drawImage(cimage, x * Constants.SQUARE_SIZE + getScreenX(), y * Constants.SQUARE_SIZE + getScreenY(), mainFrame);

                    drawAlpha *= 2;
                    drawImage += 1;
                    cimage = Client.getImages().getImage(drawImage);
                    g.drawImage(cimage, x * Constants.SQUARE_SIZE + getScreenX(), y * Constants.SQUARE_SIZE + getScreenY(), mainFrame);

                    g.setComposite(original);
                }
            }
        } catch (Exception e) {
            Logger.error("CheckerBoard.highlight(): " + e);
            Client.getGameData().click();
        }
    }


}
