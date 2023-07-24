///////////////////////////////////////////////////////////////////////
// Name: Unit
// Desc: A unit in the game
// Date: 5/10/2003 - Gabe Jones
//       10/1/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports

import leo.shared.crusades.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Stack;
import java.util.Vector;


public class Unit {

    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////

    // access level
    public static final short FREE = 0;
    public static final short CRUSADES = 1;
    public static final short LEGIONS = 2;
    public static final short INQUISITION = 3;

    // access names
    public static final String[] GAME_NAME =
            {Strings.UNIT_1,
                    Strings.UNIT_2,
                    Strings.UNIT_3,
                    Strings.UNIT_4
            };


    // teams
    public static final short TEAM_NONE = 0;
    public static final short TEAM_1 = 1;
    public static final short TEAM_2 = 2;

    // inquisition units
    public static final short HERO = 0;


    public static final short[][] UNITS =
            {

                    // tier 1
                    {Unit.FOOTMAN,
                            Unit.BOWMAN,
                            Unit.PIKEMAN,
                            Unit.TACTICIAN,
                            Unit.CAVALRY,
                            Unit.SCOUT,
                            Unit.ARCHER,
                            Unit.CATAPULT,
                            Unit.BALLISTA,
                            Unit.GENERAL,
                            Unit.MILITIA,
                            Unit.CONSPIRATOR},

                    // tier 2
                    {Unit.RIDER,
                            Unit.ASSASSIN,
                            Unit.FANATIC,
                            Unit.WARRIOR,
                            Unit.KNIGHT,
                            Unit.RANGER,
                            Unit.MASON,
                            Unit.CROSSBOWMAN,
                            Unit.GATE_GUARD,
                            Unit.RELIC_GIFT_UNIT
                    },

                    // tier 3
                    {Unit.SWORDSMAN,
                            Unit.BERSERKER,
                            Unit.COMMAND_POST,
                            Unit.BARRACKS,
                            Unit.TOWER,
                            Unit.ROGUE,
                            Unit.ACOLYTE,
                            Unit.AXEMAN,
                            Unit.BARBARIAN,
                            Unit.RELIC_BANISH},

                    // tier 4
                    {Unit.MOUNTED_ARCHER,
                            Unit.SERGEANT,
                            Unit.FIRE_ARCHER,
                            Unit.ARMORY,
                            Unit.MARTYR,
                            Unit.TEMPLAR,
                            Unit.MOURNER,
                            Unit.QUARTERMASTER,
                            Unit.SHIELD_BEARER,
                            Unit.ABBEY,
                            Unit.RELIC_RESET,
                            Unit.RELIC_EXPLODE
                    },

                    // tier 5
                    {Unit.PRIEST,
                            Unit.HEALER,
                            Unit.DRUID,
                            Unit.PALADIN,
                            Unit.GOLEM,
                            Unit.SHIELD_MAIDEN,
                            Unit.HERETIC,
                            Unit.CONFESSOR,
                            Unit.LANCER,
                            Unit.SYCOPHANT
                    },

                    // tier 6
                    {Unit.SHAMAN,
                            Unit.WAR_ELEPHANT,
                            Unit.LYCANTHROPE,
                            Unit.CHANNELER,
                            Unit.SUMMONER,
                            Unit.ABJURER,
                            Unit.GEOMANCER,
                            Unit.STRATEGIST,
                            Unit.DIPLOMAT,
                            Unit.LONGBOWMAN,
                            Unit.SUPPLICANT,
                            Unit.RELIC_EVASIVE,
                            Unit.RELIC_FLIGHT,
                            Unit.RELIC_HEAL_MOVE,
                    },

                    // tier 7
                    {
                            Unit.BOUNTY_HUNTER,
                            Unit.CONSPIRATOR,
                            Unit.WARLOCK,
                            Unit.WIZARD,
                            Unit.ENCHANTER,
                            Unit.NECROMANCER,
                            Unit.WITCH,
                            Unit.MAGUS,
                            Unit.CONJURER,
                            Unit.CHIEFTAIN,
                            Unit.CAPTAIN,
                            Unit.RELIC_CLOCKWORK,
                            Unit.RELIC_VAMPIRE,
                            Unit.RELIC_STUN,
                            Unit.RELIC_SPELL_BLOCK,
                            Unit.RELIC_PARRY,
                            Unit.DUELIST
                    },

                    // tier 8
                    {Unit.DIABOLIST,
                            Unit.ARTIFICER,
                            Unit.MIMIC,
                            Unit.DOPPELGANGER,
                            Unit.SKINWALKER,
                            Unit.CHANGELING,
                            Unit.POSSESSED,
                            Unit.ALCHEMIST
                    },

                    // tier 9
                    {Unit.DRACOLICH,
                            Unit.DRAGON,
                            Unit.FEATHERED_SERPENT,
                            Unit.HYDRA,
                            Unit.ARCHANGEL,
                            Unit.WYVERN
                    },
            };
    //////////////////////////
    // Classes
    //////////////////////////
    private static final String[] classNames =
            {Strings.UNIT_5,
                    Strings.UNIT_6,
                    Strings.UNIT_7,
                    Strings.UNIT_8,
                    Strings.UNIT_9,
                    Strings.UNIT_10,
                    Strings.UNIT_11,
                    Strings.UNIT_12,
                    Strings.UNIT_13,
                    Strings.UNIT_14,
                    Strings.UNIT_15,
                    Strings.UNIT_16,
                    Strings.UNIT_17,
                    Strings.UNIT_18,
                    Strings.UNIT_20
            };

    public static final int ARCHERS = 0;
    public static final int BLACK_MAGIC_USERS = 1;
    public static final int CLERGY = 2;
    public static final int COMMANDERS = 3;
    public static final int CULTISTS = 4;
    public static final int HORSEMEN = 5;
    public static final int NATURE = 6;
    public static final int SCOUTS = 7;
    public static final int SHAPESHIFTERS = 8;
    public static final int SIEGE = 9;
    public static final int SOLDIERS = 10;
    public static final int STRUCTURES = 11;
    public static final int WHITE_MAGIC_USERS = 12;
    public static final int WYRMS = 13;
    public static final int RELICS = 14;
    public static final int CLASS_COUNT = 15;


    //////////////////////////
    // Units
    //////////////////////////
    public static final short IMP = -1;
    public static final short DEMON = -2;
    public static final short SKELETON = -3;
    public static final short ZOMBIE = -4;
    public static final short SOLDIER = -5;
    public static final short WILL_O_THE_WISPS = -6;
    public static final short SERPENT = -7;
    public static final short BEAR = -8;
    public static final short PORTAL = -9;
    public static final short GATE = -10;
    public static final short ARCHDEMON = -11;

    // Powerups
    public static final short POWERUP = -20;
    public static final short POWERUP_TOXIC = -20;
    public static final short POWERUP_EVASIVE = -21;
    public static final short POWERUP_RESILIENT = -22;
    public static final short POWERUP_LONGSHANK = -23;
    public static final short POWERUP_MIGHTY = -24;
    public static final short POWERUP_CLOCKWORK = -25;
    public static final short POWERUP_VAMPIRIC = -26;
    public static final short POWERUP_CUNNING = -27;
    public static final short POWERUP_EPIC = -28;
    public static final short POWERUP_ARCANE = -29;
    public static final short POWERUP_ASCENDANT = -30;
    public static final short POWERUP_GUARDIAN = -31;
    public static final short POWERUP_VIGILANT = -32;
    public static final short POWERUP_ZEALOUS = -33;
    public static final short POWERUP_RAMPAGING = -34;
    public static final short POWERUP_RUTHLESS = -35;
    public static final short POWERUP_ENRAGED = -36;
    // # = 17

