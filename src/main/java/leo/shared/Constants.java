///////////////////////////////////////////////////////////////////////
// Name: Constants
// Desc: The image loader
// Date: 2/13/2003 - Gabe Jones
//   9/13/2010 - Fletcher Cole & Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports
//import leo.client.Client;
//import leo.client.Sound;
//import leo.client.OggClip;

import leo.client.Patches;

import javax.swing.text.html.Option;
import java.nio.file.Paths;
import java.util.Optional;

public class Constants {

    public static final String USER_HOME = System.getProperty("user.home");

    public static final String LOCAL_USER_NAME = Optional.ofNullable(System.getProperty("user.name")).orElse("local");

    public static final String LOCAL_USER_PASSWORD = "localpwd";

    public static final String XDG_DATA_HOME = Optional
            .ofNullable(System.getenv("XDG_DATA_HOME"))
            .orElse(Paths.get(USER_HOME, ".local", "share").toString());

    public static final String LOCAL_DIR = Paths.get(XDG_DATA_HOME, "zatikon").toString();

    public static final String STANDALONE_ARG = "standalone";

    public static final String SERVER_READY_MESSAGE = "-- Server ready --";

    public static final String SERVER_READY_PID_FILE_BASENAME = "server.ready.pid";

    public static final String READY_PID_FILE = Paths.get(LOCAL_DIR, SERVER_READY_PID_FILE_BASENAME).toString();

    public static final long GOLD_TIMER = 90000;

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static final String ART_LOC = "art/";
    public static final String ART_JAR = "/art.jar";


    public static final String SOUND_LOC = "sound/";
    public static final String SOUND_JAR = "/sound.jar";


    /////////////////////////////////////////////////////////////////
    // THESE USED TO BE IN "leo/client/Client.java"
    /////////////////////////////////////////////////////////////////
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final int WINDOW_WIDTH = 1024 + Patches.EXTRA_WIDTH;
    public static final int WINDOW_HEIGHT = 600 + Patches.EXTRA_HEIGHT;
    public static final int BOARD_SIZE = 11;
    public static final int SQUARE_SIZE = SCREEN_HEIGHT / BOARD_SIZE;
    public static final int OFFSET = (SCREEN_HEIGHT % BOARD_SIZE) / 2;
    public static final int STATE_CASTLE = 0;
    public static final int STATE_UNIT = 1;
    public static final int STEP_SPEED = 9;
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Game Types
    /////////////////////////////////////////////////////////////////
    public static final int CONSTRUCTED = 2;
    public static final int RANDOM = 3;
    public static final int MIRRORED_RANDOM = 4;

    /////////////////////////////////////////////////////////////////
    // Game Parameters
    /////////////////////////////////////////////////////////////////
    public static final int MAX_ARMY_SIZE = 1000;
    public static final int MAX_COMMANDS = 5;

    // Misc images
    public static final int IMG_SPLAT = 0;
    public static final int IMG_MIRACLE = 1;
    public static final int IMG_DEATH = 2;
    public static final int IMG_FIREBALL = 3;
    public static final int IMG_BIG_POOF = 4;
    public static final int IMG_DISABLE = 5;

    //Castles
    public static final int IMG_MY_CASTLE = 6;
    public static final int IMG_ENEMY_CASTLE = 7;

