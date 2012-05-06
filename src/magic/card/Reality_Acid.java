package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Reality_Acid {
    public static final MagicWhenLeavesPlayTrigger T = new MagicWhenLeavesPlayTrigger() {
		@Override
		public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent data) {
			final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
			return (permanent == data &&
					enchantedPermanent != MagicPermanent.NONE) ? 
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
