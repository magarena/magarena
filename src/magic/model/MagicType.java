package magic.model;

public enum MagicType {

    // these are supertypes
    Basic,
    Legendary,
    Onging,
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
