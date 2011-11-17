package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPlayer;
import magic.model.MagicPermanent;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Jotun_Owl_Keeper {
    public static final MagicWhenLeavesPlayTrigger T2 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			if (permanent == data) {
				final MagicPlayer player = permanent.getController();
				final int amount = permanent.getCounters(MagicCounterType.Charge);
				if (amount > 0) {
				return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player,amount},
                    this,
                    amount > 1 ?
                    	player + " puts " + amount + " 1/1 white Bird creature tokens with flying onto the battlefield." :
                    	player + " puts a 1/1 white Bird creature token with flying onto the battlefield.");
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
			int amount = (Integer)data[1];
			for (;amount>0;amount--) {
				game.doAction(new MagicPlayTokenAction(
						(MagicPlayer)data[0],
						TokenCardDefinitions.getInstance().getTokenDefinition("Bird1")));
			}
		}
    };
}
