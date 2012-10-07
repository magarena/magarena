package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenLeavesPlayTrigger;

public class Reality_Acid {
    public static final MagicWhenLeavesPlayTrigger T = new MagicWhenLeavesPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent left) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
            return (permanent == left && enchantedPermanent != MagicPermanent.NONE) ? 
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    enchantedPermanent.getController() + " sacrifices RN."
                ) :
            MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicSacrificeAction(event.getRefPermanent()));
        }
    };
}
