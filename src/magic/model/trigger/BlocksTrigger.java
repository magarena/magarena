package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class BlocksTrigger extends MagicTrigger<MagicPermanent> {
    public BlocksTrigger(final int priority) {
        super(priority);
    }

    public BlocksTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenBlocks;
    }

    public static BlocksTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new BlocksTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
                return filter.accept(permanent, permanent.getController(), blocker);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocker) {
                return sourceEvent.getTriggerEvent(permanent, blocker);
            }
        };
    }
}
