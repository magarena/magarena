package magic.ui.screen.images.download;

import magic.translate.MText;

public enum DownloadMode {

    CARDS(EnumStrings._S1),
    CROPS(EnumStrings._S2);

    private final String desc;

    private DownloadMode(String aDesc) {
        this.desc = MText.get(aDesc);
    }

    @Override
    public String toString() {
        return desc;
    }
}