    // Units
    public static final int IMG_FOOTMAN = 8;
    public static final int IMG_FOOTMAN_ENEMY = 9;
    public static final int IMG_BOWMAN = 10;
    public static final int IMG_BOWMAN_ENEMY = 11;
    public static final int IMG_OBOWMAN = 12;
    public static final int IMG_OBOWMAN_ENEMY = 13;
    public static final int IMG_CAVALRY = 14;
    public static final int IMG_CAVALRY_ENEMY = 15;
    public static final int IMG_ARCHER = 16;
    public static final int IMG_ARCHER_ENEMY = 17;
    public static final int IMG_PIKEMAN = 18;
    public static final int IMG_PIKEMAN_ENEMY = 19;
    public static final int IMG_KNIGHT = 20;
    public static final int IMG_KNIGHT_ENEMY = 21;
    public static final int IMG_RANGER_RANGED = 22;
    public static final int IMG_RANGER_RANGED_ENEMY = 23;
    public static final int IMG_RANGER_WOLF_RANGED = 24;
    public static final int IMG_RANGER_WOLF_RANGED_ENEMY = 25;
    public static final int IMG_WOLF = 26;
    public static final int IMG_WOLF_ENEMY = 27;
    public static final int IMG_SUMMONER = 28;
    public static final int IMG_SUMMONER_ENEMY = 29;
    public static final int IMG_IMP = 30;
    public static final int IMG_IMP_ENEMY = 31;
    public static final int IMG_DEMON = 32;
    public static final int IMG_DEMON_ENEMY = 33;
    public static final int IMG_PRIEST = 34;
    public static final int IMG_PRIEST_ENEMY = 35;
    public static final int IMG_ENCHANTER = 36;
    public static final int IMG_ENCHANTER_ENEMY = 37;
    public static final int IMG_TEMPLAR = 38;
    public static final int IMG_TEMPLAR_ENEMY = 39;
    public static final int IMG_WARRIOR = 40;
    public static final int IMG_WARRIOR_ENEMY = 41;
    public static final int IMG_RIDER = 42;
    public static final int IMG_RIDER_ENEMY = 43;
    public static final int IMG_HEALER = 44;
    public static final int IMG_HEALER_ENEMY = 45;
    public static final int IMG_WIZARD = 46;
    public static final int IMG_WIZARD_ENEMY = 47;
    public static final int IMG_SCOUT = 48;
    public static final int IMG_SCOUT_ENEMY = 49;
    public static final int IMG_ASSASSIN = 50;
    public static final int IMG_ASSASSIN_ENEMY = 51;
    public static final int IMG_TACTICIAN = 52;
    public static final int IMG_TACTICIAN_ENEMY = 53;
    public static final int IMG_GENERAL = 54;
    public static final int IMG_GENERAL_ENEMY = 55;
    public static final int IMG_STRATEGIST = 56;
    public static final int IMG_STRATEGIST_ENEMY = 57;
    public static final int IMG_WALL = 58;
    public static final int IMG_WALL_ENEMY = 59;
    public static final int IMG_CATAPULT = 60;
    public static final int IMG_CATAPULT_ENEMY = 61;
    public static final int IMG_BALLISTA = 62;
    public static final int IMG_BALLISTA_ENEMY = 63;
    public static final int IMG_NECROMANCER = 64;
    public static final int IMG_NECROMANCER_ENEMY = 65;
    public static final int IMG_LICH = 66;
    public static final int IMG_LICH_ENEMY = 67;
    public static final int IMG_SKELETON = 68;
    public static final int IMG_SKELETON_ENEMY = 69;
    public static final int IMG_ZOMBIE = 70;
    public static final int IMG_ZOMBIE_ENEMY = 71;
    public static final int IMG_SERGEANT = 72;
    public static final int IMG_SERGEANT_ENEMY = 73;
    public static final int IMG_ABJURER = 74;
    public static final int IMG_ABJURER_ENEMY = 75;
    public static final int IMG_SEAL = 76;
    public static final int IMG_SEAL_ENEMY = 77;
    public static final int IMG_WARLOCK = 78;
    public static final int IMG_WARLOCK_ENEMY = 79;
    public static final int IMG_CROSSBOWMAN = 80;
    public static final int IMG_CROSSBOWMAN_ENEMY = 81;
    public static final int IMG_UNCROSSBOWMAN = 82;
    public static final int IMG_UNCROSSBOWMAN_ENEMY = 83;
    public static final int IMG_DRAGON = 84;
    public static final int IMG_DRAGON_ENEMY = 85;
    public static final int IMG_DRACOLICH = 86;
    public static final int IMG_DRACOLICH_ENEMY = 87;
    public static final int IMG_HYDRA = 88;
    public static final int IMG_HYDRA_ENEMY = 89;
    public static final int IMG_HYDRA_5 = 90;
    public static final int IMG_HYDRA_5_ENEMY = 91;
    public static final int IMG_HYDRA_4 = 92;
    public static final int IMG_HYDRA_4_ENEMY = 93;
    public static final int IMG_HYDRA_3 = 94;
    public static final int IMG_HYDRA_3_ENEMY = 95;
    public static final int IMG_HYDRA_2 = 96;
    public static final int IMG_HYDRA_2_ENEMY = 97;
    public static final int IMG_HYDRA_1 = 98;
    public static final int IMG_HYDRA_1_ENEMY = 99;
    public static final int IMG_TOWER = 100;
    public static final int IMG_TOWER_ENEMY = 101;
    public static final int IMG_COMMAND_POST = 102;
    public static final int IMG_COMMAND_POST_ENEMY = 103;
    public static final int IMG_BARRACKS = 104;
    public static final int IMG_BARRACKS_ENEMY = 105;
    public static final int IMG_SOLDIER = 106;
    public static final int IMG_SOLDIER_ENEMY = 107;
    public static final int IMG_DRUID = 108;
    public static final int IMG_DRUID_ENEMY = 109;
    public static final int IMG_CHANNELER = 110;
    public static final int IMG_CHANNELER_ENEMY = 111;
    public static final int IMG_LYCANTHROPE = 112;
    public static final int IMG_LYCANTHROPE_ENEMY = 113;
    public static final int IMG_WEREWOLF = 114;
    public static final int IMG_WEREWOLF_ENEMY = 115;
    public static final int IMG_LYCANWOLF = 116;
    public static final int IMG_LYCANWOLF_ENEMY = 117;
    public static final int IMG_MOUNTED_ARCHER = 118;
    public static final int IMG_MOUNTED_ARCHER_ENEMY = 119;
    public static final int IMG_GEOMANCER = 120;
    public static final int IMG_GEOMANCER_ENEMY = 121;
    public static final int IMG_ROCK = 122;
    public static final int IMG_ROCK_ENEMY = 123;
    public static final int IMG_SWORDSMAN = 124;
    public static final int IMG_SWORDSMAN_ENEMY = 125;
    public static final int IMG_WITCH = 126;
    public static final int IMG_WITCH_ENEMY = 127;
    public static final int IMG_TOAD = 128;
    public static final int IMG_TOAD_ENEMY = 129;
    public static final int IMG_SHIELD_MAIDEN = 130;
    public static final int IMG_SHIELD_MAIDEN_ENEMY = 131;
    public static final int IMG_MAGUS = 132;
    public static final int IMG_MAGUS_ENEMY = 133;
    public static final int IMG_SPIRIT = 134;
    public static final int IMG_SPIRIT_ENEMY = 135;
    public static final int IMG_WILL_O_THE_WISPS = 136;
    public static final int IMG_WILL_O_THE_WISPS_ENEMY = 137;
    public static final int IMG_GOLEM = 138;
    public static final int IMG_GOLEM_ENEMY = 139;
    public static final int IMG_ARMORY = 140;
    public static final int IMG_ARMORY_ENEMY = 141;
    public static final int IMG_SERPENT = 142;
    public static final int IMG_SERPENT_ENEMY = 143;
    public static final int IMG_FIRE_ARCHER = 144;
    public static final int IMG_FIRE_ARCHER_ENEMY = 145;
    public static final int IMG_MIMIC = 146;
    public static final int IMG_MIMIC_ENEMY = 147;
    public static final int IMG_PALADIN = 148;
    public static final int IMG_PALADIN_ENEMY = 149;
    public static final int IMG_SHAMAN = 150;
    public static final int IMG_SHAMAN_ENEMY = 151;
    public static final int IMG_MARTYR = 152;
    public static final int IMG_MARTYR_ENEMY = 153;
    public static final int IMG_ROGUE = 154;
    public static final int IMG_ROGUE_ENEMY = 155;
    public static final int IMG_DIABOLIST = 156;
    public static final int IMG_DIABOLIST_ENEMY = 157;
    public static final int IMG_GHOST = 158;
    public static final int IMG_GHOST_ENEMY = 159;
    public static final int IMG_TEMPLAR_AURA = 160;
    public static final int IMG_TEMPLAR_AURA_ENEMY = 161;
    public static final int IMG_GATE_GUARD = 162;
    public static final int IMG_GATE_GUARD_ENEMY = 163;
    public static final int IMG_FEATHERED_SERPENT = 164;
    public static final int IMG_FEATHERED_SERPENT_ENEMY = 165;
    public static final int IMG_BERSERKER = 166;
    public static final int IMG_BERSERKER_ENEMY = 167;
    public static final int IMG_DEAD_BERSERKER = 168;
    public static final int IMG_DEAD_BERSERKER_ENEMY = 169;
    public static final int IMG_ARTIFICER = 170;
    public static final int IMG_ARTIFICER_ENEMY = 171;
    public static final int IMG_CHANGELING = 172;
    public static final int IMG_CHANGELING_ENEMY = 173;
    public static final int IMG_DOPPELGANGER = 174;
    public static final int IMG_DOPPELGANGER_ENEMY = 175;
    public static final int IMG_SKINWALKER = 176;
    public static final int IMG_SKINWALKER_ENEMY = 177;
    public static final int IMG_ACOLYTE = 178;
    public static final int IMG_ACOLYTE_ENEMY = 179;
    public static final int IMG_AXEMAN = 180;
    public static final int IMG_AXEMAN_ENEMY = 181;
    public static final int IMG_MOURNER = 182;
    public static final int IMG_MOURNER_ENEMY = 183;
    public static final int IMG_HERETIC = 184;
    public static final int IMG_HERETIC_ENEMY = 185;
    public static final int IMG_WAR_ELEPHANT = 186;
    public static final int IMG_WAR_ELEPHANT_ENEMY = 187;
    public static final int IMG_FANATIC = 188;
    public static final int IMG_FANATIC_ENEMY = 189;
    public static final int IMG_DISMOUNTED_KNIGHT = 190;
    public static final int IMG_DISMOUNTED_KNIGHT_ENEMY = 191;
    public static final int IMG_BEAR = 192;
    public static final int IMG_BEAR_ENEMY = 193;
    public static final int IMG_QUARTERMASTER = 194;
    public static final int IMG_QUARTERMASTER_ENEMY = 195;
    public static final int IMG_MASON = 196;
    public static final int IMG_MASON_ENEMY = 197;
    public static final int IMG_WALL_MASON = 198;
    public static final int IMG_WALL_MASON_ENEMY = 199;
    public static final int IMG_DOUBLE_DOP = 200;
    public static final int IMG_DOUBLE_DOP_ENEMY = 201;
    public static final int IMG_CONFESSOR = 202;
    public static final int IMG_CONFESSOR_ENEMY = 203;
    public static final int IMG_POSSESSED = 204;
    public static final int IMG_POSSESSED_ENEMY = 205;
    public static final int IMG_BARBARIAN = 206;
    public static final int IMG_BARBARIAN_ENEMY = 207;
    public static final int IMG_ALCHEMIST = 208;
    public static final int IMG_ALCHEMIST_ENEMY = 209;
    public static final int IMG_BOUNTY_HUNTER = 210;
    public static final int IMG_BOUNTY_HUNTER_ENEMY = 211;
    public static final int IMG_SHIELD_BEARER = 212;
    public static final int IMG_SHIELD_BEARER_ENEMY = 213;
    public static final int IMG_CHIEFTAIN = 214;
    public static final int IMG_CHIEFTAIN_ENEMY = 215;
    public static final int IMG_LANCER = 216;
    public static final int IMG_LANCER_ENEMY = 217;
    public static final int IMG_ARCHANGEL = 218;
    public static final int IMG_ARCHANGEL_ENEMY = 219;
    public static final int IMG_CONJURER = 220;
    public static final int IMG_CONJURER_ENEMY = 221;
    public static final int IMG_PORTAL = 222;
    public static final int IMG_PORTAL_ENEMY = 223;
    public static final int IMG_GATE = 224;
    public static final int IMG_GATE_ENEMY = 225;
    public static final int IMG_DIPLOMAT = 226;
    public static final int IMG_DIPLOMAT_ENEMY = 227;
    public static final int IMG_LONGBOWMAN = 228;
    public static final int IMG_LONGBOWMAN_ENEMY = 229;
    public static final int IMG_SYCOPHANT = 230;
    public static final int IMG_SYCOPHANT_ENEMY = 231;
    public static final int IMG_WYVERN = 232;
    public static final int IMG_WYVERN_ENEMY = 233;
    public static final int IMG_EGG = 234;
    public static final int IMG_EGG_ENEMY = 235;
    public static final int IMG_CAPTAIN = 236;
    public static final int IMG_CAPTAIN_ENEMY = 237;
    public static final int IMG_ABBEY = 238;
    public static final int IMG_ABBEY_ENEMY = 239;
    public static final int IMG_SUPPLICANT = 240;
    public static final int IMG_SUPPLICANT_ENEMY = 241;

