
package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicCardDefinition;
import magic.model.stack.MagicCardOnStack;
import magic.model.action.MagicPlayMod;
import magic.model.action.PlayCardFromStackAction;
import magic.model.condition.MagicCondition;

import java.util.Arrays;

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

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            ),
            new MagicEvokeEvent(source)
        );
    }
}
