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
			final MagicDamage damage = (MagicDamage)data;
            final MagicPermanent equippedCreature=permanent.getEquippedCreature();
			return (damage.getSource() == equippedCreature && 
                    damage.getTarget().isPlayer() && 
                    damage.isCombat()) ?
				new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{permanent,damage.getTarget()},
                        this,
                        "Your opponent discards a card.") :
                null;
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
