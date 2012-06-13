package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPayedCost;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicCardAction;
import magic.model.action.MagicMoveCardAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.action.MagicRemoveCardAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicGraveyardTargetPicker;

public class Grave_Exchange {
	private static final MagicEventAction EVENT = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    if (opponent.controlsPermanentWithType(MagicType.Creature)) {
                        game.addEvent(new MagicSacrificePermanentEvent(
                            event.getSource(),
                            game.getOpponent(event.getPlayer()),
                            MagicTargetChoice.SACRIFICE_CREATURE));
                    }
                }
            });
        }
    };
    
	public static final MagicSpellCardEvent S =new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
				final MagicCardOnStack cardOnStack,
				final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
			return new MagicEvent(
                    cardOnStack.getCard(),
                    player,
                    MagicTargetChoice.TARGET_CREATURE_CARD_FROM_GRAVEYARD,
                    new MagicGraveyardTargetPicker(false),
                    new Object[]{cardOnStack,player},
                    this,
                    "Return target creature card$ from your graveyard to your hand.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicMoveCardAction((MagicCardOnStack)data[0]));
            event.processTargetCard(game,choiceResults,0,new MagicCardAction() {
                public void doAction(final MagicCard targetCard) {
                    game.doAction(new MagicRemoveCardAction(
                    		targetCard,
                    		MagicLocationType.Graveyard));
                    game.doAction(new MagicMoveCardAction(
                    		targetCard,
                    		MagicLocationType.Graveyard,
                    		MagicLocationType.OwnersHand));
                }
			});
            final MagicEvent triggerEvent = new MagicEvent(
					event.getSource(),
					event.getPlayer(),
					MagicTargetChoice.NEG_TARGET_PLAYER,
					MagicEvent.NO_DATA,
                    EVENT,
                    "Target player$ sacrifices a creature."
                );
				game.doAction(new MagicPutItemOnStackAction(
						new MagicTriggerOnStack(triggerEvent)));
		}
	};
}
