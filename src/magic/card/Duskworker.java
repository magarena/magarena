package magic.card;

import magic.model.MagicGame;
import magic.model.MagicManaCost;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicRegenerateAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPermanentActivation;
import magic.model.event.MagicPumpActivation;
import magic.model.trigger.MagicWhenBecomesBlockedTrigger;

public class Duskworker {
    public static final MagicWhenBecomesBlockedTrigger T = new MagicWhenBecomesBlockedTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPlayer player = permanent.getController();
			return (permanent == data ) ?
		            new MagicEvent(
		                    permanent,
		                    player,
		                    new Object[]{permanent},
		                    this,
		                    "Regenerate " + permanent + "."):
		            MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicRegenerateAction((MagicPermanent)data[0]));
		}
    };
    
    public static final MagicPermanentActivation A = new MagicPumpActivation(MagicManaCost.THREE,1,0);
}
