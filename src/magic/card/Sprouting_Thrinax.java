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

public class Sprouting_Thrinax {
    public static final MagicWhenPutIntoGraveyardTrigger T = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = permanent.getController();
			return (MagicLocationType.Play==triggerData.fromLocation) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts three 1/1 green Saproling creature tokens onto the battlefield.") :
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			for (int count=3;count>0;count--) {
				game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.getInstance().getTokenDefinition("Saproling")));
			}
		}
    };
}
