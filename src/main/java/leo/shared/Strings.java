///////////////////////////////////////////////////////////////////////
// Name: Strings
// Desc: Strings
// Date: 8/31/2009 - Marilyn Jones
//       9/21/2010 - Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.shared;

// imports


public class Strings {
    /////////////////////////////////////////////////////////////////
    // Constants
    /////////////////////////////////////////////////////////////////
    public static String ACTION_1 = "Attack";
    public static String ACTION_2 = "Move";
    public static String ACTION_3 = "Spell";
    public static String ACTION_4 = "Skill";
    public static String ACTION_5 = "None";

    public static String CASTLE_1 = "Invalid deploy";
    public static String CASTLE_2 = " has seized the castle!";
    public static String CASTLE_3 = " deployed at ";
    public static String CASTLE_4 = " exploded!";

    public static String UNIT_1 = "the Free Version";
    public static String UNIT_2 = "Crusades";
    public static String UNIT_3 = "Legions";
    public static String UNIT_4 = "Inquisition";
    public static String UNIT_5 = "Archers";
    public static String UNIT_6 = "Black Mages";
    public static String UNIT_7 = "Clergy";
    public static String UNIT_8 = "Commanders";
    public static String UNIT_9 = "Cultists";
    public static String UNIT_10 = "Horsemen";
    public static String UNIT_11 = "Nature";
    public static String UNIT_12 = "Scouts";
    public static String UNIT_13 = "Shapeshifters";
    public static String UNIT_14 = "Siege";
    public static String UNIT_15 = "Soldiers";
    public static String UNIT_16 = "Structures";
    public static String UNIT_17 = "White Mages";
    public static String UNIT_18 = "Wyrms";
    public static String UNIT_20 = "Relics";

    //putting the newest units in their own class
    public static String UNIT_19 = "Newest Members";

    public static String INVALID_ACTION = "Invalid action";

    public static String RANGE = " range";
    public static String COSTS = "Costs ";
    public static String ACTION = " action";
    public static String ANYWHERE_WITHIN = "Anywhere within ";
    public static String ANY_ALLY = "Any ally";

    public static String ACTION_ARMISTICE_1 = "All your units are immune to enemies, and all enemies are immune to your units. The enemy castle can't be captured this turn, or the turn after. The armistice lasts until the start of your next turn. This ability can only be used once per game, and can never be recovered. This only affects your units, and not your ally's.";
    public static String ACTION_ARMISTICE_2 = " declares an armistice";
    public static String ACTION_ARMISTICE_3 = "Allies immune to enemies";
    public static String ACTION_ARMISTICE_4 = "Enemies immune to allies";
    public static String ACTION_ARMISTICE_5 = "1 action, once per game";
    public static String ACTION_ARMISTICE_6 = "Armistice";

    public static String ACTION_ATTACK_1 = "Attack the target, inflicting damage equal to this unit's power.";
    public static String ACTION_ATTACK_2 = " misses ";
    public static String ACTION_ATTACK_3 = " attacks ";
    public static String ACTION_ATTACK_4 = "Attack enemy unit";
    public static String ACTION_ATTACK_5 = "Anywhere within ";
    public static String ACTION_ATTACK_6 = "Attack";

    public static String ACTION_BANISH_1 = "Target is returned to their castle as an undeployed unit.";
    public static String ACTION_BANISH_2 = " fizzles the spell";
    public static String ACTION_BANISH_3 = " banishes ";
    public static String ACTION_BANISH_4 = "Return target to their castle";
    public static String ACTION_BANISH_5 = "Banish";

    public static String ACTION_BEAR_1 = " has seized the castle!";
    public static String ACTION_BEAR_2 = " calls a bear";
    public static String ACTION_BEAR_3 = "Call a bear";
    public static String ACTION_BEAR_4 = "Costs 1 action";
    public static String ACTION_BEAR_5 = "Bear";

    public static String ACTION_BREW_1 = "Brew a Healing Potion for an ally. The next time the target is damaged and survives, they're healed. The potion can also be used as an action. Either use consumes the potion. Units can carry 1 potion, a new potion will replace the old one.";
    public static String ACTION_BREW_2 = "Brew a Strength Potion for an ally. The next time that ally attacks an enemy, it destroys it. Units can carry 1 potion, a new potion will replace the old one.";
    public static String ACTION_BREW_3 = "Brew a Love Potion for an ally. The ally can drink the potion and kiss an enemy to persuade them to join your army. This consumes the potion. This potion is rare and expensive to brew, and can only be created once per game. Units can carry 1 potion, a new potion will replace the old one.";
    public static String ACTION_BREW_4 = "Brew an Escape potion for an ally. The ally can return to their castle. Units can carry 1 potion, a new potion will replace the old one.";
    public static String ACTION_BREW_5 = "Heal Potion";
    public static String ACTION_BREW_6 = "Give ally Healing Potion";
    public static String ACTION_BREW_7 = "Strength Potion";
    public static String ACTION_BREW_8 = "Give ally Strength Potion";
    public static String ACTION_BREW_9 = "Love Potion";
    public static String ACTION_BREW_10 = "Give ally Love Potion";
    public static String ACTION_BREW_11 = "Escape Potion";
    public static String ACTION_BREW_12 = "Give ally Escape Potion";
    public static String ACTION_BREW_13 = " brews a potion for ";
    public static String ACTION_BREW_14 = "1 action, once per game";

    public static String ACTION_BROODMOTHER_1 = "Moves to and devours enemies.";
    public static String ACTION_BROODMOTHER_2 = "";
    public static String ACTION_BROODMOTHER_3 = "The Wyvern can move into the space of an enemy organic unit and devour it. If the target unit has a way to protect itself from death, it instead returns to the castle. If the Wyvern devours a unit this way, it becomes fed, and will lay an egg the next time it moves. Eggs hatch into new Wyverns after a few turns.";
    public static String ACTION_BROODMOTHER_4 = "Broodmother";

    public static String ACTION_WYVERN_MOVE_1 = "";

    public static String ACTION_BUILD_1 = " has seized the castle!";
    public static String ACTION_BUILD_2 = " builds a wall at ";
    public static String ACTION_BUILD_3 = "Build a wall";
    public static String ACTION_BUILD_4 = "Costs 1 action";
    public static String ACTION_BUILD_5 = "Build wall";

    public static String ACTION_CASTLE_1 = "Relocate your castle to the selected square. Any units on your castle are moved along with it.";
    public static String ACTION_CASTLE_2 = " moves castle to ";
    public static String ACTION_CASTLE_3 = "Relocate the castle";
    public static String ACTION_CASTLE_4 = "Castle";

    public static String ACTION_CHANNEL_BLAST_1 = "Attack the target for an amount of damage equal to the Channeler's remaining energy. This attack depletes the Channeler of energy.";
    public static String ACTION_CHANNEL_BLAST_2 = " misses ";
    public static String ACTION_CHANNEL_BLAST_3 = " hits ";
    public static String ACTION_CHANNEL_BLAST_4 = "Blast enemy with all energy";
    public static String ACTION_CHANNEL_BLAST_5 = "Anywhere within ";
    public static String ACTION_CHANNEL_BLAST_6 = "Consumes all energy";
    public static String ACTION_CHANNEL_BLAST_7 = "Energy Blast";

    public static String ACTION_CHANNEL_BOLT_1 = "Attack target for 3 damage. This costs the Channeler 1 energy to use.";
    public static String ACTION_CHANNEL_BOLT_2 = " misses ";
    public static String ACTION_CHANNEL_BOLT_3 = " hits ";
    public static String ACTION_CHANNEL_BOLT_4 = "Attack with energy for 3";
    public static String ACTION_CHANNEL_BOLT_5 = "Anywhere within ";
    public static String ACTION_CHANNEL_BOLT_6 = "Consumes 1 energy";
    public static String ACTION_CHANNEL_BOLT_7 = "Energy Bolt";

    public static String ACTION_CHARGE_EMPOWER_1 = "Gather magical energies and recharge your ability to empower an ally.";
    public static String ACTION_CHARGE_EMPOWER_2 = " gathers power";
    public static String ACTION_CHARGE_EMPOWER_3 = "Gather enchanting power";
    public static String ACTION_CHARGE_EMPOWER_4 = "Renew Power";

    public static String ACTION_CLIMB_1 = "The Mason uses a secret, hidden stairway to climb to the top of his wall. This allows the Mason to travel along walls and be protected by them.";
    public static String ACTION_CLIMB_2 = " climbs ";
    public static String ACTION_CLIMB_3 = "Climb wall";
    public static String ACTION_CLIMB_4 = "Climb";

    public static String ACTION_COLLAPSE_1 = "The Mason left a secret keystone that, if pulled, will collapse this wall.";
    public static String ACTION_COLLAPSE_2 = " was collapsed";
    public static String ACTION_COLLAPSE_3 = "Collapse this wall";
    public static String ACTION_COLLAPSE_4 = "Collapse";

    public static String ACTION_CURSE_1 = "Curse the enemy army. Their armor is reduced by 1 until the start of your next turn.";
    public static String ACTION_CURSE_2 = " curses your army";
    public static String ACTION_CURSE_3 = "Enemy army gets -1 armor";
    public static String ACTION_CURSE_4 = "1 action, lasts 1 turn";
    public static String ACTION_CURSE_5 = "Curse";

    public static String ACTION_DAMN_1 = " fizzles the spell";
    public static String ACTION_DAMN_2 = " has seized the castle!";
    public static String ACTION_DAMN_3 = " damns ";
    public static String ACTION_DAMN_4 = "Unit becomes Demon ally";
    public static String ACTION_DAMN_5 = "Any unit range 2 or one";
    public static String ACTION_DAMN_6 = "allied Imp anywhere (1 action)";
    public static String ACTION_DAMN_7 = "Damn";

    public static String ACTION_DEATH_1 = "Target enemy dies.";
    public static String ACTION_DEATH_2 = " misses ";
    public static String ACTION_DEATH_3 = " kills ";
    public static String ACTION_DEATH_4 = "Kill enemy unit";
    public static String ACTION_DEATH_5 = "Kill";

    public static String ACTION_DEMON_1 = " fizzles the spell";
    public static String ACTION_DEMON_2 = " has seized the castle!";
    public static String ACTION_DEMON_3 = " damns ";
    public static String ACTION_DEMON_4 = "Imp becomes Demon.";
    public static String ACTION_DEMON_5 = "Anywhere";
    public static String ACTION_DEMON_6 = "Costs 1 action";
    public static String ACTION_DEMON_7 = "Demon";

    public static String ACTION_ARCHDEMON_4 = "Archdemon Apotheosis.";
    public static String ACTION_ARCHDEMON_7 = "Archdemon.";

    public static String ACTION_DEMONIC_PACT_1 = "The diabolist transforms, becoming a hungering demon. The diabolist gains an additional 4 hp and 3 actions. In demon form, the diabolist loses 1 soul per turn, and dies if it is at 0 souls at the end of your turn.";
    public static String ACTION_DEMONIC_PACT_2 = "Hunger";
    public static String ACTION_DEMONIC_PACT_3 = "Loses one soul per turn";
    public static String ACTION_DEMONIC_PACT_4 = "Dies at 0 souls";
    public static String ACTION_DEMONIC_PACT_5 = "At the end of each turn, if this unit has 0 souls, it dies. Loses one soul at the end of each turn.";
    public static String ACTION_DEMONIC_PACT_6 = " transformed";
    public static String ACTION_DEMONIC_PACT_7 = "Transform into demon";
    public static String ACTION_DEMONIC_PACT_8 = "Can be done once";
    public static String ACTION_DEMONIC_PACT_9 = "Demonic Pact";

    public static String ACTION_DETONATE_1 = " fizzles the spell";
    public static String ACTION_DETONATE_2 = " is burned for ";
    public static String ACTION_DETONATE_3 = " is burned for ";
    public static String ACTION_DETONATE_4 = " detonates ";
    public static String ACTION_DETONATE_5 = "Unit dies and explodes for ";
    public static String ACTION_DETONATE_6 = "Anywhere within ";
    public static String ACTION_DETONATE_7 = "Costs 1 action, 1 soul";
    public static String ACTION_DETONATE_8 = "Detonate";

