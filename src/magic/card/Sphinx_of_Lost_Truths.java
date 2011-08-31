package magic.card;

import magic.model.MagicCard;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sphinx_of_Lost_Truths {
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player=cardOnStack.getController();
            final MagicCard card=cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.ONE_BLUE,false),
				    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". When " + card + 
                    " enters the battlefield, draw three cards. Then if it wasn't kicked$, discard three cards.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicPlayCardFromStackAction action=new MagicPlayCardFromStackAction((MagicCardOnStack)data[0]);
			action.setKicked(((Integer)choiceResults[1])>0);
			game.doAction(action);
		}
	};
	
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final boolean kicked=permanent.hasState(MagicPermanentState.Kicked);
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,permanent,kicked},
                    this,
                    kicked ? player + " draws three cards." :
                    	player + " draws three cards. Then discards three cards.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicDrawAction(player,3));
			final boolean kicked=(Boolean)data[2];
			if (!kicked) {
				game.addEvent(new MagicDiscardEvent((MagicPermanent)data[1],player,3,false));
			}
		}		
    };
}
