///////////////////////////////////////////////////////////////////////
// Name: DNA
// Desc: DNA for unit sequencing
// Date: 9/8/2008 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.server;

// imports

import leo.shared.Unit;

import java.util.Random;

public class DNA {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    private static final short[] UNITS =
            {Unit.ARCHER,
                    Unit.BERSERKER,
                    Unit.GOLEM,
                    Unit.SWORDSMAN,
                    Unit.FOOTMAN,
                    Unit.MARTYR,
                    Unit.KNIGHT,
                    Unit.PALADIN,
                    Unit.RIDER,
                    Unit.CAVALRY,
                    Unit.PIKEMAN,
                    Unit.BALLISTA,
                    Unit.CATAPULT,
                    Unit.WARRIOR,
                    Unit.BOWMAN,
                    Unit.MOUNTED_ARCHER,
                    Unit.ROGUE,
                    Unit.HEALER,
                    Unit.NECROMANCER,
                    Unit.DRUID,
                    Unit.MAGUS,
                    Unit.SUMMONER,
                    Unit.SHAMAN,
                    Unit.CHANNELER,
                    Unit.AXEMAN,
                    Unit.MOURNER,
                    Unit.CROSSBOWMAN,
                    Unit.LYCANTHROPE,
                    Unit.DUELIST,
                    Unit.MILITIA,
                    //Unit.CONSPIRATOR,
                    //Unit.HYDRA,
                    Unit.DRAGON,
                    Unit.WARLOCK,
                    Unit.FIRE_ARCHER,
                    Unit.SKINWALKER,
                    Unit.HERETIC,
                    Unit.TEMPLAR,
                    Unit.WAR_ELEPHANT,
                    Unit.FANATIC,
                    Unit.DOPPELGANGER,
                    Unit.BARBARIAN,
                    Unit.CONFESSOR,
                    Unit.POSSESSED,
                    Unit.SHIELD_BEARER,
                    Unit.CHIEFTAIN,
                    Unit.LANCER,
                    Unit.SUPPLICANT,
                    Unit.ABJURER,
                    Unit.WITCH,
                    Unit.FEATHERED_SERPENT
            };

    private static final short[] RELICS =
            {
                    //Unit.POWERUP,
                    Unit.POWERUP_TOXIC,
                    Unit.POWERUP_EVASIVE,
                    Unit.POWERUP_RESILIENT,
                    Unit.POWERUP_LONGSHANK,
                    Unit.POWERUP_MIGHTY,
                    Unit.POWERUP_CLOCKWORK,
                    Unit.POWERUP_VAMPIRIC,
                    Unit.POWERUP_CUNNING,
                    Unit.POWERUP_EPIC,
                    Unit.POWERUP_ARCANE,
                    Unit.POWERUP_ASCENDANT,
                    Unit.POWERUP_GUARDIAN,
                    Unit.POWERUP_VIGILANT,
                    Unit.POWERUP_ZEALOUS,
                    Unit.POWERUP_RAMPAGING,
                    Unit.POWERUP_RUTHLESS,
                    Unit.POWERUP_ENRAGED
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
