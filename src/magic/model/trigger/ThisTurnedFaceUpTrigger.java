package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class ThisTurnedFaceUpTrigger extends TurnedFaceUpTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent faceUp) {
        return permanent == faceUp;
    }

    public static ThisTurnedFaceUpTrigger create(final MagicSourceEvent sourceEvent) {
        return new ThisTurnedFaceUpTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent faceUp) {
                return sourceEvent.getTriggerEvent(permanent);
            }
        };
    }
}
