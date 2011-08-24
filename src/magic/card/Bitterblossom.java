package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Bitterblossom {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			if (player==data) {
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " loses 1 life and puts a 1/1 black Faerie Rogue creature token with flying onto the battlefield.");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[0];
			game.doAction(new MagicChangeLifeAction(player,-1));
			game.doAction(new MagicPlayTokenAction(player,TokenCardDefinitions.FAERIE_ROGUE_TOKEN_CARD));
		}		
    };
}
