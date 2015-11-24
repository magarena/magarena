package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class SelfTurnedFaceUpTrigger extends TurnedFaceUpTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent faceUp) {
        return permanent == faceUp;
    }

    public static SelfTurnedFaceUpTrigger create(final MagicSourceEvent sourceEvent) {
        return new SelfTurnedFaceUpTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent faceUp) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
