package magic.ui;

import java.io.IOException;
import java.net.URL;
import java.util.EnumSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import magic.data.GeneralConfig;
import magic.data.settings.IntegerSetting;
import magic.utility.MagicResources;

public enum MagicSound {

    // uiSounds - keep sorted and verify enumset range if changed.
    ADD_CARD("cardSlide3.wav"),
    ALERT("bong.wav"),
    BEEP("noAction.wav"),
    BOOM("boom.wav"),
    REMOVE_CARD("cardTakeOutPackage1.wav"),
    CLICK("click.wav"),

    // gameSounds - keep sorted and verify enumset range if changed.
    COMBAT("combat.au"),
    LOSE("lose.au"),
    NEW_TURN("turn.au"),
    RESOLVE("resolve.au"),
    WIN("win.au");

    private static final Set<MagicSound> uiSounds = EnumSet.range(ADD_CARD, CLICK);
    private static final Set<MagicSound> gameSounds = EnumSet.range(COMBAT, WIN);

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static volatile Clip clip;
    private static final LineListener closer = (event) -> {
        if (event.getType() == LineEvent.Type.STOP) {
            event.getLine().close();
        }
    };

    private final String soundFile;

    private MagicSound(final String aFilename) {
        this.soundFile = aFilename;
    }

    private boolean isUISound() {
        return uiSounds.contains(this);
    }

    private boolean isGameSound() {
        return gameSounds.contains(this);
    }

    private boolean canPlay() {
        return getVolume() > 0;
    }

    private int getVolume() {
        if (isUISound()) {
            return GeneralConfig.get(IntegerSetting.UI_VOLUME);
        } else if (isGameSound()) {
            return GeneralConfig.get(IntegerSetting.GAME_VOLUME);
        } else {
            return 100;
        }
    }

    /**
     * Plays sound at given volume.
     *
     * @param volPercent : volume of sound clip between 0 and 100 percent.
     */
    public void play(int volPercent) {
        if (volPercent > 0 && volPercent <= 100) {
            executor.execute(() -> playSound(soundFile, volPercent));
        }
    }

    /**
     * Plays sound at volume specified in settings.
     */
    public void play() {
        if (canPlay()) {
            play(getVolume());
        }
    }

    public static void shutdown() {
        executor.shutdown();
    }

    private static void setVolume(final Clip aClip, int volPercent) {
        FloatControl gainControl = (FloatControl) aClip.getControl(FloatControl.Type.MASTER_GAIN);
        double gain = volPercent / 100D; // number between 0 and 1 (loudest)
        float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
        gainControl.setValue(dB);
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

    private static void playSound(final String soundFile, final int volPercent) {
        try (final AudioInputStream ins = AudioSystem.getAudioInputStream(MagicResources.getSoundStream(soundFile))) {
            playClip(ins, volPercent);
        } catch (Exception ex) {
            System.err.println("WARNING. Unable to play clip " + soundFile + ", " + ex.getMessage());
            // turn off all sound permanently.
            GeneralConfig.set(IntegerSetting.GAME_VOLUME, 0);
            GeneralConfig.set(IntegerSetting.UI_VOLUME, 0);
            GeneralConfig.saveToFile();
        }
    }

}
