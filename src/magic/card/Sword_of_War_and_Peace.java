package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicChangeLifeAction;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Sword_of_War_and_Peace {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			if (damage.getSource()==permanent.getEquippedCreature()&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer player=permanent.getController();
				final MagicTarget targetPlayer=damage.getTarget();
				return new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player,targetPlayer},
                        this,
                        permanent + " deals damage to " + targetPlayer + 
                        " equal to the number of cards in his or her hand and " +
                        player + " gains 1 life for each card in your hand.");
			}
			return null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPlayer targetPlayer=(MagicPlayer)data[2];
			final int amount1=targetPlayer.getHand().size();
			if (amount1>0) {
				final MagicDamage damage=new MagicDamage((MagicSource)data[0],targetPlayer,amount1,false);
				game.doAction(new MagicDealDamageAction(damage));
			}
			final MagicPlayer player=(MagicPlayer)data[1];
			final int amount2=player.getHand().size();
			if (amount2>0) {
				game.doAction(new MagicChangeLifeAction(player,amount2));
			}
		}
    };
}
