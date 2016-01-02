package magic.data;

import java.io.File;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
import magic.model.MagicGame;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class SoundEffects {

    public static final String WIN_SOUND="win.au";
    public static final String LOSE_SOUND="lose.au";
    public static final String TURN_SOUND="turn.au";
    public static final String RESOLVE_SOUND="resolve.au";
    public static final String COMBAT_SOUND="combat.au";

    private static final File SOUNDS_PATH = MagicFileSystem.getDataPath(DataPath.SOUNDS).toFile();

    private static volatile Clip clip;

    private static final LineListener closer = (event) -> {
        if (event.getType() == LineEvent.Type.STOP) {
            event.getLine().close();
        }
    };

    private SoundEffects() {}

    public static void playGameSound(final MagicGame game, final String name) {
        if (game.isReal() && GeneralConfig.getInstance().isSound()) {
            playSound(name);
        }
    }

    public static void playUISound(final String name) {
        if (GeneralConfig.getInstance().isUiSound()) {
            playSound(name);
        }
    }

    private static void playSound(final String name) {
        if (clip != null && (clip.isRunning() || clip.isActive())) {
            clip.stop();
        }
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH, name))) {
            clip = AudioSystem.getClip();
            clip.open(ins);
            clip.addLineListener(closer);
            clip.start();
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + name + ", " + ex.getMessage());
            // switch off sound for current session but restore on restart.
            GeneralConfig.getInstance().setIsUiSound(false);
            GeneralConfig.getInstance().setSound(false);
        }
    }

    public static void playSound(URL sound) {
        if (clip != null && (clip.isRunning() || clip.isActive())) {
            clip.loop(0);
        }
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(sound)) {
            clip = AudioSystem.getClip();
            clip.open(ins);
            clip.addLineListener(closer);
            clip.start();
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + sound.toExternalForm() + ", " + ex.getMessage());
            // switch off sound for current session but restore on restart.
            GeneralConfig.getInstance().setIsUiSound(false);
            GeneralConfig.getInstance().setSound(false);
        }
    }

}
