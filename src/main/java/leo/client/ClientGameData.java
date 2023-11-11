///////////////////////////////////////////////////////////////////////
// Name: ClientGameData
// Desc: The game data
// Date: 5/10/2003 - Created [Gabe Jones]
//       11/18/2010 - Updates: Finished redrawArmy [Tony Schwartz]
//       12/9/2010 - Added tutorialBoard and tutorialEvent [Dan Healy]
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.client.animation.Animation;
import leo.client.animation.AnimationDamage;
import leo.shared.*;
import org.tinylog.Logger;

import java.awt.*;
import java.util.Iterator;
import java.util.Vector;


public class ClientGameData {
    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private Castle army = new Castle();
    private final Castle enemyCastle = new Castle();
    private final Castle myCastle = new Castle();
    private BattleField battleField;
    private Unit selectedUnit = null;
    private Unit deployingUnit = null;
    private Action selectedAction = null;
    private int state = Constants.STATE_CASTLE;
    private Castle castlePlaying = enemyCastle;
    private int mouseX;
    private int mouseY;
    private final ClientObserver observer = new ClientObserver();
    private final Vector<Animation> animations = new Vector<Animation>();
    private final Vector<Animation> preAnimations = new Vector<Animation>();
    private int enemyRating;
    private String enemyName;
    private ClientTimer timer;
    private boolean offeredDraw = false;
    private boolean myDraw = false;
    private boolean clicked = false;
    private boolean castleChanged = false;
    private boolean playing = false;
    private boolean rebuilding = false;
    private boolean disconnecting = false;
    private boolean disableRepickP1 = false;
    private boolean disableRepickP2 = false;
    private int currPlayer = 1;
    private int gameType;
    private final int[][] animationDirectionGrid = new int[Constants.BOARD_SIZE][Constants.BOARD_SIZE];


