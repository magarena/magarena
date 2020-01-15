package magic.model.event;

import java.util.Arrays;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.choice.MagicTargetChoice;

public class MagicRetraceActivation extends MagicGraveyardCastActivation {

    public MagicRetraceActivation(final MagicCardDefinition cdef) {
        super(
            MagicHandCastActivation.CARD_CONDITION,
            cdef.getActivationHints(),
            "Retrace"
        );
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                source.getCost()
            ),
            new MagicDiscardChosenEvent(
                source,
                MagicTargetChoice.LAND_CARD_FROM_HAND
            )
        );
    }
}
