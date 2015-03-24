package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicCantBlockTrigger extends MagicTrigger<MagicPermanent> {
    public MagicCantBlockTrigger(final int priority) {
        super(priority);
    }

    public MagicCantBlockTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.CantBlock;
    }
    
    public static MagicCantBlockTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new MagicCantBlockTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return filter.accept(permanent.getGame(), permanent.getController(), other);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new RuntimeException(getClass() + " does not have an executeTrigger method");
            }
        };
    }
}
