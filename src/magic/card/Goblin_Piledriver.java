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

public class Goblin_Piledriver {

    public static final MagicTrigger V7448 =new MagicTrigger(MagicTriggerType.WhenAttacks,"Goblin Piledriver") {

		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			
			if (permanent==data) {
				return new MagicEvent(permanent,permanent.getController(),new Object[]{permanent},this,
					"Goblin Piledriver gets +2/+0 until end of turn for each other attacking Goblin.");
			}
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {

			int power=0;
			final MagicPermanent creature=(MagicPermanent)data[0];
			final Collection<MagicTarget> targets=game.filterTargets(creature.getController(),MagicTargetFilter.TARGET_ATTACKING_CREATURE);
			for (final MagicTarget target : targets) {

				if (creature!=target) {
					final MagicPermanent attacker=(MagicPermanent)target;
					if (attacker.hasSubType(MagicSubType.Goblin)) {
						power+=2;
					}
				}
			}
			if (power>0) {
				game.doAction(new MagicChangeTurnPTAction(creature,power,0));
			}
		}
    };
    
}
