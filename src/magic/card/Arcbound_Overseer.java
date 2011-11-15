package magic.card;

import java.util.Collection;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Arcbound_Overseer {
    public static final MagicAtUpkeepTrigger T3 = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			return (player == data) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts a +1/+1 counter on each creature " +
                        "with modular he or she controls."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			final Collection<MagicTarget> targets =
					game.filterTargets(player,MagicTargetFilter.TARGET_MODULAR_CREATURE_YOU_CONTROL);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeCountersAction(
						(MagicPermanent)target,
						MagicCounterType.PlusOne,
						1,
						true));
			}
		}
    };
}
