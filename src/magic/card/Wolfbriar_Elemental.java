package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.*;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;

public class Wolfbriar_Elemental {
	            		
    private static final MagicEventAction KICKED = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            final MagicPlayer player=(MagicPlayer)data[0];
            int count=(Integer)data[1];
            for (;count>0;count--) {
                game.doAction(new MagicPlayTokenAction(
                        player,
                        TokenCardDefinitions.
                        WOLF_TOKEN_CARD));
            }
        }
    };

	public static final MagicSpellCardEvent E =new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(null,MagicManaCost.GREEN,true),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + " enters the battlefield, " + 
                    "put a 2/2 green Wolf creature token onto the battlefield for each time it was kicked$.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction(cardOnStack,null);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent=action.getPermanent();
				final MagicPlayer player=permanent.getController();
				final MagicEvent triggerEvent=new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,kickerCount},
                        KICKED,
                        "Put "+kickerCount+" 2/2 green Wolf creature tokens onto the battlefield.");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
