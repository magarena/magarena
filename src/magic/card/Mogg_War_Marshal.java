package magic.card;

import magic.data.TokenCardDefinitions;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicPlayTokenAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;
import magic.model.trigger.MagicWhenPutIntoGraveyardTrigger;

public class Mogg_War_Marshal {
	public static final MagicWhenComesIntoPlayTrigger T2 = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 1/1 red Goblin creature token onto the battlefield.");
		}	
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(
					player,
					TokenCardDefinitions.get("Goblin1")));
		}		
    };
    
    public static final MagicWhenPutIntoGraveyardTrigger T3 = new MagicWhenPutIntoGraveyardTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			final MagicPlayer player = permanent.getController();
			return (MagicLocationType.Play == triggerData.fromLocation) ?
				new MagicEvent(
                    permanent,
                    player,
                    new Object[]{player},
                    this,
                    player + " puts a 1/1 red Goblin creature token onto the battlefield.") :
                MagicEvent.NONE;
		}	
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = (MagicPlayer)data[0];
			game.doAction(new MagicPlayTokenAction(
					player,
					TokenCardDefinitions.get("Goblin1")));
		}
    };
}
