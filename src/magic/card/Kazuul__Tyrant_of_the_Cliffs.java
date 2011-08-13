package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayOgreUnlessEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Kazuul__Tyrant_of_the_Cliffs {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenAttacks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicPermanent creature=(MagicPermanent)data;
			return (creature.getController()!=player) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        "Put a 3/3 red Ogre creature token onto the battlefield unless your opponent pays {3}."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer player=(MagicPlayer)data[1];
			game.addEvent(new MagicPlayOgreUnlessEvent(
                        (MagicPermanent)data[0],
                        game.getOpponent(player),
                        player,
                        MagicManaCost.THREE));
		}
    };
}
