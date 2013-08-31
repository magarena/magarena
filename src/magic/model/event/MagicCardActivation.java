package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicChangeCardDefinition;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;

public class MagicCardActivation extends MagicActivation<MagicCard> implements MagicChangeCardDefinition, MagicCardEvent {

    final boolean usesStack;

    public MagicCardActivation(final MagicCardDefinition cdef) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
            },
            cdef.getActivationHints(),
            "Cast"
        );
        usesStack = cdef.usesStack();
    }

    protected MagicCardActivation(final MagicActivationHints hints, final String txt) {
        super(MagicActivation.NO_COND, hints, txt);
        usesStack = true;
    }

    protected MagicCardActivation(final MagicCondition[] conditions, final MagicActivationHints hints, final String txt) {
        super(conditions, hints, txt);
        usesStack = true;
    }

    @Override
    boolean usesStack() {
        return usesStack;
    }

    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return source.getCostEvent();
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            EVENT_ACTION,
            "Play SN."
        );
    }

    private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            if (card.getCardDefinition().isLand()) {
                game.incLandPlayed();
            }
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
            if (usesStack) {
                final MagicCardOnStack cardOnStack=new MagicCardOnStack(
                    card,
                    card.getController(),
                    MagicCardActivation.this,
                    game.getPayedCost()
                );
                game.doAction(new MagicPutItemOnStackAction(cardOnStack));
            } else {
                game.doAction(new MagicPlayCardAction(card,card.getController()));
            }
        }
    };

    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        throw new RuntimeException(getClass() + " did not override executeEvent");
    }

    @Override
    public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
        return cardOnStack.getCardDefinition().getCardEvent().getEvent(cardOnStack, payedCost);
    }

    @Override
    MagicChoice getChoice(final MagicCard source) {
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(source,this,MagicPayedCost.NO_COST);
        return cardOnStack.getEvent().getChoice();
    }

    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addCardAct(this);
    }
}
