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
import org.tinylog.Logger;

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
                    {UnitType.FOOTMAN.value(),
                            UnitType.BOWMAN.value(),
                            UnitType.PIKEMAN.value(),
                            UnitType.TACTICIAN.value(),
                            UnitType.CAVALRY.value(),
                            UnitType.SCOUT.value(),
                            UnitType.ARCHER.value(),
                            UnitType.CATAPULT.value(),
                            UnitType.BALLISTA.value(),
                            UnitType.GENERAL.value(),
                            UnitType.MILITIA.value(),
                            UnitType.CONSPIRATOR.value()},

                    // tier 2
                    {UnitType.RIDER.value(),
                            UnitType.ASSASSIN.value(),
                            UnitType.FANATIC.value(),
                            UnitType.WARRIOR.value(),
                            UnitType.KNIGHT.value(),
                            UnitType.RANGER.value(),
                            UnitType.MASON.value(),
                            UnitType.CROSSBOWMAN.value(),
                            UnitType.GATE_GUARD.value(),
                            UnitType.RELIC_GIFT_UNIT.value()
                    },

                    // tier 3
                    {UnitType.SWORDSMAN.value(),
                            UnitType.BERSERKER.value(),
                            UnitType.COMMAND_POST.value(),
                            UnitType.BARRACKS.value(),
                            UnitType.TOWER.value(),
                            UnitType.ROGUE.value(),
                            UnitType.ACOLYTE.value(),
                            UnitType.AXEMAN.value(),
                            UnitType.BARBARIAN.value(),
                            UnitType.RELIC_BANISH.value()},

                    // tier 4
                    {UnitType.MOUNTED_ARCHER.value(),
                            UnitType.SERGEANT.value(),
                            UnitType.FIRE_ARCHER.value(),
                            UnitType.ARMORY.value(),
                            UnitType.MARTYR.value(),
                            UnitType.TEMPLAR.value(),
                            UnitType.MOURNER.value(),
                            UnitType.QUARTERMASTER.value(),
                            UnitType.SHIELD_BEARER.value(),
                            UnitType.ABBEY.value(),
                            UnitType.RELIC_RESET.value(),
                            UnitType.RELIC_EXPLODE.value()
                    },

                    // tier 5
                    {UnitType.PRIEST.value(),
                            UnitType.HEALER.value(),
                            UnitType.DRUID.value(),
                            UnitType.PALADIN.value(),
                            UnitType.GOLEM.value(),
                            UnitType.SHIELD_MAIDEN.value(),
                            UnitType.HERETIC.value(),
                            UnitType.CONFESSOR.value(),
                            UnitType.LANCER.value(),
                            UnitType.SYCOPHANT.value()
                    },

                    // tier 6
                    {UnitType.SHAMAN.value(),
                            UnitType.WAR_ELEPHANT.value(),
                            UnitType.LYCANTHROPE.value(),
                            UnitType.CHANNELER.value(),
                            UnitType.SUMMONER.value(),
                            UnitType.ABJURER.value(),
                            UnitType.GEOMANCER.value(),
                            UnitType.STRATEGIST.value(),
                            UnitType.DIPLOMAT.value(),
                            UnitType.LONGBOWMAN.value(),
                            UnitType.SUPPLICANT.value(),
                            UnitType.RELIC_EVASIVE.value(),
                            UnitType.RELIC_FLIGHT.value(),
                            UnitType.RELIC_HEAL_MOVE.value(),
                    },

                    // tier 7
                    {
                            UnitType.BOUNTY_HUNTER.value(),
                            UnitType.CONSPIRATOR.value(),
                            UnitType.WARLOCK.value(),
                            UnitType.WIZARD.value(),
                            UnitType.ENCHANTER.value(),
                            UnitType.NECROMANCER.value(),
                            UnitType.WITCH.value(),
                            UnitType.MAGUS.value(),
                            UnitType.CONJURER.value(),
                            UnitType.CHIEFTAIN.value(),
                            UnitType.CAPTAIN.value(),
                            UnitType.RELIC_CLOCKWORK.value(),
                            UnitType.RELIC_VAMPIRE.value(),
                            UnitType.RELIC_STUN.value(),
                            UnitType.RELIC_SPELL_BLOCK.value(),
                            UnitType.RELIC_PARRY.value(),
                            UnitType.DUELIST.value()
                    },

                    // tier 8
                    {UnitType.DIABOLIST.value(),
                            UnitType.ARTIFICER.value(),
                            UnitType.MIMIC.value(),
                            UnitType.DOPPELGANGER.value(),
                            UnitType.SKINWALKER.value(),
                            UnitType.CHANGELING.value(),
                            UnitType.POSSESSED.value(),
                            UnitType.ALCHEMIST.value()
                    },

                    // tier 9
                    {UnitType.DRACOLICH.value(),
                            UnitType.DRAGON.value(),
                            UnitType.FEATHERED_SERPENT.value(),
                            UnitType.HYDRA.value(),
                            UnitType.ARCHANGEL.value(),
                            UnitType.WYVERN.value()
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


    // # = 17


    //////////////////////////
    // The starting army
    //////////////////////////
    public static int startingCount(short id) {
        var unit = UnitType.idToUnit(id);

        switch (unit) {
            // Tier 1
            case FOOTMAN:
                return 2;

            case BOWMAN:
                return 2;

            case CAVALRY:
                return 2;

            case PIKEMAN:
                return 2;

            case SCOUT:
                return 2;

            case TACTICIAN:
                return 2;

            // Tier 2
            case ARCHER:
                return 1;

            case GENERAL:
                return 1;

            case ASSASSIN:
                return 1;

            case RIDER:
                return 1;

            case CATAPULT:
                return 1;

            case BALLISTA:
                return 1;

            // Tier 3
            case KNIGHT:
                return 1;

            case WARRIOR:
                return 1;

            case RANGER:
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
                units.add(Unit.getUnit(UnitType.ARCHER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.BOWMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CROSSBOWMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.FIRE_ARCHER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.LONGBOWMAN.value(), forCastle));
                return units;

            case Unit.BLACK_MAGIC_USERS:
                units.add(Unit.getUnit(UnitType.DIABOLIST.value(), forCastle));
                units.add(Unit.getUnit(UnitType.NECROMANCER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SUMMONER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WARLOCK.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WITCH.value(), forCastle));
                return units;

            case Unit.CLERGY:
                units.add(Unit.getUnit(UnitType.ACOLYTE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ARCHANGEL.value(), forCastle));
                units.add(Unit.getUnit(UnitType.HEALER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.PALADIN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.PRIEST.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SHIELD_MAIDEN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.TEMPLAR.value(), forCastle));
                return units;

            case Unit.COMMANDERS:
                units.add(Unit.getUnit(UnitType.CAPTAIN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.DIPLOMAT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.GENERAL.value(), forCastle));
                units.add(Unit.getUnit(UnitType.QUARTERMASTER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SERGEANT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.STRATEGIST.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SYCOPHANT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.TACTICIAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CONSPIRATOR.value(), forCastle));
                return units;

            case Unit.CULTISTS:
                units.add(Unit.getUnit(UnitType.CONFESSOR.value(), forCastle));
                units.add(Unit.getUnit(UnitType.FANATIC.value(), forCastle));
                units.add(Unit.getUnit(UnitType.HERETIC.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MARTYR.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MOURNER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.POSSESSED.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SUPPLICANT.value(), forCastle));
                return units;

            case Unit.HORSEMEN:
                units.add(Unit.getUnit(UnitType.CAVALRY.value(), forCastle));
                units.add(Unit.getUnit(UnitType.KNIGHT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.LANCER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MOUNTED_ARCHER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RIDER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WAR_ELEPHANT.value(), forCastle));
                return units;

            case Unit.NATURE:
                units.add(Unit.getUnit(UnitType.BARBARIAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.BERSERKER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CHANNELER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CHIEFTAIN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.DRUID.value(), forCastle));
                units.add(Unit.getUnit(UnitType.GEOMANCER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SHAMAN.value(), forCastle));
                return units;

            case Unit.SCOUTS:
                units.add(Unit.getUnit(UnitType.ASSASSIN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.BOUNTY_HUNTER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RANGER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ROGUE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SCOUT.value(), forCastle));
                return units;

            case Unit.SHAPESHIFTERS:
                units.add(Unit.getUnit(UnitType.CHANGELING.value(), forCastle));
                units.add(Unit.getUnit(UnitType.DOPPELGANGER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.LYCANTHROPE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MIMIC.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SKINWALKER.value(), forCastle));
                return units;

            case Unit.SIEGE:
                units.add(Unit.getUnit(UnitType.BALLISTA.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CATAPULT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.GOLEM.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MASON.value(), forCastle));
                return units;

            case Unit.SOLDIERS:
                units.add(Unit.getUnit(UnitType.AXEMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.FOOTMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.GATE_GUARD.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MILITIA.value(), forCastle));
                units.add(Unit.getUnit(UnitType.PIKEMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SHIELD_BEARER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.SWORDSMAN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WARRIOR.value(), forCastle));
                units.add(Unit.getUnit(UnitType.DUELIST.value(), forCastle));
                return units;

            case Unit.STRUCTURES:
                units.add(Unit.getUnit(UnitType.ABBEY.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ARMORY.value(), forCastle));
                units.add(Unit.getUnit(UnitType.BARRACKS.value(), forCastle));
                units.add(Unit.getUnit(UnitType.COMMAND_POST.value(), forCastle));
                units.add(Unit.getUnit(UnitType.TOWER.value(), forCastle));
                return units;

            case Unit.WHITE_MAGIC_USERS:
                units.add(Unit.getUnit(UnitType.ABJURER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ALCHEMIST.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ARTIFICER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.CONJURER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.ENCHANTER.value(), forCastle));
                units.add(Unit.getUnit(UnitType.MAGUS.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WIZARD.value(), forCastle));
                return units;

            case Unit.WYRMS:
                units.add(Unit.getUnit(UnitType.DRACOLICH.value(), forCastle));
                units.add(Unit.getUnit(UnitType.DRAGON.value(), forCastle));
                units.add(Unit.getUnit(UnitType.FEATHERED_SERPENT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.HYDRA.value(), forCastle));
                units.add(Unit.getUnit(UnitType.WYVERN.value(), forCastle));
                return units;

            case Unit.RELICS:
                units.add(Unit.getUnit(UnitType.RELIC_CLOCKWORK.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_EVASIVE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_VAMPIRE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_FLIGHT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_BANISH.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_STUN.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_HEAL_MOVE.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_GIFT_UNIT.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_RESET.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_SPELL_BLOCK.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_PARRY.value(), forCastle));
                units.add(Unit.getUnit(UnitType.RELIC_EXPLODE.value(), forCastle));
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
        var unit = UnitType.idToUnit(id);
        
        switch (unit) {
            case FOOTMAN:
                return new UnitFootman(castle);
            case BOWMAN:
                return new UnitBowman(castle);
            case CAVALRY:
                return new UnitCavalry(castle);
            case ARCHER:
                return new UnitArcher(castle);
            case PIKEMAN:
                return new UnitPikeman(castle);
            case KNIGHT:
                return new UnitKnight(castle);
            case RANGER:
                return new UnitRanger(castle);
            case WOLF:
                return new UnitWolf(castle);
            case SUMMONER:
                return new UnitSummoner(castle);
            case IMP:
                return new UnitImp(castle);
            case DEMON:
                return new UnitDemon(castle);
            case ARCHDEMON:
                return new UnitArchDemon(castle);
            case PRIEST:
                return new UnitPriest(castle);
            case ENCHANTER:
                return new UnitEnchanter(castle);
            case TEMPLAR:
                return new UnitTemplar(castle);
            case WARRIOR:
                return new UnitWarrior(castle);
            case RIDER:
                return new UnitRider(castle);
            case HEALER:
                return new UnitHealer(castle);
            case WIZARD:
                return new UnitWizard(castle);
            case SCOUT:
                return new UnitScout(castle);
            case ASSASSIN:
                return new UnitAssassin(castle);
            case TACTICIAN:
                return new UnitTactician(castle);
            case GENERAL:
                return new UnitGeneral(castle);
            case WALL:
                return new UnitWall(castle);
            case MASON:
                return new UnitMason(castle);
            case CATAPULT:
                return new UnitCatapult(castle);
            case BALLISTA:
                return new UnitBallista(castle);
            case NECROMANCER:
                return new UnitNecromancer(castle);
            case LICH:
                return new UnitLich(castle);
            case SKELETON:
                return new UnitSkeleton(castle);
            case ZOMBIE:
                return new UnitZombie(castle);
            case SERGEANT:
                return new UnitSergeant(castle);
            case ABJURER:
                return new UnitAbjurer(castle);
            case SEAL:
                return new UnitSeal(castle);
            case WARLOCK:
                return new UnitWarlock(castle);
            case CROSSBOWMAN:
                return new UnitCrossbowman(castle);
            case DRAGON:
                return new UnitDragon(castle);
            case DRACOLICH:
                return new UnitDracolich(castle);
            case HYDRA:
                return new UnitHydra(castle);
            case TOWER:
                return new UnitTower(castle);
            case COMMAND_POST:
                return new UnitCommandPost(castle);
            case BARRACKS:
                return new UnitBarracks(castle);
            case SOLDIER:
                return new UnitSoldier(castle);
            case DRUID:
                return new UnitDruid(castle);
            case CHANNELER:
                return new UnitChanneler(castle);
            case LYCANTHROPE:
                return new UnitLycanthrope(castle);
            case WEREWOLF:
                return new UnitWerewolf(castle);
            case LYCANWOLF:
                return new UnitLycanWolf(castle);
            case MOUNTED_ARCHER:
                return new UnitMountedArcher(castle);
            case GEOMANCER:
                return new UnitGeomancer(castle);
            case ROCK:
                return new UnitRock(castle, null);
            case SWORDSMAN:
                return new UnitSwordsman(castle);
            case WITCH:
                return new UnitWitch(castle);
            case TOAD:
                return new UnitToad(castle);
            case SHIELD_MAIDEN:
                return new UnitShieldMaiden(castle);
            case MAGUS:
                return new UnitMagus(castle);
            case SPIRIT:
                return new UnitSpirit(castle);
            case WILL_O_THE_WISPS:
                return new UnitWillWisps(castle);
            case GOLEM:
                return new UnitGolem(castle);
            case ARMORY:
                return new UnitArmory(castle);
            case SERPENT:
                return new UnitSerpent(castle);
            case FIRE_ARCHER:
                return new UnitFireArcher(castle);
            case MIMIC:
                return new UnitMimic(castle);
            case PALADIN:
                return new UnitPaladin(castle);
            case SHAMAN:
                return new UnitShaman(castle);
            case MARTYR:
                return new UnitMartyr(castle);
            case ROGUE:
                return new UnitRogue(castle);
            case DIABOLIST:
                return new UnitDiabolist(castle);
            case GHOST:
                return new UnitGhost(castle);
            case GATE_GUARD:
                return new UnitGateGuard(castle);
            case FEATHERED_SERPENT:
                return new UnitFeatheredSerpent(castle);
            case BERSERKER:
                return new UnitBerserker(castle);
            case ARTIFICER:
                return new UnitArtificer(castle);
            case CHANGELING:
                return new UnitChangeling(castle);
            case DOPPELGANGER:
                return new UnitDoppelganger(castle);
            case SKINWALKER:
                return new UnitSkinwalker(castle);
            case ACOLYTE:
                return new UnitAcolyte(castle);
            case AXEMAN:
                return new UnitAxeman(castle);
            case MOURNER:
                return new UnitMourner(castle);
            case HERETIC:
                return new UnitHeretic(castle);
            case WAR_ELEPHANT:
                return new UnitWarElephant(castle);
            case FANATIC:
                return new UnitFanatic(castle);
            case DISMOUNTED_KNIGHT:
                return new UnitDismountedKnight(castle);
            case BEAR:
                return new UnitBear(castle);
            case QUARTERMASTER:
                return new UnitQuartermaster(castle);
            case WALL_MASON:
                return new UnitWallMason(castle);
            case CONFESSOR:
                return new UnitConfessor(castle);
            case STRATEGIST:
                return new UnitStrategist(castle);
            case POSSESSED:
                return new UnitPossessed(castle);
            case BARBARIAN:
                return new UnitBarbarian(castle);
            case ALCHEMIST:
                return new UnitAlchemist(castle);
            case BOUNTY_HUNTER:
                return new UnitBountyHunter(castle);
            case SHIELD_BEARER:
                return new UnitShieldBearer(castle);
            case CHIEFTAIN:
                return new UnitChieftain(castle);
            case LANCER:
                return new UnitLancer(castle);
            case ARCHANGEL:
                return new UnitArchangel(castle);
            case CONJURER:
                return new UnitConjurer(castle);
            case PORTAL:
                return new UnitPortal(castle);
            case GATE:
                return new UnitGate(castle);
            case DIPLOMAT:
                return new UnitDiplomat(castle, true);
            case DIPLOMAT_USED:
                return new UnitDiplomat(castle, false);
            case LONGBOWMAN:
                return new UnitLongbowman(castle);
            case SYCOPHANT:
                return new UnitSycophant(castle);
            case WYVERN:
                return new UnitWyvern(castle);
            case EGG:
                return new UnitEgg(castle);
            case CAPTAIN:
                return new UnitCaptain(castle);
            case ABBEY:
                return new UnitAbbey(castle);
            case SUPPLICANT:
                return new UnitSupplicant(castle);
            case DUELIST:
                return new UnitDuelist(castle);
            case MILITIA:
                return new UnitMilitia(castle);
            case CONSPIRATOR:
                return new UnitConspirator(castle);
            case RELIC_CLOCKWORK:
                return new RelicClockwork(castle);
            case RELIC_BANISH:
                return new RelicBanish(castle);
            case RELIC_EVASIVE:
                return new RelicEvasive(castle);
            case RELIC_VAMPIRE:
                return new RelicVampire(castle);
            case RELIC_STUN:
                return new RelicStun(castle);
            case RELIC_FLIGHT:
                return new RelicFlight(castle);
            case RELIC_HEAL_MOVE:
                return new RelicHealMove(castle);
            case RELIC_GIFT_UNIT:
                return new RelicGiftUnit(castle);
            case RELIC_RESET:
                return new RelicReset(castle);
            case RELIC_SPELL_BLOCK:
                return new RelicSpellBlock(castle);
            case RELIC_PARRY:
                return new RelicParry(castle);
            case RELIC_EXPLODE:
                return new RelicExplode(castle);
            case NONE:
                return new UnitNone(castle);
            case POWERUP_TOXIC:
            case POWERUP_EVASIVE:
            case POWERUP_RESILIENT:
            case POWERUP_LONGSHANK:
            case POWERUP_MIGHTY:
            case POWERUP_CLOCKWORK:
            case POWERUP_VAMPIRIC:
            case POWERUP_CUNNING:
            case POWERUP_EPIC:
            case POWERUP_ARCANE:
            case POWERUP_ASCENDANT:
            case POWERUP_GUARDIAN:
            case POWERUP_VIGILANT:
            case POWERUP_ZEALOUS:
            case POWERUP_RAMPAGING:
            case POWERUP_RUTHLESS:
            case POWERUP_ENRAGED:
                return new RelicPowerUp(castle, (byte) ((0 - id) + UnitType.POWERUP.value()));
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
        if (getID() == UnitType.WALL.value() ||
                getID() == UnitType.SEAL.value() ||
                getID() == UnitType.ROCK.value() ||
                getID() == UnitType.ARMORY.value()) return true;

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
        if (getSoul().getID() == UnitType.NONE.value()) return;

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

        if (id != UnitType.NONE.value()) {
            Vector<Unit> units = getBattleField().getUnits();
            Iterator<Unit> it = units.iterator();
            while (it.hasNext()) {
                Unit tmp = it.next();
                if (tmp.isSkinwalking() && death && getOrganic(this) && tmp != this && getID() != UnitType.NONE.value() && targetable(tmp)) {
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
        Logger.error("Invalid action from getAction: " + action.getName());
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
        bonus.add(newBonus);
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

    public UnitType getEnum() {
        return UnitType.idToUnit(id);
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
