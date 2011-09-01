package magic.card;

import magic.model.MagicGame;
import magic.model.MagicLocationType;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTarget;
import magic.model.target.MagicTargetFilter;
import magic.model.trigger.MagicGraveyardTriggerData;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

import java.util.Collection;

public class Havoc_Demon {
	public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenPutIntoGraveyard) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicGraveyardTriggerData triggerData) {
			return (MagicLocationType.Play==triggerData.fromLocation) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent.getController()},
                        this,
                        "All creatures get -5/-5 until end of turn."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final Collection<MagicTarget> targets=
                game.filterTargets((MagicPlayer)data[0],MagicTargetFilter.TARGET_CREATURE);
			for (final MagicTarget target : targets) {
				game.doAction(new MagicChangeTurnPTAction((MagicPermanent)target,-5,-5));
			}
		}
    };
}
