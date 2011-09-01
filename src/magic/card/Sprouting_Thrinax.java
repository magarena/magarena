package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Sprouting_Thrinax {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = (permanent != null) ? permanent.getController() : null;
			return (MagicLocationType.Play==triggerData.fromLocation) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts three 1/1 green Saproling creature tokens onto the battlefield.") :
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.SAPROLING_TOKEN_CARD));
			}
		}
    };
}
