package magic.model;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import magic.translate.MText;

public enum MagicType {

    // these are supertypes
    Basic(MagicTypeStr._SBasic),
    Legendary(MagicTypeStr._SLegendary),
    Ongoing(MagicTypeStr._SOngoing),
    Snow(MagicTypeStr._SSnow),
    World(MagicTypeStr._SWorld),

    // these are card types
    Artifact(MagicTypeStr._SArtifact),
    Conspiracy(MagicTypeStr._SConspiracy),
    Creature(MagicTypeStr._SCreature),
    Enchantment(MagicTypeStr._SEnchantment),
    Instant(MagicTypeStr._SInstant),
    Land(MagicTypeStr._SLand),
    Phenomenon(MagicTypeStr._SPhenomenon),
    Plane(MagicTypeStr._SPlane),
    Planeswalker(MagicTypeStr._SPlaneswalker),
    Scheme(MagicTypeStr._SScheme),
    Sorcery(MagicTypeStr._SSorcery),
    Tribal(MagicTypeStr._STribal),
    Vanguard(MagicTypeStr._SVanguard),
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
    public static final List<MagicType> TYPE_ORDER = Arrays.asList(
        // Tribal first
        Tribal,
        // Priority order for combinations of Type
        Enchantment,
        Artifact,
        Land,
        Creature,
        // All others in use
        Instant,
        Planeswalker,
        Sorcery
    );

    private final int mask;
    private final String displayName;

    private MagicType(final String aName) {
        this.displayName = MText.get(aName);
        mask = 1 << ordinal();
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
                final String plural = type.toString() + "s";
                if (type.toString().equalsIgnoreCase(name) ||
                    plural.equalsIgnoreCase(name)) {
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

    public String getDisplayName() {
        return displayName;
    }
}
