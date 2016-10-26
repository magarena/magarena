package magic.ui.dialog.prefs;

import java.awt.Dimension;
import magic.data.GeneralConfig;
import magic.translate.MText;

public enum ImageSizePresets {

    SIZE_ORIGINAL(0, 0),
    SIZE_223x310(223, 310),
    SIZE_265x370(265, 370),
    SIZE_312x445(312, 445),     // magiccards.info
    SIZE_365x513(365, 513),     // cardbuilder with crop
    SIZE_480x680(480, 680),     // mtgimage.com
    SIZE_680x960(680, 960),
    SIZE_745x1040(745, 1040);

    private static final String _S1 =  "Original";

    private final Dimension size;

    private ImageSizePresets(int w, int h) {
        this.size = new Dimension(w, h);
    }

    @Override
    public String toString() {
        if (this == SIZE_ORIGINAL) {
            return MText.get(_S1);
        } else {
            return String.format("%d x %d", size.width, size.height);
        }
    }

    public Dimension getSize() {
        return size;
    }

    public static Dimension getDefaultSize() {
        ImageSizePresets preset = GeneralConfig.getInstance().getPreferredImageSize();
        if (preset == SIZE_ORIGINAL) {
            return SIZE_312x445.getSize();
        } else {
            return preset.getSize();
        }
    }
}
