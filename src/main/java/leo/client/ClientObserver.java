///////////////////////////////////////////////////////////////////////
// Name: ClientObserver
// Desc: The server game observer
// Date: 6/18/2003 - Gabe Jones
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.client.animation.*;
import leo.shared.*;
import leo.shared.crusades.UnitWall;

import java.awt.*;
import java.util.Vector;

public class ClientObserver implements Observer {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public ClientObserver() {
    }


    /////////////////////////////////////////////////////////////////
    // Select a unit
    /////////////////////////////////////////////////////////////////
    public void selectUnit(Unit unit) {
        if (unit.getCastle() == Client.getGameData().getMyCastle() && unit.getTeam() == Unit.TEAM_1)
            Client.getGameData().setSelectedUnit(unit);

    }


    /////////////////////////////////////////////////////////////////
    // Unit effect
    /////////////////////////////////////////////////////////////////
    public void unitEffect(Unit target, int effect) {
        switch (effect) {
            case Action.EFFECT_TRUCE:
                Client.getGameData().insert(new AnimationTruce(target));
                break;

            case Action.EFFECT_ANGEL:
                Client.getGameData().insert(new AnimationHalo(target));
                break;

            case Action.EFFECT_FIZZLE:
                Client.getGameData().add(new AnimationBurst(target.getLocation(), 5, Constants.IMG_WHITE_BALL));
                Client.getImages().playSound(Constants.SOUND_END_CHANNEL);
                break;

            case Action.EFFECT_WALL:

                Unit wall = new UnitWall(target.getCastle());
                wall.setLocation(target.getLocation());

                int appearance = (wall.getAppearance() +
                        (target.getCastle() ==
                                Client.getGameData().getMyCastle() ?
                                0 :
                                1));

                Client.getGameData().addPre(new AnimationImager(wall.getLocation(), appearance, wall, target, 20));
                break;

            case Action.EFFECT_FADE:
                Client.getGameData().insert(new AnimationFade(target));
                break;

            case Action.EFFECT_FADE_IN:
                Client.getGameData().insert(new AnimationFadeIn(target));
                break;

            case Action.EFFECT_BUILD:
                Client.getGameData().insert(new AnimationBuild(target));
                break;

            case Action.EFFECT_BANISH:
                if (target.getID() != UnitType.NONE.value()) {
                    Client.getGameData().insert(new AnimationBanish(target, target.getLocation(), target.getCastle().getLocation()));
                }
                break;
        }
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void endGame(Castle winner) {
        if (Client.getGameData().getTimer() != null)
            Client.getGameData().getTimer().end();
        Client.getGameData().setSelectedUnit(null);
        String message;
        boolean victory = false;

        Client.setComputing(false);

        if (winner == Client.getGameData().getMyCastle()) {
            message = "You have won!";
            victory = true;
            Client.getImages().playSound(Constants.SOUND_VICTORY);
        } else {
            message = "You have lost";
            victory = false;
            Client.getImages().playSound(Constants.SOUND_DEFEAT);
        }

        Client.getNetManager().stop();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        Client.getNetManager().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        Client.getGameData().screenEndGame(message, victory);
        Client.getText().clear();
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void enemySurrendered() {
        Client.getGameData().setSelectedUnit(null);
        Client.getImages().playSound(Constants.SOUND_VICTORY);
        Client.getNetManager().stop();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        Client.getNetManager().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        Client.getGameData().screenEndGame("Your enemy has surrendered. You have won!", true);
        Client.getText().clear();
    }


    /////////////////////////////////////////////////////////////////
    // End the game
    /////////////////////////////////////////////////////////////////
    public void allySurrendered() {
        Client.getGameData().setSelectedUnit(null);
        Client.getImages().playSound(Constants.SOUND_DEFEAT);
        Client.getNetManager().stop();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        Client.getNetManager().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        Client.getGameData().screenEndGame("Your ally has left. You have lost.", false);
        Client.getText().clear();
    }


    /////////////////////////////////////////////////////////////////
    // An attack
    /////////////////////////////////////////////////////////////////
    public void attack(Unit attacker, Unit victim, short damage, int type) {
        switch (type) {
            case Action.ATTACK_RELIC:
                // generate a fading effect for deploying a relic
                Client.getGameData().add(new AnimationRelic(attacker, victim));
                break;

            case Action.ATTACK_BEAM:
                // can add other colors later with a swith on damage
                Client.getGameData().add(new AnimationBeam(attacker.getLocation(), victim.getLocation(), Color.yellow.darker().darker(), 0.05f));
                break;

            case Action.ATTACK_SPIRIT:
                //Unit newVic = Unit.getUnit(victim.getID(), victim.getCastle());
                boolean isAlly = (victim.getCastle() == Client.getGameData().getMyCastle());
                Client.getGameData().add(new AnimationSpirit(victim.getLocation(), damage, victim.getAppearance(), isAlly));
                break;

            case Action.ATTACK_ARROW:
                Client.getImages().playSound(Constants.SOUND_BOW);
                Client.getGameData().add(new AnimationArrow(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_ARROW, victim, Constants.STEP_SPEED * 3, 4));
                break;

            case Action.ATTACK_XBOW:
                Client.getImages().playSound(Constants.SOUND_XBOW);
                Client.getGameData().add(new AnimationArrow(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_ARROW, victim, Constants.STEP_SPEED * 3, 4));
                break;

            case Action.ATTACK_BLOWGUN:
                Client.getImages().playSound(Constants.SOUND_XBOW);
                Client.getGameData().add(new AnimationArrow(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_ARROW, victim, Constants.STEP_SPEED * 3, 4));
                break;

            case Action.ATTACK_SPEAR:
                Client.getImages().playSound(Constants.SOUND_CATAPULT);
                Client.getGameData().add(new AnimationArrow(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_SPEAR, victim, Constants.STEP_SPEED * 2, 24));
                break;

            case Action.ATTACK_THROW_SPEAR:
                Client.getImages().playSound(Constants.SOUND_SWING);
                Client.getGameData().add(new AnimationArrow(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_SPEAR_2, victim, Constants.STEP_SPEED * 2, 2));
                break;

            case Action.ATTACK_MELEE:
                Client.getImages().playSound(Constants.SOUND_SWING);
                Client.getGameData().add(new AnimationMelee(attacker.getLocation(), victim.getLocation(), damage, victim));
                break;

            case Action.ATTACK_STONE:
                Client.getImages().playSound(Constants.SOUND_CATAPULT);
                Client.getGameData().add(new AnimationSpin(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_STONE, victim, 24));
                break;

            case Action.ATTACK_THROW:
                Client.getImages().playSound(Constants.SOUND_SWING);
                Client.getGameData().add(new AnimationSpin(attacker.getLocation(), victim.getLocation(), damage, Constants.IMG_STONE, victim, 2));
                break;

            case Action.ATTACK_CHANNEL_BOLT:
                Client.getGameData().add(new AnimationBolt(attacker.getLocation(), victim.getLocation(), damage, victim));
                break;

            case Action.ATTACK_CHANNEL_BLAST:
                Client.getGameData().add(new AnimationBlast(attacker.getLocation(), victim.getLocation(), damage, victim));
                break;

            case Action.ATTACK_MAGIC_BALL:
                Client.getImages().playSound(Constants.SOUND_MAGIC_BOLT);
                Client.getGameData().add(new AnimationMagicBall(attacker.getLocation(), victim.getLocation(), damage, victim));
                break;

            case Action.ATTACK_WISP:
                Client.getGameData().add(new AnimationWisp(attacker.getLocation(), victim.getLocation(), damage, attacker, victim));
                break;

            case Action.ATTACK_OTHER:
                Client.getGameData().add(new AnimationOtherDamage(attacker.getLocation(), victim.getLocation(), damage, victim));
                break;

            case Action.ATTACK_NONE:
                Client.getGameData().add(new AnimationDamage(attacker.getLocation(), victim.getLocation(), damage, victim, (byte) -1));
                break;

        }
    }


    /////////////////////////////////////////////////////////////////
    // Unit has been damaged
    /////////////////////////////////////////////////////////////////
    public void unitDamaged(Unit source, Unit damagedUnit, short amount) { //Client.getGameData().add(new AnimationDamage(source.getLocation(), damagedUnit.getLocation(), amount));
        //Client.getGameData().add(new AnimationArrow(source.getLocation(), damagedUnit.getLocation(), amount, Constants.IMG_ARROW));
    }


    /////////////////////////////////////////////////////////////////
    // Play a sound
    /////////////////////////////////////////////////////////////////
    public void playSound(short sound) {
        Client.getImages().playSound(sound);
    }


    /////////////////////////////////////////////////////////////////
    // Draw an image
    /////////////////////////////////////////////////////////////////
    public void imageDraw(Unit unit, short location, int image, int duration) {
        if (duration > 0) {
            Client.getGameData().add(new AnimationImager(location, image, null, null, duration));
        }
        if ((image <= Constants.IMG_POWERUP_ENRAGED && image >= Constants.IMG_POWERUP_TOXIC) || (image <= Constants.IMG_RELIC_VAMPIRE && image >= Constants.IMG_RELIC_BANISH)) {
            Client.getGameData().add(new AnimationRelic(unit, unit, duration));
        }
    }

    /////////////////////////////////////////////////////////////////
    // Ability used
    /////////////////////////////////////////////////////////////////
    public void abilityUsed(short source, short damagedUnit, int image) {
        switch (image) {
            case Constants.IMG_STAR:
                Client.getGameData().add(new AnimationRise(source, damagedUnit, Constants.IMG_STAR, Constants.IMG_EYE, false));
                break;

            case Constants.IMG_EYE:
                Client.getGameData().add(new AnimationRise(source, damagedUnit, Constants.IMG_STAR, Constants.IMG_EYE, true));
                break;

            case Constants.IMG_MASK:
                Client.getImages().playSound(Constants.SOUND_NATURE);
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.green, 0.05f));
                }

                Client.getGameData().add(new AnimationIcon(source, damagedUnit, Constants.IMG_MASK));
                break;


            case Constants.IMG_WHEEL:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationWheel(damagedUnit, true));
                break;

