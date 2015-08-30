package magic.model;

public enum MagicRarity {
    Basic('B'),
    Common('C'),
    Uncommon('U'),
    Rare('R'),
    Mythic_Rare('M');

    public static final int length = values().length;

    private final char c;

    private MagicRarity(final char c) {
        this.c = c;
    }

    public char getChar() {
        return c;
    }

    public String getName() {
        return toString().replace('_',' ');
    }

    public static MagicRarity getRarity(final char c) {
        for (final MagicRarity type : values()) {
            if (type.c == c) {
                return type;
            }
        }
        throw new RuntimeException("unknown rarity \"" + c + "\"");
    }
}

