package magic.card;

import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicNoCombatTargetPicker;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Temporal_Isolation {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_CREATURE,
			// not the ideal targetPicker as the creature can still
			// attack and block, but because this gives the creature
			// shadow it can't block much and the aura prevents it
			// from doing any damage.
			new MagicNoCombatTargetPicker(true,true,true));
	
	public static final MagicIfDamageWouldBeDealtTrigger T = new MagicIfDamageWouldBeDealtTrigger(1) {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
			if (damage.getSource() == permanent.getEnchantedCreature()) {
				// Replacement effect. Generates no event or action.
				damage.setAmount(0);
			}
			return MagicEvent.NONE;
		}
		
		@Override
		public void executeEvent(final MagicGame game,final MagicEvent event,final Object data[],final Object[] choiceResults) {
			
		}
    };
}
