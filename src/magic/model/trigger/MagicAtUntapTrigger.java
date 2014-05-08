package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtUntapTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtUntapTrigger(final int priority) {
        super(priority);
    }

    public MagicAtUntapTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtUntap;
    }
    
    public static final MagicAtUntapTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtUntapTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicAtUntapTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new MagicAtUntapTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getEvent(permanent);
            }
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return permanent.isController(upkeepPlayer);
            }
        };
    }

}
