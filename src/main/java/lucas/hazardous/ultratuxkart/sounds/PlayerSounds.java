package lucas.hazardous.ultratuxkart.sounds;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class PlayerSounds {
    private final Clip engineClip;

    public PlayerSounds() {
        try {
            InputStream file =
                    Objects.requireNonNull(
                            this.getClass().getClassLoader().getResourceAsStream("sounds/engine.wav"));
            InputStream inputStream = new BufferedInputStream(file);

            engineClip = AudioSystem.getClip();
            engineClip.open(AudioSystem.getAudioInputStream(inputStream));
            engineClip.loop(Clip.LOOP_CONTINUOUSLY);
            engineClip.stop();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isEngineSoundRunning() {
        return engineClip.isRunning();
    }

    public void resumeEngineSound() {
        engineClip.setFramePosition(0);
        engineClip.start();
    }

    public void stopPlayingEngineSound() {
        engineClip.stop();
    }
}
