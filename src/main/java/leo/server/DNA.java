///////////////////////////////////////////////////////////////////////
// Name: DNA
// Desc: DNA for unit sequencing
// Date: 9/8/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Unit;
import leo.shared.UnitType;

import java.util.EnumMap;
import java.util.Random;
import java.util.stream.Collectors;

public class DNA {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final UnitType[] UNITS = {
        UnitType.ARCHER,
        UnitType.BERSERKER,
        UnitType.GOLEM,
        UnitType.SWORDSMAN,
        UnitType.FOOTMAN,
        UnitType.MARTYR,
        UnitType.KNIGHT,
        UnitType.PALADIN,
        UnitType.RIDER,
        UnitType.CAVALRY,
        UnitType.PIKEMAN,
        UnitType.BALLISTA,
        UnitType.CATAPULT,
        UnitType.WARRIOR,
        UnitType.BOWMAN,
        UnitType.MOUNTED_ARCHER,
        UnitType.ROGUE,
        UnitType.HEALER,
        UnitType.NECROMANCER,
        UnitType.DRUID,
        UnitType.MAGUS,
        UnitType.SUMMONER,
        UnitType.SHAMAN,
        UnitType.CHANNELER,
        UnitType.AXEMAN,
        UnitType.MOURNER,
        UnitType.CROSSBOWMAN,
        UnitType.LYCANTHROPE,
        UnitType.DUELIST,
        UnitType.MILITIA,
        //Unit.CONSPIRATOR,
        //Unit.HYDRA,
        UnitType.DRAGON,
        UnitType.WARLOCK,
        UnitType.FIRE_ARCHER,
        UnitType.SKINWALKER,
        UnitType.HERETIC,
        UnitType.TEMPLAR,
        UnitType.WAR_ELEPHANT,
        UnitType.FANATIC,
        UnitType.DOPPELGANGER,
        UnitType.BARBARIAN,
        UnitType.CONFESSOR,
        UnitType.POSSESSED,
        UnitType.SHIELD_BEARER,
        UnitType.CHIEFTAIN,
        UnitType.LANCER,
        UnitType.SUPPLICANT,
        UnitType.ABJURER,
        UnitType.WITCH,
        UnitType.FEATHERED_SERPENT
    };

    private static final UnitType[] RELICS = {
        //Unit.POWERUP,
        UnitType.POWERUP_TOXIC,
        UnitType.POWERUP_EVASIVE,
        UnitType.POWERUP_RESILIENT,
        UnitType.POWERUP_LONGSHANK,
        UnitType.POWERUP_MIGHTY,
        UnitType.POWERUP_CLOCKWORK,
        UnitType.POWERUP_VAMPIRIC,
        UnitType.POWERUP_CUNNING,
        UnitType.POWERUP_EPIC,
        UnitType.POWERUP_ARCANE,
        UnitType.POWERUP_ASCENDANT,
        UnitType.POWERUP_GUARDIAN,
        UnitType.POWERUP_VIGILANT,
        UnitType.POWERUP_ZEALOUS,
        UnitType.POWERUP_RAMPAGING,
        UnitType.POWERUP_RUTHLESS,
        UnitType.POWERUP_ENRAGED
    };

    private static final UnitType[] BEGINNER_UNITS = {
            UnitType.AXEMAN,
            UnitType.BOWMAN,
            UnitType.BARBARIAN,
            UnitType.FOOTMAN,
            UnitType.RIDER,
            UnitType.CAVALRY,
            UnitType.FANATIC,
            UnitType.PIKEMAN,
            UnitType.BALLISTA,
            UnitType.CATAPULT,
            UnitType.LYCANTHROPE,
            UnitType.SWORDSMAN,
            UnitType.MILITIA,
            UnitType.POSSESSED,
            UnitType.KNIGHT,
            UnitType.ARCHER,
            UnitType.HEALER,
            UnitType.WARRIOR,
            UnitType.SKINWALKER,
            UnitType.MOUNTED_ARCHER,
            UnitType.ROGUE,
            UnitType.PALADIN,
            UnitType.SHIELD_BEARER,
            UnitType.MOURNER,
            UnitType.BERSERKER,
            UnitType.LANCER
    };

    private static final UnitType[] BEGINNER_RELICS = {
            UnitType.POWERUP_EVASIVE,
            UnitType.POWERUP_ZEALOUS
    };
    public static final int BEGINNER_LEVEL_LIMIT = 10;

