package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenYouScryTrigger extends MagicTrigger<MagicPlayer> {
    public MagicWhenYouScryTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenYouScryTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenScry;
    }
    
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
        return permanent.isFriend(player);
    }
    
    public static MagicWhenYouScryTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenYouScryTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
