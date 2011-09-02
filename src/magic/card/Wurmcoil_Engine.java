package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Wurmcoil_Engine {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = (permanent != null) ? permanent.getController() : null;
			return (MagicLocationType.Play==triggerData.fromLocation) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 3/3 colorless Wurm artifact creature token with deathtouch and "+
                            "a 3/3 colorless Wurm artifact creature token with lifelink onto the battlefield."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM1_TOKEN_CARD));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.WURM2_TOKEN_CARD));			
		}
    };
}