    public static String ACTION_DUPLICATE_1 = "The Mimic gains the abilities and characteristics of target enemy. Shapeshifting heals the Mimic and cleanses it of effects. Using this ability stuns the Mimic.";
    public static String ACTION_DUPLICATE_2 = " fizzles the spell";
    public static String ACTION_DUPLICATE_3 = " becomes a ";
    public static String ACTION_DUPLICATE_4 = "Transform into enemy unit";
    public static String ACTION_DUPLICATE_5 = "Costs 2 actions";
    public static String ACTION_DUPLICATE_6 = "Duplicate";

    public static String ACTION_EGG_1 = " has seized the castle!";
    public static String ACTION_EGG_2 = " lays an egg";
    public static String ACTION_EGG_3 = "Lay an egg";
    public static String ACTION_EGG_4 = "Must be fed";
    public static String ACTION_EGG_5 = "Egg";

    public static String ACTION_EMPOWER_1 = "Target ally gains +1 power and +1 armor permanently. Units may never have more than 2 armor. This spell must be recharged after being cast.";
    public static String ACTION_EMPOWER_2 = " empowers ";
    public static String ACTION_EMPOWER_3 = "Ally gains 1 power/armor";
    public static String ACTION_EMPOWER_4 = ", must be recharged";
    public static String ACTION_EMPOWER_5 = "Empower";

    public static String ACTION_ENERGY_1 = "Energy is used instead of actions by Channelers to perform their attacks.";
    public static String ACTION_ENERGY_2 = "Energy: ";
    public static String ACTION_ENERGY_3 = "Gains 2 energy per turn";
    public static String ACTION_ENERGY_4 = "Attacks use energy, not actions";
    public static String ACTION_ENERGY_5 = "Energy";

    public static String ACTION_ENTOMB_1 = "Entomb the target in a prison of solid rock. The unit will remain trapped inside until the rock is destroyed.";
    public static String ACTION_ENTOMB_2 = " fizzles the spell";
    public static String ACTION_ENTOMB_3 = "nobody";
    public static String ACTION_ENTOMB_4 = " entombs ";
    public static String ACTION_ENTOMB_5 = "Entomb unit within a rock";
    public static String ACTION_ENTOMB_6 = "Costs 2 actions";
    public static String ACTION_ENTOMB_7 = "Entomb";

    public static String ACTION_ESCAPE_POTION_1 = "Drink the Escape Potion and return to the castle.";
    public static String ACTION_ESCAPE_POTION_2 = " drinks the Escape Potion";
    public static String ACTION_ESCAPE_POTION_3 = "Return to castle";
    public static String ACTION_ESCAPE_POTION_4 = "0 actions, consume after use";
    public static String ACTION_ESCAPE_POTION_5 = "Escape Potion";

    public static String ACTION_FIREBALL_1 = " breathes fire";
    public static String ACTION_FIREBALL_2 = " shoots oil arrow";
    public static String ACTION_FIREBALL_3 = " casts a fireball";
    public static String ACTION_FIREBALL_4 = "Location explodes for ";
    public static String ACTION_FIREBALL_5 = "Fireball";
    public static String ACTION_FIREBALL_6 = "Pick a square within range. All units within 1 range of that square are attacked for ";
    public static String ACTION_FIREBALL_7 = " damage. This affects allies and enemies.";

    public static String ACTION_FLATTER_1 = "The Sycophant betrays you, showering an enemy with praise to ingratiate himself into its service. The Sycophant joins the enemy, and bonds himself to his new master. Both units become belligerently arrogant and immune to all allied effects. Due to his cunning planning, his betrayal renders him immune to enemies until the start of his next turn.";
    public static String ACTION_FLATTER_2 = " failed to flatter";
    public static String ACTION_FLATTER_3 = " flatters ";
    public static String ACTION_FLATTER_4 = "Enemy becomes new master";
    public static String ACTION_FLATTER_5 = "1 action, once per game";
    public static String ACTION_FLATTER_6 = "Flatter";

    public static String ACTION_FIGHT_1 = "Movement doesn't cost actions";
    public static String ACTION_FIGHT_2 = "Can jump over units";
    public static String ACTION_FIGHT_3 = "This unit flies into battle. Moving doesn't cost this unit action points. Flying units are able to jump over other units.";
    public static String ACTION_FIGHT_4 = "Flight";

    public static String ACTION_FLY_1 = "Target ally gains the ability to fly. Units with 2 or more range can fly 2 squares, and melee units can fly 3 squares. Flying doesn't cost a unit action points. Gaining the ability to fly stuns the target.";
    public static String ACTION_FLY_2 = " levitates ";
    public static String ACTION_FLY_3 = "Grant unit flight";
    public static String ACTION_FLY_4 = "Costs 1 action, stuns target";
    public static String ACTION_FLY_5 = "Levitate";

    public static String ACTION_FORTIFY_1 = "Raise the armor of all inorganic allies permanently by 1. Units may never have more than 2 armor.";
    public static String ACTION_FORTIFY_2 = " fortifies inorganics";
    public static String ACTION_FORTIFY_3 = "+1 armor for inorganic allies";
    public static String ACTION_FORTIFY_4 = "Maximum 2 armor";
    public static String ACTION_FORTIFY_5 = "Fortify";

    public static String ACTION_GATE_1 = " has seized the castle!";
    public static String ACTION_GATE_2 = " conjures a gate";
    public static String ACTION_GATE_3 = "Conjure a gate";
    public static String ACTION_GATE_4 = "Costs 1 action";
    public static String ACTION_GATE_5 = "Gate";

    public static String ACTION_GHOST_1 = " releases a ghost at ";
    public static String ACTION_GHOST_2 = "Release a ghost";
    public static String ACTION_GHOST_3 = "Costs 1 action, 2 souls";
    public static String ACTION_GHOST_4 = "Ghost";

    public static String ACTION_HEAL_1 = "Restore target ally's life to maximum.";
    public static String ACTION_HEAL_2 = " heals ";
    public static String ACTION_HEAL_3 = "Heal target ally";
    public static String ACTION_HEAL_4 = "Heal";

    public static String ACTION_SALVATION_1 = "Restore target ally's life to maximum and remove the murderer status";
    public static String ACTION_SALVATION_2 = " saves ";
    public static String ACTION_SALVATION_3 = "Heal and forgive ally";
    public static String ACTION_SALVATION_4 = "Salvation";

    public static String ACTION_HYDRA_ATTACK_1 = "Attack the target, inflicting damage equal to this unit's power. The Hydra can attack once per turn for each remaining head.";
    public static String ACTION_HYDRA_ATTACK_2 = " misses ";
    public static String ACTION_HYDRA_ATTACK_3 = " hits ";
    public static String ACTION_HYDRA_ATTACK_4 = " for ";
    public static String ACTION_HYDRA_ATTACK_5 = "Attack enemy";
    public static String ACTION_HYDRA_ATTACK_6 = "Can be done ";
    public static String ACTION_HYDRA_ATTACK_7 = " times";
    public static String ACTION_HYDRA_ATTACK_8 = "Attack";

    public static String ACTION_IMP_1 = " has seized the castle!";
    public static String ACTION_IMP_2 = " deploys an imp at ";
    public static String ACTION_IMP_3 = "Summon an imp";
    public static String ACTION_IMP_4 = "Costs 1 action";
    public static String ACTION_IMP_5 = "Imp";

    public static String ACTION_INORGANIC_1 = "Inorganic";
    public static String ACTION_INORGANIC_2 = "Immune to spells and skills";
    public static String ACTION_INORGANIC_3 = "Inorganic units are immune to all spell (purple) and skill (green) actions. When attacked, spells and skills attached to the attack don't trigger.";
    public static String ACTION_INORGANIC_4 = "Inorganic";

    public static String ACTION_INVERT_1 = " inverts ";
    public static String ACTION_INVERT_2 = " is burned for ";
    public static String ACTION_INVERT_3 = "Explode conjuration for ";
    public static String ACTION_INVERT_4 = "Invert";
    public static String ACTION_INVERT_5 = "Invert an allied gate or portal, creating an explosion damaging each surrounding unit for 4. This affects enemies and allies. Any other conjuration caught in the explosion will also invert.";

    public static String ACTION_INVINCIBLE_1 = "The Templar becomes indestructible and immune to everything. While indestructible, the Templar is unable to move.";
    public static String ACTION_INVINCIBLE_2 = "The Templar will no longer be indestructible, but will regain the ability to move. Doing this stuns the Templar for 1 turn.";
    public static String ACTION_INVINCIBLE_3 = " is invincible";
    public static String ACTION_INVINCIBLE_4 = " is vulnerable";
    public static String ACTION_INVINCIBLE_5 = "Become vulnerable";
    public static String ACTION_INVINCIBLE_6 = "Become indestructible";
    public static String ACTION_INVINCIBLE_7 = "1 action, unit stuns";
    public static String ACTION_INVINCIBLE_8 = "1 action, can't move";
    public static String ACTION_INVINCIBLE_9 = "Invincible";

    public static String ACTION_LICH_1 = " becomes a lich";
    public static String ACTION_LICH_2 = "Transform into a lich";
    public static String ACTION_LICH_3 = "Once a game, 1 action";
    public static String ACTION_LICH_4 = "Lich";

    public static String ACTION_LIFE_1 = "Raise target ally's maximum life by 1 and heal them.";
    public static String ACTION_LIFE_2 = " blesses ";
    public static String ACTION_LIFE_3 = "Heal and raise ally's life by 1";
    public static String ACTION_LIFE_4 = "Gift of life";

    public static String ACTION_LIGTNING_1 = "Select a square. Any unit on that square or any square between it and the caster is attacked for 5 damage. This affects allies and enemies.";
    public static String ACTION_LIGTNING_2 = " casts lightning";
    public static String ACTION_LIGTNING_3 = " damage along line";
    public static String ACTION_LIGTNING_4 = "Any location on a ";
    public static String ACTION_LIGTNING_5 = " line";
    public static String ACTION_LIGTNING_6 = "Lightning";

    public static String ACTION_LOVE_POTION_1 = "Drink the Love Potion and kiss an enemy. The enemy permanently joins your side. The overwhelming shock of emotion leaves the unit stunned for 1 turn.";
    public static String ACTION_LOVE_POTION_2 = " fizzles the spell";
    public static String ACTION_LOVE_POTION_3 = " has seized the castle!";
    public static String ACTION_LOVE_POTION_4 = " kisses ";
    public static String ACTION_LOVE_POTION_5 = "Drink potion and kiss enemy";
    public static String ACTION_LOVE_POTION_6 = "1 action, consumed after use";
    public static String ACTION_LOVE_POTION_7 = "Love Potion";

    public static String ACTION_LYCANTHROPE_1 = " becomes human";
    public static String ACTION_LYCANTHROPE_2 = "Transform into a human";
    public static String ACTION_LYCANTHROPE_3 = "Free, stuns this unit";
    public static String ACTION_LYCANTHROPE_4 = "Human";

    public static String ACTION_LYCAN_WOLF_1 = " becomes a wolf";
    public static String ACTION_LYCAN_WOLF_2 = "Transform into a wolf";
    public static String ACTION_LYCAN_WOLF_3 = "Free, stuns this unit";
    public static String ACTION_LYCAN_WOLF_4 = "LycanWolf";

    public static String ACTION_MARK_1 = "The Bounty Hunter studies his prey carefully, activating his other abilities against the target.";
    public static String ACTION_MARK_2 = " fizzled the mark";
    public static String ACTION_MARK_3 = " marks ";
    public static String ACTION_MARK_4 = "Mark target enemy";
    public static String ACTION_MARK_5 = "Mark";
    public static String ACTION_MARK_6 = "The Conspirator closely analyzes her targets, activating her other abilities against the target";