    /////////////////////////////////////////////////////////////////
    // Gui components
    /////////////////////////////////////////////////////////////////
    private final BackBoard mainBoard = new BackBoard(Color.orange, 0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
    private final CheckerBoard checkerBoard = new CheckerBoard(Constants.OFFSET, Constants.OFFSET);
    private final SideBoard sideBoard = new SideBoard(Color.white, checkerBoard.getScreenX() + checkerBoard.getWidth() + Constants.OFFSET, Constants.OFFSET, 200 - Constants.OFFSET, 600 - (Constants.OFFSET * 2));
    private final TextBoard textBoard = new TextBoard(checkerBoard.getScreenX() + checkerBoard.getWidth() + Constants.OFFSET, Constants.OFFSET, 200 - Constants.OFFSET, 600 - (Constants.OFFSET * 2));
    private final SideBoard castleBoard = new SideBoard(Color.white, checkerBoard.getScreenX() + checkerBoard.getWidth() + Constants.OFFSET, Constants.OFFSET, 200 - Constants.OFFSET, 600 - (Constants.OFFSET * 2));
    private final StatPanel statPanel = new StatPanel(sideBoard);
    private final ButtonPanel buttonPanel = new ButtonPanel(sideBoard);
    private final CastlePanel castlePanel = new CastlePanel(castleBoard);
    private final EndPanel endPanel = new EndPanel(sideBoard);
    //private RedrawPanel redrawPanel  = new RedrawPanel(sideBoard);
    private final RedrawButton redrawButton = new RedrawButton(endPanel);
    private final EndButton endButton = new EndButton(endPanel);
    private final SurrenderButton surrenderButton = new SurrenderButton(endPanel);
    private final DrawButton drawButton = new DrawButton(endPanel);
    private LoadingPanel loadingPanel = new LoadingPanel();
    private TeamLoadingPanel teamLoadingPanel = new TeamLoadingPanel();
    private TimeOutPanel timeOutPanel = new TimeOutPanel();
    private final VersusPanel versusPanel = new VersusPanel();
    private RosterPanel rosterPanel = new RosterPanel();
    private EditCastlePanel editCastlePanel = new EditCastlePanel();
    private final EndGamePanel endGamePanel = new EndGamePanel();
    private final MessagePanel messagePanel = new MessagePanel();
    private PlayerPanel playerPanel = new PlayerPanel();
    private final HiddenUnitStats hiddenUnitStats = new HiddenUnitStats(mainBoard);
    private boolean newRecruit = false;
    private long gainGold = 0;
    private TutorialBoard tutorialBoard;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientGameData() {
        // Place all the GUI components
        //mainBoard.add(checkerBoard);
        //mainBoard.add(castleBoard);
        sideBoard.add(statPanel);
        sideBoard.add(buttonPanel);
        sideBoard.add(endPanel);
        //sideBoard.add(redrawPanel);

        castleBoard.add(endPanel);
        castleBoard.add(castlePanel);
        //castleBoard.add(redrawPanel);

        endPanel.add(redrawButton);
        endPanel.add(endButton);

        // Add to the text board
        //textBoard.add(surrenderButton);
        //textBoard.add(drawButton);

        // Add to the end panel. Order is important!!!
        // Parent must be set to the end panel
        endPanel.add(surrenderButton);
        endPanel.add(drawButton);


        getMyCastle().setObserver(observer);
        getEnemyCastle().setObserver(observer);

        initBattle();

        for (int i = 0; i < Constants.BOARD_SIZE; ++i) {
            for (int j = 0; j < Constants.BOARD_SIZE; ++j) {
                animationDirectionGrid[i][j] = 0;
            }
        }
    }


    /////////////////////////////////////////////////////////////////
    // Tutorial Event - gets messages from TutorialGame on the server
    /////////////////////////////////////////////////////////////////
    public void tutorialEvent(int message, int first) {
        if (message != 0) timer.end();
        if (tutorialBoard == null) tutorialBoard = new TutorialBoard();
        else {
            mainBoard.remove(tutorialBoard);
        }
        if (first == 1) {
            tutorialBoard.setClosed(false);
        }


        tutorialBoard.setMessage(message, first);
        tutorialBoard.addIndex(message);

        mainBoard.add(tutorialBoard);
    }


    /////////////////////////////////////////////////////////////////
    // Resynch
    /////////////////////////////////////////////////////////////////
    public void resynch() {
        try {
            while (!Client.load())
                Thread.sleep(50);
        } catch (Exception e) {
        }


        playing = false;
        rebuilding = true;
        leo.shared.Observer quiet = new QuietObserver();
        getMyCastle().setObserver(quiet);
        getEnemyCastle().setObserver(quiet);
        screenMessage("The game is resynching...");
        //mainBoard.add(checkerBoard);
    }


    /////////////////////////////////////////////////////////////////
    // Opponent disconnect
    /////////////////////////////////////////////////////////////////
    public void opponentDisconnect() {
        playing = false;
        rebuilding = true;
        QuietObserver quiet = new QuietObserver();
        getMyCastle().setObserver(quiet);
        getEnemyCastle().setObserver(quiet);
        screenMessage("Opponent disconnected, please wait while they reconnect...");
        //mainBoard.add(checkerBoard);
    }


    /////////////////////////////////////////////////////////////////
    // End Resynch
    /////////////////////////////////////////////////////////////////
    public void endResynch() {
        // clean up the units
        Vector<Unit> units = battleField.getUnits();
        Iterator<Unit> it = units.iterator();
        offeredDraw = false;

        while (it.hasNext()) {
            Unit tmp = it.next();
            tmp.setLocation(tmp.getLocation());
            tmp.setHidden(false);
        }

        getMyCastle().setObserver(observer);
        getEnemyCastle().setObserver(observer);
        rebuilding = false;
        screenGame();
        Castle tmp = getCastlePlaying();
        setCastlePlaying(myCastle);
        setCastlePlaying(enemyCastle);
        setCastlePlaying(tmp);
        setSelectedUnit(null);
    }


    /////////////////////////////////////////////////////////////////
    // End it all
    /////////////////////////////////////////////////////////////////
    public void end() {
        if (timer != null) timer.end();
    }


    /////////////////////////////////////////////////////////////////
    // Initialize battlefield
    /////////////////////////////////////////////////////////////////
    private void initBattle() { // Initialize the battlefield
        battleField = new BattleField(getMyCastle(), getEnemyCastle());
        getMyCastle().setBattleField(getBattleField());
        getEnemyCastle().setBattleField(getBattleField());
        getMyCastle().setLocation(BattleField.getLocation((byte) 5, (byte) 10));
        getEnemyCastle().setLocation(BattleField.getLocation((byte) 5, (byte) 0));
        getMyCastle().refresh(Unit.TEAM_1);
        getEnemyCastle().refresh(Unit.TEAM_1);

        // Set the observer
        //getMyCastle().setObserver(observer);
        //getEnemyCastle().setObserver(observer);

    }


    /////////////////////////////////////////////////////////////////
    // Cancel a queue wait
    /////////////////////////////////////////////////////////////////
    public void cancelQueue() {
        if (mainBoard.queueing())
            screenRoster();
    }


    /////////////////////////////////////////////////////////////////
    // Set Screen to loading
    /////////////////////////////////////////////////////////////////
    public void screenLoading(String newText) {
        //System.out.println("screen loading: " + newText);
        mainBoard.clear();
        loadingPanel = new LoadingPanel();
        loadingPanel.setText(newText);
        mainBoard.add(loadingPanel);
        ////////////////////////////////////////////
        mainBoard.add(playerPanel);
        //Client.getText().clear();
        mainBoard.queue();
    }

    /////////////////////////////////////////////////////////////////
    // Set screen to loading and preparing a team game
    /////////////////////////////////////////////////////////////////
    public void screenTeamLoading() {
        mainBoard.clear();
        teamLoadingPanel = new TeamLoadingPanel();
        mainBoard.add(teamLoadingPanel);
        mainBoard.add(playerPanel);
        mainBoard.queue();
    }

    /////////////////////////////////////////////////////////////////
    // Set Screen to loading
    /////////////////////////////////////////////////////////////////
    public void screenTimingOut() {
        //System.out.println("screen loading: " + newText);
        mainBoard.clear();
        timeOutPanel = new TimeOutPanel();
        mainBoard.add(timeOutPanel);
        ////////////////////////////////////////////
        mainBoard.add(playerPanel);

        Client.getText().clear();
        mainBoard.queue();
    }


    /////////////////////////////////////////////////////////////////
    // Queue. Don't ask. I hope I never have to see this code again
    /////////////////////////////////////////////////////////////////
    public void queue() {
        mainBoard.queue();
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to versus
    /////////////////////////////////////////////////////////////////
    public void screenVersus() {
        mainBoard.clear();
        ////////////////////////////////////////////
        mainBoard.add(playerPanel);

        mainBoard.add(versusPanel);
        Client.getImages().playSound(Constants.SOUND_PRELUDE);
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to the game
    /////////////////////////////////////////////////////////////////
    public void screenGame() {
        playing = true;
        mainBoard.clear();
        Client.getText().clear();
        Client.getMessageBuffer().delete(0, Client.getMessageBuffer().length());

        //mainBoard.add(tutorialBoard);
        mainBoard.add(checkerBoard);
        /////////////////////////////////////
        mainBoard.add(playerPanel);

        //mainBoard.add(castleBoard);
        //mainBoard.add(textBoard);

    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to roster
    /////////////////////////////////////////////////////////////////
    public void screenRoster() {
        newRecruit = false;
        gainGold = 0;
        mainBoard.clear();
        messagePanel.setMessage("Loading player data...");
        mainBoard.add(messagePanel);
        //Client.getNetManager().getArmyUnits();
        Client.getNetManager().sendAction(Action.GET_UPDATE, Action.NOTHING, Action.NOTHING);
        mainBoard.clear();
        mainBoard.add(rosterPanel);
        ////////////////////////////////////////////
        Client.getNetManager().sendAction(Action.CHATTING, Action.NOTHING, Action.NOTHING);
        mainBoard.add(playerPanel);

    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to lose link
    /////////////////////////////////////////////////////////////////
    public void screenDisconnect() {
        if (disconnecting) return;
        if (Client.timingOut()) return;
        disconnecting = true;

        mainBoard.clear();
        messagePanel.setMessage("Your connection interrupted, attempting to reconnect...");
        mainBoard.add(messagePanel);
        //////////////////////////////////
        mainBoard.add(playerPanel);

        Reconnector reconnector = new Reconnector();

        //try
        //{ Thread.sleep(3000);
        //} catch (Exception e)
        //{
        //}
        //Client.shutdown();
    }


    /////////////////////////////////////////////////////////////////
    // Reconnect
    /////////////////////////////////////////////////////////////////
    public void reconnect() {
        disconnecting = false;
        screenRoster();
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to end game
    /////////////////////////////////////////////////////////////////
    public void screenEndGame(String message, boolean victory) {
        playing = false;
        if (timer != null) timer.end();
        mainBoard.clear();
        endGamePanel.setMessage(message);
        endGamePanel.setVictory(victory);
        mainBoard.add(endGamePanel);
        ////////////////////////////////
        mainBoard.add(playerPanel);
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to edit castle
    /////////////////////////////////////////////////////////////////
    public void screenEditCastle() {
        mainBoard.clear();
        editCastlePanel = new EditCastlePanel();
        editCastlePanel.initialize(true);
        mainBoard.add(editCastlePanel);
        //////////////////////////////////////
        mainBoard.add(playerPanel);
        Client.getNetManager().sendAction(Action.EDIT_ARMY, Action.NOTHING, Action.NOTHING);
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to a message
    /////////////////////////////////////////////////////////////////
    public void screenMessage(String message) {
        mainBoard.clear();
        messagePanel.setMessage(message);
        mainBoard.add(messagePanel);
        /////////////////////////////////////////
        mainBoard.add(playerPanel);
    }


    /////////////////////////////////////////////////////////////////
    // SetScreen to a message
    /////////////////////////////////////////////////////////////////
    public void screenProgress() {
        mainBoard.clear();
        //mainBoard.add(playerPanel);
        mainBoard.add(new ProgressPanel());
    }


    /////////////////////////////////////////////////////////////////
    // Process a click
    /////////////////////////////////////////////////////////////////
    public void clickAt(int x, int y) {
        // Fire away
        //System.out.println("Click at " + x + ", " + y);

        mainBoard.clickAt(x, y);
    }


    /////////////////////////////////////////////////////////////////
    // Set the selected unit
    /////////////////////////////////////////////////////////////////
    public void setSelectedUnit(Unit newSelectedUnit) {
        try {
            // Going to null
            if (newSelectedUnit == null && selectedUnit != null) {
                mainBoard.remove(sideBoard);
                if (myTurn())
                    mainBoard.add(castleBoard);
                else
                    mainBoard.add(textBoard);
                setState(Constants.STATE_CASTLE);
                castlePanel.initialize();
                setDeployingUnit(null);
            }

            // Coming from null
            if (newSelectedUnit != null && selectedUnit == null) {
                mainBoard.add(sideBoard);
                mainBoard.remove(castleBoard);
                mainBoard.remove(textBoard);
                setState(Constants.STATE_UNIT);
                setDeployingUnit(null);
            }

            // Set the unit
            selectedUnit = newSelectedUnit;

            // Clear the old interface
            buttonPanel.clear();

            // Create the interface
            if (selectedUnit != null) {
                Vector<Action> actions = selectedUnit.getActions();
                for (int i = 0; i < actions.size(); i++) {
                    Action action = actions.elementAt(i);
                    ActionButton button = new ActionButton(action, buttonPanel);
                    buttonPanel.add(button);
                }
            }
            if (tutorialBoard != null) tutorialEvent(0, 0);

            // Clear the selected action
            selectedAction = null;
            deployingUnit = null;
        } catch (Exception e) {
            Logger.error("ClientGameData.setSelectedUnit(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Set the deploying unit
    /////////////////////////////////////////////////////////////////
    public void setDeployingUnit(Unit newUnit) {
        deployingUnit = newUnit;
    }


    /////////////////////////////////////////////////////////////////
    // Set the deploying unit
    /////////////////////////////////////////////////////////////////
    public void setCastlePlaying(Castle newCastlePlaying) {
        setSelectedUnit(null);
        if (castlePlaying == newCastlePlaying) return;
        castlePlaying = newCastlePlaying;
        if (rebuilding) return;
        mainBoard.clear();
        mainBoard.add(checkerBoard);
        ////////////////////////////////////
        mainBoard.add(playerPanel);

        // Is it my turn?
        if (newCastlePlaying == getMyCastle()) {
            // Refresh their units
            castlePanel.initialize();
            mainBoard.remove(sideBoard);
            mainBoard.remove(textBoard);
            mainBoard.add(castleBoard);

            // tmp
            //sideBoard.add(endPanel);
        }

        // Their turn?
        else {
            textBoard.clear();
            textBoard.add("Opponent's turn");

            mainBoard.remove(sideBoard);
            mainBoard.remove(castleBoard);
            mainBoard.add(textBoard);
            //sideBoard.remove(endPanel);


        }
        setSelectedUnit(null);
    }


    /////////////////////////////////////////////////////////////////
    // Set mouse location
    /////////////////////////////////////////////////////////////////
    public void setMouseLoc(int x, int y) {
        mouseX = x;
        mouseY = y;
    }


    /////////////////////////////////////////////////////////////////
    // Add an animation
    /////////////////////////////////////////////////////////////////
    public void add(Animation animation) {
        int animDelay = 0;
        if (!(BattleField.getX(animation.getLocation()) < 0 || BattleField.getX(animation.getLocation()) > Constants.BOARD_SIZE ||
                BattleField.getY(animation.getLocation()) < 0 || BattleField.getY(animation.getLocation()) > Constants.BOARD_SIZE))
            animDelay = animationDirectionGrid[BattleField.getX(animation.getLocation())][BattleField.getY(animation.getLocation())]; //animations.size() * 10;
        animation.setDirection((byte) animDelay);
        animationBegin(animation);
        animations.add(animation);
    }


    /////////////////////////////////////////////////////////////////
    // Add an animation
    /////////////////////////////////////////////////////////////////
    public void addPre(Animation animation) { //preAnimations.add(animation);
        preAnimations.insertElementAt(animation, 0);
    }


    /////////////////////////////////////////////////////////////////
    // Add an animation to front
    /////////////////////////////////////////////////////////////////
    public void insert(Animation animation) {
        animations.insertElementAt(animation, 0);
    }


    /////////////////////////////////////////////////////////////////
    // Signal that an animation is finished
    /////////////////////////////////////////////////////////////////
    public void animationDone(Animation animation) {
        // Return if out of the field
        if (BattleField.getX(animation.getLocation()) < 0 || BattleField.getX(animation.getLocation()) > Constants.BOARD_SIZE ||
                BattleField.getY(animation.getLocation()) < 0 || BattleField.getY(animation.getLocation()) > Constants.BOARD_SIZE)
            return;

        // Make sure damage animations at this location can use this direction again
        if (animation instanceof AnimationDamage)
            animationDirectionGrid[BattleField.getX(animation.getLocation())][BattleField.getY(animation.getLocation())] -= 1;

        // Remove the animation
        animations.remove(animation);
        preAnimations.remove(animation);
    }


    /////////////////////////////////////////////////////////////////
    // Signal that an animation has started
    /////////////////////////////////////////////////////////////////
    public void animationBegin(Animation animation) {
        // Return if out of the field
        if (BattleField.getX(animation.getLocation()) < 0 || BattleField.getX(animation.getLocation()) > Constants.BOARD_SIZE ||
                BattleField.getY(animation.getLocation()) < 0 || BattleField.getY(animation.getLocation()) > Constants.BOARD_SIZE)
            return;

        // Make sure next damage animation at this location is sent in another direction so it is still visible
        if (animation instanceof AnimationDamage)
            animationDirectionGrid[BattleField.getX(animation.getLocation())][BattleField.getY(animation.getLocation())] += 1;
    }


    /////////////////////////////////////////////////////////////////
    // Set opponent
    /////////////////////////////////////////////////////////////////
    public void setOpponent(int rating, String name) {
        enemyRating = rating;
        enemyName = name;
    }


    /////////////////////////////////////////////////////////////////
    // Set the timer
    /////////////////////////////////////////////////////////////////
    public void setTimer(int count) {
        if (timer != null) timer.end();
        timer = new ClientTimer(count);
    }


    /////////////////////////////////////////////////////////////////
    // Return remaining supply of a unit
    /////////////////////////////////////////////////////////////////
    public int getRemaining(Unit unit) {
        return Client.getUnits()[unit.getID()] -
                getArmy().getUnits()[unit.getID()];
    }


    /////////////////////////////////////////////////////////////////
    // Clicks
    /////////////////////////////////////////////////////////////////
    public void click() {
        clicked = true;
    }

    public void castleChange() {
        castleChanged = true;
    }

    public boolean castleChanged() {
        boolean tmpCastle = castleChanged;
        castleChanged = false;
        return tmpCastle;

    }

    public boolean clicked() {
        return clicked;
    }

    public void unclick() {
        clicked = false;
    }


    /////////////////////////////////////////////////////////////////
    // Recruit a new unit
    /////////////////////////////////////////////////////////////////
    public void recruit(short newUnitID) {
        Unit newUnit = Unit.getUnit(newUnitID, new Castle());
        getHiddenUnitStats().initialize(newUnit);
        newRecruit = true;
        gainGold = 0;
    }


    /////////////////////////////////////////////////////////////////
    // Noob
    /////////////////////////////////////////////////////////////////
    public void noob() {
        rosterPanel = new RosterPanel(true);
    }


    /////////////////////////////////////////////////////////////////
    // Gain gold
    /////////////////////////////////////////////////////////////////
    public void gainGold(long newGold) {
        gainGold = newGold;
        newRecruit = false;
    }


    /////////////////////////////////////////////////////////////////
    // end my turn
    /////////////////////////////////////////////////////////////////
    public void endTurn() {
        Client.getGameData().setSelectedUnit(null);
        //Client.getGameData().getMyCastle().refresh(Unit.TEAM_1);
        //Client.getGameData().setCastlePlaying(Client.getGameData().getEnemyCastle());
        Client.getGameData().setCastlePlaying(null);
        Client.getNetManager().sendAction(Action.END_TURN, (byte) 0, (byte) 0);
    }

    /////////////////////////////////////////////////////////////////
    // redraw army
    /////////////////////////////////////////////////////////////////
    public void redraw() {
        Client.getNetManager().sendAction(Action.REDRAW_ARMY, (byte) 0, (byte) 0);
    }

    /////////////////////////////////////////////////////////////////
// Offset the endPanel to the bottom right (when repick button is no longer there)
/////////////////////////////////////////////////////////////////
    public void setPanelLocation() {
        endPanel.setLocation(endPanel.getX(), endPanel.getY() + 50);
    }

    /////////////////////////////////////////////////////////////////
// Disable repick button for Player 1
/////////////////////////////////////////////////////////////////
    public void DisableRepickP1() {
        disableRepickP1 = true;
    }

    /////////////////////////////////////////////////////////////////
// Disable repick button for Player 1
/////////////////////////////////////////////////////////////////
    public void DisableRepickP2() {
        disableRepickP2 = true;
    }

    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public BackBoard getMainBoard() {
        return mainBoard;
    }

    public TutorialBoard getTutorialBoard() {
        return tutorialBoard;
    }

    public SideBoard getSideBoard() {
        return sideBoard;
    }

    public TextBoard getTextBoard() {
        return textBoard;
    }

    public StatPanel getStatPanel() {
        return statPanel;
    }

    public CastlePanel getCastlePanel() {
        return castlePanel;
    }

    public Castle getArmy() {
        return army;
    }

    public Castle getMyCastle() {
        return myCastle;
    }

    public Castle getEnemyCastle() {
        return enemyCastle;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public Unit getSelectedUnit() {
        return selectedUnit;
    }

    public Action getSelectedAction() {
        return selectedAction;
    }

    public int getState() {
        return state;
    }

    public Unit getDeployingUnit() {
        return deployingUnit;
    }

    public Castle getCastlePlaying() {
        return castlePlaying;
    }

    public int getMouseX() {
        return mouseX;
    }

    public int getMouseY() {
        return mouseY;
    }

    public Vector<Animation> getAnimations() {
        return animations;
    }

    public Vector<Animation> getPreAnimations() {
        return preAnimations;
    }

    public int getEnemyRating() {
        return enemyRating;
    }

    public String getEnemyName() {
        return enemyName;
    }

    public boolean drawOffered() {
        return offeredDraw;
    }

    public boolean getMyDraw() {
        return myDraw;
    }

    public ClientTimer getTimer() {
        return timer;
    }

    public boolean myTurn() {
        return getCastlePlaying() == getMyCastle();
    }

    public HiddenUnitStats getHiddenUnitStats() {
        return hiddenUnitStats;
    }

    public boolean playing() {
        return playing;
    }

    public boolean newRecruit() {
        return newRecruit;
    }

    public long gainGold() {
        return gainGold;
    }

    public int getGameType() {
        return gameType;
    }

    public boolean getDisableRepickP1() {
        return disableRepickP1;
    }

    public boolean getDisableRepickP2() {
        return disableRepickP2;
    }

    public int getCurrPlayer() {
        return currPlayer;
    }

    public PlayerPanel getPlayerPanel() {
        return playerPanel;
    }

    public TeamLoadingPanel getTeamLoadingPanel() {
        return teamLoadingPanel;
    }


    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void setPlaying(boolean newState) {
        playing = newState;
    }

    public void setBattleField(BattleField newBattleField) {
        battleField = newBattleField;
    }

    public void setSelectedAction(Action newAction) {
        selectedAction = newAction;
    }

    public void setState(int newState) {
        state = newState;
    }

    public void offerDraw() {
        offeredDraw = true;
    }

    public void setMyDraw() {
        myDraw = true;
    }

    public void setArmy(Castle newUnits) {
        army = newUnits;
    }

    public void setGameType(int GameType) {
        gameType = GameType;
    }

    public void setCurrPlayer(int player) {
        currPlayer = player;
    }

    public void setPlayerPanel(PlayerPanel newPlayerPanel) {
        playerPanel = newPlayerPanel;
    }

}
