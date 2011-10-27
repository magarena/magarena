package magic.card;

import magic.model.MagicCard;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Goblin_Lackey {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player = permanent.getController();
			return (damage.getSource() == permanent &&
					damage.getTarget().isPlayer()) ?
				new MagicEvent(
						permanent,
						player,
						new MagicMayChoice(
							player + " may put a Goblin permanent card from " +
							"his or her hand onto the battlefield.",
							MagicTargetChoice.TARGET_GOBLIN_CARD_FROM_HAND),
						new MagicGraveyardTargetPicker(true),
						new Object[]{player},
						this,
						player + " may$ put a Goblin permanent card$ from " +
						"his or her hand onto the battlefield."):
				MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
			if (MagicMayChoice.isYesChoice(choiceResults[0])) {
				event.processTargetCard(game,choiceResults,1,new MagicCardAction() {
	                public void doAction(final MagicCard card) {
	                	game.doAction(new MagicRemoveCardAction(card,MagicLocationType.OwnersHand));
	    				game.doAction(new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE));
	                }
	            });
			}
		}
    };
}
