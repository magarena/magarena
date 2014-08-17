package magic.data;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import magic.model.MagicGame;
import magic.utility.MagicFileSystem;
import magic.utility.MagicFileSystem.DataPath;

public class SoundEffects implements LineListener {

    public static final String WIN_SOUND="win.au";
    public static final String LOSE_SOUND="lose.au";
    public static final String TURN_SOUND="turn.au";
    public static final String RESOLVE_SOUND="resolve.au";
    public static final String COMBAT_SOUND="combat.au";

    private static final File SOUNDS_PATH = MagicFileSystem.getDataPath(DataPath.SOUNDS).toFile();
    private static final SoundEffects INSTANCE=new SoundEffects();

    private SoundEffects() {}

    public static void playClip(final String name) {
        if (GeneralConfig.getInstance().isSound()) {
            Clip clip = null;
            AudioInputStream ins = null;
            try { //load sound clip
                clip = AudioSystem.getClip();
                ins = AudioSystem.getAudioInputStream(new File(SOUNDS_PATH,name));
                clip.open(ins);
                clip.addLineListener(INSTANCE);
                clip.start();
            } catch (final LineUnavailableException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
            } catch (final UnsupportedAudioFileException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
            } catch (final IOException ex) {
                System.err.println("WARNING. Unable to load clip " + name);
            } finally {
                magic.data.FileIO.close(ins);
            }
        }
    }

    public static void playClip(final MagicGame game,final String name) {
        if (game.isSound()) {
            playClip(name);
        }
    }

    @Override
    public void update(final LineEvent event) {
        if (LineEvent.Type.STOP.equals(event.getType())) {
            event.getLine().close();
        }
    }
}
