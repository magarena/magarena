package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.choice.MagicTargetChoice;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Brink_of_Disaster {
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
