package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicHeroicTrigger extends MagicWhenSelfTargetedTrigger {

    public MagicHeroicTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
        return super.accept(permanent, item) &&
               item.isFriend(permanent) && 
               item.isSpell(); 
    }
    
    public static MagicHeroicTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicHeroicTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
