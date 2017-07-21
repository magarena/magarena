package magic.model;

public enum MagicLocationType {
    Stack,
    Battlefield,
    OwnersHand("your hand"),
    OwnersLibrary,
    TopOfOwnersLibrary,
    BottomOfOwnersLibrary,
    Graveyard("your graveyard"),
    OpponentsGraveyard("an opponent's graveyard"),
    Exile,
    ;

    private final String suffix;

    private MagicLocationType() {
        this("NONE");
    }

    private MagicLocationType(final String aSuffix) {
        suffix = aSuffix;
    }

    public static MagicLocationType create(final String name) {
        MagicLocationType match = null;
        for (final MagicLocationType loc : values()) {
            if (name.endsWith(loc.suffix)) {
                match = loc;
            }
        }
        if (match == null) {
            throw new RuntimeException("unknown location type \"" + name + "\"");
        } else {
            return match;
        }
    }
}
