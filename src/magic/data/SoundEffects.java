package magic.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.SwingUtilities;
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

    private static void setVolume(final Clip aClip, int volPercent) {

        BooleanControl muteControl = (BooleanControl) aClip.getControl(BooleanControl.Type.MUTE);
        muteControl.setValue(volPercent == 0);
        if (volPercent == 0)
            return;

        if (volPercent > 0 && volPercent <= 100) {
            FloatControl gainControl = (FloatControl) aClip.getControl(FloatControl.Type.MASTER_GAIN);
            double gain = volPercent / 100D; // number between 0 and 1 (loudest)
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
        } else {
            throw new IndexOutOfBoundsException("Valid volume range is 0 to 100 (percent).");
        }
    }

    private static void playClip(AudioInputStream ins, int volPercent) throws IOException, LineUnavailableException {

        if (clip != null && (clip.isRunning() || clip.isActive())) {
            clip.loop(0);
        }

        clip = AudioSystem.getClip();
        clip.addLineListener(closer);
        clip.open(ins);
        setVolume(clip, volPercent);
        clip.start();
    }

    private static void playSound(final String name) {
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH, name))) {
            playClip(ins, 100);
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + name + ", " + ex.getMessage());
            // switch off sound for current session but restore on restart.
            GeneralConfig.getInstance().setIsUiSound(false);
            GeneralConfig.getInstance().setSound(false);
        }
    }

    /**
     * Play sound file at a given volume.
     *
     * @param volPercent : volume of sound clip between 0 and 100 percent.
     */
    public static void playSound(URL url, int volPercent) {
        assert SwingUtilities.isEventDispatchThread() == false;
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(url)) {
            playClip(ins, volPercent);
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + url.toExternalForm() + ", " + ex.getMessage());
            // switch off sound for current session but restore on restart.
            GeneralConfig.getInstance().setIsUiSound(false);
            GeneralConfig.getInstance().setSound(false);
        }

    }

}
