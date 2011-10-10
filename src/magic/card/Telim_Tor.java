package magic.card;

import java.util.Collection;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicFlankingTrigger;
import magic.model.trigger.MagicWhenAttacksTrigger;

public class Telim_Tor {
    public static final MagicFlankingTrigger T1 = new MagicFlankingTrigger();
    
    public static final MagicWhenAttacksTrigger T2 = new MagicWhenAttacksTrigger() {
		@Override
		public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPermanent data) {
            final MagicPlayer player = permanent.getController();
			return (permanent == data) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    "Attacking creatures with flanking get +1/+1 until end of turn."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets = game.filterTargets(
                    (MagicPlayer)data[0],
                    MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,1,1));
			}
		}		
    };
}
