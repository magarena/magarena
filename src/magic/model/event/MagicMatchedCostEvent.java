package magic.model.event;

import magic.model.MagicSource;
import java.util.List;
import java.util.LinkedList;

public class MagicMatchedCostEvent {
    
    private static final String COMMA = "\\s*,\\s*";

    private final String cost;
    private final MagicCostEvent costEvent;

    public MagicMatchedCostEvent(final String aCost) {
        cost = capitalize(aCost.replaceAll("\\.$",""));
        costEvent = MagicCostEvent.build(cost);
    }

    public MagicEvent getEvent(final MagicSource source) {
        return costEvent.toEvent(cost, source);
    }

    public boolean isIndependent() {
        return costEvent.isIndependent();
    }

    public static List<MagicMatchedCostEvent> build(final String costs) {
        final List<MagicMatchedCostEvent> matched = new LinkedList<MagicMatchedCostEvent>();
        final String[] splitCosts = costs.split(COMMA);
        for (String cost : splitCosts) {
            matched.add(new MagicMatchedCostEvent(cost));
        }
        return matched;
    }
    
    private static String capitalize(final String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }
}
