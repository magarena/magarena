package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtYourUpkeepTrigger extends MagicAtUpkeepTrigger {
    public MagicAtYourUpkeepTrigger(final int priority) {
        super(priority);
    }

    public MagicAtYourUpkeepTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
        return permanent.isController(upkeepPlayer);
    }
    
    public static MagicAtYourUpkeepTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtYourUpkeepTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer upkeepPlayer) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
