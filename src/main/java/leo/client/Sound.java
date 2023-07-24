///////////////////////////////////////////////////////////////////////
//      Name:	Sound.java
//      Desc:	Play a happy, buffered sound
//      Date:	2/4/2003 - Gabe Jones
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

// imports

import leo.shared.Log;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;


public class Sound implements LineListener, Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    //private Clip clip;
    private AudioFormat format;
    private byte[] buffer;
    private int playcount;


    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Sound(URL url) {
        try {
            // Get the format
            format = AudioSystem.getAudioFileFormat(url).getFormat();

            // Get a shiny new clip
            Clip clip = AudioSystem.getClip();

            var stream = url.openStream();
            buffer = stream.readAllBytes();

            // read the audio input stream into a short array
            AudioInputStream ais = AudioSystem.getAudioInputStream(url.openStream());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            int fs = format.getFrameSize();
            byte[] buf = new byte[fs];
            int size;
            while ((size = ais.read(buf)) != -1 && !Client.shuttingDown()) {
                baos.write(buf, 0, size);
            }
            buffer = baos.toByteArray();

            // Add the close event
            clip.addLineListener(this);

            // close the streams
            baos.close();
            ais.close();

            playcount = 0;
        } catch (Exception e) {
            System.out.println("Sound(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Play the sound
    /////////////////////////////////////////////////////////////////
    public void play() {
        try {
            //if (clip.isActive())
            //{	clip.stop();
            //	if (clip.isOpen()) clip.close();
            //}
            if (playcount < GameMedia.MAX_SOUND_INDIVIDUAL && Client.getImages().belowMaxSounds()) {
                Thread runner = new Thread(this);
                runner.start();
                ++playcount;
                Client.getImages().soundStarted();
            }

        } catch (Exception e) {
            System.out.println("Sound.play(): " + e);
        }
    }


    /////////////////////////////////////////////////////////////////
    // Play the sound
    /////////////////////////////////////////////////////////////////
    public void run() {
        try {
            Clip clip = AudioSystem.getClip();
            ByteArrayInputStream bais = new ByteArrayInputStream(buffer);
            AudioInputStream ais = new AudioInputStream(bais, format, buffer.length);
            clip.open(ais);
            clip.start();
            //clip.drain();
            long sleepMillis = clip.getMicrosecondLength()/1000 + 10; // some buffer

            Thread.sleep(sleepMillis);

            clip.flush();
            clip.close();
            ais.close();
            bais.close();
            --playcount;
            Client.getImages().soundFinished();
        } catch (Exception e) {
            System.out.println("Sound.run(): " + e);
        }

    }


    /////////////////////////////////////////////////////////////////
    // Event
    /////////////////////////////////////////////////////////////////
    public void update(LineEvent lineEvent) {    //try
        //{
        //	if ( lineEvent.getType() == LineEvent.Type.STOP)
        //	{	if (lineEvent.getLine().isOpen())
        //			lineEvent.getLine().close();
        //	}
        //} catch(Exception e)
        //{       System.out.println(e);
        //}
    }


}
