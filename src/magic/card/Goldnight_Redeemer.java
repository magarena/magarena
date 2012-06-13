package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenComesIntoPlayTrigger;


public class Goldnight_Redeemer {
	public static final MagicWhenComesIntoPlayTrigger T = new MagicWhenComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(
				final MagicGame game,
				final MagicPermanent permanent,
				final MagicPlayer player) {
			return new MagicEvent(
                    permanent,
                    player,
                    MagicEvent.NO_DATA,
                    this,
                    player + " gains 2 life for each " +
                    "other creature he or she controls.");
		}
                	
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player = event.getPlayer();
			final int x = player.controlsPermanent((MagicPermanent)event.getSource()) ? 1 : 0;
			final int amount = player.getNrOfPermanentsWithType(MagicType.Creature) - x;
			if (amount > 0) {
				game.doAction(new MagicChangeLifeAction(player,amount * 2));
			}
		}
    };
}
