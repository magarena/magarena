package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class AtDrawTrigger extends MagicTrigger<MagicPlayer> {
    public AtDrawTrigger(final int priority) {
        super(priority);
    }

    public AtDrawTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.AtDraw;
    }

    public static AtDrawTrigger create(final MagicSourceEvent sourceEvent) {
        return new AtDrawTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return sourceEvent.getTriggerEvent(permanent, drawPlayer);
            }
        };
    }

    public static AtDrawTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new AtDrawTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return permanent.isController(drawPlayer);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return sourceEvent.getTriggerEvent(permanent, drawPlayer);
            }
        };
    }

    public static AtDrawTrigger createOpp(final MagicSourceEvent sourceEvent) {
        return new AtDrawTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return permanent.isOpponent(drawPlayer);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return sourceEvent.getTriggerEvent(permanent, drawPlayer);
            }
        };
    }
}
