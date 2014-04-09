package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicSubType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAllyTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicAllyTrigger(final int priority) {
        super(priority);
    }

    public MagicAllyTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
        return played.isFriend(permanent) && played.hasSubType(MagicSubType.Ally);
    }
    
    public static final MagicAllyTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAllyTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
