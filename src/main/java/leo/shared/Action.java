///////////////////////////////////////////////////////////////////////
// Name: Action
// Desc: An interface for actions
// Date: 5/10/2003 - Created [Gabe Jones]
//       10/19/2010 - Added Mirrored random constants [Tony Schwartz]
//       11/5/2010 - Added tutorial support [Dan Healy]
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports

import java.util.Vector;


public interface Action {

    /////////////////////////////////////////////////////////////////
    // Action types
    /////////////////////////////////////////////////////////////////
    short ATTACK = 0;
    short MOVE = 1;
    short SPELL = 2;
    short SKILL = 3;
    short OTHER = 4;

    String[] TYPE_NAME =
            {Strings.ACTION_1,  // attack
                    Strings.ACTION_2,  // move
                    Strings.ACTION_3,  // spell
                    Strings.ACTION_4,        // skill
                    Strings.ACTION_5  // other
            };

    int[] TYPE_COLOR =
            { /*ClientImages.IMG_RED,  // attack
  ClientImages.IMG_BLUE,  // move
  ClientImages.IMG_PURPLE, // spell
  ClientImages.IMG_GREEN,  // skill
  ClientImages.IMG_BLACK  // other*/
                    Constants.IMG_RED,  // attack
                    Constants.IMG_BLUE,  // move
                    Constants.IMG_PURPLE, // spell
                    Constants.IMG_GREEN,  // skill
                    Constants.IMG_BLACK  // other
            };

    /////////////////////////////////////////////////////////////////
    // Grow characteristics
    /////////////////////////////////////////////////////////////////
    short GROW_TOXIC = 0;
    short GROW_EVASIVE = 1;
    short GROW_RESILIENT = 2;
    short GROW_LONGSHANK = 3;
    short GROW_MIGHTY = 4;
    short GROW_CLOCKWORK = 5;
    short GROW_VAMPIRIC = 6;
    short GROW_CUNNING = 7;
    short GROW_EPIC = 8;
    short GROW_ARCANE = 9;
    short GROW_ASCENDANT = 10;
    short GROW_GUARDIAN = 11;
    short GROW_VIGILANT = 12;
    short GROW_ZEALOUS = 13;
    short GROW_RAMPAGING = 14;
    short GROW_RUTHLESS = 15;
    short GROW_ENRAGED = 16;

    short GROW_AI_COUNT = 17;

    // grows used for relics
    short GROW_RELIC_CLOCKWORK = 17;
    short GROW_RELIC_EVASION = 18;
    short GROW_RELIC_FLYING = 19;
    short GROW_RELIC_STUNNING = 20;
    short GROW_RELIC_VAMPIRE = 21;
    short GROW_RELIC_HEAL_MOVE = 22;
    short GROW_RELIC_GIFT = 23;          //-
    short GROW_RELIC_RESET = 24;         //-
    short GROW_RELIC_SPELL_BLOCK = 25;
    short GROW_RELIC_PARRY = 26;
    short GROW_RELIC_EXPLODE = 27;
    short GROW_RELIC_BANISH = 28;

    short GROW_RELIC_COUNT = 12;
    short GROW_COUNT = 29;

    // all grows below grow_count are used internally
    short GROW_POSSESSED = 100;
    short GROW_SKINWALKING = 101;
    short GROW_POISONED = 102;


    int[] GROW_COST =
            {200, // toxic
                    250, // evasive
                    200, // resilient
                    200, // longshank
                    200, // mighty
                    200, // clockwork
                    200, // vampiric
                    300, // cunning
                    300, // epic
                    250, // arcane
                    350, // ascendant
                    200, // guardian
                    200, // vigilant
                    250, // zealous
                    250, // rampaging
                    250, // ruthless
                    250, // enraged

                    // Not Powerups
                    /*000, // relic clockwork
                    000, // relic evasion
                    000, // relic flying
                    000, // relic stunning
                    000, // relic vampire
                    000, // relic healmove
                    000, // relic gift
                    000, // relic reset
                    000, // relic spellblock
                    000, // relic parry
                    000, // relic banish
                    000  // relic explode*/
            };

