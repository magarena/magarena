package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenSelfTurnedFaceUpTrigger extends TurnedFaceUpTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicPermanent faceUp) {
        return permanent == faceUp;
    }

    public static MagicWhenSelfTurnedFaceUpTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfTurnedFaceUpTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicPermanent faceUp) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