    /////////////////////////////////////////////////////////
    // 9/21/10 Update: Constants are all in numerical order
    ////////////////////////////////////////////////////////

    // some new buttons
    public static final int IMG_MUSIC = 242;
    public static final int IMG_MUSIC_HIGHLIGHT = 243;
    public static final int IMG_MUSIC_OFF = 244;
    public static final int IMG_FORUM = 245;
    public static final int IMG_FORUM_HIGHLIGHT = 246;
    public static final int IMG_BLOG = 247;
    public static final int IMG_BLOG_HIGHLIGHT = 248;
    public static final int IMG_GUIDE = 249;
    public static final int IMG_GUIDE_HIGHLIGHT = 250;

    // white flag
    public static final int IMG_WHITE_FLAG = 251;

    // Projectiles - Spear 2... randomly set to 252
    public static final int IMG_SPEAR_2 = 252;

    // 2v2 test
    public static final int IMG_TEAM = 253;
    public static final int IMG_TEAM_HIGHLIGHT = 254;

    // spotlight
    public static final int IMG_SPOTLIGHT = 255;
    public static final int IMG_BLACKLIGHT = 256;

    // the ad
    public static final int IMG_AD = 257;

    // border for bosses
    public static final int IMG_BORDER = 258;

    // cooperative: other buttons
    public static final int IMG_MUTE = 259;
    public static final int IMG_MUTE_HIGHLIGHT = 260;
    public static final int IMG_MUTED = 261;
    public static final int IMG_REFER_FRIEND = 262;
    public static final int IMG_REFER_FRIEND_HIGHLIGHT = 263;
    public static final int IMG_SCORES = 264;
    public static final int IMG_SCORES_HIGHLIGHT = 265;

