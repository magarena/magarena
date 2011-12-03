package magic.card;

import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.mstatic.MagicLayer;
import magic.model.MagicManaCost;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.target.MagicTargetFilter;

public class Joraga_Warcaller {
	public static final MagicStatic S = new MagicStatic(
			MagicLayer.ModPT, 
			MagicTargetFilter.TARGET_ELF_YOU_CONTROL) {

		private int amount = 0;

        @Override
        public void setSource(final MagicPermanent source) {
            amount = source.getCounters(MagicCounterType.PlusOne);
        }

		@Override
		public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
			pt.add(amount, amount);
		}
		@Override
		public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
			return source != target;
		}
	};
	            		
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(final MagicCardOnStack cardOnStack,final MagicPayedCost payedCost) {
			final MagicPlayer player = cardOnStack.getController();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    player,
                    new MagicKickerChoice(MagicManaCost.ONE_GREEN,true),
                    new Object[]{cardOnStack},
                    this,
                    "$Play " + card + ". " + card + " enters the battlefield " + 
                    "with a +1/+1 counter on it for each time it was kicked$");
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
				game.doAction(new MagicChangeCountersAction(
						permanent,
	            		MagicCounterType.PlusOne,
	            		kickerCount,
	            		true));
			}
		}
	};
}
