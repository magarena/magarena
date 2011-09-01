package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.event.MagicEvent;

public class MagicFromGraveyardToLibraryTrigger extends MagicTrigger {
	
    public MagicFromGraveyardToLibraryTrigger() {
		super(MagicTriggerType.WhenPutIntoGraveyard);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
		final MagicCard card=triggerData.card;
		return (!card.isToken()) ?
			new MagicEvent(
                card,
                triggerData.card.getController(),
                new Object[]{card},
                this,
                "Shuffle "+card.getName()+" into its owners library."):
            null;
	}

	@Override
	public void executeEvent(final MagicGame game,final MagicEvent event,final Object[] data,final Object[] choiceResults) {
		final MagicCard card=(MagicCard)data[0];
		if (card.getOwner().getGraveyard().contains(card)) {
			game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
			game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.OwnersLibrary));
		}
	}
}
