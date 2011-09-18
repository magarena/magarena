package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPlayerAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSacrificePermanentEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;

public class Gatekeeper_of_Malakir {
    private static final MagicEventAction KICKED = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            event.processTargetPlayer(game,choiceResults,0,new MagicPlayerAction() {
                public void doAction(final MagicPlayer opponent) {
                    if (opponent.controlsPermanentWithType(MagicType.Creature,game)) {
                        game.addEvent(new MagicSacrificePermanentEvent(
                            event.getSource(),
                            game.getOpponent(event.getPlayer()),
                            MagicTargetChoice.SACRIFICE_CREATURE));
                    }
                }
            });
        }
    };

    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.BLACK,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + " enters the battlefield, " +
                    "if it is was kicked$, target player sacrifices a creature.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final int kickerCount = (Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			if (kickerCount > 0) {
				final MagicPermanent permanent = action.getPermanent();
				final MagicPlayer player = permanent.getController();
				final MagicEvent triggerEvent = new MagicEvent(
					permanent,
					player,
					MagicTargetChoice.NEG_TARGET_PLAYER,
					MagicEvent.NO_DATA,
                    KICKED,
                    "Target player$ sacrifices a creature."
                );
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
