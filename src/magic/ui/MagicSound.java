package magic.ui;

import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import magic.data.GeneralConfig;
import magic.model.MagicGame;
import magic.utility.MagicResources;

public enum MagicSound {

    // UI
    ADD_CARD("cardSlide3.wav"),
    ALERT("bong.wav"),
    BEEP("noAction.wav"),
    REMOVE_CARD("cardTakeOutPackage1.wav"),

    // Game
    WIN_GAME("win.au"),
    LOSE_GAME("lose.au"),
    NEW_TURN("turn.au"),
    RESOLVE_ACTION("resolve.au"),
    COMBAT_DAMAGE("combat.au")
    ;

    private static final Set<MagicSound> uiSounds = EnumSet.range(ADD_CARD, REMOVE_CARD);
    private static final Set<MagicSound> gameSounds = EnumSet.range(WIN_GAME, COMBAT_DAMAGE);

    private static final GeneralConfig config = GeneralConfig.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static volatile Clip clip;
    private static final LineListener closer = (event) -> {
        if (event.getType() == LineEvent.Type.STOP) {
            event.getLine().close();
        }
    };

    private final URL soundUrl;

    private MagicSound(final String aFilename) {
        this.soundUrl = MagicResources.getSoundUrl(aFilename);
    }

    private boolean isUISound() {
        return uiSounds.contains(this);
    }

    private boolean isGameSound() {
        return gameSounds.contains(this);
    }

    private boolean canPlay() {
        return (isUISound() && config.isUiSound()) || (isGameSound() && config.isSound());
    }

    private int getVolume() {
        return isUISound() ? config.getUiSoundVolume() : 100;
    }

    /**
     * Plays sound effect at given volume.
     *
     * @param volPercent : volume of sound clip between 0 and 100 percent.
     */
    public void play(int volume) {
        executor.submit(() -> {
            playSound(soundUrl, volume);
        });
    }
    
    /**
     * Plays sound effect at volume specified in settings.
     */
    public void play() {
        if (canPlay()) {
            play(getVolume());
        }
    }

    /**
     * Convenience method that only plays sound effect if
     * game is not running as an AI simulation.
     */
    public void play(MagicGame game) {
        if (game.isReal()) {
            play();
        }
    }


    public static void shutdown() {
        executor.shutdown();
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

    private static void playSound(URL url, int volPercent) {
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(url)) {
            playClip(ins, volPercent);
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + url.toExternalForm() + ", " + ex.getMessage());
            // turn off all sound.
            config.setIsUiSound(false);
            config.setSound(false);
            config.save();
        }

    }

}
