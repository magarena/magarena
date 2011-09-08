package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicChangeTurnPTAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherComesIntoPlayTrigger;

public class Adventuring_Gear {
    public static final MagicWhenOtherComesIntoPlayTrigger T = new MagicWhenOtherComesIntoPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent played) {
			final MagicPermanent equippedCreature = permanent.getEquippedCreature();
			final MagicPlayer player = permanent.getController();
			return (equippedCreature.isValid() &&
					player == played.getController() && played.isLand()) ?
                new MagicEvent(
                        permanent,
                        player,
                        new Object[]{equippedCreature},
                        this,
                        "Equipped creature gets +2/+2 until end of turn."):
                MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.doAction(new MagicChangeTurnPTAction((MagicPermanent)data[0],2,2));
		}	
    };
}
