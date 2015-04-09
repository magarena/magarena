package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicLocationType;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicAbilityOnStack;

import java.util.List;
import java.util.LinkedList;

public class MagicRecoverSelfActivation extends MagicGraveyardActivation {

    private static final MagicActivationHints HINT = new MagicActivationHints(MagicTiming.Draw,true);
    final List<MagicMatchedCostEvent> matchedCostEvents;

    public MagicRecoverSelfActivation(final List<MagicMatchedCostEvent> aCosts) {
        super(
            MagicActivation.NO_COND,
            HINT,
            "Recover"
        );
        matchedCostEvents = aCosts;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        final List<MagicEvent> costEvents = new LinkedList<MagicEvent>();
        for (final MagicMatchedCostEvent matched : matchedCostEvents) {
            costEvents.add(matched.getEvent(source));
        }
        return costEvents;
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            new MagicEventAction() {
                @Override
                public void executeEvent(final MagicGame game, final MagicEvent event) {
                    final MagicAbilityOnStack abilityOnStack = new MagicAbilityOnStack(
                        MagicRecoverSelfActivation.this,
                        getCardEvent(event.getCard(), game.getPayedCost())
                    );
                    game.doAction(new MagicPutItemOnStackAction(abilityOnStack));
                }
            },
            "Recover SN."
        );
    }

    public MagicEvent getCardEvent(final MagicCard source,final MagicPayedCost payedCost) {
        return new MagicEvent(
            source,
            this,
            "Return SN from PN's graveyard to his or her hand."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        final MagicRemoveCardAction remove = new MagicRemoveCardAction(card,MagicLocationType.Graveyard);
        game.doAction(remove);
        if (remove.isValid()) {
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
        }
    }

    @Override
    final MagicChoice getChoice(final MagicCard source) {
        return getCardEvent(source,MagicPayedCost.NO_COST).getChoice();
    }
}
