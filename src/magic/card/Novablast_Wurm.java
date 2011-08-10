package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Novablast_Wurm {

    public static final MagicTrigger V8311 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Novablast Wurm") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,"Destroy all creatures other than Novablast Wurm.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			final MagicPermanent permanent=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(permanent.getController(),MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				
				if (target!=permanent) {
					game.doAction(new MagicDestroyAction((MagicPermanent)target));
				}
			}
		}
    };

}
