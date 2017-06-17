package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtUntapTrigger extends MagicTrigger<MagicPlayer> {
    public AtUntapTrigger(final int priority) {
        super(priority);
    }

    public AtUntapTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtUntap;
    }

    public static final AtUntapTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtUntapTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }

    public static final AtUntapTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new AtUntapTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getTriggerEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return permanent.isController(upkeepPlayer);
            }
        };
    }

}
