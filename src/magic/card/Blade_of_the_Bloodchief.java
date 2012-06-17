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
            final boolean isVampire = equippedCreature.hasSubType(MagicSubType.Vampire);
            return (equippedCreature != MagicPermanent.NONE && otherPermanent.isCreature()) ?
                new MagicEvent(
                    permanent,
                    permanent.getController(),
                    new Object[]{equippedCreature,isVampire ? 2:1},
                    this,
                    isVampire ?
                        "Put two +1/+1 counters on " + equippedCreature + "." :
                        "Put a +1/+1 counter on " + equippedCreature + ".") :
                MagicEvent.NONE;
        }
        @Override
        public void executeEvent(
                final MagicGame game,
                final MagicEvent event,
                final Object data[],
                final Object[] choiceResults) {
            game.doAction(new MagicChangeCountersAction(
                    (MagicPermanent)data[0],
                    MagicCounterType.PlusOne,
                    (Integer)data[1],
                    true));
        }
    };
}