    // edit army
    public static final int IMG_EDIT_STAT_PANEL = 266;
    public static final int IMG_EDIT_CATEGORY_PANEL = 267;
    public static final int IMG_EDIT_UNITS_PANEL = 268;
    public static final int IMG_EDIT_CASTLE_PANEL = 269;
    public static final int IMG_EDIT_STAT_BOX = 270;
    public static final int IMG_EDIT_ADD = 271;
    public static final int IMG_EDIT_ADD_HIGHLIGHT = 272;
    public static final int IMG_EDIT_ADD_ALERT = 273;
    public static final int IMG_EDIT_BUTTON = 274;
    public static final int IMG_EDIT_BUTTON_HIGHLIGHT = 275;
    public static final int IMG_NEW_UNIT = 276;
    public static final int IMG_NEW_UNIT_DISABLED = 277;
    public static final int IMG_NEW_UNIT_SELECTED = 278;
    public static final int IMG_NEW_UNIT_HIGHLIGHT = 279;

    // new backpanel
    public static final int IMG_BACK_PANEL_EDIT = 280;

    // cooperative
    // public static final int IMG_COOPERATIVE = 281;
    public static final int IMG_COOPERATIVE_RED = 282;

    // administrative menu buttons
    public static final int IMG_ACCOUNT = 283;
    public static final int IMG_ACCOUNT_HIGHLIGHT = 284;
    public static final int IMG_CREDITS = 285;
    public static final int IMG_CREDITS_HIGHLIGHT = 286;

    // menu buttons [reordered numerically as of 9/21/10]
    // public static final int IMG_ARCHIVE = 287;
    public static final int IMG_ARCHIVE_RED = 288;
    // public static final int IMG_BUY = 289;
    public static final int IMG_BUY_RED = 290;
    //public static final int IMG_BUY_DISABLED = 291;
    public static final int IMG_EDIT_RED = 292;
    // public static final int IMG_EDIT = 293;
    public static final int IMG_RANDOM_RED = 294;
    // public static final int IMG_RANDOM = 295;
    public static final int IMG_CONSTRUCTED_RED = 296;
    // public static final int IMG_CONSTRUCTED = 297;
    public static final int IMG_SINGLE_RED = 298; // IMG_SINGLE lies at 449

    // Projectiles
    public static final int IMG_ARROW = 299;
    public static final int IMG_STONE = 300;
    public static final int IMG_GREEN_BALL = 301;
    public static final int IMG_PURPLE_BALL = 302;
    public static final int IMG_SPEAR = 303;
    public static final int IMG_MAGIC_BALL_1 = 304;
    public static final int IMG_MAGIC_BALL_2 = 305;
    public static final int IMG_MAGIC_BALL_3 = 306;
    public static final int IMG_MAGIC_BALL_4 = 307;
    public static final int IMG_MAGIC_BALL_5 = 308;
    public static final int IMG_MAGIC_BALL_6 = 309;
    public static final int IMG_WHITE_BALL = 310;
    public static final int IMG_RED_BALL = 311;

    // Interface
    public static final int IMG_ACTION = 312;
    public static final int IMG_ACTION_HIGHLIGHT = 313;
    public static final int IMG_ACTION_DISABLE = 314;
    public static final int IMG_ACTION_PASSIVE = 315;
    public static final int IMG_ACTION_OVERLAY = 316;
    public static final int IMG_BACK_PANEL = 317;
    public static final int IMG_BACK_PANEL_GAME = 318;
    public static final int IMG_STAT_PANEL = 319;
    public static final int IMG_DEPLOY = 320;
    public static final int IMG_DEPLOY_HIGHLIGHT = 321;
    public static final int IMG_DEPLOY_SELECTED = 322;
    public static final int IMG_DEPLOY_GREY = 323;
    public static final int IMG_END_PANEL = 324;
    public static final int IMG_END_TURN = 325;
    public static final int IMG_SURRENDER = 326;
    public static final int IMG_DRAW = 327;

    // Highlights
    public static final int IMG_BLUE = 328;
    public static final int IMG_BLUE_BORDER = 329;
    public static final int IMG_RED = 330;
    public static final int IMG_RED_BORDER = 331;
    public static final int IMG_GREEN = 332;
    public static final int IMG_GREEN_BORDER = 333;

    public static final int IMG_PURPLE = 334;
    public static final int IMG_PURPLE_BORDER = 335;
    public static final int IMG_BLACK = 336;
    public static final int IMG_BLACK_BORDER = 337;
    public static final int IMG_YELLOW = 338;
    public static final int IMG_YELLOW_BORDER = 339;

    // the "miss" icon
    public static final int IMG_MISS = 340;

    // Chat icons
    public static final int IMG_USER_ICON = 341;
    //public static final int IMG_BATTLE_ICON     = 342;
    public static final int IMG_IDLE_ICON = 342;

