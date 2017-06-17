package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class YouScryTrigger extends MagicTrigger<MagicPlayer> {
    public YouScryTrigger(final int priority) {
        super(priority);
    }

    public YouScryTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenScry;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
        return permanent.isFriend(player);
    }

    public static YouScryTrigger create(final MagicSourceEvent sourceEvent) {
        return new YouScryTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
