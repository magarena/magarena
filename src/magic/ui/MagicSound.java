package magic.ui;

import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import magic.data.GeneralConfig;
import magic.data.SoundEffects;
import magic.model.MagicGame;
import magic.utility.MagicResources;

public enum MagicSound {

    ADD_CARD("cardSlide3.wav"),
    ALERT("bong.wav"),
    BEEP("noAction.wav"),
    REMOVE_CARD("cardTakeOutPackage1.wav"),

    WIN_GAME("win.au"),
    LOSE_GAME("lose.au"),
    NEW_TURN("turn.au"),
    RESOLVE_ACTION("resolve.au"),
    COMBAT_DAMAGE("combat.au")
    ;

    private static final GeneralConfig config = GeneralConfig.getInstance();
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    private final URL soundUrl;

    private MagicSound(final String aFilename) {
        this.soundUrl = MagicResources.getSoundUrl(aFilename);
    }

    /**
     * plays sound at a volume of between 0 and 100%.
     */
    public void play(int volume) {
        if (config.isUiSound()) {
            executor.submit(() -> {
                SoundEffects.playSound(soundUrl, volume);
            });
        }
    }

    public void play() {
        play(config.getUiSoundVolume());
    }

    public static void shutdown() {
        executor.shutdown();
    }

    public void play(final MagicGame game) {
        if (game.isReal() && config.isSound()) {
            MagicSound.this.play();
        }
    }

}
