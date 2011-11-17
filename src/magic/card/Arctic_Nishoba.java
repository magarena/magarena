package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Arctic_Nishoba {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			if (permanent == data) {
				final MagicPlayer player = permanent.getController();
				final int amount = permanent.getCounters(MagicCounterType.Charge) * 2;
				if (amount > 0) {
				return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,amount},
                    this,
                    player + " gains " + amount + " life.");
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
			game.doAction(new MagicChangeLifeAction(
					(MagicPlayer)data[0],
					(Integer)data[1]));
		}
    };
}
