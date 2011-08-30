package magic.card;

import magic.model.*;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Glaze_Fiend {
	public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherComesIntoPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPermanent otherPermanent = (MagicPermanent)data;
			final MagicPlayer player = permanent.getController();
            return (otherPermanent != permanent &&
            		otherPermanent.isArtifact() &&
            		otherPermanent.getController() == player) ?
				new MagicEvent(
                        permanent,
                        player,
                        new Object[]{permanent},
                        this,
                        "Whenever another artifact enters the battlefield under your control, " +
                        permanent + " gets +2/+2 until end of turn") :
                null;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] data,
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}
    };
}
