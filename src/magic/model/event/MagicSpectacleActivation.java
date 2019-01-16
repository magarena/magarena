package magic.model.event;

import java.util.Arrays;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

public class MagicSpectacleActivation extends MagicHandCastActivation {

    final MagicManaCost cost;

    public MagicSpectacleActivation(final MagicCardDefinition cdef, final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
                MagicCondition.SPECTACLE
            },
            cdef.getActivationHints(),
            "Spectacle"
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
