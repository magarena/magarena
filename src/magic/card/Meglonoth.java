package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDealDamageAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Meglonoth {

    public static final MagicTrigger V8021 =new MagicTrigger(MagicTriggerType.WhenBlocks,"Meglonoth") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {

			if (permanent==data) {
				final MagicPermanent blocked=permanent.getBlockedCreature();
				if (blocked!=null) {
					return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent,blocked.getController()},this,
						"Megnoloth deals damage to the blocked creature's controller equal to Megnoloth's power.");
				}
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final MagicDamage damage=new MagicDamage(permanent,(MagicTarget)data[1],permanent.getPower(game),false);
			game.doAction(new MagicDealDamageAction(damage));
		}
    };

}
