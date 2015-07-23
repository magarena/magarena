package magic.data;

import magic.ui.UiString;

public enum CardLegality {
    Legal(CardLegalityStrings._S1),
    Illegal(CardLegalityStrings._S2),
    Banned(CardLegalityStrings._S3),
    Restricted(CardLegalityStrings._S4),
    TooManyCopies(CardLegalityStrings._S5);
    
    private final String description;

    private CardLegality(String aString) {
        this.description = UiString.get(aString);
    }

    public String getDescription() {
        return description;
    }
}
