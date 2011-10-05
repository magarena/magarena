package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;

public class Unbreathing_Horde {
    public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
			final MagicTargetFilter targetFilter = new MagicTargetFilter.MagicOtherPermanentTargetFilter(
					MagicTargetFilter.TARGET_ZOMBIE_YOU_CONTROL,permanent);
			int amount = game.filterTargets(player,targetFilter).size();
			amount += game.filterTargets(player,MagicTargetFilter.TARGET_ZOMBIE_CARD_FROM_GRAVEYARD).size();
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{permanent,amount},
                    this,
					permanent + " enters the battlefield with " + 
                    amount + " +1/+1 counters on it.");
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
		@Override
		public boolean usesStack() {
			return false;
		}
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger T1 = new MagicIfDamageWouldBeDealtTrigger(4) {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
			if (!damage.isUnpreventable() && 
				damage.getAmount() > 0 && 
                damage.getTarget() == permanent &&
                permanent.getCounters(MagicCounterType.PlusOne) > 0) {
					// Prevention effect.
					damage.setAmount(0);
					return new MagicEvent(
                            permanent,
                            permanent.getController(),
                            new Object[]{permanent},
                            this,
                            "Remove a +1/+1 counter from " + permanent + ".");
			}
			return MagicEvent.NONE;
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
                        -1,
                        true));
		}
    };
}
