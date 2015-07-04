package magic.data;

import magic.ui.UiString;

public enum CardLegality {
    Legal(CardLegalityConstants._S1),
    Illegal(CardLegalityConstants._S2),
    Banned(CardLegalityConstants._S3),
    Restricted(CardLegalityConstants._S4),
    TooManyCopies(CardLegalityConstants._S5);
    
    private final String description;

    private CardLegality(String aString) {
        this.description = UiString.get(aString);
    }

    public String getDescription() {
        return description;
    }
}
