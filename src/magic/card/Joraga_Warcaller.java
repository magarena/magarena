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
import magic.model.action.MagicPutItemOnStackAction;
import magic.model.choice.MagicKickerChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.stack.MagicTriggerOnStack;
import magic.model.target.MagicTargetFilter;

public class Joraga_Warcaller {
	public static final MagicStatic S = new MagicStatic(
	        MagicLayer.ModPT, 
		    MagicTargetFilter.TARGET_ELF_YOU_CONTROL) {
	    	
	    	private int amount = 0;
	    	
	    	@Override
			public void getPowerToughness(final MagicGame game,final MagicPermanent permanent,final MagicPowerToughness pt) {
					pt.add(amount, amount);
			}
	        @Override
	        public boolean condition(final MagicGame game,final MagicPermanent source,final MagicPermanent target) {
	        	if (source.hasCounters()) {
	        		amount = source.getCounters(MagicCounterType.PlusOne);
	        	}
	        	return source != target;
	        }
	    };
	            		
    private static final MagicEventAction KICKED = new MagicEventAction() {
        @Override
        public void executeEvent(
            final MagicGame game,
            final MagicEvent event,
            final Object[] data,
            final Object[] choiceResults) {
            	game.doAction(
            		new MagicChangeCountersAction(
            		(MagicPermanent)data[0],
            		MagicCounterType.PlusOne,
            		(Integer)data[1],
            		true));
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
                    new MagicKickerChoice(MagicManaCost.ONE_GREEN,true),
                    new Object[]{cardOnStack,player},
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
			final int kickerCount=(Integer)choiceResults[1];
			final MagicCardOnStack cardOnStack = (MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			if (kickerCount>0) {
				final MagicPermanent permanent = action.getPermanent();
				final MagicPlayer player = permanent.getController();
				final MagicEvent triggerEvent = new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,kickerCount},
                        KICKED,
                        "Put " + kickerCount + " +1/+1 counters on " + permanent + ".");
				game.doAction(new MagicPutItemOnStackAction(new MagicTriggerOnStack(permanent,triggerEvent)));
			}
		}
	};
}
