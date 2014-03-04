package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.MagicCounterType;
import magic.model.action.MagicChangeCountersAction;
import magic.model.choice.MagicSimpleMayChoice;

public abstract class MagicLandfallTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicLandfallTrigger(final int priority) {
        super(priority);
    }

    public MagicLandfallTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
        return played.isFriend(permanent) && played.isLand();
    }
    
    public static final MagicLandfallTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicLandfallTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
