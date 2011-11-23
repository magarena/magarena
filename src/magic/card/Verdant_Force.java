package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicAtUpkeepTrigger;

public class Verdant_Force {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
    	@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer data) {
    		final MagicPlayer player = permanent.getController();
			return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        player + " puts a 1/1 green Saproling creature token onto the battlefield.");
		}	
    	@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
    		game.doAction(new MagicPlayTokenAction((MagicPlayer)data[0],TokenCardDefinitions.get("Saproling")));
		}
	};
}
