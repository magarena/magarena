package magic.model.trigger;

import magic.model.MagicCounterType;
import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.ChangeCountersAction;
import magic.model.event.MagicEvent;

public abstract class AtBeginOfFirstMainPhaseTrigger extends MagicTrigger<MagicPlayer> {
    public AtBeginOfFirstMainPhaseTrigger(final int priority) {
        super(priority);
    }

    public AtBeginOfFirstMainPhaseTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtBeginOfFirstMainPhase;
    }

}