    public static String ACTION_MECHANIZE_1 = "Target ally gains the Inorganic trait, rendering it immune to spells and skills.";
    public static String ACTION_MECHANIZE_2 = " mechanizes ";
    public static String ACTION_MECHANIZE_3 = "Ally becomes inorganic";
    public static String ACTION_MECHANIZE_4 = "Costs 1 action";
    public static String ACTION_MECHANIZE_5 = "Mechanize";

    public static String ACTION_MOUNTED_1 = "Movement doesn't cost actions";
    public static String ACTION_MOUNTED_2 = "This unit depends on its mount to move. Moving doesn't cost this unit action points.";
    public static String ACTION_MOUNTED_3 = "Mounted";

    public static String ACTION_MOVE_1 = "Move to a new location.";
    public static String ACTION_MOVE_2 = " has seized the castle!";
    public static String ACTION_MOVE_3 = " moves to ";
    public static String ACTION_MOVE_4 = "Jump to target location";
    public static String ACTION_MOVE_5 = "Move to target location";
    public static String ACTION_MOVE_6 = "Can be done ";
    public static String ACTION_MOVE_7 = " times";
    public static String ACTION_MOVE_8 = "Move";

    public static String ACTION_MOVE_CASTLE_1 = "Move to a new location, including your own castle.";

    public static String ACTION_OBLITERATE_1 = "Destroy an enemy by crushing them with a boulder.";
    public static String ACTION_OBLITERATE_2 = "Destroy an enemy by smashing them with the Golem's giant, stone fist.";
    public static String ACTION_OBLITERATE_3 = "Who the hell knows. You should never see this.";
    public static String ACTION_OBLITERATE_4 = " misses ";
    public static String ACTION_OBLITERATE_5 = " obliterates ";
    public static String ACTION_OBLITERATE_6 = "Destroy enemy";
    public static String ACTION_OBLITERATE_7 = "Obliterate";

    public static String ACTION_OVERWATCH_1 = "The Bowman stands guard, attacking enemies that move into a square within attack range. This ends if the Bowman moves.";
    public static String ACTION_OVERWATCH_2 = " stands guard";
    public static String ACTION_OVERWATCH_3 = "Attack approaching enemies";
    public static String ACTION_OVERWATCH_4 = "Ends when this unit moves";
    public static String ACTION_OVERWATCH_5 = "Costs 1 action";
    public static String ACTION_OVERWATCH_6 = "Overwatch";

    public static String ACTION_PARALYZE_1 = "Temporarily rob the life from their limbs. The target is stunned until the end of their turn.";
    public static String ACTION_PARALYZE_2 = " fizzles the spell";
    public static String ACTION_PARALYZE_3 = " paralyzes ";
    public static String ACTION_PARALYZE_4 = "Stun enemy unit";
    public static String ACTION_PARALYZE_5 = ", lasts 1 turn";
    public static String ACTION_PARALYZE_6 = "Paralyze";

    public static String ACTION_PLAN_1 = "Add 1 plan to your Command Post. Plans can be spent to raise your commands. The increase is immediate, and only lasts the turn you gain them. This action will activate automatically if you have spare commands left at the end of your turn.";
    public static String ACTION_PLAN_2 = " plans";
    public static String ACTION_PLAN_3 = "Add 1 plan";
    public static String ACTION_PLAN_4 = "Costs this unit 1 action";
    public static String ACTION_PLAN_5 = "Plans";

    public static String ACTION_POISON_1 = "Fire a poisoned dart at an enemy. Poisoned units permanently lose 1 life at the end of their turn.";
    public static String ACTION_POISON_2 = " misses ";
    public static String ACTION_POISON_3 = " poisons ";
    public static String ACTION_POISON_4 = "Poison enemy (-1 life/turn)";
    public static String ACTION_POISON_5 = "Poison";

    public static String ACTION_PORTAL_1 = " has seized the castle!";
    public static String ACTION_PORTAL_2 = " conjures a portal";
    public static String ACTION_PORTAL_3 = "Conjure a portal";
    public static String ACTION_PORTAL_4 = "Costs 1 action";
    public static String ACTION_PORTAL_5 = "Portal";

    public static String ACTION_PROMOTE_OFFENSE_1 = "The promoted soldier receives +1 Power and +1 Actions. A soldier can only have one promotion at a time. Adding a new promotion replaces the old one. A soldier can have a relic and still be promoted.";
    public static String ACTION_PROMOTE_OFFENSE_2 = "Field Promotion: Striker";
    public static String ACTION_PROMOTE_OFFENSE_3 = "Soldier gets +1 Power, +1 Actions";
    public static String ACTION_PROMOTE_OFFENSE_4 = "Promote: Striker";
    public static String ACTION_PROMOTE_DEFENSE_1 = "The promoted soldier receives +1 Life and +1 Armor. A soldier can only have one promotion at a time. Adding a new promotion replaces the old one. A soldier can have a relic and still be promoted.";
    public static String ACTION_PROMOTE_DEFENSE_2 = "Field Promotion: Defender";
    public static String ACTION_PROMOTE_DEFENSE_3 = "Soldier gets +1 Life, +1 Armor";
    public static String ACTION_PROMOTE_DEFENSE_4 = "Promote: Defender";
    public static String ACTION_PROMOTE_RANGE = "1 range";


    public static String ACTION_PROTECT_1 = "The Shield Maiden focuses her protective power on an allied unit. Any damage that unit takes will be redirected to the Shield Maiden, using her armor value. The protected unit is still considered to be attacked, and any attached affects still occur. The Shield Maiden can only protect one target at a time. Protecting a new unit will cancel the previous protection.";
    public static String ACTION_PROTECT_2 = " protects ";
    public static String ACTION_PROTECT_3 = "Begin protecting ally";
    public static String ACTION_PROTECT_4 = "Protect";

    public static String ACTION_PURGE_1 = " fizzles the spell";
    public static String ACTION_PURGE_2 = " purges ";
    public static String ACTION_PURGE_3 = "Reset target unit's stats";
    public static String ACTION_PURGE_4 = "1 action, stuns target";
    public static String ACTION_PURGE_5 = "Purge";
    public static String ACTION_PURGE_6 = "The target of this spell is stunned, and resets to a freshly deployed state. This heals the unit and removes all harmful and beneficial effects from it.";

    public static String ACTION_RALLY_1 = "Allied, melee range unit no longer costs commands when it acts. The sergeant can only have one unit rallied at a time. Using it on a new target will cancel the previous effect.  Target must not have an attack with range greater than 1, skills and spells are not counted.";
    public static String ACTION_RALLY_2 = " rallies ";
    public static String ACTION_RALLY_3 = "Ally acts at no cost";
    public static String ACTION_RALLY_4 = " range, melee only";
    public static String ACTION_RALLY_5 = "Rally";

    public static String ACTION_RECALL_1 = " fizzles the spell";
    public static String ACTION_RECALL_2 = " recalls ";
    public static String ACTION_RECALL_3 = "Return target to their castle";
    public static String ACTION_RECALL_4 = "Costs 1 action";
    public static String ACTION_RECALL_5 = "Recall";
    public static String ACTION_RECALL_6 = "Ally is returned to your castle as an undeployed unit.";

    public static String ACTION_RECONSTRUCT_1 = "All allies with the inorganic trait heal up to their maximum life.";
    public static String ACTION_RECONSTRUCT_2 = " reconstructs inorganics";
    public static String ACTION_RECONSTRUCT_3 = "Heal all inorganic allies";
    public static String ACTION_RECONSTRUCT_4 = "Reconstruct";

    public static String ACTION_RELOAD_1 = "Reload the crossbow to prepare for another attack.";
    public static String ACTION_RELOAD_2 = " reloads crossbow";
    public static String ACTION_RELOAD_3 = "Reload crossbow";
    public static String ACTION_RELOAD_4 = "Reload";

    public static String ACTION_RUSH_1 = "Run up to the targetted enemy, and strike it with an attack. This ability counts as both a move and an attack action, and activates triggered abilities for both.";
    public static String ACTION_RUSH_2 = " has seized the castle!";
    public static String ACTION_RUSH_3 = " misses ";
    public static String ACTION_RUSH_4 = " hits ";
    public static String ACTION_RUSH_5 = " for ";
    public static String ACTION_RUSH_6 = "Move to unit and attack it";
    public static String ACTION_RUSH_7 = "Costs 1 action";
    public static String ACTION_RUSH_8 = "Rush";

    public static String ACTION_SACRIFICE_1 = "Perform ritual sacrifice on a unit to gain extra energy from its released soul.";
    public static String ACTION_SACRIFICE_2 = " fizzles the spell";
    public static String ACTION_SACRIFICE_3 = " sacrifices ";
    public static String ACTION_SACRIFICE_4 = "Sacrifice target unit";
    public static String ACTION_SACRIFICE_5 = "Costs 1 action, gain 1 soul";
    public static String ACTION_SACRIFICE_6 = "Sacrifice";

    public static String ACTION_SEAL_1 = "This spell creates an indestructible seal over your castle's gate that lasts for 2 turns. Your castle's square must be empty before this spell can be cast.";
    public static String ACTION_SEAL_2 = " seals the gate";
    public static String ACTION_SEAL_3 = "Seal the castle gate";
    public static String ACTION_SEAL_4 = "1 action, lasts 2 turns";
    public static String ACTION_SEAL_5 = "Seal";

    public static String ACTION_SERPENT_1 = " has seized the castle!";
    public static String ACTION_SERPENT_2 = " severs a head";
    public static String ACTION_SERPENT_3 = "Sever head into a serpent";
    public static String ACTION_SERPENT_4 = "Costs 1 life";
    public static String ACTION_SERPENT_5 = "Serpent";

    public static String ACTION_SERPENTS_1 = " has seized the castle!";
    public static String ACTION_SERPENTS_2 = " calls a serpent";
    public static String ACTION_SERPENTS_3 = "Call a serpent";
    public static String ACTION_SERPENTS_4 = "Costs 1 action";
    public static String ACTION_SERPENTS_5 = "Serpent";

    public static String ACTION_SHIELD_1 = "The Acolyte focuses their protective power on an allied unit, reducing the next damage it takes to 0. The unit is still considered to be hit, and any effects attached to the attack will still occur. The Acolyte can only shield one unit at a time. Shielding a new target will cancel the previous shield.";
    public static String ACTION_SHIELD_2 = " shields ";
    public static String ACTION_SHIELD_3 = "Ally ignores enemy damage";
    public static String ACTION_SHIELD_4 = "1 action, ends when triggered";
    public static String ACTION_SHIELD_5 = "Shield";

    public static String ACTION_SHOW_PLANS_1 = "Plans can be spent to raise your commands. The increase is immediate, and only lasts the turn you gain them.";
    public static String ACTION_SHOW_PLANS_2 = "Plans: ";
    public static String ACTION_SHOW_PLANS_3 = "Planning";

    public static String ACTION_SICKEN_1 = "Afficts the target with sickness that immediately lowers their life by 2.";
    public static String ACTION_SICKEN_2 = " fizzles the spell";
    public static String ACTION_SICKEN_3 = " sickens ";
    public static String ACTION_SICKEN_4 = "Enemy's life lowered by 2";
    public static String ACTION_SICKEN_5 = "Sicken";

    public static String ACTION_SKELETON_1 = " has seized the castle!";
    public static String ACTION_SKELETON_2 = " creates a skeleton";
    public static String ACTION_SKELETON_3 = "Summon a skeleton";
    public static String ACTION_SKELETON_4 = "Costs 1 action";
    public static String ACTION_SKELETON_5 = "Skeleton";

    public static String ACTION_SOLDIER_1 = " has seized the castle!";
    public static String ACTION_SOLDIER_2 = " creates a soldier";
    public static String ACTION_SOLDIER_3 = "Deploy a soldier";
    public static String ACTION_SOLDIER_4 = "Costs 1 action";
    public static String ACTION_SOLDIER_5 = "Soldier";

