package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicDiscardEvent;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Specter_s_Shroud {
    public static final MagicTrigger T =new MagicTrigger(MagicTriggerType.WhenDamageIsDealt) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicDamage damage=(MagicDamage)data;
			final MagicPermanent equippedCreature=permanent.getEquippedCreature();
            MagicEvent event = null;
			if (damage.getSource()==equippedCreature&&damage.getTarget().isPlayer()&&damage.isCombat()) {
				final MagicPlayer opponent=(MagicPlayer)damage.getTarget();
				event = new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,opponent},
                        this,
                        "Your opponent discards a card.");
			}
			return event;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			game.addEvent(new MagicDiscardEvent((MagicPermanent)data[0],(MagicPlayer)data[1],1,false));
		}
    };
}
