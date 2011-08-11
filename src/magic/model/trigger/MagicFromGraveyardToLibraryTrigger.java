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

	public MagicFromGraveyardToLibraryTrigger(final String name) {
		super(MagicTriggerType.WhenPutIntoGraveyard,name);
	}

	@Override
	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

		final MagicGraveyardTriggerData triggerData=(MagicGraveyardTriggerData)data;
		final MagicCard card=triggerData.card;
		if (!card.isToken()) {
			return new MagicEvent(card,triggerData.card.getController(),new Object[]{card},this,
				"Shuffle "+card.getName()+" into its owners library.");
		}
		return null;
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
