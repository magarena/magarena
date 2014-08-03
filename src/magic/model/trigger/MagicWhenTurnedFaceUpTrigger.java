package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenTurnedFaceUpTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenTurnedFaceUpTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenTurnedFaceUpTrigger() {}
    
    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTurnedFaceUp;
    }
    
    public static final MagicWhenSelfTurnedFaceUpTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfTurnedFaceUpTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent untapped) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
