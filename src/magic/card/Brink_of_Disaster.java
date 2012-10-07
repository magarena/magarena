package magic.card;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicDestroyAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenBecomesTappedTrigger;

public class Brink_of_Disaster {
    public static final MagicWhenBecomesTappedTrigger T = new MagicWhenBecomesTappedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent tapped) {
            final MagicPermanent enchantedPermanent = permanent.getEnchantedCreature();
            return (enchantedPermanent == tapped) ?
                new MagicEvent(
                    permanent,
                    enchantedPermanent,
                    this,
                    "Destroy RN."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object[] choiceResults) {
            game.doAction(new MagicDestroyAction(event.getRefPermanent()));
        }
    };
}
