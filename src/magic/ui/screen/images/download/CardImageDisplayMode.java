package magic.ui.screen.images.download;

import magic.translate.MText;

public enum CardImageDisplayMode {

    PRINTED(EnumStrings._S1),
    PROXY(EnumStrings._S2);

    private final String desc;

    private CardImageDisplayMode(String aDesc) {
        this.desc = aDesc;
    }

    @Override
    public String toString() {
        return MText.get(desc);
    }
}
