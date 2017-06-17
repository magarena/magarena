package magic.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum MagicManaType {

    Colorless("colorless","{C}"),
    White("white","{W}"),
    Blue("blue","{U}"),
    Black("black","{B}"),
    Red("red","{R}"),
    Green("green","{G}"),
    Snow("snow","{S}"),
    NONE("none","{N}")
    ;

    private static final List<MagicManaType> ALL_COLORS = Collections.unmodifiableList(
        Arrays.asList(White,Blue,Black,Red,Green)
    );

    // Colorless must be in front.
    private static final List<MagicManaType> ALL_TYPES = Collections.unmodifiableList(
        Arrays.asList(Colorless,White,Blue,Black,Red,Green,Snow)
    );

    public static final int NR_OF_TYPES = ALL_TYPES.size();

    private final String name;
    private final String text;

    private MagicManaType(final String name, final String text) {
        this.name=name;
        this.text=text;
    }

    public boolean isValid() {
        return this != MagicManaType.NONE;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public static MagicManaType get(final String name) {
        for (final MagicManaType type : values()) {
            if (type.toString().equalsIgnoreCase(name)) {
                return type;
            }
        }
        throw new RuntimeException("unknown mana type \"" + name + "\"");
    }

    public static List<MagicManaType> getList(final String name) {
        if ("one mana of any color".equals(name)) {
            return ALL_COLORS;
        } else {
            final String[] tokens = name.split("( or |, or |, )");
            final MagicManaType[] types = new MagicManaType[tokens.length];
            for (int i = 0; i < tokens.length; i++) {
                types[i] = get(tokens[i]);
            }
            return Arrays.asList(types);
        }
    }

    @Override
    public String toString() {
        return text;
    }

    public MagicColor getColor() {
        switch (this) {
            case Black: return MagicColor.Black;
            case Blue: return MagicColor.Blue;
            case Green: return MagicColor.Green;
            case Red: return MagicColor.Red;
            case White: return MagicColor.White;
            default: throw new RuntimeException("No color available for MagicManaType " + this);
        }
    }
}
