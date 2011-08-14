package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicTrigger;
import magic.model.trigger.MagicTriggerType;

public class Guardian_Seraph {
    public static final MagicTrigger T = new MagicTrigger(MagicTriggerType.IfDamageWouldBeDealt,5) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final Object data) {
			final MagicPlayer player=permanent.getController();
			final MagicDamage damage=(MagicDamage)data;
			final int amount=damage.getAmount();
			if (!damage.isUnpreventable()&&amount>0&&damage.getSource().getController()!=player&&damage.getTarget()==player) {
				// Prevention effect.
				damage.setAmount(amount-1);
			}			
			return null;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
		
		}
    };
}
