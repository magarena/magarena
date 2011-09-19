package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayCardAction;
import magic.model.action.MagicReanimateAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Stitch_Together {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
			// estimated number of cards in the graveyard. this may change
			// before resolution but we need to make a choice here
			final boolean threshold = player.getGraveyard().size() >= 7;
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(threshold ?true:false),
                    new Object[]{cardOnStack,player},
                    this,
                    "Return target creature card$ from your graveyard to your hand. " +
                    "Return that card from your graveyard to the battlefield instead " +
                    "if seven or more cards are in your graveyard.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[1];
			final boolean threshold = player.getGraveyard().size() >= 7;
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {          	
                	if (threshold) {
                		game.doAction(new MagicReanimateAction(player,targetCard,MagicPlayCardAction.NONE));
                	} else {
                		game.doAction(new MagicRemoveCardAction(targetCard,MagicLocationType.Graveyard));
                		game.doAction(new MagicMoveCardAction(targetCard,MagicLocationType.Graveyard,MagicLocationType.OwnersHand));
                	}
                }
			});
		}
	};
}
