package magic.model;

import magic.data.MagicIcon;

public enum MagicPlayerZone {
    
    HAND("Hand", MagicIcon.HAND_ZONE),
    LIBRARY("Library", MagicIcon.LIBRARY_ZONE),
    GRAVEYARD("Graveyard", MagicIcon.GRAVEYARD_ZONE),
    EXILE("Exile", MagicIcon.EXILE_ZONE);
    
    private final String zoneName;
    private final MagicIcon zoneIcon;

    private MagicPlayerZone(final String name, final MagicIcon icon) {
        this.zoneName = name;
        this.zoneIcon = icon;
    }

    public String getName() {
        return this.zoneName;
    }

    public MagicIcon getIcon() {
        return this.zoneIcon;
    }

}
