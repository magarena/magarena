package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;

public abstract class HeroicTrigger extends MagicWhenSelfTargetedTrigger {

    public HeroicTrigger() {}

    @Override
    public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
        return super.accept(permanent, item) &&
               item.isFriend(permanent) &&
               item.isSpell();
    }

    public static HeroicTrigger create(final MagicSourceEvent sourceEvent) {
        return new HeroicTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack data) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
}
