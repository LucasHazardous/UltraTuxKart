package lucas.hazardous.ultratuxkart.sounds;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class SoundFileLoader {
    public static AudioInputStream getSoundFile(String path) throws UnsupportedAudioFileException, IOException {
        InputStream file =
                Objects.requireNonNull(
                        SoundFileLoader.class.getClassLoader().getResourceAsStream(path));
        return AudioSystem.getAudioInputStream(new BufferedInputStream(file));
    }
}
