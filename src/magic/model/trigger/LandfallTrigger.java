package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class LandfallTrigger extends OtherEntersBattlefieldTrigger {
    public LandfallTrigger(final int priority) {
        super(priority);
    }

    public LandfallTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
        return played.isFriend(permanent) && played.isLand();
    }

    public static final LandfallTrigger create(final MagicSourceEvent sourceEvent) {
        return new LandfallTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
