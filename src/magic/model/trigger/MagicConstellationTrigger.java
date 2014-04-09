package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.MagicType;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicConstellationTrigger extends MagicWhenOtherComesIntoPlayTrigger {
    public MagicConstellationTrigger(final int priority) {
        super(priority);
    }

    public MagicConstellationTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent played) {
        return played.isFriend(permanent) && played.hasType(MagicType.Enchantment);
    }
    
    public static final MagicConstellationTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicConstellationTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent played) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
