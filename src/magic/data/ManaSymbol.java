package magic.data;

public enum ManaSymbol {

    TAPPED(50),
    WHITE(24),
    BLUE(25),
    BLACK(26),
    RED(27),
    GREEN(28),
    HYBRID_WHITE(40),
    HYBRID_BLUE(41),
    HYBRID_BLACK(42),
    HYBRID_RED(43),
    HYBRID_GREEN(44),
    PHYREXIAN_WHITE(45),
    PHYREXIAN_BLUE(46),
    PHYREXIAN_BLACK(47),
    PHYREXIAN_RED(48),
    PHYREXIAN_GREEN(49),
    WHITE_BLUE(30),
    WHITE_BLACK(31),
    BLUE_BLACK(32),
    BLUE_RED(33),
    BLACK_RED(34),
    BLACK_GREEN(35),
    RED_WHITE(36),
    RED_GREEN(37),
    GREEN_WHITE(38),
    GREEN_BLUE(39),
    ZERO(0),
    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5),
    SIX(6),
    SEVEN(7),
    EIGHT(8),
    NINE(9),
    TEN(10),
    ELEVEN(11),
    TWELVE(12),
    THIRTEEN(13),
    FOURTEEN(14),
    FIFTEEN(15),
    SIXTEEN(16),
    X(21);

    public static final String ICON_SHEET_FILENAME = "Mana.png";
    
    private final int iconIndex;

    private ManaSymbol(final int iconIndex) {
        this.iconIndex = iconIndex;
    }

    public int getIconIndex() {
        return iconIndex;
    }
    
}
