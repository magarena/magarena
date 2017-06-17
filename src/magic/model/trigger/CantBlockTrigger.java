package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.exception.GameException;

public abstract class CantBlockTrigger extends MagicTrigger<MagicPermanent> {
    public CantBlockTrigger(final int priority) {
        super(priority);
    }

    public CantBlockTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.CantBlock;
    }

    public static CantBlockTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new CantBlockTrigger() {
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

    public static CantBlockTrigger create(final long id) {
        return new CantBlockTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return other.getId() == id;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new GameException(getClass() + " does not have an executeTrigger method", game);
            }
        };
    }
}
