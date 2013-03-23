package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicSubType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.trigger.MagicWhenOtherPutIntoGraveyardFromPlayTrigger;

public class Blade_of_the_Bloodchief {
    public static final MagicWhenOtherPutIntoGraveyardFromPlayTrigger T = new MagicWhenOtherPutIntoGraveyardFromPlayTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicPermanent otherPermanent) {
            final MagicPermanent equippedCreature = permanent.getEquippedCreature();
            return (equippedCreature.isValid() && otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    this,
                    "Put a +1/+1 counter on equipped creature. " + 
                    "If equipped creature is a Vampire, put two +1/+1 counters on it instead."
                ):
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event) {
            final MagicPermanent equippedCreature = event.getPermanent().getEquippedCreature();
            final int amount = equippedCreature.hasSubType(MagicSubType.Vampire) ? 2 : 1;
            game.doAction(new MagicChangeCountersAction(
                equippedCreature,
                MagicCounterType.PlusOne,
                amount,
                true
            ));
        }
    };
}
