package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

public class MagicRetraceActivation extends MagicCardActivation {
    
    private static final MagicCondition[] CARD_GRAVEYARD_CONDITIONS = new MagicCondition[]{
        MagicCondition.CARD_CONDITION,  
        MagicCondition.GRAVEYARD_CONDITION
    };

    public MagicRetraceActivation(final MagicCardDefinition cdef) {
        super(
            CARD_GRAVEYARD_CONDITIONS,
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
