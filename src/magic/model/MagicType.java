package magic.model;

import java.util.EnumSet;

public enum MagicType {

    // these are supertypes
    Basic,
    Legendary,
    Ongoing,
    Snow,
    World,
    
    // these are card types
    Artifact,
    Creature,
    Enchantment,
    Instant,
    Land,
    Plane,
    Planeswalker,
    Scheme,
    Sorcery,
    Tribal,
    Vanguard,
    ;
    
    public static final EnumSet<MagicType> ALL_CARD_TYPES = EnumSet.range(Artifact, Vanguard);
    public static final EnumSet<MagicType> FILTER_TYPES = EnumSet.of(
            Legendary,
            Artifact,
            Creature,
            Enchantment,
            Instant,
            Land,
            Sorcery,
            Tribal);
    
    private final int mask;
    
    private MagicType() {
        mask=1<<ordinal();
    }
    
    public int getMask() {
        return mask;
    }
    
    public static MagicType getType(final String name) {
        for (final MagicType type : values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new RuntimeException("No corresponding MagicType for " + name);
    }
    
    public boolean hasType(final int flags) {
        return (flags & getMask()) != 0;
    }
}
