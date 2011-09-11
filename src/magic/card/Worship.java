package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLifeIsDamagedTrigger;

public class Worship {
    public static final MagicWhenLifeIsDamagedTrigger T = new MagicWhenLifeIsDamagedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
			final MagicPlayer player = permanent.getController();
			if (player == data && player.getNrOfPermanentsWithType(MagicType.Creature) > 0 && player.getLife() < 1) {
                player.setLife(1);
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
