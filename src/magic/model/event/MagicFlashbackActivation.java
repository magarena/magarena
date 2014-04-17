package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicManaCost;
import magic.model.MagicSource;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.condition.MagicCondition;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.stack.MagicCardOnStack;

import java.util.Arrays;

public class MagicFlashbackActivation extends MagicGraveyardActivation {
    
    final MagicManaCost cost;
    
    public MagicFlashbackActivation(final MagicCardDefinition cdef, final MagicManaCost aCost) {
        super(
            MagicCardActivation.CARD_CONDITION,
            cdef.getActivationHints(),
            "Flashback"
        );
        cost = aCost;
    }
   
    @Override
    public Iterable<? extends MagicEvent> getCostEvent(final MagicCard source) {
        return Arrays.asList(
            new MagicPayManaCostEvent(
                source,
                cost
            )
        );
    }
    
    @Override
    public MagicEvent getEvent(final MagicSource source) {
        return new MagicEvent(
            source,
            this,
            "Flashback SN."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        game.doAction(new MagicRemoveCardAction(card, MagicLocationType.Graveyard)); 
        
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(
            card,
            MagicFlashbackActivation.this,
            game.getPayedCost()
        );
        cardOnStack.setMoveLocation(MagicLocationType.Exile);
        game.doAction(new MagicPutItemOnStackAction(cardOnStack));
    }
}
