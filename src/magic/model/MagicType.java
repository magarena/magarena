package magic.model;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Set;

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

    public static final Set<MagicType> ALL_CARD_TYPES = EnumSet.range(Artifact, Vanguard);
    public static final Set<MagicType> FILTER_TYPES = EnumSet.of(
        Legendary,
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
    
    //Takes array of strings and only returns valid MagicTypes   
    public static ArrayList<MagicType> convertTypes(final String[] typeNames) {
        final ArrayList<MagicType> types = new ArrayList<MagicType>();
        for (final String name:typeNames) {
            for (final MagicType type : values()) {
                if (type.toString().equalsIgnoreCase(name)) {
                    types.add(MagicType.getType(name));
                    System.out.println("Type: "+name);
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