    public static String ACTION_SPIRIT_1 = " transcends";
    public static String ACTION_SPIRIT_2 = "Transform into a spirit";
    public static String ACTION_SPIRIT_3 = "Once a game, 1 action";
    public static String ACTION_SPIRIT_4 = "Spirit";

    public static String ACTION_SPOTTER_1 = "Select a spotter to help pick targets. Whenever the spotter attacks an enemy, the Longbowman will attack the same enemy at no cost.  The Longbowman can only have one spotter at a time.";
    public static String ACTION_SPOTTER_2 = " spots for ";
    public static String ACTION_SPOTTER_3 = "Select a spotter";
    public static String ACTION_SPOTTER_4 = "Spotter";

    public static String ACTION_STUNBALL_1 = "Pick a square within range. All units within 1 range of that square are stunned for 1 turn. This affects allies and enemies.";
    public static String ACTION_STUNBALL_2 = " is stunned";
    public static String ACTION_STUNBALL_3 = " dazzles";
    public static String ACTION_STUNBALL_4 = "Stun units within explosion";
    public static String ACTION_STUNBALL_5 = "Dazzle";

    public static String ACTION_SUICIDAL_1 = "Force an enemy to take this unit's life.";
    public static String ACTION_SUICIDAL_2 = " fizzles the spell";
    public static String ACTION_SUICIDAL_3 = " kills ";
    public static String ACTION_SUICIDAL_4 = "Enemy kills this unit";
    public static String ACTION_SUICIDAL_5 = "Suicide";

    public static String ACTION_SUICIDE_1 = "Attack the target, inflicting damage equal to this unit's power to every unit within one square of the target.  This unit is destroyed after the attack is performed.";
    public static String ACTION_SUICIDE_2 = " misses ";
    public static String ACTION_SUICIDE_3 = " hits ";
    public static String ACTION_SUICIDE_4 = " for ";
    public static String ACTION_SUICIDE_5 = "Attack an enemy";
    public static String ACTION_SUICIDE_6 = ", self destructs";
    public static String ACTION_SUICIDE_7 = "Attack";

    public static String ACTION_SUMMON_1 = "Summon a unit to this location and stun it. This destroys the Portal.";
    public static String ACTION_SUMMON_2 = " summons ";
    public static String ACTION_SUMMON_3 = "Summon unit to this location";
    public static String ACTION_SUMMON_4 = "1 action, this dies";
    public static String ACTION_SUMMON_5 = "Summon";

    public static String ACTION_SWITCH_1 = "This unit and the target switch positions. The movement doesn't activate any triggered abilities.";
    public static String ACTION_SWITCH_2 = " fizzles the spell";
    public static String ACTION_SWITCH_3 = " has seized the castle!";
    public static String ACTION_SWITCH_4 = " and ";
    public static String ACTION_SWITCH_5 = " trade places";
    public static String ACTION_SWITCH_6 = "Trade places with target unit";
    public static String ACTION_SWITCH_7 = "Can be done ";
    public static String ACTION_SWITCH_8 = " times";
    public static String ACTION_SWITCH_9 = "Switch places";

    public static String ACTION_TOAD_1 = " fizzles the spell";
    public static String ACTION_TOAD_2 = " transforms ";
    public static String ACTION_TOAD_3 = "Enemy becomes a toad";
    public static String ACTION_TOAD_4 = "Costs 1 action";
    public static String ACTION_TOAD_5 = "Toad";

    public static String ACTION_TRADE_1 = "This unit and the target trade places. Your opponent gains control of this unit, and you gain control of the target.";
    public static String ACTION_TRADE_2 = " fizzles the spell";
    public static String ACTION_TRADE_3 = " was a ";
    public static String ACTION_TRADE_4 = "Trade this unit for target enemy";
    public static String ACTION_TRADE_5 = "Trade";

    public static String ACTION_TRUCE_1 = "Ally becomes immune to enemies until it performs an action aside from moving away from the enemy castle. Only units that haven't acted this turn are eligible for immunity. The Diplomat can negotiate one truce at a time. A new target will cancel the previous one.";
    public static String ACTION_TRUCE_2 = " negotiates truce";
    public static String ACTION_TRUCE_3 = "Give ally diplomatic immunity";
    public static String ACTION_TRUCE_4 = "Truce";

    public static String ACTION_TWIN_1 = "The Doppelganger spawns a copy of itself. Another twin can't be created until either twin dies. If you lose control over a twin, it immediately dies. The twin copies the injuries of the original.";
    public static String ACTION_TWIN_2 = " has seized the castle!";
    public static String ACTION_TWIN_3 = " spawns twin at ";
    public static String ACTION_TWIN_4 = "Spawn a twin";
    public static String ACTION_TWIN_5 = "1 action (max 1 twin)";
    public static String ACTION_TWIN_6 = "Twin";

    public static String ACTION_UNDUPLICATE_1 = "Return to the Mimic's true form. This will leave the Mimic stunned for the turn.";
    public static String ACTION_UNDUPLICATE_2 = " unshifts";
    public static String ACTION_UNDUPLICATE_3 = "Revert to Mimic form";
    public static String ACTION_UNDUPLICATE_4 = "1 action, stuns this unit";
    public static String ACTION_UNDUPLICATE_5 = "Unshift";

    public static String ACTION_USE_PLANS_1 = "All of the plans saved up by Command Post are converted into commands. If the commands aren't used, they'll be lost at the end of the turn.";
    public static String ACTION_USE_PLANS_2 = " uses plans";
    public static String ACTION_USE_PLANS_3 = "Convert plans to commands";
    public static String ACTION_USE_PLANS_4 = "Costs 1 action, all plans";
    public static String ACTION_USE_PLANS_5 = "Use Plans";

    public static String ACTION_WALL_WALK_1 = "The Mason can walk along Walls after climbing. While on a Wall, the Wall will take any attacks directed at the Mason. If that Wall is destroyed, the Mason will appear on the same square without the protection of the Wall.";
    public static String ACTION_WALL_WALK_2 = " walks along the wall";
    public static String ACTION_WALL_WALK_3 = "Move to target location";
    public static String ACTION_WALL_WALK_4 = "Wall walk";

    public static String ACTION_WARD_1 = "All allied units gain +1 armor until the beginning of your next turn. Units may never have more than 2 armor.";
    public static String ACTION_WARD_2 = " wards their army";
    public static String ACTION_WARD_3 = "Army gets +1 armor (max 2)";
    public static String ACTION_WARD_4 = "1 action, lasts 1 turn";
    public static String ACTION_WARD_5 = "Ward";

    public static String ACTION_WEAPON_SWITCH_1 = "The ranger switches between using a bow and a sword. The bow is 3 range. The sword is 1 range and +2 power.";
    public static String ACTION_WEAPON_SWITCH_2 = " wields ";
    public static String ACTION_WEAPON_SWITCH_3 = "bow";
    public static String ACTION_WEAPON_SWITCH_4 = "sword";
    public static String ACTION_WEAPON_SWITCH_5 = "Switch to ";
    public static String ACTION_WEAPON_SWITCH_6 = "melee";
    public static String ACTION_WEAPON_SWITCH_7 = "ranged";
    public static String ACTION_WEAPON_SWITCH_8 = "Switch Weapon";

    public static String ACTION_WEREWOLF_1 = " becomes a werewolf";
    public static String ACTION_WEREWOLF_2 = "Transform into a werewolf";
    public static String ACTION_WEREWOLF_3 = "Free, stuns this unit";
    public static String ACTION_WEREWOLF_4 = "Werewolf";

    public static String ACTION_WILL_WISPS_1 = " creates will o' the wisps";
    public static String ACTION_WILL_WISPS_2 = "Create will o' the wisps";
    public static String ACTION_WILL_WISPS_3 = "Costs 1 action";
    public static String ACTION_WILL_WISPS_4 = "Will o' the wisps";

    public static String ACTION_WOLF_1 = " has seized the castle!";
    public static String ACTION_WOLF_2 = " deploys a wolf at ";
    public static String ACTION_WOLF_3 = "Deploy your wolf";
    public static String ACTION_WOLF_4 = "Once a game, 1 action";
    public static String ACTION_WOLF_5 = "Wolf";

    public static String ACTION_WOLVES_1 = " has seized the castle!";
    public static String ACTION_WOLVES_2 = " calls a wolf";
    public static String ACTION_WOLVES_3 = "Call a wolf";
    public static String ACTION_WOLVES_4 = "Costs 1 action";
    public static String ACTION_WOLVES_5 = "Wolf";

    public static String ACTION_ZOMBIE_1 = " has seized the castle!";
    public static String ACTION_ZOMBIE_2 = " creates a zombie";
    public static String ACTION_ZOMBIE_3 = "Summon a zombie";
    public static String ACTION_ZOMBIE_4 = "Costs 1 actions";
    public static String ACTION_ZOMBIE_5 = "Zombie";

    public static String EVENT_AEGIS_1 = "At the end of turn, all allies gain a protection that cancels the next enemy attack, skill or spell directed at them. The enemy unit still pays all costs for attempting the action.";
    public static String EVENT_AEGIS_2 = "Cancel attacks, skills, spells";
    public static String EVENT_AEGIS_3 = "1/turn per ally";
    public static String EVENT_AEGIS_4 = "Aegis";

    public static String EVENT_BARRAGE_1 = "When the spotter attacks an enemy, this unit makes a free attack against the target at any range.";
    public static String EVENT_BARRAGE_2 = "Attack spotter's target";
    public static String EVENT_BARRAGE_3 = "Triggers on spotter attacks";
    public static String EVENT_BARRAGE_4 = "Barrage";

    public static String EVENT_BARRAGE_BLOCK_1 = "Block any attack made from a unit farther than 1 away. The enemy unit still pays all costs for attempting the attack.";
    public static String EVENT_BARRAGE_BLOCK_2 = "Block all enemy attacks";
    public static String EVENT_BARRAGE_BLOCK_3 = "Doesn't trigger at range 1";
    public static String EVENT_BARRAGE_BLOCK_4 = "Block";

    public static String EVENT_CLEAVE_1 = "Reduce an attacked enemy's armor to 0.";
    public static String EVENT_CLEAVE_2 = "Lower enemy armor to 0";
    public static String EVENT_CLEAVE_3 = "Triggers on attack";
    public static String EVENT_CLEAVE_4 = "Cleave";

    public static String EVENT_DEVOUR_1 = "Devour any attacked unit. Eating an enemy allows the Wyvern to lay an egg. Enemies are eaten before damage is dealt, circumventing abilities that trigger from damage.";
    public static String EVENT_DEVOUR_2 = "Devour attacked enemies";
    public static String EVENT_DEVOUR_3 = "Wyvern is fed and fertile";
    public static String EVENT_DEVOUR_4 = "Wyvern is hungry";
    public static String EVENT_DEVOUR_5 = "Replaces attack";
    public static String EVENT_DEVOUR_6 = "Devour";

    public static String EVENT_DODGE_1 = "Cancel the first attack or skill directed at this unit. The unit still pays all costs for attempting the action.";
    public static String EVENT_DODGE_2 = "Dodge next attack or skill";
    public static String EVENT_DODGE_3 = "/1 times per turn";
    public static String EVENT_DODGE_4 = "Dodge";

    public static String EVENT_DUELIST_1 = "An enemy that the duelist damages deals 0 damage when attacking the duelist.";
    public static String EVENT_DUELIST_2 = "Immune to damaged enemies";
    public static String EVENT_DUELIST_3 = "";
    public static String EVENT_DUELIST_4 = "Duelist";

    public static String EVENT_EFFICIENCY_1 = "When this unit kills the marked target, it regains all its action points.";
    public static String EVENT_EFFICIENCY_2 = "Refresh action points";
    public static String EVENT_EFFICIENCY_3 = "Triggers on killing mark";
    public static String EVENT_EFFICIENCY_4 = "Efficiency";
    public static String EVENT_EFFICIENCY_5 = "If an organic unit kills the marked target it regains all its action points.";
    public static String EVENT_EFFICIENCY_6 = "Killer of mark refreshes actions";

