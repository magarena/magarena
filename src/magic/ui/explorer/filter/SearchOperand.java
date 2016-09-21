package magic.ui.explorer.filter;

import java.util.EnumSet;
import java.util.Set;

public enum SearchOperand {

    MATCH_ANY("Match any"),
    MATCH_ALL("Match all"),
    EXCLUDE("Exclude");

    private static final Set<SearchOperand> noAND = EnumSet.of(MATCH_ANY, EXCLUDE);

    private final String displayName;

    private SearchOperand(String text) {
        this.displayName = text;
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
