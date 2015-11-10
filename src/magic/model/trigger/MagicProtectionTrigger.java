package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.exception.GameException;

public abstract class MagicProtectionTrigger extends MagicTrigger<MagicPermanent> {
    public MagicProtectionTrigger(final int priority) {
        super(priority);
    }

    public MagicProtectionTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.Protection;
    }
    
    public static MagicProtectionTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicProtectionTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return filter.accept(permanent, permanent.getController(), other);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new GameException(getClass() + " does not have an executeTrigger method", game);
            }
        };
    }
}
