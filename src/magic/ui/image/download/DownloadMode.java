package magic.ui.image.download;

import magic.translate.UiString;

enum DownloadMode {

    MISSING(UiString.get(EnumStrings._S1)),
    ALL(UiString.get(EnumStrings._S2));

    private final String desc;

    private DownloadMode(String aDesc) {
        this.desc = aDesc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