    // poof animation
    public static final int IMG_POOF = 343;
    public static final int IMG_POOF_1 = 344;
    public static final int IMG_POOF_2 = 345;
    public static final int IMG_POOF_3 = 346;
    public static final int IMG_POOF_4 = 347;
    public static final int IMG_POOF_5 = 348;
    public static final int IMG_POOF_6 = 349;
    public static final int IMG_POOF_7 = 350;
    public static final int IMG_POOF_8 = 351;
    public static final int IMG_POOF_9 = 352;
    public static final int IMG_POOF_10 = 353;
    public static final int IMG_POOF_11 = 354;
    public static final int IMG_POOF_12 = 355;
    public static final int IMG_POOF_13 = 356;
    public static final int IMG_POOF_14 = 357;
    public static final int IMG_POOF_15 = 358;
    public static final int IMG_POOF_16 = 359;
    public static final int IMG_POOF_17 = 360;
    public static final int IMG_POOF_18 = 361;
    public static final int IMG_POOF_19 = 362;
    public static final int IMG_POOF_20 = 363;

    public static final int IMG_EXPLOSION_1 = 364;
    public static final int IMG_EXPLOSION_2 = 365;
    public static final int IMG_EXPLOSION_3 = 366;
    public static final int IMG_EXPLOSION_4 = 367;
    public static final int IMG_EXPLOSION_5 = 368;
    public static final int IMG_EXPLOSION_6 = 369;
    public static final int IMG_EXPLOSION_7 = 370;
    public static final int IMG_EXPLOSION_8 = 371;
    public static final int IMG_EXPLOSION_9 = 372;
    public static final int IMG_EXPLOSION_10 = 373;
    public static final int IMG_EXPLOSION_11 = 374;
    public static final int IMG_EXPLOSION_12 = 375;
    public static final int IMG_EXPLOSION_13 = 376;
    public static final int IMG_EXPLOSION_14 = 377;
    public static final int IMG_EXPLOSION_15 = 378;
    public static final int IMG_EXPLOSION_16 = 379;

    // tiles
    public static final int IMG_LIGHT_TILE = 380;
    public static final int IMG_DARK_TILE = 381;

    // detonate
    public static final int IMG_DETONATE_1 = 382;
    public static final int IMG_DETONATE_2 = 383;
    public static final int IMG_DETONATE_3 = 384;
    public static final int IMG_DETONATE_4 = 385;
    public static final int IMG_DETONATE_5 = 386;
    public static final int IMG_DETONATE_6 = 387;
    public static final int IMG_DETONATE_7 = 388;
    public static final int IMG_DETONATE_8 = 389;
    public static final int IMG_DETONATE_9 = 390;
    public static final int IMG_DETONATE_10 = 391;
    public static final int IMG_DETONATE_11 = 392;
    public static final int IMG_DETONATE_12 = 393;
    public static final int IMG_DETONATE_13 = 394;
    public static final int IMG_DETONATE_14 = 395;
    public static final int IMG_DETONATE_15 = 396;
    public static final int IMG_DETONATE_16 = 397;

    public static final int IMG_BIG_POOF_1 = 398;
    public static final int IMG_BIG_POOF_2 = 399;
    public static final int IMG_BIG_POOF_3 = 400;
    public static final int IMG_BIG_POOF_4 = 401;
    public static final int IMG_BIG_POOF_5 = 402;
    public static final int IMG_BIG_POOF_6 = 403;
    public static final int IMG_BIG_POOF_7 = 404;
    public static final int IMG_BIG_POOF_8 = 405;
    public static final int IMG_BIG_POOF_9 = 406;
    public static final int IMG_BIG_POOF_10 = 407;
    public static final int IMG_BIG_POOF_11 = 408;
    public static final int IMG_BIG_POOF_12 = 409;
    public static final int IMG_BIG_POOF_13 = 410;
    public static final int IMG_BIG_POOF_14 = 411;
    public static final int IMG_BIG_POOF_15 = 412;
    public static final int IMG_BIG_POOF_16 = 413;
    public static final int IMG_BIG_POOF_17 = 414;
    public static final int IMG_BIG_POOF_18 = 415;
    public static final int IMG_BIG_POOF_19 = 416;
    public static final int IMG_BIG_POOF_20 = 417;
    public static final int IMG_BIG_POOF_21 = 418;
    public static final int IMG_BIG_POOF_22 = 419;
    public static final int IMG_BIG_POOF_23 = 420;
    public static final int IMG_BIG_POOF_24 = 421;
    public static final int IMG_BIG_POOF_25 = 422;

    // holy visual effect
    public static final int IMG_EYE = 423;
    public static final int IMG_STAR = 424;

    // holy burst
    public static final int IMG_BURST_1 = 425;
    public static final int IMG_BURST_2 = 426;
    public static final int IMG_BURST_3 = 427;
    public static final int IMG_BURST_4 = 428;
    public static final int IMG_BURST_5 = 429;
    public static final int IMG_BURST_6 = 430;
    public static final int IMG_BURST_7 = 431;
    public static final int IMG_BURST_8 = 432;
    public static final int IMG_BURST_9 = 433;
    public static final int IMG_BURST_10 = 434;
    public static final int IMG_BURST_11 = 435;
    public static final int IMG_BURST_12 = 436;
    public static final int IMG_BURST_13 = 437;
    public static final int IMG_BURST_14 = 438;
    public static final int IMG_BURST_15 = 439;
    public static final int IMG_BURST_16 = 440;
    public static final int IMG_BURST_17 = 441;
    public static final int IMG_BURST_18 = 442;
    public static final int IMG_BURST_19 = 443;
    public static final int IMG_BURST_20 = 444;

    // nature magic weirdness
    public static final int IMG_WHEEL = 445;
    public static final int IMG_FACE = 446;
    public static final int IMG_MASK = 447;

    // menu
    public static final int IMG_MENU = 448;
    //public static final int IMG_SINGLE = 449;

    // icons for lobby (while in a game)
    public static final int IMG_SINGLE_ICON = 450;
    public static final int IMG_CONSTRUCTED_ICON = 451;
    public static final int IMG_COOPERATIVE_ICON = 452;
    public static final int IMG_RANDOM_ICON = 454;
    public static final int IMG_TEAM_ICON = 455;

