package lucas.hazardous.ultratuxkart.sounds;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

import static lucas.hazardous.ultratuxkart.sounds.SoundFileLoader.getSoundFile;

public class MenuSounds {
    private final Clip menuClip;

    public MenuSounds() {
        try {
            menuClip = AudioSystem.getClip();
            menuClip.open(getSoundFile("sounds/menu.wav"));
            menuClip.setFramePosition(0);
            menuClip.start();
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void stopMenuSound() {
        menuClip.stop();
    }
}
