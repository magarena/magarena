package magic.model.trigger;

import magic.model.MagicCard;
import magic.model.MagicPermanent;
import magic.model.MagicGame;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenOtherDrawnTrigger extends MagicTrigger<MagicCard> {
    public MagicWhenOtherDrawnTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenOtherDrawnTrigger() {}

    public MagicTriggerType getType() {
        return MagicTriggerType.WhenOtherDrawn;
    }
    
    public static MagicWhenOtherDrawnTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDrawnTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getEvent(permanent, card.getOwner());
            }
        };
    }
    
    public static MagicWhenOtherDrawnTrigger createYou(final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDrawnTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCard card) {
                return card.isFriend(permanent);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getEvent(permanent, card.getOwner());
            }
        };
    }
    
    public static MagicWhenOtherDrawnTrigger createOpp(final MagicSourceEvent sourceEvent) {
        return new MagicWhenOtherDrawnTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicCard card) {
                return card.isEnemy(permanent);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicCard card) {
                return sourceEvent.getEvent(permanent, card.getOwner());
            }
        };
    }
}
