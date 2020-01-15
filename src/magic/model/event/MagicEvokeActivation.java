package magic.model.event;

import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

public class MagicEvokeActivation extends MagicHandCastActivation {

    final MagicManaCost cost;

    public MagicEvokeActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump, true),
            "Evoke"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            MagicPayManaCostEvent.Cast(
                source,
                cost
            ),
            new MagicEvokeEvent(source)
        );
    }
}
