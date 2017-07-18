package magic.ui.screen.cardflow;

import magic.data.GeneralConfig;
import magic.data.settings.StringSetting;
import magic.ui.dialog.prefs.ImageSizePresets;

class ScreenSettings {

    private static final String DELIM = "¦";

    private ImageSizePresets sizePreset;
    private final boolean useOpaqueCardFlowImage;

    public ScreenSettings() {
        String settings = GeneralConfig.get(StringSetting.CARDFLOW_SETTINGS);
        sizePreset = getImageSizePreset(settings);
        useOpaqueCardFlowImage = getUseOpaqueImageFlag(settings);
    }

    private boolean getUseOpaqueImageFlag(String settings) {
        try {
            return Boolean.valueOf(settings.split(DELIM)[1]);
        } catch (ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    private ImageSizePresets getImageSizePreset(String settings) {
        try {
            return ImageSizePresets.valueOf(settings.split(DELIM)[0]);
        } catch (IllegalArgumentException ex) {
            return ImageSizePresets.SIZE_312x445;
        }
    }

    ImageSizePresets getImageSizePreset() {
        return sizePreset;
    }

    void setImageSizePreset(ImageSizePresets preset) {
        this.sizePreset = preset;
    }

    void save() {
        GeneralConfig.set(StringSetting.CARDFLOW_SETTINGS,
            sizePreset.name() + DELIM
            + useOpaqueCardFlowImage + DELIM
        );
    }

    boolean useOpaqueImage() {
        return useOpaqueCardFlowImage;
    }

}
