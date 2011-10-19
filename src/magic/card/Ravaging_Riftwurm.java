package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicFadeVanishCounterTrigger;

public class Ravaging_Riftwurm {
    public static final MagicFadeVanishCounterTrigger T = new MagicFadeVanishCounterTrigger("time");
    
    public static final MagicSpellCardEvent S = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.FOUR,false),
                    new Object[]{cardOnStack,player},
                    this,
                    "$Play " + card + ". If " + card + " was kicked$, " + 
                    "it enters the battlefield with three additional time counters on it.");
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final boolean kicked = ((Integer)choiceResults[1]) > 0;
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			final MagicPermanent permanent = action.getPermanent();
			game.doAction(new MagicChangeCountersAction(
					permanent,
					MagicCounterType.Charge,
					kicked ? 5 : 2,
					true));
		}
	};
}
