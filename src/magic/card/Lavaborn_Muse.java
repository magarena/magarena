package magic.card;

import magic.model.*;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Lavaborn_Muse {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.AtUpkeep) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=(MagicPlayer)data;
			return (permanent.getController()!=player&&player.getHandSize()<3) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,player},
                        this,
                        permanent.getName() + " deals 3 damage to " + player.getName() + "."):
                null;
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
