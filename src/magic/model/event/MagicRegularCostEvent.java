package magic.model.event;

import magic.model.MagicSource;
import magic.model.ARG;

import java.util.regex.Matcher;
import java.util.LinkedList;
import java.util.List;

public class MagicRegularCostEvent implements MagicMatchedCostEvent {

    private static final String COMMA = "\\s*,\\s*";

    private final Matcher arg;
    private final MagicCostEvent costEvent;

    public MagicRegularCostEvent(final String aCost) {
        final String cost = capitalize(
            aCost.replaceAll("\\.$","")
                 .replaceAll("\\b(T|t)his " + ARG.THING + "( |\\.|'s|\\b)" + ARG.EVENQUOTES, "SN$3")
        );
        costEvent = MagicCostEvent.build(cost);
        arg = costEvent.matched(cost);
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return costEvent.toEvent(arg, source);
    }

    @Override
    public boolean isIndependent() {
        return costEvent.isIndependent();
    }

    public static List<MagicMatchedCostEvent> buildCast(final String costs) {
        final String costsWithMana = costs.matches(".*" + ARG.MANACOST + ".*") ? costs : costs + ", {0}";
        return build(costsWithMana.replaceFirst(ARG.MANACOST, "alt mana cost $1"));
    }

    public static List<MagicMatchedCostEvent> build(final String costs) {
        final List<MagicMatchedCostEvent> matched = new LinkedList<>();
        final String[] splitCosts = costs.split(COMMA);
        for (String cost : splitCosts) {
            matched.add(new MagicRegularCostEvent(cost));
        }
        return matched;
    }

    private static String capitalize(final String text) {
        return Character.toUpperCase(text.charAt(0)) + text.substring(1);
    }

    public static final MagicRegularCostEvent NONE = new MagicRegularCostEvent("{0}");
}
