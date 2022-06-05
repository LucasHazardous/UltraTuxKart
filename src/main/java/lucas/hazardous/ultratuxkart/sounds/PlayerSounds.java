package lucas.hazardous.ultratuxkart.sounds;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.Objects;

public class PlayerSounds {
    private final Clip engineClip;

    public PlayerSounds() {
        try {
            AudioInputStream file = AudioSystem.getAudioInputStream(
                    Objects.requireNonNull(
                            this.getClass().getClassLoader().getResourceAsStream("sounds/engine.wav")));
            engineClip = AudioSystem.getClip();
            engineClip.open(file);
            engineClip.loop(Clip.LOOP_CONTINUOUSLY);
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
