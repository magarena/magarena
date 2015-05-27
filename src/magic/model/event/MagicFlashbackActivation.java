package magic.model.event;

import magic.model.MagicCard;
import magic.model.MagicCardDefinition;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicSource;
import magic.model.action.PutItemOnStackAction;
import magic.model.action.RemoveCardAction;
import magic.model.stack.MagicCardOnStack;

import java.util.LinkedList;
import java.util.List;

public class MagicFlashbackActivation extends MagicGraveyardCastActivation {
    
    private final List<MagicMatchedCostEvent> matchedCostEvents;
    
    public MagicFlashbackActivation(final MagicCardDefinition cdef, final List<MagicMatchedCostEvent> aMatchedCostEvents) {
        super(
            MagicCardActivation.CARD_CONDITION,
            cdef.getActivationHints(),
            "Flashback"
        );
        matchedCostEvents = aMatchedCostEvents;
    }
   
    @Override
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
            this,
            "Flashback SN."
        );
    }
    
    @Override
    public void executeEvent(final MagicGame game, final MagicEvent event) {
        final MagicCard card = event.getCard();
        game.doAction(new RemoveCardAction(card, MagicLocationType.Graveyard)); 
        
        final MagicCardOnStack cardOnStack=new MagicCardOnStack(
            card,
            MagicFlashbackActivation.this,
            game.getPayedCost()
        );
        cardOnStack.setFromLocation(MagicLocationType.Graveyard);
        cardOnStack.setMoveLocation(MagicLocationType.Exile);
        game.doAction(new PutItemOnStackAction(cardOnStack));
    }
}
