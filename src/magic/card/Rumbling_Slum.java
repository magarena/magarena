package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Rumbling_Slum {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=(MagicPlayer)data;
			return (permanent.getController()==player) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        permanent + " deals 1 damage to each player."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicSource source=(MagicSource)data[0];
			for (final MagicPlayer player : game.getPlayers()) {
				final MagicDamage damage=new MagicDamage(source,player,1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
		}
    };
}
