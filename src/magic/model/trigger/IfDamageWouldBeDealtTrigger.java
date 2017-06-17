package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicDamage;
import magic.model.event.MagicEvent;

public abstract class IfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public IfDamageWouldBeDealtTrigger(final int priority) {
        super(priority);
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0;
    }

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.IfDamageWouldBeDealt;
    }

    public static IfDamageWouldBeDealtTrigger CantBePrevented = new IfDamageWouldBeDealtTrigger(MagicTrigger.CANT_BE_PREVENTED) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            damage.setUnpreventable();
            return MagicEvent.NONE;
        }
    };
}
