package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class OtherDrawnTrigger extends MagicTrigger<MagicCard> {
    public OtherDrawnTrigger(final int priority) {
        super(priority);
    }

    public OtherDrawnTrigger() {}

    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDrawn;
    }

    public static OtherDrawnTrigger create(final MagicSourceEvent sourceEvent) {
        return new OtherDrawnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getTriggerEvent(permanent, card.getOwner());
            }
        };
    }

    public static OtherDrawnTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new OtherDrawnTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCard card) {
                return card.isFriend(permanent);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getTriggerEvent(permanent, card.getOwner());
            }
        };
    }

    public static OtherDrawnTrigger createOpp(final MagicSourceEvent sourceEvent) {
        return new OtherDrawnTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCard card) {
                return card.isEnemy(permanent);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getTriggerEvent(permanent, card.getOwner());
            }
        };
    }
}
