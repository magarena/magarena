package magic.model.trigger;

import magic.model.MagicGame;
import magic.model.MagicPermanent;
import magic.model.action.MagicSacrificeAction;
import magic.model.event.MagicEvent;
import magic.model.event.MagicSourceEvent;
import magic.model.stack.MagicItemOnStack;
import magic.model.target.MagicTargetFilter;

public abstract class MagicWhenSelfTargetedTrigger extends MagicWhenTargetedTrigger {
    @Override
    public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
        return item.containsInChoiceResults(permanent);
    }
    
    public static MagicWhenSelfTargetedTrigger create(final MagicTargetFilter<MagicItemOnStack> filter, final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfTargetedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return super.accept(permanent, itemOnStack) && filter.accept(permanent, permanent.getController(), itemOnStack);
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static MagicWhenSelfTargetedTrigger create(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfTargetedTrigger() {
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static MagicWhenSelfTargetedTrigger createSpell(final MagicSourceEvent sourceEvent) {
        return new MagicWhenSelfTargetedTrigger() {
            @Override
            public boolean accept(final MagicPermanent permanent, final MagicItemOnStack item) {
                return super.accept(permanent, item) && item.isSpell();
            }
            @Override
            public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent, final MagicItemOnStack itemOnStack) {
                return sourceEvent.getEvent(permanent);
            }
        };
    }
    
    public static final MagicWhenSelfTargetedTrigger SacWhenTargeted = new MagicWhenSelfTargetedTrigger() {
        @Override
        public MagicEvent executeTrigger(final MagicGame game,final MagicPermanent permanent,final MagicItemOnStack target) {
            return new MagicEvent(
                permanent,
                this,
                "Sacrifice SN."
            );
        }

        @Override
        public void executeEvent(final MagicGame game, final MagicEvent event) {
            game.doAction(new MagicSacrificeAction(event.getPermanent()));
        }
    };
}