    // new units as of 9/15/10
    public static final int IMG_DUELIST = 456;
    public static final int IMG_DUELIST_ENEMY = 457;
    public static final int IMG_MILITIA = 458;
    public static final int IMG_MILITIA_ENEMY = 459;

    public static final int IMG_CONSPIRATOR = 460;
    public static final int IMG_CONSPIRATOR_ENEMY = 461;
    public static final int IMG_SOLDIER_O = 462;
    public static final int IMG_SOLDIER_O_ENEMY = 463;
    public static final int IMG_SOLDIER_D = 464;
    public static final int IMG_SOLDIER_D_ENEMY = 465;
/*public static final int IMG_NEWUNIT5  = 466;
public static final int IMG_NEWUNIT5_ENEMY  = 467;
public static final int IMG_NEWUNIT6  = 468;
public static final int IMG_NEWUNIT6_ENEMY  = 469;
public static final int IMG_NEWUNIT7  = 470;
public static final int IMG_NEWUNIT7_ENEMY  = 471;
public static final int IMG_NEWUNIT8  = 472;
public static final int IMG_NEWUNIT8_ENEMY  = 473;
public static final int IMG_NEWUNIT9  = 468;
public static final int IMG_NEWUNIT9_ENEMY  = 474;
public static final int IMG_NEWUNIT10  = 475;
public static final int IMG_NEWUNIT10_ENEMY  = 476;
public static final int IMG_NEWUNIT11  = 477;
public static final int IMG_NEWUNIT11_ENEMY  = 478;*/

    // tutorial images
    public static final int IMG_TUTORIAL_BORDER = 479;
    public static final int IMG_TUTORIAL_BACK = 480;
    public static final int IMG_PREV = 481;
    public static final int IMG_PREV_HIGHLIGHTED = 482;
    public static final int IMG_NEXT = 483;
    public static final int IMG_NEXT_HIGHLIGHTED = 484;
    public static final int IMG_CLOSE = 485;
    public static final int IMG_CLOSE_HIGHLIGHTED = 486;
    public static final int IMG_INDEX = 487;
    public static final int IMG_INDEX_HIGHLIGHTED = 488;
    public static final int IMG_TUTORIAL_01 = 489;
    public static final int IMG_TUTORIAL_03 = 490;
    public static final int IMG_TUTORIAL_04 = 491;
    public static final int IMG_TUTORIAL_05 = 492;
    public static final int IMG_TUTORIAL_06 = 493;
    public static final int IMG_TUTORIAL_07 = 494;
    public static final int IMG_TUTORIAL_08 = 495;
    public static final int IMG_TUTORIAL_10 = 496;
    public static final int IMG_TUTORIAL_11 = 497;
    public static final int IMG_TUTORIAL_12 = 498;
    public static final int IMG_TUTORIAL_13 = 499;
    public static final int IMG_TUTORIAL_14 = 500;
    public static final int IMG_TUTORIAL_15 = 501;
    public static final int IMG_TUTORIAL_16 = 502;
    public static final int IMG_TUTORIAL_17 = 503;
    public static final int IMG_TUTORIAL_18 = 504;
    public static final int IMG_TUTORIAL_19 = 505;
    public static final int IMG_TUTORIAL_20 = 506;
    public static final int IMG_TUTORIAL_21 = 507;
    public static final int IMG_TUTORIAL_22 = 508;
    public static final int IMG_TUTORIAL_23 = 509;
    public static final int IMG_TUTORIAL_24 = 510;
    public static final int IMG_TUTORIAL_25 = 511;
    public static final int IMG_TUTORIAL_26 = 512;
    public static final int IMG_TUTORIAL_27 = 513;
    public static final int IMG_TUTORIAL_28 = 514;
    public static final int IMG_TUTORIAL_29 = 515;
    public static final int IMG_TUTORIAL_31 = 516;
    public static final int IMG_TUTORIAL_32 = 517;
    public static final int IMG_TUTORIAL_33 = 518;
    public static final int IMG_TUTORIAL_34 = 519;
    public static final int IMG_TUTORIAL_35 = 520;
    public static final int IMG_TUTORIAL_36 = 521;
    public static final int IMG_JUMP_PREV = 522;
    public static final int IMG_JUMP_PREV_HIGHLIGHTED = 523;
    public static final int IMG_JUMP_NEXT = 524;
    public static final int IMG_JUMP_NEXT_HIGHLIGHTED = 525;


    // New Units and relics
    public static final int IMG_ARCH_DEMON = 526;
    public static final int IMG_ARCH_DEMON_ENEMY = 527;

    public static final int IMG_RELIC_BANISH = 528;
    public static final int IMG_RELIC_CLOCKWORK = 529;
    public static final int IMG_RELIC_EVASIVE = 530;
    public static final int IMG_RELIC_EXPLODE = 531;
    public static final int IMG_RELIC_FLIGHT = 532;
    public static final int IMG_RELIC_GIFT_UNIT = 533;
    public static final int IMG_RELIC_HEAL_MOVE = 534;
    public static final int IMG_RELIC_PARRY = 535;
    public static final int IMG_RELIC_RESET = 536;
    public static final int IMG_RELIC_SPELL_BLOCK = 537;
    public static final int IMG_RELIC_STUN = 538;
    public static final int IMG_RELIC_VAMPIRE = 539;

    // Icons for unit stats
    public static final int IMG_ICON_ACTIONS = 540;
    public static final int IMG_ICON_ARMOR = 541;
    public static final int IMG_ICON_DEPLOY = 542;
    public static final int IMG_ICON_LIFE = 543;
    public static final int IMG_ICON_POWER = 544;

    // buttons for scrolling in army editor
    public static final int IMG_NEW_UNIT_DOWN = 545;
    public static final int IMG_NEW_UNIT_DOWN_HIGHLIGHT = 546;
    public static final int IMG_NEW_UNIT_UP = 547;
    public static final int IMG_NEW_UNIT_UP_HIGHLIGHT = 548;
    public static final int IMG_EDIT_GOLD = 549;

    // parry
    public static final int IMG_PARRY = 550;

