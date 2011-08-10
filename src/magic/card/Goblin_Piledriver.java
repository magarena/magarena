package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

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
