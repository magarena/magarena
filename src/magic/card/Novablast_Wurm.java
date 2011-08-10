package magic.card;
import java.util.*;
import magic.model.event.*;
import magic.model.stack.*;
import magic.model.choice.*;
import magic.model.target.*;
import magic.model.action.*;
import magic.model.trigger.*;
import magic.model.condition.*;
import magic.model.*;
import magic.data.*;
import magic.model.variable.*;

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
