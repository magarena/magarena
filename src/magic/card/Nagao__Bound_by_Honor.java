package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicBecomesBlockedPumpTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;
import magic.model.trigger.MagicWhenBlocksPumpTrigger;

public class Nagao__Bound_by_Honor {
	private static final int amount = 1;
	
	public static final MagicBecomesBlockedPumpTrigger T1 = new MagicBecomesBlockedPumpTrigger(amount,amount,false);
	
	public static final MagicWhenBlocksPumpTrigger T2 = new MagicWhenBlocksPumpTrigger(amount,amount);
	
	public static final MagicWhenAttacksTrigger T3 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPlayer player = creature.getController();
			return (permanent == creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "Samurai creatures you control get +1/+1 until end of turn.") :
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets =
					game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_SAMURAI_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				final MagicPermanent creature = (MagicPermanent)target;
					game.doAction(new MagicChangeTurnPTAction(creature,1,1));
			}
		}
    };
}
