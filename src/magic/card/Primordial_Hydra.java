package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicCard;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPayedCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.action.MagicPlayCardFromStackAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.stack.MagicCardOnStack;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Primordial_Hydra {
	public static final MagicStatic S = new MagicStatic(MagicLayer.Ability) {
		@Override
		public long getAbilityFlags(
                final MagicGame game,
                final MagicPermanent permanent,
                final long flags) {
			final int amount = permanent.getCounters(MagicCounterType.PlusOne);
			return amount >= 10 ?
					flags|MagicAbility.Trample.getMask() :
					flags;
		}
    };
    
	public static final MagicSpellCardEvent E = new MagicSpellCardEvent() {
		@Override
		public MagicEvent getEvent(
                final MagicCardOnStack cardOnStack,
                final MagicPayedCost payedCost) {
			final int amount = payedCost.getX();
            final MagicCard card = cardOnStack.getCard();
			return new MagicEvent(
                    card,
                    cardOnStack.getController(),
                    new Object[]{cardOnStack,amount},
                    this,
                    card + " enters the battlefield with " + amount + " +1/+1 counters on it.");
		}

		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			final MagicCardOnStack cardOnStack=(MagicCardOnStack)data[0];
			final MagicPlayCardFromStackAction action = new MagicPlayCardFromStackAction(cardOnStack);
			game.doAction(action);
			final MagicPermanent permanent = action.getPermanent();
			game.doAction(new MagicChangeCountersAction(
                        permanent,
                        MagicCounterType.PlusOne,
                        (Integer)data[1],
                        true));
		}
	};
	
	 public static final MagicAtUpkeepTrigger T2 = new MagicAtUpkeepTrigger() {
		 @Override
	    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
	    		final MagicPlayer player = permanent.getController();
	    		final int amount = permanent.getCounters(MagicCounterType.PlusOne);
	    		return (data == player) ?
	                new MagicEvent(
	                        permanent,
	                        player,
	                        new Object[]{permanent,amount},
	                        this,
	                        "Put " + amount + " +1/+1 counters on " + permanent + ".") :
	                MagicEvent.NONE;
	    	}
	    	@Override
	    	public void executeEvent(
	                final MagicGame game,
	                final MagicEvent event,
	                final Object data[],
	                final Object[] choiceResults) {
	    		game.doAction(new MagicChangeCountersAction(
	    				(MagicPermanent)data[0],
	    				MagicCounterType.PlusOne,
	    				(Integer)data[1],
	    				true));
	    	}
		};
}
