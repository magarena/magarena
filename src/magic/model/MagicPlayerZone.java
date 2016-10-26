package magic.model;

import magic.data.MagicIcon;
import magic.translate.MText;

public enum MagicPlayerZone {

    HAND(MagicPlayerZoneStrings._S1, MagicIcon.HAND_ZONE),
    LIBRARY(MagicPlayerZoneStrings._S2, MagicIcon.LIBRARY_ZONE),
    GRAVEYARD(MagicPlayerZoneStrings._S3, MagicIcon.GRAVEYARD_ZONE),
    EXILE(MagicPlayerZoneStrings._S4, MagicIcon.EXILE_ZONE);

    private final String zoneName;
    private final MagicIcon zoneIcon;

    private MagicPlayerZone(final String name, final MagicIcon icon) {
        this.zoneName = MText.get(name);
        this.zoneIcon = icon;
    }

    public String getName() {
        return this.zoneName;
    }

    public MagicIcon getIcon() {
        return this.zoneIcon;
    }

}

/**
 * translatable strings
 */
final class MagicPlayerZoneStrings {
    private MagicPlayerZoneStrings() {}
    static final String _S1 = "Hand";
    static final String _S2 = "Library";
    static final String _S3 = "Graveyard";
    static final String _S4 = "Exile";
}
