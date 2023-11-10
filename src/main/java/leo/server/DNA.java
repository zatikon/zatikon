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
            {UnitType.ARCHER.value(),
                    UnitType.BERSERKER.value(),
                    UnitType.GOLEM.value(),
                    UnitType.SWORDSMAN.value(),
                    UnitType.FOOTMAN.value(),
                    UnitType.MARTYR.value(),
                    UnitType.KNIGHT.value(),
                    UnitType.PALADIN.value(),
                    UnitType.RIDER.value(),
                    UnitType.CAVALRY.value(),
                    UnitType.PIKEMAN.value(),
                    UnitType.BALLISTA.value(),
                    UnitType.CATAPULT.value(),
                    UnitType.WARRIOR.value(),
                    UnitType.BOWMAN.value(),
                    UnitType.MOUNTED_ARCHER.value(),
                    UnitType.ROGUE.value(),
                    UnitType.HEALER.value(),
                    UnitType.NECROMANCER.value(),
                    UnitType.DRUID.value(),
                    UnitType.MAGUS.value(),
                    UnitType.SUMMONER.value(),
                    UnitType.SHAMAN.value(),
                    UnitType.CHANNELER.value(),
                    UnitType.AXEMAN.value(),
                    UnitType.MOURNER.value(),
                    UnitType.CROSSBOWMAN.value(),
                    UnitType.LYCANTHROPE.value(),
                    UnitType.DUELIST.value(),
                    UnitType.MILITIA.value(),
                    //Unit.CONSPIRATOR,
                    //Unit.HYDRA,
                    UnitType.DRAGON.value(),
                    UnitType.WARLOCK.value(),
                    UnitType.FIRE_ARCHER.value(),
                    UnitType.SKINWALKER.value(),
                    UnitType.HERETIC.value(),
                    UnitType.TEMPLAR.value(),
                    UnitType.WAR_ELEPHANT.value(),
                    UnitType.FANATIC.value(),
                    UnitType.DOPPELGANGER.value(),
                    UnitType.BARBARIAN.value(),
                    UnitType.CONFESSOR.value(),
                    UnitType.POSSESSED.value(),
                    UnitType.SHIELD_BEARER.value(),
                    UnitType.CHIEFTAIN.value(),
                    UnitType.LANCER.value(),
                    UnitType.SUPPLICANT.value(),
                    UnitType.ABJURER.value(),
                    UnitType.WITCH.value(),
                    UnitType.FEATHERED_SERPENT.value()
            };

    private static final short[] RELICS =
            {
                    //Unit.POWERUP,
                    UnitType.POWERUP_TOXIC.value(),
                    UnitType.POWERUP_EVASIVE.value(),
                    UnitType.POWERUP_RESILIENT.value(),
                    UnitType.POWERUP_LONGSHANK.value(),
                    UnitType.POWERUP_MIGHTY.value(),
                    UnitType.POWERUP_CLOCKWORK.value(),
                    UnitType.POWERUP_VAMPIRIC.value(),
                    UnitType.POWERUP_CUNNING.value(),
                    UnitType.POWERUP_EPIC.value(),
                    UnitType.POWERUP_ARCANE.value(),
                    UnitType.POWERUP_ASCENDANT.value(),
                    UnitType.POWERUP_GUARDIAN.value(),
                    UnitType.POWERUP_VIGILANT.value(),
                    UnitType.POWERUP_ZEALOUS.value(),
                    UnitType.POWERUP_RAMPAGING.value(),
                    UnitType.POWERUP_RUTHLESS.value(),
                    UnitType.POWERUP_ENRAGED.value()
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
