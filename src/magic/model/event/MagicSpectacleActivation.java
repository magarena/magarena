package magic.model.event;

import java.util.Collections;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.condition.MagicCondition;

public class MagicSpectacleActivation extends MagicHandCastActivation {

    private final List<MagicMatchedCostEvent> matchedCostEvents;

    public MagicSpectacleActivation(final MagicCardDefinition cdef, final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
                MagicCondition.SPECTACLE
            },
            cdef.getActivationHints(),
            "Spectacle"
        );
        matchedCostEvents = aMatchedCostEvents;
    }

    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return MagicMatchedCostEvent.getCostEvent(matchedCostEvents, source);
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return new MagicEvent(
            cardOnStack,
            this,
            "Put SN onto the battlefield, " +
            "it gains haste and it's returned from the battlefield to its owner's hand at the beginning of the next end step."
        );
    }

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        game.doAction(new PlayCardFromStackAction(
            event.getCardOnStack(),
            MagicPlayMod.HASTE, MagicPlayMod.RETURN_AT_END_OF_TURN
        ));
    }
}
