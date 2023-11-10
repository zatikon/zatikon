package leo.shared;

import java.util.TreeMap;

public enum UnitType {
    // Units
    IMP(-1),
    DEMON(-2),
    SKELETON(-3),
    ZOMBIE(-4),
    SOLDIER(-5),
    WILL_O_THE_WISPS(-6),
    SERPENT(-7),
    BEAR(-8),
    PORTAL(-9),
    GATE(-10),
    ARCHDEMON(-11),

    // Powerups
    POWERUP(-20),
    POWERUP_TOXIC(-20),
    POWERUP_EVASIVE(-21),
    POWERUP_RESILIENT(-22),
    POWERUP_LONGSHANK(-23),
    POWERUP_MIGHTY(-24),
    POWERUP_CLOCKWORK(-25),
    POWERUP_VAMPIRIC(-26),
    POWERUP_CUNNING(-27),
    POWERUP_EPIC(-28),
    POWERUP_ARCANE(-29),
    POWERUP_ASCENDANT(-30),
    POWERUP_GUARDIAN(-31),
    POWERUP_VIGILANT(-32),
    POWERUP_ZEALOUS(-33),
    POWERUP_RAMPAGING(-34),
    POWERUP_RUTHLESS(-35),
    POWERUP_ENRAGED(-36),
    // # = 17

    FOOTMAN(0),
    BOWMAN(1),
    CAVALRY(2),
    ARCHER(3),
    PIKEMAN(4),
    KNIGHT(5),
    RANGER(6),
    WOLF(7),
    SUMMONER(8),
    // 9
    // 10
    PRIEST(11),
    ENCHANTER(12),
    TEMPLAR(13),
    WARRIOR(14),
    RIDER(15),
    HEALER(16),
    WIZARD(17),
    SCOUT(18),
    ASSASSIN(19),
    TACTICIAN(20),
    GENERAL(21),
    WALL(22),
    MASON(23),
    CATAPULT(24),
    BALLISTA(25),
    NECROMANCER(26),
    LICH(27),
    // 28
    // 29
    SERGEANT(30),
    ABJURER(31),
    SEAL(32),
    WARLOCK(33),
    CROSSBOWMAN(34),
    DRAGON(35),
    DRACOLICH(36),
    HYDRA(37),
    TOWER(38),
    COMMAND_POST(39),
    BARRACKS(40),
    // 41
    DRUID(42),
    CHANNELER(43),
    LYCANTHROPE(44),
    WEREWOLF(45),
    LYCANWOLF(46),
    MOUNTED_ARCHER(47),
    GEOMANCER(48),
    ROCK(49),
    SWORDSMAN(50),
    WITCH(51),
    TOAD(52),
    SHIELD_MAIDEN(53),
    MAGUS(54),
    SPIRIT(55),
    // 56
    GOLEM(57),
    ARMORY(58),
    // 59
    FIRE_ARCHER(60),
    MIMIC(61),
    PALADIN(62),
    SHAMAN(63),
    MARTYR(64),
    ROGUE(65),
    DIABOLIST(66),
    GHOST(67),
    GATE_GUARD(68),
    FEATHERED_SERPENT(69),
    BERSERKER(70),
    ARTIFICER(71),
    CHANGELING(72),
    DOPPELGANGER(73),
    SKINWALKER(74),
    ACOLYTE(75),
    AXEMAN(76),
    MOURNER(77),
    HERETIC(78),
    WAR_ELEPHANT(79),
    FANATIC(80),
    DISMOUNTED_KNIGHT(81),
    // 82
    QUARTERMASTER(83),
    WALL_MASON(84),
    CONFESSOR(85),
    STRATEGIST(86),
    POSSESSED(87),
    BARBARIAN(88),
    ALCHEMIST(89),
    BOUNTY_HUNTER(90),
    SHIELD_BEARER(91),
    CHIEFTAIN(92),
    LANCER(93),
    ARCHANGEL(94),
    CONJURER(95),
    // 96
    // 97
    DIPLOMAT(98),
    DIPLOMAT_USED(99),
    LONGBOWMAN(100),
    SYCOPHANT(101),
    WYVERN(102),
    EGG(103),
    CAPTAIN(104),
    ABBEY(105),
    SUPPLICANT(106),
    DUELIST(107),
    MILITIA(108),
    CONSPIRATOR(109),
    RELIC_CLOCKWORK(110),
    RELIC_BANISH(111),
    RELIC_VAMPIRE(112),
    RELIC_EVASIVE(113),
    RELIC_STUN(114),
    RELIC_FLIGHT(115),
    RELIC_HEAL_MOVE(116),
    RELIC_GIFT_UNIT(117),
    RELIC_RESET(118),
    RELIC_SPELL_BLOCK(119),
    RELIC_PARRY(120),
    RELIC_EXPLODE(121),
    // 122
    NONE(123),
    UNIT_COUNT(124);

    private final short unitId;
    private static final TreeMap<Short, UnitType> shortToEnum = new TreeMap<>();

    public static UnitType idToUnit(short unitId) {
        if (shortToEnum.isEmpty()) {
            for (var e: UnitType.values()) {
                shortToEnum.put(e.value(), e);
            }
        }
        return shortToEnum.get(unitId);
    }

    UnitType(int unitId) {
        assert unitId >= Short.MIN_VALUE;
        assert unitId <= Short.MAX_VALUE;

        this.unitId = (short) unitId;
    }

    public short value() {
        return this.unitId;
    }
    
    
}
