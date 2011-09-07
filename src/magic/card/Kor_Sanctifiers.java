package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDestroyAction;
import magic.model.action.MagicPermanentAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicDestroyTargetPicker;

public class Kor_Sanctifiers {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.WHITE,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + " enters the battlefield, " +
                    "if it is was kicked$, destroy target artifact or enchantment.");
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
					MagicTargetChoice.NEG_TARGET_ARTIFACT_OR_ENCHANTMENT,
					new MagicDestroyTargetPicker(false),
					MagicEvent.NO_DATA,
                	new MagicEventAction() {
                        @Override
                        public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object[] data,
                            final Object[] choiceResults) {
                        	event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                                public void doAction(final MagicPermanent target) {
                                	game.doAction(new MagicDestroyAction(target));
                                }
                			});
		                }
	                },
	                "Destroy target artifact or enchantment$."
                );
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
