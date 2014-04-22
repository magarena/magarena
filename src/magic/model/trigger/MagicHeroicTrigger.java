package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class MagicHeroicTrigger extends MagicWhenTargetedTrigger {

    public MagicHeroicTrigger(final int priority) {
        super(priority);
    }

    public MagicHeroicTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
        return item.isFriend(permanent) && 
               item.isSpell() && 
               item.containsInChoiceResults(permanent);
    }
    
    public static final MagicHeroicTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicHeroicTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
