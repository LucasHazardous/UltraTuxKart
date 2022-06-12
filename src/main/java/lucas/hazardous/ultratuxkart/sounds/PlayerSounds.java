package lucas.hazardous.ultratuxkart.sounds;

import javax.sound.sampled.*;
import java.io.IOException;

import static lucas.hazardous.ultratuxkart.sounds.SoundFileLoader.getSoundFile;

public class PlayerSounds {
    private Clip engineClip;
    private Clip boostClip;

    public PlayerSounds() {
        try {
            loadEngineClip();
            loadBoostClip();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadEngineClip() throws LineUnavailableException, UnsupportedAudioFileException, IOException {
        engineClip = AudioSystem.getClip();
        engineClip.open(getSoundFile("sounds/engine.wav"));
    }

    private void loadBoostClip() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        boostClip = AudioSystem.getClip();
        boostClip.open(getSoundFile("sounds/boost.wav"));
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

    public void playBoost() {
        boostClip.setFramePosition(0);
        boostClip.start();
    }
}
