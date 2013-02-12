package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicSource;
import magic.model.MagicChangeCardDefinition;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.stack.MagicCardOnStack;

public class MagicCardActivation extends MagicActivation<MagicCard> implements MagicChangeCardDefinition {

    final boolean usesStack;
    
    public MagicCardActivation(final MagicCardDefinition cdef) {
        super(
            new MagicCondition[]{
                MagicCondition.CARD_CONDITION,
                cdef.getCost().getCondition()
            },
            cdef.getActivationHints(),
            "Cast"
        );
        usesStack = cdef.usesStack();
    }
    
    protected MagicCardActivation(
        final MagicCondition[] conditions,
        final MagicActivationHints hints,
        final String txt) {
        super(conditions, hints, txt);
        usesStack = true;
    }
  
    @Override
    boolean usesStack() {
        return usesStack;
    }

    public MagicEvent[] getCostEvent(final MagicCard source) {
        return source.getCostEvent();
    }

    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Play SN."
        );
    }

    @Override
    public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] choices) {
        final MagicCard card = event.getCard();
        if (card.getCardDefinition().isLand()) {
            game.incLandPlayed();
        }
        game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
        if (usesStack) {
            final MagicCardOnStack cardOnStack=new MagicCardOnStack(card,game.getPayedCost());
            game.doAction(new MagicPutItemOnStackAction(cardOnStack));
        } else {
            game.doAction(new MagicPlayCardAction(card,card.getController(),MagicPlayCardAction.NONE));
        }
    }

    @Override
    MagicTargetChoice getTargetChoice(final MagicCard source) {
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(source,MagicPayedCost.NO_COST);
        return cardOnStack.getEvent().getTargetChoice();
    }
    
    @Override
    public void change(final MagicCardDefinition cdef) {
        cdef.addCardAct(this);
    }
}
