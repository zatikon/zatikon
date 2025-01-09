//all working except popping sound, loading as file instead of urls
///////////////////////////////////////////////////////////////////////
//      Name:   Sound.java
//      Desc:   Play a happy, buffered sound
//      Date:   2/4/2003 - Gabe Jones
//      TODO:
///////////////////////////////////////////////////////////////////////
package leo.client;

import leo.shared.Log;
import org.tinylog.Logger;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALC10;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;
import java.io.BufferedInputStream;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public class Sound implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static long device;
    private static long context;
    
    private byte[] buffer;
    private int playcount;
    private int bufferID;
    private int sourceID;
    //private boolean isPlaying;
    private AudioFormat format;
    private int sampleRate;
    private String soundName;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public Sound(URL url) {
        this.soundName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
        Logger.error("Sound(): construct: " + this.soundName);
        try {
            // Load the audio data from the JAR file
            try (AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new BufferedInputStream(getClass().getResourceAsStream(
                            url.getPath().substring(url.getPath().lastIndexOf("!") + 1))))) {
                if (audioStream == null) {
                    throw new IOException("Resource not found: " + url.getPath().substring(url.getPath().lastIndexOf("!") + 1));
                }

                // Get the format of the audio
                format = audioStream.getFormat();
                sampleRate = (int) format.getSampleRate();

                // Read the audio data into a byte array
                buffer = audioStream.readAllBytes();

                Logger.info("Sound(): Audio loaded: Format = " + format + ", Buffer size = " + buffer.length);
            } catch (UnsupportedAudioFileException | IOException e) {
                Logger.error("Error loading audio: " + e);
            }

            // If OpenAL isn't initialized yet, initialize it
            if (device == 0) {
                initOpenAL();
            }

            // Load the sound into a buffer
            loadSound();

        } catch (Exception e) {
            Logger.error("Sound(): " + e);
        }
    }

    private void initOpenAL() {
        Logger.error("Sound(): Initialize OpenAL");
        // Initialize OpenAL context only once
        try (MemoryStack stack = MemoryStack.stackPush()) {
            // Get default device name
            String deviceName = ALC10.alcGetString(0, ALC10.ALC_DEFAULT_DEVICE_SPECIFIER);

            // Open the device
            device = ALC10.alcOpenDevice(deviceName);
            if (device == MemoryUtil.NULL) {
                throw new IllegalStateException("Failed to open the default OpenAL device.");
            }

            // Create the context
            context = ALC10.alcCreateContext(device, (IntBuffer) null);
            ALC10.alcMakeContextCurrent(context);
            AL.createCapabilities(ALC.createCapabilities(device));

            Logger.info("OpenAL initialized.");
        } catch (Exception e) {
            Logger.error("Failed to initialize OpenAL: " + e);
        }
    }

    private void loadSound() {
        // Generate a new buffer for this sound
        bufferID = AL10.alGenBuffers();

        // Determine the OpenAL format based on the audio data
        int alFormat = getALFormat(format);

        // Load the audio data into OpenAL buffer
        ByteBuffer byteBuffer = MemoryUtil.memAlloc(buffer.length);
        byteBuffer.put(buffer).flip();
        AL10.alBufferData(bufferID, alFormat, byteBuffer, sampleRate);

        //alBufferData(bufferID, alFormat, MemoryUtil.memAlloc(buffer.length).put(buffer).flip(), sampleRate);

        // Check for OpenAL errors
        int error = AL10.alGetError();
        if (error != AL10.AL_NO_ERROR) {
            Logger.error("OpenAL Error during buffer loading: " + error);
        }

        // Create a new source for this sound
        sourceID = AL10.alGenSources();
        AL10.alSourcei(sourceID, AL10.AL_BUFFER, bufferID);

        // Turn off positional sound
        AL10.alSourcei(sourceID, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);

        Logger.info("Sound(): buffered: " + this.soundName + " rate " + sampleRate);
    }

    private int getALFormat(AudioFormat format) {
        // Determine the OpenAL format based on the AudioFormat
        boolean isSigned = format.getEncoding() == AudioFormat.Encoding.PCM_SIGNED;
        int channels = format.getChannels();
        int sampleSizeInBits = format.getSampleSizeInBits();

        if (channels == 1) { // Mono
            if (sampleSizeInBits == 8) {
                return AL10.AL_FORMAT_MONO8; // OpenAL treats all 8-bit as unsigned
            } else if (sampleSizeInBits == 16) {
                if (isSigned) {
                    return AL10.AL_FORMAT_MONO16;
                } else {
                    throw new IllegalArgumentException("16-bit unsigned is not supported.");
                }
            }
        } else if (channels == 2) { // Stereo
            if (sampleSizeInBits == 8) {
                return AL10.AL_FORMAT_STEREO8; // OpenAL treats all 8-bit as unsigned
            } else if (sampleSizeInBits == 16) {
                if (isSigned) {
                    return AL10.AL_FORMAT_STEREO16;
                } else {
                    throw new IllegalArgumentException("16-bit unsigned is not supported.");
                }
            }
        }

        throw new IllegalArgumentException("Unsupported audio format: " + format);
    }

    /////////////////////////////////////////////////////////////////
    // Play the sound
    /////////////////////////////////////////////////////////////////
    public void play() {
        try {
            Logger.info("Sound(): playing: " + this.soundName + " playcount: " + playcount + "/" + Client.getImages().getSoundCount());
            if (playcount < GameMedia.MAX_SOUND_INDIVIDUAL && Client.getImages().belowMaxSounds()) {
                Thread runner = new Thread(this, "SoundPlayThread-" + System.currentTimeMillis());
                runner.start();
                ++playcount;
                Client.getImages().soundStarted();
            }

        } catch (Exception e) {
            Logger.error("Sound.play(): " + e);
        }
    }

    /////////////////////////////////////////////////////////////////
    // Run the sound playback in a new thread
    /////////////////////////////////////////////////////////////////
    public void run() {
        try {
            // If already playing, stop the sound and reset the source
            /*
            if (isPlaying) {
                Logger.info("Sound(): is already playing" + this.soundName);
                // Stop the source and reset if necessary
                alSourceStop(sourceID);
                alSourceRewind(sourceID);
                isPlaying = false;
            }*/

            // Play the sound
            AL10.alSourcePlay(sourceID);
            //isPlaying = true;

            // Wait for the sound to finish
            while (AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
                Thread.sleep(10);
            }

            // Clean up after playing the sound
            AL10.alSourceStop(sourceID);
            AL10.alSourceRewind(sourceID);
            --playcount;
            Client.getImages().soundFinished();

        } catch (Exception e) {
            Logger.error("Sound.run(): " + e);
        }
    }
}
