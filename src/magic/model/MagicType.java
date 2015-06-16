package magic.model;

import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public enum MagicType {

    // these are supertypes
    Basic,
    Elite,
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

    public static final Set<MagicType> ALL_CARD_TYPES = EnumSet.range(Artifact, Vanguard);
    public static final Set<MagicType> SUPERTYPES = EnumSet.range(Basic, World);
    public static final Set<MagicType> FILTER_TYPES = EnumSet.of(
        Basic,
        Legendary,
        Snow,
        World,
        Artifact,
        Creature,
        Enchantment,
        Instant,
        Land,
        Planeswalker,
        Sorcery,
        Tribal
    );

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

    public static EnumSet<MagicType> prefixTypes(final List<String> tokens) {
        final EnumSet<MagicType> types = EnumSet.noneOf(MagicType.class);
        boolean matched = true;
        for (Iterator<String> iterator = tokens.iterator(); iterator.hasNext() && matched;) {
            final String name = iterator.next();
            matched = false;
            for (final MagicType type : values()) {
                if (type.toString().equalsIgnoreCase(name)) {
                    matched = true;
                    types.add(type);
                    iterator.remove();
                    break;
                }
            }
        }
        return types;
    }

    public static int getTypes(final String[] typeNames) {
        int givenTypeFlags = 0;
        for (final String typeName : typeNames) {
            givenTypeFlags |= getType(typeName).getMask();
        }
        return givenTypeFlags;
    }

    public boolean hasType(final int flags) {
        return (flags & getMask()) != 0;
    }
}
