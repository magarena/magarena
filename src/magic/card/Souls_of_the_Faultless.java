package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeLifeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenDamageIsDealtTrigger;


public class Souls_of_the_Faultless {
    public static final MagicWhenDamageIsDealtTrigger T = new MagicWhenDamageIsDealtTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            final MagicPlayer player=permanent.getController();
            final MagicPlayer opponent=damage.getSource().getController();
            final int amount=damage.getDealtAmount();
			return (damage.getTarget()==permanent && 
                    damage.isCombat()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player,opponent,amount},
                        this,
                        player + " gains " + amount + " life and attacking player loses " +
                        		amount + " life.") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final int life=(Integer)data[2];
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[0],life));
			game.doAction(new MagicChangeLifeAction((MagicPlayer)data[1],-life));
		}
    };
}
