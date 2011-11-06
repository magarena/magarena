package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicRemoveFromPlayAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Dissipation_Field {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			final MagicPlayer player=permanent.getController();
			final MagicSource source=damage.getSource();
			return (damage.getTarget()==player&&source.isPermanent()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{source},
                        this,
                        "Return "+source+" to its owner's hand."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicRemoveFromPlayAction(
					(MagicPermanent)data[0],
					MagicLocationType.OwnersHand));
		}
    };
}
