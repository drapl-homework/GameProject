//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.base.engine.core;

import java.io.IOException;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.AudioFormat.Encoding;

public class SoundClip {
    private Clip clip;

    public SoundClip(String s) {
        try {
            AudioInputStream e = AudioSystem.getAudioInputStream(SoundClip.class.getResourceAsStream(s));
            AudioFormat baseFormat = e.getFormat();
            AudioFormat decodeFormat = new AudioFormat(Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            AudioInputStream dais = AudioSystem.getAudioInputStream(decodeFormat, e);
            this.clip = AudioSystem.getClip();
            this.clip.open(dais);
        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException var6) {
            var6.printStackTrace();
        }

    }

    public void play() {
        if(this.clip != null) {
            this.stop();
            this.clip.setFramePosition(0);

            while(!this.clip.isRunning()) {
                this.clip.start();
            }

        }
    }

    public void stop() {
        if(this.clip.isRunning()) {
            this.clip.stop();
        }

    }

    public void close() {
        this.stop();
        this.clip.close();
    }
}