    /////////////////////////////////////////////////////////////////
    // Values
    /////////////////////////////////////////////////////////////////
    private final EnumMap<UnitType, Integer> weights = new EnumMap<>(UnitType.class);
    private final EnumMap<UnitType, Integer> relicWeights = new EnumMap<>(UnitType.class);


    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private int total = 0;
    private int relicTotal = 0;
    private final Random random;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public DNA(Random newRandom, int level) {
        random = newRandom;

        for (UnitType unit : UNITS) {
            weights.put(unit, 0);
        }
        for (UnitType relic : RELICS) {
            relicWeights.put(relic, 0);
        }

        System.err.printf("AI Level: %d%n", level);
        if (level <= BEGINNER_LEVEL_LIMIT) {
            // should amount to at least 3 at level 1, and all of them at BEGINNER_LEVEL_LIMIT
            var howMuch = 1 + (BEGINNER_UNITS.length - 1) * level / BEGINNER_LEVEL_LIMIT;
            System.err.printf("Adding this many units: %d%n", howMuch);

            for (int i = 0; i < howMuch; i++) {
                var unit = BEGINNER_UNITS[i];
                System.err.printf("Adding %s%n", unit.name());
                total += 1;
                weights.put(unit, 1);
            }

            var howMany = BEGINNER_RELICS.length;
            System.err.printf("Adding this many relics: %d%n", howMany);

            for (int i = 0; i < howMany; i++) {
                var relic = BEGINNER_RELICS[i];
                System.err.printf("Adding %s%n", relic.name());
                relicTotal += 1;
                relicWeights.put(relic, 1);
            }
        } else {
            // first, set uniform chances across all the units and relics
            for (UnitType unit : UNITS) {
                total += 1;
                weights.put(unit, 1);
            }
            for (UnitType relic : RELICS) {
                relicTotal += 1;
                relicWeights.put(relic, 1);
            }


            /////////////////////////////////////////
            // UNITS
            /////////////////////////////////////////
            int picks = 0;

            // pick 0-3 units which will get extra 10 weight
            picks = random.nextInt(4);
            for (int i = 0; i < picks; i++) {
                total += 10;
                var choice = UNITS[random.nextInt(UNITS.length)];
                var currentWeight = weights.get(choice);
                weights.put(choice, currentWeight + 10);
            }

            // pick 0-3 units which will get extra 25 weight
            picks = random.nextInt(4);
            for (int i = 0; i < picks; i++) {
                total += 25;
                var choice = UNITS[random.nextInt(UNITS.length)];
                var currentWeight = weights.get(choice);
                weights.put(choice, currentWeight + 25);
            }

            // pick 0-3 units which will get 50 extra weight
            picks = random.nextInt(4);
            for (int i = 0; i < picks; i++) {
                total += 50;
                var choice = UNITS[random.nextInt(UNITS.length)];
                var currentWeight = weights.get(choice);
                weights.put(choice, currentWeight + 50);
            }

            // temporary to test a unit
  /*int fs = 0;
  for (int i = 0; i < UNITS.length; i++) {
   if (UNITS[i] == Unit.FEATHERED_SERPENT || UNITS[i] == Unit.MOURNER) { fs = i;
  
  total-=weights[fs];
  weights[fs] = 500;
  total+=500;
   }
  }*/


            /////////////////////////////////////////
            // RELICS
            /////////////////////////////////////////

            // pick 0-3 relics which will get 20 extra weight
            picks = random.nextInt(4);
            for (int i = 0; i < picks; i++) {
                relicTotal += 20;
                var choice = RELICS[random.nextInt(RELICS.length)];
                var currentWeight = relicWeights.get(choice);
                relicWeights.put(choice, currentWeight + 20);
            }

            // temporary to test a relic
  /*int fsr = 0;
  for (int i = 0; i < RELICS.length; i++)
   if (RELICS[i] == Unit.POWERUP_ARCANE) fsr = i;
  
  relicTotal-=relicWeights[fsr];
  relicWeights[fsr] = 500;
  relicTotal+=500;*/
        }

        var chosen = weights.entrySet().stream().filter((x) -> x.getValue() > 1).map((x) -> x.getKey().name()).collect(Collectors.joining());

        System.err.println("Units chosen: " + chosen);
    }


    /////////////////////////////////////////////////////////////////
    // Get a want
    /////////////////////////////////////////////////////////////////
    public short getUnit() {
        //return 87; // 87 = possessed, 4 = pikemen
        
        int choice = random.nextInt(total);
        int weight = 0;
        for (int i = 0; i < UNITS.length; i++) {
            weight += weights.get(UNITS[i]);
            if (choice < weight) return UNITS[i].value();
        }
        System.err.println("This is bad.");
        return (short) -1;
    }

    public short getRelic() {
        // return -33; //-33 zelous relic
        
        int choice = random.nextInt(relicTotal);
        int weight = 0;
        for (int i = 0; i < RELICS.length; i++) {
            weight += relicWeights.get(RELICS[i]);
            if (choice < weight) return RELICS[i].value();
        }
        System.err.println("That's not great.");
        return (short) -1;
    }

}
