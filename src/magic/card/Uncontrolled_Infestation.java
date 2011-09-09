package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.event.MagicPlayAuraEvent;
import magic.model.event.MagicSpellCardEvent;
import magic.model.target.MagicDestroyTargetPicker;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;


public class Uncontrolled_Infestation {
	public static final MagicSpellCardEvent S = new MagicPlayAuraEvent(
			MagicTargetChoice.NEG_TARGET_NONBASIC_LAND,
			new MagicDestroyTargetPicker(false));

	public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
    	@Override
    	public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
    		final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
    		return (enchantedPermanent == data) ?
                new MagicEvent(
                        permanent,
                        permanent.getController(),
                        new Object[]{enchantedPermanent},
                        this,
                        "Destroy " + enchantedPermanent + ".") :
                MagicEvent.NONE;
    	}
    	@Override
    	public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
    		game.doAction(new MagicDestroyAction((MagicPermanent)data[0]));
    	}
    };
}
