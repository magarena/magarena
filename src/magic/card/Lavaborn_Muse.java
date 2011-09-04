package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSource;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicAtUpkeepTrigger;


public class Lavaborn_Muse {
    public static final MagicAtUpkeepTrigger T = new MagicAtUpkeepTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPlayer player) {
			return (permanent.getController()!=player&&player.getHandSize()<3) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent,player},
                        this,
                        permanent + " deals 3 damage to " + player + "."):
                MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicDamage damage=new MagicDamage((MagicSource)data[0],(MagicTarget)data[1],3,false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };
}