    /////////////////////////////////////////////////////////////////
    // Effects
    /////////////////////////////////////////////////////////////////
    int EFFECT_FADE = 0;
    int EFFECT_FADE_IN = 1;
    int EFFECT_BANISH = 2;
    int EFFECT_WALL = 3;
    int EFFECT_ANGEL = 4;
    int EFFECT_FIZZLE = 5;
    int EFFECT_TRUCE = 6;
    int EFFECT_BUILD = 7;


    /////////////////////////////////////////////////////////////////
    // Game Types
    /////////////////////////////////////////////////////////////////
    int GAME_NONE = 0;
    int GAME_CONSTRUCTED = 2;
    int GAME_RANDOM = 3;
    int GAME_MIRRORED_RANDOM = 4;
    int GAME_PRACTICE = 5;
    int GAME_COOPERATIVE = 6;
    int GAME_TEAM = 7;
    int GAME_REMATCH_RANDOM = 8;


    /////////////////////////////////////////////////////////////////
    // Attack types
    /////////////////////////////////////////////////////////////////
    int ATTACK_ARROW = 0;
    int ATTACK_MELEE = 1;
    int ATTACK_OTHER = 2;
    int ATTACK_CHANNEL_BOLT = 3;
    int ATTACK_CHANNEL_BLAST = 4;
    int ATTACK_STONE = 5;
    int ATTACK_SPEAR = 6;
    int ATTACK_MAGIC_BALL = 7;
    int ATTACK_XBOW = 8;
    int ATTACK_WISP = 9;
    int ATTACK_BLOWGUN = 10;
    int ATTACK_SPIRIT = 11;
    int ATTACK_BEAM = 12;
    int ATTACK_NONE = 13;
    int ATTACK_THROW = 14;
    int ATTACK_THROW_SPEAR = 15;
    int ATTACK_RELIC = 16;


    /////////////////////////////////////////////////////////////////
    // Area effect
    /////////////////////////////////////////////////////////////////
    int AOE_DETONATE = 0;
    int AOE_EXPLOSION = 1;


    /////////////////////////////////////////////////////////////////
    // Game Types
    /////////////////////////////////////////////////////////////////
    //public static final short SET_CONSTRUCTED  = 32;
    short SET_RANDOM = 33;  // Only for offsetting the endTurn panel for Redraw button
    //public static final short SET_MIRRORED_RANDOM   = 34;


    /////////////////////////////////////////////////////////////////
    // Network Constants
    /////////////////////////////////////////////////////////////////

    // in game actions, 30 - 39
    short OFFER_DRAW = 35;
    short SURRENDER = 36;
    short DEPLOY = 37;
    short END_TURN = 38;
    short REDRAW_ARMY = 39;

    // Constants used to implement the redraw function, 40 - 44
    short DISABLE_REPICK_P1 = 40;
    short DISABLE_REPICK_P2 = 41;
    short P1_TURN = 42;
    short P2_TURN = 43;
    short MOVE_PANEL = 44;

