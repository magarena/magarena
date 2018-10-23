package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class YouSurveilTrigger extends MagicTrigger<MagicPlayer> {
    public YouSurveilTrigger(final int priority) {
        super(priority);
    }

    public YouSurveilTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenSurveil;
    }

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPlayer player) {
        return permanent.isFriend(player);
    }

    public static YouSurveilTrigger create(final MagicSourceEvent sourceEvent) {
        return new YouSurveilTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer player) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
