package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Skullclamp {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.WhenOtherPutIntoGraveyardFromPlay) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
            MagicEvent e = null;
			if (permanent.getEquippedCreature()==data) {
				final MagicPlayer player=permanent.getController();
				e = new MagicEvent(
                        permanent,
                        player,
                        new Object[]{player},
                        this,
                        "You draw two cards.");
			}
            return e;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicDrawAction((MagicPlayer)data[0],2));
		}
    };
}
