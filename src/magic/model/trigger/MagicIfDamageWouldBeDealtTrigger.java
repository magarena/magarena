package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicDamage;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;

public abstract class MagicIfDamageWouldBeDealtTrigger extends MagicTrigger<MagicDamage> {
    public MagicIfDamageWouldBeDealtTrigger(final int priority) {
        super(priority);
    }

    public MagicIfDamageWouldBeDealtTrigger() {}
    
    public boolean accept(final MagicPermanent permanent, final MagicDamage damage) {
        return damage.getAmount() > 0;
    }

    public MagicTriggerType getType() {
        return MagicTriggerType.IfDamageWouldBeDealt;
    }
    
    public static final MagicIfDamageWouldBeDealtTrigger PreventDamageDealtToDealtBy = new MagicIfDamageWouldBeDealtTrigger(MagicTrigger.PREVENT_DAMAGE) {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicDamage damage) {
            if (damage.isCombat() && 
                (damage.getTarget() == permanent || damage.getSource() == permanent)) {
                // Replacement effect. Generates no event or action.
                damage.prevent();
            }
            return MagicEvent.NONE;
        }
    };
}
