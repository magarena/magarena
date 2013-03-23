package magic.card;

import magic.model.MagicCounterType;
import magic.model.MagicDamage;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicChangeCountersAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicEventAction;
import magic.model.target.MagicTarget;
import magic.model.trigger.MagicIfDamageWouldBeDealtTrigger;

public class Vigor {
    public static final MagicIfDamageWouldBeDealtTrigger T1 = new MagicIfDamageWouldBeDealtTrigger(4) {
        @Override
        public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
            final MagicTarget target=damage.getTarget();
            final int amount=damage.getAmount();
            return 
                !damage.isUnpreventable() &&
                amount > 0 && 
                target != permanent && 
                target.isCreature() && 
                permanent.isFriend(target);
        }

        @Override
        public MagicEvent executeTrigger(
                final MagicGame game,
                final MagicPermanent permanent,
                final MagicDamage damage) {
            final MagicTarget target=damage.getTarget();
            final int amount=damage.getAmount();
        
            // Prevention effect.
            damage.setAmount(0);
            
            return new MagicEvent(
                permanent,
                target,
                new MagicEventAction() {
                    @Override
                    public void executeEvent(
                            final MagicGame game,
                            final MagicEvent event) {
                        game.doAction(new MagicChangeCountersAction(
                            event.getRefPermanent(),
                            MagicCounterType.PlusOne,
                            amount,
                            true
                        ));
                    }
                },
                "Put "+amount+" +1/+1 counters on "+target+"."
            );
        }
    };
}