    // splat animations
    public static final int IMG_SPLAT_INORGANIC_1 = 551;
    public static final int IMG_SPLAT_INORGANIC_2 = 552;
    public static final int IMG_SPLAT_INORGANIC_3 = 553;
    public static final int IMG_SPLAT_INORGANIC_4 = 554;
    public static final int IMG_SPLAT_INORGANIC_5 = 555;
    public static final int IMG_SPLAT_ORGANIC_1 = 556;
    public static final int IMG_SPLAT_ORGANIC_2 = 557;
    public static final int IMG_SPLAT_ORGANIC_3 = 558;
    public static final int IMG_SPLAT_ORGANIC_4 = 559;
    public static final int IMG_SPLAT_ORGANIC_5 = 560;

    public static final int IMG_SUMMON_1 = 561;
    public static final int IMG_SUMMON_2 = 562;
    public static final int IMG_SUMMON_3 = 563;
    public static final int IMG_SUMMON_4 = 564;
    public static final int IMG_SUMMON_5 = 565;
    public static final int IMG_SUMMON_6 = 566;
    public static final int IMG_SUMMON_7 = 567;
    public static final int IMG_SUMMON_8 = 568;

    public static final int IMG_MPBACK = 569;

    public static final int IMG_LOCKDOWN_1 = 570;
    public static final int IMG_LOCKDOWN_2 = 571;
    public static final int IMG_LOCKDOWN_3 = 572;
    public static final int IMG_LOCKDOWN_4 = 573;
    public static final int IMG_LOCKDOWN_5 = 574;
    public static final int IMG_LOCKDOWN_6 = 575;
    public static final int IMG_LOCKDOWN_7 = 576;
    public static final int IMG_LOCKDOWN_8 = 577;
    public static final int IMG_LOCKDOWN_9 = 578;
    public static final int IMG_LOCKDOWN_10 = 579;
    public static final int IMG_LOCKDOWN_11 = 580;

    public static final int IMG_TOMB_LORD_1 = 581;
    public static final int IMG_TOMB_LORD_2 = 582;

    public static final int IMG_DAMBACK = 583;

    public static final int IMG_ICON_MURDERER = 584;

    public static final int IMG_RANGER_MELEE = 585;
    public static final int IMG_RANGER_MELEE_ENEMY = 586;
    public static final int IMG_RANGER_WOLF_MELEE = 587;
    public static final int IMG_RANGER_WOLF_MELEE_ENEMY = 588;

    public static final int IMG_TIME_1 = 589;
    public static final int IMG_TIME_2 = 590;
    public static final int IMG_TIME_3 = 591;

    public static final int IMG_RELIC = 592;

    public static final int IMG_BUILD_01 = 593;
    public static final int IMG_BUILD_02 = 594;
    public static final int IMG_BUILD_03 = 595;
    public static final int IMG_BUILD_04 = 596;
    public static final int IMG_BUILD_05 = 597;
    public static final int IMG_BUILD_06 = 598;
    public static final int IMG_BUILD_07 = 599;
    public static final int IMG_BUILD_08 = 600;
    public static final int IMG_BUILD_09 = 601;
    public static final int IMG_BUILD_10 = 602;
    public static final int IMG_BUILD_11 = 603;
    public static final int IMG_BUILD_12 = 604;
    public static final int IMG_BUILD_13 = 605;
    public static final int IMG_BUILD_14 = 606;
    public static final int IMG_BUILD_15 = 607;
    public static final int IMG_BUILD_16 = 608;
    public static final int IMG_BUILD_17 = 609;
    public static final int IMG_BUILD_18 = 610;
    public static final int IMG_BUILD_19 = 611;
    public static final int IMG_BUILD_20 = 612;
    public static final int IMG_BUILD_21 = 613;
    public static final int IMG_BUILD_22 = 614;
    public static final int IMG_BUILD_23 = 615;
    public static final int IMG_BUILD_24 = 616;
    public static final int IMG_BUILD_25 = 617;
    public static final int IMG_BUILD_26 = 618;
    public static final int IMG_BUILD_27 = 619;
    public static final int IMG_BUILD_28 = 620;
    public static final int IMG_BUILD_29 = 621;
    public static final int IMG_BUILD_30 = 622;
    public static final int IMG_BUILD_31 = 623;
    public static final int IMG_BUILD_32 = 624;
    public static final int IMG_BUILD_33 = 625;
    public static final int IMG_BUILD_34 = 626;

    public static final int IMG_SUMMON_BLUE_1 = 627;
    public static final int IMG_SUMMON_BLUE_2 = 628;
    public static final int IMG_SUMMON_BLUE_3 = 629;
    public static final int IMG_SUMMON_BLUE_4 = 630;
    public static final int IMG_SUMMON_BLUE_5 = 631;
    public static final int IMG_SUMMON_BLUE_6 = 632;
    public static final int IMG_SUMMON_BLUE_7 = 633;
    public static final int IMG_SUMMON_BLUE_8 = 634;

    public static final int IMG_POWERUP_TOXIC = 635;
    public static final int IMG_POWERUP_EVASIVE = 636;
    public static final int IMG_POWERUP_RESILIENT = 637;
    public static final int IMG_POWERUP_LONGSHANK = 638;
    public static final int IMG_POWERUP_MIGHTY = 639;
    public static final int IMG_POWERUP_CLOCKWORK = 640;
    public static final int IMG_POWERUP_VAMPIRIC = 641;
    public static final int IMG_POWERUP_CUNNING = 642;
    public static final int IMG_POWERUP_EPIC = 643;
    public static final int IMG_POWERUP_ARCANE = 644;
    public static final int IMG_POWERUP_ASCENDANT = 645;
    public static final int IMG_POWERUP_GUARDIAN = 646;
    public static final int IMG_POWERUP_VIGILANT = 647;
    public static final int IMG_POWERUP_ZEALOUS = 648;
    public static final int IMG_POWERUP_RAMPAGING = 649;
    public static final int IMG_POWERUP_RUTHLESS = 650;
    public static final int IMG_POWERUP_ENRAGED = 651;

