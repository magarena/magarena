package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicComesIntoPlayWithCounterTrigger;
import magic.model.trigger.MagicFadeVanishCounterTrigger;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Reality_Acid {
	public static final MagicComesIntoPlayWithCounterTrigger T1 = 
			new MagicComesIntoPlayWithCounterTrigger(MagicCounterType.Charge,"time",3);
	
    public static final MagicFadeVanishCounterTrigger T2 = new MagicFadeVanishCounterTrigger("time");
    
    public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_PERMANENT,
			new MagicDestroyTargetPicker(false));
    
    public static final MagicWhenLeavesPlayTrigger T3 = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
			return (permanent == data) ? 
					new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{enchantedPermanent},
                    this,
                    enchantedPermanent.getController() + " sacrifices " + enchantedPermanent + ".") :
            MagicEvent.NONE;
		}
		@Override
		public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
			final MagicPermanent permanent = (MagicPermanent)data[0];
			game.doAction(new MagicSacrificeAction(permanent));
		}
    };
}