            case Constants.IMG_TIME_1:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.white, 0.05f));
                }
                Client.getGameData().add(new AnimationTime(damagedUnit, true));
                break;

            case Constants.IMG_TOMB_LORD_1:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationTombLord(damagedUnit, true));
                break;


            case Constants.IMG_SUMMON_1:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationSummon(damagedUnit, true, false));
                break;

            case Constants.IMG_SUMMON_BLUE_1:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationSummon(damagedUnit, true, true));
                break;

            case Constants.IMG_CLASH_1:
                Client.getGameData().add(new AnimationClash(damagedUnit, true));
                break;

            case Constants.IMG_LOCKDOWN_1:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationLockdown(damagedUnit, true));
                break;

            case Constants.IMG_TARGET_01:
                Client.getGameData().add(new AnimationTarget(damagedUnit, true));
                break;

            case Constants.IMG_SHIELD_1:
                Client.getGameData().add(new AnimationShield(damagedUnit, true));
                break;


            case Constants.IMG_FACE:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, Color.black, 0.05f));
                }
                Client.getGameData().add(new AnimationWheel(damagedUnit, false));
                break;


            case Constants.IMG_POOF:
                if (source != damagedUnit) {
                    Client.getImages().playSound(Constants.SOUND_POOF);
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, (new Color(148, 84, 160)), 0.15f));
                }
                Client.getImages().playSound(Constants.SOUND_POOF);
                Client.getGameData().add(new AnimationImages(source, damagedUnit, Constants.IMG_POOF_1, 20, 0));
                break;

            case Constants.IMG_BIG_POOF:
                if (source != damagedUnit) {
                    Client.getGameData().add(new AnimationBeam(source, damagedUnit, (new Color(148, 84, 160)), 0.15f));
                }
                Client.getImages().playSound(Constants.SOUND_STUNBALL);
                Client.getGameData().add(new AnimationImages(source, damagedUnit, Constants.IMG_BIG_POOF_1, 25, -1));
                break;

            case Constants.IMG_DEATH:
                Client.getImages().playSound(Constants.SOUND_SQUISH);
                Client.getGameData().add(new AnimationIcon(source, damagedUnit, image));
                break;

            default:
                Client.getGameData().add(new AnimationIcon(source, damagedUnit, image));
        }
    }


    /////////////////////////////////////////////////////////////////
    // Castle addition
    /////////////////////////////////////////////////////////////////
    public void castleAddition(Unit newUnit) {
        if (newUnit.getID() == UnitType.NONE.value()) return;
        Client.getGameData().getEnemyCastle().clear();
        if (newUnit.getTeam() == Unit.TEAM_2) {
            Client.getGameData().getMyCastle().removeLast();
        }
    }


    /////////////////////////////////////////////////////////////////
    // Castle needs refreshing
    /////////////////////////////////////////////////////////////////
    public void castleRefresh(Castle castle) {
    }


    /////////////////////////////////////////////////////////////////
    // Area effect
    /////////////////////////////////////////////////////////////////
    public void areaEffect(short source, short destination, int type, Unit victim) {
        switch (type) {
            case Action.AOE_DETONATE:
                Client.getGameData().add(new AnimationBeam(source, destination, Color.black, 0.15f));
                Client.getGameData().add(new AnimationDetonate(source, destination, victim));
                break;

            case Action.AOE_EXPLOSION:
                Client.getImages().playSound(Constants.SOUND_EXPLOSION);
                Client.getGameData().add(new AnimationExplosion(source, destination, Constants.IMG_EXPLOSION_1));
                break;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Fireball!
    /////////////////////////////////////////////////////////////////
    public void fireball(short source, short destination, int image, Vector<Unit> victims, Vector<Short> damages, short type) {
        var unit = UnitType.idToUnit(type);

        switch (unit) {
            case FIRE_ARCHER:
                Client.getImages().playSound(Constants.SOUND_BOW);
                Client.getGameData().add(new AnimationFireball(source, destination, damages, Constants.IMG_ARROW, victims, Constants.STEP_SPEED * 3));
                break;

            case WARLOCK:
                Client.getImages().playSound(Constants.SOUND_FIREBALL);
                Client.getGameData().add(new AnimationFireball(source, destination, damages, Constants.IMG_FIREBALL, victims, Constants.STEP_SPEED * 2));
                break;

            case DRAGON:
                Client.getImages().playSound(Constants.SOUND_GROWL);
                Client.getGameData().add(new AnimationFireball(source, destination, damages, Constants.IMG_FIREBALL, victims, Constants.STEP_SPEED * 2));
                break;

            default:
                Client.getImages().playSound(Constants.SOUND_EXPLOSION);
                Client.getGameData().add(new AnimationExplosion(source, destination, image));
        }

    }


    /////////////////////////////////////////////////////////////////
    // Lightning!
    /////////////////////////////////////////////////////////////////
    public void lightning(short source, short destination) {
        Client.getImages().playSound(Constants.SOUND_BOOM);
        Client.getGameData().add(new AnimationLightning(source, destination));
    }


    /////////////////////////////////////////////////////////////////
    // Text!
    /////////////////////////////////////////////////////////////////
    public void text(String text) {
        Client.getGameData().getTextBoard().add(text);
    }


    /////////////////////////////////////////////////////////////////
    // Something dies
    /////////////////////////////////////////////////////////////////
    public void death(Unit victim) {
        if (victim.getID() == UnitType.WALL_MASON.value()) victim.setAppearance(Constants.IMG_WALL);
        Client.getGameData().insert(new AnimationDeath(victim));
    }


    /////////////////////////////////////////////////////////////////
    // Draw game
    /////////////////////////////////////////////////////////////////
    public void drawGame() {
        Client.getGameData().setSelectedUnit(null);
        Client.getNetManager().stop();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }

        Client.getNetManager().sendAction(Action.NOTHING, Action.NOTHING, Action.NOTHING);
        Client.getGameData().screenEndGame("The game has ended in a draw.", true);
        Client.getText().clear();
    }
}
