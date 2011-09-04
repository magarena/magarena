package magic.model.action;

import magic.ai.ArtificialScoringSystem;
import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicPersistTrigger;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class MagicMoveCardAction extends MagicAction {

	private final MagicCard card;
	private final MagicPermanent permanent;
	private final MagicLocationType fromLocation;
	private final MagicLocationType toLocation;
		
	private MagicMoveCardAction(
            final MagicCard card,
            final MagicPermanent permanent,
            final MagicLocationType fromLocation,
            final MagicLocationType toLocation) {
		this.card=card;
		this.permanent=permanent;
		this.fromLocation=fromLocation;
		this.toLocation=toLocation;
	}
	
	public MagicMoveCardAction(final MagicCard card,final MagicLocationType fromLocation,final MagicLocationType toLocation) {
		this(card,MagicPermanent.NONE,fromLocation,toLocation);
	}
	
	public MagicMoveCardAction(final MagicPermanent permanent,final MagicLocationType toLocation) {
		this(permanent.getCard(),permanent,MagicLocationType.Play,toLocation);
	}
	
	public MagicMoveCardAction(final MagicCardOnStack cardOnStack) {
		this(cardOnStack.getCard(),MagicPermanent.NONE,MagicLocationType.Stack,cardOnStack.getMoveLocation());
	}

	@Override
	public void doAction(final MagicGame game) {

		// Move card.
		if (!card.isToken()) {
			final MagicPlayer owner=card.getOwner();
			switch (toLocation) {
				case TopOfOwnersLibrary:
					owner.getLibrary().addToTop(card);
					break;
				case BottomOfOwnersLibrary:
					owner.getLibrary().addToBottom(card);
					break;
				case OwnersLibrary:
					game.doAction(new MagicShuffleIntoLibraryAction(card));
					break;
				case OwnersHand:
					owner.addCardToHand(card);
					setScore(owner,ArtificialScoringSystem.getCardScore(card));
					break;
				case Graveyard:
					owner.getGraveyard().addToTop(card);
					break;
				case Exile:
					owner.getExile().addToTop(card);
					break;
			}
		}

		// Execute triggers.
		if (toLocation==MagicLocationType.Graveyard) {
			final MagicSource triggerSource=permanent!=MagicPermanent.NONE?permanent:card;
			for (final MagicTrigger trigger : card.getCardDefinition().getPutIntoGraveyardTriggers()) {
				
				game.executeTrigger(trigger,permanent,triggerSource,new MagicGraveyardTriggerData(card,fromLocation));
			}
			
			// Persist.
			if (permanent!=MagicPermanent.NONE&&permanent.hasAbility(game,MagicAbility.Persist)) {
				game.executeTrigger(MagicPersistTrigger.getInstance(),permanent,permanent,new MagicGraveyardTriggerData(card,fromLocation));
			} 	
			
			// Discard.
			if (fromLocation == MagicLocationType.OwnersHand) {
				game.executeTrigger(MagicTriggerType.WhenDiscarded,card);
			}
		}
		
		game.setStateCheckRequired();
	}

	@Override
	public void undoAction(final MagicGame game) {

		if (!card.isToken()) {
			final MagicPlayer owner=card.getOwner();
			switch (toLocation) {
				case TopOfOwnersLibrary:
				case BottomOfOwnersLibrary:
					owner.getLibrary().remove(card);
					break;
				case OwnersHand:
					owner.removeCardFromHand(card);
					break;
				case Graveyard:
					owner.getGraveyard().remove(card);
					break;
				case Exile:
					owner.getExile().remove(card);
					break;
			}			
		}
	}
}
