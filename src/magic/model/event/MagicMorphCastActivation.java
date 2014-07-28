package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicMorphCastActivation extends MagicCardActivation {

    public MagicMorphCastActivation() {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            new MagicActivationHints(MagicTiming.Pump, true),
            "Morph"
        );
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                MagicManaCost.create("{3}")
            ),
            new MagicMorphEvent(source)
        );
    }
}