    public static final int IMG_DEVIL = 652;
    public static final int IMG_DEVIL_ENEMY = 653;

    public static final int IMG_PLAYERPANEL = 654;
    public static final int IMG_CHAT_LINE = 655;
    public static final int IMG_CHAT = 656;
    public static final int IMG_CHAT_UP = 657;
    public static final int IMG_CHAT_DOWN = 658;
    public static final int IMG_PLAYERS = 659;
    public static final int IMG_SCROLL_PLAYER = 660;
    public static final int IMG_SCROLL = 661;
    public static final int IMG_SCROLL_BAR = 662;
    public static final int IMG_PLAYERS_BUTTON = 663;
    public static final int IMG_CHAT_BUTTON = 664;

    public static final int IMG_BONES = 665;

    public static final int IMG_EXIT = 666;
    public static final int IMG_EXIT_HIGHLIGHT = 667;

    public static final int IMG_WAITING_CONSTRUCTED_ICON = 668;
    public static final int IMG_WAITING_RANDOM_ICON = 669;
    public static final int IMG_WAITING_COOPERATIVE_ICON = 670;
    public static final int IMG_WAITING_TEAM_ICON = 671;

    public static final int IMG_TEAM_NOT_READY = 672;
    public static final int IMG_SWORD_1 = 673;
    public static final int IMG_SWORD_2 = 674;
    public static final int IMG_TEAMS_BOX = 675;
    public static final int IMG_TEAMS_PLAYER = 676;

    public static final int IMG_CLASH_1 = 677;
    public static final int IMG_CLASH_2 = 678;
    public static final int IMG_CLASH_3 = 679;
    public static final int IMG_CLASH_4 = 680;
    public static final int IMG_CLASH_5 = 681;
    public static final int IMG_CLASH_6 = 682;
    public static final int IMG_CLASH_7 = 683;
    public static final int IMG_CLASH_8 = 684;

    public static final int IMG_TARGET_01 = 685;
    public static final int IMG_TARGET_02 = 686;
    public static final int IMG_TARGET_03 = 687;
    public static final int IMG_TARGET_04 = 688;
    public static final int IMG_TARGET_05 = 689;
    public static final int IMG_TARGET_06 = 690;
    public static final int IMG_TARGET_07 = 691;
    public static final int IMG_TARGET_08 = 692;
    public static final int IMG_TARGET_09 = 693;
    public static final int IMG_TARGET_10 = 694;
    public static final int IMG_TARGET_11 = 695;
    public static final int IMG_TARGET_12 = 696;

    public static final int IMG_SHIELD_1 = 697;
    public static final int IMG_SHIELD_2 = 698;

    public static final int IMG_EDIT_ICON = 699;

    public static final int IMG_USER_ICON_CRU = 700;
    public static final int IMG_USER_ICON_LEG = 701;
    public static final int IMG_USER_ICON_CRU_LEG = 702;

    public static final int IMG_END_GAME_VICTORY = 703;
    public static final int IMG_END_GAME_DEFEAT = 704;
    public static final int IMG_TEAM_ICON_1 = 705;
    public static final int IMG_TEAM_ICON_2 = 706;
    public static final int IMG_TEAM_ICON_3 = 707;
    public static final int IMG_TEAM_ICON_4 = 708;

    public static final int IMG_TEAM_ICONS_BUTTON = 709;
    public static final int IMG_PLUS_BUTTON = 710;
    public static final int IMG_MINUS_BUTTON = 711;

    //  Lastly...
    public static final int IMG_COUNT = 712;

    ////////////////////////////////////////////////////
    // Sounds
    ////////////////////////////////////////////////////

    public static final short SOUND_VICTORY = 0;
    public static final short SOUND_PRELUDE = 1;
    public static final short SOUND_BOW = 2;
    public static final short SOUND_XBOW = 3;
    public static final short SOUND_RELOAD = 4;
    public static final short SOUND_ORG_HIT = 5;
    public static final short SOUND_ORG_DEATH = 6;
    public static final short SOUND_INORG_HIT = 7;
    public static final short SOUND_INORG_DEATH = 8;
    public static final short SOUND_SWING = 9;
    public static final short SOUND_DETONATE = 10;
    public static final short SOUND_DEFEAT = 11;
    public static final short SOUND_END_TURN = 12;
    public static final short SOUND_START_TURN = 13;
    public static final short SOUND_EXPLOSION = 14;
    public static final short SOUND_FIREBALL = 15;
    public static final short SOUND_BOOM = 16;
    public static final short SOUND_TING = 17;
    public static final short SOUND_POOF = 18;
    public static final short SOUND_MAGIC_BOLT = 19;
    public static final short SOUND_STUNBALL = 20;
    public static final short SOUND_GROWL = 21;
    public static final short SOUND_EYE = 22;
    public static final short SOUND_JUDGEMENT = 23;
    public static final short SOUND_START_CHANNEL = 24;
    public static final short SOUND_END_CHANNEL = 25;
    public static final short SOUND_SQUISH = 26;
    public static final short SOUND_CATAPULT = 27;
    public static final short SOUND_SHEATH = 28;
    public static final short SOUND_UNSHEATH = 29;
    public static final short SOUND_WRITE = 30;
    public static final short SOUND_PAPER = 31;
    public static final short SOUND_BUILD = 32;
    public static final short SOUND_NATURE = 33;
    public static final short SOUND_WHEEL = 34;
    public static final short SOUND_BUTTON = 35;
    public static final short SOUND_RALLY = 36;
    public static final short SOUND_CHARGE = 37;
    public static final short SOUND_BOWPULL = 38;
    public static final short SOUND_WOLF = 39;
    public static final short SOUND_SHAPESHIFT = 40;
    public static final short SOUND_BUY = 41;
    public static final short SOUND_MOUSEOVER = 42;
    public static final short SOUND_GOLD = 43;

    // count
    public static final short SOUND_COUNT = 44;
}