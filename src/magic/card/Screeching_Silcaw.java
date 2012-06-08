package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.action.MagicMillLibraryAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Screeching_Silcaw {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicTarget target = damage.getTarget();
			final MagicPlayer player = permanent.getController();
			return (player.getNrOfPermanentsWithType(MagicType.Artifact) >= 3 &&
					permanent == damage.getSource() && 
                    target.isPlayer() && 
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{target},
                        this,
                        target + " puts the top four cards of " +
                        "his or her library into his or her graveyard."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicMillLibraryAction((MagicPlayer)data[0],4));
		}
    };
}
