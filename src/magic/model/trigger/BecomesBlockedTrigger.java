package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPermanentState;
import magic.model.event.MagicSourceEvent;
import magic.model.event.MagicEvent;
import magic.model.target.MagicTargetFilter;
import magic.model.action.ChangeStateAction;

public abstract class BecomesBlockedTrigger extends BecomesStateTrigger {
    public BecomesBlockedTrigger(final int priority) {
        super(priority);
    }

    public BecomesBlockedTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final ChangeStateAction act) {
        return act.state == MagicPermanentState.Blocked && accept(permanent, act.permanent);
    }

    public boolean accept(final MagicPermanent permanent, final MagicPermanent blocker) {
        return true;
    }

    @Override
    public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final ChangeStateAction act) {
        return executeTrigger(game, permanent, act.permanent);
    }

    abstract public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked);

    public static final BecomesBlockedTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new BecomesBlockedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent blocked) {
                return filter.accept(permanent, permanent.getController(), blocked);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent blocked) {
                return sourceEvent.getTriggerEvent(permanent, blocked);
            }
        };
    }
}