    public static final short FOOTMAN = 0;
    public static final short BOWMAN = 1;
    public static final short CAVALRY = 2;
    public static final short ARCHER = 3;
    public static final short PIKEMAN = 4;
    public static final short KNIGHT = 5;
    public static final short RANGER = 6;
    public static final short WOLF = 7;
    public static final short SUMMONER = 8;
    // 9
    // 10
    public static final short PRIEST = 11;
    public static final short ENCHANTER = 12;
    public static final short TEMPLAR = 13;
    public static final short WARRIOR = 14;
    public static final short RIDER = 15;
    public static final short HEALER = 16;
    public static final short WIZARD = 17;
    public static final short SCOUT = 18;
    public static final short ASSASSIN = 19;
    public static final short TACTICIAN = 20;
    public static final short GENERAL = 21;
    public static final short WALL = 22;
    public static final short MASON = 23;
    public static final short CATAPULT = 24;
    public static final short BALLISTA = 25;
    public static final short NECROMANCER = 26;
    public static final short LICH = 27;
    // 28
    // 29
    public static final short SERGEANT = 30;
    public static final short ABJURER = 31;
    public static final short SEAL = 32;
    public static final short WARLOCK = 33;
    public static final short CROSSBOWMAN = 34;
    public static final short DRAGON = 35;
    public static final short DRACOLICH = 36;
    public static final short HYDRA = 37;
    public static final short TOWER = 38;
    public static final short COMMAND_POST = 39;
    public static final short BARRACKS = 40;
    // 41
    public static final short DRUID = 42;
    public static final short CHANNELER = 43;
    public static final short LYCANTHROPE = 44;
    public static final short WEREWOLF = 45;
    public static final short LYCANWOLF = 46;
    public static final short MOUNTED_ARCHER = 47;
    public static final short GEOMANCER = 48;
    public static final short ROCK = 49;
    public static final short SWORDSMAN = 50;
    public static final short WITCH = 51;
    public static final short TOAD = 52;
    public static final short SHIELD_MAIDEN = 53;
    public static final short MAGUS = 54;
    public static final short SPIRIT = 55;
    // 56
    public static final short GOLEM = 57;
    public static final short ARMORY = 58;
    // 59
    public static final short FIRE_ARCHER = 60;
    public static final short MIMIC = 61;
    public static final short PALADIN = 62;
    public static final short SHAMAN = 63;
    public static final short MARTYR = 64;
    public static final short ROGUE = 65;
    public static final short DIABOLIST = 66;
    public static final short GHOST = 67;
    public static final short GATE_GUARD = 68;
    public static final short FEATHERED_SERPENT = 69;
    public static final short BERSERKER = 70;
    public static final short ARTIFICER = 71;
    public static final short CHANGELING = 72;
    public static final short DOPPELGANGER = 73;
    public static final short SKINWALKER = 74;
    public static final short ACOLYTE = 75;
    public static final short AXEMAN = 76;
    public static final short MOURNER = 77;
    public static final short HERETIC = 78;
    public static final short WAR_ELEPHANT = 79;
    public static final short FANATIC = 80;
    public static final short DISMOUNTED_KNIGHT = 81;
    // 82
    public static final short QUARTERMASTER = 83;
    public static final short WALL_MASON = 84;
    public static final short CONFESSOR = 85;
    public static final short STRATEGIST = 86;
    public static final short POSSESSED = 87;
    public static final short BARBARIAN = 88;
    public static final short ALCHEMIST = 89;
    public static final short BOUNTY_HUNTER = 90;
    public static final short SHIELD_BEARER = 91;
    public static final short CHIEFTAIN = 92;
    public static final short LANCER = 93;
    public static final short ARCHANGEL = 94;
    public static final short CONJURER = 95;
    // 96
    // 97
    public static final short DIPLOMAT = 98;
    public static final short DIPLOMAT_USED = 99;
    public static final short LONGBOWMAN = 100;
    public static final short SYCOPHANT = 101;
    public static final short WYVERN = 102;
    public static final short EGG = 103;
    public static final short CAPTAIN = 104;
    public static final short ABBEY = 105;
    public static final short SUPPLICANT = 106;
    public static final short DUELIST = 107;
    public static final short MILITIA = 108;
    public static final short CONSPIRATOR = 109;
    public static final short RELIC_CLOCKWORK = 110;
    public static final short RELIC_BANISH = 111;
    public static final short RELIC_VAMPIRE = 112;
    public static final short RELIC_EVASIVE = 113;
    public static final short RELIC_STUN = 114;
    public static final short RELIC_FLIGHT = 115;
    public static final short RELIC_HEAL_MOVE = 116;
    public static final short RELIC_GIFT_UNIT = 117;
    public static final short RELIC_RESET = 118;
    public static final short RELIC_SPELL_BLOCK = 119;
    public static final short RELIC_PARRY = 120;
    public static final short RELIC_EXPLODE = 121;
    // 122
    public static final short NONE = 123;
    public static final short UNIT_COUNT = 124;


