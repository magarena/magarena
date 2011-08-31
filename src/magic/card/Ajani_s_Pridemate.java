package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Ajani_s_Pridemate {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenLifeIsGained) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player = permanent.getController();
			if (player == (MagicPlayer)data) {
				return new MagicEvent(
						permanent,
						permanent.getController(),
						new Object[]{permanent},
						this,
						"Put a +1/+1 counter on " + permanent + ".");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeCountersAction((MagicPermanent)data[0],MagicCounterType.PlusOne,1,true));
		}
    };
}
