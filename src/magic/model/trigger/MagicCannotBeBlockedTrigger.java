package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicCannotBeBlockedTrigger extends MagicTrigger<MagicPermanent> {
    public MagicCannotBeBlockedTrigger(final int priority) {
        super(priority);
    }

    public MagicCannotBeBlockedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.CannotBeBlocked;
    }
    
    public static MagicCannotBeBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCannotBeBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return filter.accept(permanent, permanent.getController(), other);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new RuntimeException(getClass() + " does not have an executeTrigger method");
            }
        };
    }
    
    public static MagicCannotBeBlockedTrigger createExcept(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCannotBeBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return filter.accept(permanent, permanent.getController(), other) == false;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new RuntimeException(getClass() + " does not have an executeTrigger method");
            }
        };
    }
}
