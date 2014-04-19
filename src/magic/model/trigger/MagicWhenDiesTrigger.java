package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;

public abstract class MagicWhenDiesTrigger extends MagicWhenOtherDiesTrigger {
    public static final MagicWhenDiesTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenDiesTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent source, final MagicPermanent died) {
                return sourceEvent.getEvent(source);
            }
        };
    }
    
    public MagicWhenDiesTrigger(final int priority) {
        super(priority);
    }
    
    public MagicWhenDiesTrigger() {}
    
    @Override
    public boolean accept(final MagicPermanent source, final MagicPermanent died) {
        return source == died;
    }
}
