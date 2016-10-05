package magic.ui.widget.card.filter;

import java.util.EnumSet;
import java.util.Set;
import magic.translate.UiString;

public enum SearchOperand {

    MATCH_ANY(MyStrings._S1),
    MATCH_ALL(MyStrings._S2),
    EXCLUDE(MyStrings._S3);

    // Translatable strings.
    private class MyStrings {
        static final String _S1 = "Match any";
        static final String _S2 = "Match all";
        static final String _S3 = "Exclude";
    };

    private static final Set<SearchOperand> noAND = EnumSet.of(MATCH_ANY, EXCLUDE);

    private final String displayName;

    private SearchOperand(String text) {
        this.displayName = UiString.get(text);
    }

    @Override
    public String toString() {
        return displayName;
    }

    public static SearchOperand[] getValues(boolean hideAND) {
        return hideAND
                ? noAND.toArray(new SearchOperand[noAND.size()])
                : values();
    }
}
