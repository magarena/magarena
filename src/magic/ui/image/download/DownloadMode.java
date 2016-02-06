package magic.ui.image.download;

import magic.translate.UiString;

enum DownloadMode {

    CARDS(EnumStrings._S1),
    CROPS(EnumStrings._S2);

    private final String desc;

    private DownloadMode(String aDesc) {
        this.desc = UiString.get(aDesc);
    }

    @Override
    public String toString() {
        return desc;
    }
}
