package magic.model.event;

import java.util.Arrays;
import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.condition.MagicCondition;

public class MagicSurgeActivation extends MagicHandCastActivation {

    final MagicManaCost cost;

    public MagicSurgeActivation(final MagicCardDefinition cdef, final MagicManaCost aCost) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
                MagicCondition.CAST_ANOTHER_SPELL_THIS_TURN
            },
            cdef.getActivationHints(),
            "Surge"
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
