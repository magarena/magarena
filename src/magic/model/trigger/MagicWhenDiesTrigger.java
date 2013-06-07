package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;

public abstract class MagicWhenDiesTrigger extends MagicWhenPutIntoGraveyardTrigger {
    public MagicWhenDiesTrigger(final int priority) {
        super(priority); 
    }
    
    public MagicWhenDiesTrigger() {}

    protected abstract MagicEvent getEvent(final MagicPermanent permanent);
    
    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
        return (triggerData.fromLocation == MagicLocationType.Play) ?
            getEvent(permanent) : MagicEvent.NONE;
    }

    public static MagicWhenDiesTrigger ReturnToOwnersHand = new MagicWhenDiesTrigger() {
        @Override
        public MagicEvent getEvent(final MagicPermanent permanent) {
            final MagicCard card = permanent.getCard();
            return new MagicEvent(
                card,
                card.getController(),
                this,
                "Return " + card + " to its owner's hand."
            );
        }
        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            final MagicCard card = event.getCard();
            game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
            game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
        }
    };
}