    public static String EVENT_GOAD_1 = "Cancel any enemy movement resulting in the enemy being farther away from this unit. That enemy still pays all costs for attempting to move.";
    public static String EVENT_GOAD_2 = "Cancel enemy movement";
    public static String EVENT_GOAD_3 = "Any range";
    public static String EVENT_GOAD_4 = "Triggers when enemies flee";
    public static String EVENT_GOAD_5 = "Goad";

    public static String EVENT_GUARDIAN_1 = "The first time an enemy within 4 range of this unit moves, that action is cancelled. The unit still pays all costs for attempting it.";
    public static String EVENT_GUARDIAN_2 = "Cancels Enemy Movement";
    public static String EVENT_GUARDIAN_3 = "4 range";
    public static String EVENT_GUARDIAN_4 = "/1 times per turn";
    public static String EVENT_GUARDIAN_5 = "Guardian";

    public static String EVENT_HEAL_1 = "The first time an ally is damaged and survives, that unit is healed.";
    public static String EVENT_HEAL_2 = "Heal damaged ally";
    public static String EVENT_HEAL_3 = "Triggered by enemy damage";
    public static String EVENT_HEAL_4 = "/1 times per turn";
    public static String EVENT_HEAL_5 = "Heal";

    public static String EVENT_HEAL_POTION_1 = "The next time this unit is damaged and survives, it drinks this potion and heals. The potion can also be used as an action.";
    public static String EVENT_HEAL_POTION_2 = "Heal self";
    public static String EVENT_HEAL_POTION_3 = "Consumed after use";
    public static String EVENT_HEAL_POTION_4 = "0 actions or trigger";
    public static String EVENT_HEAL_POTION_5 = " drinks a heal potion";
    public static String EVENT_HEAL_POTION_6 = "Heal Potion";

    public static String EVENT_INTERFERENCE_1 = "Cancel all enemy movement within 1 range. The enemy unit still pays all costs for the attempt.";
    public static String EVENT_INTERFERENCE_2 = "Cancels Enemy Movement";
    public static String EVENT_INTERFERENCE_3 = "1 range";
    public static String EVENT_INTERFERENCE_4 = "Interference";

    public static String EVENT_JOUST_1 = "The Lancer is a skilled jouster, trained to strike all foes it passes. When this unit moves, it attacks all enemies in range of each square it travels through.";
    public static String EVENT_JOUST_2 = "Attack all passed enemies";
    public static String EVENT_JOUST_3 = "Triggers on move";
    public static String EVENT_JOUST_4 = "Joust";

    public static String EVENT_LAMENT_1 = "The first time an ally dies, this unit performs a stirring lament that raises the life of your army by 1.";
    public static String EVENT_LAMENT_2 = "Raise army's life by 1";
    public static String EVENT_LAMENT_3 = "Triggers when an ally dies";
    public static String EVENT_LAMENT_4 = "/1 times per turn";
    public static String EVENT_LAMENT_5 = "Lament";

    public static String EVENT_HEALMOVE_1 = "If this unit has suffered any damage, it heals one of the damage each time it moves.";
    public static String EVENT_HEALMOVE_2 = "Heals when moving";
    public static String EVENT_HEALMOVE_3 = "Triggers on move";
    public static String EVENT_HEALMOVE_4 = "Blessing of Healing";

    public static String EVENT_SACRIFICE_1 = "The next time an ally within 2 range is killed by an enemy, the supplicant dies and the victim is reborn in its place.";
    public static String EVENT_SACRIFICE_2 = "Become slain ally";
    public static String EVENT_SACRIFICE_3 = "Triggers when an ally dies";
    public static String EVENT_SACRIFICE_4 = "Anywhere within 2";
    public static String EVENT_SACRIFICE_5 = "Sacrifice";

    public static String EVENT_OVERPOWER_1 = "Overpower the weak, killing them outright. Whenever this unit attacks an enemy with less than 4 power, the victim dies immediately. This occurs before damage is dealt, circumventing abilites that trigger from damage.";
    public static String EVENT_OVERPOWER_2 = "Kill enemy under 4 power";
    public static String EVENT_OVERPOWER_3 = "Replaces attack";
    public static String EVENT_OVERPOWER_4 = "Overpower";

    public static String EVENT_PARRY_1 = "Cancel the first attack directed at this unit. The unit still pays all costs for attempting the attack. If the attacker is an enemy and parried within range, a counterattack is performed at no cost.";
    public static String EVENT_PARRY_2 = "Parry next attack";
    public static String EVENT_PARRY_3 = "Attack parried enemy";
    public static String EVENT_PARRY_4 = " times per turn";
    public static String EVENT_PARRY_5 = "Parry";
    public static String EVENT_PARRY_6 = "Attack parried enemies within ";

    public static String EVENT_COUNTER_1 = "When an enemy attacks within range of this unit's attack this unit performs a counterattack with the same power of its standard attack. This can occur only twice per turn.";
    public static String EVENT_COUNTER_2 = "Counter 2 attacks within range";
    public static String EVENT_COUNTER_3 = "";
    public static String EVENT_COUNTER_4 = "";
    public static String EVENT_COUNTER_5 = "Counter";

    public static String EVENT_POISON_1 = "Poison attacked enemy. Poisoned units permanently lose 1 life at the end of their turn.";
    public static String EVENT_POISON_2 = "Poison attacked enemy";
    public static String EVENT_POISON_3 = "Poison";

    public static String EVENT_POSSESSED_1 = "When this unit kills another unit, the slain unit is reanimated under your control and stunned. If this triggers, this takes place instead of any other death effects.";
    public static String EVENT_POSSESSED_2 = "Gain control over slain units";
    public static String EVENT_POSSESSED_3 = "Victim is stunned";
    public static String EVENT_POSSESSED_4 = "Possessed";

    public static String EVENT_PROTECT_1 = "Redirect damage from the protected ally to the Shield Maiden, using her armor value. The protected unit is still considered to be attacked, and any attached affects still occur.";
    public static String EVENT_PROTECT_2 = "Receive unit's damage";
    public static String EVENT_PROTECT_3 = "Protecting: ";
    public static String EVENT_PROTECT_4 = "nobody";
    public static String EVENT_PROTECT_5 = "Protect";

    public static String EVENT_PUSH_1 = "Push an attacked enemy back 1 square. If the space behind the victim isn't available, they aren't pushed.";
    public static String EVENT_PUSH_2 = "Triggers on attack";
    public static String EVENT_PUSH_3 = "Push";
    public static String EVENT_PUSH_4 = "Push attacked enemy";

    public static String EVENT_RAGE_1 = "This unit gains +1 power and +2 life every time it attacks an enemy. If the unit doesn't attack by the end of its turn, it loses all the bonuses.";
    public static String EVENT_RAGE_2 = "Gain +1 power/+2 life";
    public static String EVENT_RAGE_3 = "Triggers on attacks";
    public static String EVENT_RAGE_4 = "Ends if unit doesn't attack";
    public static String EVENT_RAGE_5 = "Rage";

    public static String EVENT_RAMPAGE_1 = "Whenever this unit kills it gains +1 action until end of turn.";
    public static String EVENT_RAMPAGE_2 = "Gains +1 action for the turn";
    public static String EVENT_RAMPAGE_3 = "Triggers when this unit kills";
    public static String EVENT_RAMPAGE_4 = "Rampage";

    public static String EVENT_REAPER_1 = "Souls are used to power the Diabolist's spells.";
    public static String EVENT_REAPER_2 = "Souls: ";
    public static String EVENT_REAPER_3 = "+1 when a unit dies";
    public static String EVENT_REAPER_4 = "Reaper";

    public static String EVENT_REBIRTH_1 = "The first allied unit killed is reborn and sent back to its castle.";
    public static String EVENT_REBIRTH_2 = "Killed ally returns to castle";
    public static String EVENT_REBIRTH_3 = "/1 times per turn";
    public static String EVENT_REBIRTH_4 = "Rebirth";

    public static String EVENT_RETRIBUTION_1 = "When this unit is killed by an enemy, whichever unit performed the killing blow also dies.";
    public static String EVENT_RETRIBUTION_2 = "If killed, the killer also dies";
    public static String EVENT_RETRIBUTION_3 = "Retribution";

    public static String EVENT_SERVILE_1 = "Redirect damage from the master to the Sycophant, using the Sycophant's armor value. The protected unit is still considered to be attacked, and any attached affects still occur.";
    public static String EVENT_SERVILE_2 = "Receive master's damage";
    public static String EVENT_SERVILE_3 = "Servile";

    public static String EVENT_SKIRMISH_1 = "After this unit attacks it returns to the castle.";
    public static String EVENT_SKIRMISH_2 = "Return to castle";
    public static String EVENT_SKIRMISH_3 = "Triggers on attack";
    public static String EVENT_SKIRMISH_4 = "Skirmish";

    public static String EVENT_SLAY_1 = "Kill any attacked unit with 0 armor. This occurs before damage is dealt, circumventing abilities that trigger from damage.";
    public static String EVENT_SLAY_2 = "Kill unarmored enemies";
    public static String EVENT_SLAY_3 = "Replaces attack";
    public static String EVENT_SLAY_4 = "Slay";

    public static String EVENT_SPELL_BLOCK_1 = "This unit will evade one enemy attack, skill, or spell directed at it every turn.";
    public static String EVENT_SPELL_BLOCK_2 = "Evade attack, skill or spell";
    public static String EVENT_SPELL_BLOCK_3 = "/1 times per turn.";
    public static String EVENT_SPELL_BLOCK_4 = "Blessed Barrier";

    public static String EVENT_SPLENDOR_1 = "Prevent 1 damage from attacks against allied units.";
    public static String EVENT_SPLENDOR_2 = "Enemy damage reduced by 1";
    public static String EVENT_SPLENDOR_3 = "Splendor";

    public static String EVENT_STING_1 = "Whenever an enemy enters a square that's within a range of 2, this unit performs an attack against it and poisons the target.";
    public static String EVENT_STING_2 = "Poisons approaching enemies";
    public static String EVENT_STING_3 = "Costs no actions.";
    public static String EVENT_STING_4 = "Sting Attack";

    public static String EVENT_STRATAGEM_1 = "Whenever an enemy unit attacks one of your units, gain +1 command at the start of your next turn.";
    public static String EVENT_STRATAGEM_2 = "Gain +1 command next turn";
    public static String EVENT_STRATAGEM_3 = "Triggers when enemies attack";
    public static String EVENT_STRATAGEM_4 = "Extra commands gained: ";
    public static String EVENT_STRATAGEM_5 = "Stratagem";

    public static String EVENT_STRENGTH_1 = "Destroy next attacked enemy. This occurs before damage is dealt, circumventing abilities that trigger from damage. This effect is consumed after use.";
    public static String EVENT_STRENGTH_2 = "Destroy attacked enemy";
    public static String EVENT_STRENGTH_3 = "Triggers on next attack";
    public static String EVENT_STRENGTH_4 = "Strength Potion";

    public static String EVENT_STUN_1 = "Stun attacked enemy. Lasts 1 turn.";
    public static String EVENT_STUN_2 = "Stun attacked enemy";
    public static String EVENT_STUN_3 = "Lasts 1 turn";
    public static String EVENT_STUN_4 = "Stun";
    public static String EVENT_STUN_5 = "Has effect within range ";

    public static String EVENT_SUCCOR_1 = "This unit heals itself whenever it attacks an enemy.";
    public static String EVENT_SUCCOR_2 = "Heal self";
    public static String EVENT_SUCCOR_3 = "Triggers on attack";
    public static String EVENT_SUCCOR_4 = "Succor";

