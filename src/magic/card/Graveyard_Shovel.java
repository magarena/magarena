package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.condition.MagicCondition;
import magic.model.event.MagicActivationHints;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicPayManaCostTapEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicTiming;
import magic.model.target.MagicGraveyardTargetPicker;

public class Graveyard_Shovel {
	public static final MagicPermanentActivation A = new MagicPermanentActivation(
			new MagicCondition[]{MagicCondition.CAN_TAP_CONDITION,MagicManaCost.TWO.getCondition()},
            new MagicActivationHints(MagicTiming.Main),
            "Exile") {
		@Override
		public MagicEvent[] getCostEvent(final MagicSource source) {
			return new MagicEvent[]{new MagicPayManaCostTapEvent(source,source.getController(),MagicManaCost.TWO)};
		}
		@Override
		public MagicEvent getPermanentEvent(final MagicPermanent source,final MagicPayedCost payedCost) {
			final MagicPlayer player = source.getController();
			return new MagicEvent(
                    source,
                    player,
                    MagicTargetChoice.TARGET_PLAYER,
                    new Object[]{player,source},
                    this,
                    "Target player$ exiles a card from his or her graveyard. " +
                    "If it's a creature card, you gain 2 life.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer targetPlayer) {
                	if (targetPlayer.getGraveyard().size() > 0) {
                		final MagicPlayer player = (MagicPlayer)data[0];
                		game.addEvent(new MagicEvent(
                				(MagicSource)data[1],
                				targetPlayer,
                                MagicTargetChoice.TARGET_CARD_FROM_GRAVEYARD,
                                new MagicGraveyardTargetPicker(true),
                                new Object[]{player},
                                EVENT_ACTION,
                                targetPlayer + " exiles a card$ from his or her graveyard."));
                	}
                }
			});
		}
		private final MagicEventAction EVENT_ACTION = new MagicEventAction() {
	        @Override
	        public void executeEvent(
	                final MagicGame game,
	                final MagicEvent event,
	                final Object[] data,
	                final Object[] choiceResults) {
	        	event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
	                public void doAction(final MagicCard card) {
	                    game.doAction(new MagicRemoveCardAction(card,MagicLocationType.Graveyard));
	                    game.doAction(new MagicMoveCardAction(card,MagicLocationType.Graveyard,MagicLocationType.Exile));
	                    if (card.getCardDefinition().isCreature()) {
	                    	game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],2));
	                    }
	                }
				});
	        }
	    };
	};
}
