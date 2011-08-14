package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Mage_Slayer {
    public static final MagicTrigger T =new MagicTrigger(MagicTriggerType.WhenAttacks,1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            final MagicPlayer player=permanent.getController();
			return (equippedCreature!=null&&equippedCreature==data) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{equippedCreature,game.getOpponent(player)},
                        this,
						equippedCreature+ " deals damage equal to its power to defending player."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
}
