package magic.card;

import magic.model.MagicAbility;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Ezuri_s_Archers {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenBlocks) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
            final MagicPermanent blocked=permanent.getBlockedCreature();
			return (permanent==data && 
                    blocked!=null &&
                    blocked.hasAbility(game,MagicAbility.Flying)) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent},
                        this,
                        permanent + " gets +3/+0 until end of turn."):
                null;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],3,0));
		}
    };
}
