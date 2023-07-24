///////////////////////////////////////////////////////////////////////
// Name: GameMedia
// Desc: The image loader
// Date: 2/13/2003 - Gabe Jones
//   9/13/2010 - Fletcher Cole & Alexander McCaleb
// TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports
//import leo.client.Client;
//import leo.client.Sound;
//import leo.client.OggClip;

import leo.shared.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLClassLoader;

public class GameMedia {
    public static final int MAX_SOUND_COUNT = 8;
    public static final int MAX_SOUND_INDIVIDUAL = 3;

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private final Image[] images = new Image[Constants.IMG_COUNT];
    private final Image[] grayedImages = new Image[Constants.IMG_COUNT];
    private final Image[] rotatedImages = new Image[Constants.IMG_COUNT];
    private final Sound[] sounds = new Sound[Constants.SOUND_COUNT];
    private int soundCount = 0;
    private OggClip music;
    private final Toolkit tk = Toolkit.getDefaultToolkit();
    private boolean soundLoaded = false;
    private boolean artLoaded = false;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public GameMedia() {
    }

    public String getUserDir() {
        return (System.getProperty("user.home") + "/zatikon");

    }

    private void loadSounds() {
        URL url;
        URL[] urls = new URL[1];

        try {
            // Load the sound loader
            ClassLoader soundLoader = Thread.currentThread().getContextClassLoader();

            if (!Client.isWeb()) {
                String urlName = "file:" + getUserDir() + Constants.SOUND_JAR;
                urls[0] = new URL(urlName);
                soundLoader = new URLClassLoader(urls);
            }

            // The background music
            url = soundLoader.getResource(Constants.SOUND_LOC + "music.ogg");
            music = new OggClip(new BufferedInputStream(url.openStream()));
            //music = new OggClip(AudioSystem.getAudioInputStream(url.openStream()));

            int loadingSound;

            loadingSound = Constants.SOUND_VICTORY;
            url = soundLoader.getResource(Constants.SOUND_LOC + "victory.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_PRELUDE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "prelude.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BOW;
            url = soundLoader.getResource(Constants.SOUND_LOC + "bow.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_XBOW;
            url = soundLoader.getResource(Constants.SOUND_LOC + "xbow.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_RELOAD;
            url = soundLoader.getResource(Constants.SOUND_LOC + "reload.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_ORG_HIT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "orghit.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_ORG_DEATH;
            url = soundLoader.getResource(Constants.SOUND_LOC + "orgdeath.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_INORG_HIT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "inorghit.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_INORG_DEATH;
            url = soundLoader.getResource(Constants.SOUND_LOC + "inorgdeath.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_SWING;
            url = soundLoader.getResource(Constants.SOUND_LOC + "swing.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_DETONATE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "detonate.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_DEFEAT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "defeat.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_END_TURN;
            url = soundLoader.getResource(Constants.SOUND_LOC + "endturn.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_START_TURN;
            url = soundLoader.getResource(Constants.SOUND_LOC + "startturn.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_EXPLOSION;
            url = soundLoader.getResource(Constants.SOUND_LOC + "explosion.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_FIREBALL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "fireball.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BOOM;
            url = soundLoader.getResource(Constants.SOUND_LOC + "boom.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_TING;
            url = soundLoader.getResource(Constants.SOUND_LOC + "ting.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_POOF;
            url = soundLoader.getResource(Constants.SOUND_LOC + "poof.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_MAGIC_BOLT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "magicbolt.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_STUNBALL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "stunball.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_GROWL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "growl.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_EYE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "eye.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_JUDGEMENT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "judgement.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_START_CHANNEL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "startchannel.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_END_CHANNEL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "endchannel.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_SQUISH;
            url = soundLoader.getResource(Constants.SOUND_LOC + "squish.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_CATAPULT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "catapult.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_SHEATH;
            url = soundLoader.getResource(Constants.SOUND_LOC + "sheath.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_UNSHEATH;
            url = soundLoader.getResource(Constants.SOUND_LOC + "unsheath.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_WRITE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "write.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_PAPER;
            url = soundLoader.getResource(Constants.SOUND_LOC + "paper.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BUILD;
            url = soundLoader.getResource(Constants.SOUND_LOC + "build.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_NATURE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "nature.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_WHEEL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "wheel.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BUTTON;
            url = soundLoader.getResource(Constants.SOUND_LOC + "button.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_RALLY;
            url = soundLoader.getResource(Constants.SOUND_LOC + "rally.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_CHARGE;
            url = soundLoader.getResource(Constants.SOUND_LOC + "charge.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BOWPULL;
            url = soundLoader.getResource(Constants.SOUND_LOC + "bowpull.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_WOLF;
            url = soundLoader.getResource(Constants.SOUND_LOC + "wolf.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_SHAPESHIFT;
            url = soundLoader.getResource(Constants.SOUND_LOC + "shapeshift.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_BUY;
            url = soundLoader.getResource(Constants.SOUND_LOC + "buy.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_MOUSEOVER;
            url = soundLoader.getResource(Constants.SOUND_LOC + "mouseover.wav");
            sounds[loadingSound] = new Sound(url);

            loadingSound = Constants.SOUND_GOLD;
            url = soundLoader.getResource(Constants.SOUND_LOC + "gold.wav");
            sounds[loadingSound] = new Sound(url);

        } catch (Exception e) {
            System.out.println("loadSounds " + e);
        }
        soundLoaded = true;
    }


    /////////////////////////////////////////////////////////////////
    // Preload some interface art
    /////////////////////////////////////////////////////////////////
    public void preload() {
        URL url = null;
        URL[] urls = new URL[1];
        try {
            // Load the art loader
            ClassLoader artLoader = Thread.currentThread().getContextClassLoader();

            if (!Client.isWeb()) {
                try {
                    String urlName = "file:" + getUserDir() + Constants.ART_JAR;
                    urls[0] = new URL(urlName);
                    artLoader = new URLClassLoader(urls);
                } catch (Exception e) {
                    System.out.println("Classloader preload " + e);
                }
            }

            // user icon
            url = artLoader.getResource(Constants.ART_LOC + "usericon.png");
            images[Constants.IMG_USER_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "usericon_c.png");
            images[Constants.IMG_USER_ICON_CRU] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "usericon_l.png");
            images[Constants.IMG_USER_ICON_LEG] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "usericon_cl.png");
            images[Constants.IMG_USER_ICON_CRU_LEG] = ImageIO.read(url);
            //url = artLoader.getResource(Constants.ART_LOC + "battleicon.png");
            //images[Constants.IMG_BATTLE_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "usericon_idle.png");
            images[Constants.IMG_IDLE_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "singleicon.png");
            images[Constants.IMG_SINGLE_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "constructedicon.png");
            images[Constants.IMG_CONSTRUCTED_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "cooperativeicon.png");
            images[Constants.IMG_COOPERATIVE_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "randomicon.png");
            images[Constants.IMG_RANDOM_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "teamicon.png");
            images[Constants.IMG_TEAM_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editicon.png");
            images[Constants.IMG_EDIT_ICON] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "waiting_constructedicon.png");
            images[Constants.IMG_WAITING_CONSTRUCTED_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "waiting_randomicon.png");
            images[Constants.IMG_WAITING_RANDOM_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "waiting_cooperativeicon.png");
            images[Constants.IMG_WAITING_COOPERATIVE_ICON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "waiting_teamicon.png");
            images[Constants.IMG_WAITING_TEAM_ICON] = ImageIO.read(url);

        } catch (Exception e) {
            System.out.println("preload: " + url + " " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Load the images
    /////////////////////////////////////////////////////////////////
    public void load() {

        URL url = null;
        URL[] urls = new URL[1];

        try {

            /////////////////////////////////////////////////////////
            // Sounds
            /////////////////////////////////////////////////////////
            loadSounds();

            /////////////////////////////////////////////////////////
            // Art
            /////////////////////////////////////////////////////////

            // Load the art loader
            ClassLoader artLoader = Thread.currentThread().getContextClassLoader();

            if (!Client.isWeb()) {
                try {
                    String urlName = "file:" + getUserDir() + Constants.ART_JAR;
                    urls[0] = new URL(urlName);
                    artLoader = new URLClassLoader(urls);
                } catch (Exception e) {
                    System.out.println("classloader load " + e);
                }
            }

            // start loading image urls
            url = artLoader.getResource(Constants.ART_LOC + "splat.png");
            images[Constants.IMG_SPLAT] = ImageIO.read(url);

            // load an empty
            images[Constants.IMG_POOF] = images[Constants.IMG_SPLAT];


            // miss
            url = artLoader.getResource(Constants.ART_LOC + "miss.png");
            images[Constants.IMG_MISS] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "miracle.png");
            images[Constants.IMG_MIRACLE] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "death.png");
            images[Constants.IMG_DEATH] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "fireball.png");
            images[Constants.IMG_FIREBALL] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "bigpoof.png");
            images[Constants.IMG_BIG_POOF] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "disable.png");
            images[Constants.IMG_DISABLE] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "castle1.gif");
            images[Constants.IMG_MY_CASTLE] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "castle2.gif");
            images[Constants.IMG_ENEMY_CASTLE] = ImageIO.read(url);

            /////////////////////////////////////////////////////////////////
            // Units
            /////////////////////////////////////////////////////////////////


            // Footman
            url = artLoader.getResource(Constants.ART_LOC + "footman1.png");
            images[Constants.IMG_FOOTMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "footman2.png");
            images[Constants.IMG_FOOTMAN_ENEMY] = ImageIO.read(url);


            // Bowman
            url = artLoader.getResource(Constants.ART_LOC + "bowman1.png");
            images[Constants.IMG_BOWMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bowman2.png");
            images[Constants.IMG_BOWMAN_ENEMY] = ImageIO.read(url);


            // overwatch Bowman
            url = artLoader.getResource(Constants.ART_LOC + "obowman1.png");
            images[Constants.IMG_OBOWMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "obowman2.png");
            images[Constants.IMG_OBOWMAN_ENEMY] = ImageIO.read(url);


            // Cavalry
            url = artLoader.getResource(Constants.ART_LOC + "cavalry1.png");
            images[Constants.IMG_CAVALRY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "cavalry2.png");
            images[Constants.IMG_CAVALRY_ENEMY] = ImageIO.read(url);


            // Archer
            url = artLoader.getResource(Constants.ART_LOC + "archer1.png");
            images[Constants.IMG_ARCHER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "archer2.png");
            images[Constants.IMG_ARCHER_ENEMY] = ImageIO.read(url);


            // Pikeman
            url = artLoader.getResource(Constants.ART_LOC + "pikeman1.png");
            images[Constants.IMG_PIKEMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "pikeman2.png");
            images[Constants.IMG_PIKEMAN_ENEMY] = ImageIO.read(url);


            // Knight
            url = artLoader.getResource(Constants.ART_LOC + "knight1.png");
            images[Constants.IMG_KNIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "knight2.png");
            images[Constants.IMG_KNIGHT_ENEMY] = ImageIO.read(url);


            // Ranger
            url = artLoader.getResource(Constants.ART_LOC + "rangermelee1.png");
            images[Constants.IMG_RANGER_MELEE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangermelee2.png");
            images[Constants.IMG_RANGER_MELEE_ENEMY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangerranged1.png");
            images[Constants.IMG_RANGER_RANGED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangerranged2.png");
            images[Constants.IMG_RANGER_RANGED_ENEMY] = ImageIO.read(url);


            // Ranger with his wolf
            url = artLoader.getResource(Constants.ART_LOC + "rangerwolfmelee1.png");
            images[Constants.IMG_RANGER_WOLF_MELEE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangerwolfmelee2.png");
            images[Constants.IMG_RANGER_WOLF_MELEE_ENEMY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangerwolfranged1.png");
            images[Constants.IMG_RANGER_WOLF_RANGED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rangerwolfranged2.png");
            images[Constants.IMG_RANGER_WOLF_RANGED_ENEMY] = ImageIO.read(url);


            // Wolf
            url = artLoader.getResource(Constants.ART_LOC + "wolf1.png");
            images[Constants.IMG_WOLF] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "wolf2.png");
            images[Constants.IMG_WOLF_ENEMY] = ImageIO.read(url);


            // Summoner
            url = artLoader.getResource(Constants.ART_LOC + "summoner1.png");
            images[Constants.IMG_SUMMONER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summoner2.png");
            images[Constants.IMG_SUMMONER_ENEMY] = ImageIO.read(url);


            // Imp
            url = artLoader.getResource(Constants.ART_LOC + "imp1.png");
            images[Constants.IMG_IMP] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "imp2.png");
            images[Constants.IMG_IMP_ENEMY] = ImageIO.read(url);


            // Demon
            url = artLoader.getResource(Constants.ART_LOC + "demon1.png");
            images[Constants.IMG_DEMON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "demon2.png");
            images[Constants.IMG_DEMON_ENEMY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "archdemon1.png");
            images[Constants.IMG_ARCH_DEMON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "archdemon2.png");
            images[Constants.IMG_ARCH_DEMON_ENEMY] = ImageIO.read(url);


            // Priest
            url = artLoader.getResource(Constants.ART_LOC + "priest1.png");
            images[Constants.IMG_PRIEST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "priest2.png");
            images[Constants.IMG_PRIEST_ENEMY] = ImageIO.read(url);


            // Enchanter
            url = artLoader.getResource(Constants.ART_LOC + "enchanter1.png");
            images[Constants.IMG_ENCHANTER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "enchanter2.png");
            images[Constants.IMG_ENCHANTER_ENEMY] = ImageIO.read(url);


            // Templar
            url = artLoader.getResource(Constants.ART_LOC + "templar1.png");
            images[Constants.IMG_TEMPLAR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "templar2.png");
            images[Constants.IMG_TEMPLAR_ENEMY] = ImageIO.read(url);


            // Warrior
            url = artLoader.getResource(Constants.ART_LOC + "warrior1.png");
            images[Constants.IMG_WARRIOR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "warrior2.png");
            images[Constants.IMG_WARRIOR_ENEMY] = ImageIO.read(url);


            // Rider
            url = artLoader.getResource(Constants.ART_LOC + "rider1.png");
            images[Constants.IMG_RIDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rider2.png");
            images[Constants.IMG_RIDER_ENEMY] = ImageIO.read(url);


            // Healer
            url = artLoader.getResource(Constants.ART_LOC + "healer1.png");
            images[Constants.IMG_HEALER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "healer2.png");
            images[Constants.IMG_HEALER_ENEMY] = ImageIO.read(url);


            // Wizard
            url = artLoader.getResource(Constants.ART_LOC + "wizard1.png");
            images[Constants.IMG_WIZARD] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "wizard2.png");
            images[Constants.IMG_WIZARD_ENEMY] = ImageIO.read(url);


            // Scout
            url = artLoader.getResource(Constants.ART_LOC + "scout1.png");
            images[Constants.IMG_SCOUT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "scout2.png");
            images[Constants.IMG_SCOUT_ENEMY] = ImageIO.read(url);


            // Assassin
            url = artLoader.getResource(Constants.ART_LOC + "assassin1.png");
            images[Constants.IMG_ASSASSIN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "assassin2.png");
            images[Constants.IMG_ASSASSIN_ENEMY] = ImageIO.read(url);


            // Tactician
            url = artLoader.getResource(Constants.ART_LOC + "tactician1.png");
            images[Constants.IMG_TACTICIAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tactician2.png");
            images[Constants.IMG_TACTICIAN_ENEMY] = ImageIO.read(url);


            // General
            url = artLoader.getResource(Constants.ART_LOC + "general1.png");
            images[Constants.IMG_GENERAL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "general2.png");
            images[Constants.IMG_GENERAL_ENEMY] = ImageIO.read(url);


            // Strategist
            url = artLoader.getResource(Constants.ART_LOC + "strategist1.png");
            images[Constants.IMG_STRATEGIST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "strategist2.png");
            images[Constants.IMG_STRATEGIST_ENEMY] = ImageIO.read(url);


            // Wall
            url = artLoader.getResource(Constants.ART_LOC + "wall1.png");
            images[Constants.IMG_WALL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "wall2.png");
            images[Constants.IMG_WALL_ENEMY] = ImageIO.read(url);


            // Catapult
            url = artLoader.getResource(Constants.ART_LOC + "catapult1.png");
            images[Constants.IMG_CATAPULT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "catapult2.png");
            images[Constants.IMG_CATAPULT_ENEMY] = ImageIO.read(url);


            // Ballista
            url = artLoader.getResource(Constants.ART_LOC + "ballista1.png");
            images[Constants.IMG_BALLISTA] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "ballista2.png");
            images[Constants.IMG_BALLISTA_ENEMY] = ImageIO.read(url);


            // Necromancer
            url = artLoader.getResource(Constants.ART_LOC + "necromancer1.png");
            images[Constants.IMG_NECROMANCER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "necromancer2.png");
            images[Constants.IMG_NECROMANCER_ENEMY] = ImageIO.read(url);


            // Lich
            url = artLoader.getResource(Constants.ART_LOC + "lich1.png");
            images[Constants.IMG_LICH] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lich2.png");
            images[Constants.IMG_LICH_ENEMY] = ImageIO.read(url);


            // Skeleton
            url = artLoader.getResource(Constants.ART_LOC + "skeleton1.png");
            images[Constants.IMG_SKELETON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "skeleton2.png");
            images[Constants.IMG_SKELETON_ENEMY] = ImageIO.read(url);


            // Zombie
            url = artLoader.getResource(Constants.ART_LOC + "zombie1.png");
            images[Constants.IMG_ZOMBIE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "zombie2.png");
            images[Constants.IMG_ZOMBIE_ENEMY] = ImageIO.read(url);


            // Sergeant
            url = artLoader.getResource(Constants.ART_LOC + "sergeant1.png");
            images[Constants.IMG_SERGEANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "sergeant2.png");
            images[Constants.IMG_SERGEANT_ENEMY] = ImageIO.read(url);


            // Abjurer
            url = artLoader.getResource(Constants.ART_LOC + "abjurer1.png");
            images[Constants.IMG_ABJURER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "abjurer2.png");
            images[Constants.IMG_ABJURER_ENEMY] = ImageIO.read(url);


            // Seal
            url = artLoader.getResource(Constants.ART_LOC + "seal1.png");
            images[Constants.IMG_SEAL] = ImageIO.read(url);
            images[Constants.IMG_SEAL_ENEMY] = images[Constants.IMG_SEAL];


            // Warlock
            url = artLoader.getResource(Constants.ART_LOC + "warlock1.png");
            images[Constants.IMG_WARLOCK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "warlock2.png");
            images[Constants.IMG_WARLOCK_ENEMY] = ImageIO.read(url);


            // Crossbowman
            url = artLoader.getResource(Constants.ART_LOC + "crossbowman1.png");
            images[Constants.IMG_CROSSBOWMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "crossbowman2.png");
            images[Constants.IMG_CROSSBOWMAN_ENEMY] = ImageIO.read(url);


            // Unloaded Crossbowman
            url = artLoader.getResource(Constants.ART_LOC + "uncrossbowman1.png");
            images[Constants.IMG_UNCROSSBOWMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "uncrossbowman2.png");
            images[Constants.IMG_UNCROSSBOWMAN_ENEMY] = ImageIO.read(url);


            // Dragon
            url = artLoader.getResource(Constants.ART_LOC + "dragon1.png");
            images[Constants.IMG_DRAGON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "dragon2.png");
            images[Constants.IMG_DRAGON_ENEMY] = ImageIO.read(url);


            // Draco-lich
            url = artLoader.getResource(Constants.ART_LOC + "dracolich1.png");
            images[Constants.IMG_DRACOLICH] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "dracolich2.png");
            images[Constants.IMG_DRACOLICH_ENEMY] = ImageIO.read(url);


            // Hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra1.png");
            images[Constants.IMG_HYDRA] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra2.png");
            images[Constants.IMG_HYDRA_ENEMY] = ImageIO.read(url);


            // Five headed hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra3.png");
            images[Constants.IMG_HYDRA_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra4.png");
            images[Constants.IMG_HYDRA_5_ENEMY] = ImageIO.read(url);


            // Four headed hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra5.png");
            images[Constants.IMG_HYDRA_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra6.png");
            images[Constants.IMG_HYDRA_4_ENEMY] = ImageIO.read(url);


            // three headed hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra7.png");
            images[Constants.IMG_HYDRA_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra8.png");
            images[Constants.IMG_HYDRA_3_ENEMY] = ImageIO.read(url);


            // two headed hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra9.png");
            images[Constants.IMG_HYDRA_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra10.png");
            images[Constants.IMG_HYDRA_2_ENEMY] = ImageIO.read(url);


            // one headed hydra
            url = artLoader.getResource(Constants.ART_LOC + "hydra11.png");
            images[Constants.IMG_HYDRA_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "hydra12.png");
            images[Constants.IMG_HYDRA_1_ENEMY] = ImageIO.read(url);


            // Tower
            url = artLoader.getResource(Constants.ART_LOC + "tower1.gif");
            images[Constants.IMG_TOWER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tower2.gif");
            images[Constants.IMG_TOWER_ENEMY] = ImageIO.read(url);


            // Command Post
            url = artLoader.getResource(Constants.ART_LOC + "commandPost1.gif");
            images[Constants.IMG_COMMAND_POST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "commandPost2.gif");
            images[Constants.IMG_COMMAND_POST_ENEMY] = ImageIO.read(url);


            // Barracks
            url = artLoader.getResource(Constants.ART_LOC + "barracks1.gif");
            images[Constants.IMG_BARRACKS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "barracks2.gif");
            images[Constants.IMG_BARRACKS_ENEMY] = ImageIO.read(url);


            // Soldier
            url = artLoader.getResource(Constants.ART_LOC + "soldier1.png");
            images[Constants.IMG_SOLDIER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "soldier2.png");
            images[Constants.IMG_SOLDIER_ENEMY] = ImageIO.read(url);


            // Druid
            url = artLoader.getResource(Constants.ART_LOC + "druid1.png");
            images[Constants.IMG_DRUID] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "druid2.png");
            images[Constants.IMG_DRUID_ENEMY] = ImageIO.read(url);


            // Channeler
            url = artLoader.getResource(Constants.ART_LOC + "channeler1.png");
            images[Constants.IMG_CHANNELER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "channeler2.png");
            images[Constants.IMG_CHANNELER_ENEMY] = ImageIO.read(url);


            // Lycanthrope
            url = artLoader.getResource(Constants.ART_LOC + "lycanthrope1.png");
            images[Constants.IMG_LYCANTHROPE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lycanthrope2.png");
            images[Constants.IMG_LYCANTHROPE_ENEMY] = ImageIO.read(url);


            // Werewolf
            url = artLoader.getResource(Constants.ART_LOC + "werewolf1.png");
            images[Constants.IMG_WEREWOLF] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "werewolf2.png");
            images[Constants.IMG_WEREWOLF_ENEMY] = ImageIO.read(url);


            // Lycanwolf
            url = artLoader.getResource(Constants.ART_LOC + "lycanwolf1.png");
            images[Constants.IMG_LYCANWOLF] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lycanwolf2.png");
            images[Constants.IMG_LYCANWOLF_ENEMY] = ImageIO.read(url);


            // Horse archer
            url = artLoader.getResource(Constants.ART_LOC + "mountedarcher1.png");
            images[Constants.IMG_MOUNTED_ARCHER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mountedarcher2.png");
            images[Constants.IMG_MOUNTED_ARCHER_ENEMY] = ImageIO.read(url);


            // Geomancer
            url = artLoader.getResource(Constants.ART_LOC + "geomancer1.png");
            images[Constants.IMG_GEOMANCER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "geomancer2.png");
            images[Constants.IMG_GEOMANCER_ENEMY] = ImageIO.read(url);


            // Rock
            url = artLoader.getResource(Constants.ART_LOC + "rock1.png");
            images[Constants.IMG_ROCK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rock2.png");
            images[Constants.IMG_ROCK_ENEMY] = ImageIO.read(url);


            // Swordsman
            url = artLoader.getResource(Constants.ART_LOC + "swordsman1.png");
            images[Constants.IMG_SWORDSMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "swordsman2.png");
            images[Constants.IMG_SWORDSMAN_ENEMY] = ImageIO.read(url);


            // Witch
            url = artLoader.getResource(Constants.ART_LOC + "witch1.png");
            images[Constants.IMG_WITCH] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "witch2.png");
            images[Constants.IMG_WITCH_ENEMY] = ImageIO.read(url);


            // Toad
            url = artLoader.getResource(Constants.ART_LOC + "toad1.png");
            images[Constants.IMG_TOAD] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "toad2.png");
            images[Constants.IMG_TOAD_ENEMY] = ImageIO.read(url);


            // Shield maiden
            url = artLoader.getResource(Constants.ART_LOC + "shieldmaiden1.png");
            images[Constants.IMG_SHIELD_MAIDEN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "shieldmaiden2.png");
            images[Constants.IMG_SHIELD_MAIDEN_ENEMY] = ImageIO.read(url);


            // Magus
            url = artLoader.getResource(Constants.ART_LOC + "magus1.png");
            images[Constants.IMG_MAGUS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magus2.png");
            images[Constants.IMG_MAGUS_ENEMY] = ImageIO.read(url);


            // Spirit
            url = artLoader.getResource(Constants.ART_LOC + "spirit1.png");
            images[Constants.IMG_SPIRIT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "spirit2.png");
            images[Constants.IMG_SPIRIT_ENEMY] = ImageIO.read(url);


            // Will-o-the-wisps
            url = artLoader.getResource(Constants.ART_LOC + "will-o-the-wisps1.png");
            images[Constants.IMG_WILL_O_THE_WISPS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "will-o-the-wisps2.png");
            images[Constants.IMG_WILL_O_THE_WISPS_ENEMY] = ImageIO.read(url);


            // Golem
            url = artLoader.getResource(Constants.ART_LOC + "golem1.png");
            images[Constants.IMG_GOLEM] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "golem2.png");
            images[Constants.IMG_GOLEM_ENEMY] = ImageIO.read(url);


            // Armory
            url = artLoader.getResource(Constants.ART_LOC + "armory1.gif");
            images[Constants.IMG_ARMORY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "armory2.gif");
            images[Constants.IMG_ARMORY_ENEMY] = ImageIO.read(url);


            // Serpent
            url = artLoader.getResource(Constants.ART_LOC + "serpent1.png");
            images[Constants.IMG_SERPENT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "serpent2.png");
            images[Constants.IMG_SERPENT_ENEMY] = ImageIO.read(url);


            // Fire archer
            url = artLoader.getResource(Constants.ART_LOC + "firearcher1.png");
            images[Constants.IMG_FIRE_ARCHER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "firearcher2.png");
            images[Constants.IMG_FIRE_ARCHER_ENEMY] = ImageIO.read(url);


            // Mimic
            url = artLoader.getResource(Constants.ART_LOC + "mimic1.png");
            images[Constants.IMG_MIMIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mimic2.png");
            images[Constants.IMG_MIMIC_ENEMY] = ImageIO.read(url);


            // Paladin
            url = artLoader.getResource(Constants.ART_LOC + "paladin1.png");
            images[Constants.IMG_PALADIN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "paladin2.png");
            images[Constants.IMG_PALADIN_ENEMY] = ImageIO.read(url);


            // Shaman
            url = artLoader.getResource(Constants.ART_LOC + "shaman1.png");
            images[Constants.IMG_SHAMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "shaman2.png");
            images[Constants.IMG_SHAMAN_ENEMY] = ImageIO.read(url);


            // Martyr
            url = artLoader.getResource(Constants.ART_LOC + "martyr1.png");
            images[Constants.IMG_MARTYR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "martyr2.png");
            images[Constants.IMG_MARTYR_ENEMY] = ImageIO.read(url);


            // Rogue
            url = artLoader.getResource(Constants.ART_LOC + "rogue1.png");
            images[Constants.IMG_ROGUE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "rogue2.png");
            images[Constants.IMG_ROGUE_ENEMY] = ImageIO.read(url);


            // Diabolist
            url = artLoader.getResource(Constants.ART_LOC + "diabolist1.png");
            images[Constants.IMG_DIABOLIST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "diabolist2.png");
            images[Constants.IMG_DIABOLIST_ENEMY] = ImageIO.read(url);
            // Devil
            url = artLoader.getResource(Constants.ART_LOC + "devil1.png");
            images[Constants.IMG_DEVIL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "devil2.png");
            images[Constants.IMG_DEVIL_ENEMY] = ImageIO.read(url);


            // Ghost
            url = artLoader.getResource(Constants.ART_LOC + "ghost1.png");
            images[Constants.IMG_GHOST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "ghost2.png");
            images[Constants.IMG_GHOST_ENEMY] = ImageIO.read(url);


            // templar aura
            url = artLoader.getResource(Constants.ART_LOC + "templaraura1.png");
            images[Constants.IMG_TEMPLAR_AURA] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "templaraura2.png");
            images[Constants.IMG_TEMPLAR_AURA_ENEMY] = ImageIO.read(url);


            // gate guard
            url = artLoader.getResource(Constants.ART_LOC + "gateguard1.png");
            images[Constants.IMG_GATE_GUARD] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "gateguard2.png");
            images[Constants.IMG_GATE_GUARD_ENEMY] = ImageIO.read(url);


            // feathered serpent
            url = artLoader.getResource(Constants.ART_LOC + "featheredserpent1.png");
            images[Constants.IMG_FEATHERED_SERPENT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "featheredserpent2.png");
            images[Constants.IMG_FEATHERED_SERPENT_ENEMY] = ImageIO.read(url);


            // berserker
            url = artLoader.getResource(Constants.ART_LOC + "berserker1.png");
            images[Constants.IMG_BERSERKER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "berserker2.png");
            images[Constants.IMG_BERSERKER_ENEMY] = ImageIO.read(url);


            // berserker, dead
            url = artLoader.getResource(Constants.ART_LOC + "deadberserker1.png");
            images[Constants.IMG_DEAD_BERSERKER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "deadberserker2.png");
            images[Constants.IMG_DEAD_BERSERKER_ENEMY] = ImageIO.read(url);


            // artificer
            url = artLoader.getResource(Constants.ART_LOC + "artificer1.png");
            images[Constants.IMG_ARTIFICER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "artificer2.png");
            images[Constants.IMG_ARTIFICER_ENEMY] = ImageIO.read(url);


            // Changeling
            url = artLoader.getResource(Constants.ART_LOC + "changeling1.png");
            images[Constants.IMG_CHANGELING] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "changeling2.png");
            images[Constants.IMG_CHANGELING_ENEMY] = ImageIO.read(url);


            // Doppelganger
            url = artLoader.getResource(Constants.ART_LOC + "doppelganger1.png");
            images[Constants.IMG_DOPPELGANGER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "doppelganger2.png");
            images[Constants.IMG_DOPPELGANGER_ENEMY] = ImageIO.read(url);


            // Skinwalker
            url = artLoader.getResource(Constants.ART_LOC + "skinwalker1.png");
            images[Constants.IMG_SKINWALKER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "skinwalker2.png");
            images[Constants.IMG_SKINWALKER_ENEMY] = ImageIO.read(url);


            // Acolyte
            url = artLoader.getResource(Constants.ART_LOC + "acolyte1.png");
            images[Constants.IMG_ACOLYTE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "acolyte2.png");
            images[Constants.IMG_ACOLYTE_ENEMY] = ImageIO.read(url);


            // Axeman
            url = artLoader.getResource(Constants.ART_LOC + "axeman1.png");
            images[Constants.IMG_AXEMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "axeman2.png");
            images[Constants.IMG_AXEMAN_ENEMY] = ImageIO.read(url);


            // Mourner
            url = artLoader.getResource(Constants.ART_LOC + "mourner1.png");
            images[Constants.IMG_MOURNER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mourner2.png");
            images[Constants.IMG_MOURNER_ENEMY] = ImageIO.read(url);


            // Heretic
            url = artLoader.getResource(Constants.ART_LOC + "heretic1.png");
            images[Constants.IMG_HERETIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "heretic2.png");
            images[Constants.IMG_HERETIC_ENEMY] = ImageIO.read(url);


            // War elephant
            url = artLoader.getResource(Constants.ART_LOC + "warelephant1.png");
            images[Constants.IMG_WAR_ELEPHANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "warelephant2.png");
            images[Constants.IMG_WAR_ELEPHANT_ENEMY] = ImageIO.read(url);


            // fanatic
            url = artLoader.getResource(Constants.ART_LOC + "fanatic1.png");
            images[Constants.IMG_FANATIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "fanatic2.png");
            images[Constants.IMG_FANATIC_ENEMY] = ImageIO.read(url);


            // the dismounted knight
            url = artLoader.getResource(Constants.ART_LOC + "dismountedknight1.png");
            images[Constants.IMG_DISMOUNTED_KNIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "dismountedknight2.png");
            images[Constants.IMG_DISMOUNTED_KNIGHT_ENEMY] = ImageIO.read(url);


            // bear
            url = artLoader.getResource(Constants.ART_LOC + "bear1.png");
            images[Constants.IMG_BEAR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bear2.png");
            images[Constants.IMG_BEAR_ENEMY] = ImageIO.read(url);


            // quartermaster
            url = artLoader.getResource(Constants.ART_LOC + "quartermaster1.png");
            images[Constants.IMG_QUARTERMASTER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "quartermaster2.png");
            images[Constants.IMG_QUARTERMASTER_ENEMY] = ImageIO.read(url);


            // mason
            url = artLoader.getResource(Constants.ART_LOC + "mason1.png");
            images[Constants.IMG_MASON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mason2.png");
            images[Constants.IMG_MASON_ENEMY] = ImageIO.read(url);


            // wall mason
            url = artLoader.getResource(Constants.ART_LOC + "wallmason1.png");
            images[Constants.IMG_WALL_MASON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "wallmason2.png");
            images[Constants.IMG_WALL_MASON_ENEMY] = ImageIO.read(url);


            // double dop
            url = artLoader.getResource(Constants.ART_LOC + "doubledop1.png");
            images[Constants.IMG_DOUBLE_DOP] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "doubledop2.png");
            images[Constants.IMG_DOUBLE_DOP_ENEMY] = ImageIO.read(url);


            // confessor
            url = artLoader.getResource(Constants.ART_LOC + "confessor1.png");
            images[Constants.IMG_CONFESSOR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "confessor2.png");
            images[Constants.IMG_CONFESSOR_ENEMY] = ImageIO.read(url);


            // possessed
            url = artLoader.getResource(Constants.ART_LOC + "possessed1.png");
            images[Constants.IMG_POSSESSED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "possessed2.png");
            images[Constants.IMG_POSSESSED_ENEMY] = ImageIO.read(url);


            // barbarian
            url = artLoader.getResource(Constants.ART_LOC + "barbarian1.png");
            images[Constants.IMG_BARBARIAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "barbarian2.png");
            images[Constants.IMG_BARBARIAN_ENEMY] = ImageIO.read(url);


            // alchemist
            url = artLoader.getResource(Constants.ART_LOC + "alchemist1.png");
            images[Constants.IMG_ALCHEMIST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "alchemist2.png");
            images[Constants.IMG_ALCHEMIST_ENEMY] = ImageIO.read(url);


            // bounty hunter
            url = artLoader.getResource(Constants.ART_LOC + "bountyhunter1.png");
            images[Constants.IMG_BOUNTY_HUNTER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bountyhunter2.png");
            images[Constants.IMG_BOUNTY_HUNTER_ENEMY] = ImageIO.read(url);


            // shield bearer
            url = artLoader.getResource(Constants.ART_LOC + "shieldbearer1.png");
            images[Constants.IMG_SHIELD_BEARER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "shieldbearer2.png");
            images[Constants.IMG_SHIELD_BEARER_ENEMY] = ImageIO.read(url);


            // chieftain
            url = artLoader.getResource(Constants.ART_LOC + "chieftain1.png");
            images[Constants.IMG_CHIEFTAIN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chieftain2.png");
            images[Constants.IMG_CHIEFTAIN_ENEMY] = ImageIO.read(url);


            // lancer
            url = artLoader.getResource(Constants.ART_LOC + "lancer1.png");
            images[Constants.IMG_LANCER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lancer2.png");
            images[Constants.IMG_LANCER_ENEMY] = ImageIO.read(url);


            // archangel
            url = artLoader.getResource(Constants.ART_LOC + "archangel1.png");
            images[Constants.IMG_ARCHANGEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "archangel2.png");
            images[Constants.IMG_ARCHANGEL_ENEMY] = ImageIO.read(url);


            // conjurer
            url = artLoader.getResource(Constants.ART_LOC + "conjurer1.png");
            images[Constants.IMG_CONJURER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "conjurer2.png");
            images[Constants.IMG_CONJURER_ENEMY] = ImageIO.read(url);


            // portal
            url = artLoader.getResource(Constants.ART_LOC + "portal1.png");
            images[Constants.IMG_PORTAL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "portal2.png");
            images[Constants.IMG_PORTAL_ENEMY] = ImageIO.read(url);


            // gate
            url = artLoader.getResource(Constants.ART_LOC + "gate1.png");
            images[Constants.IMG_GATE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "gate2.png");
            images[Constants.IMG_GATE_ENEMY] = ImageIO.read(url);


            // diplomat
            url = artLoader.getResource(Constants.ART_LOC + "diplomat1.png");
            images[Constants.IMG_DIPLOMAT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "diplomat2.png");
            images[Constants.IMG_DIPLOMAT_ENEMY] = ImageIO.read(url);


            // longbowman
            url = artLoader.getResource(Constants.ART_LOC + "longbowman1.png");
            images[Constants.IMG_LONGBOWMAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "longbowman2.png");
            images[Constants.IMG_LONGBOWMAN_ENEMY] = ImageIO.read(url);


            // sycophant
            url = artLoader.getResource(Constants.ART_LOC + "sycophant1.png");
            images[Constants.IMG_SYCOPHANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "sycophant2.png");
            images[Constants.IMG_SYCOPHANT_ENEMY] = ImageIO.read(url);


            // wyvern
            url = artLoader.getResource(Constants.ART_LOC + "wyvern1.png");
            images[Constants.IMG_WYVERN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "wyvern2.png");
            images[Constants.IMG_WYVERN_ENEMY] = ImageIO.read(url);


            // egg
            url = artLoader.getResource(Constants.ART_LOC + "egg1.png");
            images[Constants.IMG_EGG] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "egg2.png");
            images[Constants.IMG_EGG_ENEMY] = ImageIO.read(url);


            // captain
            url = artLoader.getResource(Constants.ART_LOC + "captain1.png");
            images[Constants.IMG_CAPTAIN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "captain2.png");
            images[Constants.IMG_CAPTAIN_ENEMY] = ImageIO.read(url);


            // abbey
            url = artLoader.getResource(Constants.ART_LOC + "abbey1.png");
            images[Constants.IMG_ABBEY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "abbey2.png");
            images[Constants.IMG_ABBEY_ENEMY] = ImageIO.read(url);


            // supplicant
            url = artLoader.getResource(Constants.ART_LOC + "supplicant1.png");
            images[Constants.IMG_SUPPLICANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "supplicant2.png");
            images[Constants.IMG_SUPPLICANT_ENEMY] = ImageIO.read(url);


            ////////////////////////////////////////////////////////////////////
            // New units as of 9/15/10
            ////////////////////////////////////////////////////////////////////


            // duelist (new unit)
            url = artLoader.getResource(Constants.ART_LOC + "duelist1.png");
            images[Constants.IMG_DUELIST] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "duelist2.png");
            images[Constants.IMG_DUELIST_ENEMY] = ImageIO.read(url);


            // militia (new unit)
            url = artLoader.getResource(Constants.ART_LOC + "militia1.png");
            images[Constants.IMG_MILITIA] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "militia2.png");
            images[Constants.IMG_MILITIA_ENEMY] = ImageIO.read(url);


            // conspirator (new unit)
            url = artLoader.getResource(Constants.ART_LOC + "conspirator1.png");
            images[Constants.IMG_CONSPIRATOR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "conspirator2.png");
            images[Constants.IMG_CONSPIRATOR_ENEMY] = ImageIO.read(url);


            // upgraded soldier offensive
            url = artLoader.getResource(Constants.ART_LOC + "soldiero1.png");
            images[Constants.IMG_SOLDIER_O] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "soldiero2.png");
            images[Constants.IMG_SOLDIER_O_ENEMY] = ImageIO.read(url);


            // upgraded soldier defensive
            url = artLoader.getResource(Constants.ART_LOC + "soldierd1.png");
            images[Constants.IMG_SOLDIER_D] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "soldierd2.png");
            images[Constants.IMG_SOLDIER_D_ENEMY] = ImageIO.read(url);
/* 
  // newunit3
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit31.png");
  images[Constants.IMG_NEWUNIT3] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit32.png");
  images[Constants.IMG_NEWUNIT3_ENEMY] = ImageIO.read(url);
 
  // newunit4
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit41.png");
  images[Constants.IMG_NEWUNIT4] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit42.png");
  images[Constants.IMG_NEWUNIT4_ENEMY] = ImageIO.read(url);
 
  // newunit5
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit51.png");
  images[Constants.IMG_NEWUNIT5] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit52.png");
  images[Constants.IMG_NEWUNIT5_ENEMY] = ImageIO.read(url);
 
  // newunit6
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit61.png");
  images[Constants.IMG_NEWUNIT6] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit62.png");
  images[Constants.IMG_NEWUNIT6_ENEMY] = ImageIO.read(url);
 
  // newunit7
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit71.png");
  images[Constants.IMG_NEWUNIT7] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit72.png");
  images[Constants.IMG_NEWUNIT7_ENEMY] = ImageIO.read(url);
 
  // newunit8
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit81.png");
  images[Constants.IMG_NEWUNIT8] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit82.png");
  images[Constants.IMG_NEWUNIT8_ENEMY] = ImageIO.read(url);
 
  // newunit9
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit91.png");
  images[Constants.IMG_NEWUNIT9] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit92.png");
  images[Constants.IMG_NEWUNIT9_ENEMY] = ImageIO.read(url);
 
  // newunit10
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit101.png");
  images[Constants.IMG_NEWUNIT10] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit102.png");
  images[Constants.IMG_NEWUNIT10_ENEMY] = ImageIO.read(url);
 
  // newunit11
  System.out.println(url + "WORLD");
  url = artLoader.getResource(Constants.ART_LOC + "newunit111.png");
  images[Constants.IMG_NEWUNIT11] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "newunit112.png");
  images[Constants.IMG_NEWUNIT11_ENEMY] = ImageIO.read(url);

*/
            /////////////////////////////////////////////////////////////////
            // Projectiles
            /////////////////////////////////////////////////////////////////

            // Arrow
            url = artLoader.getResource(Constants.ART_LOC + "arrow.gif");
            images[Constants.IMG_ARROW] = ImageIO.read(url);

            // Stone
            url = artLoader.getResource(Constants.ART_LOC + "stone.png");
            images[Constants.IMG_STONE] = ImageIO.read(url);

            // Green ball
            url = artLoader.getResource(Constants.ART_LOC + "greenball.png");
            images[Constants.IMG_GREEN_BALL] = ImageIO.read(url);

            // purple ball
            url = artLoader.getResource(Constants.ART_LOC + "purpleball.png");
            images[Constants.IMG_PURPLE_BALL] = ImageIO.read(url);

            // Spear
            url = artLoader.getResource(Constants.ART_LOC + "spear.png");
            images[Constants.IMG_SPEAR] = ImageIO.read(url);
            images[Constants.IMG_SPEAR_2] = images[Constants.IMG_SPEAR];

            // magic ball 1-6
            url = artLoader.getResource(Constants.ART_LOC + "magicball1.png");
            images[Constants.IMG_MAGIC_BALL_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magicball2.png");
            images[Constants.IMG_MAGIC_BALL_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magicball3.png");
            images[Constants.IMG_MAGIC_BALL_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magicball4.png");
            images[Constants.IMG_MAGIC_BALL_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magicball5.png");
            images[Constants.IMG_MAGIC_BALL_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "magicball6.png");
            images[Constants.IMG_MAGIC_BALL_6] = ImageIO.read(url);

            // white ball
            url = artLoader.getResource(Constants.ART_LOC + "whiteball.png");
            images[Constants.IMG_WHITE_BALL] = ImageIO.read(url);

            // red ball
            url = artLoader.getResource(Constants.ART_LOC + "redball.png");
            images[Constants.IMG_RED_BALL] = ImageIO.read(url);


            /////////////////////////////////////////////////////////////////
            // Interface
            /////////////////////////////////////////////////////////////////

            // action button
            url = artLoader.getResource(Constants.ART_LOC + "action.jpg");
            images[Constants.IMG_ACTION] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "actiondisable.jpg");
            images[Constants.IMG_ACTION_DISABLE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "actionpassive.gif");
            images[Constants.IMG_ACTION_PASSIVE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "actionhighlight.jpg");
            images[Constants.IMG_ACTION_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "actionoverlay.png");
            images[Constants.IMG_ACTION_OVERLAY] = ImageIO.read(url);

            // panels
            url = artLoader.getResource(Constants.ART_LOC + "backpanel.jpg");
            images[Constants.IMG_BACK_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "backpanelgame.jpg");
            images[Constants.IMG_BACK_PANEL_GAME] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "statpanel.jpg");
            images[Constants.IMG_STAT_PANEL] = ImageIO.read(url);

            // deploy button
            url = artLoader.getResource(Constants.ART_LOC + "deploy.jpg");
            images[Constants.IMG_DEPLOY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "deployhighlight.jpg");
            images[Constants.IMG_DEPLOY_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "deployselected.jpg");
            images[Constants.IMG_DEPLOY_SELECTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "deploygrey.jpg");
            images[Constants.IMG_DEPLOY_GREY] = ImageIO.read(url);

            // end panel
            url = artLoader.getResource(Constants.ART_LOC + "endpanel.jpg");
            images[Constants.IMG_END_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "endturn.jpg");
            images[Constants.IMG_END_TURN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "surrender.jpg");
            images[Constants.IMG_SURRENDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "draw.jpg");
            images[Constants.IMG_DRAW] = ImageIO.read(url);

            // highlights
            url = artLoader.getResource(Constants.ART_LOC + "blue.gif");
            images[Constants.IMG_BLUE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "blueborder.gif");
            images[Constants.IMG_BLUE_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "red.gif");
            images[Constants.IMG_RED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "redborder.gif");
            images[Constants.IMG_RED_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "green.gif");
            images[Constants.IMG_GREEN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "greenborder.gif");
            images[Constants.IMG_GREEN_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "purple.gif");
            images[Constants.IMG_PURPLE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "purpleborder.gif");
            images[Constants.IMG_PURPLE_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "black.gif");
            images[Constants.IMG_BLACK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "blackborder.gif");
            images[Constants.IMG_BLACK_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "yellow.gif");
            images[Constants.IMG_YELLOW] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "yellowborder.gif");
            images[Constants.IMG_YELLOW_BORDER] = ImageIO.read(url);

            // poof animation
            url = artLoader.getResource(Constants.ART_LOC + "poof1.png");
            images[Constants.IMG_POOF_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof2.png");
            images[Constants.IMG_POOF_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof3.png");
            images[Constants.IMG_POOF_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof4.png");
            images[Constants.IMG_POOF_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof5.png");
            images[Constants.IMG_POOF_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof6.png");
            images[Constants.IMG_POOF_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof7.png");
            images[Constants.IMG_POOF_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof8.png");
            images[Constants.IMG_POOF_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof9.png");
            images[Constants.IMG_POOF_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof10.png");
            images[Constants.IMG_POOF_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof11.png");
            images[Constants.IMG_POOF_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof12.png");
            images[Constants.IMG_POOF_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof13.png");
            images[Constants.IMG_POOF_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof14.png");
            images[Constants.IMG_POOF_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof15.png");
            images[Constants.IMG_POOF_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof16.png");
            images[Constants.IMG_POOF_16] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof17.png");
            images[Constants.IMG_POOF_17] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof18.png");
            images[Constants.IMG_POOF_18] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof19.png");
            images[Constants.IMG_POOF_19] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "poof20.png");
            images[Constants.IMG_POOF_20] = ImageIO.read(url);

            // explosion
            url = artLoader.getResource(Constants.ART_LOC + "explosion1.png");
            images[Constants.IMG_EXPLOSION_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion2.png");
            images[Constants.IMG_EXPLOSION_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion3.png");
            images[Constants.IMG_EXPLOSION_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion4.png");
            images[Constants.IMG_EXPLOSION_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion5.png");
            images[Constants.IMG_EXPLOSION_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion6.png");
            images[Constants.IMG_EXPLOSION_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion7.png");
            images[Constants.IMG_EXPLOSION_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion8.png");
            images[Constants.IMG_EXPLOSION_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion9.png");
            images[Constants.IMG_EXPLOSION_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion10.png");
            images[Constants.IMG_EXPLOSION_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion11.png");
            images[Constants.IMG_EXPLOSION_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion12.png");
            images[Constants.IMG_EXPLOSION_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion13.png");
            images[Constants.IMG_EXPLOSION_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion14.png");
            images[Constants.IMG_EXPLOSION_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion15.png");
            images[Constants.IMG_EXPLOSION_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "explosion16.png");
            images[Constants.IMG_EXPLOSION_16] = ImageIO.read(url);

            // tiles
            url = artLoader.getResource(Constants.ART_LOC + "lighttile.jpg");
            images[Constants.IMG_LIGHT_TILE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "darktile.jpg");
            images[Constants.IMG_DARK_TILE] = ImageIO.read(url);

            // detonate
            url = artLoader.getResource(Constants.ART_LOC + "detonate1.png");
            images[Constants.IMG_DETONATE_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate2.png");
            images[Constants.IMG_DETONATE_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate3.png");
            images[Constants.IMG_DETONATE_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate4.png");
            images[Constants.IMG_DETONATE_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate5.png");
            images[Constants.IMG_DETONATE_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate6.png");
            images[Constants.IMG_DETONATE_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate7.png");
            images[Constants.IMG_DETONATE_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate8.png");
            images[Constants.IMG_DETONATE_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate9.png");
            images[Constants.IMG_DETONATE_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate10.png");
            images[Constants.IMG_DETONATE_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate11.png");
            images[Constants.IMG_DETONATE_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate12.png");
            images[Constants.IMG_DETONATE_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate13.png");
            images[Constants.IMG_DETONATE_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate14.png");
            images[Constants.IMG_DETONATE_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate15.png");
            images[Constants.IMG_DETONATE_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "detonate16.png");
            images[Constants.IMG_DETONATE_16] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "bigpoof1.png");
            images[Constants.IMG_BIG_POOF_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof2.png");
            images[Constants.IMG_BIG_POOF_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof3.png");
            images[Constants.IMG_BIG_POOF_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof4.png");
            images[Constants.IMG_BIG_POOF_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof5.png");
            images[Constants.IMG_BIG_POOF_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof6.png");
            images[Constants.IMG_BIG_POOF_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof7.png");
            images[Constants.IMG_BIG_POOF_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof8.png");
            images[Constants.IMG_BIG_POOF_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof9.png");
            images[Constants.IMG_BIG_POOF_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof10.png");
            images[Constants.IMG_BIG_POOF_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof11.png");
            images[Constants.IMG_BIG_POOF_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof12.png");
            images[Constants.IMG_BIG_POOF_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof13.png");
            images[Constants.IMG_BIG_POOF_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof14.png");
            images[Constants.IMG_BIG_POOF_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof15.png");
            images[Constants.IMG_BIG_POOF_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof16.png");
            images[Constants.IMG_BIG_POOF_16] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof17.png");
            images[Constants.IMG_BIG_POOF_17] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof18.png");
            images[Constants.IMG_BIG_POOF_18] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof19.png");
            images[Constants.IMG_BIG_POOF_19] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof20.png");
            images[Constants.IMG_BIG_POOF_20] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof21.png");
            images[Constants.IMG_BIG_POOF_21] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof22.png");
            images[Constants.IMG_BIG_POOF_22] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof23.png");
            images[Constants.IMG_BIG_POOF_23] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof24.png");
            images[Constants.IMG_BIG_POOF_24] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bigpoof25.png");
            images[Constants.IMG_BIG_POOF_25] = ImageIO.read(url);

            // Clergy effects
            url = artLoader.getResource(Constants.ART_LOC + "eye.png");
            images[Constants.IMG_EYE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "star.png");
            images[Constants.IMG_STAR] = ImageIO.read(url);

            // holy burst
            url = artLoader.getResource(Constants.ART_LOC + "burst1.png");
            images[Constants.IMG_BURST_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst2.png");
            images[Constants.IMG_BURST_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst3.png");
            images[Constants.IMG_BURST_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst4.png");
            images[Constants.IMG_BURST_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst5.png");
            images[Constants.IMG_BURST_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst6.png");
            images[Constants.IMG_BURST_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst7.png");
            images[Constants.IMG_BURST_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst8.png");
            images[Constants.IMG_BURST_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst9.png");
            images[Constants.IMG_BURST_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst10.png");
            images[Constants.IMG_BURST_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst11.png");
            images[Constants.IMG_BURST_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst12.png");
            images[Constants.IMG_BURST_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst13.png");
            images[Constants.IMG_BURST_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst14.png");
            images[Constants.IMG_BURST_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst15.png");
            images[Constants.IMG_BURST_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst16.png");
            images[Constants.IMG_BURST_16] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst17.png");
            images[Constants.IMG_BURST_17] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst18.png");
            images[Constants.IMG_BURST_18] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst19.png");
            images[Constants.IMG_BURST_19] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "burst20.png");
            images[Constants.IMG_BURST_20] = ImageIO.read(url);

            // some nature magic
            url = artLoader.getResource(Constants.ART_LOC + "wheel.png");
            images[Constants.IMG_WHEEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "face.png");
            images[Constants.IMG_FACE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mask.png");
            images[Constants.IMG_MASK] = ImageIO.read(url);

            // white flag
            url = artLoader.getResource(Constants.ART_LOC + "whiteflag.png");
            images[Constants.IMG_WHITE_FLAG] = ImageIO.read(url);

            // menu animation
            url = artLoader.getResource(Constants.ART_LOC + "menu.jpg");
            images[Constants.IMG_MENU] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "single.png");
            images[Constants.IMG_SINGLE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "singlered.png");
            images[Constants.IMG_SINGLE_RED] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "constructed.png");
            images[Constants.IMG_CONSTRUCTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "constructedred.png");
            images[Constants.IMG_CONSTRUCTED_RED] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "random.png");
            images[Constants.IMG_RANDOM] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "randomred.png");
            images[Constants.IMG_RANDOM_RED] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "edit.png");
            images[Constants.IMG_EDIT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editred.png");
            images[Constants.IMG_EDIT_RED] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "archive.png");
            images[Constants.IMG_ARCHIVE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "archivered.png");
            images[Constants.IMG_ARCHIVE_RED] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "buy.png");
            images[Constants.IMG_BUY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "buyred.png");
            images[Constants.IMG_BUY_RED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "buydisabled.png");
            images[Constants.IMG_BUY_DISABLED] = ImageIO.read(url);

            // buttons
            url = artLoader.getResource(Constants.ART_LOC + "account.png");
            images[Constants.IMG_ACCOUNT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "accounthighlight.png");
            images[Constants.IMG_ACCOUNT_HIGHLIGHT] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "credits.png");
            images[Constants.IMG_CREDITS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "creditshighlight.png");
            images[Constants.IMG_CREDITS_HIGHLIGHT] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "exit.jpg");
            images[Constants.IMG_EXIT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "exithighlight.jpg");
            images[Constants.IMG_EXIT_HIGHLIGHT] = ImageIO.read(url);

            // tutorial
            url = artLoader.getResource(Constants.ART_LOC + "tutorialBorder.png");
            images[Constants.IMG_TUTORIAL_BORDER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tutorialBack.png");
            images[Constants.IMG_TUTORIAL_BACK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_01.png");
            images[Constants.IMG_TUTORIAL_01] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_03.png");
            images[Constants.IMG_TUTORIAL_03] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_04.png");
            images[Constants.IMG_TUTORIAL_04] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_05.png");
            images[Constants.IMG_TUTORIAL_05] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_06.png");
            images[Constants.IMG_TUTORIAL_06] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_07.png");
            images[Constants.IMG_TUTORIAL_07] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_08.png");
            images[Constants.IMG_TUTORIAL_08] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_10.png");
            images[Constants.IMG_TUTORIAL_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_11.png");
            images[Constants.IMG_TUTORIAL_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_12.png");
            images[Constants.IMG_TUTORIAL_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_13.png");
            images[Constants.IMG_TUTORIAL_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_14.png");
            images[Constants.IMG_TUTORIAL_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_15.png");
            images[Constants.IMG_TUTORIAL_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_16.png");
            images[Constants.IMG_TUTORIAL_16] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_17.png");
            images[Constants.IMG_TUTORIAL_17] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_18.png");
            images[Constants.IMG_TUTORIAL_18] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_19.png");
            images[Constants.IMG_TUTORIAL_19] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_20.png");
            images[Constants.IMG_TUTORIAL_20] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_21.png");
            images[Constants.IMG_TUTORIAL_21] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_22.png");
            images[Constants.IMG_TUTORIAL_22] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_23.png");
            images[Constants.IMG_TUTORIAL_23] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_24.png");
            images[Constants.IMG_TUTORIAL_24] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_25.png");
            images[Constants.IMG_TUTORIAL_25] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_26.png");
            images[Constants.IMG_TUTORIAL_26] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_27.png");
            images[Constants.IMG_TUTORIAL_27] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_28.png");
            images[Constants.IMG_TUTORIAL_28] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_29.png");
            images[Constants.IMG_TUTORIAL_29] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_31.png");
            images[Constants.IMG_TUTORIAL_31] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_32.png");
            images[Constants.IMG_TUTORIAL_32] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_33.png");
            images[Constants.IMG_TUTORIAL_33] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_34.png");
            images[Constants.IMG_TUTORIAL_34] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_35.png");
            images[Constants.IMG_TUTORIAL_35] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_36.png");
            images[Constants.IMG_TUTORIAL_36] = ImageIO.read(url);

            // tutorial buttons
            url = artLoader.getResource(Constants.ART_LOC + "tut_prev.png");
            images[Constants.IMG_PREV] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_prev_highlighted.png");
            images[Constants.IMG_PREV_HIGHLIGHTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_jump_prev.png");
            images[Constants.IMG_JUMP_PREV] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_jump_prev_highlighted.png");
            images[Constants.IMG_JUMP_PREV_HIGHLIGHTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_next.png");
            images[Constants.IMG_NEXT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_next_highlighted.png");
            images[Constants.IMG_NEXT_HIGHLIGHTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_jump_next.png");
            images[Constants.IMG_JUMP_NEXT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_jump_next_highlighted.png");
            images[Constants.IMG_JUMP_NEXT_HIGHLIGHTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_close.png");
            images[Constants.IMG_CLOSE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_close_highlighted.png");
            images[Constants.IMG_CLOSE_HIGHLIGHTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_index.png");
            images[Constants.IMG_INDEX] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tut_index_highlighted.png");
            images[Constants.IMG_INDEX_HIGHLIGHTED] = ImageIO.read(url);

            // Relic Art

            // banish
            url = artLoader.getResource(Constants.ART_LOC + "relic_banish.png");
            images[Constants.IMG_RELIC_BANISH] = ImageIO.read(url);

            // clockwork
            url = artLoader.getResource(Constants.ART_LOC + "relic_clockwork.png");
            images[Constants.IMG_RELIC_CLOCKWORK] = ImageIO.read(url);

            // evasive
            url = artLoader.getResource(Constants.ART_LOC + "relic_evasive.png");
            images[Constants.IMG_RELIC_EVASIVE] = ImageIO.read(url);

            // explode
            url = artLoader.getResource(Constants.ART_LOC + "relic_explode.png");
            images[Constants.IMG_RELIC_EXPLODE] = ImageIO.read(url);

            // flight
            url = artLoader.getResource(Constants.ART_LOC + "relic_flight.png");
            images[Constants.IMG_RELIC_FLIGHT] = ImageIO.read(url);

            // command
            url = artLoader.getResource(Constants.ART_LOC + "relic_gift_unit.png");
            images[Constants.IMG_RELIC_GIFT_UNIT] = ImageIO.read(url);

            // heal'n'move
            url = artLoader.getResource(Constants.ART_LOC + "relic_heal_move.png");
            images[Constants.IMG_RELIC_HEAL_MOVE] = ImageIO.read(url);

            // parry
            url = artLoader.getResource(Constants.ART_LOC + "relic_parry.png");
            images[Constants.IMG_RELIC_PARRY] = ImageIO.read(url);

            // reset
            url = artLoader.getResource(Constants.ART_LOC + "relic_reset.png");
            images[Constants.IMG_RELIC_RESET] = ImageIO.read(url);

            // aegis
            url = artLoader.getResource(Constants.ART_LOC + "relic_spell_block.png");
            images[Constants.IMG_RELIC_SPELL_BLOCK] = ImageIO.read(url);

            // stun
            url = artLoader.getResource(Constants.ART_LOC + "relic_stun.png");
            images[Constants.IMG_RELIC_STUN] = ImageIO.read(url);

            // vampiric
            url = artLoader.getResource(Constants.ART_LOC + "relic_vampire.png");
            images[Constants.IMG_RELIC_VAMPIRE] = ImageIO.read(url);

            // unit stats icons
            url = artLoader.getResource(Constants.ART_LOC + "icon_actions.png");
            images[Constants.IMG_ICON_ACTIONS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "icon_armor.png");
            images[Constants.IMG_ICON_ARMOR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "icon_deploy.png");
            images[Constants.IMG_ICON_DEPLOY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "icon_life.png");
            images[Constants.IMG_ICON_LIFE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "icon_power.png");
            images[Constants.IMG_ICON_POWER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "icon_murderer.png");
            images[Constants.IMG_ICON_MURDERER] = ImageIO.read(url);

            // parry/damage
            url = artLoader.getResource(Constants.ART_LOC + "parry.png");
            images[Constants.IMG_PARRY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mpback.png");
            images[Constants.IMG_MPBACK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "damback.png");
            images[Constants.IMG_DAMBACK] = ImageIO.read(url);

            // splat animations
            url = artLoader.getResource(Constants.ART_LOC + "splat_inorganic1.png");
            images[Constants.IMG_SPLAT_INORGANIC_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_inorganic2.png");
            images[Constants.IMG_SPLAT_INORGANIC_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_inorganic3.png");
            images[Constants.IMG_SPLAT_INORGANIC_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_inorganic4.png");
            images[Constants.IMG_SPLAT_INORGANIC_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_inorganic5.png");
            images[Constants.IMG_SPLAT_INORGANIC_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_organic1.png");
            images[Constants.IMG_SPLAT_ORGANIC_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_organic2.png");
            images[Constants.IMG_SPLAT_ORGANIC_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_organic3.png");
            images[Constants.IMG_SPLAT_ORGANIC_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_organic4.png");
            images[Constants.IMG_SPLAT_ORGANIC_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "splat_organic5.png");
            images[Constants.IMG_SPLAT_ORGANIC_5] = ImageIO.read(url);

            // summon animation
            url = artLoader.getResource(Constants.ART_LOC + "summon1.png");
            images[Constants.IMG_SUMMON_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon2.png");
            images[Constants.IMG_SUMMON_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon3.png");
            images[Constants.IMG_SUMMON_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon4.png");
            images[Constants.IMG_SUMMON_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon5.png");
            images[Constants.IMG_SUMMON_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon6.png");
            images[Constants.IMG_SUMMON_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon7.png");
            images[Constants.IMG_SUMMON_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summon8.png");
            images[Constants.IMG_SUMMON_8] = ImageIO.read(url);

            // summon blue animation
            url = artLoader.getResource(Constants.ART_LOC + "summonblue1.png");
            images[Constants.IMG_SUMMON_BLUE_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue2.png");
            images[Constants.IMG_SUMMON_BLUE_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue3.png");
            images[Constants.IMG_SUMMON_BLUE_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue4.png");
            images[Constants.IMG_SUMMON_BLUE_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue5.png");
            images[Constants.IMG_SUMMON_BLUE_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue6.png");
            images[Constants.IMG_SUMMON_BLUE_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue7.png");
            images[Constants.IMG_SUMMON_BLUE_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "summonblue8.png");
            images[Constants.IMG_SUMMON_BLUE_8] = ImageIO.read(url);

            // lockdown animation
            url = artLoader.getResource(Constants.ART_LOC + "lockdown1.png");
            images[Constants.IMG_LOCKDOWN_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown2.png");
            images[Constants.IMG_LOCKDOWN_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown3.png");
            images[Constants.IMG_LOCKDOWN_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown4.png");
            images[Constants.IMG_LOCKDOWN_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown5.png");
            images[Constants.IMG_LOCKDOWN_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown6.png");
            images[Constants.IMG_LOCKDOWN_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown7.png");
            images[Constants.IMG_LOCKDOWN_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown8.png");
            images[Constants.IMG_LOCKDOWN_8] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown9.png");
            images[Constants.IMG_LOCKDOWN_9] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown10.png");
            images[Constants.IMG_LOCKDOWN_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "lockdown11.png");
            images[Constants.IMG_LOCKDOWN_11] = ImageIO.read(url);

            // tomb lord animation for dracolich
            url = artLoader.getResource(Constants.ART_LOC + "tomblord1.png");
            images[Constants.IMG_TOMB_LORD_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "tomblord2.png");
            images[Constants.IMG_TOMB_LORD_2] = ImageIO.read(url);

            // images for time animation
            url = artLoader.getResource(Constants.ART_LOC + "time1.png");
            images[Constants.IMG_TIME_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "time2.png");
            images[Constants.IMG_TIME_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "time3.png");
            images[Constants.IMG_TIME_3] = ImageIO.read(url);

            // relic rune
            url = artLoader.getResource(Constants.ART_LOC + "relic.png");
            images[Constants.IMG_RELIC] = ImageIO.read(url);

            // mason's wall building animation
            url = artLoader.getResource(Constants.ART_LOC + "build01.png");
            images[Constants.IMG_BUILD_01] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build02.png");
            images[Constants.IMG_BUILD_02] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build03.png");
            images[Constants.IMG_BUILD_03] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build04.png");
            images[Constants.IMG_BUILD_04] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build05.png");
            images[Constants.IMG_BUILD_05] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build06.png");
            images[Constants.IMG_BUILD_06] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build07.png");
            images[Constants.IMG_BUILD_07] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build08.png");
            images[Constants.IMG_BUILD_08] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build09.png");
            images[Constants.IMG_BUILD_09] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build10.png");
            images[Constants.IMG_BUILD_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build11.png");
            images[Constants.IMG_BUILD_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build12.png");
            images[Constants.IMG_BUILD_12] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build13.png");
            images[Constants.IMG_BUILD_13] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build14.png");
            images[Constants.IMG_BUILD_14] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build15.png");
            images[Constants.IMG_BUILD_15] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build16.png");
            images[Constants.IMG_BUILD_16] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build17.png");
            images[Constants.IMG_BUILD_17] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build18.png");
            images[Constants.IMG_BUILD_18] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build19.png");
            images[Constants.IMG_BUILD_19] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build20.png");
            images[Constants.IMG_BUILD_20] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build21.png");
            images[Constants.IMG_BUILD_21] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build22.png");
            images[Constants.IMG_BUILD_22] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build23.png");
            images[Constants.IMG_BUILD_23] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build24.png");
            images[Constants.IMG_BUILD_24] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build25.png");
            images[Constants.IMG_BUILD_25] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build26.png");
            images[Constants.IMG_BUILD_26] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build27.png");
            images[Constants.IMG_BUILD_27] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build28.png");
            images[Constants.IMG_BUILD_28] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build29.png");
            images[Constants.IMG_BUILD_29] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build30.png");
            images[Constants.IMG_BUILD_30] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build31.png");
            images[Constants.IMG_BUILD_31] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build32.png");
            images[Constants.IMG_BUILD_32] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build33.png");
            images[Constants.IMG_BUILD_33] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "build34.png");
            images[Constants.IMG_BUILD_34] = ImageIO.read(url);

            // powerup art
            url = artLoader.getResource(Constants.ART_LOC + "powerup_toxic.png");
            images[Constants.IMG_POWERUP_TOXIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_evasive.png");
            images[Constants.IMG_POWERUP_EVASIVE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_resilient.png");
            images[Constants.IMG_POWERUP_RESILIENT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_longshank.png");
            images[Constants.IMG_POWERUP_LONGSHANK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_mighty.png");
            images[Constants.IMG_POWERUP_MIGHTY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_clockwork.png");
            images[Constants.IMG_POWERUP_CLOCKWORK] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_vampiric.png");
            images[Constants.IMG_POWERUP_VAMPIRIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_cunning.png");
            images[Constants.IMG_POWERUP_CUNNING] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_epic.png");
            images[Constants.IMG_POWERUP_EPIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_arcane.png");
            images[Constants.IMG_POWERUP_ARCANE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_ascendant.png");
            images[Constants.IMG_POWERUP_ASCENDANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_guardian.png");
            images[Constants.IMG_POWERUP_GUARDIAN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_vigilant.png");
            images[Constants.IMG_POWERUP_VIGILANT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_zealous.png");
            images[Constants.IMG_POWERUP_ZEALOUS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_rampaging.png");
            images[Constants.IMG_POWERUP_RAMPAGING] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_ruthless.png");
            images[Constants.IMG_POWERUP_RUTHLESS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "powerup_enraged.png");
            images[Constants.IMG_POWERUP_ENRAGED] = ImageIO.read(url);

            // PLayer Panel Stuff
            url = artLoader.getResource(Constants.ART_LOC + "PlayerPanel.jpg");
            images[Constants.IMG_PLAYERPANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chat_line.png");
            images[Constants.IMG_CHAT_LINE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chat_up.png");
            images[Constants.IMG_CHAT_UP] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chat_down.png");
            images[Constants.IMG_CHAT_DOWN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chat.png");
            images[Constants.IMG_CHAT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "players.png");
            images[Constants.IMG_PLAYERS] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "scroll.png");
            images[Constants.IMG_SCROLL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "scroll_player.png");
            images[Constants.IMG_SCROLL_PLAYER] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "scroll_bar.png");
            images[Constants.IMG_SCROLL_BAR] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "players_button.png");
            images[Constants.IMG_PLAYERS_BUTTON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "chat_button.png");
            images[Constants.IMG_CHAT_BUTTON] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "bones.png");
            images[Constants.IMG_BONES] = ImageIO.read(url);


            // edit army stuff
            url = artLoader.getResource(Constants.ART_LOC + "newunitdown.jpg");
            images[Constants.IMG_NEW_UNIT_DOWN] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunitdownhighlight.jpg");
            images[Constants.IMG_NEW_UNIT_DOWN_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunitup.jpg");
            images[Constants.IMG_NEW_UNIT_UP] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunituphighlight.jpg");
            images[Constants.IMG_NEW_UNIT_UP_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editgold.jpg");
            images[Constants.IMG_EDIT_GOLD] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "editstatpanel.jpg");
            images[Constants.IMG_EDIT_STAT_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editcategorypanel.jpg");
            images[Constants.IMG_EDIT_CATEGORY_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editunitspanel.jpg");
            images[Constants.IMG_EDIT_UNITS_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editcastlepanel.jpg");
            images[Constants.IMG_EDIT_CASTLE_PANEL] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editstatbox.jpg");
            images[Constants.IMG_EDIT_STAT_BOX] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "editadd.jpg");
            images[Constants.IMG_EDIT_ADD] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editaddhighlight.jpg");
            images[Constants.IMG_EDIT_ADD_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editaddalert.jpg");
            images[Constants.IMG_EDIT_ADD_ALERT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editbutton.jpg");
            images[Constants.IMG_EDIT_BUTTON] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "editbuttonhighlight.jpg");
            images[Constants.IMG_EDIT_BUTTON_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunit.jpg");
            images[Constants.IMG_NEW_UNIT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunitdisabled.jpg");
            images[Constants.IMG_NEW_UNIT_DISABLED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunitselected.jpg");
            images[Constants.IMG_NEW_UNIT_SELECTED] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "newunithighlight.jpg");
            images[Constants.IMG_NEW_UNIT_HIGHLIGHT] = ImageIO.read(url);

            // etc
            url = artLoader.getResource(Constants.ART_LOC + "backpaneledit.jpg");
            images[Constants.IMG_BACK_PANEL_EDIT] = ImageIO.read(url);

            // coop
            url = artLoader.getResource(Constants.ART_LOC + "cooperative.png");
            images[Constants.IMG_COOPERATIVE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "cooperativered.png");
            images[Constants.IMG_COOPERATIVE_RED] = ImageIO.read(url);


            // mute
            url = artLoader.getResource(Constants.ART_LOC + "mute.png");
            images[Constants.IMG_MUTE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "mutehighlight.png");
            images[Constants.IMG_MUTE_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "muted.png");
            images[Constants.IMG_MUTED] = ImageIO.read(url);

            // music
            url = artLoader.getResource(Constants.ART_LOC + "music.png");
            images[Constants.IMG_MUSIC] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "musichighlight.png");
            images[Constants.IMG_MUSIC_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "musicoff.png");
            images[Constants.IMG_MUSIC_OFF] = ImageIO.read(url);

            // new url buttons
            url = artLoader.getResource(Constants.ART_LOC + "forum.png");
            images[Constants.IMG_FORUM] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "forumhighlight.png");
            images[Constants.IMG_FORUM_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "blog.png");
            images[Constants.IMG_BLOG] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "bloghighlight.png");
            images[Constants.IMG_BLOG_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "guide.png");
            images[Constants.IMG_GUIDE] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "guidehighlight.png");
            images[Constants.IMG_GUIDE_HIGHLIGHT] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "referfriend.png");
            images[Constants.IMG_REFER_FRIEND] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "referfriendhighlight.png");
            images[Constants.IMG_REFER_FRIEND_HIGHLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "topscores.png");
            images[Constants.IMG_SCORES] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "topscoreshighlight.png");
            images[Constants.IMG_SCORES_HIGHLIGHT] = ImageIO.read(url);

            // advertisement
            url = artLoader.getResource(Constants.ART_LOC + "ad.jpg");
            images[Constants.IMG_AD] = ImageIO.read(url);

            // gold border
            url = artLoader.getResource(Constants.ART_LOC + "border.png");
            images[Constants.IMG_BORDER] = ImageIO.read(url);

            // splotlight
            url = artLoader.getResource(Constants.ART_LOC + "spotlight.png");
            images[Constants.IMG_SPOTLIGHT] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "blacklight.png");
            images[Constants.IMG_BLACKLIGHT] = ImageIO.read(url);

            // team
            url = artLoader.getResource(Constants.ART_LOC + "team.png");
            images[Constants.IMG_TEAM] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "teamhighlight.png");
            images[Constants.IMG_TEAM_HIGHLIGHT] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "team_notready.png");
            images[Constants.IMG_TEAM_NOT_READY] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "sword_1.png");
            images[Constants.IMG_SWORD_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "sword_2.png");
            images[Constants.IMG_SWORD_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "teams_box.png");
            images[Constants.IMG_TEAMS_BOX] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "teams_player.png");
            images[Constants.IMG_TEAMS_PLAYER] = ImageIO.read(url);

            // clash animation
            url = artLoader.getResource(Constants.ART_LOC + "clash1.png");
            images[Constants.IMG_CLASH_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash2.png");
            images[Constants.IMG_CLASH_2] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash3.png");
            images[Constants.IMG_CLASH_3] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash4.png");
            images[Constants.IMG_CLASH_4] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash5.png");
            images[Constants.IMG_CLASH_5] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash6.png");
            images[Constants.IMG_CLASH_6] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash7.png");
            images[Constants.IMG_CLASH_7] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "clash8.png");
            images[Constants.IMG_CLASH_8] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "target01.png");
            images[Constants.IMG_TARGET_01] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target02.png");
            images[Constants.IMG_TARGET_02] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target03.png");
            images[Constants.IMG_TARGET_03] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target04.png");
            images[Constants.IMG_TARGET_04] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target05.png");
            images[Constants.IMG_TARGET_05] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target06.png");
            images[Constants.IMG_TARGET_06] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target07.png");
            images[Constants.IMG_TARGET_07] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target08.png");
            images[Constants.IMG_TARGET_08] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target09.png");
            images[Constants.IMG_TARGET_09] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target10.png");
            images[Constants.IMG_TARGET_10] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target11.png");
            images[Constants.IMG_TARGET_11] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "target12.png");
            images[Constants.IMG_TARGET_12] = ImageIO.read(url);

            url = artLoader.getResource(Constants.ART_LOC + "shield1.png");
            images[Constants.IMG_SHIELD_1] = ImageIO.read(url);
            url = artLoader.getResource(Constants.ART_LOC + "shield2.png");
            images[Constants.IMG_SHIELD_2] = ImageIO.read(url);
  
  /*
  url = artLoader.getResource(Constants.ART_LOC + "endGameScreenVictory.jpg");
  images[Constants.IMG_END_GAME_VICTORY] = ImageIO.read(url);
  url = artLoader.getResource(Constants.ART_LOC + "endGameScreenDefeat.jpg");
  images[Constants.IMG_END_GAME_DEFEAT] = ImageIO.read(url);
  */

            // All done
            //System.out.println("Images loaded.");

        } catch (Exception e) {
            System.out.println("load " + url + e);
        }
        artLoaded = true;
    }


    /////////////////////////////////////////////////////////////////
    // Get the progress
    /////////////////////////////////////////////////////////////////
    public String getProgress() {
  /*float max = Constants.SOUND_COUNT*10;
  max+=Constants.IMG_COUNT*2;
  float sofar;
  
  
  if (!soundLoaded)
  { sofar = 0;
   int count = 0;
                        while(count < Constants.SOUND_COUNT && sounds[count] != null)
                                count++;
   sofar+=count*10;
   //return "Loading sounds: " + count + "/" + Constants.SOUND_COUNT;

  } else if (!artLoaded)
  { int count = 0;
   sofar = Constants.SOUND_COUNT*10;
   while(count < Constants.IMG_COUNT && images[count] != null)
   { 
     count++;
   }
   sofar+=count;
   
   // tmp
   //return "Loading images: " + count + "/" + Constants.IMG_COUNT;

  } else
  { sofar = Constants.SOUND_COUNT*10;
   sofar+=Constants.IMG_COUNT;
   int count = 0;
   while(count < Constants.IMG_COUNT && rotatedImages[count] != null)
   
    count++;
   sofar+=count;
   
   // tmp
   //return "Processing images: " + count + "/" + Constants.IMG_COUNT;
  }*/

        float max = 0;
        max += Constants.SOUND_COUNT * 10;
        max += Constants.IMG_COUNT;
        float sofar = 0;

        for (int i = 0; i < Constants.SOUND_COUNT; i++) {
            if (sounds[i] != null) sofar += 10;
        }
        for (int i = 0; i < Constants.IMG_COUNT; i++) {
            if (images[i] != null) sofar++;
        }

        float percent = max / sofar;
        percent = 100 / percent;
        int value = (int) percent;
        return "Loading game data: " + value + "%";

    }


    /////////////////////////////////////////////////////////////////
    // Rotate the images
    /////////////////////////////////////////////////////////////////
    public void initialize() {
        int debug = 0;
        try {
            GraphicsConfiguration gc = GraphicsEnvironment.
                    getLocalGraphicsEnvironment()
                    .getDefaultScreenDevice()
                    .getDefaultConfiguration();

            for (int i = 0; i < Constants.IMG_COUNT; i++) {
                debug = i;
                Image image = getImage(i);

                if (image == null) {
                    images[i] = images[0];
                } else {
                    BufferedImage buf = new
                            BufferedImage(image.getWidth(null),
                            image.getHeight(null),
                            BufferedImage.TYPE_INT_ARGB);

                    buf.getGraphics().drawImage(image, 0, 0, null);

                    images[i] = copy(buf);
                    image = getImage(i);

                    //if (image.getCapabilities(gc).isAccelerated())
                    // System.out.println(i + " is");
                    //else
                    // System.out.println(i + " isn't");

    /*if (image.getWidth(null) == Constants.SQUARE_SIZE)
    {
     rotatedImages[i] = rotate(buf);
     if (rotatedImages[i] == null)
     { rotatedImages[i] = images[0];
     }
    } else
    {
     rotatedImages[i] = images[0];
    }*/
                }
            }

        } catch (Exception e) {
            System.out.println("Initialize: " + debug + " " + e);
        }
    }


    private BufferedImage rotate(BufferedImage bi) {
        //System.out.println("rotating");
        int width = bi.getWidth();
        int height = bi.getHeight();

        //BufferedImage biFlip = new BufferedImage(height, width, bi.getType());
        BufferedImage biFlip = new BufferedImage(height, width, BufferedImage.TYPE_INT_ARGB);

        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++)
                biFlip.setRGB(height - 1 - y, x, bi.getRGB(x, y));
        return biFlip;
    }

/*
 /////////////////////////////////////////////////////////////////
 // Copy the image
 /////////////////////////////////////////////////////////////////
 private Image copy(BufferedImage bi)
 {
  int width = bi.getWidth();
  int height = bi.getHeight();
  
  BufferedImage biCopy = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

  for(int x=0; x<width; x++)
   for(int y=0; y<height; y++)
    biCopy.setRGB(x, y, bi.getRGB(x, y));
  return biCopy;
 }
 */

    //////////////////////////////////////////////////////////////////////
//Get a copy
//////////////////////////////////////////////////////////////////////
    private BufferedImage copy(BufferedImage img) {
        try {
            //Create an RGB buffered image
            BufferedImage bimage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_4BYTE_ABGR_PRE);

            //copy it
            Graphics2D g2 = bimage.createGraphics();
            g2.drawImage(img, 0, 0, null);
            g2.dispose();

            for (int x = 0; x < bimage.getWidth(); x++) {
                for (int y = 0; y < bimage.getHeight(); y++) {
                    bimage.setRGB(x, y, img.getRGB(x, y));
                }
            }
            return bimage;
        } catch (Exception e) {
            System.out.println("GameMedia.copy(): " + e);
            return null;
        }
    }


    /////////////////////////////////////////////////////////////////
    // Play music
    /////////////////////////////////////////////////////////////////
    public void playMusic() {
        try {
            if (!Client.mute() && !Client.musicOff()) music.play();
        } catch (Exception e) {
            System.out.println("GameMedia.playMusic(): " + e);
        }
    }

    public void stopMusic() {
        try {
            if (!Client.mute() && !Client.musicOff()) music.stop();
        } catch (Exception e) {
            System.out.println("GameMedia.stopMusic(): " + e);
        }
    }

    public void pauseMusic() {
        try {
            music.pause();
        } catch (Exception e) {
            System.out.println("GameMedia.pauseMusic(): " + e);
        }
    }

    public void resumeMusic() {
        try {
            music.resume();
        } catch (Exception e) {
            System.out.println("GameMedia.resumeMusic(): " + e);
        }
    }

    public void clean() {
        try {
            for (int i = 0; i < images.length; i++) {
                if (images[i] != null) images[i].flush();
            }
            for (int i = 0; i < rotatedImages.length; i++) {
                if (rotatedImages[i] != null) rotatedImages[i].flush();
            }

            music.close();
        } catch (Exception e) {
            System.out.println("GameMedia.clean(): " + e);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Play a sound
    /////////////////////////////////////////////////////////////////
    public void playSound(short sound) {
        if (!Client.mute() && sounds[sound] != null)
            sounds[sound].play();
    }

    public Sound getSound(short index) {
        return sounds[index];
    }

    /////////////////////////////////////////////////////////////////
    // Get a particular image, also checks for gray images
    /////////////////////////////////////////////////////////////////
    public Image getImage(int i) { //System.out.println(
        // GraphicsEnvironment
        // .getLocalGraphicsEnvironment()
        // .getDefaultScreenDevice()
        // .getAvailableAcceleratedMemory() + " mem");

  /*if (images[i] == null) // This means it is a gray image not yet loaded
  {
   if (images[i - 2] == null) return null;  // Checks if spot is supposed to be null
   BufferedImage new_image = generateShadow(i - 2);
   images[i] = copy(new_image);
  }*/
        return images[i];
    }

    private BufferedImage generateShadow(int image) {
        // Get non-RGB image
        Image img = images[image];

        // Create an RGB buffered image
        BufferedImage bimage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        // copy it
        Graphics2D g2 = bimage.createGraphics();
        g2.drawImage(img, 0, 0, null);
        g2.dispose();

        // color magic
        for (int x = 0; x < bimage.getWidth(); x++) {
            for (int y = 0; y < bimage.getHeight(); y++) {
                int pixel = bimage.getRGB(x, y);
                int a = (pixel >> 24) & 0xff;
                int r = (pixel >> 16) & 0xff;
                int g = (pixel >> 8) & 0xff;
                int b = (pixel >> 0) & 0xff;
                int greyConstant = (int) (((double) r * .299 + (double) g * .587 + (double) b * .114) * .8);
                r = greyConstant;
                g = greyConstant;
                b = greyConstant;
                int newPixel =
                        ((a & 0xFF) << 24) |
                                ((r & 0xFF) << 16) |
                                ((g & 0xFF) << 8) |
                                ((b & 0xFF) << 0);
                bimage.setRGB(x, y, newPixel);
            }
        }
        return bimage;
    }

    public Image getGrayedImage(int image, boolean friendly) {
        if (!friendly)
            ++image;
        if (grayedImages[image] == null) {
            BufferedImage new_image = generateShadow(image);
            grayedImages[image] = copy(new_image);
        }
        return grayedImages[image];
    }

    /////////////////////////////////////////////////////////////////
    // Get a prerotated image
    /////////////////////////////////////////////////////////////////
    public Image getRotatedImage(int image, boolean friendly) {
        if (rotatedImages[image] == null) {
            Image img = null;
            if (friendly) img = getGrayedImage(image, true);
            else img = getImage(image);
            if (img.getWidth(null) == Constants.SQUARE_SIZE) {
                BufferedImage buf = new
                        BufferedImage(img.getWidth(null),
                        img.getHeight(null),
                        BufferedImage.TYPE_INT_ARGB);
                buf.getGraphics().drawImage(img, 0, 0, null);
                rotatedImages[image] = rotate(buf);
                if (rotatedImages[image] == null) {
                    System.out.println("rotate(buf) returned null.");
                    rotatedImages[image] = images[0];
                }
            } else {
                System.out.println("getWidth (which is: " + img.getWidth(null) + ") != square size.");
                rotatedImages[image] = images[0];
            }
        }
        return rotatedImages[image];
    }


    public void soundStarted() {
        ++soundCount;
    }

    public void soundFinished() {
        --soundCount;
    }

    public boolean belowMaxSounds() {
        return soundCount < MAX_SOUND_COUNT;
    }
}
