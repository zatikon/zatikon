///////////////////////////////////////////////////////////////////////
//      Name:   OggClip.java
//      Desc:   Play an Ogg through OpenAL
//      Date:   1/9/25
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

public class OggClip implements Runnable {

    /////////////////////////////////////////////////////////////////
    // Properties
    /////////////////////////////////////////////////////////////////
    private static long device;
    private static long context;
    
    private byte[] buffer;
    private int bufferID;
    private int sourceID;
    private AudioFormat format;
    private int sampleRate;
    private String soundName;

    /////////////////////////////////////////////////////////////////
    // Constructor
    /////////////////////////////////////////////////////////////////
    public OggClip(URL url) {
        this.soundName = url.getPath().substring(url.getPath().lastIndexOf("/") + 1);
        //Logger.info("OggClip(); construct: " + this.soundName);
        try {
            // Load the audio data from the JAR file
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new BufferedInputStream(getClass().getResourceAsStream(url.getPath().substring(url.getPath().lastIndexOf("!") + 1))));
            if (audioStream == null) {
                throw new IOException("Resource not found: " + url.getPath().substring(url.getPath().lastIndexOf("!") + 1));
            }

            // Get the format of the audio
            format = audioStream.getFormat();

            if(soundName.toLowerCase().endsWith(".ogg")) {
                AudioFormat targetFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, format.getSampleRate(), 16, format.getChannels(), format.getChannels() * 2, format.getSampleRate(), false);
                audioStream = AudioSystem.getAudioInputStream(targetFormat, audioStream);
                format = audioStream.getFormat();
            }

            sampleRate = (int) format.getSampleRate();

            // Read the audio data into a byte array
            buffer = audioStream.readAllBytes();

            //Logger.info("OggClip(); Audio loaded: Format = " + format + ", Buffer size = " + buffer.length);

            // If OpenAL isn't initialized yet, initialize it
            if (device == 0) {
                initOpenAL();
            }

            // Load the sound into a buffer
            loadSound();

        } catch (Exception e) {
            Logger.error("OggClip(); " + e);
        }
    }

    private void initOpenAL() {
        Logger.error("OggClip(); Initialize OpenAL");
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
        
        //set to loop
        AL10.alSourcei(sourceID, AL10.AL_LOOPING, AL10.AL_TRUE);

        // Turn off positional sound
        AL10.alSourcei(sourceID, AL10.AL_SOURCE_RELATIVE, AL10.AL_TRUE);

        //Logger.info("OggClip(); buffered: " + this.soundName + " rate " + sampleRate);
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
            Logger.info("OggClip(); playing: " + this.soundName);
            Thread runner = new Thread(this, "MusicPlayThread-" + System.currentTimeMillis());
            runner.start();
        } catch (Exception e) {
            Logger.error("Sound.play(): " + e);
        }
    }

    public void run() {
        try {
            // Play the sound
            AL10.alSourcePlay(sourceID);

            // Wait for the sound to finish
            while (AL10.alGetSourcei(sourceID, AL10.AL_SOURCE_STATE) == AL10.AL_PLAYING) {
                Thread.sleep(100);
            }

            // Clean up after playing the sound
            AL10.alSourceStop(sourceID);
            //AL10.alDeleteSources(sourceID);
        } catch (Exception e) {
            Logger.error("Sound.run(): " + e);
        }
    }

    public void pause() {
        Logger.info("pausing ogg");
        AL10.alSourcePause(sourceID);
    }

    public void resume() {
        Logger.info("resume ogg");
        AL10.alSourcePlay(sourceID);
    }

    public void stop() {
        Logger.info("stopping ogg");
        AL10.alSourceStop(sourceID);
        AL10.alSourceRewind(sourceID);
    }

    public void close() {
        Logger.info("closing ogg");
        AL10.alSourceStop(sourceID);
        AL10.alDeleteSources(sourceID);
    }
    public void setVolume(int volume) {
        AL10.alSourcef(sourceID, AL10.AL_GAIN, volume / 5.0f);
    }   

}
