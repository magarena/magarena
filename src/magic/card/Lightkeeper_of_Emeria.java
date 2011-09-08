package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;

public class Lightkeeper_of_Emeria {
                    
    private static final MagicEventAction KICKED = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],(Integer)data[1]));
        }
    };

	public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.WHITE,true),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + " enters the battlefield, " + 
                    player + " gains 2 life for each time it was kicked$.");
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
				final int life=kickerCount*2;
				final MagicEvent triggerEvent=new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,life},
                    KICKED,
                    player + " gains " + life + " life."
                );
                game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
