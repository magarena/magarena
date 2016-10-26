package magic.data;

import magic.translate.MText;

public enum CardLegality {
    Legal(CardLegalityStrings._S1),
    Illegal(CardLegalityStrings._S2),
    Banned(CardLegalityStrings._S3),
    Restricted(CardLegalityStrings._S4),
    TooManyCopies(CardLegalityStrings._S5);

    private final String description;

    private CardLegality(String aString) {
        this.description = MText.get(aString);
    }

    public String getDescription() {
        return description;
    }
}

/**
 * translatable strings
 */
final class CardLegalityStrings {
    private CardLegalityStrings() {}
    static final String _S1 = "is legal";
    static final String _S2 = "is illegal";
    static final String _S3 = "is banned";
    static final String _S4 = "is restricted";
    static final String _S5 = "has too many copies";
}
