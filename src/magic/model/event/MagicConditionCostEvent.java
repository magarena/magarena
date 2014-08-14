package magic.model.event;

import magic.model.MagicSource;
import magic.model.MagicPermanent;
import magic.model.condition.MagicCondition;
import magic.model.condition.MagicConditionParser;

import java.util.LinkedList;
import java.util.List;

public class MagicConditionCostEvent implements MagicMatchedCostEvent {

    private final MagicCondition condition;

    public MagicConditionCostEvent(final String text) {
        final String processed = text
            .replaceFirst("^only ", "")
            .replaceFirst("^if ", "")
            .replaceFirst("\\.$", "");
        condition = MagicConditionParser.build(processed); 
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        if (condition == MagicCondition.ABILITY_ONCE_CONDITION) {
            return new MagicPlayAbilityEvent((MagicPermanent)source);
        } else {
            return new MagicConditionEvent(source, condition);
        }
    }

    @Override
    public boolean isIndependent() {
        return true;
    }
    
    public static List<MagicMatchedCostEvent> build(final String costs) {
        final List<MagicMatchedCostEvent> matched = new LinkedList<MagicMatchedCostEvent>();
        final String[] splitCosts = costs.split(" and ");
        for (String cost : splitCosts) {
            matched.add(new MagicConditionCostEvent(cost));
        }
        return matched;
    }
}
