package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicPowerToughness;
import magic.model.event.MagicEvent;
import magic.model.mstatic.MagicLayer;
import magic.model.mstatic.MagicStatic;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Hedron_Field_Purists {
	public static final MagicStatic S = new MagicStatic(MagicLayer.SetPT) {
		@Override
		public void getPowerToughness(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicPowerToughness pt) {
			final int charges = permanent.getCounters(MagicCounterType.Charge);
			if (charges >= 5) {
				pt.set(2,5);
			} else if (charges >= 1) {
				pt.set(1,4);
			}
		}
    };
    
    public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(5) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player = permanent.getController();
			final int amountDamage = damage.getAmount();
			final int amountCounters = permanent.getCounters(MagicCounterType.Charge);
			if (amountCounters > 0 &&
				!damage.isUnpreventable() &&
				amountDamage > 0) {
				if ((damage.getTarget().isPermanent() &&
					((MagicPermanent)damage.getTarget()).isCreature(game) &&
					damage.getTarget().getController() == player) ||
					damage.getTarget() == player) {
					final int amountPrevented = amountCounters >= 5 ? 2:1;
					// Prevention effect.
					damage.setAmount(amountDamage - amountPrevented);
				}
			}			
			return MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
				final MagicGame game,
				final MagicEvent event,
				final Object data[],
				final Object[] choiceResults) {
		}
    };
}
