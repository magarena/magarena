package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.action.MagicTapAction;
import magic.model.choice.MagicMayChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.target.MagicGraveyardTargetPicker;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Elvish_Pioneer {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new MagicMayChoice(
							player + " may put a basic land card from his or her hand into play tapped.",
							MagicTargetChoice.TARGET_BASIC_LAND_CARD_FROM_HAND),
                    //MagicTargetChoice.TARGET_BASIC_LAND_CARD_FROM_HAND,
                    MagicGraveyardTargetPicker.getInstance(),
                    new Object[]{player},
                    this,
					player + " may$ put a basic land card$ from his or her hand into play tapped.");
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
						final MagicPlayCardAction action = 
								new MagicPlayCardAction(card,(MagicPlayer)data[0],MagicPlayCardAction.NONE);
						game.doAction(action);
						game.doAction(new MagicTapAction(action.getPermanent(),false));
					}
				});
			}
		}
    };
}