    //////////////////////////
    // The starting army
    //////////////////////////
    public static int startingCount(short id) {
        switch (id) {
            // Tier 1
            case Unit.FOOTMAN:
                return 2;

            case Unit.BOWMAN:
                return 2;

            case Unit.CAVALRY:
                return 2;

            case Unit.PIKEMAN:
                return 2;

            case Unit.SCOUT:
                return 2;

            case Unit.TACTICIAN:
                return 2;

            // Tier 2
            case Unit.ARCHER:
                return 1;

            case Unit.GENERAL:
                return 1;

            case Unit.ASSASSIN:
                return 1;

            case Unit.RIDER:
                return 1;

            case Unit.CATAPULT:
                return 1;

            case Unit.BALLISTA:
                return 1;

            // Tier 3
            case Unit.KNIGHT:
                return 1;

            case Unit.WARRIOR:
                return 1;

            case Unit.RANGER:
                return 1;

            // Everything else
            default:
                return 0;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////

    private static ArrayList<Vector<Event>> makeArray() {
        ArrayList<Vector<Event>> ev = new ArrayList<Vector<Event>>(Event.EVENT_COUNT);
        ev.ensureCapacity(Event.EVENT_COUNT);
        for (int i = 0; i < Event.EVENT_COUNT; i++) { //ev.set(i, null);
            ev.add(null);
        }
        return ev;
    }

    private final ArrayList<Vector<Event>> events = Unit.makeArray();

    protected int eventPriority = 10;
    private final Vector<Short> bonus = new Vector<Short>();
    private short team = TEAM_NONE;
    protected short accessLevel = Unit.FREE;
    private boolean hidden = false;
    protected boolean skinwalking = false;
    private Unit soul = null;
    protected boolean opaque = false;
    protected short id;
    protected short category = CLASS_COUNT;
    private int sequence;
    private short location = -1;
    protected short lastLocation = -1;
    protected int stepX;
    protected int stepY;
    protected int stepSpeedX;
    protected int stepSpeedY;
    private float fade = 0f;
    private boolean killed = false;
    protected String name;
    protected Vector<Action> actions;
    protected short damage;
    protected short armor;
    protected short life;
    protected short lifeMax;
    protected short actionsLeft;
    protected short actionsMax;
    protected Action move;
    protected Action attack;
    protected short deployCost;
    protected int castleCost;
    protected boolean organic;
    protected boolean isPowerUp = false;
    protected boolean isRelic = false;
    protected boolean canWin = true;
    protected boolean freelyActs = false;
    protected boolean noCost = false;
    protected int appearance;
    private transient BattleField battleField = null;
    protected Castle castle = null;
    protected boolean deployed = false;
    protected boolean stunned = false;
    private boolean dead = false;
    private boolean murderer = false;
    private boolean poisoned = false;
    private boolean mechanical = false;
    private boolean targetable = true;
    private boolean enemyTargetable = true;
    private boolean friendTargetable = true;
    private boolean armistice = false;
    private boolean rested = true;
    private boolean boss = false;
    private boolean doomed = false;
    protected boolean haunted = false;
    private boolean erased = false;
    private Unit soulmate = null;

    // Client related
    private final Unit link = null;

    // AI related
    private Script script = null;


    /////////////////////////////////////////////////////////////////
    // Get the level of a unit
    /////////////////////////////////////////////////////////////////
    public static int getLevel(short id) {
        for (int x = 0; x < UNITS.length; x++) {
            for (int y = 0; y < UNITS[x].length; y++)
                if (UNITS[x][y] == id) return x + 1;
        }
        return 0;
    }


    /////////////////////////////////////////////////////////////////
    // Get the cost of the unit
    /////////////////////////////////////////////////////////////////
    public static int getCost(short id) { // To avoid cost issues
        //return 0;
  /*//for testing new units, make them free. note that this will only work if there is no specified access level for a unit
   //if(id==Unit.DUELIST || id==Unit.MILITIA || id==Unit.CONSPIRATOR 
        || id==Unit.NEWUNIT3 || id==Unit.NEWUNIT4 || id==Unit.NEWUNIT5 
        || id==Unit.NEWUNIT6 || id==Unit.NEWUNIT7 || id==Unit.NEWUNIT8 
        || id==Unit.NEWUNIT9 || id==Unit.NEWUNIT10 || id==Unit.NEWUNIT11)
   {
     int cost = 0;
   return cost;
   }*/
        int level = Unit.getLevel(id) - 1;
        int cost = 25;
        int increase = 10;
        for (int i = 0; i < level; i++) {
            cost += increase;
            increase += 10;
        }
        return cost;
    }


    /////////////////////////////////////////////////////////////////
    // All the units in a class
    /////////////////////////////////////////////////////////////////
    public static Vector getUnits(int unitClass, Castle forCastle) {
        Vector<Unit> units = new Vector<Unit>();

        switch (unitClass) {
            case Unit.ARCHERS:
                units.add(Unit.getUnit(Unit.ARCHER, forCastle));
                units.add(Unit.getUnit(Unit.BOWMAN, forCastle));
                units.add(Unit.getUnit(Unit.CROSSBOWMAN, forCastle));
                units.add(Unit.getUnit(Unit.FIRE_ARCHER, forCastle));
                units.add(Unit.getUnit(Unit.LONGBOWMAN, forCastle));
                return units;

            case Unit.BLACK_MAGIC_USERS:
                units.add(Unit.getUnit(Unit.DIABOLIST, forCastle));
                units.add(Unit.getUnit(Unit.NECROMANCER, forCastle));
                units.add(Unit.getUnit(Unit.SUMMONER, forCastle));
                units.add(Unit.getUnit(Unit.WARLOCK, forCastle));
                units.add(Unit.getUnit(Unit.WITCH, forCastle));
                return units;

            case Unit.CLERGY:
                units.add(Unit.getUnit(Unit.ACOLYTE, forCastle));
                units.add(Unit.getUnit(Unit.ARCHANGEL, forCastle));
                units.add(Unit.getUnit(Unit.HEALER, forCastle));
                units.add(Unit.getUnit(Unit.PALADIN, forCastle));
                units.add(Unit.getUnit(Unit.PRIEST, forCastle));
                units.add(Unit.getUnit(Unit.SHIELD_MAIDEN, forCastle));
                units.add(Unit.getUnit(Unit.TEMPLAR, forCastle));
                return units;

            case Unit.COMMANDERS:
                units.add(Unit.getUnit(Unit.CAPTAIN, forCastle));
                units.add(Unit.getUnit(Unit.DIPLOMAT, forCastle));
                units.add(Unit.getUnit(Unit.GENERAL, forCastle));
                units.add(Unit.getUnit(Unit.QUARTERMASTER, forCastle));
                units.add(Unit.getUnit(Unit.SERGEANT, forCastle));
                units.add(Unit.getUnit(Unit.STRATEGIST, forCastle));
                units.add(Unit.getUnit(Unit.SYCOPHANT, forCastle));
                units.add(Unit.getUnit(Unit.TACTICIAN, forCastle));
                units.add(Unit.getUnit(Unit.CONSPIRATOR, forCastle));
                return units;

            case Unit.CULTISTS:
                units.add(Unit.getUnit(Unit.CONFESSOR, forCastle));
                units.add(Unit.getUnit(Unit.FANATIC, forCastle));
                units.add(Unit.getUnit(Unit.HERETIC, forCastle));
                units.add(Unit.getUnit(Unit.MARTYR, forCastle));
                units.add(Unit.getUnit(Unit.MOURNER, forCastle));
                units.add(Unit.getUnit(Unit.POSSESSED, forCastle));
                units.add(Unit.getUnit(Unit.SUPPLICANT, forCastle));
                return units;

            case Unit.HORSEMEN:
                units.add(Unit.getUnit(Unit.CAVALRY, forCastle));
                units.add(Unit.getUnit(Unit.KNIGHT, forCastle));
                units.add(Unit.getUnit(Unit.LANCER, forCastle));
                units.add(Unit.getUnit(Unit.MOUNTED_ARCHER, forCastle));
                units.add(Unit.getUnit(Unit.RIDER, forCastle));
                units.add(Unit.getUnit(Unit.WAR_ELEPHANT, forCastle));
                return units;

            case Unit.NATURE:
                units.add(Unit.getUnit(Unit.BARBARIAN, forCastle));
                units.add(Unit.getUnit(Unit.BERSERKER, forCastle));
                units.add(Unit.getUnit(Unit.CHANNELER, forCastle));
                units.add(Unit.getUnit(Unit.CHIEFTAIN, forCastle));
                units.add(Unit.getUnit(Unit.DRUID, forCastle));
                units.add(Unit.getUnit(Unit.GEOMANCER, forCastle));
                units.add(Unit.getUnit(Unit.SHAMAN, forCastle));
                return units;

            case Unit.SCOUTS:
                units.add(Unit.getUnit(Unit.ASSASSIN, forCastle));
                units.add(Unit.getUnit(Unit.BOUNTY_HUNTER, forCastle));
                units.add(Unit.getUnit(Unit.RANGER, forCastle));
                units.add(Unit.getUnit(Unit.ROGUE, forCastle));
                units.add(Unit.getUnit(Unit.SCOUT, forCastle));
                return units;

            case Unit.SHAPESHIFTERS:
                units.add(Unit.getUnit(Unit.CHANGELING, forCastle));
                units.add(Unit.getUnit(Unit.DOPPELGANGER, forCastle));
                units.add(Unit.getUnit(Unit.LYCANTHROPE, forCastle));
                units.add(Unit.getUnit(Unit.MIMIC, forCastle));
                units.add(Unit.getUnit(Unit.SKINWALKER, forCastle));
                return units;

            case Unit.SIEGE:
                units.add(Unit.getUnit(Unit.BALLISTA, forCastle));
                units.add(Unit.getUnit(Unit.CATAPULT, forCastle));
                units.add(Unit.getUnit(Unit.GOLEM, forCastle));
                units.add(Unit.getUnit(Unit.MASON, forCastle));
                return units;

            case Unit.SOLDIERS:
                units.add(Unit.getUnit(Unit.AXEMAN, forCastle));
                units.add(Unit.getUnit(Unit.FOOTMAN, forCastle));
                units.add(Unit.getUnit(Unit.GATE_GUARD, forCastle));
                units.add(Unit.getUnit(Unit.MILITIA, forCastle));
                units.add(Unit.getUnit(Unit.PIKEMAN, forCastle));
                units.add(Unit.getUnit(Unit.SHIELD_BEARER, forCastle));
                units.add(Unit.getUnit(Unit.SWORDSMAN, forCastle));
                units.add(Unit.getUnit(Unit.WARRIOR, forCastle));
                units.add(Unit.getUnit(Unit.DUELIST, forCastle));
                return units;

            case Unit.STRUCTURES:
                units.add(Unit.getUnit(Unit.ABBEY, forCastle));
                units.add(Unit.getUnit(Unit.ARMORY, forCastle));
                units.add(Unit.getUnit(Unit.BARRACKS, forCastle));
                units.add(Unit.getUnit(Unit.COMMAND_POST, forCastle));
                units.add(Unit.getUnit(Unit.TOWER, forCastle));
                return units;

            case Unit.WHITE_MAGIC_USERS:
                units.add(Unit.getUnit(Unit.ABJURER, forCastle));
                units.add(Unit.getUnit(Unit.ALCHEMIST, forCastle));
                units.add(Unit.getUnit(Unit.ARTIFICER, forCastle));
                units.add(Unit.getUnit(Unit.CONJURER, forCastle));
                units.add(Unit.getUnit(Unit.ENCHANTER, forCastle));
                units.add(Unit.getUnit(Unit.MAGUS, forCastle));
                units.add(Unit.getUnit(Unit.WIZARD, forCastle));
                return units;

            case Unit.WYRMS:
                units.add(Unit.getUnit(Unit.DRACOLICH, forCastle));
                units.add(Unit.getUnit(Unit.DRAGON, forCastle));
                units.add(Unit.getUnit(Unit.FEATHERED_SERPENT, forCastle));
                units.add(Unit.getUnit(Unit.HYDRA, forCastle));
                units.add(Unit.getUnit(Unit.WYVERN, forCastle));
                return units;

            case Unit.RELICS:
                units.add(Unit.getUnit(Unit.RELIC_CLOCKWORK, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_EVASIVE, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_VAMPIRE, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_FLIGHT, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_BANISH, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_STUN, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_HEAL_MOVE, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_GIFT_UNIT, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_RESET, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_SPELL_BLOCK, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_PARRY, forCastle));
                units.add(Unit.getUnit(Unit.RELIC_EXPLODE, forCastle));
                return units;

        }
        return units;
    }


    /////////////////////////////////////////////////////////////////
    // Get the class name
    /////////////////////////////////////////////////////////////////
    public static String getClassName(int name) {
        return classNames[name];
    }


    /////////////////////////////////////////////////////////////////
    // Static to get a new unit of specified type
    /////////////////////////////////////////////////////////////////
    public static Unit getUnit(short id, Castle castle) {
        switch (id) {
            case Unit.FOOTMAN:
                return new UnitFootman(castle);
            case Unit.BOWMAN:
                return new UnitBowman(castle);
            case Unit.CAVALRY:
                return new UnitCavalry(castle);
            case Unit.ARCHER:
                return new UnitArcher(castle);
            case Unit.PIKEMAN:
                return new UnitPikeman(castle);
            case Unit.KNIGHT:
                return new UnitKnight(castle);
            case Unit.RANGER:
                return new UnitRanger(castle);
            case Unit.WOLF:
                return new UnitWolf(castle);
            case Unit.SUMMONER:
                return new UnitSummoner(castle);
            case Unit.IMP:
                return new UnitImp(castle);
            case Unit.DEMON:
                return new UnitDemon(castle);
            case Unit.ARCHDEMON:
                return new UnitArchDemon(castle);
            case Unit.PRIEST:
                return new UnitPriest(castle);
            case Unit.ENCHANTER:
                return new UnitEnchanter(castle);
            case Unit.TEMPLAR:
                return new UnitTemplar(castle);
            case Unit.WARRIOR:
                return new UnitWarrior(castle);
            case Unit.RIDER:
                return new UnitRider(castle);
            case Unit.HEALER:
                return new UnitHealer(castle);
            case Unit.WIZARD:
                return new UnitWizard(castle);
            case Unit.SCOUT:
                return new UnitScout(castle);
            case Unit.ASSASSIN:
                return new UnitAssassin(castle);
            case Unit.TACTICIAN:
                return new UnitTactician(castle);
            case Unit.GENERAL:
                return new UnitGeneral(castle);
            case Unit.WALL:
                return new UnitWall(castle);
            case Unit.MASON:
                return new UnitMason(castle);
            case Unit.CATAPULT:
                return new UnitCatapult(castle);
            case Unit.BALLISTA:
                return new UnitBallista(castle);
            case Unit.NECROMANCER:
                return new UnitNecromancer(castle);
            case Unit.LICH:
                return new UnitLich(castle);
            case Unit.SKELETON:
                return new UnitSkeleton(castle);
            case Unit.ZOMBIE:
                return new UnitZombie(castle);
            case Unit.SERGEANT:
                return new UnitSergeant(castle);
            case Unit.ABJURER:
                return new UnitAbjurer(castle);
            case Unit.SEAL:
                return new UnitSeal(castle);
            case Unit.WARLOCK:
                return new UnitWarlock(castle);
            case Unit.CROSSBOWMAN:
                return new UnitCrossbowman(castle);
            case Unit.DRAGON:
                return new UnitDragon(castle);
            case Unit.DRACOLICH:
                return new UnitDracolich(castle);
            case Unit.HYDRA:
                return new UnitHydra(castle);
            case Unit.TOWER:
                return new UnitTower(castle);
            case Unit.COMMAND_POST:
                return new UnitCommandPost(castle);
            case Unit.BARRACKS:
                return new UnitBarracks(castle);
            case Unit.SOLDIER:
                return new UnitSoldier(castle);
            case Unit.DRUID:
                return new UnitDruid(castle);
            case Unit.CHANNELER:
                return new UnitChanneler(castle);
            case Unit.LYCANTHROPE:
                return new UnitLycanthrope(castle);
            case Unit.WEREWOLF:
                return new UnitWerewolf(castle);
            case Unit.LYCANWOLF:
                return new UnitLycanWolf(castle);
            case Unit.MOUNTED_ARCHER:
                return new UnitMountedArcher(castle);
            case Unit.GEOMANCER:
                return new UnitGeomancer(castle);
            case Unit.ROCK:
                return new UnitRock(castle, null);
            case Unit.SWORDSMAN:
                return new UnitSwordsman(castle);
            case Unit.WITCH:
                return new UnitWitch(castle);
            case Unit.TOAD:
                return new UnitToad(castle);
            case Unit.SHIELD_MAIDEN:
                return new UnitShieldMaiden(castle);
            case Unit.MAGUS:
                return new UnitMagus(castle);
            case Unit.SPIRIT:
                return new UnitSpirit(castle);
            case Unit.WILL_O_THE_WISPS:
                return new UnitWillWisps(castle);
            case Unit.GOLEM:
                return new UnitGolem(castle);
            case Unit.ARMORY:
                return new UnitArmory(castle);
            case Unit.SERPENT:
                return new UnitSerpent(castle);
            case Unit.FIRE_ARCHER:
                return new UnitFireArcher(castle);
            case Unit.MIMIC:
                return new UnitMimic(castle);
            case Unit.PALADIN:
                return new UnitPaladin(castle);
            case Unit.SHAMAN:
                return new UnitShaman(castle);
            case Unit.MARTYR:
                return new UnitMartyr(castle);
            case Unit.ROGUE:
                return new UnitRogue(castle);
            case Unit.DIABOLIST:
                return new UnitDiabolist(castle);
            case Unit.GHOST:
                return new UnitGhost(castle);
            case Unit.GATE_GUARD:
                return new UnitGateGuard(castle);
            case Unit.FEATHERED_SERPENT:
                return new UnitFeatheredSerpent(castle);
            case Unit.BERSERKER:
                return new UnitBerserker(castle);
            case Unit.ARTIFICER:
                return new UnitArtificer(castle);
            case Unit.CHANGELING:
                return new UnitChangeling(castle);
            case Unit.DOPPELGANGER:
                return new UnitDoppelganger(castle);
            case Unit.SKINWALKER:
                return new UnitSkinwalker(castle);
            case Unit.ACOLYTE:
                return new UnitAcolyte(castle);
            case Unit.AXEMAN:
                return new UnitAxeman(castle);
            case Unit.MOURNER:
                return new UnitMourner(castle);
            case Unit.HERETIC:
                return new UnitHeretic(castle);
            case Unit.WAR_ELEPHANT:
                return new UnitWarElephant(castle);
            case Unit.FANATIC:
                return new UnitFanatic(castle);
            case Unit.DISMOUNTED_KNIGHT:
                return new UnitDismountedKnight(castle);
            case Unit.BEAR:
                return new UnitBear(castle);
            case Unit.QUARTERMASTER:
                return new UnitQuartermaster(castle);
            case Unit.WALL_MASON:
                return new UnitWallMason(castle);
            case Unit.CONFESSOR:
                return new UnitConfessor(castle);
            case Unit.STRATEGIST:
                return new UnitStrategist(castle);
            case Unit.POSSESSED:
                return new UnitPossessed(castle);
            case Unit.BARBARIAN:
                return new UnitBarbarian(castle);
            case Unit.ALCHEMIST:
                return new UnitAlchemist(castle);
            case Unit.BOUNTY_HUNTER:
                return new UnitBountyHunter(castle);
            case Unit.SHIELD_BEARER:
                return new UnitShieldBearer(castle);
            case Unit.CHIEFTAIN:
                return new UnitChieftain(castle);
            case Unit.LANCER:
                return new UnitLancer(castle);
            case Unit.ARCHANGEL:
                return new UnitArchangel(castle);
            case Unit.CONJURER:
                return new UnitConjurer(castle);
            case Unit.PORTAL:
                return new UnitPortal(castle);
            case Unit.GATE:
                return new UnitGate(castle);
            case Unit.DIPLOMAT:
                return new UnitDiplomat(castle, true);
            case Unit.DIPLOMAT_USED:
                return new UnitDiplomat(castle, false);
            case Unit.LONGBOWMAN:
                return new UnitLongbowman(castle);
            case Unit.SYCOPHANT:
                return new UnitSycophant(castle);
            case Unit.WYVERN:
                return new UnitWyvern(castle);
            case Unit.EGG:
                return new UnitEgg(castle);
            case Unit.CAPTAIN:
                return new UnitCaptain(castle);
            case Unit.ABBEY:
                return new UnitAbbey(castle);
            case Unit.SUPPLICANT:
                return new UnitSupplicant(castle);
            case Unit.DUELIST:
                return new UnitDuelist(castle);
            case Unit.MILITIA:
                return new UnitMilitia(castle);
            case Unit.CONSPIRATOR:
                return new UnitConspirator(castle);
            case Unit.RELIC_CLOCKWORK:
                return new RelicClockwork(castle);
            case Unit.RELIC_BANISH:
                return new RelicBanish(castle);
            case Unit.RELIC_EVASIVE:
                return new RelicEvasive(castle);
            case Unit.RELIC_VAMPIRE:
                return new RelicVampire(castle);
            case Unit.RELIC_STUN:
                return new RelicStun(castle);
            case Unit.RELIC_FLIGHT:
                return new RelicFlight(castle);
            case Unit.RELIC_HEAL_MOVE:
                return new RelicHealMove(castle);
            case Unit.RELIC_GIFT_UNIT:
                return new RelicGiftUnit(castle);
            case Unit.RELIC_RESET:
                return new RelicReset(castle);
            case Unit.RELIC_SPELL_BLOCK:
                return new RelicSpellBlock(castle);
            case Unit.RELIC_PARRY:
                return new RelicParry(castle);
            case Unit.RELIC_EXPLODE:
                return new RelicExplode(castle);
            case Unit.NONE:
                return new UnitNone(castle);
            case Unit.POWERUP_TOXIC:
            case Unit.POWERUP_EVASIVE:
            case Unit.POWERUP_RESILIENT:
            case Unit.POWERUP_LONGSHANK:
            case Unit.POWERUP_MIGHTY:
            case Unit.POWERUP_CLOCKWORK:
            case Unit.POWERUP_VAMPIRIC:
            case Unit.POWERUP_CUNNING:
            case Unit.POWERUP_EPIC:
            case Unit.POWERUP_ARCANE:
            case Unit.POWERUP_ASCENDANT:
            case Unit.POWERUP_GUARDIAN:
            case Unit.POWERUP_VIGILANT:
            case Unit.POWERUP_ZEALOUS:
            case Unit.POWERUP_RAMPAGING:
            case Unit.POWERUP_RUTHLESS:
            case Unit.POWERUP_ENRAGED:
                return new RelicPowerUp(castle, (byte) ((0 - id) + Unit.POWERUP));
            default:
                return null;
        }
    }

    ////////////////////////////////////////////////////////////////
    // get a powerup with specific power
    ////////////////////////////////////////////////////////////////
 /*public static Unit getPowerUp(short power, Castle castle)
 {
   return new RelicPowerUp(castle,power);
 }*/


    /////////////////////////////////////////////////////////////////
    // Interpret command
    /////////////////////////////////////////////////////////////////
    public String process(short action, short ID, short target) {
        if (action < 50) {
            if (actions.size() - 1 < action) return Strings.INVALID_ACTION;
            Action tmpAction = actions.elementAt(action);
            return tmpAction.perform(target);
            //return;
        }
        return Strings.INVALID_ACTION;
    }


    /////////////////////////////////////////////////////////////////
    // Ready
    /////////////////////////////////////////////////////////////////
    public boolean ready() {
        if (isDead()) return false;
        if (getID() == Unit.WALL ||
                getID() == Unit.SEAL ||
                getID() == Unit.ROCK ||
                getID() == Unit.ARMORY) return true;

        Iterator<Action> it = actions.iterator();
        while (it.hasNext()) {
            Action action = it.next();
            if (action.getRemaining() > 0) return true;
        }
        return false;
    }


    /////////////////////////////////////////////////////////////////
    // Skinwalk
    /////////////////////////////////////////////////////////////////
    public void skinwalk() {
        if (!isSkinwalking()) return;
        if (!targetable(this)) return;
        //if (!getOrganic(this)) return;
        if (getSoul() == null) return;
        if (getSoul().getID() == Unit.NONE) return;

        // transform
        short oldID = getID();
        die(false, this);

        // Put a soul in it's place
        Unit newUnit = Unit.getUnit(soul.getID(), getCastle());
        newUnit.setID(oldID);
        newUnit.setName("Skinwalker");
        newUnit.setLocation(getLocation());
        newUnit.setBattleField(getCastle().getBattleField());
        getCastle().getBattleField().add(newUnit);
        getCastle().addOut(getTeam(), newUnit);
        newUnit.unDeploy();

        // set the trait up
        newUnit.grow(getBonus());
        newUnit.grow(Action.GROW_SKINWALKING);

        // Generate a nifty effect
        getCastle().getObserver().unitEffect(this, Action.EFFECT_FADE);
        newUnit.setHidden(true);
        getCastle().getObserver().unitEffect(newUnit, Action.EFFECT_FADE_IN);

        getCastle().getObserver().playSound(Constants.SOUND_SHAPESHIFT);  //getCastle().getObserver().playSound(ClientImages.SOUND_SHAPESHIFT); }
    }

    /////////////////////////////////////////////////////////////////
    // Damage
    /////////////////////////////////////////////////////////////////
    public short damage(Unit source, short rawAmount) {
        // a variable to play with
        short amount = rawAmount;

        // the preview damage event
        amount = getBattleField().event(Event.PREVIEW_DAMAGE, source, this, Event.NONE, Event.NONE, amount);

        // if the damage was cancelled,
        if (amount == Event.CANCEL) return Event.CANCEL;

        // the WITNESS_DAMAGE event should receive amount of life lost (less than damage amount if life went below 0)
        boolean wentPast = (amount - getArmor()) > life;
        short oldLife = life;

        // do the damage
        short result = directDamage(source, amount);
        short eventAmount = result;

        // the witness damage event
        if (wentPast)
            eventAmount = oldLife;
        getBattleField().event(Event.WITNESS_DAMAGE, source, this, eventAmount, Event.NONE, Event.OK);

        return result;
    }


    /////////////////////////////////////////////////////////////////
    // Damage, no redirect.
    /////////////////////////////////////////////////////////////////
    public short directDamage(Unit source, short amount) {
        short inflicted = (byte) (amount - getArmor());
        if (inflicted < 0) inflicted = 0;
        life -= inflicted;
        if (life <= 0) die(true, source);

        return inflicted;
    }


    /////////////////////////////////////////////////////////////////
    // Die
    /////////////////////////////////////////////////////////////////
    public void die(boolean death, Unit source) {
        dead = true;
        if (!death) doomed = true;
        // preview the death
        if (death) {
            short result = getBattleField().event(Event.PREVIEW_DEATH, this, source, Event.NONE, Event.NONE, Event.NONE);
            if (result == Event.CANCEL) {
                if (!doomed) dead = false;
                return;
            }
        }
        if (death && getOrganic(this)) {
            getBattleField().addGrave(location);
        }
        getBattleField().remove(this);
        getCastle().removeOut(this);
        deathTrigger(death, source);

        if (death) {
            if (getOrganic(this)) getCastle().addGraveyard(this);
            if (getOrganic(this)) {
                if (source.getOrganic(source)) {
                    source.setMurderer();
                }
            }
            //getCastle().getObserver().death(this, source);
        }

        short deathType = death ? Event.TRUE : Event.FALSE;

        // the death event
        //tmp.witnessDeath(deathType, source, this);
        getBattleField().event(Event.WITNESS_DEATH, this, source, deathType, Event.NONE, Event.OK);

        // skinwalker hack, if death was not a summoned unit
        //boolean summoned = id == NONE; //this instanceof UnitSummon || (this instanceof UnitWolf && ((UnitWolf)this).isSummoned());

        // sycophant hack, kill linked units regardless of death type
        //if (getSoulmate() != null)
        //{
        //   getSoulmate().die(false, source);
        //}

        if (id != NONE) {
            Vector<Unit> units = getBattleField().getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit tmp = it.next();
                if (tmp.isSkinwalking() && death && getOrganic(this) && tmp != this && getID() != Unit.NONE && targetable(tmp)) {
                    tmp.setSoul(this);
                    getCastle().getObserver().attack(tmp, this, tmp.getLocation(), Action.ATTACK_SPIRIT);
                }
            }
        }


    }


    /////////////////////////////////////////////////////////////////
    // unDie
    /////////////////////////////////////////////////////////////////
    public void unDie() {
        dead = false;
    }


    /////////////////////////////////////////////////////////////////
    // Deduct the cost
    /////////////////////////////////////////////////////////////////
    public void deductActions(short amount) {
        rested = false;
        if (!noCost) actionsLeft -= amount;
    }


    /////////////////////////////////////////////////////////////////
    // Refresh
    /////////////////////////////////////////////////////////////////
    public void refresh() {
        deployed = true;
        stunned = false;
        actionsLeft = actionsMax;

        refreshActions();

        // wither and die
        if (isPoisoned() && getOrganic(this) && targetable(this)) {
            getCastle().getObserver().abilityUsed(getLocation(), getLocation(), Constants.IMG_DEATH);   //getCastle().getObserver().abilityUsed(getLocation(), getLocation(), ClientImages.IMG_DEATH);
            lowerLife((byte) 1, this);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Refresh actions
    /////////////////////////////////////////////////////////////////
    private void refreshActions() {
        Stack<Action> stack = new Stack<Action>();

        Iterator<Action> it = actions.iterator();
        while (it.hasNext()) {
            Action action = it.next();
            stack.push(action);
        }

        while (!stack.empty()) {
            Action action = stack.pop();
            action.refresh();
        }
    }


    /////////////////////////////////////////////////////////////////
    // start actions
    /////////////////////////////////////////////////////////////////
    private void startActions() {
        Stack<Action> stack = new Stack<Action>();

        Iterator<Action> it = actions.iterator();
        while (it.hasNext()) {
            Action action = it.next();
            stack.push(action);
        }

        while (!stack.empty()) {
            Action action = stack.pop();
            action.startTurn();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Perform an action
    /////////////////////////////////////////////////////////////////
    public String performAction(short actionNumber, short target) {
        if (actionNumber > getActions().size()) return "";
        Action action = getActions().elementAt(actionNumber);
        return action.perform(target);
    }


    /////////////////////////////////////////////////////////////////
    // Get the # of an action
    /////////////////////////////////////////////////////////////////
    public short getAction(Action action) {
        Iterator<Action> it = actions.iterator();
        short i = 0;
        while (it.hasNext()) {
            Action tmpAction = it.next();
            if (tmpAction == action) return i;
            i++;
        }
        System.out.println("Invalid action from getAction: " + action.getName());
        return 100;
    }


    /////////////////////////////////////////////////////////////////
    // Add an event
    /////////////////////////////////////////////////////////////////
    public void add(Event newEvent) {
        short type = newEvent.getEventType();
        if (events.get(type) == null) {
            events.set(type, new Vector<Event>());
        }
        events.get(type).add(newEvent);
    }


    /////////////////////////////////////////////////////////////////
    // Remove an event
    /////////////////////////////////////////////////////////////////
    public void remove(Event oldEvent) {
        short type = oldEvent.getEventType();
        if (events.get(type) == null) {
            return;
        }
        events.get(type).remove(oldEvent);
    }


    /////////////////////////////////////////////////////////////////
    // Get the events
    /////////////////////////////////////////////////////////////////
    public Vector<Event> getEvents(short type) {
        return events.get(type);
    }


    /////////////////////////////////////////////////////////////////
    // Add an action
    /////////////////////////////////////////////////////////////////
    public void add(Action newAction) {
        actions.add(newAction);
    }


    /////////////////////////////////////////////////////////////////
    // Remove an action
    /////////////////////////////////////////////////////////////////
    public void remove(Action oldAction) {
        actions.remove(oldAction);
        if (oldAction instanceof Event)
            remove((Event) oldAction);
    }


    /////////////////////////////////////////////////////////////////
    // Set deployed to false
    /////////////////////////////////////////////////////////////////
    public void stun() { //deployed = false;
        stunned = true;
    }


    /////////////////////////////////////////////////////////////////
    // Set deployed to false
    /////////////////////////////////////////////////////////////////
    public void unDeploy() {
        deployed = true;
    }


    /////////////////////////////////////////////////////////////////
    // predeploy processor
    /////////////////////////////////////////////////////////////////
    public void preDeploy(short location) {
    }


    /////////////////////////////////////////////////////////////////
    // Deployed event
    /////////////////////////////////////////////////////////////////
    public void entered() {
    }


    /////////////////////////////////////////////////////////////////
    // Removed from play
    /////////////////////////////////////////////////////////////////
    public void removed() {
    }


    /////////////////////////////////////////////////////////////////
    // Start a turn
    /////////////////////////////////////////////////////////////////
    public void deathTrigger(boolean death, Unit source) {
    }


    /////////////////////////////////////////////////////////////////
    // Start a turn
    /////////////////////////////////////////////////////////////////
    public void startTurn() {
        startActions();
        rested = true;
    }

    /////////////////////////////////////////////////////////////////
    // Get the deploy cost
    /////////////////////////////////////////////////////////////////
    public short getDeployCost() {
        short deploy = (byte) (deployCost - getCastle().getLogistics());
        return deploy < 0 ? 0 : deploy;
    }


    /////////////////////////////////////////////////////////////////
    // Get the armor
    /////////////////////////////////////////////////////////////////
    public short getArmor() {
        short newArmor = organic && targetable(this) ? (byte) (armor + getCastle().getArmor()) : armor;
        if (newArmor < 0) newArmor = 0;
        return newArmor > 2 ? 2 : newArmor;
    }

    public short getBaseArmor() {
        return armor;
    }

    /////////////////////////////////////////////////////////////////
    // Get the power
    /////////////////////////////////////////////////////////////////
    public short getDamage() {
        short newPower = organic ? (byte) (damage + getCastle().getPower()) : damage;
        if (organic && newPower == getCastle().getPower()) newPower = 0;
        return newPower;
    }

    public short getBaseDamage() {
        return damage;
    }

    /////////////////////////////////////////////////////////////////
    // can this target given unit (used by powerups)
    /////////////////////////////////////////////////////////////////
    public boolean canTarget(Unit target) {
        return false;
    }

    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getCastleTargets() {
        Vector<Short> targets = getCastle().getBattleField().getCastleTargets(getCastle());
        getCastle().getBattleField().addBonusCastleTargets(this, targets, getCastle());
        return targets;
    }


    /////////////////////////////////////////////////////////////////
    // Get the castle targets
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getBonusCastleTargets(Unit looker) {
        return null;
    }


    /////////////////////////////////////////////////////////////////
    // Replace their old movement type with this
    /////////////////////////////////////////////////////////////////
    public void setMove(Action newMove) {
        if (move != null) remove(move);
        add(newMove);
        move = newMove;
    }


    /////////////////////////////////////////////////////////////////
    // Get the maximum range of actions
    /////////////////////////////////////////////////////////////////
    public int getMaxRange() {
        int range = 0;
        Vector<Action> tmpActions = getActions();

        Iterator<Action> it = tmpActions.iterator();
        while (it.hasNext()) {
            Action tmpAction = it.next();
            if (tmpAction != getMove())
                range = tmpAction.getRange() > range ? tmpAction.getRange() : range;
        }
        return range;
    }

    /////////////////////////////////////////////////////////////////
    // Get the maximum range of attacks
    /////////////////////////////////////////////////////////////////
    public int getMaxAttackRange() {
        int range = 0;
        Vector<Action> tmpActions = getActions();
        Iterator<Action> it = tmpActions.iterator();
        while (it.hasNext()) {
            Action tmpAction = it.next();
            if (tmpAction != getMove() && tmpAction.getType() == Action.ATTACK)
                range = tmpAction.getRange() > range ? tmpAction.getRange() : range;
        }
        return range;
    }


    /////////////////////////////////////////////////////////////////
    // Poison a unit
    /////////////////////////////////////////////////////////////////
    public void poison() {
        grow(Action.GROW_POISONED);
    }


    /////////////////////////////////////////////////////////////////
    // Heal a unit
    /////////////////////////////////////////////////////////////////
    public boolean heal(Unit source) {
        if (getLife() < getLifeMax()) {
            setLife(getLifeMax());
            return true;
        } else {
            return false;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Heal a unit
    /////////////////////////////////////////////////////////////////
    public short heal(short amount, Unit source) {
        // if it's max, don't heal
        if (getLife() >= getLifeMax()) return 0;

        // set the healed return value
        short healed = amount;

        // do some healing
        setLife((byte) (getLife() + amount));

        // compute overheal
        short overHeal = (byte) (getLife() - getLifeMax());
        if (overHeal > 0) {
            setLife(getLifeMax());
            healed -= overHeal;
        }

        return healed;
    }


    /////////////////////////////////////////////////////////////////
    // Raise life
    /////////////////////////////////////////////////////////////////
    public void raiseLife(short byThis) {
        if (getLifeMax() + byThis > (byte) 99) {
            setLifeMax((byte) 99);
        } else
            setLifeMax((byte) (getLifeMax() + byThis));
        if (getLife() + byThis > (byte) 99) {
            setLife((byte) 99);
        } else
            setLife((byte) (getLife() + byThis));
    }


    /////////////////////////////////////////////////////////////////
    // Lower life
    /////////////////////////////////////////////////////////////////
    public void lowerLife(short byThis, Unit source) {
        setLife((byte) (getLife() - byThis));
        setLifeMax((byte) (getLifeMax() - byThis));
        if (getLife() < 1) die(true, source);
        if (isDead()) {
            getCastle().getObserver().death(this);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Get Power
    /////////////////////////////////////////////////////////////////
    public short getPower() {
        return (byte) 0;
    }

    /////////////////////////////////////////////////////////////////
    // Grow a lot
    /////////////////////////////////////////////////////////////////
    public void grow(Vector<Short> growths) {
        rested = false;
        Iterator<Short> it = growths.iterator();
        while (it.hasNext()) {
            Short growme = it.next();
            grow(growme.byteValue());
        }
    }


    /////////////////////////////////////////////////////////////////
    // Grow. Used by the AI
    /////////////////////////////////////////////////////////////////
    public void grow(short newBonus) {
        if (newBonus < 0) return;

        if (newBonus < Action.GROW_COUNT) boss = true;

        EventPowerUpDrop eventPowerUpDrop = new EventPowerUpDrop(this, newBonus);

        // generate effect
  /*try{
  if(castle != null && castle.isAI()){
    if (newBonus < Action.GROW_COUNT)
    {
      Unit power = getPowerUp(newBonus,castle);
      castle.getObserver().attack( power, this, (byte) 0, Action.ATTACK_RELIC);
    }
  }
  }catch (Exception e){
   System.out.println("Unit.grow(byte): " + e); 
  }*/

        switch (newBonus) {
            case Action.GROW_RUTHLESS:
    /*if (damage <= 0 || getAttack() == null || getAttack().getMax() > 0)
    { grow(Action.GROW_CUNNING);
     return;
    }*/
                EventWhirlwind eventWhirlwind = new EventWhirlwind(this);
                add((Event) eventWhirlwind);
                actions.add(0, eventWhirlwind);
                life++;
                lifeMax++;
                name = "Ruthless " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;


            case Action.GROW_GUARDIAN:
                EventGuardian eventGuardian = new EventGuardian(this);
                add((Event) eventGuardian);
                actions.add(0, eventGuardian);
                life += 2;
                lifeMax += 2;
                name = "Guardian " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_VIGILANT:
    /*if (damage <= 0)
    { grow(Action.GROW_GUARDIAN);
     return;
    }*/
                EventVigilant eventVigilant = new EventVigilant(this);
                add((Event) eventVigilant);
                actions.add(0, eventVigilant);
                life++;
                lifeMax++;
                name = "Vigilant " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_ZEALOUS:
    /*if (damage <= 0 || this instanceof UnitFanatic)
    { grow(Action.GROW_CLOCKWORK);
     return;
    }*/
                EventZeal eventZeal = new EventZeal(this);
                add((Event) eventZeal);
                actions.add(0, eventZeal);
                life += 2;
                lifeMax += 2;
                armor += 1;
                name = "Zealous " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_RAMPAGING:
    /*if (damage <= 0)
    { grow(Action.GROW_LONGSHANK);
     return;
    }*/
                EventRampage eventRampage = new EventRampage(this);
                add((Event) eventRampage);
                actions.add(0, eventRampage);
                damage += 2;
                life++;
                lifeMax++;
                name = "Rampaging " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_TOXIC:
    /*if (damage <= 0)
    { grow(Action.GROW_EVASIVE);
     return;
    }*/
                EventPoison eventPoison = new EventPoison(this);
                add((Event) eventPoison);
                actions.add(0, eventPoison);
                life++;
                lifeMax++;
                name = "Toxic " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;


            case Action.GROW_EVASIVE:
                EventDodge eventDodge = new EventDodge(this);
                add((Event) eventDodge);
                actions.add(0, eventDodge);
                life++;
                lifeMax++;
                name = "Nimble " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_ENRAGED:
    /*if (damage <= 0)
    {       grow(Action.GROW_RESILIENT);
            return;
    }*/
                EventRage eventRage = new EventRage(this);
                add((Event) eventRage);
                actions.add(0, eventRage);
                name = "Enraged " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_RESILIENT:
                armor++;
                life += 3;
                lifeMax += 3;
                name = "Resilient " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_LONGSHANK:
                Action mover = getMove();
    /*if (mover == null)
    { grow(Action.GROW_RESILIENT);
     return;
    }*/
                move = new ActionMove(this, move.getMax(), move.getCost(), move.getTargetType(), (byte) (move.getRange() + 1));
                remove(mover);
                add(getMove());
                life += 2;
                lifeMax += 2;

                name = "Longshank " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;


            case Action.GROW_MIGHTY:
    /*if (damage <= 0)
    { grow(Action.GROW_CUNNING);
     return;
    }*/
                life++;
                lifeMax++;
                damage += 1;
                EventStun eventStun = new EventStun(this);
                add((Event) eventStun);
                actions.add(0, eventStun);
                name = "Mighty " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_CLOCKWORK:
                life += 2;
                lifeMax += 2;
                armor++;
                mechanical = true;
                organic = false;
                actions.add(0, new ActionTrait(this, "Inorganic", "Inorganic", "Immune to spells and skills"));
                name = "Clockwork " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_VAMPIRIC:
                Action va = getAttack();
    /*if (va == null || damage <= 0)
    { grow(Action.GROW_CUNNING);
     return;
    }*/
                EventVampire eventVampire = new EventVampire(this);
                add((Event) eventVampire);
                actions.add(0, eventVampire);
                life += 1;
                lifeMax += 1;

                name = "Vampiric " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_CUNNING:
                life++;
                lifeMax++;
                actionsLeft++;
                actionsMax++;
                name = "Cunning " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_EPIC:
    /*if (damage <= 0)
    { grow(Action.GROW_ASCENDANT);
     return;
    }*/
                life += 2;
                lifeMax += 2;
                armor++;
                damage += 2;
                name = "Epic " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_ASCENDANT:
                life += 4;
                lifeMax += 4;
                armor += 2;
                actionsLeft++;
                actionsMax++;
                name = "Ascendant " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;

            case Action.GROW_ARCANE:
    /*if (getAttack() == null || damage <= 0 || getID() == Unit.WARLOCK || getID() == Unit.WARRIOR || getID() == Unit.LANCER)
    { grow(Action.GROW_ASCENDANT);
     return;
    }*/
                life += 1;
                lifeMax += 1;
                damage = 5;
                actions.remove(attack);
                //attack = new ActionAttack(this, (byte) 0, actionsMax, TargetType.UNIT_AREA, (byte) 5, Action.ATTACK_MAGIC_BALL);
                attack = new ActionArcane(this);
                actions.add(attack);
                name = "Arcane " + name;
                ///////////////////////////
                if (getCastle().isAI()) {
                    add((Event) eventPowerUpDrop);
                    actions.add(0, eventPowerUpDrop);
                }
                break;
            // relic grow start

            case Action.GROW_RELIC_CLOCKWORK:
                //if (getCastle().isAI())
                //grow(Action.GROW_CLOCKWORK);
                //else {
                setOrganic(false); // flags properties
                setMechanical(true); // flags properties
                getActions().add(0, new ActionTrait(this, "Inorganic", "Inorganic", "Immune to spells and skills"));
                //}
                break;

            case Action.GROW_RELIC_EVASION:
                //if (getCastle().isAI())
                // grow(Action.GROW_EVASIVE);
                //else {
                EventDodge eventRelicDodge = new EventDodge(this);
                add((Event) eventRelicDodge);
                getActions().add(0, eventRelicDodge);
                //}
                break;

            case Action.GROW_RELIC_FLYING:

                if (getMaxRange() <= 1) {
                    setMove(new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 3));
                } else {
                    setMove(new ActionMove(this, (byte) 1, (byte) 0, TargetType.LOCATION_LINE_JUMP, (byte) 2));
                }
                getActions().add(0, new ActionFlight(this));
                stun(); // Stuns the unit. It would have it's movement at "0/1" anyway.
                break;

            case Action.GROW_RELIC_STUNNING:
                //if (getCastle().isAI())
                // grow(Action.GROW_MIGHTY);
                //else {
                EventStun eventRelicStun = new EventStun(this);
                eventRelicStun.setStunRange((byte) 2);
                add((Event) eventRelicStun);
                getActions().add(0, eventRelicStun);
                //}
                break;

            case Action.GROW_RELIC_VAMPIRE:
                //if (getCastle().isAI())
                // grow(Action.GROW_VAMPIRIC);
                //else {
                EventMiniVampire eventMiniVampire = new EventMiniVampire(this);
                add((Event) eventMiniVampire);
                getActions().add(0, eventMiniVampire);
                //}
                break;

            case Action.GROW_RELIC_HEAL_MOVE:
                raiseLife((byte) 2);
                EventHealMove eventHealMove = new EventHealMove(this);
                add((Event) eventHealMove);
                getActions().add(0, eventHealMove);
                break;
    
   /*case Action.GROW_RELIC_GIFT:
    //Unit victim = null; //Initialize a victim to null, until we find a friendly target to use as a scapegoat
    
    //Get all potential friendly targets
    //Vector<Short> targets = this.getBattleField().getTargets(this, TargetType.UNIT_AREA, (byte) 1, false, true, false, TargetType.FRIENDLY, this.getCastle());
    //int i = 0;
    //while(i < targets.size()) { //Find the first unit that belongs to partner & use that unit to do our switching
     //victim = this.getBattleField().getUnitAt(targets.elementAt(i));
     //if(victim.getTeam() != this.getTeam()){break;}
     //i++;
     //if(i==targets.size()) {//Make some dummy unit & force it on the other team to switch sides
      //victim = new RelicGiftUnit (this.getCastle());
      short t = this.getTeam();
      if(t == TEAM_1)
       team = TEAM_2;
      else
       team = TEAM_1;
      //victim.setTeam(team);
     //}
    //}
    
    //Switch sides by removing the unit from your castle & setting it to ally's castle
    //this.getCastle().removeOut(this);
    //this.setCastle(victim.getCastle());
    //victim.getCastle().addOut(victim.getTeam(), this);
    //getCastle().addOut(team, this);

    //Stun the unit on completion
    //victim.stun();
    stun();
    break;*/

            case Action.GROW_RELIC_RESET:
                // Make a reset action
                Action reset = new ActionRelicReset(this);
                // Add it to the ally's abilities
                actions.add(reset);
                break;

            case Action.GROW_RELIC_BANISH:
                // Make a banish action
                Action banish = new ActionRelicBanish(this);
                // Add it to the ally's abilities
                actions.add(banish);
                break;

            case Action.GROW_RELIC_SPELL_BLOCK:
                //do the blocking of spells by adding the Spell Block event. Based on the Aegis event
                EventRelicSpellBlock esp = new EventRelicSpellBlock(this);
                add((Event) esp);
                getActions().add(0, esp);
                break;

            case Action.GROW_RELIC_PARRY:
                //if (getCastle().isAI())
                EventParry ep = new EventParry(this, 1);
                ep.setParryRange((byte) 1);
                add((Event) ep);
                getActions().add(0, ep);
                break;

            case Action.GROW_RELIC_EXPLODE:
                //Make a detonate action
                Action explode = new ActionRelicExplode(this);
                //Add it to the ally's abilities
                actions.add(explode);
                break;

            // relic grow end
            case Action.GROW_POSSESSED:
                EventPossessed eventPossessed = new EventPossessed(this);
                add((Event) eventPossessed);
                actions.add(0, eventPossessed);
                haunted = true;
                break;

            case Action.GROW_POISONED:
                if (poisoned) return;
                poisoned = true;
                break;

            case Action.GROW_SKINWALKING:
                skinwalking = true;
                ActionTrait skinTrait = new ActionTrait(this, "Skindance", "Becomes the last unit killed", "", "Triggers at start of turn");
                skinTrait.setDetail("Whenever a unit dies, its passing soul imprints on the Skinwalker. At the beginning of its turn, the Skinwalker transforms into the unit that most recently died.");
                skinTrait.setType(Action.SPELL);
                actions.add(0, skinTrait);
                return;

        }

        // add it to the inherit list, if it gets this far
        bonus.add(new Short(newBonus));
    }


    /////////////////////////////////////////////////////////////////
    // Get the bonus targets. Empty vector. Here for overriding
    /////////////////////////////////////////////////////////////////
    public Vector<Short> getTargets(short type) {
        return new Vector<Short>();
    }


    /////////////////////////////////////////////////////////////////
    // Gets
    /////////////////////////////////////////////////////////////////
    public short getTeam() {
        return team;
    }

    public Vector<Action> getActions() {
        return actions;
    }

    public short getID() {
        return id;
    }

    public short getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public short getLife() {
        return life;
    }

    public short getLifeMax() {
        return lifeMax < 0 ? 0 : lifeMax;
    }

    public short getActionsLeft() {
        return actionsLeft;
    }

    public short getActionsMax() {
        return actionsMax;
    }

    public int getCastleCost() {
        return castleCost;
    }

    public Action getAttack() {
        return attack;
    }

    public Action getMove() {
        return move;
    }

    public boolean getOrganic(Unit looker) {
        return organic;
    }

    public int getAppearance() {
        return appearance;
    }

    public BattleField getBattleField() {
        return battleField;
    }

    public Castle getCastle() {
        return castle;
    }

    public boolean deployed() {
        return deployed && !stunned;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean canWin() {
        if (armistice) return false;
        if (getCastle().armistice()) return false;
        return canWin;
    }

    public boolean freelyActs() {
        return freelyActs || noCost;
    }

    public boolean isPowerUp() {
        return isPowerUp;
    }

    public boolean isRelic() {
        return isRelic;
    }

    public boolean isMurderer() {
        return murderer;
    }

    public boolean isPoisoned() {
        return poisoned && organic;
    }

    public boolean isMechanical() {
        return mechanical;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isStunned() {
        return stunned;
    }

    public boolean isWounded() {
        return getLife() < getLifeMax();
    }

    public Unit getSoulmate() {
        return soulmate;
    }


    /////////////////////////////////////////////////////////////////
    // Sets the location
    /////////////////////////////////////////////////////////////////
    public void setLocation(short newLocation) {
        lastLocation = location;
        location = newLocation;

        // Initialize last location
        if (lastLocation == -1) lastLocation = location;

        // Get some X and Y
        short x = BattleField.getX(location);
        short y = BattleField.getY(location);
        short lastX = BattleField.getX(lastLocation);
        short lastY = BattleField.getY(lastLocation);

        // Calculate the step distance for animation
        stepX = lastX - x;
        stepY = lastY - y;

        // How fast
        stepSpeedX = Math.abs(stepX) * Constants.STEP_SPEED;
        stepSpeedY = Math.abs(stepY) * Constants.STEP_SPEED;

        // Multiply for square size
        stepX *= Constants.SQUARE_SIZE;
        stepY *= Constants.SQUARE_SIZE;

        fade = 0f;

    }


    /////////////////////////////////////////////////////////////////
    // Get StepX and StepY
    /////////////////////////////////////////////////////////////////
    public boolean killed() {
        return killed;
    }

    public void kill() {
        killed = true;
    }

    public float getFade() {
        if (fade < 0.15)
            fade += 0.01;
        return fade;
    }

    public int getStepX() {
        if (stepX > 0) return stepX -= stepSpeedX;
        if (stepX < 0) return stepX += stepSpeedX;
        return 0;
    }

    public int getStepY() {
        if (stepY > 0) return stepY -= stepSpeedY;
        if (stepY < 0) return stepY += stepSpeedY;
        return 0;
    }

    public int getStepSpeedX() {
        return stepSpeedX;
    }

    public int getStepSpeedY() {
        return stepSpeedY;
    }

    public int readStepX() {
        return stepX;
    }

    public int readStepY() {
        return stepY;
    }

    public Unit getLink() {
        return null;
    }

    public boolean targetable(Unit looker) {
        if (!targetable) return false;
        if ((!enemyTargetable || armistice) && looker.getCastle() != getCastle()) return false;
        if (!friendTargetable && looker.getCastle() == getCastle()) return false;
        if (looker.armistice() && looker.getCastle() != getCastle()) return false;
        return targetable;
    }

    public boolean armistice() {
        return armistice;
    }

    public Script getScript() {
        return script;
    }

    public int getSequence() {
        return sequence;
    }

    public boolean isOpaque() {
        return opaque;
    }

    public boolean isSkinwalking() {
        return skinwalking;
    }

    public Unit getSoul() {
        return soul;
    }

    public short accessLevel() {
        return accessLevel;
    }

    public short getLastLocation() {
        return lastLocation;
    }

    public boolean rested() {
        return rested;
    }

    public boolean boss() {
        return boss;
    }

    public Vector<Short> getBonus() {
        return bonus;
    }

    public int getPriority() {
        return eventPriority;
    }

    public int getDeployRange() {
        return 1;
    }

    public boolean haunted() {
        return haunted;
    }

    public boolean noCost() {
        return noCost;
    }

    public int getCategory() {
        return category;
    }

    public boolean getGraves() {
        return false;
    }

    public boolean erased() {
        return erased;
    }


    /////////////////////////////////////////////////////////////////
    // Sets
    /////////////////////////////////////////////////////////////////
    public void setDead(boolean d) {
        dead = d;
    }

    public void targetable(boolean newState) {
        targetable = newState;
    }

    public void enemyTargetable(boolean newState) {
        enemyTargetable = newState;
    }

    public void friendTargetable(boolean newState) {
        friendTargetable = newState;
    }

    public void armistice(boolean newState) {
        armistice = newState;
    }

    public void setMechanical(boolean newState) {
        mechanical = newState;
    }

    public void setTeam(short newTeam) {
        team = newTeam;
    }

    public void setBattleField(BattleField newBattleField) {
        battleField = newBattleField;
    }

    public void setAppearance(int newAppearance) {
        appearance = newAppearance;
    }

    public void setCastle(Castle newCastle) {
        castle = newCastle;
    }

    public void setDamage(short newDamage) {
        damage = newDamage;
    }

    public void setArmor(short newArmor) {
        armor = newArmor;
    }

    public void setLife(short newLife) {
        life = newLife;
    }

    public void setLifeMax(short newLife) {
        lifeMax = newLife;
    }

    public void setAttack(Action newAttack) {
        attack = newAttack;
    }

    public void setActions(short newActions) {
        actionsLeft = newActions;
    }

    public void setActionsMax(short newActionsMax) {
        actionsMax = newActionsMax;
    }

    public void setID(short newID) {
        id = newID;
    }

    public void setMurderer() {
        murderer = true;
    }

    public void forgive() {
        murderer = false;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void setScript(Script newScript) {
        script = newScript;
    }

    public void setSequence(int newSequence) {
        sequence = newSequence;
    }

    public void setHidden(boolean newState) {
        hidden = newState;
    }

    public void setSkinwalking(boolean newState) {
        skinwalking = true;
    }

    public void setSoul(Unit newSoul) {
        soul = newSoul;
    }

    public void setOrganic(boolean newState) {
        organic = newState;
    }

    public void setFreelyActs(boolean newState) {
        freelyActs = newState;
    }

    public void noCost(boolean newState) {
        noCost = newState;
    }

    public void setBoss(boolean newState) {
        boss = newState;
    }

    public void setErased(boolean newState) {
        erased = newState;
    }

    public void setSoulmate(Unit newSoulmate) {
        soulmate = newSoulmate;
    }

    /////////////////////////////////////////////////////////////////
    // Get Mark: for use with the Bounty Hunter & Conspirator.
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public Unit getMark() {
        return null;
    }

    /////////////////////////////////////////////////////////////////
    // Set Mark: for use with the Bounty Hunter & Conspirator.
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public void setMark(Unit newUnit) {
    }

    /////////////////////////////////////////////////////////////////
    // getWards: for use with Archangel & SpellBlock relic - not for relic anymore
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public Vector<Unit> getWards() {
        Vector<Unit> wards = new Vector<Unit>();
        wards.add(this);
        return wards;
    }

    /////////////////////////////////////////////////////////////////
    // preDetonate: for use with Explode relic
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public String preDetonate(short target) {
        return "";
    }

    /////////////////////////////////////////////////////////////////
    // getSouls: for use with Diabolist
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public int getSouls() {
        return 0;
    }

    /////////////////////////////////////////////////////////////////
    // setSouls: for use with Diabolist
    // Here for overriding
    /////////////////////////////////////////////////////////////////
    public void setSouls(int newSouls) {
    }

    /////////////////////////////////////////////////////////////////
    // Dying methods, for use mainly with Berserker, here for overriding
    /////////////////////////////////////////////////////////////////
    public boolean isDying() {
        return false;
    }

    public void stopDying() {
    }

    // For summons
    public EventSummoner getSummonManager() {
        return null;
    }
}
