package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicMorphCastActivation extends MagicCardActivation {

    final MagicManaCost cost;

    public MagicMorphCastActivation(final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump, true),
            "Morph"
        );
        cost = aCost;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicMorphEvent(source)
        );
    }
}
