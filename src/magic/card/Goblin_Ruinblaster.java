package magic.card;

import magic.model.*;
import magic.model.action.MagicDestroyAction;
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
import magic.model.action.MagicPermanentAction;

public class Goblin_Ruinblaster {
	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.RED,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + " enters the battlefield, " +
                    "if it is was kicked$, destroy target nonbasic land.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(permanent,player,
					MagicTargetChoice.NEG_TARGET_NONBASIC_LAND,new MagicDestroyTargetPicker(false),
					MagicEvent.NO_DATA,
                	new MagicEventAction() {
                        @Override
                        public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event,
                            final Object[] data,
                            final Object[] choiceResults) {
                            event.processTargetPermanent(game,choiceResults,0,new MagicPermanentAction() {
                                public void doAction(final MagicPermanent land) {
                                    game.doAction(new MagicDestroyAction(land));
                                }
                            });
		                }
	                },
                    "Destroy target nonbasic land$."
                );
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
