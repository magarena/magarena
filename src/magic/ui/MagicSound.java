package magic.ui;

import kuusisto.tinysound.Sound;
import kuusisto.tinysound.TinySound;
import magic.data.GeneralConfig;
import magic.utility.MagicResources;

public enum MagicSound {

    ADD_CARD("cardSlide3.wav"),
    ALERT("bong.wav"),
    BEEP("noAction.wav"),
    REMOVE_CARD("cardTakeOutPackage1.wav")
    ;

    private static final GeneralConfig config = GeneralConfig.getInstance();

    static {
        TinySound.init();
    }

    private final String filename;
    private Sound sound;

    private MagicSound(final String aFilename) {
        this.filename = aFilename;
    }

    /**
     * plays sound at a volume of between 0 and 100%.
     */
    public void play(int volume) {
        if (config.isUiSound()) {
            if (sound == null) {
                sound = TinySound.loadSound(MagicResources.getSoundUrl(filename));
            }
            try {
                sound.play(volume / 100.0d);
            } catch (Exception ex) {
                config.setIsUiSound(false);
                config.save();
                throw new RuntimeException(ex);
            }
        }
    }

    public void play() {
        play(config.getUiSoundVolume());
    }

}
