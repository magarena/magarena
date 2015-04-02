package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.MagicPlayer;
import magic.model.action.MagicDrawAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicAtDrawTrigger extends MagicTrigger<MagicPlayer> {
    public MagicAtDrawTrigger(final int priority) {
        super(priority);
    }

    public MagicAtDrawTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.AtDraw;
    }
    
    public static MagicAtDrawTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicAtDrawTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return sourceEvent.getEvent(permanent, drawPlayer);
            }
        };
    }
    
    public static MagicAtDrawTrigger createYour(final MagicSourceEvent sourceEvent) {
        return new MagicAtDrawTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return permanent.isController(drawPlayer);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPlayer drawPlayer) {
                return sourceEvent.getEvent(permanent, drawPlayer);
            }
        };
    }
}
