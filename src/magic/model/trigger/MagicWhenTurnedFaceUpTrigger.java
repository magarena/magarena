package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenTurnedFaceUpTrigger extends MagicTrigger<MagicPermanent> {
    public MagicWhenTurnedFaceUpTrigger(final int priority) {
        super(priority);
    }

    public MagicWhenTurnedFaceUpTrigger() {}
    
    @Override
    public MagicTriggerType getType() {
        return MagicTriggerType.WhenTurnedFaceUp;
    }
    
    public static MagicWhenTurnedFaceUpTrigger create(final MagicTargetFilter<MagicPermanent> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenTurnedFaceUpTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicPermanent faceUp) {
                return filter.accept(permanent, permanent.getController(), faceUp);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent faceUp) {
                return sourceEvent.getEvent(permanent, faceUp);
            }
        };
    }
}