    public static String EVENT_SUMMONER_1 = "This unit has the ability to summon other units, but can only have so many summoned at the same time. If this unit dies, so does everything it has summoned.";
    public static String EVENT_SUMMONER_2 = "Available Summons: ";
    public static String EVENT_SUMMONER_3 = "Summons die when this does.";
    public static String EVENT_SUMMONER_4 = "Summoner";

    public static String EVENT_TOMB_LORD_1 = "Whenever an enemy dies it rises from the dead under your control.";
    public static String EVENT_TOMB_LORD_2 = "Risen ";
    public static String EVENT_TOMB_LORD_3 = "Enemy resurrects on your side";
    public static String EVENT_TOMB_LORD_4 = "Triggers when an enemy dies";
    public static String EVENT_TOMB_LORD_5 = "Tomb Lord";

    public static String EVENT_TOXIDERMIS_1 = "When this unit is struck by an enemy's melee attack, it poisons the attacker.";
    public static String EVENT_TOXIDERMIS_2 = "Poisons attackers";
    public static String EVENT_TOXIDERMIS_3 = "Triggers on melee attack";
    public static String EVENT_TOXIDERMIS_4 = "Toxidermis";

    public static String EVENT_TRAMPLE_1 = "When this unit passes over an enemy during a move, that enemy is attacked for 6 damage.";
    public static String EVENT_TRAMPLE_2 = " is trampled for ";
    public static String EVENT_TRAMPLE_3 = "Tramples enemies for ";
    public static String EVENT_TRAMPLE_4 = "Triggers on crossed enemies";
    public static String EVENT_TRAMPLE_5 = "Trample";

    public static String EVENT_POUNCE_1 = "When this unit passes over an enemy during a move, that enemy is attacked.";
    public static String EVENT_POUNCE_2 = " is pounced for ";
    public static String EVENT_POUNCE_3 = "Attacks crossed enemies";
    public static String EVENT_POUNCE_4 = "";
    public static String EVENT_POUNCE_5 = "Pounce";

    public static String EVENT_VAMPIRE_1 = "Whenever this unit deals damage, its life is raised by an equal amount.";
    public static String EVENT_VAMPIRE_2 = "Gain inflicted damage as life";
    public static String EVENT_VAMPIRE_3 = "Vampire";

    public static String EVENT_VOLATILE_NATURE_1 = "The will-o-the-wisp's very nature is unstable. It explodes upon death, dealing 4 damage to every unit within one square.";
    public static String EVENT_VOLATILE_NATURE_2 = "Volatile Nature";
    public static String EVENT_VOLATILE_NATURE_3 = "Nearby units take 4 damage";
    public static String EVENT_VOLATILE_NATURE_4 = "Triggers upon death";

    public static String EVENT_MINI_VAMPIRE_1 = "Whenever this unit deals damage, its life is raised by one.";
    public static String EVENT_MINI_VAMPIRE_2 = "Gains one life on attack.";
    public static String EVENT_MINI_VAMPIRE_3 = "Lesser Vampire";

    public static String EVENT_VIGILANT_1 = "Whenever an enemy enters a square that's within attack range, this unit performs an attack against it.";
    public static String EVENT_VIGILANT_2 = "Attacks approaching enemies";
    public static String EVENT_VIGILANT_3 = "Triggers default attack";
    public static String EVENT_VIGILANT_4 = "Vigilant";

    public static String EVENT_WHIRLWIND_1 = "Whenever this unit performs any attack, it also attacks all enemies within range. The first attack must succeed for the other attacks to occur.";
    public static String EVENT_WHIRLWIND_2 = "Attack all enemies in range";
    public static String EVENT_WHIRLWIND_3 = "Triggers on every attack";
    public static String EVENT_WHIRLWIND_4 = "Whirlwind";

    public static String EVENT_COORDINATE_1 = "Whenever this unit performs any attack all allies that are able to will also attack the target. The additional attacks cost no commands, but all other costs are still deducted. Units from an allied player are unaffected.";
    public static String EVENT_COORDINATE_2 = "All allies attack target";
    public static String EVENT_COORDINATE_3 = "Triggers on every attack";
    public static String EVENT_COORDINATE_4 = "Coordinate";

    public static String EVENT_ZEAL_1 = "This unit trades places with any enemy it attacks.";
    public static String EVENT_ZEAL_2 = "Trade places with enemy";
    public static String EVENT_ZEAL_3 = "Triggers on attacked enemy";
    public static String EVENT_ZEAL_4 = "Zeal";

    public static String RELIC_POWERUP_1 = "Triggered by moving onto this";
    public static String RELIC_POWERUP_2 = "Decay";
    public static String RELIC_POWERUP_3 = "Loses 1 life per turn";
    public static String RELIC_POWERUP_4 = "This power up loses 1 life each turn until it dies. Triggers at the end of owner's turn. Owner is determined by who killed the unit that dropped this.";

    public static String RELIC_BASE_1 = "Relic Blessing";
    public static String RELIC_BASE_2 = "Only deploys on allied units.";
    public static String RELIC_BASE_3 = "Relic consumed after use.";
    public static String RELIC_BASE_4 = "Instead of deploying from the castle, the relic is deployed onto an allied unit, granting it the effects of the relic. The relic disappears after use.";

    public static String RELIC_BANISH_1 = "Banishing Relic";
    public static String RELIC_BANISH_2 = "Banish";
    public static String RELIC_BANISH_3 = "Ally can banish itself";
    public static String RELIC_BANISH_4 = "Can be done once";
    public static String RELIC_BANISH_5 = "Targeted ally gains the ability to return to their castle as an undeployed unit. Can be done once for no actions or commands.";

    public static String RELIC_CLOCKWORK_1 = "Clockwork Relic";
    public static String RELIC_CLOCKWORK_2 = "Clockwork Blessing";
    public static String RELIC_CLOCKWORK_3 = "Makes target ally inorganic.";
    public static String RELIC_CLOCKWORK_4 = "";
    public static String RELIC_CLOCKWORK_5 = "Transforms the targeted ally into a clockwork version of itself. It gains the inorganic property and becomes immune to spells and skills.";

    public static String RELIC_EVASIVE_1 = "Evasive Relic";
    public static String RELIC_EVASIVE_2 = "Blessing of Evasion";
    public static String RELIC_EVASIVE_3 = "Ally can dodge one attack or skill per turn.";
    public static String RELIC_EVASIVE_4 = "";
    public static String RELIC_EVASIVE_5 = "Blesses the target with evasion, allowing it to dodge one attack or skill per turn. The enemy still pays all costs for the action.";

    public static String RELIC_FLIGHT_1 = "Flying Relic";
    public static String RELIC_FLIGHT_2 = "Blessing of Flight";
    public static String RELIC_FLIGHT_3 = "Ally gains the ability to fly.";
    public static String RELIC_FLIGHT_4 = "Stuns unit on deploy";
    public static String RELIC_FLIGHT_5 = "Target ally gains the ability to fly. Units with 2 or more range can fly 2 squares, and melee units can fly 3 squares. Flying doesn't cost a unit action points. Using this relic stuns the target.";

    public static String RELIC_VAMPIRE_1 = "Vampiric Relic";
    public static String RELIC_VAMPIRE_2 = "Vampiric Blessing";
    public static String RELIC_VAMPIRE_3 = "Raises life by 1 on attack";
    public static String RELIC_VAMPIRE_4 = "";
    public static String RELIC_VAMPIRE_5 = "Transforms the targeted ally into a vampiric version of itself. It heals one life and increases its maximum life by one when attacking.";

    public static String RELIC_STUN_1 = "Stun Relic";
    public static String RELIC_STUN_2 = "Blessing of Impact";
    public static String RELIC_STUN_3 = "Ally can stun the enemy on attack";
    public static String RELIC_STUN_4 = "Only has effect within range of 2";
    public static String RELIC_STUN_5 = "Allows the target ally to stun enemies they attack.";

    public static String RELIC_HEAL_MOVE_1 = "Vitality Relic";
    public static String RELIC_HEAL_MOVE_2 = "Blessing of Health";
    public static String RELIC_HEAL_MOVE_3 = "Ally heals one damage upon moving";
    public static String RELIC_HEAL_MOVE_4 = "Increases max health by 2";
    public static String RELIC_HEAL_MOVE_5 = "Target ally's max health will increase by 2 and will be able to heal one damage upon movement, provided they have been damaged.";

    public static String RELIC_GIFT_UNIT_1 = "Command Relic";
    public static String RELIC_GIFT_UNIT_2 = "Gift Ally";
    public static String RELIC_GIFT_UNIT_3 = "Ally is put under partner's command";
    public static String RELIC_GIFT_UNIT_4 = "Stuns unit";
    public static String RELIC_GIFT_UNIT_6 = "Multi-Use";
    public static String RELIC_GIFT_UNIT_7 = "Relic returns to castle after use";
    public static String RELIC_GIFT_UNIT_8 = "";
    public static String RELIC_GIFT_UNIT_9 = "This relic returns to the castle after being used and can be used multiple times.";
    public static String RELIC_GIFT_UNIT_5 = "Target ally will be put under your partner's command. Ally is without a commander for one turn.";

    public static String RELIC_RESET_1 = "Reset Relic";
    public static String RELIC_RESET_2 = "Rejuvenation";
    public static String RELIC_RESET_3 = "Ally can reset its stats";
    public static String RELIC_RESET_4 = "Can be done once, stuns on use";
    public static String RELIC_RESET_5 = "The target ally gains the ability to reset its own stats. This heals the unit and removes all harmful and beneficial effects from it. Can be done once for no actions or commands. Stuns the unit on use.";

    public static String RELIC_SPELL_BLOCK_1 = "Aegis Relic";
    public static String RELIC_SPELL_BLOCK_2 = "Blessed Barrier";
    public static String RELIC_SPELL_BLOCK_3 = "Ally evades attacks, skills and spells";
    public static String RELIC_SPELL_BLOCK_4 = "Once per turn";
    public static String RELIC_SPELL_BLOCK_5 = "The target ally becomes immune to the first enemy attack, skill or spell directed at it cast on it each turn. The enemy still pays all costs for the action.";

    public static String RELIC_PARRY_1 = "Parry Relic";
    public static String RELIC_PARRY_2 = "Blessed Counter";
    public static String RELIC_PARRY_3 = "Ally cancels the first attack toward them";
    public static String RELIC_PARRY_4 = "Attack parried enemies within range 1";
    public static String RELIC_PARRY_5 = "Cancel the first attack directed at this unit. The unit still pays all costs for attempting the attack. If the attacker is an enemy and parried within melee range, a counterattack is performed at no cost.";

    public static String RELIC_EXPLODE_1 = "Explode Relic";
    public static String RELIC_EXPLODE_2 = "Detonate";
    public static String RELIC_EXPLODE_3 = "Ally can explode";
    public static String RELIC_EXPLODE_4 = "Can be done once";
    public static String RELIC_EXPLODE_5 = "The target ally gains the ability to explode, causing 4 damage to surrounding units. Can be done once for no actions or commands.";
    //public static String RELIC_EXPLODE_5 = "The target ally decides to explode and take as many units with them as possible. They had the bombs strapped to them the whole time and chose right now to use them.";

    public static String ACTION_RELIC = "Costs no actions/commands";

    public static String ACTION_RELIC_EXPLODE_1 = "This unit explodes and deals 4 damage to all units within 1 square. Can only be done once. Kills this unit upon use.";
    public static String ACTION_RELIC_EXPLODE_2 = "Explode for 4 damage";
    public static String ACTION_RELIC_EXPLODE_3 = "Damages surrounding units";
    public static String ACTION_RELIC_EXPLODE_4 = "Explode";

    public static String ACTION_RELIC_RESET_1 = "This unit reverts back to its original state, removing all status changes and damage. Can only be done once.";
    public static String ACTION_RELIC_RESET_2 = "Reset unit's stats";
    public static String ACTION_RELIC_RESET_3 = "Reset";

