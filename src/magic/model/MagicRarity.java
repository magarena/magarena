package magic.model;

import java.util.EnumSet;
import magic.translate.UiString;

public enum MagicRarity {
    
    Basic(MagicRarityStr._SBasic, 'B'),
    Common(MagicRarityStr._SCommon, 'C'),
    Uncommon(MagicRarityStr._SUncommon, 'U'),
    Rare(MagicRarityStr._SRare, 'R'),
    Mythic_Rare(MagicRarityStr._SMythicRare, 'M');

    public static final int length = values().length;

    private final char c;
    private final String displayName;

    private MagicRarity(final String aName, final char c) {
        this.displayName = UiString.get(aName);
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public String getName() {
        return displayName;
    }

    public static MagicRarity getRarity(final char c) {
        for (final MagicRarity type : values()) {
            if (type.c == c) {
                return type;
            }
        }
        throw new RuntimeException("unknown rarity \"" + c + "\"");
    }

    public static String[] getDisplayNames() {
        return EnumSet.allOf(MagicRarity.class)
            .stream()
            .map(MagicRarity::getName)
            .toArray(String[]::new);
    }
    
}