    // Other server-client communications, 50+
    short CHAT_CRU = 53;
    short CHAT_LEG = 54;
    short CHAT_CRU_LEG = 55;
    short CHATTING = 56;
    short EDIT_ARMY = 57;
    short START_TEAM_GAME = 58;
    short CLEAR_TEAM = 59;
    short SELECT_TEAM = 60;
    short TIME_OUT = 61;
    short CLEAR_CASTLE = 62;
    short CHOOSE_UNIT = 63;
    short NOOB = 64;
    short TEAM = 68;
    short DISCONNECT = 69;
    short GROW = 70;
    short RESYNCH = 71;
    short RESYNCH_READY = 72;
    short END_RESYNCH = 73;
    short NEW_GAME = 74;
    short GET_UPDATE = 75;
    short NEW_ARMY = 76;
    short NEW_ARMY_UNIT = 77;
    short GAME_LOOP = 78;
    short TOP_SCORES = 79;
    short NEED_EMAIL = 80;
    short NO_REFERRAL = 81;
    short REFER_FRIEND = 82;
    short COOPERATIVE = 83;
    short ALLY_LEFT = 84;
    short START_TURN = 85;
    short START_TURN_ALLY = 86;
    short START_TURN_ENEMY = 87;
    short START_TURN_ENEMY_ALLY = 65;
    short REFRESH = 88;
    short REFRESH_ALLY = 89;
    short REFRESH_ENEMY = 90;
    short REFRESH_ENEMY_ALLY = 66;
    short DEPLOY_ALLY = 91;
    short NEW_ALLY = 92;
    short RECRUIT_UNIT = 93;
    short ACCEPT_KEY = 94;
    short REJECT_KEY = 95;
    short REGISTER = 96;
    short REJECT_PASSWORD = 97;
    short NEW_PASSWORD = 98;
    short LOAD_ARCHIVE = 99;
    short SAVE_ARCHIVE = 100;
    short SEND_ARCHIVE = 101;
    short PRACTICE = 102;
    short AI = 103;
    short SELL_UNIT = 104;
    short BUY_UNIT = 105;
    short JOIN_DUEL = 106;
    short JOIN_MIRRORED_DUEL = 107;
    short DEPLOY_ENEMY = 108;
    short DEPLOY_ENEMY_ALLY = 67;
    short NEW_UNIT = 109;
    short START_GAME = 110;
    short ENEMY_LEFT = 111;
    short NEW_CASTLE = 112;
    short QUIT = 113;
    short CANCEL = 114;
    short YES = 115;
    short NO = 116;
    short IDLE = 117;
    short COUNT = 118;
    short OPPONENT = 119;
    short SET_ARMY = 120;
    short GET_ARMY = 121;
    short END_ARMY = 122;
    short SET_RATING = 123;
    short JOIN = 124;
    short UNLOCK_UNITS = 125;
    short SET_GOLD = 126;
    short NOTHING = 127;
    short SET_RANK = -20;
    short SET_WINS = -21;
    short SET_LOSSES = -22;

    /////////////////////////////////////////////////////////////////
    // Chat actions
    /////////////////////////////////////////////////////////////////
    short CHAT_ADD_PLAYER = 1;
    short CHAT_REMOVE_PLAYER = 2;
    short CHAT_WHISPER = 3;
    short CHAT_BROADCAST = 4;
    short CHAT_SYSTEM = 5;
    short CHAT_UPDATE_RATING = 6;
    short CHAT_CHATTING = 7;
    short CHAT_INVITE = 8;
    short CHAT_ACCEPT = 9;
    short CHAT_DISABLE = 10;
    short CHAT_MESSAGE = 11;
    short CHAT_TUTORIAL_MESSAGE = 12;
    short CHAT_TUTORIAL_CLOSED = 31;
    short ALLOW_REMATCH = 32;


    //public static final short CHAT_EDITING = 13;
    short CHAT_FIGHTING_SINGLE = 13;
    short CHAT_FIGHTING_CONS = 14;
    short CHAT_FIGHTING_RAND = 15;
    short CHAT_FIGHTING_MIRR_RAND = 16;
    short CHAT_FIGHTING_COOP = 17;
    short CHAT_FIGHTING_2V2 = 18;

    short CHAT_WAITING_CONS = 19;
    short CHAT_WAITING_RAND = 20;
    short CHAT_WAITING_COOP = 21;
    short CHAT_WAITING_2V2 = 22;

    short CHAT_EDIT = 23;


    /////////////////////////////////////////////////////////////////
    // Perform the action on the client
    /////////////////////////////////////////////////////////////////
    String perform(short target);


    /////////////////////////////////////////////////////////////////
    // Validate the action
    /////////////////////////////////////////////////////////////////
    boolean validate(short target);


    /////////////////////////////////////////////////////////////////
    // Refresh the action
    /////////////////////////////////////////////////////////////////
    void refresh();


    /////////////////////////////////////////////////////////////////
    // Start the turn
    /////////////////////////////////////////////////////////////////
    void startTurn();


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    Vector<Short> getTargets();


    /////////////////////////////////////////////////////////////////
    // Get the targets
    /////////////////////////////////////////////////////////////////
    Vector<Short> getClientTargets();


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    String getName();

    String getDescription();

    String getRangeDescription();

    String getCostDescription();

    short getRemaining();

    short getMax();

    short getCost();

    short getRange();

    short getTargetType();

    Unit getOwner();

    Unit getHiddenUnit();

    boolean passive();

    short getType();

    String getDetail();
}