    public static String ACTION_RELIC_BANISH_1 = "This unit is sent back to the castle. Can only be done once.";
    public static String ACTION_RELIC_BANISH_2 = "Return unit to the castle";
    public static String ACTION_RELIC_BANISH_3 = "Banish";

    public static String UNIT_ABBEY_1 = "Abbey";
    public static String UNIT_ABBEY_2 = "Monastery";
    public static String UNIT_ABBEY_3 = "Deploy on adjacent squares";
    public static String UNIT_ABBEY_4 = "Affects Clergy, Cultists";
    public static String UNIT_ABBEY_5 = "Clergy and Cultist units can be deployed on the squares surrounding this unit.";


    public static String UNIT_ABJURER_1 = "Abjurer";

    public static String UNIT_ACOLYTE_1 = "Acolyte";

    public static String UNIT_ALCHEMIST_1 = "Alchemist";

    public static String UNIT_ARCHANGEL_1 = "Archangel";
    public static String UNIT_ARCHANGEL_2 = "Immortal";
    public static String UNIT_ARCHANGEL_3 = "Returns to castle when killed";
    public static String UNIT_ARCHANGEL_4 = "When this unit dies, it returns to its castle undeployed.";

    public static String UNIT_MILITIA_1 = "Militia";
    public static String UNIT_MILITIA_2 = "Proletariat";
    public static String UNIT_MILITIA_3 = "Returns to castle when killed";
    public static String UNIT_MILITIA_4 = "Activates when outnumbered";
    public static String UNIT_MILITIA_5 = "When this unit dies, it returns to its castle undeployed as long as your enemy has more deployed units than you, not including this unit.";
    public static String UNIT_MILITIA_6 = "Reinforce";
    public static String UNIT_MILITIA_7 = "Can be deployed next to";
    public static String UNIT_MILITIA_8 = "non-militia allied units";
    public static String UNIT_MILITIA_9 = "This unit can be deployed onto spaces surrounding non-militia, allied units. Can still be deployed onto spaces surrounding your castle.";

    public static String UNIT_ARCHER_1 = "Archer";

    public static String UNIT_ARMORY_1 = "Armory";
    public static String UNIT_ARMORY_2 = "Arsenal";
    public static String UNIT_ARMORY_3 = "+1 power for all allies";
    public static String UNIT_ARMORY_4 = "+1 armor for all allies";
    public static String UNIT_ARMORY_5 = "Maximum 2 armor";
    public static String UNIT_ARMORY_6 = "All allied units gain +1 power and +1 armor. Units may never have more than 2 armor.";

    public static String UNIT_ARTIFICER_1 = "Artificer";

    public static String UNIT_ASSASSIN_1 = "Assassin";
    public static String UNIT_ASSASSIN_2 = "Disguise";
    public static String UNIT_ASSASSIN_3 = "Only deploys on allies";
    public static String UNIT_ASSASSIN_4 = "Returns that ally to castle";
    public static String UNIT_ASSASSIN_5 = "Instead of deploying from the castle, an Assassin picks an allied unit to deploy onto. That unit is sent back to the castle, and the Assassin appears in its place.";
    public static String UNIT_ASSASSIN_6 = "Smokebomb";
    public static String UNIT_ASSASSIN_7 = "Stuns all surrounding units";
    public static String UNIT_ASSASSIN_8 = "Triggers when deployed";
    public static String UNIT_ASSASSIN_9 = "When the Assassin is deployed, all units within 1 square of it are stunned. This affects allies and enemies.";

    public static String UNIT_AXEMAN_1 = "Axeman";

    public static String UNIT_BALLISTA_1 = "Ballista";

    public static String UNIT_BARBARIAN_1 = "Barbarian";

    public static String UNIT_BARRACKS_1 = "Barracks";
    public static String UNIT_BARRACKS_2 = "Forward Deployment";
    public static String UNIT_BARRACKS_3 = "Deploy on adjacent squares";
    public static String UNIT_BARRACKS_4 = "Affects Commanders, Soldiers";
    public static String UNIT_BARRACKS_5 = "Commander and Soldier units can be deployed on the squares surrounding this unit.";
    public static String UNIT_BARRACKS_6 = "Supply Lines";
    public static String UNIT_BARRACKS_7 = "This unit has the ability to deploy soldiers, but can only have so many deployed at the same time. If this unit dies, so do the soldiers that it deployed.";
    public static String UNIT_BARRACKS_8 = "Available Soldiers: ";

    public static String UNIT_BEAR_1 = "Bear";

    public static String UNIT_BERSERKER_1 = "Berserker";
    public static String UNIT_BERSERKER_2 = "Relentless";
    public static String UNIT_BERSERKER_3 = "Only dies at end of turn";
    public static String UNIT_BERSERKER_4 = "When the Berserker is killed, it instead becomes immune to damage and dies at the end of its turn.";
    public static String UNIT_BERSERKER_5 = "Will die at end of turn";
    public static String UNIT_BERSERKER_6 = "Can't capture the castle";

    public static String UNIT_BOUNTY_HUNTER_1 = "Bounty Hunter";
    public static String UNIT_BOUNTY_HUNTER_2 = "Relentless";
    public static String UNIT_BOUNTY_HUNTER_3 = "Immune to marked target";
    public static String UNIT_BOUNTY_HUNTER_4 = "This unit gains complete immunity to anything the marked unit does.";

    public static String UNIT_BOWMAN_1 = "Bowman";
    public static String UNIT_BOWMAN_2 = "Vaulted Shots";
    public static String UNIT_BOWMAN_3 = "Can shoot over other units";
    public static String UNIT_BOWMAN_4 = "This unit's ranged attacks aren't blocked by other units, allowing it to select any target within its full range.";

    public static String UNIT_CATAPULT_1 = "Catapult";
    public static String UNIT_CATAPULT_2 = "Vaulted Shot";
    public static String UNIT_CATAPULT_3 = "Can shoot over other units";
    public static String UNIT_CATAPULT_4 = "This unit's ranged attacks aren't blocked by other units, allowing it to select any target within its full range.";

    public static String UNIT_CALVARY_1 = "Cavalry";

    public static String UNIT_CHANGELING_1 = "Changeling";
    public static String UNIT_CHANGELING_2 = "Indestructible";
    public static String UNIT_CHANGELING_3 = "Indestructible";
    public static String UNIT_CHANGELING_4 = "Immune to everything";
    public static String UNIT_CHANGELING_5 = "Fey";
    public static String UNIT_CHANGELING_6 = "Indestructible after trade";
    public static String UNIT_CHANGELING_7 = "Lasts until start of turn";
    public static String UNIT_CHANGELING_8 = "When a Changeling chooses a target, they warp time to rewrite history, replacing their victim in every way. Manifesting these primordial forces renders them briefly indestructible.";

    public static String UNIT_CHANNELER_1 = "Channeler";

    public static String UNIT_CHIEFTAIN_1 = "Chieftain";

    public static String UNIT_COMMAND_POST_1 = "Command Post";
    public static String UNIT_COMMAND_POST_2 = "Command Center";
    public static String UNIT_COMMAND_POST_3 = "Add 1 command";
    public static String UNIT_COMMAND_POST_4 = "Deploy units for 1 less";
    public static String UNIT_COMMAND_POST_5 = "While this unit is under your control, you have +1 command per turn, and your costs to deploy new units are reduced by 1.";

    public static String UNIT_CONFESSOR_1 = "Confessor";
    public static String UNIT_CONFESSOR_2 = "Penance";
    public static String UNIT_CONFESSOR_3 = "Murderers are always in range";
    public static String UNIT_CONFESSOR_4 = "Any enemy that has killed another unit is always in range of the Confessor, regardless of how far away they are.";

    public static String UNIT_CONJURER_1 = "Conjurer";

    public static String UNIT_CONSPIRATOR_1 = "Conspirator";

    public static String UNIT_CROSSBOWMAN_1 = "Crossbowman";

    public static String UNIT_DEMON_1 = "Demon";

    public static String UNIT_ARCH_DEMON_1 = "Archdemon";

    public static String UNIT_DIABOLIST_1 = "Diabolist";

    public static String UNIT_DIPLOMAT_1 = "Diplomat";
    public static String UNIT_DIPLOMAT_2 = "Diplomatic Immunity";
    public static String UNIT_DIPLOMAT_3 = "Indestructible";
    public static String UNIT_DIPLOMAT_4 = "Immune to enemies";
    public static String UNIT_DIPLOMAT_5 = "This unit has diplomatic immunity. If this unit performs any action aside from moving away from the enemy castle, the immunity ends.";

    public static String UNIT_DISMOUNTED_KNIGHT_1 = "Dismounted Knight";

    public static String UNIT_DOPPELGANGER_1 = "Doppelganger";
    public static String UNIT_DOPPELGANGER_2 = "Shared Soul";
    public static String UNIT_DOPPELGANGER_3 = "Dies if control is lost";
    public static String UNIT_DOPPELGANGER_4 = "Any affect that would cause you to lose control over this unit immediately causes it to vanish, instead.";

    public static String UNIT_DRACOLICH_1 = "Dracolich";
    public static String UNIT_DRACOLICH_2 = "Despair";
    public static String UNIT_DRACOLICH_3 = "Enemy has -1 commands";
    public static String UNIT_DRACOLICH_4 = "While this unit is in play, the enemy has 1 less command per turn.";

    public static String UNIT_DRAGON_1 = "Dragon";

    public static String UNIT_DRUID_1 = "Druid";

    public static String UNIT_EGG_1 = "Wyvern hatches in ";
    public static String UNIT_EGG_2 = "Egg";
    public static String UNIT_EGG_3 = "Hatch";
    public static String UNIT_EGG_4 = " turns";

    public static String UNIT_ENCHANTER_1 = "Enchanter";

    public static String UNIT_FANATIC_1 = "Fanatic";

    public static String UNIT_FEATHERED_SERPENT_1 = "Feathered Serpent";
    public static String UNIT_FEATHERED_SERPENT_2 = "Benevolence";
    public static String UNIT_FEATHERED_SERPENT_3 = "Heals all friendly units";
    public static String UNIT_FEATHERED_SERPENT_4 = "Triggers at end of turn";
    public static String UNIT_FEATHERED_SERPENT_5 = "At the end of your turn, all allied units are healed.";

    public static String UNIT_FIRE_ARCHER_1 = "Fire Archer";

    public static String UNIT_FOOTMAN_1 = "Footman";

    public static String UNIT_GATE_1 = "Gate";
    public static String UNIT_GATE_2 = "Gateway";
    public static String UNIT_GATE_3 = "Deploy units here";
    public static String UNIT_GATE_4 = "Destroys this unit";
    public static String UNIT_GATE_5 = "New units can be deployed at this unit's location. Doing this will destroy the gate.";

    public static String UNIT_GATE_GUARD_1 = "Gate Guard";
    public static String UNIT_GATE_GUARD_2 = "Sentry";
    public static String UNIT_GATE_GUARD_3 = "Can move into castle's square";
    public static String UNIT_GATE_GUARD_4 = "This unit can enter your castle's square, preventing the enemy from winning until it's gone.";
    public static String UNIT_GATE_GUARD_5 = "Cover";
    public static String UNIT_GATE_GUARD_6 = "Immune to enemy spells/skills";
    public static String UNIT_GATE_GUARD_7 = "+1 armor";
    public static String UNIT_GATE_GUARD_8 = "Activates while on castle";
    public static String UNIT_GATE_GUARD_9 = "This unit can use your castle's defenses as cover, gaining +1 armor and immunity to enemy spells and skills.";

    public static String UNIT_GENERAL_1 = "General";
    public static String UNIT_GENERAL_2 = "Commanding";
    public static String UNIT_GENERAL_3 = "Add 1 command";
    public static String UNIT_GENERAL_4 = "Deploy units for 1 less";
    public static String UNIT_GENERAL_5 = "While this unit is under your control, you gain +1 commands per turn, and your costs to deploy new units are reduced by 1.";

    public static String UNIT_GEOMANCER_1 = "Geomancer";

