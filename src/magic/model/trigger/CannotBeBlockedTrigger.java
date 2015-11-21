package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.exception.GameException;

public abstract class CannotBeBlockedTrigger extends MagicTrigger<MagicPermanent> {
    public CannotBeBlockedTrigger(final int priority) {
        super(priority);
    }

    public CannotBeBlockedTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.CannotBeBlocked;
    }

    public static CannotBeBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new CannotBeBlockedTrigger() {
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

    public static CannotBeBlockedTrigger createExcept(final MagicTargetFilter<MagicPermanent> filter) {
        return new CannotBeBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return filter.accept(permanent, permanent.getController(), other) == false;
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new GameException(getClass() + " does not have an executeTrigger method", game);
            }
        };
    }
}
