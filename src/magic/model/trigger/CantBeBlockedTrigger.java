package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.exception.GameException;

public abstract class CantBeBlockedTrigger extends MagicTrigger<MagicPermanent> {
    public CantBeBlockedTrigger(final int priority) {
        super(priority);
    }

    public CantBeBlockedTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.CannotBeBlocked;
    }

    public static final CantBeBlockedTrigger Skulk =
        new CantBeBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return other.getPower() > permanent.getPower();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new GameException(getClass() + " does not have an executeTrigger method", game);
            }
        };

    public static CantBeBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter) {
        return new CantBeBlockedTrigger() {
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

    public static CantBeBlockedTrigger createExcept(final MagicTargetFilter<MagicPermanent> filter) {
        return new CantBeBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent other) {
                return !filter.accept(permanent, permanent.getController(), other);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent other) {
                throw new GameException(getClass() + " does not have an executeTrigger method", game);
            }
        };
    }
}