    public static String UNIT_GHOST_1 = "Ghost";
    public static String UNIT_GHOST_2 = "No Victory";
    public static String UNIT_GHOST_3 = "Can't capture the castle";
    public static String UNIT_GHOST_4 = "Upon killing an enemy organic unit, this unit will become a possessed once again.";
    public static String UNIT_GHOST_5 = "Returns from the dead";
    public static String UNIT_GHOST_6 = "Triggers on killing";
    public static String UNIT_GHOST_7 = "This unit can't be used to capture the enemy's castle.";
    public static String UNIT_GHOST_8 = "Undying";

    public static String UNIT_GOLEM_1 = "Golem";

    public static String UNIT_HEALER_1 = "Healer";

    public static String UNIT_HERETIC_1 = "Heretic";
    public static String UNIT_HERETIC_2 = "Reckoning";
    public static String UNIT_HERETIC_3 = "Lower enemy army's life by 1";
    public static String UNIT_HERETIC_4 = "Triggered on death.";
    public static String UNIT_HERETIC_5 = "When this unit is killed by an enemy, the life of all enemy units is lowered by 1.";

    public static String UNIT_HYDRA_1 = "Hydra";
    public static String UNIT_HYDRA_2 = "Regeneration";
    public static String UNIT_HYDRA_3 = "Regenerates heads";
    public static String UNIT_HYDRA_4 = "Triggers at end of turn";
    public static String UNIT_HYDRA_5 = "Regenerates one head at the end of each round.";
    public static String UNIT_HYDRA_6 = "Many Heads";
    public static String UNIT_HYDRA_7 = "Loses a head instead of dying";
    public static String UNIT_HYDRA_8 = "The Hydra starts with 6 heads, and loses 1 each time it dies until it has no remaining heads. When the hydra dies, it is stunned until the end of the round. The Hydra's attacks don't cost it actions to use. It can attack once for each remaining head.";

    public static String UNIT_IMP_1 = "Imp";

    public static String UNIT_KNIGHT_1 = "Knight";
    public static String UNIT_KNIGHT_2 = "Dismount";
    public static String UNIT_KNIGHT_3 = "Becomes a Dismounted Knight";
    public static String UNIT_KNIGHT_4 = "Triggers when this unit dies";

    public static String UNIT_LANCER_1 = "Lancer";

    public static String UNIT_LICH_1 = "Lich";

    public static String UNIT_LONGBOWMAN_1 = "Longbowman";

    public static String UNIT_LYCANTHROPE_1 = "Lycanthrope";

    public static String UNIT_LYCAN_WOLF_1 = "Wolf";

    public static String UNIT_MAGUS_1 = "Magus";

    public static String UNIT_MARTYR_1 = "Martyr";

    public static String UNIT_MASON_1 = "Mason";

    public static String UNIT_MIMIC_1 = "Mimic";

    public static String UNIT_MOUNTED_ARCHER_1 = "Mounted Archer";

    public static String UNIT_MOURNER_1 = "Mourner";

    public static String UNIT_NECROMANCER_1 = "Necromancer";

    public static String UNIT_NONE_1 = "Error";
    public static String UNIT_NONE_2 = "None shall enter";

    public static String UNIT_PALADIN_1 = "Paladin";
    public static String UNIT_PALADIN_2 = "Sanctuary";
    public static String UNIT_PALADIN_3 = "Damage is reduced to 1";
    public static String UNIT_PALADIN_4 = "All damage inflicted on the Paladin is reduced to 1.";
    public static String UNIT_PALADIN_5 = "Grace";
    public static String UNIT_PALADIN_6 = "Raise army's life by 1";
    public static String UNIT_PALADIN_7 = "Triggers when this unit dies";
    public static String UNIT_PALADIN_8 = "When this unit dies, allies who witness its noble sacrifice are inspired, raising their life by 1.";

    public static String UNIT_PIKEMAN_1 = "Pikeman";
    public static String UNIT_PIKEMAN_2 = "Reach";
    public static String UNIT_PIKEMAN_3 = "Can attack over other units";
    public static String UNIT_PIKEMAN_4 = "The Pikeman's training with long weapons allows him to strike past other units.";

    public static String UNIT_PORTAL_1 = "Portal";
    public static String UNIT_PORTAL_2 = "Warded";
    public static String UNIT_PORTAL_3 = "Summon killer";
    public static String UNIT_PORTAL_4 = "Triggers when destroyed";
    public static String UNIT_PORTAL_5 = "If this unit is destroyed, the summon ability triggers on its killer.";

    public static String UNIT_POSSESSED_1 = "Possessed";
    public static String UNIT_POSSESSED_2 = "Host Body";
    public static String UNIT_POSSESSED_3 = "Spawns a ghost";
    public static String UNIT_POSSESSED_4 = "Triggers upon death";
    public static String UNIT_POSSESSED_5 = "When this unit dies, it spawns the ghost that possesses its body.";

    public static String UNIT_PRIEST_1 = "Priest";

    public static String UNIT_QUARTERMASTER_1 = "Quartermaster";
    public static String UNIT_QUARTERMASTER_2 = "Provisions";
    public static String UNIT_QUARTERMASTER_3 = "Heal allies that don't act";
    public static String UNIT_QUARTERMASTER_4 = "Triggers at end of turn";
    public static String UNIT_QUARTERMASTER_5 = "The Quartermaster keeps your army well provided, allowing allies the opportunity to eat and bandage their wounds when they rest. Any ally that performed no actions heals at the end their turn.";
    public static String UNIT_QUARTERMASTER_6 = "Logistics";
    public static String UNIT_QUARTERMASTER_7 = "Deploy units for 1 less";
    public static String UNIT_QUARTERMASTER_8 = "While this unit is under your control your costs to deploy new units are reduced by 1.";

    public static String UNIT_RANGER_1 = "Ranger";
    public static String UNIT_RANGER_2 = "Self reliant";
    public static String UNIT_RANGER_3 = "Costs no commands to use";
    public static String UNIT_RANGER_4 = "The Ranger is accustomed to operating alone and acting independently. When the Ranger acts, no commands are deducted.";

    public static String UNIT_RIDER_1 = "Rider";

    public static String UNIT_ROCK_1 = "Rock";
    public static String UNIT_ROCK_2 = "Captive";
    public static String UNIT_ROCK_3 = "Captive: ";
    public static String UNIT_ROCK_4 = "Destroy to free captive";
    public static String UNIT_ROCK_5 = "This is an empty stone without a captive.";

    public static String UNIT_ROGUE_1 = "Rogue";

    public static String UNIT_SCOUT_1 = "Scout";

    public static String UNIT_SEAL_1 = "Sealed gate";
    public static String UNIT_SEAL_2 = "Indestructible";
    public static String UNIT_SEAL_3 = "Immune to everything";
    public static String UNIT_SEAL_4 = "The Seal is indestructible and immune to everything. It will vanish when the spell expires.";
    public static String UNIT_SEAL_5 = "Temporary";
    public static String UNIT_SEAL_6 = "Vanishes after 2 rounds";
    public static String UNIT_SEAL_7 = "The Seal vanishes after 2 turns.";
    public static String UNIT_SEAL_8 = "Vanishes after 1 round";

    public static String UNIT_SERGEANT_1 = "Sergeant";
    public static String UNIT_SERGEANT_2 = "Rallied";
    public static String UNIT_SERGEANT_3 = "Costs no commands to use";
    public static String UNIT_SERGEANT_4 = "The unit has been rallied. When this unit acts, no commands are deducted.";

    public static String UNIT_SERPENT_1 = "Serpent";

    public static String UNIT_SHAMAN_1 = "Shaman";

    public static String UNIT_SHIELD_BEARER_1 = "Shield Bearer";

    public static String UNIT_SHIELD_MAIDEN_1 = "Shield Maiden";

    public static String UNIT_SKELETON_1 = "Skeleton";

    public static String UNIT_SKINWALKER_1 = "Skinwalker";

    public static String UNIT_SOLDIER_1 = "Soldier";

    public static String UNIT_SPIRIT_1 = "Spirit";
    public static String UNIT_SPIRIT_2 = "Immune to everything";
    public static String UNIT_SPIRIT_3 = "Intangible";
    public static String UNIT_SPIRIT_4 = "Can't capture the castle";
    public static String UNIT_SPIRIT_5 = "This unit can't be used to capture the enemy's castle.";
    public static String UNIT_SPIRIT_6 = "This unit is indestructible and immune to everything.";
    public static String UNIT_SPIRIT_7 = "Indestructible";

    public static String UNIT_STRATEGIST_1 = "Strategist";

    public static String UNIT_SUMMONER_1 = "Summoner";

    public static String UNIT_SUPPLICANT_1 = "Supplicant";

    public static String UNIT_SWORDSMAN_1 = "Swordsman";

    public static String UNIT_SYCOPHANT_1 = "Sycophant";
    public static String UNIT_SYCOPHANT_2 = "Immune to everything";
    public static String UNIT_SYCOPHANT_3 = "This unit is indestructible and immune to everything.";
    public static String UNIT_SYCOPHANT_4 = "Hubris";
    public static String UNIT_SYCOPHANT_5 = "Immune to allies";
    public static String UNIT_SYCOPHANT_6 = "This unit is consumed with arrogance, refusing all help. This renders the unit immune to all allies.";
    public static String UNIT_SYCOPHANT_7 = "Codependent";
    public static String UNIT_SYCOPHANT_8 = "When this leaves, master dies";
    public static String UNIT_SYCOPHANT_9 = "If this unit leaves play or you lose control over it, the Sycophant dies and kills its master.";

    public static String UNIT_TACTICIAN_1 = "Tactician";
    public static String UNIT_TACTICIAN_2 = "Tactics";
    public static String UNIT_TACTICIAN_3 = "Add 1 command";
    public static String UNIT_TACTICIAN_4 = "While this unit is under your control, you gain +1 commands per turn.";
    public static String UNIT_TACTICIAN_5 = "Contingency";
    public static String UNIT_TACTICIAN_6 = "Gain 3 commands";
    public static String UNIT_TACTICIAN_7 = "Triggers when this dies";
    public static String UNIT_TACTICIAN_8 = "When this unit dies, you gain +3 commands until the end of your turn.";

    public static String UNIT_TEMPLAR_1 = "Templar";
    public static String UNIT_TEMPLAR_2 = "Holy Shield";
    public static String UNIT_TEMPLAR_3 = "Indestructible, immobile";
    public static String UNIT_TEMPLAR_4 = "Immune to everything";
    public static String UNIT_TEMPLAR_5 = "This unit is indestructible and immune to everything.";

    public static String UNIT_TOAD_1 = "Toad";

    public static String UNIT_TOWER_1 = "Tower";

    public static String UNIT_WALL_1 = "Wall";

    public static String UNIT_WALL_MASON_1 = "Mason";

    public static String UNIT_WAR_ELEPHANT_1 = "War Elephant";

    public static String UNIT_WARLOCK_1 = "Warlock";

    public static String UNIT_WARRIOR_1 = "Warrior";

    public static String UNIT_WEREWOLF_1 = "Werewolf";

    public static String UNIT_WILL_WISPS_1 = "Will o' the wisps";
    public static String UNIT_WILL_WISPS_2 = "Intangible";
    public static String UNIT_WILL_WISPS_3 = "Can't capture the castle";
    public static String UNIT_WILL_WISPS_4 = "This unit can't be used to capture the enemy's castle.";

    public static String UNIT_WITCH_1 = "Witch";

    public static String UNIT_WIZARD_1 = "Wizard";

    public static String UNIT_WOLF_1 = "Wolf";

    public static String UNIT_WYVERN_1 = "Wyvern";

    public static String UNIT_ZOMBIE_1 = "Zombie";

    public static String UNIT_CAPTAIN_1 = "Captain";

    public static String UNIT_DUELIST_1 = "Duelist";

    public static String HERO_1 = "Error";

    public static String HERO_VANGUARD_1 = "Vanguard";


}

