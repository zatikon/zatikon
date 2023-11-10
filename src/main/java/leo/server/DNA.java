///////////////////////////////////////////////////////////////////////
// Name: DNA
// Desc: DNA for unit sequencing
// Date: 9/8/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.UnitType;

import java.util.Random;

public class DNA {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final short[] UNITS =
            {UnitType.ARCHER,
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

    private static final short[] RELICS =
            {
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


    /////////////////////////////////////////////////////////////////
    // Values
    /////////////////////////////////////////////////////////////////
    private final int[] weights = new int[UNITS.length];
    private final int[] relicWeights = new int[RELICS.length];


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

        // first, set uniform changes across all the units and relics
        for (int i = 0; i < UNITS.length; i++) {
            total += 1;
            weights[i] = 1;
        }
        for (int i = 0; i < RELICS.length; i++) {
            relicTotal += 1;
            relicWeights[i] = 1;
        }

        if (level < 10) return;


        /////////////////////////////////////////
        // UNITS
        /////////////////////////////////////////
        int picks = 0;

        // pick 0-3 units which will get extra 10 weight
        picks = random.nextInt(4);
        for (int i = 0; i < picks; i++) {
            total += 10;
            weights[random.nextInt(UNITS.length)] += 10;
        }

        // pick 0-3 units which will get extra 25 weight
        picks = random.nextInt(4);
        for (int i = 0; i < picks; i++) {
            total += 25;
            weights[random.nextInt(UNITS.length)] += 25;
        }

        // pick 0-3 units which will get 50 extra weight
        picks = random.nextInt(4);
        for (int i = 0; i < picks; i++) {
            total += 50;
            weights[random.nextInt(UNITS.length)] += 50;
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
            relicWeights[random.nextInt(RELICS.length)] += 20;
        }

        // temporary to test a relic
  /*int fsr = 0;
  for (int i = 0; i < RELICS.length; i++)
   if (RELICS[i] == Unit.POWERUP_ARCANE) fsr = i;
  
  relicTotal-=relicWeights[fsr];
  relicWeights[fsr] = 500;
  relicTotal+=500;*/
    }


    /////////////////////////////////////////////////////////////////
    // Get a want
    /////////////////////////////////////////////////////////////////
    public short getUnit() {
        int choice = random.nextInt(total);
        int weight = 0;
        for (int i = 0; i < UNITS.length; i++) {
            weight += weights[i];
            if (choice < weight) return UNITS[i];
        }
        return (byte) -1;
    }

    public short getRelic() {
        int choice = random.nextInt(relicTotal);
        int weight = 0;
        for (int i = 0; i < RELICS.length; i++) {
            weight += relicWeights[i];
            if (choice < weight) return RELICS[i];
        }
        return (byte) -1;
    }

}
