package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenAttacksTrigger;


public class Mage_Slayer {
    public static final MagicWhenAttacksTrigger T =new MagicWhenAttacksTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent creature) {
			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            final MagicPlayer player=permanent.getController();
			return (equippedCreature != MagicPermanent.NONE && equippedCreature==creature) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{equippedCreature,game.getOpponent(player)},
                        this,
						equippedCreature+ " deals damage equal to its power to defending player."):
                MagicEvent.NONE;
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
